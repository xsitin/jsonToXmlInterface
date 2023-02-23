package org.example.util;

import com.fasterxml.jackson.core.*;
import org.apache.xerces.dom.ChildNode;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.NodeImpl;
import org.example.node.*;

import java.io.IOException;

public class JsonUtil {
    public static JsonFactory factory;

    public static JsonParser createParserFromLocation(JsonLocation location) throws IOException {
        if (factory == null) factory = new JsonFactory();
        return createParserFromLocation(location, factory);
    }

    static JsonParser createParserFromLocation(JsonLocation location, JsonFactory factory) throws IOException {
        var content = (byte[]) location.contentReference().getRawContent();
        var length = location.contentReference().contentLength();
        if (length == -1) length = content.length;
        length -= (int) Math.max(0, location.getByteOffset());
        var offset = (int) (Math.max(0, location.contentReference().contentOffset()) + Math.max(0, location.getByteOffset()));
        return factory.createParser(content, offset, length);
    }

    public static void setSiblings(JsonNodeList elementsList) {
        for (int i = 1; i < elementsList.size(); i++) {
            var item = (JsonElement) elementsList.item(i - 1);
            var next = (JsonElement) elementsList.item(i);
            item.setNextSibling(next);
            next.setPreviousSibling(item);
        }
    }

    public static JsonNodeList parseChildren(JsonParser parser, CoreDocumentImpl owner, NodeImpl parent, String defaultName) throws IOException {
        var children = new JsonNodeList();
        if (parser.currentToken() == null)
            parser.nextToken();
        if (!parser.currentToken().isStructStart()) throw new JsonParseException(parser, "Should be struct start");
        var structureEndToken = JsonToken.values()[parser.currentToken().ordinal() + 1];
        while (parser.nextToken() != null && parser.currentToken() != structureEndToken) {
            var currentToken = parser.currentToken();
            if (currentToken == JsonToken.FIELD_NAME) {
                children.addAll(parseField(parser, owner, parent, defaultName));
            } else if (currentToken.isScalarValue()) {
                children.add(parseValue(parser, owner, parent, defaultName));
            } else if (currentToken == JsonToken.START_ARRAY) {
                var arrayElements = parseChildren(parser, owner, parent, "array");
                children.addAll(arrayElements);
            } else if (currentToken == JsonToken.START_OBJECT) {
                children.add(new JsonObjectNode(owner, parent, parser.currentTokenLocation(), defaultName));
                parser.skipChildren();
            }

        }
        return children;
    }

    private static JsonFieldNode parseValue(JsonParser parser, CoreDocumentImpl owner, NodeImpl parent, String fieldName) throws IOException {
        var field = new JsonFieldNode(owner, parent, fieldName);
        var value = new JsonValue(field, parser.getValueAsString());
        field.setValue(value);
        return field;
    }

    private static JsonNodeList parseField(JsonParser parser, CoreDocumentImpl owner, NodeImpl parent, String defaultName) throws IOException {
        var fields = new JsonNodeList();
        var nodeName = StringUtil.isNullOrBlank(parser.getCurrentName()) ? defaultName : parser.getCurrentName();
        parser.nextToken();
        if (parser.currentToken().isStructStart()) {
            //todo: parent should be fields, not current parent
            var values = parseChildren(parser, owner, parent, defaultName);
            for (int i = 0; i < values.getLength(); i++) {
                var field = new JsonFieldNode(owner, parent, nodeName);
                ChildNode value = (ChildNode) values.item(i);
                field.setValue(value);
                fields.add(field);
            }
        } else {
            fields.add(parseValue(parser, owner, parent, nodeName));
        }
        return fields;
    }

}
