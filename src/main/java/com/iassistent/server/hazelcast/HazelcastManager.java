package com.iassistent.server.hazelcast;

import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.LifecycleListener;
import org.apache.log4j.Logger;

/**
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 */
public class HazelcastManager extends HazelcastBaseManager {

    private static final Logger log = Logger.getLogger(HazelcastManager.class);

    private String groupName;
    private String groupPassword;
    private String targetHost = "localhost";

    public void setGroupName( String groupName )
    {
        this.groupName = groupName;
    }

    public void setGroupPassword( String groupPassword )
    {
        this.groupPassword = groupPassword;
    }

    public void setTargetHost( String targetHost )
    {
        this.targetHost = targetHost;
    }

    public void initialize(){
        log.info("HazelcastClientManager initializing...");
        tryInitialize(6, 5000);
    }

    private void tryInitialize( int nAttempts, long waitMsPerAttempt ){
        int attemptNum = 0;
        do{
            attemptNum++;
            HazelcastConnector connector = new HazelcastConnector(groupName, groupPassword, targetHost, this);
            connector.setName("HazelcastClientConnector-" + attemptNum);
            connector.start();

            try {
                hazelcastInstance = connector.getConnectedInstance(waitMsPerAttempt);
            } catch (InterruptedException e) {
                // ignore...
            }

        }while(hazelcastInstance == null && attemptNum < nAttempts);

        if (hazelcastInstance == null) {
            throw new IllegalStateException("Failed to initialize Hazelcast Client after " + nAttempts + " tries");
        } else {
            log.info("HazelcastClientManager initialized on attempt " + attemptNum + ": " + hazelcastInstance.getCluster().getMembers());
        }
    }

    private static class HazelcastConnector extends Thread {
        private LifecycleListener f_listener;
        private HazelcastClient f_client;
        private ClientConfig f_config;

        public HazelcastConnector(String groupName, String groupPassword, String targetHost, LifecycleListener linster){
            f_listener = linster;
            f_config = new ClientConfig();
            f_config.getGroupConfig().setName(groupName).setPassword(groupPassword);
            f_config.addAddress(targetHost);
        }

        @Override
        public void run(){
            HazelcastClient client = HazelcastClient.newHazelcastClient(f_config);
            if(f_listener != null){
                client.getLifecycleService().addLifecycleListener(f_listener);
            }
            synchronized (this){
                f_client = client;
                notifyAll();
            }
        }

        public HazelcastClient getConnectedInstance(long waitMs) throws InterruptedException{
            long endTime = System.currentTimeMillis()+waitMs;
            synchronized (this){
                while(f_client == null && System.currentTimeMillis() < endTime){
                    wait(waitMs);
                }
            }
            return f_client;
        }
    }
}
