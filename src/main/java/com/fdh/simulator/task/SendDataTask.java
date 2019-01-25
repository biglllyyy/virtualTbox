package com.fdh.simulator.task;


import com.fdh.simulator.PacketAnalyze;
import com.fdh.simulator.constant.CommandTag;
import com.fdh.simulator.utils.ByteUtils;
import com.fdh.simulator.utils.VechileUtils;
import io.netty.channel.Channel;
import net.jodah.expiringmap.ExpirationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * @author fudh
 * @ClassNmme SendDataTask
 * @date 2019/1/25 15:35
 * @Description: TODO
 */
public class SendDataTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SendDataTask.class);
    private Channel channel;
    private CommandTag commandTag;
    private int packeExpiredTime;
    private String vin;

    public SendDataTask(Channel channel, CommandTag commandTag, int packeExpiredTime, String vin) {
        this.channel = channel;
        this.commandTag = commandTag;
        this.packeExpiredTime = packeExpiredTime;
        this.vin = vin;
    }

    @Override
    public void run() {
        try {
            long packetSerialNum = 0;
            String logstr="";
            if (commandTag == CommandTag.REALTIME_INFO_REPORT) {//只统计实时数据上报
                packetSerialNum = PacketAnalyze.getPacketSerialNum();
                PacketAnalyze.sendPacketMap.put(packetSerialNum, System.currentTimeMillis(), ExpirationPolicy.CREATED, packeExpiredTime, TimeUnit.SECONDS);
                logstr = "[NO." + packetSerialNum +"]=>";
            }
            byte[] packet = VechileUtils.getPacket(commandTag, vin, packetSerialNum);
            channel.writeAndFlush(packet);
            String toHexString = ByteUtils.bytesToHexString(packet);
            logger.info("[车辆]" + "[" + vin + "][SENDED]" + logstr + toHexString);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送报文失败,vin:" + vin, e);
        }
    }
}
