package org.eg;

import org.eg.cache.Cluster;
import org.eg.cache.Node;
import org.eg.hash.HashAlgorithm;

import java.util.UUID;

public class TestCluster {
    static final int DATA_SIZE = 1000000;
    static final int NODE_SIZE = 10;
    static final int REPLICIES_RANGE = 500;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int virtalNodes = 1;
        System.out.println("DataSize: " + DATA_SIZE + ", NodeSize: " + NODE_SIZE + ", VirtualNodes: " + virtalNodes);
        new TestCluster().testHashAlgorithm(virtalNodes);

        virtalNodes = 50;
        System.out.println("DataSize: " + DATA_SIZE + ", NodeSize: " + NODE_SIZE + ", VirtualNodes: " + virtalNodes);
        new TestCluster().testHashAlgorithm(virtalNodes);

        virtalNodes = 100;
        System.out.println("DataSize: " + DATA_SIZE + ", NodeSize: " + NODE_SIZE + ", VirtualNodes: " + virtalNodes);
        new TestCluster().testHashAlgorithm(virtalNodes);

        virtalNodes = 150;
        System.out.println("DataSize: " + DATA_SIZE + ", NodeSize: " + NODE_SIZE + ", VirtualNodes: " + virtalNodes);
        new TestCluster().testHashAlgorithm(virtalNodes);


        virtalNodes = 300;
        System.out.println("DataSize: " + DATA_SIZE + ", NodeSize: " + NODE_SIZE + ", VirtualNodes: " + virtalNodes);
        new TestCluster().testHashAlgorithm(virtalNodes);

        virtalNodes = 500;
        System.out.println("DataSize: " + DATA_SIZE + ", NodeSize: " + NODE_SIZE + ", VirtualNodes: " + virtalNodes);
        new TestCluster().testHashAlgorithm(virtalNodes);

        virtalNodes = 1000;
        System.out.println("DataSize: " + DATA_SIZE + ", NodeSize: " + NODE_SIZE + ", VirtualNodes: " + virtalNodes);
        new TestCluster().testHashAlgorithm(virtalNodes);
//        new TestCluster().identifyNoOfReplicies();
        System.out.println("Time spent: " + (System.currentTimeMillis() - start));
    }

    // 测试各种Hash算法在100万K-V下
    private void testHashAlgorithm(int replicies) {
        for (HashAlgorithm alg : HashAlgorithm.values()) {
            long start = System.currentTimeMillis();
            Cluster cluster = new ConsistentHashCluster(alg, replicies);
            setup(cluster);
            long sdt = calcStandardDeviation(cluster);
            System.out.println( alg + ", " + sdt + ", " + (System.currentTimeMillis()- start));
        }
    }

    //识别最优虚拟节点数量，待优化
    private void identifyNoOfReplicies() {
        StringBuilder sb = new StringBuilder();
        for (HashAlgorithm alg: HashAlgorithm.values()
             ) {
            long bestSDT = Long.MAX_VALUE;
            long bestNum = 1;
            for (int i = 1; i < REPLICIES_RANGE; i++) {
                long sdt = testHashAlgorithm(alg, i);
                if(bestSDT > sdt) {
                    bestSDT = sdt;
                    bestNum = i;
                }
            }
            System.out.println("Algorithm : " + alg + ", Best VirtualNodes: " + bestNum + ", StandardDeviation:" + bestSDT);
        }
    }

    private long testHashAlgorithm(HashAlgorithm alg, int noOfReplicies) {
        Cluster cluster = new ConsistentHashCluster(alg, noOfReplicies);
        setup(cluster);
        long sdt = calcStandardDeviation(cluster);
        return sdt;
    }

    private void setup(Cluster cluster) {
        for (int i = 0; i < NODE_SIZE; i++) {
            cluster.addNode(new Node("c" + i, "192.168.0." + i));
        }

        for (int i = 0; i < DATA_SIZE; i++) {
            String key = UUID.randomUUID().toString() + i;
            String value = "value" + key;
            cluster.get(key).put(key, value);
        }
    }

    private long calcStandardDeviation(Cluster cluster) {
        long sum = 0;
        long avg = DATA_SIZE / NODE_SIZE;
        for (Node node : cluster.getNodes()) {
            sum += Math.pow((node.getData().size() - avg), 2);
        }
        long sdt = Math.round(Math.sqrt(sum / NODE_SIZE));
        return sdt;
    }
}
