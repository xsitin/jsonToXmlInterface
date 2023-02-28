package org.example.node;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.apache.xerces.dom.DocumentImpl;
import org.example.util.JsonUtil;
import org.w3c.dom.*;

import java.io.IOException;


public class JsonDocument extends DocumentImpl implements Document {
    private final byte[] data;
    private final JsonElement root;

    public JsonDocument(byte[] data) throws IOException {
        this.data = data;
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(data);

        if (parser.nextToken() == JsonToken.START_OBJECT) {
            root = new JsonObjectNode(parser.currentTokenLocation(), "json");
            root.setParent(this);

        } else if (parser.getCurrentToken() == JsonToken.START_ARRAY) {
            var childNodes = JsonUtil.parseChildren(parser, "array");
            JsonUtil.setSiblings(childNodes);
            root = new JsonObjectStrongRefNode(this, "json", childNodes);
            JsonUtil.setParent(childNodes, root);
        } else {
            throw new IOException("Invalid JSON data");
        }


    }

    @Override
    public Element getDocumentElement() {
        return root;
    }

    @Override
    public NodeList getChildNodes() {
        return root;
    }

    @Override
    public Node getFirstChild() {
        return root;
    }

    @Override
    public Node getLastChild() {
        return root;
    }

    @Override
    public boolean hasChildNodes() {
        return root != null;
    }

    @Override
    public Node getNextSibling() {
        return root;
    }
}
