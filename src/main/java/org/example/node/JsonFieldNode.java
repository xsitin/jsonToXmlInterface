package org.example.node;

import org.apache.xerces.dom.ChildNode;
import org.apache.xerces.dom.NodeImpl;

public class JsonFieldNode extends JsonElement {

    public JsonFieldNode(String fieldName) {
        this.name = fieldName;
        flags = (short) (flags | NodeImpl.OWNED);
    }

    public void setValue(ChildNode value) {
        firstChild = value;
    }
}
