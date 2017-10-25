package com.viettel.web.notify;

import com.viettel.fw.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Arrays;
import java.util.List;

/**
 * Created by thiendn1 on 21/12/2015.
 */
@Component
@ManagedBean
public class NotifyResource {

    private long growlLife = 600000;
    private boolean runNotify;
    private String pushServer;
    private List<String> pushs;
    @Autowired
    SystemConfig systemConfig;

    @PostConstruct
    public void init(){
        runNotify = systemConfig.IS_RUN_NOTIFY;
        pushServer = systemConfig.PUSH_SERVER;
        growlLife = systemConfig.GROW_LIFE;
        if(runNotify){
            String[] pushArrays = pushServer.split(";");
            pushs = Arrays.asList(pushArrays);
        }
    }

    public long getGrowlLife() {
        return growlLife;
    }

    public boolean isRunNotify() {
        return runNotify;
    }


    public List<String> getPushs() {
        return pushs;
    }
}
