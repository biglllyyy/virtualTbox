package com.fdh.simulator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

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
        // TODO: 2019/1/22 在每个连接处理还是总的workGroup
        EventLoopGroup workgroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors()*2);
//        for (int i = 1; i <= tcpConnections; i++) {
//            taskExecutor.submit(new ConnectTask(address, port, i, workgroup));
//        }
        for (int i = 0; i < tcpConnections; i++) {
            new Thread(new ConnectTask(address,port,i,workgroup)).start();
        }
        /***
         * 适当延时等待连接完成
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
