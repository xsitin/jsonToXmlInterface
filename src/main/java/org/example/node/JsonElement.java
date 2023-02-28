package org.example.node;

import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.ParentNode;
import org.w3c.dom.Element;

public abstract class JsonElement extends ElementImpl implements Element {
    public void setNextSibling(JsonElement element) {
        this.nextSibling = element;
    }

    public void setPreviousSibling(JsonElement element) {
        this.previousSibling = element;
    }

    @Override
    protected int changes() {
        return 0;
    }

    public void setParent(ParentNode parent) {
        this.ownerNode = parent;
        this.ownerDocument = (CoreDocumentImpl) parent.getOwnerDocument();
    }

}
