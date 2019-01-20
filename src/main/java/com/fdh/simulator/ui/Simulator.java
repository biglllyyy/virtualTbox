package com.fdh.simulator.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.fdh.simulator.NettyChannelManager;
import com.fdh.simulator.utils.SpringContextUtils;
import com.fdh.simulator.utils.Utils;
import com.fdh.simulator.task.ConnectTask;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


public class Simulator {

    private ThreadPoolTaskExecutor taskExecutor;
    public static Map<String, String> vinMap = new HashMap<>();
    private boolean isconnected = false;
    PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    private static final Logger logger = LoggerFactory.getLogger(Simulator.class);

    /**
     * 初始化1000个VIN号
     */
    public Simulator() {
        taskExecutor = SpringContextUtils.getBean("taskExecutor");
        String suffix = "LNBSCB3FXJM";
        Integer start = 100000;
        for (int i = 0; i < 100000; i++) {
            int endfix = start + 1;
            vinMap.put(i + "", suffix + endfix);
        }
    }
    /**
     * 心跳时间秒
     */
    private int heartBeat = 0;

    public int getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(int heartBeat) {
        this.heartBeat = heartBeat;
    }

    public void startControllPanel() {
        new  ControlPanel();
    }

    public boolean connect(String inetHost, int inetPort) {

        if (isconnected) {
            return true;
        }
        EventLoopGroup workgroup = new NioEventLoopGroup(1);
        BlockingQueue<Runnable> threads = new LinkedBlockingQueue<>();
        int num = 1000;
        for (int i = 1; i <= num; i++) {
            taskExecutor.submit(new ConnectTask(inetHost, inetPort, i, workgroup));
        }
        return isconnected = true;
    }

    public boolean isIsconnected() {
        return isconnected;
    }

    public void setIsconnected(boolean isconnected) {
        this.isconnected = isconnected;
    }

    public void close() {
        //所有的客户端连接全部断开
        NettyChannelManager.removeAllChannel();
        isconnected = false;
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
