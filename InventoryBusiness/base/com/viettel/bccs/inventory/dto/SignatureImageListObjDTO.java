package com.viettel.bccs.inventory.dto;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.viettel.fw.dto.BaseDTO;

import java.util.List;

public class SignatureImageListObjDTO extends BaseDTO {

    private List<SignatureImageObjDTO> lstSignatureImageObj;


    public SignatureImageListObjDTO() {
    }

    public List<SignatureImageObjDTO> getLstSignatureImageObj() {
        return lstSignatureImageObj;
    }

    public void setLstSignatureImageObj(List<SignatureImageObjDTO> lstSignatureImageObj) {
        this.lstSignatureImageObj = lstSignatureImageObj;
    }
}
