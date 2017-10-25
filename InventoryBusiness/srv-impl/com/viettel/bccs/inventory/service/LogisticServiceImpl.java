package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockTotal;
import com.viettel.bccs.inventory.model.StockTrans;
import com.viettel.bccs.inventory.model.StockTransAction;
import com.viettel.bccs.inventory.repo.StockTransRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by hoangnt14 on 7/14/2016.
 */
@Service
public class LogisticServiceImpl extends BaseServiceImpl implements LogisticService {


    public static final Logger logger = Logger.getLogger(LogisticServiceImpl.class);

    @Autowired
    private LogisticBaseService logisticBaseService;

    @Override
    public BillStockResultDTO createBill(List<BillStockDTO> lstBillStockDTO) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(lstBillStockDTO)) {
            return new BillStockResultDTO(Const.LOGISTICS.FAILED, getText("logistics.create.bill.lst.empty"));
        }
        String synTransCode = "";
        for (BillStockDTO billStockDTO : lstBillStockDTO) {
            try {
                ResultLogisticsDTO resultLogisticsDTO = logisticBaseService.createNote(billStockDTO.getOrderAction(), billStockDTO);
//                billStockDTO.setErrorCode(resultLogisticsDTO.getResponseCode());
                billStockDTO.setStatus(resultLogisticsDTO.getResponseCode());
                billStockDTO.setErrorMessage(resultLogisticsDTO.getDescription());
                billStockDTO.setActionCode(resultLogisticsDTO.getActionCode());
                billStockDTO.setOrderActionOld(resultLogisticsDTO.getOrderAction());
                synTransCode = resultLogisticsDTO.getActionCode();
            } catch (LogicException ex) {
                logger.error(ex.getMessage(), ex);
                billStockDTO.setErrorCode(Const.LOGISTICS.FAILED);
                billStockDTO.setErrorMessage(ex.getDescription());
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                billStockDTO.setErrorCode(Const.LOGISTICS.FAILED);
                billStockDTO.setErrorMessage(ex.getMessage());
            }
        }

        return new BillStockResultDTO(Const.LOGISTICS.SUCCESS, getText("logistics.create.bill.success"), lstBillStockDTO, synTransCode);
    }


    @Override
    public BillStockResultDTO transStock(BillStockDTO billStockDTO) throws LogicException, Exception {
        ResultLogisticsDTO resultLogisticsDTO = new ResultLogisticsDTO();
        try {
            resultLogisticsDTO = logisticBaseService.transStock(billStockDTO);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            return new BillStockResultDTO(Const.LOGISTICS.FAILED, ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            return new BillStockResultDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.trans.stock.failed", billStockDTO.getOrderCode()));
        }
        return new BillStockResultDTO(resultLogisticsDTO.getResponseCode(), resultLogisticsDTO.getDescription());
    }

    @Override
    public BillStockResultDTO cancelOrderOrBill(OrderObjectDTO orderObjectDTO) throws LogicException, Exception {
        ResultLogisticsDTO resultLogisticsDTO = new ResultLogisticsDTO();
        try {
            resultLogisticsDTO = logisticBaseService.cancelOrderOrBill(orderObjectDTO);
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            return new BillStockResultDTO(Const.LOGISTICS.FAILED, ex.getDescription());
        } catch (Exception ex) {
            logger.error(ex);
            return new BillStockResultDTO(Const.LOGISTICS.FAILED, getTextParam("logistics.cancel.order.failed", orderObjectDTO.getOrderCode()));
        }
        return new BillStockResultDTO(resultLogisticsDTO.getResponseCode(), resultLogisticsDTO.getDescription());
    }
}
