package by.tc.task02.dao;

import by.tc.task02.dao.exceptions.DAOException;
import by.tc.task02.entity.Element;

public interface DomDAO {
	/**Method retrieves DOM from document.*/
	Element getDOM(String documentAddress) throws DAOException;
}
