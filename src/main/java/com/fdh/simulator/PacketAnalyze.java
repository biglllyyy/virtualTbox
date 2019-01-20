package com.fdh.simulator;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * 数据包性能统计
 */
public class PacketAnalyze {

    public static AtomicInteger atomicLong = new AtomicInteger(0);

    /**
     * key packetserialNum
     * value send packet timestamp
     */
    public static ConcurrentHashMap<Integer, Long> sendPacketMap = new ConcurrentHashMap<Integer, Long>();

    /***
     * key packetserialNum
     * value send and recevie time
     */
    public static ConcurrentHashMap<Integer, Integer> packetMap = new ConcurrentHashMap<Integer, Integer>();

    public static int getPacketSerialNum() {
        return atomicLong.incrementAndGet();
    }


    public static int partition(long[] array, int lo, int hi) {
        //固定的切分方式
        long key = array[lo];
        while (lo < hi) {
            while (array[hi] >= key && hi > lo) {//从后半部分向前扫描
                hi--;
            }
            array[lo] = array[hi];
            while (array[lo] <= key && hi > lo) {//从前半部分向后扫描
                lo++;
            }
            array[hi] = array[lo];
        }
        array[hi] = key;
        return hi;
    }

    /**
     * 分析报文性能
     */
    public static List<Integer> analyze() {
        List<Integer> responseDiff = (List<Integer>) packetMap.values();
        Collections.sort(responseDiff);
        return responseDiff;
    }

    /**
     * 计算平均响应时间
     *
     * @return
     */
    public static String average(List<Integer> list) {
        if (list == null || list.size() == 0) {
            return 0 + "";
        }
        OptionalDouble average = list.stream().mapToInt(Integer::intValue).average();
        return String.format("%.2f", average.getAsDouble());//保留两位小数
    }

    /***
     * 计算最大响应时间
     * @param list
     * @return
     */
    public static String max(List<Integer> list) {
        if (list == null || list.size() == 0) {
            return 0 + "";
        }
        OptionalInt OptionalMax = list.stream().mapToInt(Integer::intValue).max();
        Integer max = OptionalMax.getAsInt();
        return max + "";
    }

    /***
     * 计算最小响应时间
     * @param list
     * @return
     */
    public static String min(List<Integer> list) {
        if (list == null || list.size() == 0) {
            return 0 + "";
        }
        OptionalInt OptionalMin = list.stream().mapToInt(Integer::intValue).min();
        Integer min = OptionalMin.getAsInt();
        return min + "";
    }

    /***
     * 计算丢包率
     * @param
     * @return
     */
    public static String getSupplementary(List<Integer> sends, List<Integer> receivces) {
        boolean suppleList = sends.remove(receivces);
        return sends.size() + "";

    }


    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        List<Integer> a = new ArrayList<>();
        for (int i = 0; i < 10000000; i++) {
            a.add(i);
        }
        String average = average(a);
        String max = max(a);
        String min = min(a);
        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println(average);
        System.out.println(max);
        System.out.println(min);
        System.out.println(currentTimeMillis1 - currentTimeMillis);

    }
}
