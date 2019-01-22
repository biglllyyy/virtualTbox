package com.fdh.simulator;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.fdh.simulator.task.ConnectTask;
import com.fdh.simulator.utils.VechileUtils;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server端 Channel管理器
 *
 * @author <a href="mailto:liws@xingyuanauto.com">liws</a>
 * @version $Revision$
 * @since 2017年1月6日
 */
public class NettyChannelManager {

    /**
     * 存放channelId和Vin的对应关系
     */
    public static ConcurrentHashMap<String, String> channnelVinMap = new ConcurrentHashMap<String, String>();

    /***
     * chanelId和channel的对应关系
     */
    public static ConcurrentHashMap<String, Channel>  channnelMap = new ConcurrentHashMap<String, Channel>();

    /**
     * 添加通道
     * @param channel
     */
    public static void putChannel(Channel channel) {
        channnelMap.put(channel.id().asLongText(),channel);
        channnelVinMap.put(channel.id().asLongText(),VechileUtils.getVin());
    }
    public static void removeChannel(Channel channel) {
        String chanelId = channel.id().asLongText();
        channnelMap.remove(chanelId);
        channnelVinMap.remove(chanelId);
    }

    public  static  void removeAll(){

        Set<Map.Entry<String, Channel>> entries = channnelMap.entrySet();
        for (Map.Entry<String, Channel> entry : entries) {
            Channel channel = entry.getValue();
            channel.close();
        }
    }


    public static long getActiveChannelSize() {
        return channnelMap.size();
    }

    public static ConcurrentHashMap<String, String> getChannnelVinMap() {
        return channnelVinMap;
    }

    public static void setChannnelVinMap(ConcurrentHashMap<String, String> channnelVinMap) {
        NettyChannelManager.channnelVinMap = channnelVinMap;
    }

    public static ConcurrentHashMap<String, Channel> getChannnelMap() {
        return channnelMap;
    }

    public static void setChannnelMap(ConcurrentHashMap<String, Channel> channnelMap) {
        NettyChannelManager.channnelMap = channnelMap;
    }
}
