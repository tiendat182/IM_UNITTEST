package com.viettel.bccs.processIM.process;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.model.ShopIm1;
import com.viettel.bccs.im1.service.ShopIm1Service;
import com.viettel.bccs.inventory.service.StockDeliverService;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hoangnt14 on 1/24/2017.
 */
public class DeleteStockThread extends InventoryThread {

    @Autowired
    private StockDeliverService stockDeliverService;

    @Autowired
    private ShopIm1Service shopIm1Service;

    public DeleteStockThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            List<Long> lstShopId = stockDeliverService.getAllStockForDelete();
            if (!DataUtil.isNullOrEmpty(lstShopId)) {
                reportError("Bat dau xoa kho voi size : " + lstShopId.size());
                List<Long> lstCommit = Lists.newArrayList();
                for (int i = 0; i < lstShopId.size(); i++) {
                    lstCommit.add(lstShopId.get(i));
                    if (lstCommit.size() == 999) {
                        shopIm1Service.deleteStock(lstCommit);
                        lstCommit = Lists.newArrayList();
                    }
                }
                shopIm1Service.deleteStock(lstCommit);
                reportError("Ket thuc xoa kho voi size : " + lstShopId.size());
            }
        } catch (Exception ex) {
            reportError(ex);
        }
    }
}
