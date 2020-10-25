package org.eg;


import org.eg.cache.Cluster;
import org.eg.cache.Node;
import org.eg.hash.HashAlgorithm;

import java.util.stream.IntStream;

public class Test {
    static int DATA_COUNT = 1000000;
    static String PRE_KEY = "CACHE_KEY_";

    public static void main(String[] args) {

//        new Test().test(new NormalCluster());
        new Test().test(new ConsistentHashCluster(HashAlgorithm.NATIVE_HASH,150));
    }

    public void test(Cluster cluster) {
        String  clusterName = cluster.getClass().getName();
        System.out.println("--------------Normal-----------" + clusterName);
        initiateCluster(cluster);
        distribution(cluster);
        hitRatio(cluster);

        System.out.println("\n--------------Add 1 Node-----------" + clusterName);
        initiateCluster(cluster);
        cluster.addNode(new Node("c5", "192.168.0.5"));
        distribution(cluster);
        hitRatio(cluster);

        System.out.println("\n--------------Remove 1 Node-----------" + clusterName);
        initiateCluster(cluster);
        cluster.removeNode(new Node("c4", "192.168.0.4"));
        distribution(cluster);
        hitRatio(cluster);
    }

    private void initiateCluster(Cluster cluster) {
        cluster.removeNode(new Node("c1", "192.168.0.1"));
        cluster.removeNode(new Node("c2", "192.168.0.2"));
        cluster.removeNode(new Node("c3", "192.168.0.3"));
        cluster.removeNode(new Node("c4", "192.168.0.4"));
        cluster.addNode(new Node("c1", "192.168.0.1"));
        cluster.addNode(new Node("c2", "192.168.0.2"));
        cluster.addNode(new Node("c3", "192.168.0.3"));
        cluster.addNode(new Node("c4", "192.168.0.4"));
        IntStream.range(0, DATA_COUNT)
                .forEach(index -> {
                    Node node = cluster.get(PRE_KEY + index);
                    node.put(PRE_KEY + index, "Test Data");
                });
    }
    private void distribution(Cluster cluster) {
        System.out.println("数据分布情况：");
        cluster.getNodes().forEach(node -> System.out.println("IP:" + node.getIp() + ",数据量:" + node.getData().size()));
    }

    //缓存命中率
    private void hitRatio(Cluster cluster) {
        long hitCount = IntStream.range(0, DATA_COUNT)
                .filter(index -> cluster.get(PRE_KEY + index).get(PRE_KEY + index) != null)
                .count();
        System.out.println("缓存命中率：" + hitCount * 1f / DATA_COUNT);
    }
}
