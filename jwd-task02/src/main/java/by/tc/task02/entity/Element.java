package by.tc.task02.entity;

import java.util.List;
import java.util.Map;

public class Element {
    
    private int level;
    private String name;
    private Map<String, String> attributes;
    private StringBuilder data = new StringBuilder();
    private List<Element> childrenElements;
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Map<String, String> getAttributes() {
        return attributes;
    }
    
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
    
    public StringBuilder getData() {
        return data;
    }
    
    public void setData(String data) {
        String data2 = data.trim();
        
        this.data = this.data.append(data2);
    }
    
    public List<Element> getChildrenElements() {
        return childrenElements;
    }
    
    public void setChildrenElements(List<Element> childrenElements) {
        this.childrenElements = childrenElements;
    }
    
    //TODO порядок размещения полей и методов в классе
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Element)) return false;
        
        Element element = (Element) o;
        
        if (level != element.level) return false;
        if (name != null ? !name.equals(element.name) : element.name != null) return false;
        if (attributes != null ? !attributes.equals(element.attributes) : element.attributes != null) return false;
        if (!data.equals(element.data)) return false;
        return childrenElements != null ? childrenElements.equals(element.childrenElements) : element.childrenElements == null;
    
    }
    
    //TODO можно ли считать hashcode от коллекции и надо ли это делать
    @Override
    public int hashCode() {
        int result = level;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + data.hashCode();
        result = 31 * result + (childrenElements != null ? childrenElements.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder tabs = new StringBuilder();
        String indent = "\t";
        int tabsCounter = level - 1;
        for (int i = 0; i < tabsCounter; i++) {
            tabs.append(indent);
        }

        String childrenPrint = childrenListPrint();
        return String.format("%s[%d] %s %s: %s%n%s", tabs, level, name, attributes, data, childrenPrint);
        
    }
    
    private String childrenListPrint() {
        StringBuilder children = new StringBuilder();
        if (childrenElements != null) {
            childrenElements.forEach(children::append);
        }
        
        return children.toString();
    }
}
