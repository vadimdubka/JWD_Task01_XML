package by.tc.task02.main;

import by.tc.task02.entity.Element;

class PrintDOM {
    
    private static final String DOM_CAN_T_BE_RECEIVED_FROM_FILE = "DOM can't be received from file.";
    
    static void print(Element element) {
        if (element != null) {
            StringBuilder stringBuilder = new StringBuilder();
            printElem(element, stringBuilder);
            System.out.println(stringBuilder.toString());
        } else {
            System.out.println(DOM_CAN_T_BE_RECEIVED_FROM_FILE);
        }
    }
    
    private static void printElem(Element element, StringBuilder stringBuilder) {
        stringBuilder.append(element.toString()).append("\n");
        if (element.getChildrenElements()!=null) {
            element.getChildrenElements().forEach((element1) -> printElem(element1, stringBuilder));
        }
    }
}
