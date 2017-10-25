package com.viettel.bccs.processIM.process;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.model.StockTransVoffice;
import com.viettel.bccs.inventory.repo.StockTransVofficeRepo;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.service.StockTransVofficeService;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hoangnt14 on 8/9/2016.
 */
public class UpdateVofficeDeviceThread extends InventoryThread {

    public UpdateVofficeDeviceThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Autowired
    private StockTransVofficeService stockTransVofficeService;

    @Autowired
    private OptionSetValueService optionSetValueService;

    @Autowired
    private StockTransVofficeRepo repository;

    @Override
    public void execute() {
        try {
            reportError("------Bat dau chay tien trinh cap nhat phan ra thiet bi ");
            List<OptionSetValueDTO> lstDayDevice = optionSetValueService.getByOptionSetCode(Const.VOFFICE_DOA.DAY_VOFFICE_DEVICE);
            if (DataUtil.isNullOrEmpty(lstDayDevice)) {
                reportError("------Chua cau hinh max day cho tien trinh phan ra thiet bi ");
                return;
            }
            Long maxDay = DataUtil.safeToLong(lstDayDevice.get(0).getValue());
            List<StockTransVoffice> lstStockTransVofficeDOA = stockTransVofficeService.getLstVofficeSigned(maxDay, Const.STOCK_DEVICE_TEMPLATE);
            for (StockTransVoffice stockTransVoffice : lstStockTransVofficeDOA) {
                try {
                    stockTransVofficeService.updateVofficeDevice(stockTransVoffice);
                } catch (LogicException e) {
                    try {
                        StockTransVoffice stockTransVofficeError = repository.findOneById(stockTransVoffice.getStockTransVofficeId());
                        stockTransVofficeError.setErrorCode(e.getErrorCode());
                        stockTransVofficeError.setErrorCodeDescription(e.getDescription());
                        if (DataUtil.isNullOrZero(stockTransVofficeError.getRetry())) {
                            stockTransVofficeError.setRetry(1L);
                        } else {
                            stockTransVofficeError.setRetry(stockTransVofficeError.getRetry() + 1L);
                        }
                        repository.save(stockTransVofficeError);
                        reportError(e);
                    } catch (Exception el) {
                        reportError("Loi khi cap nhat retry cho voffice");
                    }
                } catch (Exception ex) {
                    reportError(ex);
                }
            }
            reportError("------Ket thuc chay tien trinh phan ra thiet bi ");
        } catch (Exception ex) {
            reportError(ex);
        }
    }

}
