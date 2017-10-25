package com.viettel.bccs.processIM.process;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.bccs.inventory.service.StockTransVofficeService;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luannt23.
 * @comment
 * @date 3/4/2016.
 */
@Service
@Scope("prototype")
@Lazy
public class SignVofficeThreadExecutor extends InventoryThread {

    private Long maxRetry;
    private Long maxDay;
    private Long numberThread;
    private List<Long> allowList;

    @Autowired
    private StockTransVofficeService stockTransVofficeService;

    public SignVofficeThreadExecutor(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    public SignVofficeThreadExecutor(String name, int numActorLimit, int type, Long maxRetry, Long maxDay, Long numberThread, List<Long> allowList) {
        super(name, numActorLimit, type);
        this.maxRetry = maxRetry;
        this.maxDay = maxDay;
        this.numberThread = numberThread;
        this.allowList = allowList;
    }

    @Override
    public void execute() {
        try {
            StockTransVofficeDTO stockTransVofficeSearch = new StockTransVofficeDTO(maxDay, maxRetry, allowList, numberThread);
            stockTransVofficeSearch.setPrefixTemplate(Const.DEBIT_REQUEST_PREFIX_TEMPLATE);
//            stockTransVofficeSearch.setPrefixTemplate(Const.GOODS_REVOKE_PREFIX_TEMPLATE);
            List<StockTransVofficeDTO> stockTransVofficeDTOs =
                    DataUtil.defaultIfNull(stockTransVofficeService.searchStockTransVoffice(stockTransVofficeSearch), Lists.newArrayList());
            for (StockTransVofficeDTO stockTransVofficeDTO : stockTransVofficeDTOs) {
                reportError("trinh ky: " + stockTransVofficeDTO.getActionCode());
                stockTransVofficeService.doSignVOffice(stockTransVofficeDTO);
            }
        } catch (Exception ex) {
            reportError(ex);
        }
    }

    public Long getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(Long maxRetry) {
        this.maxRetry = maxRetry;
    }

    public Long getMaxDay() {
        return maxDay;
    }

    public void setMaxDay(Long maxDay) {
        this.maxDay = maxDay;
    }


    public Long getNumberThread() {
        return numberThread;
    }

    public void setNumberThread(Long numberThread) {
        this.numberThread = numberThread;
    }


    public List<Long> getAllowList() {
        return allowList;
    }

    public void setAllowList(List<Long> allowList) {
        this.allowList = allowList;
    }
}
