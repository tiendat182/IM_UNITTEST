package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.voffice.vo.ResultObj;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Service
public class HandleVOfficeResultServiceImpl extends BaseServiceImpl implements HandleVOfficeResultService {

    Logger logger = Logger.getLogger(HandleVOfficeResultService.class);
    public static String ERR_VALIDATE = "-1";
    public static String SUCCESS = "0";

    @Autowired
    private HandleVOfficeResultBaseService handleVOfficeResultBaseService;

    @WebMethod
    @Override
    public BaseMessage returnSignResult(ResultObj resultObj) throws Exception {
        BaseMessage baseMessage = new BaseMessage(false);
        try {
            //validate
            if (DataUtil.isNullObject(resultObj)) {
                baseMessage.setErrorCode(ERR_VALIDATE);
                baseMessage.setDescription(getText("handle.voffice.null"));
                return baseMessage;
            }
            if (DataUtil.isNullOrEmpty(resultObj.getTransCode())) {
                baseMessage.setErrorCode(ERR_VALIDATE);
                baseMessage.setDescription(getText("handle.voffice.transCode"));
                return baseMessage;
            }
            if (DataUtil.isNullOrEmpty(resultObj.getSignStatus())) {
                baseMessage.setErrorCode(ERR_VALIDATE);
                baseMessage.setDescription(getText("handle.voffice.signStatus"));
                return baseMessage;
            }
            baseMessage = handleVOfficeResultBaseService.returnSignResult(resultObj);
        } catch (LogicException ex) {
            logger.error("LogicException", ex);
            baseMessage.setDescription(getText("handle.voffice.failed"));
        } catch (Exception ex) {
            logger.error("Exception", ex);
            baseMessage.setDescription(getText("handle.voffice.failed"));
        }
        return baseMessage;
    }
}
