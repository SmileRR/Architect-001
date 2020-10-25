package org.eg;

import org.eg.cache.Cluster;
import org.eg.cache.Node;
import org.eg.hash.HashAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashCluster extends Cluster {

    private SortedMap<Long, Node> circle = new TreeMap<Long, Node>();
    private final HashAlgorithm algorithm;
    private final int numberOfReplicies;

    public ConsistentHashCluster(HashAlgorithm algorithm, int numberOfReplicas) {
        this.algorithm = algorithm;
        this.numberOfReplicies = numberOfReplicas;
    }

    @Override
    public void addNode(Node node) {
        for (int i = 0; i < numberOfReplicies; i++) {
            circle.put(algorithm.hash(node.toString() + i), node);
        }
    }

    @Override
    public void removeNode(Node node) {
        for (int i = 0; i < numberOfReplicies; i++) {
            circle.remove(algorithm.hash(node.toString() + i));
        }
    }

    @Override
    public List<Node> getNodes() {
        List<Node> nodes = new ArrayList<Node>();
        for (Node node: circle.values()) {
            if (!nodes.contains(node))
               nodes.add(node);
        }
        return nodes;
    }

    @Override
    public Node get(String key) {
        if (circle.isEmpty()) return null;

        long hash = algorithm.hash(key);
        //顺时针（正序）查找
        if (! circle.containsKey(hash)) {
            SortedMap<Long, Node> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty()?circle.firstKey():tailMap.firstKey();
        }
        return circle.get(hash);
    }
}
