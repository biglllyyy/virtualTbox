package com.fdh.simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class StartSimulator {

    private static final Logger logger = LoggerFactory.getLogger(StartSimulator.class);

    public static void main(String[] args) {
        //加载spring配置文件
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/spring-base.xml");
        ctx.start();
        logger.info("开始执行测试！");
    }
}
