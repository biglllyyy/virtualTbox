package com.fdh.simulator;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * 数据包性能统计
 */
public class PacketAnalyze {

    private static AtomicInteger atomicLong = new AtomicInteger(0);

    /**
     * key packetserialNum
     * value send packet timestamp
     */
    public static ConcurrentHashMap<Integer,Long> sendPacketMap = new ConcurrentHashMap<Integer,Long>();

    /***
     * key packetserialNum
     * value send and recevie time
     */
    public static ConcurrentHashMap<Integer,Long> packetMap = new ConcurrentHashMap<Integer,Long>();

    public  static int getPacketSerialNum(){
        return atomicLong.incrementAndGet();
    }
}
