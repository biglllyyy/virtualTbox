package com.fdh.simulator.utils;

/**
 * @author fudh
 * @ClassNmme ByteUtils
 * @date 2019/1/19 15:24
 * @Description: TODO
 */
public class ByteUtils {
    /**
     * bcc 异或校验返回byte
     *
     * @param bytes
     * @return
     */
    public static byte bccEncode(byte[] bytes) {
        byte retByte = 0;
        if (bytes == null || bytes.length <= 0) {
            return retByte;
        }
        retByte = bytes[0];
        for (int i = 1; i < bytes.length; i++) {
            retByte ^= bytes[i];
        }
        return retByte;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    /**
     *  转换int为byte数组
     *
     * @param x
     */
    public static byte[]  getIntegerByte(Integer x) {
        byte[] bb = new byte[4];
        bb[0] = (byte) (x >> 24);
        bb[1] = (byte) (x >> 16);
        bb[2] = (byte) (x >> 8);
        bb[3] = (byte) (x >> 0);
        return bb;
    }

    /**
     * 通过byte数组取到int
     *
     * @param bb
     * @param index 第几位开始
     * @return
     */
    public static int getInt(byte[] bb, int index) {
        return (int) ((((bb[0] & 0xff) << 24)
                | ((bb[1] & 0xff) << 16)
                | ((bb[2] & 0xff) << 8) | ((bb[3] & 0xff) << 0)));
    }

//    public static void main(String[] args) {
//        int anInt = getInt(new byte[]{0x07, 0x5B, (byte) 0xCD, 0x15},0);
//        System.out.println(anInt);
//    }


//    public  static  int parseSerialNum(byte[] packet){
//
//        return  = getInt(packet, 10);
//
//    }

    /**
     * 转换long型为byte数组
     *
     * @param x
     */
    public static byte[]  putLong(long x) {
        byte[] bb = new byte[8];
        bb[0] = (byte) (x >> 56);
        bb[1] = (byte) (x >> 48);
        bb[2] = (byte) (x >> 40);
        bb[3] = (byte) (x >> 32);
        bb[4] = (byte) (x >> 24);
        bb[5] = (byte) (x >> 16);
        bb[6] = (byte) (x >> 8);
        bb[7] = (byte) (x >> 0);
        return bb;
    }

    /**
     * 通过byte数组取到long
     *
     * @param bb
     * @param index
     * @return
     */
    public static long getLong(byte[] bb, int index) {
        return ((((long) bb[0] & 0xff) << 56)
                | (((long) bb[1] & 0xff) << 48)
                | (((long) bb[2] & 0xff) << 40)
                | (((long) bb[3] & 0xff) << 32)
                | (((long) bb[4] & 0xff) << 24)
                | (((long) bb[5] & 0xff) << 16)
                | (((long) bb[6] & 0xff) << 8) | (((long) bb[7] & 0xff) << 0));
    }
}
