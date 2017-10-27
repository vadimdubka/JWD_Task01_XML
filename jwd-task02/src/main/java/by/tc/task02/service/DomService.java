package by.tc.task02.service;

import by.tc.task02.entity.Element;
import by.tc.task02.service.exceptions.ServiceException;

public interface DomService {

    Element getDOM(String documentAddress) throws ServiceException;

}
