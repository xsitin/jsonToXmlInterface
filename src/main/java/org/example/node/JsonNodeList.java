package org.example.node;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class JsonNodeList extends ArrayList<Node> implements NodeList {
    @Override
    public Node item(int index) {
        return this.get(index);
    }

    @Override
    public int getLength() {
        return this.size();
    }
}
