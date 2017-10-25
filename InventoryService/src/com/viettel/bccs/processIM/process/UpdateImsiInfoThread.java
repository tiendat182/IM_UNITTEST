package com.viettel.bccs.processIM.process;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.UpdateImsiInfoDTO;
import com.viettel.bccs.inventory.service.ImsiInfoService;
import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by vanho on 05/06/2017.
 */
public class UpdateImsiInfoThread extends InventoryThread {

    @Autowired
    private ImsiInfoService imsiInfoService;

    public UpdateImsiInfoThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    @Override
    public void execute(){
        reportError("------Bat dau chay tien trinh Cap nhat thong tin IMSI");
        try {
            List<UpdateImsiInfoDTO> results = imsiInfoService.getImsiHasFillPartner();
            if (DataUtil.isNullOrEmpty(results)) {
                reportError("------Khong co IMSI nao da nhap doi tac");
            } else {
                reportError("------Tong so IMSI da nhap doi tac la: " + results.size());
                for (UpdateImsiInfoDTO updateImsiInfoDTO : results) {
                    try {
                        reportError("------Cap nhat thong tin IMSI theo dai SERIAL da nhap kho: " + updateImsiInfoDTO.toString());
                        imsiInfoService.updateImsiInfoHasConnectSim(updateImsiInfoDTO);
                        reportError("------Cap nhat thong tin IMSI thanh cong");
                    } catch (Exception e) {
                        reportError("------Cap nhat thon tin IMSI that bai");
                    }
                }
            }

            List<UpdateImsiInfoDTO> transaction = imsiInfoService.getTransactionToUpdateImsi();
            if (DataUtil.isNullOrEmpty(transaction)) {
                reportError("------Khong lay duoc thong tin giao dich nao");
            } else {
                reportError("------Tong so giao dich lay duoc la: " + transaction.size());
                for (UpdateImsiInfoDTO updateImsiInfoDTO : transaction) {
                    try {
                        reportError("------Cap nhat thong tin IMSI theo dai SERIAL da su dung: " + updateImsiInfoDTO.toString());
                        imsiInfoService.updateImsiInfoToHasUsedSim(updateImsiInfoDTO);
                        reportError("------Cap nhat thong tin IMSI thanh cong");
                    } catch (Exception e) {
                        reportError("------Cap nhat thon tin IMSI that bai");
                    }
                }
            }

        } catch (Exception ex){
            reportError(ex);
        }
        reportError("------Ket thuc chay tien trinh Cap nhat thong tin IMSI ");
    }
}
