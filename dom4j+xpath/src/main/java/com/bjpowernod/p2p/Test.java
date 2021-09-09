package com.bjpowernod.p2p;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

/**
 * ClassName:Test
 * Package:com.bjpowernod.p2p
 * Description: 描述信息
 *
 * @date:2021/8/30 16:33
 * @author:sjf
 * @Copyright:动力节点
 */
public class Test {
    public static void main(String[] args) throws DocumentException {
        String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "\n" +
                "<bookstore>\n" +
                "\n" +
                "<book>\n" +
                "  <title lang=\"eng\">Harry Potter</title>\n" +
                "  <price>29.99</price>\n" +
                "</book>\n" +
                "\n" +
                "<book>\n" +
                "  <title lang=\"eng\">Learning XML</title>\n" +
                "  <price>39.95</price>\n" +
                "</book>\n" +
                "\n" +
                "</bookstore>";

        // 根据xml生成一个Document对象
        Document document = DocumentHelper.parseText(xml);
        // 获取第一个book标签的节点对象
        Node node = document.selectSingleNode("/bookstore/book[1]/price[1]");
        String text = node.getText();
        System.out.println(text);
    }
}
