package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.voffice.vo.ResultObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

/**
 * Created by hoangnt14 on 11/28/2016.
 */
@Service
public class HandleVOfficeResultBaseService extends BaseServiceImpl {

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    public static String ERR_VALIDATE = "-1";
    public static String SUCCESS = "0";

    @Autowired
    private StockTransVofficeService stockTransVofficeService;

    @Autowired
    private StockTransActionService stockTransActionService;

    @Autowired
    private StockTransService stockTransService;

    @Transactional(rollbackFor = Exception.class)
    public BaseMessage returnSignResult(ResultObj resultObj) throws Exception {
        BaseMessage baseMessage = new BaseMessage(false);
        Date currentDate = getSysDate(em);
        StockTransVofficeDTO stockTransVofficeDTO = stockTransVofficeService.findStockTransVofficeByRequestId(resultObj.getTransCode());
        if (DataUtil.isNullObject(stockTransVofficeDTO)) {
            baseMessage.setErrorCode(ERR_VALIDATE);
            baseMessage.setDescription(getText("handle.voffice.transCode.not.found"));
            return baseMessage;
        }
        if (DataUtil.safeEqual(resultObj.getSignStatus(), Const.VOFFICE_STATUS.VOFFICE_SIGNED)) {
            stockTransVofficeDTO.setStatus(Const.VOFFICE_STATUS.SIGNED);
            stockTransVofficeDTO.setNote(resultObj.getSignComment());
            stockTransVofficeDTO.setLastModify(currentDate);
            stockTransVofficeService.save(stockTransVofficeDTO);
            baseMessage = new BaseMessage(true);
            baseMessage.setErrorCode(SUCCESS);
            baseMessage.setDescription(getText("handle.voffice.success"));
            return baseMessage;
        }
        //Neu tu choi
        stockTransVofficeDTO.setStatus(Const.VOFFICE_STATUS.REJECT);
        stockTransVofficeDTO.setNote(resultObj.getSignComment());
        stockTransVofficeDTO.setDeniedDate(currentDate);
        stockTransVofficeDTO.setDeniedUser(resultObj.getLastSignEmail());
        stockTransVofficeDTO.setLastModify(currentDate);
        //cap nhat lai trang thai lenh va phieu

        StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(stockTransVofficeDTO.getStockTransActionId());
        if (!DataUtil.isNullObject(stockTransActionDTO)) {
            StockTransDTO stockTransDTO = stockTransService.findOne(stockTransActionDTO.getStockTransId());
            StockTransDTO stockTransRegion = null;
            if (!DataUtil.isNullObject(stockTransDTO) && !DataUtil.isNullOrZero(stockTransDTO.getRegionStockTransId())) {
                stockTransRegion = stockTransService.findOne(stockTransDTO.getRegionStockTransId());
            }
            if (DataUtil.safeEqual(stockTransActionDTO.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_ORDER)
                    || DataUtil.safeEqual(stockTransActionDTO.getStatus(), Const.STOCK_TRANS_STATUS.IMPORT_ORDER)) {
                //bo ky vOffice neu la lenh
                stockTransActionDTO.setSignCaStatus(Const.VOFFICE_STATUS.NOT_SIGNED);
                stockTransActionService.save(stockTransActionDTO);
                //neu la giao dich dieu chuyen --> Xoa bo ky voffice lenh dieu chuyen
                if (!DataUtil.isNullObject(stockTransRegion) && DataUtil.safeEqual(stockTransRegion.getIsAutoGen(), Const.STOCK_TRANS.IS_TRANSFER)) {
                    StockTransActionDTO stockTransActionExchange = stockTransActionService.getStockTransActionByIdAndStatus(stockTransRegion.getStockTransId(),
                            Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER));
                    stockTransActionExchange.setSignCaStatus(Const.VOFFICE_STATUS.NOT_SIGNED);
                    stockTransActionService.save(stockTransActionExchange);
                }
            } else if (DataUtil.safeEqual(stockTransActionDTO.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)
                    || DataUtil.safeEqual(stockTransActionDTO.getStatus(), Const.STOCK_TRANS_STATUS.IMPORT_NOTE)) {
                //neu la phieu
                StockTransActionDTO stockTransOrder = stockTransActionService.getStockTransActionByIdAndStatus(stockTransActionDTO.getStockTransId(),
                        Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_ORDER, Const.STOCK_TRANS_STATUS.IMPORT_ORDER));
                if (DataUtil.isNullObject(stockTransOrder)) {
                    //phieu khong xuat phat tu lenh
                    stockTransActionDTO.setSignCaStatus(Const.VOFFICE_STATUS.NOT_SIGNED);
                    stockTransActionService.save(stockTransActionDTO);
                    baseMessage = new BaseMessage(true);
                    baseMessage.setErrorCode(SUCCESS);
                    baseMessage.setDescription(getText("handle.voffice.success"));
                    return baseMessage;
                }
                //phieu xuat phat tu lenh
                if (!DataUtil.isNullObject(stockTransRegion) && !DataUtil.isNullOrZero(stockTransRegion.getIsAutoGen())) {
                    stockTransActionService.deleteStockTransActionByIdAndStatus(stockTransRegion.getStockTransId(),
                            Lists.newArrayList(Const.STOCK_TRANS_STATUS.EXPORT_NOTE, Const.STOCK_TRANS_STATUS.IMPORT_NOTE));
                    //Neu la giao dich 3 mien
                    if (DataUtil.safeEqual(stockTransRegion.getIsAutoGen(), Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION)) {
                        //cap nhat stock_trans ve trang thai lap lenh
                        stockTransRegion.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
                    } else {
                        stockTransRegion.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
                    }
                    stockTransService.save(stockTransRegion);
                }
                //Xoa phieu
                stockTransActionService.deleteStockTransAction(stockTransActionDTO.getStockTransActionId());
                //Cap nhat giao dich ve lap lenh
                if (DataUtil.safeEqual(stockTransDTO.getStatus(), Const.STOCK_TRANS_STATUS.EXPORT_NOTE)) {
                    stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
                } else {
                    stockTransDTO.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
                }
                stockTransService.save(stockTransDTO);
            }
        }
        stockTransVofficeService.save(stockTransVofficeDTO);
        baseMessage = new BaseMessage(true);
        baseMessage.setErrorCode(SUCCESS);
        baseMessage.setDescription(getText("handle.voffice.success"));
        return baseMessage;
    }
}
