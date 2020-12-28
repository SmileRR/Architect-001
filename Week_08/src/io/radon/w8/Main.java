package io.radon.w8;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        Main instance = new Main();
        LinkedList<String> m = new LinkedList<String>();
        m.addAll(Arrays.asList(new String[] {"a","b","x","y","z"}));
        LinkedList<String> n = new LinkedList<String>();
        n.addAll(Arrays.asList(new String[] {"d","e","f","x","y","z"}));

        boolean isMerged = instance.isMerged(m, n);
        System.out.printf("is Merged : %s \n", isMerged );
    }

    boolean isMerged(LinkedList<String> m, LinkedList<String> n) {
        HashMap<String,String> map = new HashMap<String, String>();
        int step = 0;
        boolean result = false;
        if (m.size() > n.size()) {
            for (String temp: m) {
                System.out.printf("step: %s \n",++step);
                map.put(temp,null);

            }
            for (String temp: n) {
                System.out.printf("step: %s \n",++step);
                if(map.containsKey(temp)) {
                    result = true;
                    break;
                }
            }
        } else {
            for (String temp: n) {
                System.out.printf("step: %s \n",++step);
                map.put(temp,null);

            }
            for (String temp: m) {
                System.out.printf("step: %s \n",++step);
                if(map.containsKey(temp)) {
                    result = true;
                    break;
                }
            }
        }
        System.out.printf("m.length: %s, n.length: %s, final step: %s\n",m.size(), n.size(), step);
        return result;
    }

}
