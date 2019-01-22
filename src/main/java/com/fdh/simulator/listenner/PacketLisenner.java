package com.fdh.simulator.listenner;

import com.fdh.simulator.ExpirieListenner;
import com.fdh.simulator.NettyChannelManager;
import com.fdh.simulator.PacketAnalyze;
import com.fdh.simulator.Simulator;
import com.fdh.simulator.utils.ReportUtils;
import com.fdh.simulator.utils.SpringContextUtils;
import net.jodah.expiringmap.ExpirationListener;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fudh
 * @ClassNmme PacketLisenner
 * @date 2019/1/22 11:48
 * @Description: 发送的数据包过期策略
 */
public class PacketLisenner implements ExpirationListener<Integer, Long> {

    private static final Logger logger = LoggerFactory.getLogger(ExpirieListenner.class);

    @Override
    public void expired(Integer packetSerailNum, Long timestamp) {
        ExpiringMap<Integer, Long> sendPacketMap = PacketAnalyze.sendPacketMap;
//        int expiredCount = PacketAnalyze.expireSendCount.incrementAndGet();
//        int packetCount = PacketAnalyze.atomicLong.get();
//        logger.error("sendPacketMap->"+sendPacketMap.size());
//        logger.info("expiredCount->"+expiredCount+"------packetCount"+packetCount);
        if (sendPacketMap.size() == 0) {
            logger.error("***********************************************************测试完成***********************************************************");
            Simulator.timer.cancel();
            //断开所有的连接不在接收报文
            NettyChannelManager.removeAll();
            ReportUtils.report();
        }

    }
}
