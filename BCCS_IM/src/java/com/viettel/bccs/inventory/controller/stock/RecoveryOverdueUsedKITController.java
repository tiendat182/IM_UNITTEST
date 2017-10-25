package com.viettel.bccs.inventory.controller.stock;

import com.viettel.web.common.annotation.Security;
import com.viettel.web.common.controller.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

/**
 * Created by anhvv4 on 19/11/2015.
 */
@Component
@Scope("view")
@ManagedBean (name="recoveryOverdueUsedKITController")
public class RecoveryOverdueUsedKITController extends BaseController {
    private String typeInputSerial ="0";

    @Security("@")
    @PostConstruct
    public void init(){
        typeInputSerial = "0";
    }

    public String getTypeInputSerial() {
        return typeInputSerial;
    }

    public void setTypeInputSerial(String typeInputSerial) {
        this.typeInputSerial = typeInputSerial;
    }
}
