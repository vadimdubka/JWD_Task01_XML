package by.tc.task02.main;

import by.tc.task02.entity.Element;

class PrintDOM {
    
    private static final String DOM_CAN_T_BE_RECEIVED_FROM_FILE = "DOM can't be received from file.";
    
    static void print(Element element) {
        if (element != null) {
            System.out.println(element);
        } else {
            System.out.println(DOM_CAN_T_BE_RECEIVED_FROM_FILE);
        }
    }
}
