package com.viettel.bccs.inventory.common;
import com.viettel.fw.dto.BaseMessage;

import java.util.List;

/**
 * Created by thetm1 on 20/01/2015.
 */
public class RevokeMessage extends BaseMessage {
    private Long revokeTransId;
    private List<String> errList;

    public Long getRevokeTransId() {
        return revokeTransId;
    }

    public void setRevokeTransId(Long revokeTransId) {
        this.revokeTransId = revokeTransId;
    }

    public List<String> getErrList() {
        return errList;
    }

    public void setErrList(List<String> errList) {
        this.errList = errList;
    }
}
