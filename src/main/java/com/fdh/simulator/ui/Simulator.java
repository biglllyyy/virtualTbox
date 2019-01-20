package com.fdh.simulator.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.fdh.simulator.NettyChannelManager;
import com.fdh.simulator.task.ScheduleTask;
import com.fdh.simulator.utils.PropertiesUtils;
import com.fdh.simulator.utils.SpringContextUtils;
import com.fdh.simulator.task.ConnectTask;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


public class Simulator {

    private ThreadPoolTaskExecutor taskExecutor;
    /**
     * 存储channelId和vin对应关系的map
     */
    public static ConcurrentHashMap<String, String> channnelVinMap = new ConcurrentHashMap<String, String>();
    PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    private static final Logger logger = LoggerFactory.getLogger(Simulator.class);

    /**
     * 初始化1000个VIN号
     */
    public Simulator() {
        taskExecutor = SpringContextUtils.getBean("taskExecutor");
        boolean config = initConfig();
        if(config){
            connect(address,port);
        }else {
            logger.error("初始化参数失败");
        }
    }

    private String address;
    private int port;
    private int sendInterval;
    private int testAvailableTime;
    private int tcpConnections;

    /***
     * 初始化配置
     */
    private boolean initConfig() {
        address = PropertiesUtils.getProperty("serverip");
        port = PropertiesUtils.getIntegerProperty("serverport");
        if (StringUtil.isNullOrEmpty(address)) {
            logger.error("ip或者端口为空");
        }
        sendInterval = PropertiesUtils.getIntegerProperty("send_interval");
        testAvailableTime = PropertiesUtils.getIntegerProperty("test_available_time");
        tcpConnections = PropertiesUtils.getIntegerProperty("tcp_connections");
        if(tcpConnections > 10000){
            logger.error("TCP连接数不能大于10000！");
            return false;
        }
        NettyChannelManager.test(testAvailableTime);
        return true;
    }

//    public void startControllPanel() {
//        new ControlPanel();
//    }

    public void connect(String inetHost, int inetPort) {
        //设置测试结束时间
        EventLoopGroup workgroup = new NioEventLoopGroup(1);
        BlockingQueue<Runnable> threads = new LinkedBlockingQueue<>();
        for (int i = 1; i <= tcpConnections; i++) {
            taskExecutor.submit(new ConnectTask(inetHost, inetPort, i, workgroup));
        }
        new Timer().schedule(new ScheduleTask(), 0,sendInterval);
    }

    public void close() {
        //所有的客户端连接全部断开
        NettyChannelManager.removeAllChannel();
    }

    public void addPropertyChangerListsener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    public void removePropertyChangerListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    public void firePropertyChangerListener(String prop, Object oldValue,
                                            Object newValue) {

        listeners.firePropertyChange(prop, oldValue, newValue);
    }
}
