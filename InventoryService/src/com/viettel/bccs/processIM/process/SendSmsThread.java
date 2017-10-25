package com.viettel.bccs.processIM.process;

import com.viettel.bccs.processIM.common.InventoryThread;
import com.viettel.bccs.inventory.common.SmsClient;
import com.viettel.bccs.inventory.common.StringUtil;
import com.viettel.bccs.inventory.dto.MtDTO;
import com.viettel.bccs.inventory.service.MtService;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author luannt23.
 * @comment
 * @date 3/5/2016.
 */
public class SendSmsThread extends InventoryThread {

    @Autowired
    private MtService mtService;

    @Autowired
    private SmsClient smsClient;

    public SendSmsThread(String name, int numActorLimit, int type) {
        super(name, numActorLimit, type);
    }

    public void execute() {
        try {
            List<MtDTO> listMtDTO = mtService.findAll();
            for (MtDTO mt : listMtDTO) {
                if (!DataUtil.isNullOrEmpty(mt.getMsisdn()) || !DataUtil.isNullOrEmpty(mt.getMessage())) {
                    reportError("pre send sms to ...." + mt.getMsisdn());
                    String message = StringUtil.convertUnicode2Nosign(mt.getMessage());
                    reportError(message);
                    int success = smsClient.sendSMS(mt.getMsisdn(), message);
                    if (success == 1) {
                        mtService.delete(mt.getMtId());
                    } else {
                        mt.setRetryNum(mt.getRetryNum() == null ? 1l : mt.getRetryNum() + 1L);
                        mt.setReceiveTime(new Date());
                        mtService.update(mt);
                    }
                    reportError("sms was sent to ...." + mt.getMsisdn());
                } else {
                    reportError("sms -id = " + mt.getMtId() == null ? "0" : mt.getMtId() + " haven't enough infomation(msisdn, content, reciever, etc)");
                }
            }
        } catch (Exception ex) {
            reportError(ex);
        }
    }
}
