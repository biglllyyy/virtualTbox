package com.fdh.simulator.handler;

import com.fdh.simulator.NettyChannelManager;
import com.fdh.simulator.ui.Simulator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class simulatorHandler extends ChannelInboundHandlerAdapter {

	private Simulator printer;
	private static final Logger logger = LoggerFactory.getLogger(Simulator.class);

//	public simulatorHandler(Simulator printer) {
//		this.printer = printer;
//	}

    public simulatorHandler() {
    }

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        logger.info(ctx.channel().id()+"   say:"+(String)msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NettyChannelManager.removeChannel(ctx.channel());
	}


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        NettyChannelManager.putChannel(channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannelManager.removeChannel(ctx.channel());
    }
}
