package org.example.node;

import org.apache.xerces.dom.ChildNode;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.NodeImpl;

public class JsonFieldNode extends JsonElement {

    public JsonFieldNode(CoreDocumentImpl owner, NodeImpl parent, String fieldName) {
        this.name = fieldName;
        ownerNode = parent;
        if (parent != null)
            flags = (short) (flags | NodeImpl.OWNED);
        this.ownerDocument = owner;
    }

    public void setValue(ChildNode value) {
        firstChild = value;
    }
}
