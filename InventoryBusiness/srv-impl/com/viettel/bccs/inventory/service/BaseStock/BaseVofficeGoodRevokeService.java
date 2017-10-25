package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ReportUtil;
import com.viettel.bccs.inventory.dto.StockRequestOrderDTO;
import com.viettel.bccs.inventory.dto.StockRequestOrderDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.bccs.inventory.dto.VStockTransDTO;
import com.viettel.bccs.inventory.service.StockRequestOrderDetailService;
import com.viettel.bccs.inventory.service.StockRequestOrderService;
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
public class BaseVofficeGoodRevokeService extends BaseVofficeService {
    public static final Logger logger = Logger.getLogger(BaseVofficeGoodRevokeService.class);

    @Autowired
    private StockRequestOrderService stockRequestOrderService;

    @Autowired
    private StockRequestOrderDetailService stockRequestOrderDetailService;

    @Autowired
    private ReportUtil fileUtil;

    @Override
    public void doValidate(StockTransVofficeDTO stockTransVofficeDTO) throws LogicException, Exception {
        //set tittle
        stockTransVofficeDTO.setTittle(getText("voffice.doctitle.request.good.revoke"));
        //
        StockRequestOrderDTO stockRequestOrderDTO = stockRequestOrderService.findOne(stockTransVofficeDTO.getStockTransActionId());
        if (DataUtil.isNullObject(stockRequestOrderDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "voffice.good.revoke.stock.request.order.not.found");
        }
        List<StockRequestOrderDetailDTO> lstDetail = stockRequestOrderDetailService.getLstDetailTemplate(stockTransVofficeDTO.getStockTransActionId());
        if (DataUtil.isNullObject(lstDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "voffice.good.revoke.stock.request.order.detail.not.found");
        }
    }

    @Override
    public List<FileAttachTranfer> doCreateFileAttach(StockTransVofficeDTO stockTransVofficeDTO, VStockTransDTO vStockTransDTO, List<String> lstEmail) throws LogicException, Exception {
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        try {
            StockRequestOrderDTO stockRequestOrderDTO = stockRequestOrderService.findOne(stockTransVofficeDTO.getStockTransActionId());
            List<StockRequestOrderDetailDTO> lstDetail = stockRequestOrderDetailService.getLstDetailTemplate(stockTransVofficeDTO.getStockTransActionId());
            for (StockRequestOrderDetailDTO stockRequestOrderDetailDTO : lstDetail) {
                stockRequestOrderDetailDTO.setTypeTransfer(getText("good.revoke.type.transfer"));
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String key = "good.revoke.msg.signDate";
            String vOfficeDate = GetTextFromBundleHelper.getTextParam(key, DataUtil.safeToString(day), "" + month, "" + year);

            Map bean = new HashMap<>();
            bean.put("lstDetail", lstDetail);
            bean.put("ownerName", lstDetail.get(0).getToOwnerName());
            bean.put("vOfficeNumber", stockRequestOrderDTO.getOrderCode());
            bean.put("vOfficeDate", vOfficeDate);
            String templateName = fileUtil.getTemplatePath() + Const.REPORT_TEMPLATE.GOOD_REVOKE;
            byte[] bytes = JasperReportUtils.exportPdfByte(bean, templateName);
            if (bytes == null || bytes.length == 0) {
                return lstFile;
            }
            bytes = fileUtil.insertComment(bytes, lstEmail);
            FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
            fileAttachTranfer.setAttachBytes(bytes);
            fileAttachTranfer.setFileSign(1L);
            fileAttachTranfer.setFileName(Const.VOFFICE_DOA.GOOD_REVOKE_FILE_NAME);
            lstFile.add(fileAttachTranfer);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lstFile;
    }
}
