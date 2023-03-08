import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.example.node.JsonFieldNode;
import org.example.node.JsonValue;
import org.example.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilTests {
    private JsonParser parser;
    private final JsonFactory factory = new JsonFactory();

    @Test
    void emptyObject_beEmpty() throws IOException {
        parser = factory.createParser("{ }");

        var children = JsonUtil.parseChildren(parser, "array");

        assertEquals(0, children.getLength());
    }

    @Test
    void emptyArray_beEmpty() throws IOException {
        parser = factory.createParser("[ ]");

        var children = JsonUtil.parseChildren(parser, "array");

        assertEquals(0, children.getLength());
    }

    @Test
    void objectWithEmptyArray_beEmpty() throws IOException {
        parser = factory.createParser("{ \"arr\" : [ ] }");

        var children = JsonUtil.parseChildren(parser, "array");

        assertEquals(0, children.getLength());
    }

    @Test
    void objectWithInnerEmptyObject_shoud_beEmpty() throws IOException {
        parser = factory.createParser("{\"arr\" : { } }");

        var children = JsonUtil.parseChildren(parser, "array");

        assertEquals(0, children.getLength());
    }

    @Test
    void simpleObject_beCorrect() throws IOException {
        parser = factory.createParser("{\"field\" : 123}");

        var children = JsonUtil.parseChildren(parser, "array");

        assertEquals(1, children.getLength());
        var field = children.item(0);
        assertTrue(field instanceof JsonFieldNode);
        assertSame("field", field.getNodeName());
        var child = field.getFirstChild();
        assertTrue(child instanceof JsonValue);
        assertSame(field, child.getParentNode());
        assertEquals("123", child.getTextContent());
    }

    @Test
    void arrayWithValues_valuesShouldBeInDifferentTags() throws IOException {
        parser = factory.createParser("[1, 2, 3]");
        var defaultArrayTagname = "array";

        var children = JsonUtil.parseChildren(parser, defaultArrayTagname);

        assertEquals(3, children.getLength());
        assertTrue(children.stream().allMatch(x -> x.getNodeName().equals(defaultArrayTagname)));
        for (int i = 0; i < children.getLength(); i++) {
            assertEquals(Integer.toString(i + 1), children.item(i).getFirstChild().getTextContent());
        }
    }

    @Test
    void innerArrayWithValues_correctNodesTree() throws IOException {
        parser = factory.createParser("{ \"innerArray\" : [ [ 1 ], [ 2 ] ] }".getBytes());

        var children = JsonUtil.parseChildren(parser, "array");

        assertEquals(2, children.getLength());
        assertTrue(children.stream().allMatch(x -> x.getNodeName().equals("innerArray")));
        for (int i = 0; i < children.getLength(); i++) {
            var topElement = children.item(i);
            assertEquals("innerArray", topElement.getNodeName());
            assertTrue(topElement.hasChildNodes());
            var middleElement = topElement.getFirstChild();
            assertEquals("array", middleElement.getNodeName());
            assertTrue(middleElement.hasChildNodes());
            var value = middleElement.getFirstChild();
            assertTrue(value instanceof JsonValue);
            assertEquals(Integer.toString(i + 1), value.getTextContent());
        }
    }

    @Test
    void innerObjectInArray_correctNodesTree() throws IOException {
        parser = factory.createParser("[ { \"someKey\" : 123 } ]".getBytes());

        var children = JsonUtil.parseChildren(parser, "array");

        assertEquals(1, children.getLength());
        assertEquals("array", children.get(0).getNodeName());
        assertEquals("someKey", children.get(0).getFirstChild().getNodeName());
    }

}
