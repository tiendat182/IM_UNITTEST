package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ReportUtil;
import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.bccs.inventory.dto.VStockTransDTO;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.webservice.voffice.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by hoangnt14 on 12/5/2016.
 */

@Service
public abstract class BaseVofficeService extends BaseServiceImpl {

    @Value("${voffice.client.appCode}")
    private String appCode;
    @Value("${voffice.client.appPass}")
    private String appPass;

    public static final Logger logger = Logger.getLogger(BaseVofficeService.class);

    @Autowired
    private StockTransService stockTransService;

    @Autowired
    private ReportUtil fileUtil;

//    @Autowired
//    private Vo2AutoSignSystemImpl vo2AutoSignSystem;

    public Long doSignVOffice(StockTransVofficeDTO stockTransVofficeDTO) throws LogicException, Exception {

        //1. Validate giao dich
        doValidate(stockTransVofficeDTO);

        //2. Lay danh sach email trinh ky
        List<String> lstEmail = getLstEmail(stockTransVofficeDTO.getSignUserList());

        //3.0 Lay thong tin giao dich
        VStockTransDTO vStockTransDTO = stockTransService.findVStockTransDTO(null, stockTransVofficeDTO.getStockTransActionId());

        //3.1 Tao file trinh ky
        List<FileAttachTranfer> lstFileAttach = doCreateFileAttach(stockTransVofficeDTO, vStockTransDTO, lstEmail);

        //4 Trinh ky
        Long result = doAuthenticateVoffice(stockTransVofficeDTO, vStockTransDTO, lstFileAttach, lstEmail);

        return result;
    }

    public List<String> getLstEmail(String signUserList) {
        List<String> lstEmail = Lists.newArrayList();
        String[] emailList = signUserList.split(",");
        for (int i = 0; i < emailList.length; i++) {
            lstEmail.add(emailList[i] + "@viettel.com.vn");
        }
        return lstEmail;
    }

    public Long doAuthenticateVoffice(StockTransVofficeDTO stockTransVofficeDTO,
                                      VStockTransDTO vStockTransDTO,
                                      List<FileAttachTranfer> listFileAttach,
                                      List<String> listEmail) throws LogicException, Exception {

        KttsVofficeCommInpuParam params = new KttsVofficeCommInpuParam();
        //set app_code
        String appCodeEnc = EncryptionUtils.encrypt(appCode, EncryptionUtils.getKey());
        String appPassEnc1 = PassWordUtil.getInstance().encrypt(appPass);
        String appPassEnc2 = EncryptionUtils.encrypt(appPassEnc1, EncryptionUtils.getKey());
        //
        params.setAppCode(appCodeEnc);
        params.setAppPass(appPassEnc2);
        //
        if (DataUtil.isNullOrEmpty(listFileAttach)) {
            throw new LogicException("", "sign.state8");
        }
        params.getLstFileAttach().addAll(listFileAttach);
        params.setCreateDate(DateUtil.date2yyMMddString(stockTransVofficeDTO.getCreateDate()));
        if (!DataUtil.isNullObject(vStockTransDTO)) {
            params.setMoneyTransfer(stockTransService.getTransAmount(vStockTransDTO.getStockTransID()));
            params.setMoneyUnitID(Const.STOCK_TRANS_VOFFICE.VND);
            params.setCreateDate(DateUtil.date2yyMMddString(vStockTransDTO.getCreateDateTime()));
        }
        params.setAccountName(stockTransVofficeDTO.getAccountName());
        params.setAccountPass(stockTransVofficeDTO.getAccountPass());
        params.setTransCode(stockTransVofficeDTO.getStockTransVofficeId());
        params.setIsCanVanthuXetduyet(false);
        params.setDocTitle(stockTransVofficeDTO.getTittle());
        params.setRegisterNumber(fileUtil.getImCode() + stockTransVofficeDTO.getStockTransVofficeId());
        params.getLstEmail().addAll(listEmail);
//        return vo2AutoSignSystem.vo2RegDigitalDocByEmail(params);
        return null;
    }

    public abstract void doValidate(StockTransVofficeDTO stockTransVofficeDTO) throws LogicException, Exception;

    public abstract List<FileAttachTranfer> doCreateFileAttach(StockTransVofficeDTO stockTransVofficeDTO, VStockTransDTO vStockTransDTO, List<String> lstEmail) throws LogicException, Exception;

}
