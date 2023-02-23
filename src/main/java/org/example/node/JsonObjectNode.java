package org.example.node;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import org.apache.xerces.dom.ChildNode;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.NodeImpl;
import org.example.util.JsonUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.lang.ref.SoftReference;

public class JsonObjectNode extends JsonElement {
    private SoftReference<JsonNodeList> childNodeListReference = new SoftReference<>(null);
    private final JsonLocation startLocation;

    public JsonObjectNode(CoreDocumentImpl owner, NodeImpl parent, JsonLocation startLocation, String fieldName) {
        this.ownerDocument = owner;
        this.startLocation = startLocation;
        this.name = fieldName;
        this.ownerNode = parent;
        if (parent != null)
            flags = (short) (flags | NodeImpl.OWNED);
    }

    private JsonNodeList retrieveChildNodes() throws IOException {
        JsonNodeList childrenList;
        try (JsonParser parser = JsonUtil.createParserFromLocation(startLocation)) {
            childrenList = JsonUtil.parseChildren(parser, ownerDocument, this, "array");
        }
        if (childrenList.size() > 0) {
            this.firstChild = (ChildNode) childrenList.item(0);
            JsonUtil.setSiblings(childrenList);
        }
        childNodeListReference = new SoftReference<>(childrenList);
        return childrenList;
    }


    @Override
    public NodeList getChildNodes() {
        var childrenList = childNodeListReference.get();
        if (childrenList == null) {
            try {
                childrenList = retrieveChildNodes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return childrenList;
    }

    @Override
    public boolean hasChildNodes() {
        var childrenList = getChildNodes();
        return childrenList.getLength() > 0;
    }


    @Override
    public Node getFirstChild() {
        var childrenList = getChildNodes();
        return childrenList.getLength() > 0 ? childrenList.item(0) : null;
    }

    @Override
    public Node getLastChild() {
        var childrenList = getChildNodes();
        return childrenList.getLength() > 0 ? childrenList.item(childrenList.getLength() - 1) : null;
    }


}
