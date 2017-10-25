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
public class UpdateVofficeDebitThread extends InventoryThread {

    private List<OptionSetValueDTO> lstDayDebit = Lists.newArrayList();

    public UpdateVofficeDebitThread(String name, int numActorLimit, int type) {
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
            reportError("------Bat dau chay tien trinh cap nhat DEBIT_REQUEST ");
            lstDayDebit = optionSetValueService.getByOptionSetCode(Const.VOFFICE_DOA.DAY_VOFFICE_DEBIT);
            if (DataUtil.isNullOrEmpty(lstDayDebit)) {
                reportError("------Chua cau hinh max day cho DEBIT_REQUEST ");
                return;
            }
            Long maxDay = DataUtil.safeToLong(lstDayDebit.get(0).getValue());
            List<StockTransVoffice> lstStockTransVofficeDebit = stockTransVofficeService.getLstVofficeSigned(maxDay, Const.DEBIT_REQUEST_PREFIX_TEMPLATE);
            for (StockTransVoffice stockTransVoffice : lstStockTransVofficeDebit) {
                try {
                    stockTransVofficeService.updateVofficeDebit(stockTransVoffice);
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
            reportError("------Ket thuc chay tien trinh cap nhat DEBIT_REQUEST ");
        } catch (Exception ex) {
            reportError(ex);
        }
    }

}
