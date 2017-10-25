package com.viettel.bccs.inventory.controller.stock;

import com.viettel.bccs.fw.controller.InventoryController;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.DebitRequestDTO;
import org.primefaces.component.fileupload.FileUpload;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

/**
 * @author:thanhnt77
 */
@Component
@Scope("view")
@ManagedBean
public class LimitStockCreateOrderByFileController extends InventoryController {
    private FileUpload fileUpload;
    private DebitRequestDTO debitRequestDTO;
    private FileUpload fileImport, fileAttach;

    //get-set
    public DebitRequestDTO getDebitRequestDTO() {
        return debitRequestDTO;
    }

    public void setDebitRequestDTO(DebitRequestDTO debitRequestDTO) {
        this.debitRequestDTO = debitRequestDTO;
    }

    @Secured("@")
    @PostConstruct
    public void init() {
        initControls();
    }

    private void initControls() {
        debitRequestDTO = new DebitRequestDTO();
        debitRequestDTO.setRequestObjectType(Const.OWNER_TYPE.SHOP);
    }
}


