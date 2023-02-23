package org.example.node;

import org.apache.xerces.dom.ElementImpl;
import org.w3c.dom.*;

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


}
