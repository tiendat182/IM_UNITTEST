package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.exception.HandleException;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.message.BaseExtMessage;
import com.viettel.bccs.inventory.message.BasePartnerMessage;
import com.viettel.bccs.inventory.model.StockTransDetail;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockService;
import com.viettel.bccs.inventory.service.BaseStock.StockTransFactory;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service
public class ExecuteStockTransServiceImpl extends BaseServiceImpl implements ExecuteStockTransService {

    public static final Logger logger = Logger.getLogger(ExecuteStockTransService.class);
    @Autowired
    private StockTransFactory stockTransFactory;
    @Autowired
    private StockTransDetailService stockTransDetailService;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;


    @Override
    public BaseMessageStockTrans executeStockTrans(String mode, String type, StockTransDTO stockTransDTO, StockTransActionDTO stockTransActionDTO,
                                                   List<StockTransDetailDTO> lstStockTransDetail, RequiredRoleMap requiredRoleMap) {

        BaseMessageStockTrans baseMessage = new BaseMessageStockTrans();
        try {
            //lay loai giao dich tuong ung
            Date sysdate = DbUtil.getSysDate(em);
            stockTransDTO.setCreateDatetime(sysdate);
            BaseStockService baseStockService = stockTransFactory.getInstance(mode, type);

            StockTransActionDTO transActionDTO = baseStockService.executeStockTrans(stockTransDTO, stockTransActionDTO, lstStockTransDetail, requiredRoleMap);
            baseMessage.setStockTransActionId(transActionDTO.getStockTransActionId());
        } catch (LogicException le) {
            baseMessage.setErrorCode(le.getErrorCode());
            baseMessage.setKeyMsg(le.getKeyMsg());
            baseMessage.setParamsMsg(le.getParamsMsg());
            baseMessage.setDescription(le.getDescription());
            logger.error(le.getMessage(), le);
        } catch (Exception ex) {
            baseMessage.setErrorCode(ErrorCode.ERROR_NOT_DEFINE);
            baseMessage.setKeyMsg("common.error.happen");
            logger.error(ex.getMessage(), ex);
        }
        return baseMessage;
    }

    @Override
    public List<BaseExtMessage> executeStockTransList(String mode, String type, List<StockTransFileDTO> lsStockTransFileDTOs, RequiredRoleMap requiredRoleMap) {
        List<BaseExtMessage> lsBaseMessage = Lists.newArrayList();
        if (lsStockTransFileDTOs == null) {
            return lsBaseMessage;
        }

        for (StockTransFileDTO stockTransFile : lsStockTransFileDTOs) {
            BaseMessage baseMessage = new BaseMessage();
            if (DataUtil.isNullOrEmpty(stockTransFile.getMsgError())) {
                baseMessage = executeStockTrans(mode, type, stockTransFile.getStockTransDTO(), stockTransFile.getStockTransActionDTO(),
                        stockTransFile.getLstStockTransDetail(), requiredRoleMap);
            } else {
                baseMessage.setDescription(stockTransFile.getMsgError());
            }
            BaseExtMessage extMessage = new BaseExtMessage(baseMessage);
            extMessage.setDescriptionFile(baseMessage.getDescription());
            extMessage.setStockTransFileDTO(stockTransFile);
            lsBaseMessage.add(extMessage);
        }
        return lsBaseMessage;
    }

    @Override
    public BasePartnerMessage createExportToPartner(String mode, String type, StockTransFullDTO stockTransFullDTO, StaffDTO staffDTO,
                                                    String typeExport, String serialList, RequiredRoleMap requiredRoleMap) {
        BasePartnerMessage baseMessage = new BasePartnerMessage();
        try {
            BaseStockService baseStockService = stockTransFactory.getInstance(mode, type);
            baseMessage = baseStockService.createTransToPartner(stockTransFullDTO, staffDTO, typeExport, serialList, requiredRoleMap);
        } catch (HandleException he) {
            baseMessage.setErrorCode(he.getErrorCode());
            baseMessage.setKeyMsg(he.getKeyMsg());
            baseMessage.setParamsMsg(he.getParamsMsg());
            baseMessage.setDescription(he.getDescription());
            baseMessage.setSuccess(false);
            baseMessage.setLsSerialErorr(he.getLsErrorData());
            logger.error(he.getMessage(), he);
        } catch (LogicException le) {
            baseMessage.setErrorCode(le.getErrorCode());
            baseMessage.setKeyMsg(le.getKeyMsg());
            baseMessage.setParamsMsg(le.getParamsMsg());
            baseMessage.setDescription(le.getDescription());
            baseMessage.setSuccess(false);
            logger.error(le.getMessage(), le);
        } catch (Exception ex) {
            baseMessage.setErrorCode(ErrorCode.ERROR_NOT_DEFINE);
            baseMessage.setKeyMsg("common.error.happen");
            logger.error(ex.getMessage(), ex);
        }
        return baseMessage;
    }
}
