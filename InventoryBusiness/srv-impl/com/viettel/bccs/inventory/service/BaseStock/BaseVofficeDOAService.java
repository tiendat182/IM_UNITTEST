package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ReportUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.CommonService;
import com.viettel.bccs.inventory.service.ShopService;
import com.viettel.bccs.inventory.service.StockTransActionService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.fw.common.util.JasperReportUtils;
import com.viettel.webservice.voffice.FileAttachTranfer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by hoangnt14 on 12/5/2016.
 */
@Service
public class BaseVofficeDOAService extends BaseVofficeService {
    public static final Logger logger = Logger.getLogger(BaseVofficeDOAService.class);

    @Autowired
    private StockTransActionService stockTransActionService;

    @Autowired
    private StockTransService stockTransService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ReportUtil fileUtil;

    @Override
    public void doValidate(StockTransVofficeDTO stockTransVofficeDTO) throws LogicException, Exception {
        //set tittle
        stockTransVofficeDTO.setTittle(getText("voffice.doctitle.request.DOA"));
        //Lay thong tin giao dich
        StockTransActionDTO stockTransActionDTO = stockTransActionService.findOne(stockTransVofficeDTO.getStockTransActionId());
        if (DataUtil.isNullObject(stockTransActionDTO)) {
            throw new LogicException("", "stock.trans.voffice.valid.trans");
        }
        if (!DataUtil.safeEqual(stockTransActionDTO.getSignCaStatus(), Const.SIGN_VOFFICE)) {
            throw new LogicException("", "stock.trans.voffice.status.not.valid");
        }
        StockTransDTO stockTransDTO = stockTransService.findStockTransByActionId(stockTransVofficeDTO.getStockTransActionId());
        if (DataUtil.isNullObject(stockTransDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "voffice.DOA.stock.trans.not.found");
        }
        List<DOATransferDTO> lstContent = stockTransService.getContentFileDOA(stockTransDTO.getStockTransId());
        if (DataUtil.isNullObject(lstContent)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "voffice.DOA.stock.trans.serial.not.found");
        }
    }

    @Override
    public List<FileAttachTranfer> doCreateFileAttach(StockTransVofficeDTO stockTransVofficeDTO, VStockTransDTO vStockTransDTO, List<String> lstEmail) throws LogicException, Exception {
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        String shopName = "";
        Long shopId = null;
        try {
            StockTransDTO stockTransDTO = stockTransService.findStockTransByActionId(stockTransVofficeDTO.getStockTransActionId());
            List<DOATransferDTO> lstContent = stockTransService.getContentFileDOA(stockTransDTO.getStockTransId());

            DOATransferDTO doaTransferDTO = lstContent.get(0);
            if (DataUtil.safeEqual(doaTransferDTO.getOwnerType(), Const.OWNER_TYPE.SHOP_LONG)) {
                shopName = doaTransferDTO.getOwnerName();
                shopId = doaTransferDTO.getOwnerId();
            } else {
                ShopDTO shopDTO = shopService.findOne(doaTransferDTO.getParrentShopId());
                if (DataUtil.isNullObject(shopDTO)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "voffice.DOA.shop.not.found");
                }
                shopName = shopDTO.getName();
                shopId = shopDTO.getShopId();
            }
            String province = commonService.getProvince(shopId);
            Date createDate = doaTransferDTO.getCreateDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(createDate);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String key = "stock.balance.msg.signDate";
            if (DataUtil.isNullOrEmpty(province)) {
                province = GetTextFromBundleHelper.getText("stock.balance.msg.defaultParam");
            }
            String vofficeDate = GetTextFromBundleHelper.getTextParam(key, province, "" + day, "" + month, "" + year);

            Map bean = new HashMap<>();
            bean.put("lstStockModel", lstContent);
            bean.put("shopName", shopName);
            bean.put("ownerName", doaTransferDTO.getOwnerName());
            bean.put("ownerCode", doaTransferDTO.getOwnerCode());
            bean.put("vOfficeDate", vofficeDate);
            String templateName = fileUtil.getTemplatePath() + Const.REPORT_TEMPLATE.DOA_TRANSFER;
            byte[] bytes = JasperReportUtils.exportPdfByte(bean, templateName);
            if (bytes == null || bytes.length == 0) {
                return lstFile;
            }
            bytes = fileUtil.insertComment(bytes, lstEmail);
            FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
            fileAttachTranfer.setAttachBytes(bytes);
            fileAttachTranfer.setFileSign(1L);
            fileAttachTranfer.setFileName(Const.VOFFICE_DOA.STOCK_DOA_FILE_NAME);
            lstFile.add(fileAttachTranfer);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lstFile;
    }
}
