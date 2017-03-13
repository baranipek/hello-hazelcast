package com.example;


import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.util.Collections.singletonList;

@SpringBootApplication
public class HelloHazelcastApplication {

    private static final String KEY_NAME = "myKey";
    private static final String MAP_NAME = "clusterMap";

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.getJoin().getMulticastConfig().setEnabled(false);
        networkConfig.getJoin().getTcpIpConfig().setEnabled(true);
        networkConfig.getJoin().getTcpIpConfig().setMembers(singletonList("localhost"));

        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);

        IMap<String, Boolean> clusterMap = hazelcastInstance.getMap(MAP_NAME);
        if (clusterMap.get(KEY_NAME) == null) {
            clusterMap.put(KEY_NAME, Boolean.TRUE);
            System.out.println("Just Started");

        }

        SpringApplication.run(HelloHazelcastApplication.class, args);
    }


}

