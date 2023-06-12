package org.example;

import org.example.node.JsonDocument;
import org.example.node.JsonObjectNode;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        CheckJsonDoc();
    }

    private static void CheckJsonDoc() {
        FileInputStream stream;
        byte[] input;
        try {
            stream = new FileInputStream("src/main/resources/jsons/big_data_3.json");
            input = stream.readAllBytes();
            stream.close();
            var doc = new JsonDocument(input);

            //printJsonAndXml(input, doc);
            //searchLastTag(doc);
            inspectDoc(doc.getDocumentElement());
            System.out.println("complete");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void searchLastTag(JsonDocument doc) {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "/json/sometag/sometag/*";

        try {
            var result = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
//            System.out.println(result);
            var length = result.getLength();
            System.out.println(length);
            for (var i = 0; i < length; i++) {
                System.out.println(result.item(i).getTextContent());
            }
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    private static void inspectDoc(Node root) {
        var nodes = root.getChildNodes();
        for (var i = 0; i < nodes.getLength(); i++) {
            inspectDoc(nodes.item(i));
        }
    }

    private static void printJsonAndXml(byte[] input, JsonDocument doc) throws TransformerException {
        //System.out.println(new String(input));
        TransformerFactory factory = TransformerFactory.newDefaultInstance();
        var transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(2));

        var result = new StreamResult(PrintWriter.nullWriter());
        transformer.transform(new DOMSource(doc), result);
    }

}