package org.example.node;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JsonObjectStrongRefNode extends JsonElement {
    private final JsonNodeList childNodeList;


    public JsonObjectStrongRefNode(JsonDocument owner, String fieldName, JsonNodeList nodes) {
        this.ownerDocument = owner;
        this.name = fieldName;
        childNodeList = nodes;
    }


    @Override
    public NodeList getChildNodes() {
        return childNodeList;
    }

    @Override
    public boolean hasChildNodes() {
        return childNodeList.getLength() > 0;
    }


    @Override
    public Node getFirstChild() {
        return childNodeList.getLength() > 0 ? childNodeList.item(0) : null;
    }

    @Override
    public Node getLastChild() {
        return childNodeList.getLength() > 0 ? childNodeList.item(childNodeList.getLength() - 1) : null;
    }

}
