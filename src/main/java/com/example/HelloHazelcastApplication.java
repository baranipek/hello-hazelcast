package com.example;


import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

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

        final IMap<String, Long> clusterMap = hazelcastInstance.getMap(MAP_NAME);

        if (clusterMap.tryLock(KEY_NAME, 2, TimeUnit.SECONDS)) {
            if (clusterMap.get(KEY_NAME) == null) {
                try {
                    clusterMap.put(KEY_NAME, 1L);
                    System.out.println("Just Started");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    clusterMap.unlock(KEY_NAME);
                }
            }

        }

        SpringApplication.run(HelloHazelcastApplication.class, args);
    }


}

