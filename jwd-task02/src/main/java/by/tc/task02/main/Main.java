package by.tc.task02.main;

import by.tc.task02.entity.Element;
import by.tc.task02.service.DomService;
import by.tc.task02.service.ServiceFactory;

public class Main {
    
    public static void main(String[] args) {
        
        ServiceFactory factory = ServiceFactory.getInstance();
        DomService service = factory.getDomService();
        
        String documentAddress = "/document.xml";
        Element dom = service.getDOM(documentAddress);
        
        PrintDOM.print(dom);
    }
    
    //TODO сделать обработку ошибок
    //TODO может все таки сделать отдельный метод для пичати entity
}
