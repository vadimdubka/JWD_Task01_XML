package by.tc.task02.dao.impl;

import by.tc.task02.dao.DomDAO;
import by.tc.task02.entity.Element;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DomDAOImpl implements DomDAO {
    
    private static final String TAG = "(</?)([^\\s<>?]+)([^<>]*)(/?>)";
    private static final String EMPTY_TAG = "<\\w[^<>]*/>";
    private static final String CLOSE_TAG = "</(\\w[^<>]*)>";
    private static final String NAME = "(</?)([^\\s/]+)(\\s*[^<>]*)>";
    private static final String ATTRIBUTE = "(\\s*)([^\\s]+)=\"([^\\s]+)\"";
    private static final Pattern TAG_PATTERN = Pattern.compile(TAG);
    private static final Pattern EMPTY_TAG_PATTERN = Pattern.compile(EMPTY_TAG);
    private static final Pattern CLOSE_TAG_PATTERN = Pattern.compile(CLOSE_TAG);
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME);
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile(ATTRIBUTE);
    
    private final Deque<Element> openedElements = new ArrayDeque<>();
    private final Deque<Element> closedElements = new ArrayDeque<>();
    
    private final Matcher closeTagMatcher = CLOSE_TAG_PATTERN.matcher("");
    private final Matcher emptyTagMatcher = EMPTY_TAG_PATTERN.matcher("");
    private final Matcher nameMatcher = NAME_PATTERN.matcher("");
    
    private String document;
    private int level = 0;
    
    @Override
    public Element getDOM(String documentAddress) {
        document = readDocument(documentAddress);
        
        parseXML();
        
        Element dom = closedElements.pollLast();
        
        return dom;
    }
    
    
    /**
     * Method reads xml document into memory.
     */
    private String readDocument(String fileAddress) {
        URL resourceURL = DomDAOImpl.class.getResource(fileAddress);
        BufferedReader reader = null;
        
        StringBuilder wholeFile = new StringBuilder();
        
        try {
            reader = new BufferedReader(new InputStreamReader(resourceURL.openStream()));
            
            while (reader.ready()) {
                String line = reader.readLine();
                wholeFile.append(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Database is not available.");
        } catch (IOException e) {
            System.err.println(" I/O exception of some sort has occurred.");
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println("Can't close reader.");
            }
        }
        return wholeFile.toString();
    }
    
    /**
     * Метод находит в документе теги и отправляет данные о тегах на обработку.
     */
    private void parseXML() {
        Matcher tagMatcher = TAG_PATTERN.matcher(document);
        
        int endOfPreviousTag = 0;
        while (tagMatcher.find(endOfPreviousTag)) {
            int startOfCurrentTag = tagMatcher.start();
            
            if (endOfPreviousTag < startOfCurrentTag) {
                tagDataAnalyzer(endOfPreviousTag, startOfCurrentTag);
            }
            
            endOfPreviousTag = tagMatcher.end();
            
            String tag = tagMatcher.group();
            tagHandlersDirector(tag);
        }
    }
    
    
    /**
     * Анализирует тег и проводит его обработку в зависимости от типа тега.
     */
    private void tagHandlersDirector(String tag) {
        closeTagMatcher.reset(tag);
        
        if (closeTagMatcher.matches()) {
            closeTagHandler(tag);
        } else {
            openTagHandler(tag);
        }
    }
    
    private void openTagHandler(String tag) {
        emptyTagMatcher.reset(tag);
        ++level;
        
        Element element = new Element();
        element.setLevel(level);
        elementNameAnalyzer(element, tag);
        elementAttributesAnalyzer(element, tag);
        
        if (emptyTagMatcher.matches()) {
            closedElements.addLast(element);
            --level;
        } else {
            openedElements.addLast(element);
        }
    }
    
    private void closeTagHandler(String tag) {
        --level;
        
        Element currentElem = openedElements.pollLast();
        
        if (!closedElements.isEmpty()) {
            Element previousElem = closedElements.peekLast();
            
            int currentElemLevel = currentElem.getLevel();
            int previousElemLevel = previousElem.getLevel();
            
            if (currentElemLevel < previousElemLevel) {
                removeClosedElemsIntoCurrentElem(currentElem);
            }
        }
        
        closedElements.addLast(currentElem);
    }
    
    //TODO дать имена методам
    private void removeClosedElemsIntoCurrentElem(Element currentElem) {
        List<Element> childrenElements = new ArrayList<>();
        int currentLevel = currentElem.getLevel();
        
        Iterator<Element> iterator = closedElements.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            
            if (currentLevel < element.getLevel()) {
                childrenElements.add(element);
                iterator.remove();
            }
        }
        
        currentElem.setChildrenElements(childrenElements);
    }
    
    /**
     * Анализирует имя элемента из описания тега
     */
    private void elementNameAnalyzer(Element element, String tag) {
        nameMatcher.reset(tag);
        if (nameMatcher.find()) {
            String name = nameMatcher.group(2);
            element.setName(name);
        }
    }
    
    /**
     * Анализирует атрибуты элемента из описания тега
     */
    private void elementAttributesAnalyzer(Element element, String tag) {
        Matcher attributeMatcher = ATTRIBUTE_PATTERN.matcher(tag);
        
        Map<String, String> attributes = new HashMap<>();
        while (attributeMatcher.find()) {
            String attrName = attributeMatcher.group(2);
            String attrValue = attributeMatcher.group(3);
            attributes.put(attrName, attrValue);
        }
        
        element.setAttributes(attributes);
        
    }
    
    /**
     * Метод анализирует данные между тегами и добавляет эти данные в последний открытый элемент.
     */
    private void tagDataAnalyzer(int endOfPreviousTag, int startOfCurrentTag) {
        String tagData = document.substring(endOfPreviousTag, startOfCurrentTag);
        
        Element element = openedElements.peekLast();
        if (element != null) {
            element.setData(tagData);
        }
    }
}
