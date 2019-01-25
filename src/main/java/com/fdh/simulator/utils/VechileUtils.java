package com.fdh.simulator.utils;


import com.fdh.simulator.constant.CommandTag;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fudh
 * @ClassNmme VechileUtils
 * @date 2019/1/21 8:55
 * @Description: TODO
 */
public class VechileUtils {

    public static byte[] comand = {0x07, (byte) 0xFE};

    public static String prefix = "LNBSCB3FXYZ";

    public static AtomicInteger suffix = new AtomicInteger(PropertiesUtils.getIntegerProperty("client.vin.suffix"));


    private static byte[] vechileData;

    /**
     * 数据单元有效数据的个数
     */
    private static int count = 0;

    /**
     * 只生成一次假的实时数据
     */
    static {
        count = 400;
        vechileData = new byte[count];//填充假的实时数据
        for (int i = 0; i < count; i++) {
            vechileData[i] = (byte) i;
        }
    }

    /**
     * 终端校时数据包
     *
     * @param vin
     * @param packetSerialNum
     * @return
     */
    public static byte[] getPacket(CommandTag commandTag, String vin, int packetSerialNum) {

        byte[] fixPrefixPacket;
        byte[] packetSerialNo = ByteUtils.getIntegerByte(packetSerialNum);
        byte[] middle;
        if (commandTag == CommandTag.REALTIME_INFO_REPORT) {
            count = 400;
            //实时数据处理在包序号后面追假的实时数据
        } else {
            count = 0;
        }
        fixPrefixPacket = fixPrefixPacket(commandTag, vin, (short) (4 + count));
        middle = new byte[fixPrefixPacket.length + packetSerialNo.length + count];
        System.arraycopy(fixPrefixPacket, 0, middle, 0, fixPrefixPacket.length);
        System.arraycopy(packetSerialNo, 0, middle, fixPrefixPacket.length, packetSerialNo.length);
        System.arraycopy(vechileData, 0, middle, fixPrefixPacket.length + packetSerialNo.length, count);

        byte bccEncode = ByteUtils.bccEncode(middle);
        byte[] ret = new byte[2 + middle.length + 1];
        ret[0] = 0x23;
        ret[1] = 0x23;
        System.arraycopy(middle, 0, ret, 2, middle.length);
        ret[middle.length + 2] = bccEncode;
        return ret;
    }


    /**
     * 生成报文头
     *
     * @param commandTag 命令标识
     * @param vin        vin
     * @return
     */
    public static byte[] fixPrefixPacket(CommandTag commandTag, String vin, short dataLength) {
        byte[] preffixBytes = new byte[22];
        preffixBytes[0] = commandTag.getV();
        preffixBytes[1] = (byte) 0xFE;
        byte[] vinBytes = vin.getBytes();
        System.arraycopy(vinBytes, 0, preffixBytes, 2, vinBytes.length);
        preffixBytes[19] = 0x01;
        byte[] length = new byte[2];
        ByteUtils.putShort(length, dataLength, 0);
        preffixBytes[20] = length[0];
        preffixBytes[21] = length[1];
        //包序列号
        return preffixBytes;
    }

    /**
     * @param vin
     * @param
     * @return 获取注册报文
     */
//    public static byte[] getRegisterPacket(String vin, int packetSerialNum) {
//
//
//    }
    public static String getVin() {
        int incrementAndGet = suffix.incrementAndGet();
        return prefix + incrementAndGet;
    }


//    public static void main(String[] args) {
//        ExpiringMap<String,Integer> expiringMap = ExpiringMap.builder()
//                .variableExpiration()
//                .expirationListener(new ExpirationListener<String, Integer>() {
//                    @Override
//                    public void expired(String s, Integer integer) {
//                        System.out.println(s+"--->"+integer);
//                    }
//                })
//                .build();
//
//
//        for (int i = 0; i < 10; i++) {
//            expiringMap.put(i+"d",i, ExpirationPolicy.CREATED, 10, TimeUnit.SECONDS);
//
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (int i = 0; i < 100000000; i++) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

    public static void main(String[] args) {
        String vin = getVin();
//        byte[] bytes = fixPrefixPacket(CommandTag.HEARTBEAT, vin, (short) 4);
//        byte[] timingPacket = getTimingPacket(getVin(), 1);
//        String vin1 = getVin();
        byte[] timingPacket = getPacket(CommandTag.REALTIME_INFO_REPORT,vin, 1);
        System.out.println(ByteUtils.bytesToHexString(timingPacket));

    }

}
