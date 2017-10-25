package com.viettel.bccs.inventory.controller.stock;

import com.viettel.web.common.annotation.Security;
import com.viettel.web.common.controller.BaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

/**
 * Created by anhvv4 on 18/11/2015.
 */
@Component
@Scope("view")
@ManagedBean (name ="trackActualWarehouseController")
public class TrackActualWarehouseController extends BaseController {
    private String stockSerial;
    private boolean beforSearch;

    @PostConstruct
    @Security("@")
    public void init (){
        beforSearch = false;
        stockSerial = "";
    }

    @Security("@")
    public void search(){
        beforSearch = true;
    }

    public boolean isBeforSearch() {
        return beforSearch;
    }

    public void setBeforSearch(boolean beforSearch) {
        this.beforSearch = beforSearch;
    }

    public String getStockSerial() {
        return stockSerial;
    }

    public void setStockSerial(String stockSerial) {
        this.stockSerial = stockSerial;
    }
}
