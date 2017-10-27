package by.tc.task02.service.impl;

import by.tc.task02.dao.DomDAO;
import by.tc.task02.dao.DAOFactory;
import by.tc.task02.dao.exceptions.DAOException;
import by.tc.task02.entity.Element;
import by.tc.task02.service.DomService;
import by.tc.task02.service.exceptions.ServiceException;

public class DomServiceImpl implements DomService {
    
    @Override
    public Element getDOM(String documentAddress) throws ServiceException {
        DAOFactory factory = DAOFactory.getInstance();
        DomDAO domDAO = factory.getDomDAO();
        
        Element dom = null;
        try {
            dom = domDAO.getDOM(documentAddress);
        } catch (DAOException e) {
            throw new ServiceException("Exception on DAO layer.", e);
        }
        
        return dom;
    }
    
    
}