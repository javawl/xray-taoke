package com.xray.taoke.act.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.xray.act.exception.RtException;

public class TbErrInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(TbErrInterceptor.class);

    @Override
    public void intercept(Invocation inv) {
        Controller c = inv.getController();
        try {
            inv.invoke();
        } catch (Exception e) {
            this.doErrMsg(c, e);
        }
    }

    private void doErrMsg(Controller c, Exception e) {
        logger.error("errmsg", e);
        if (e instanceof RtException) {
            int code = ((RtException) e).getCode();
            if (Constant.para_err == code) {
                c.render("/html/tips_paraerr.html");
                return;
            } else if (Constant.data_err == code) {
                c.render("/html/tips_dataerr.html");
                return;
            }
        }
        c.render("/html/tips_neterr.html");
    }

}
