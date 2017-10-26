package by.tc.task02.dao;

/* Знакомство с тегами
Считайте с консоли имя файла, который имеет HTML-формат
Пример:
Info about Leela <span xml:lang="en" lang="en"><b><span>Turanga Leela
</span></b></span><span>Super</span><span>girl</span>
Первым параметром в метод main приходит тег. Например, "span"
Вывести на консоль все теги, которые соответствуют заданному тегу
Каждый тег на новой строке, порядок должен соответствовать порядку следования в файле
Количество пробелов, \n, \r не влияют на результат
Файл не содержит тег CDATA, для всех открывающих тегов имеется отдельный закрывающий тег, одиночных тегов нету
Тег может содержать вложенные теги
Пример вывода:
<span xml:lang="en" lang="en"><b><span>Turanga Leela</span></b></span>
<span>Turanga Leela</span>
<span>Super</span>
<span>girl</span>

Шаблон тега:
<tag>text1</tag>
<tag text2>text1</tag>
<tag
text2>text1</tag>

text1, text2 могут быть пустыми
*/

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Solution {
    public static void main(String[] args) throws IOException {

        //читаем содержимое файла в строку
        FileReader reader = new FileReader("src\\main\\java\\songs.xml");
        StringBuilder stringBuilder = new StringBuilder();
        while (reader.ready()) {
            int i = reader.read();
            if (i == 62) { //>
                stringBuilder.append((char) i);
                while (reader.ready()) {
                    i = reader.read();
                    if (i != 32 && i != 10 && i != 13) { // " " , "\n" , "\r"
                        stringBuilder.append((char) i);
                        break;
                    }
                }
            } else {
                if (i == 10 || i == 13) i = 32;// "\n" , "\r"  ----> " "
                if (i == 32) {
                    while (reader.ready()) {
                        int n = reader.read();
                        if (n == 10 || n == 13) n = 32; // "\n" , "\r"  ----> " "
                        if (n == 60 || n == 62) {//< и >
                            if (n == 62) { //>
                                stringBuilder.append((char) n);
                                while (reader.ready()) {
                                    int j = reader.read();
                                    if (j != 32 && j != 10 && j != 13) { // " " , "\n" , "\r"
                                        stringBuilder.append((char) j);
                                        break;
                                    }
                                }
                            } else {
                                stringBuilder.append((char) n);
                                break;
                            }
                        } else if (n != 32) {
                            stringBuilder.append((char) i);
                            stringBuilder.append((char) n);
                            break;
                        }
                    }
                } else stringBuilder.append((char) i);
            }
        }
        reader.close();

        // тег
        String tag = args[0];
        // Id начала закрывающегося тега. Если Id ==-1 - тегов болше нет, выходим из цикла поиска тегов.
        int tegIdBegin = 0;
        int tegIdClose = 0;
        // с какого индекса будем искать открывающиеся и закрывающиеся теги
        int fromIdBegin = 0;
        int fromIdClose = 0;
        //теги
        String tag1_1 = "<" + tag + ">"; // <tag>.
        String tag1_2 = "<" + tag + " "; // <tag .
        String tag1_3 = "<" + tag + "\n";// <tag\n.
        String tag1_4 = "<" + tag + "\r";// <tag\r.
        String tag2 = "</" + tag + ">"; // закрывающий тег

        // считаем количество открытых тегов
        int tOp = -1;
        // список c перечени тегов по порядку
        ArrayList<Integer> listOpenId = new ArrayList<>();
        TreeMap<Integer, String> listTeg = new TreeMap<>();

        tagReader(stringBuilder, tegIdBegin, tegIdClose, fromIdBegin, fromIdClose, tag1_1, tag1_2, tag1_3, tag1_4, tag2, tOp, listTeg, listOpenId);

        for (Map.Entry<Integer, String> pair : listTeg.entrySet()) {
            System.out.println(pair.getValue());
        }
    }

    // метод возвращает fromIdClose - id-ограничитель, с которого будем искать закрытые теги.
    static int tagReader(StringBuilder stringBuilder, int tegIdBegin, int tegIdClose, int fromIdBegin, int fromIdClose, String tag1_1, String tag1_2, String tag1_3, String tag1_4, String tag2, int tOp, TreeMap<Integer, String> listTeg, ArrayList<Integer> listOpenId) {
        tegIdBegin = tagBegin(stringBuilder, tag1_1, tag1_2, tag1_3, tag1_4, fromIdBegin); // 1) ищем первое появление открывающегося и закрывающегося тега
        tegIdClose = tagClose(stringBuilder, tag2, fromIdClose);
        if (tegIdBegin != -1 && tegIdBegin < tegIdClose) {
            tOp++;
        }
        // 1) если нет закрывающих тегов до конца строки
        if (tegIdClose == -1) return fromIdClose;
            // 2)если все открытые теги закрыты, но открывающих больше не осталось - выходим
        else if (tOp == -1 && tegIdBegin == -1) return fromIdClose;
            // 3) если все открыты  е теги закрыты, но остались открывающие - смещаем if (tOp == -1 && tegIdBegin != -1)
        else if (tOp == -1) {
            fromIdClose = tegIdClose + tag2.length();
            tagReader(stringBuilder, tegIdBegin, tegIdClose, fromIdBegin, fromIdClose, tag1_1, tag1_2, tag1_3, tag1_4, tag2, tOp, listTeg, listOpenId);
            return fromIdClose;
        }   // 4) если есть закрывающие теги и есть открывающиеся теги и НЕ ВСЕ открытые теги закрыты
        else {
            if (tegIdBegin != -1 && tegIdBegin < tegIdClose) { // 2.1) если первый тег - открывающийся:
                listOpenId.add(tegIdBegin);
                fromIdBegin = tegIdBegin + tag1_1.length();// сдвигаем ИД поиска начала строки
                while (tOp != -1) {
                    fromIdClose = tagReader(stringBuilder, tegIdBegin, tegIdClose, fromIdBegin, fromIdClose, tag1_1, tag1_2, tag1_3, tag1_4, tag2, tOp, listTeg, listOpenId); // рекурсия - получаем индекс окончания закрывающего тега
                    tOp--;
                }
                fromIdClose = tagReader(stringBuilder, tegIdBegin, tegIdClose, fromIdBegin, fromIdClose, tag1_1, tag1_2, tag1_3, tag1_4, tag2, tOp, listTeg, listOpenId); // рекурсия - получаем индекс окончания закрывающего тега
                return fromIdClose;
            } else {// 2.2) eсли первый тег - закрывающийся
                fromIdClose = tegIdClose + tag2.length();// сдвигаем позицию поиска закрытого тега
                String s_n = stringBuilder.substring(listOpenId.get(listOpenId.size() - 1), fromIdClose);
                listTeg.put(listOpenId.get(listOpenId.size() - 1), s_n);
                listOpenId.remove(listOpenId.size() - 1);
                return fromIdClose;
            }
        }
    }


    static int tagBegin(StringBuilder stringBuilder, String tag1_1, String tag1_2, String tag1_3, String tag1_4, int fromIdBegin) {
        int count = 0;
        int begin;
        ArrayList<Integer> tagBeginList = new ArrayList<>();
        int i_1 = stringBuilder.indexOf(tag1_1, fromIdBegin);
        if (i_1 != -1) {
            tagBeginList.add(i_1);
            count++;
        }
        int i_2 = stringBuilder.indexOf(tag1_2, fromIdBegin);
        if (i_2 != -1) {
            tagBeginList.add(i_2);
            count++;
        }
        int i_3 = stringBuilder.indexOf(tag1_3, fromIdBegin);
        if (i_3 != -1) {
            tagBeginList.add(i_3);
            count++;
        }
        int i_4 = stringBuilder.indexOf(tag1_4, fromIdBegin);
        if (i_4 != -1) {
            tagBeginList.add(i_4);
            count++;
        }
        if (count != 0) {
            Collections.sort(tagBeginList);
            begin = tagBeginList.get(0);
        } else begin = -1;
        return begin;
    }

    // ищем первое появление закрывающегося тега
    static int tagClose(StringBuilder stringBuilder, String tag2, int fromIdClose) {
        return stringBuilder.indexOf(tag2, fromIdClose);
    }
}