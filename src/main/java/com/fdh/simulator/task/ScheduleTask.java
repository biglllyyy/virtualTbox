package com.fdh.simulator.task;

import com.fdh.simulator.NettyChannelManager;
import com.fdh.simulator.PacketAnalyze;
import com.fdh.simulator.ui.Simulator;
import com.fdh.simulator.utils.ByteUtils;
import com.fdh.simulator.utils.VechileUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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

        ExpiringMap<String, Channel> expiringMap = NettyChannelManager.expiringMap;
        if (expiringMap.size() > 0) {
            Set<Map.Entry<String, Channel>> entries = expiringMap.entrySet();
            int i = 0;
            for (Map.Entry<String, Channel> entry : entries) {
//                Channel channel = entry.getValue();
                int packetSerialNum = PacketAnalyze.getPacketSerialNum();
                PacketAnalyze.sendPacketMap.put(packetSerialNum,System.currentTimeMillis());
//                byte[] realTimePacket = VechileUtils.getRealTimePacket(channel, packetSerialNum);
//                channel.writeAndFlush(realTimePacket);
//                String toHexString = ByteUtils.bytesToHexString(realTimePacket);
//                logger.info("[CHANNEL]" + "[" + channel.id().asShortText() + "][SENDED]->" + toHexString);
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
