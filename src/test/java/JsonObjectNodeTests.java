import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.example.node.JsonObjectNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonObjectNodeTests {

    private final JsonFactory factory = new JsonFactory();

    @Test
    void simpleTree_beCorrect() throws IOException {
        JsonParser parser = factory.createParser("{\"objectField\": {\"field\": \"value\"}}".getBytes());

        var obj = new JsonObjectNode(parser.currentTokenLocation(), "json");

        assertEquals(1, obj.getChildNodes().getLength());
        var objectField = obj.getFirstChild();
        assertEquals("objectField", objectField.getNodeName());
        assertEquals(obj, objectField.getParentNode());
        assertEquals(objectField, obj.getLastChild());
        var field = objectField.getFirstChild();
        assertEquals(objectField, field.getParentNode());
        assertEquals(field, field.getFirstChild().getParentNode());
    }

}
