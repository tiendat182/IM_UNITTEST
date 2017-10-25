package com.viettel.bccs.inventory.controller.stock;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;


@Component
@Scope("view")
@ManagedBean(name = "revokeStockKitController")
public class RevokeStockKitController extends ExportFileStockTransController {


    @PostConstruct
    public void init() {
    }


}
