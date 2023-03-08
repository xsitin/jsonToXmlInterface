import org.example.node.JsonDocument;
import org.example.node.JsonNodeList;
import org.example.node.JsonObjectStrongRefNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonObjectStrongRefTests {
    @Test
    void returnsCorrectElementFromChildList() throws IOException {
        var doc = new JsonDocument("[ 1, 2, 3 ]".getBytes());

        var obj = doc.getFirstChild();

        var child = obj.getChildNodes();
        assertEquals(3, child.getLength());
        assertEquals(child.item(0), obj.getFirstChild());
        assertEquals(child.item(2), obj.getLastChild());
        assertTrue(obj.hasChildNodes());
    }

    @Test
    void returnsAcceptedValues() throws IOException {
        var doc = new JsonDocument("[]".getBytes());
        var childList = new JsonNodeList();

        var obj = new JsonObjectStrongRefNode(doc, "obj", childList);

        assertEquals(childList, obj.getChildNodes());
        assertEquals(doc, obj.getOwnerDocument());
    }


}
