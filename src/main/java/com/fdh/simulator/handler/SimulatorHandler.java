package com.fdh.simulator.handler;

import com.fdh.simulator.NettyChannelManager;
import com.fdh.simulator.PacketAnalyze;
import com.fdh.simulator.Simulator;
import com.fdh.simulator.utils.ByteUtils;
import com.fdh.simulator.utils.ReportUtils;
import com.fdh.simulator.utils.VechileUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulatorHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(Simulator.class);
    public SimulatorHandler() {
    }

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        byte[] bytes = (byte[]) msg;
        //解析报文获得报文序列号，计算响应时间
        int packetSerail = ByteUtils.getInt(bytes, 24);
        long receiveTimeMillis = System.currentTimeMillis();
        Long receiveTimeMillis1 = receiveTimeMillis;
        Long sendTimeMillis = PacketAnalyze.sendPacketMap.get(packetSerail);
        if(sendTimeMillis!=null){
            Integer diff =(int)(receiveTimeMillis1-sendTimeMillis);
            PacketAnalyze.receiveMap.put(packetSerail,diff);
            //防止长期占用内存过大，及时销毁
        }
        logger.info("[CHANNEL]" + "[" + ctx.channel().id().asShortText() + "][RECE][NO."+packetSerail+"]->" + ByteUtils.bytesToHexString(bytes));
        long activeChannelSize = NettyChannelManager.getActiveChannelSize();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NettyChannelManager.removeChannel(ctx.channel());
	}


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        NettyChannelManager.putChannel(channel);//保存channel
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannelManager.removeChannel(ctx.channel());
    }
}
