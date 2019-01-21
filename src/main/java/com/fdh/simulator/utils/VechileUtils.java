package com.fdh.simulator.utils;

import com.fdh.simulator.Simulator;
import io.netty.channel.Channel;

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

    public static AtomicInteger suffix = new AtomicInteger(100000);
//    public static byte[] getRealTimePacket(Channel channel, int packetSerialNum) {
//    }

    /**
     * 校时数据包
     *
     * @param channel
     * @param packetSerialNum
     * @return
     */


    public static byte[] getTimingPacket(Channel channel, int packetSerialNum) {


        String vin = Simulator.channnelVinMap.get(channel.id().asLongText());
//        String vin = getVin();
        byte[] vinBytes = vin.getBytes();
        byte[] dataLengh = {0x01,0x00,0x04};
        byte[] time = ByteUtils.getIntegerByte(packetSerialNum);
        byte[] middle = new byte[comand.length+vinBytes.length+dataLengh.length+time.length];
        System.arraycopy(comand,0,middle,0,comand.length);
        System.arraycopy(vinBytes,0,middle,comand.length,vinBytes.length);
        System.arraycopy(dataLengh,0,middle,comand.length+vinBytes.length,dataLengh.length);
        System.arraycopy(time,0,middle,comand.length+vinBytes.length+dataLengh.length,time.length);
        byte b = ByteUtils.bccEncode(middle);
        byte[] ret = new byte[2+middle.length+1];
        ret[0] = 0x23;
        ret[1] = 0x23;
        System.arraycopy(middle,0,ret,2,middle.length);
        ret[2+middle.length] = b;
        return  ret;

    }


    public static String getVin() {
        int incrementAndGet = suffix.incrementAndGet();
        return  prefix+incrementAndGet;
    }


//    public static void main(String[] args) {
//
//        byte[] timingPacket = getTimingPacket(null, 1);
//        System.out.println(ByteUtils.bytesToHexString(timingPacket));
////        System.out.println(System.currentTimeMillis());
//    }


}
