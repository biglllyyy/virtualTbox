package com.fdh.simulator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.fdh.simulator.model.Tbox;
import com.fdh.simulator.task.ScheduleTask;
import com.fdh.simulator.utils.ExcelUtils;
import com.fdh.simulator.utils.PropertiesUtils;
import com.fdh.simulator.utils.SpringContextUtils;
import com.fdh.simulator.task.ConnectTask;
import com.fdh.simulator.utils.VechileUtils;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


public class Simulator {

    private ThreadPoolTaskExecutor taskExecutor;
    public static Timer timer;
    private String address;
    private int port;
    private int sendInterval;
    private int tcpConnections;
    private TimerTask  timerTask;

    /**
     * 存储channelId和vin对应关系的map
     */
//    PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    private static final Logger logger = LoggerFactory.getLogger(Simulator.class);


    public Simulator() {
//        taskExecutor = SpringContextUtils.getBean("taskExecutor");
//        connect(address, port);
    }

    public void connect() {
        tcpConnections = VechileUtils.mlist.size();
        logger.info("tcpConnections:"+tcpConnections);
        EventLoopGroup workgroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors()*2+1);
        for (int i = 0; i < tcpConnections; i++) {
            new Thread(new ConnectTask(address,port,i,workgroup)).start();
        }
        /**
         * 处理实时报文发送，等待登陆完成立马发送数据
         */
        timer = new Timer();
        timer.schedule(timerTask, 0, sendInterval);
    }


//    public void addPropertyChangerListsener(PropertyChangeListener listener) {
//        listeners.addPropertyChangeListener(listener);
//    }
//
//    public void removePropertyChangerListener(PropertyChangeListener listener) {
//        listeners.removePropertyChangeListener(listener);
//    }
//
//    public void firePropertyChangerListener(String prop, Object oldValue,
//                                            Object newValue) {
//
//        listeners.firePropertyChange(prop, oldValue, newValue);
//    }


    public TimerTask getTimerTask() {
        return timerTask;
    }

    public void setTimerTask(TimerTask timerTask) {
        this.timerTask = timerTask;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSendInterval() {
        return sendInterval;
    }

    public void setSendInterval(int sendInterval) {
        this.sendInterval = sendInterval;
    }

    public int getTcpConnections() {
        return tcpConnections;
    }

    public void setTcpConnections(int tcpConnections) {
        this.tcpConnections = tcpConnections;
    }
}
