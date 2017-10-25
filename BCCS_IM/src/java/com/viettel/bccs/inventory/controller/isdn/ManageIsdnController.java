package com.viettel.bccs.inventory.controller.isdn;

import com.viettel.web.common.controller.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

/**
 * Created by anhvv4 on 20/11/2015.
 */
@Component
@Scope("view")
@ManagedBean( name ="manageIsdn")
public class ManageIsdnController extends BaseController {

    private boolean renderDetailTrans;

    @Secured("@")
    @PostConstruct
    public void init(){
        renderDetailTrans = false;
    }

    @Secured("@")
    public void viewDetail(){
        renderDetailTrans = true;
    }

    public boolean isRenderDetailTrans() {
        return renderDetailTrans;
    }

    public void setRenderDetailTrans(boolean renderDetailTrans) {
        this.renderDetailTrans = renderDetailTrans;
    }
}
