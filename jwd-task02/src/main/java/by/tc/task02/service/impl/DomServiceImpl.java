package by.tc.task02.service.impl;

import by.tc.task02.dao.DomDAO;
import by.tc.task02.dao.DAOFactory;
import by.tc.task02.entity.Element;
import by.tc.task02.service.DomService;

public class DomServiceImpl implements DomService {

    @Override
    public Element getDOM(String documentAddress) {
        DAOFactory factory = DAOFactory.getInstance();
        DomDAO domDAO = factory.getDomDAO();

        Element dom = domDAO.getDOM(documentAddress);

        return dom;
    }


}