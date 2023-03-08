import org.apache.xerces.dom.DeepNodeListImpl;
import org.example.node.JsonDocument;
import org.example.node.JsonElement;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonDocumentTests {
    private JsonDocument jsonDocument;

    @Test
    void getDocumentElement() throws IOException {
        //given
        jsonDocument = new JsonDocument("{\"json\":\"test\"}".getBytes());

        //when
        Element element = jsonDocument.getDocumentElement();

        //then
        assertTrue(element instanceof JsonElement);
        assertTrue(element.hasChildNodes());
        assertEquals("json", element.getTagName());
        assertEquals("test", element.getFirstChild().getFirstChild().getNodeValue());

    }

    @Test
    void getElementsByTagName() throws IOException {
        //given
        jsonDocument = new JsonDocument("{\"json\":\"test\"}".getBytes());

        //when
        var nodeList = jsonDocument.getElementsByTagName("json");
        //then
        assertTrue(nodeList instanceof DeepNodeListImpl);
        assertEquals(nodeList.getLength(), 2);
        assertEquals(nodeList.item(1).getFirstChild().getTextContent(), "test");
    }


    @Test
    void getParentNode() throws IOException {
        //given
        jsonDocument = new JsonDocument("{\"json\":\"test\"}".getBytes());

        //when
        Node parentNode = jsonDocument.getParentNode();

        //then
        assertNull(parentNode);
    }

    @Test
    void getChildNodes() throws IOException {
        //given
        jsonDocument = new JsonDocument("{\"json\":\"test\"}".getBytes());

        //when
        NodeList nodeList = jsonDocument.getChildNodes();

        //then
        assertTrue(nodeList instanceof JsonElement);
        assertEquals(jsonDocument.getFirstChild(), nodeList);
    }

    @Test
    void getLastChild() throws IOException {
        //given
        jsonDocument = new JsonDocument("{\"json\":\"test\"}".getBytes());

        //when
        Node lastChild = jsonDocument.getLastChild();

        //then
        assertTrue(lastChild instanceof JsonElement);
        assertSame(jsonDocument.getFirstChild(), lastChild);
    }


    @Test
    void getNextSibling() throws IOException {
        //given
        jsonDocument = new JsonDocument("{\"json\":\"test\",\"str\":\"some\"}".getBytes());
        var rootNode = jsonDocument.getFirstChild();

        //when
        Node nextSibling = rootNode.getFirstChild().getNextSibling();

        //then
        assertTrue(nextSibling instanceof JsonElement);
        assertEquals("str", nextSibling.getNodeName());
        assertNull(nextSibling.getNextSibling());
    }


    @Test
    void hasChildNodes() throws IOException {
        //given
        jsonDocument = new JsonDocument("{\"json\":\"test\"}".getBytes());

        //when
        boolean hasChildNodes = jsonDocument.hasChildNodes();

        //then
        assertTrue(hasChildNodes);
    }

    @Test
    void startsFromArray() throws IOException {
        //given
        jsonDocument = new JsonDocument("[ 1, 2, 3 ]".getBytes());

        //when
        int childCount = jsonDocument.getChildNodes().getLength();

        //then
        assertEquals(3, childCount);
    }

}