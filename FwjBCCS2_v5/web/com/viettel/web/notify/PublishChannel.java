package com.viettel.web.notify;

/**
 * Created by vtsoft on 12/10/2014.
 */

import com.viettel.fw.common.util.Const;
import org.primefaces.push.annotation.*;
import org.primefaces.push.impl.JSONEncoder;

@PushEndpoint("/"+ Const.NOTIFY.CHANNEL)
@Singleton
public class PublishChannel {

    @OnMessage(encoders = {JSONEncoder.class})
    public Object onMessage(Object pushData) {
        return pushData;
    }

}