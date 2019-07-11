package com.xray.taoke.admin.app.pdd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Enhancer;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.xray.act.service.RedisService;
import com.xray.act.service.RedisServiceFactory;
import com.xray.taoke.admin.service.WeixinApi;

public class AbstractApp extends Thread {
    protected static Logger logger = LoggerFactory.getLogger(AbstractApp.class);
    
    protected static RedisService cache = RedisServiceFactory.getDefaulInstance();
    protected static final Prop config = PropKit.use("config.properties");
    
    protected static final WeixinApi wxApi = Enhancer.enhance(WeixinApi.class);
}
