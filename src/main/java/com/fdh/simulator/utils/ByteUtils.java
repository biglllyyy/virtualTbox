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

    public static String bytesToHexString(byte[] src){
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
            stringBuilder.append(hv).append(" ");
        }
        return stringBuilder.toString().toUpperCase();
    }

}
