package com.fdh.simulator.handler;

import com.fdh.simulator.NettyChannelManager;
import com.fdh.simulator.PacketAnalyze;
import com.fdh.simulator.Simulator;
import com.fdh.simulator.constant.CommandTag;
import com.fdh.simulator.task.SendDataTask;
import com.fdh.simulator.utils.ByteUtils;
import com.fdh.simulator.utils.SpringContextUtils;
import com.fdh.simulator.utils.VechileUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class SimulatorHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(Simulator.class);

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 连接数
     */
    private int connections;

    public SimulatorHandler() {
        threadPoolTaskExecutor = SpringContextUtils.getBean("taskExecutor");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        byte[] bytes = (byte[]) msg;

        //解析注册是否成功
//        if (bytes != null && bytes.length > 15) {
//            if ((bytes[2]+256) == 0xDC) {
//                String vin = VechileUtils.parseByte2Vin(bytes);
//                if (bytes[3] == 0x01) {
//                    logger.info("[VIN][" + vin + "][" + "注册成功]");
//                } else {
//                    logger.info("[VIN][" + vin + "][" + "注册失败]");
//                }
//            }
//        }
        long packetSerail = ByteUtils.getLong(bytes, 24);
        //解析车辆登入是否成功
        if (bytes[2] == 0x01) {//登陆数据
            String vin = VechileUtils.parseByte2Vin(bytes);
            if (bytes[3] == 0x01) {
                logger.info("[VIN][" + vin + "][" + "登陆成功]");
                Channel channel = ctx.channel();
                NettyChannelManager.putLoginChannel(channel);//保存channel
            } else {
                logger.info("[VIN][" + vin + "][" + "登陆失败]");
                //移除发登陆使用的连接
                NettyChannelManager.removeChannel(ctx.channel());
            }
        } else if (bytes[2] == 0x02) {//实时数据
            //解析报文获得报文序列号，计算响应时间
            long receiveTimeMillis = System.currentTimeMillis();
            Long receiveTimeMillis1 = receiveTimeMillis;
            Long sendTimeMillis = PacketAnalyze.sendPacketMap.get(packetSerail);
            if (sendTimeMillis != null) {
                Integer diff = (int) (receiveTimeMillis1 - sendTimeMillis);
                PacketAnalyze.receiveMap.put(packetSerail, diff);
            }
        }
        logger.info("[CHANNEL]" + "[" + ctx.channel().id().asShortText() + "][RECE][NO." + packetSerail + "]->" + ByteUtils.bytesToHexString(bytes));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NettyChannelManager.removeChannel(ctx.channel());
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        NettyChannelManager.putChannel(channel);//保存channel
        //连接一旦建立立即登陆
        threadPoolTaskExecutor.execute(new SendDataTask(channel, CommandTag.VEHICLE_LOGIN,0));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannelManager.removeChannel(ctx.channel());
    }
}
