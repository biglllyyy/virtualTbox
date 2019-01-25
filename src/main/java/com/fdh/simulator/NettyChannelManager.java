package com.fdh.simulator;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.fdh.simulator.utils.VechileUtils;
import io.netty.channel.Channel;

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
     * 已经建立连接的chanelId和channel的对应关系
     */
    public static ConcurrentHashMap<String, Channel>  channnelMap = new ConcurrentHashMap<String, Channel>();

    /**
     * 已经登陆的channel,chanelId和channel的对应关系
     */
    public  static ConcurrentHashMap<String, Channel> loginChanel = new ConcurrentHashMap<>();

    /**
     * 连接建立后注册通道
     * @param channel
     */
    public static void putChannel(Channel channel) {
        channnelMap.put(channel.id().asLongText(),channel);
        channnelVinMap.put(channel.id().asLongText(), VechileUtils.getVin());
    }
    public static void removeChannel(Channel channel) {
        String chanelId = channel.id().asLongText();
        channnelMap.remove(chanelId);
        channnelVinMap.remove(chanelId);
    }

    /**
     * 将已经登陆channel添加到map中
     * @param channel
     */
    public static void putLoginChannel(Channel channel) {
        loginChanel.put(channel.id().asLongText(), channel);
    }

    /**
     * 移除已经登陆的channel
     * @param channel
     */
    public static void removeLoginChannel(Channel channel) {
        channnelVinMap.remove(channel.id().asLongText());
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

    public static ConcurrentHashMap<String, Channel> getLoginChannnelMap() {
        return loginChanel;
    }


    public static void setChannnelMap(ConcurrentHashMap<String, Channel> channnelMap) {
        NettyChannelManager.channnelMap = channnelMap;
    }
}
