package com.fdh.simulator.packet;

import com.fdh.simulator.utils.ByteUtils;

/**
 * @author fudh
 * @ClassNmme VechilePacket
 * @date 2019/1/19 15:00
 * @Description: TODO
 */
public class VechilePacket {


    public static byte[] getRealTimePacket(String vin) {
        byte[] command = {0x08, (byte) 0xFE};
        byte[] vinbytes = vin.getBytes();
        byte[] contentData = {0x01,0x00,0x00};

        byte[] middleData = new byte[command.length + vinbytes.length + contentData.length];
        System.arraycopy(command, 0, middleData, 0, command.length);
        System.arraycopy(vinbytes, 0, middleData, command.length, vinbytes.length);
        System.arraycopy(contentData, 0, middleData, command.length + vinbytes.length , contentData.length);
        byte bccCode = ByteUtils.bccEncode(middleData);
        byte[] ret = new byte[2 + middleData.length + 1];
        ret[0] = 0x23;
        ret[1] = 0x23;

        System.arraycopy(middleData, 0, ret, 2, middleData.length);
        ret[2 + middleData.length] = bccCode;
        return ret;
    }




    public static void main(String[] args) {
        byte[] aa = getRealTimePacket("661D0000000000002");
        String bytesToHexString = ByteUtils.bytesToHexString(aa);
        System.out.println(bytesToHexString);
    }
}
