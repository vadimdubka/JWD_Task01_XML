package by.tc.task02.service.exceptions;

public class ServiceException extends Exception {
    public ServiceException() {
    }
    
    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}