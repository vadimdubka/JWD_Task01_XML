package by.tc.task02.dao.exceptions;

public class DAOException extends Exception {
    public DAOException() {
    }
    
    public DAOException(String message) {
        super(message);
    }
    
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}