package com.fdh.simulator.task;


import com.fdh.simulator.NettyChannelManager;
import com.fdh.simulator.PacketAnalyze;
import com.fdh.simulator.Simulator;
import com.fdh.simulator.utils.ByteUtils;
import com.fdh.simulator.utils.VechileUtils;
import io.netty.channel.Channel;
import net.jodah.expiringmap.ExpirationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


/***
 * 定时任务
 */
public class ScheduleTask extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    /**
     * 执行调度的次数
     */
    int count = 10;

    int mcount = 0;

    /**
     * 每包的过期时间
     */
    int packeExpiredTime = 10;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPackeExpiredTime() {
        return packeExpiredTime;
    }

    public void setPackeExpiredTime(int packeExpiredTime) {
        this.packeExpiredTime = packeExpiredTime;
    }

    @Override
    public void run() {

        if (count <= 0) {
            Simulator.timer.cancel();
            logger.info("数据发送完成!");
            return;
        }
        mcount++;
        try {
            ConcurrentHashMap<String, Channel> concurrentHashMap = NettyChannelManager.getChannnelMap();
            logger.error("第:"+mcount+"次发送，发送量"+concurrentHashMap.size());
            if (concurrentHashMap.size() > 0) {
                Set<Map.Entry<String, Channel>> entries = concurrentHashMap.entrySet();
                for (Map.Entry<String, Channel> entry : entries) {
                    String channelId = entry.getKey();
                    String vin = NettyChannelManager.getChannnelVinMap().get(channelId);
                    Channel channel = entry.getValue();
                    if (channel.isOpen() && channel.isActive()) {
                        int packetSerialNum = PacketAnalyze.getPacketSerialNum();
                        PacketAnalyze.sendPacketMap.put(packetSerialNum, System.currentTimeMillis(), ExpirationPolicy.CREATED, packeExpiredTime, TimeUnit.SECONDS);
                        byte[] realTimePacket = VechileUtils.getTimingPacket(vin, packetSerialNum);
                        channel.writeAndFlush(realTimePacket);
                        String toHexString = ByteUtils.bytesToHexString(realTimePacket);
                        logger.info("[CHANNEL]" + "[" + channel.id().asShortText() + "][SENDED][NO." + packetSerialNum + "]->" + toHexString);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("数据发送异常");
        }
        count--;


    }
}
