package by.tc.task02.main;

import by.tc.task02.entity.Element;
import by.tc.task02.service.DomService;
import by.tc.task02.service.ServiceFactory;
import by.tc.task02.service.exceptions.ServiceException;

public class Main {
    
    public static void main(String[] args) {
        
        ServiceFactory factory = ServiceFactory.getInstance();
        DomService service = factory.getDomService();
        
        String documentAddress = "/document.xml";
        
        Element dom = null;
        try {
            dom = service.getDOM(documentAddress);
        } catch (ServiceException e) {
            System.err.println(e.getCause().getMessage());
        }
        PrintDOM.print(dom);
    }
}
