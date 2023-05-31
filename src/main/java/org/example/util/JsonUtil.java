package org.example.util;

import com.fasterxml.jackson.core.*;
import org.example.node.*;
import org.w3c.dom.Node;

import java.io.IOException;

public class JsonUtil {
    public static JsonFactory factory;
    private static final JsonToken[] jsonTokenValues = JsonToken.values();

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

    public static void setParent(JsonNodeList nodes, JsonElement parent) {
        for (Node node : nodes)
            ((JsonElement) node).setParent(parent);
    }


    public static JsonNodeList parseChildren(JsonParser parser, String defaultName) throws IOException {
        var children = new JsonNodeList();
        if (parser.currentToken() == null)
            parser.nextToken();
        if (!parser.currentToken().isStructStart()) throw new JsonParseException(parser, "Should be struct start");
        var structureEndToken = jsonTokenValues[parser.currentToken().ordinal() + 1];
        while (parser.nextToken() != null && parser.currentToken() != structureEndToken) {
            var currentToken = parser.currentToken();
            if (currentToken == JsonToken.FIELD_NAME) {
                children.addAll(parseField(parser, defaultName));
            } else if (currentToken.isScalarValue()) {
                children.add(parseValue(parser, defaultName));
            } else if (currentToken == JsonToken.START_ARRAY) {
                var arrayElements = parseChildren(parser, "array");
                children.addAll(arrayElements);
            } else if (currentToken == JsonToken.START_OBJECT) {
                var childObject = new JsonObjectNode(parser.currentTokenLocation(), defaultName);
                children.add(childObject);
                parser.skipChildren();
            }

        }
        return children;
    }

    private static JsonFieldNode parseValue(JsonParser parser, String fieldName) throws IOException {
        var field = new JsonFieldNode(fieldName);
        var value = parser.getText();
        if (value == null)
            value = "null";
        var valueNode = new JsonValue(field, value);
        field.setValue(valueNode);
        return field;
    }

    private static JsonNodeList parseField(JsonParser parser, String defaultName) throws IOException {
        var fields = new JsonNodeList();
        var nodeName = StringUtil.isNullOrBlank(parser.getCurrentName()) ? defaultName : parser.getCurrentName();
        parser.nextToken();
        if (parser.currentToken().isStructStart()) {
            if (parser.currentToken() == JsonToken.START_OBJECT) {
                fields.add(new JsonObjectNode(parser.currentTokenLocation(), nodeName));
                parser.skipChildren();
            } else {
                var values = parseChildren(parser, defaultName);
                for (int i = 0; i < values.getLength(); i++) {
                    var field = new JsonFieldNode(nodeName);
                    var value = (JsonElement) values.item(i);
                    field.setValue(value);
                    value.setParent(field);
                    fields.add(field);
                }
            }
        } else {
            fields.add(parseValue(parser, nodeName));
        }
        return fields;
    }

}
