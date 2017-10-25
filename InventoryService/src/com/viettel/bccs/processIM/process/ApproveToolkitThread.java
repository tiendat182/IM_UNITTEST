package com.viettel.bccs.processIM.process;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ChangeModelTransDTO;
import com.viettel.bccs.inventory.dto.ShopDTO;
import com.viettel.bccs.inventory.dto.StockNumberDTO;
import com.viettel.bccs.inventory.model.ChangeModelTrans;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by hoangnt14 on 6/15/2016.
 */
public class ApproveToolkitThread extends InventoryThread {

    @Autowired
    private ChangeModelTransService changeModelTransService;
    
    @Autowired
    private OptionSetValueService optionSetValueService;


    public ApproveToolkitThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute() {
        try {
            reportError("------Bat dau tien trinh duyet chuyen doi Toolkit ");
            Long maxDay = DataUtil.safeToLong(optionSetValueService.getValueByTwoCodeOption(Const.PROCESS_TOOLKIT.DAY_APPROVE_CHANGE_TOOLKIT, Const.PROCESS_TOOLKIT.DAY_APPROVE_CHANGE_TOOLKIT));
            if (DataUtil.isNullOrZero(maxDay)) {
                reportError("------Chua cau hinh thoi gian chuyen doi Toolkit--- ");
                return;
            }

            List<ChangeModelTrans> lstCancel = changeModelTransService.getLstCancelRequestThread(maxDay);
            if (!DataUtil.isNullOrEmpty(lstCancel)) {
                for (ChangeModelTrans changeModelTrans : lstCancel) {
                    try {
                        changeModelTransService.processCancelRequest(changeModelTrans.getChangeModelTransId());
                    } catch (LogicException ex) {
                        ChangeModelTransDTO changeModelTransError = changeModelTransService.findOne(changeModelTrans.getChangeModelTransId());
                        changeModelTransError.setErrorCode(ex.getErrorCode());
                        changeModelTransError.setErrorCodeDescription(ex.getDescription());
                        if (DataUtil.isNullOrZero(changeModelTransError.getRetry())) {
                            changeModelTransError.setRetry(1L);
                        } else {
                            changeModelTransError.setRetry(changeModelTransError.getRetry() + 1L);
                        }
                        changeModelTransService.save(changeModelTransError);
                        reportError(ex);
                    } catch (Exception e) {
                        reportError(e);
                    }
                }
            }
            List<ChangeModelTrans> lstApprove = changeModelTransService.getLstApproveRequestThread(maxDay);
            if (!DataUtil.isNullOrEmpty(lstApprove)) {
                for (ChangeModelTrans changeModelTrans : lstApprove) {
                    try {
                        changeModelTransService.processApproveRequest(changeModelTrans.getChangeModelTransId());
                    } catch (LogicException ex) {
                        ChangeModelTransDTO changeModelTransError = changeModelTransService.findOne(changeModelTrans.getChangeModelTransId());
                        changeModelTransError.setErrorCode(ex.getErrorCode());
                        changeModelTransError.setErrorCodeDescription(ex.getDescription());
                        if (DataUtil.isNullOrZero(changeModelTransError.getRetry())) {
                            changeModelTransError.setRetry(1L);
                        } else {
                            changeModelTransError.setRetry(changeModelTransError.getRetry() + 1L);
                        }
                        changeModelTransService.save(changeModelTransError);
                        reportError(ex);
                    } catch (Exception e) {
                        reportError(e);
                    }
                }
            }
            reportError("------Ket thuc tien trinh duyet chuyen doi Toolkit ");
        } catch (Exception ex) {
            reportError(ex);
        }
    }


}
