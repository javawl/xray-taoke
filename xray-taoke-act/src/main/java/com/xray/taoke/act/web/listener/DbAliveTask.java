package com.xray.taoke.act.web.listener;

import java.util.TimerTask;

import com.xray.taoke.act.model.UoUser;

public class DbAliveTask extends TimerTask {

    @Override
    public void run() {
        UoUser.dao.queryRateByUserid("1");
    }

}
