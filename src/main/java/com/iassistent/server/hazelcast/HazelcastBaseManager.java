package com.iassistent.server.hazelcast;

import com.hazelcast.core.*;
import com.hazelcast.monitor.*;
import com.hazelcast.partition.Partition;
import com.hazelcast.partition.PartitionService;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 */
public class HazelcastBaseManager implements LifecycleListener {

    private static final Logger log = Logger.getLogger(HazelcastBaseManager.class);

    protected HazelcastInstance hazelcastInstance;

    public HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }

    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @ManagedOperation(description = "List the members of this cluster. The order they are listed is from oldest to newest.")
    public String listClusterMembers(){
        String retString = null;
        Cluster cluster = hazelcastInstance.getCluster();
        if(cluster != null){
            retString = cluster.toString();
        }
        return retString;
    }

    @ManagedOperation(description = "List partition ownership")
    public String listPartitions(){
        PartitionService partitionService = hazelcastInstance.getPartitionService();
        Set<Partition> sets = partitionService.getPartitions();
        Map<Member, List<Integer>> memberPartitions = new HashMap<Member, List<Integer>>();
        int partitionCount = 0;

        for(Partition p : sets){
            partitionCount++;
            Member member = p.getOwner();
            int partitionId = p.getPartitionId();

            List<Integer> existings = memberPartitions.get(member);
            if(existings == null){
                existings = new ArrayList<Integer>();
                memberPartitions.put(member,existings);
            }
            existings.add(new Integer(partitionId));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Members: ");
        sb.append(memberPartitions.keySet().size());
        sb.append("\nPartitions: ");
        sb.append(partitionCount);

        for (Member member : memberPartitions.keySet()){
            sb.append("\n================================================================================\n");
            sb.append(member);

            List<Integer> ownedPartitions = memberPartitions.get(member);
            Collections.sort(ownedPartitions);

            sb.append("\n");
            sb.append(ownedPartitions.size());
            sb.append(" Partitions\n");
            sb.append(WordUtils.wrap(ownedPartitions.toString(), 80));
        }
        sb.append("\n================================================================================\n");

        return sb.toString();
    }

    @ManagedOperation(description = "List all entries in a named map")
    @ManagedOperationParameters( { @ManagedOperationParameter(name = "mapName", description = "name of the target map") })
    public String listMapContents( String mapName )
    {
        IMap<Object, Object> map = hazelcastInstance.getMap(mapName);

        Set<Object> keySet = map.keySet();

        StringBuilder sb = new StringBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        sb.append(mapName);
        sb.append("\n");
        sb.append(fixedWidthStrings(48, "Key=Value", 8, "Valid", 24, "Create Time", 24, "Expire Time"));
        sb.append("\n");
        sb.append("========================================================================================================\n");

        for (Object key : keySet)
        {
            MapEntry<Object, Object> mapEntry = map.getMapEntry(key);

            sb.append(fixedWidthString(mapEntry.getKey().toString() + "=" + mapEntry.getValue().toString(), 48));
            sb.append(fixedWidthString(Boolean.toString(mapEntry.isValid()), 8));
            sb.append(fixedWidthString(sdf.format(new Date(mapEntry.getCreationTime())), 24));
            long expire = mapEntry.getExpirationTime();
            if (expire == Long.MAX_VALUE)
            {
                sb.append(fixedWidthString("Never", 24));
            }
            else
            {
                sb.append(fixedWidthString(sdf.format(new Date(expire)), 24));
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    @ManagedOperation(description = "List distributed objects")
    public String listDistributedObjectInstances()
    {
        final Collection<Instance> instances = hazelcastInstance.getInstances();
        final Set<Member> members = hazelcastInstance.getCluster().getMembers();

        StringBuilder sb = new StringBuilder();

        Map<Instance.InstanceType, List<String>> instanceTypeMap = new HashMap<Instance.InstanceType, List<String>>();

        for (Instance instance : instances)
        {
            Instance.InstanceType instanceType = instance.getInstanceType();

            List<String> names = instanceTypeMap.get(instanceType);
            if (names == null)
            {
                names = new ArrayList<String>();
                instanceTypeMap.put(instanceType, names);
            }

            names.add(instance.getId().toString());
        }

        for (Instance.InstanceType type : instanceTypeMap.keySet())
        {
            List<String> names = instanceTypeMap.get(type);

            sb.append("================================================================================================================================\n");
            sb.append(type);
            sb.append(" Instances: ");
            sb.append(names.size());
            sb.append("\n");

            Collections.sort(names);

            if (type == Instance.InstanceType.MAP)
            {
                for (String name : names)
                {
                    String realName = name.substring(Prefix.MAP.length());

                    sb.append("\n");
                    sb.append(mapFormattedString(realName, "Owned (bytes)", "Backup (bytes)", "Marked Removed (bytes)", "Puts", "Gets", "Removes", "Locks", "Lock Waits"));
                    sb.append("\n");

                    MultiTask<DistributedMapStatsCallable.MemberMapStat> task = new MultiTask<DistributedMapStatsCallable.MemberMapStat>(new DistributedMapStatsCallable(realName), members);
                    hazelcastInstance.getExecutorService().execute(task);

                    try
                    {
                        Collection<DistributedMapStatsCallable.MemberMapStat> results = task.get();

                        for (DistributedMapStatsCallable.MemberMapStat result : results)
                        {
                            LocalMapStats stats = result.getLocalMapStats();
                            LocalMapOperationStats opStats = stats.getOperationStats();
                            sb.append(mapFormattedString(result.getMember().toString().trim(), stats.getOwnedEntryCount() + " (" + stats.getOwnedEntryMemoryCost() + ")", stats.getBackupEntryCount() + " (" + stats.getBackupEntryMemoryCost() + ")", stats.getMarkedAsRemovedEntryCount() + " (" + stats.getMarkedAsRemovedMemoryCost() + ")", opStats.getNumberOfPuts(), opStats.getNumberOfGets(), opStats.getNumberOfRemoves(), stats.getLockedEntryCount(), stats.getLockWaitCount()));
                            sb.append("\n");
                        }
                    }
                    catch (Throwable t)
                    {
                        // just get the local instance info
                        log.warn("Failed to get non-local map stats", t);
                        IMap map = hazelcastInstance.getMap(realName);
                        LocalMapStats stats = map.getLocalMapStats();
                        LocalMapOperationStats opStats = stats.getOperationStats();

                        Member localMember = hazelcastInstance.getCluster().getLocalMember();
                        sb.append(mapFormattedString(localMember.toString().trim(), stats.getOwnedEntryCount() + " (" + stats.getOwnedEntryMemoryCost() + ")", stats.getBackupEntryCount() + " (" + stats.getBackupEntryMemoryCost() + ")", stats.getMarkedAsRemovedEntryCount() + " (" + stats.getMarkedAsRemovedMemoryCost() + ")", opStats.getNumberOfPuts(), opStats.getNumberOfGets(), opStats.getNumberOfRemoves(), stats.getLockedEntryCount(), stats.getLockWaitCount()));
                        sb.append("\n");
                    }
                }
            }
            else if (type == Instance.InstanceType.QUEUE)
            {
                for (String name : names)
                {
                    String realName = name.substring(Prefix.QUEUE.length());

                    sb.append("\n");
                    sb.append(queueFormattedString(realName, "Owned", "Backup", "Avg Age", "Min Age", "Max Age", "Offers", "Polls"));
                    sb.append("\n");

                    MultiTask<DistributedQueueStatsCallable.MemberQueueStats> task = new MultiTask<DistributedQueueStatsCallable.MemberQueueStats>(new DistributedQueueStatsCallable(realName), members);
                    hazelcastInstance.getExecutorService().execute(task);

                    try
                    {
                        Collection<DistributedQueueStatsCallable.MemberQueueStats> results = task.get();

                        for (DistributedQueueStatsCallable.MemberQueueStats result : results)
                        {
                            LocalQueueStats stats = result.getLocalQueueStats();
                            LocalQueueOperationStats opStats = stats.getOperationStats();

                            String minAgeStr = "N/A";
                            long minAge = stats.getMinAge();
                            if (minAge != Long.MAX_VALUE)
                            {
                                minAgeStr = String.valueOf(minAge);
                            }
                            String maxAgeStr = "N/A";
                            long maxAge = stats.getMaxAge();
                            if (maxAge != Long.MIN_VALUE)
                            {
                                maxAgeStr = String.valueOf(maxAge);
                            }

                            sb.append(queueFormattedString(result.getMember().toString().trim(), stats.getOwnedItemCount(), stats.getBackupItemCount(), stats.getAveAge(), minAgeStr, maxAgeStr, opStats.getNumberOfOffers(), opStats.getNumberOfPolls()));
                            sb.append("\n");
                        }
                    }
                    catch (Throwable t)
                    {
                        // just get the local instance info
                        log.warn("Failed to get non-local queue stats", t);
                        IQueue queue = hazelcastInstance.getQueue(realName);
                        LocalQueueStats stats = queue.getLocalQueueStats();
                        LocalQueueOperationStats opStats = stats.getOperationStats();

                        String minAgeStr = "N/A";
                        long minAge = stats.getMinAge();
                        if (minAge != Long.MAX_VALUE)
                        {
                            minAgeStr = String.valueOf(minAge);
                        }
                        String maxAgeStr = "N/A";
                        long maxAge = stats.getMaxAge();
                        if (maxAge != Long.MIN_VALUE)
                        {
                            maxAgeStr = String.valueOf(maxAge);
                        }

                        Member localMember = hazelcastInstance.getCluster().getLocalMember();
                        sb.append(queueFormattedString(localMember.toString().trim(), stats.getOwnedItemCount(), stats.getBackupItemCount(), stats.getAveAge(), minAgeStr, maxAgeStr, opStats.getNumberOfOffers(), opStats.getNumberOfPolls()));

                        sb.append("\n");
                    }
                }
            }
            else
            {
                for (String name : names)
                {
                    sb.append(name.substring(name.lastIndexOf(':') + 1));
                    sb.append("\n");
                }
            }
        }

        sb.append("================================================================================================================================\n");

        return sb.toString();
    }




    public void shutdown(){
        log.info("Shutting down Hazelcast...");
        hazelcastInstance.getLifecycleService().shutdown();
    }

    @Override
    public void stateChanged(LifecycleEvent event) {
        log.info("Hazelcast lifecycle event state: " + event.getState());
    }

    protected static String fixedWidthString( String str, int width )
    {
        return String.format("%" + width + "." + width + "s", str);
    }

    protected static String fixedWidthStrings( Object... sizeAndStrs )
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < sizeAndStrs.length - 1; i += 2)
        {
            int size = (Integer)sizeAndStrs[i];
            String str = (String)sizeAndStrs[i + 1];

            sb.append(fixedWidthString(str, size));
        }

        return sb.toString();
    }

    protected static String queueFormattedString( Object... objs )
    {
        if (objs.length >= 8)
        {
            return fixedWidthStrings(28, String.valueOf(objs[0]), 12, String.valueOf(objs[1]), 12, String.valueOf(objs[2]), 12, String.valueOf(objs[3]), 12, String.valueOf(objs[4]), 12, String.valueOf(objs[5]), 12, String.valueOf(objs[6]), 12, String.valueOf(objs[7]));
        }
        else
        {
            return "Bad Input";
        }
    }

    protected static String mapFormattedString( Object... objs )
    {
        if (objs.length >= 9)
        {
            return fixedWidthStrings(28, String.valueOf(objs[0]), 20, String.valueOf(objs[1]), 20, String.valueOf(objs[2]), 24, String.valueOf(objs[3]), 8, String.valueOf(objs[4]), 8, String.valueOf(objs[5]), 10, String.valueOf(objs[6]), 8, String.valueOf(objs[7]), 12, String.valueOf(objs[8]));
        }
        else
        {
            return "Bad Input";
        }
    }
}
