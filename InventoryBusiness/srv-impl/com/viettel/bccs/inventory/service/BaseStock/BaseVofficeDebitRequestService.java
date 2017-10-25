package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.ReportUtil;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.DebitRequest;
import com.viettel.bccs.inventory.model.DebitRequestDetail;
import com.viettel.bccs.inventory.service.*;
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
 * Created by hoangnt14 on 12/16/2016.
 */
@Service
public class BaseVofficeDebitRequestService extends BaseVofficeService {
    public static final Logger logger = Logger.getLogger(BaseVofficeDebitRequestService.class);

    @Autowired
    private DebitRequestService debitRequestService;

    @Autowired
    private DebitRequestDetailService debitRequestDetailService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private ReportUtil fileUtil;

    @Override
    public void doValidate(StockTransVofficeDTO stockTransVofficeDTO) throws LogicException, Exception {
        //set tittle
        stockTransVofficeDTO.setTittle(getText("voffice.doctitle.request.debit.request"));
        //
        DebitRequestDTO debitRequestDTO = debitRequestService.findOne(stockTransVofficeDTO.getStockTransActionId());
        if (DataUtil.isNullObject(debitRequestDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "vofice.debit.request.not.found");
        }
        List<DebitRequestDetailDTO> lstDetail = debitRequestDetailService.getLstDetailByRequestId(stockTransVofficeDTO.getStockTransActionId());
        if (DataUtil.isNullObject(lstDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "vofice.debit.request.detail.not.found");
        }
    }

    @Override
    public List<FileAttachTranfer> doCreateFileAttach(StockTransVofficeDTO stockTransVofficeDTO, VStockTransDTO vStockTransDTO, List<String> lstEmail) throws LogicException, Exception {
        List<FileAttachTranfer> lstFile = Lists.newArrayList();
        try {
            DebitRequestDTO debitRequestDTO = debitRequestService.findOne(stockTransVofficeDTO.getStockTransActionId());
            List<DebitRequestReportDTO> lstDetail = debitRequestDetailService.getLstDetailForReport(stockTransVofficeDTO.getStockTransActionId());
            // Lay thong tin nhan vien tao
            StaffDTO staffDTO = staffService.getStaffByStaffCode(debitRequestDTO.getCreateUser());
            if (DataUtil.isNullObject(staffDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "balance.vali.createUser");
            }
            MvShopStaffDTO mvShopStaffDTO = shopService.getMViewShopStaff(Const.OWNER_TYPE.STAFF_LONG, staffDTO.getStaffId());
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String key = "good.revoke.msg.signDate";
            String vOfficeDate = GetTextFromBundleHelper.getTextParam(key, DataUtil.safeToString(day), "" + month, "" + year);

            Map bean = new HashMap<>();
            bean.put("lstDetail", lstDetail);
            bean.put("districtName", mvShopStaffDTO.getDistrictName());
            bean.put("provinceName", mvShopStaffDTO.getProvinceName());
            bean.put("vOfficeDate", vOfficeDate);
            String templateName = fileUtil.getTemplatePath() + Const.REPORT_TEMPLATE.DEBIT_REQUEST;
            byte[] bytes = JasperReportUtils.exportPdfByte(bean, templateName);
            if (bytes == null || bytes.length == 0) {
                return lstFile;
            }
            bytes = fileUtil.insertComment(bytes, lstEmail);
            FileAttachTranfer fileAttachTranfer = new FileAttachTranfer();
            fileAttachTranfer.setAttachBytes(bytes);
            fileAttachTranfer.setFileSign(1L);
            fileAttachTranfer.setFileName(Const.VOFFICE_DOA.DEBIT_REQUEST_FILE_NAME);
            lstFile.add(fileAttachTranfer);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lstFile;
    }
}
