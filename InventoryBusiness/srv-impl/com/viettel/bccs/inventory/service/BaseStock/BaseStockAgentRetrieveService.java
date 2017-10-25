package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.ProductOfferPriceService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by hoangnt14 on 1/14/2016.
 */
@Service
public class BaseStockAgentRetrieveService extends BaseStockService {

    public static final Logger logger = Logger.getLogger(BaseStockAgentExpService.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private BaseValidateService baseValidateService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductOfferPriceService productOfferPriceService;
    @Autowired
    private ProductOfferingService productOfferingService;

    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {
        stockTransDTO.setNote(stockTransDTO.getNote() != null ? stockTransDTO.getNote().trim() : null);
        stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        stockTransDTO.setCreateDatetime(getSysDate(em));
        stockTransDTO.setStockTransType(Const.STOCK_TRANS_TYPE.AGENT);
        //
        stockTransActionDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        //Tru so luong dap ung kho xuat
        flagStockDTO.setExportStock(true);
        flagStockDTO.setExpAvailableQuantity(Const.STOCK_TOTAL.MINUS_QUANTITY);
        //
        flagStockDTO.setPrefixActionCode(Const.STOCK_TRANS.TRANS_CODE_PN);
    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {
        //Validate cac truong trong giao dich
        if (!DataUtil.isNullOrEmpty(stockTransActionDTO.getActionCode()) && stockTransActionDTO.getActionCode().getBytes("UTF-8").length > 50) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.actionCode.retrieve.overLength");
        }
        //5. kiem tra ghi chu co thoa man khong
        if (!DataUtil.isNullOrEmpty(stockTransDTO.getNote()) && stockTransDTO.getNote().getBytes("UTF-8").length > 500) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.note.overLength");
        }
        //Validate han muc kho nhan
        baseValidateService.doDebitValidate(stockTransDTO, lstStockTransDetail);
        //validate hang hoa ko vuot qua tong han muc kho xuat--thaont19
        baseValidateService.doQuantityAvailableValidate(stockTransDTO, lstStockTransDetail);
        // validate cho dai ly
        validateAgentExp(stockTransDTO, lstStockTransDetail);
    }

    private void validateAgentExp(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

        List<String> lstProductOfferingCode = Lists.newArrayList();
        if (DataUtil.isNullOrEmpty(lstStockTransDetail)) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "validate.agent.retrieve.order.product");
        }
        for (StockTransDetailDTO stockTransDetailDTO : lstStockTransDetail) {
            ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(stockTransDetailDTO.getProdOfferId());
            if (lstProductOfferingCode.contains(productOfferingDTO.getCode())) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescueInformation.validate.list.duplicate");
            }
            lstProductOfferingCode.add(productOfferingDTO.getCode());
            if (DataUtil.isNullObject(productOfferingDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_VALIDATE_INPUT, "stock.rescueInformation.required.stock.exist");
            }
        }
    }

    public void doUnlockUser(StockTransDTO stockTransDTO) throws Exception {
    }

    public void doSaveStockTransSerial(StockTransDTO stockTransDTO, List<StockTransDetailDTO> lstStockTransDetail) throws Exception {
    }
}