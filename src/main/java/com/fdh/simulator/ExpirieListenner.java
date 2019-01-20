package com.fdh.simulator;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import net.jodah.expiringmap.ExpirationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author fudh
 * @ClassNmme ExpirieListenner
 * @date 2019/1/19 16:05
 * @Description: TODO
 */
public class ExpirieListenner implements ExpirationListener<String, Channel> {


    private static final Logger logger = LoggerFactory.getLogger(ExpirieListenner.class);

    @Override
    public void expired(String channelId, Channel channel) {

        //过期时间到所有的map
        try {
            ChannelFuture channelFuture = channel.close();
            if (channelFuture.isSuccess()) {
                logger.info("[channel]" + "[" + channel.id() + "]" + "[已经断开]");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

        } finally {
            NettyChannelManager.removeChannel(channel);
        }
    }
}
