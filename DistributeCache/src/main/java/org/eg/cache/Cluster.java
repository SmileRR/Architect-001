package org.eg.cache;

import java.util.ArrayList;
import java.util.List;

public abstract class Cluster {

    public Cluster() {
    }

    public abstract void addNode(Node node);
    public abstract void removeNode(Node node);
    public abstract List<Node> getNodes() ;

    public abstract Node get(String key);
}
