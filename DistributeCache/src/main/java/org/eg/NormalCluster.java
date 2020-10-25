package org.eg;

import org.eg.cache.Cluster;
import org.eg.cache.Node;

import java.util.ArrayList;
import java.util.List;

public class NormalCluster extends Cluster {

    private final List<Node> nodes;
    public NormalCluster() {
        nodes = new ArrayList<Node>();
    }

    @Override
    public void addNode(Node node) {
        this.nodes.add(node);
    }

    @Override
    public void removeNode(Node node) {
        this.nodes.removeIf(o -> o.getIp().equals(node.getClass()) || o.getDomain().equals(node.getDomain()));
    }

    @Override
    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public Node get(String key) {
        long hash = key.hashCode();
        long index = hash % this.nodes.size();
        index = index > -1? index : index* -1;
        return nodes.get((int) index);
    }
}
