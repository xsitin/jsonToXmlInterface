package org.example;

import org.example.node.JsonDocument;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

public class Main {
    public static void main(String[] args) {
        CheckJsonDoc();
    }

    private static void CheckJsonDoc() {
        FileInputStream stream;
        byte[] input;
        try {
            stream = new FileInputStream("src/main/resources/jsons/test.json");
            input = stream.readAllBytes();
            stream.close();
            var doc = new JsonDocument(input);

            System.out.println(new String(input));
            TransformerFactory factory = TransformerFactory.newDefaultInstance();
            var transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(2));

            var swr = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(swr));
            System.out.println(swr);
        } catch (IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

}