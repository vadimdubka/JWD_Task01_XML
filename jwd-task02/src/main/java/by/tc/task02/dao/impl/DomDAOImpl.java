package by.tc.task02.dao.impl;

import by.tc.task02.dao.DomDAO;
import by.tc.task02.dao.DomParser;
import by.tc.task02.dao.exceptions.DAOException;
import by.tc.task02.entity.Element;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class DomDAOImpl implements DomDAO {
    
    @Override
    public Element getDOM(String documentAddress) throws DAOException {
        String document = readDocument(documentAddress);
        DomParser domParser = new DomParser();
        Element dom = domParser.parseXML(document);
        return dom;
    }
    
    
    /**
     * Method reads xml document.
     */
    private String readDocument(String fileAddress) throws DAOException {
        URL resourceURL = DomDAOImpl.class.getResource(fileAddress);
        if (resourceURL == null) {
            throw new DAOException("Document is not available");
        }
        
        StringBuilder wholeFile = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(resourceURL.openStream()));
            
            while (reader.ready()) {
                String line = reader.readLine();
                wholeFile.append(line);
            }
        } catch (FileNotFoundException e) {
            throw new DAOException("Document is not available.", e);
        } catch (IOException e) {
            throw new DAOException("I/O exception of some sort has occurred.", e);
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
}
