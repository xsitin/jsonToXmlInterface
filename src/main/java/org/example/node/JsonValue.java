package org.example.node;

import org.apache.xerces.dom.NodeImpl;
import org.apache.xerces.dom.TextImpl;

public class JsonValue extends TextImpl {
    public JsonValue(NodeImpl parent, String value) {
        this.data = value;
        ownerNode = parent;
        if (parent != null)
            flags = (short) (flags | NodeImpl.OWNED);
    }
}
