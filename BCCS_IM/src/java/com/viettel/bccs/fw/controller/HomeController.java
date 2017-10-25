package com.viettel.bccs.fw.controller;

import com.viettel.web.common.controller.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.bean.ManagedBean;

/**
 * Created by phuvk on 6/5/2015.
 */

@ManagedBean
@Component
@Scope("view")
public class HomeController extends BaseController {

    public void init() {
        try {
           // BccsLoginSuccessHandler.getStaffDTO();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
