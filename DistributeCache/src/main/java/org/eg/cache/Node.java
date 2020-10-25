package org.eg.cache;

import java.util.HashMap;
import java.util.Map;


public class Node {
    private String domain;
    private String ip;
    private Map<String, Object> data;

    public Node(String domain, String ip) {
        this.domain = domain;
        this.ip = ip;
        this.data = new HashMap<String, Object>();
    }

    public <T> void put(String key, T obj) {
        this.data.put(key, obj);
    }

    public <T> T get(String key) {
        return (T) this.data.get(key);
    }

    public void remove(String key) {
        this.data.remove(key);
    }

    public String getDomain() {
        return domain;
    }

    public String getIp() {
        return ip;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public String toString() {
        return this.ip+"-"+this.domain;
    }
}
