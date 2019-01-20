package com.fdh.simulator.task;

import com.fdh.simulator.NettyChannelManager;
import com.fdh.simulator.ui.Simulator;
import com.fdh.simulator.packet.VechilePacket;
import com.fdh.simulator.utils.ByteUtils;
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
        Map<String, String> vinMap = Simulator.vinMap;
        ExpiringMap<String, Channel> expiringMap = NettyChannelManager.expiringMap;
        if (expiringMap.size() > 0) {
            Set<Map.Entry<String, Channel>> entries = expiringMap.entrySet();
            int i = 0;
            for (Map.Entry<String, Channel> entry : entries) {
                Channel channel = entry.getValue();
                String vin = vinMap.get(i);
                i++;
                byte[] realTimePacket = VechilePacket.getRealTimePacket(vin);
                ChannelFuture channelFuture = channel.writeAndFlush(realTimePacket);
                if (channelFuture.isSuccess()) {
                    String toHexString = ByteUtils.bytesToHexString(realTimePacket);
                    logger.info("[SEND]->" + toHexString);
                }
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
