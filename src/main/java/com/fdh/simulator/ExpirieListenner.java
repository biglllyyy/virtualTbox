package com.fdh.simulator;

import com.fdh.simulator.ui.Simulator;
import com.fdh.simulator.utils.ReportUtils;
import io.netty.channel.Channel;
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

        //过期时间到所有的entry
        try {
            //取消sing是任务
            if (Simulator.bisRuning) {
                Simulator.timer.cancel();
            }
            channel.close();
            logger.info("[channel]" + "[" + channel.id() + "]" + "[已经断开]");
            long activeChannelSize = NettyChannelManager.getActiveChannelSize();
            if (activeChannelSize == 0) {
                logger.info("测试完成");
                ReportUtils.report();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

        } finally {
            NettyChannelManager.removeChannel(channel);
        }
    }
}
