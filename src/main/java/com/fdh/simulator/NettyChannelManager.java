/*
 * 文件名： NettyChannelManager.java
 *
 * 创建日期： 2017年1月6日
 *
 * Copyright(C) 2017, by <a href="mailto:liws@xingyuanauto.com">liws</a>.
 *
 * 原始作者: liws
 *
 */
package com.fdh.simulator;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.fdh.simulator.task.ConnectTask;
import io.netty.channel.ChannelFuture;
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

    private static final Logger logger = LoggerFactory.getLogger(ConnectTask.class);
    /**
     * 过期时间，单位秒
     */
    public static int expireTime = 30;

    public static int getExpireTime() {
        return expireTime;
    }

    public static ExpiringMap<String, Channel> expiringMap;

    /***
     * 此方法只有在变量过期的时候才能调用，否则报错
     * @param expireTime
     */
    public static void setExpireTime(int expireTime) {
        expiringMap.setExpiration(expireTime, TimeUnit.MINUTES);
    }

    public static void test(int expireTime) {
        expiringMap = ExpiringMap.builder()
                .expiration(expireTime, TimeUnit.MINUTES)
                .expirationListener(new ExpirieListenner())
                .build();
    }


    /**
     * 端口所有的链接
     *
     * @param
     */
    public static void removeAllChannel() {

        Set<Entry<String, Channel>> entries = expiringMap.entrySet();
        for (Entry<String, Channel> entry : entries) {
            Channel channel = entry.getValue();
            String channelId = entry.getKey();
            if (channel.isOpen()) {
                ChannelFuture channelFuture = channel.close();
                if (channelFuture.isSuccess()) {
                    logger.info("[channel]" + "[" + channel.id() + "]" + "[已经断开]");
                }
            }

        }
    }

    /**
     * 添加通道
     *
     * @param channel
     */
    public static void putChannel(Channel channel) {
        expiringMap.put(channel.id().asLongText(), channel);
    }

    public static void removeChannel(Channel channel) {
        expiringMap.remove(channel.id().asLongText());
    }


    public static void setExpireTime() {
        expiringMap.setExpiration(expireTime, TimeUnit.SECONDS);
    }

    public static long getActiveChannelSize() {
        return expiringMap.size();
    }
}
