package com.xray.taoke.act.web.listener;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleListener.class);
    private static final Timer timer = new Timer();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        timer.schedule(new DbAliveTask(), 60000, 5000);
        logger.info("listener.DbAliveTask");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        timer.cancel();
    }

}
