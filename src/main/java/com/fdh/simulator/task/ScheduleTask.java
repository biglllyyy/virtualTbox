package com.fdh.simulator.task;

import com.fdh.simulator.NettyChannelManager;
import com.fdh.simulator.PacketAnalyze;
import com.fdh.simulator.utils.ByteUtils;
import com.fdh.simulator.utils.VechileUtils;
import io.netty.channel.Channel;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

/***
 * 定时任务
 */
public class ScheduleTask extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    @Override
    public void run() {

        try {
            ExpiringMap<String, Channel> expiringMap = NettyChannelManager.expiringMap;
            if (expiringMap.size() > 0) {
                Set<Map.Entry<String, Channel>> entries = expiringMap.entrySet();
                for (Map.Entry<String, Channel> entry : entries) {
                    Channel channel = entry.getValue();
                    if(channel.isOpen() && channel.isActive()){
                        int packetSerialNum = PacketAnalyze.getPacketSerialNum();
                        PacketAnalyze.sendPacketMap.put(packetSerialNum,System.currentTimeMillis());
                        byte[] realTimePacket = VechileUtils.getTimingPacket(channel, packetSerialNum);
                        channel.writeAndFlush(realTimePacket);
                        String toHexString = ByteUtils.bytesToHexString(realTimePacket);
                        logger.info("[CHANNEL]" + "[" + channel.id().asShortText() + "][SENDED][NO."+packetSerialNum+"]->" + toHexString);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("数据发送异常");
        }

    }
}
