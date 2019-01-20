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
    public static int expireTime = 6000;

    public static ExpiringMap<String, Channel> expiringMap = ExpiringMap.builder()
            .expiration(expireTime,TimeUnit.SECONDS)
            .expirationListener(new ExpirieListenner())
            .build();
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
                    logger.info("[channel]"+"["+channel.id()+"]"+"[已经断开]");
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
        expiringMap.putIfAbsent(channel.id().asLongText(), channel);
//        logger.info("[当前连接数]->" + "[" + expiringMap.size() + "]");
    }

    public static void removeChannel(Channel channel) {

        expiringMap.remove(channel.id().asLongText());
        logger.info("[channel]"+"["+channel.id()+"]"+"[已经断开]");

    }

    public static long getActiveChannelSize() {
        return expiringMap.size();
    }
}
