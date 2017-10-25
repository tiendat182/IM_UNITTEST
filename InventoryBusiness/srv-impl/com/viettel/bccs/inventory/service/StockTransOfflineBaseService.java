package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 10/20/2016.
 */
@Service
public class StockTransOfflineBaseService extends BaseStockService {

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private StockTransActionService stockTransActionService;

    @Autowired
    private PartnerContractService partnerContractService;

    @Autowired
    private StockTransOfflineService stockTransOfflineService;


    public void processExpStockOffine(StockTrans stockTrans) throws Exception, LogicException {

        Date currentDate = getSysDate(em);
        SendSmsDTO sendSmsDTO = new SendSmsDTO();
        sendSmsDTO.setStartTime(currentDate);
        BaseMessage result;
        //
        List<String> lstStatus = Lists.newArrayList();
        lstStatus.add(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
        lstStatus.add(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
        Long stockTransId = stockTrans.getStockTransId();
        StockTransActionDTO stockTransActionDTO = stockTransActionService.getStockTransActionByIdAndStatus(stockTransId, lstStatus);
        //Neu la giao dich nhap
        if (DataUtil.safeEqual(stockTransActionDTO.getStatus(), Const.STOCK_TRANS_STATUS.IMPORT_NOTE)) {
            PartnerContractDTO partnerContractDTO = partnerContractService.findByStockTransID(stockTransId);
            if (!DataUtil.isNullObject(partnerContractDTO)) {
                //Nhap kho tu doi tac
            } else {

            }
        }


    }

    public BaseMessage ImportStock(StockTrans stockTrans, StockTransActionDTO stockTransActionDTO, SendSmsDTO sendSmsDTO) throws Exception, LogicException {
        //Lay thong tin giao dich offline
        StockTransOfflineDTO stockTransOfflineDTO = stockTransOfflineService.getStockTransOfflineById(stockTrans.getStockTransId());
        if (DataUtil.isNullObject(stockTransOfflineDTO)) {
            return new BaseMessage(false, "", "thread.exp.stock.offline.not.found.stock.trans.offline");
        }
        sendSmsDTO.setRealTransUserId(stockTransOfflineDTO.getRealTransUserId());
        sendSmsDTO.setStockTransType(stockTrans.getStockTransType());
        sendSmsDTO.setStockTransStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
        sendSmsDTO.setStockTrans(stockTrans);
        sendSmsDTO.setActionCode(stockTransActionDTO.getActionCode());
        //
        
        return null;
    }


    @Override
    public void doPrepareData(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, FlagStockDTO flagStockDTO) throws LogicException, Exception {

    }

    @Override
    public void doValidate(StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO, List<StockTransDetailDTO> lstStockTransDetail) throws LogicException, Exception {

    }

}
