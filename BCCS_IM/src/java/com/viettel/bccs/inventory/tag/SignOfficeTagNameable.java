package com.viettel.bccs.inventory.tag;

import com.viettel.bccs.inventory.dto.SignFlowDTO;
import com.viettel.bccs.inventory.dto.SignFlowDetailDTO;
import com.viettel.bccs.inventory.dto.SignOfficeDTO;
import com.viettel.fw.Exception.LogicException;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 * @author ThanhNT
 */
public interface SignOfficeTagNameable {

    @Secured("@")
    public void init(Object objectController, Long shopId);

    /**
     * load mac dinh
     *
     * @param writeOfficeDTO
     * @author ThanhNT
     */
    @Secured("@")
    public void load(SignOfficeDTO writeOfficeDTO);

    /**
     * reset office
     * @author ThanhNT77
     */
    @Secured("@")
    public void resetOffice();

    public SignOfficeDTO getSignOfficeDTO();

    public void setSignOfficeDTO(SignOfficeDTO writeOfficeDTO);

    public Object getObjecController();

    public void setObjecController(Object objecController);

    public List<SignFlowDTO> getLsSignFlowDTOs();

    public void setLsSignFlowDTOs(List<SignFlowDTO> lsSignFlowDTOs);

    /**
     * @return accountVoffice
     * @author LuanNT23
     */
    public SignOfficeDTO validateVofficeAccount() throws LogicException, Exception;

    public void showDetailSignOfficeFlow() throws Exception;

    public List<SignFlowDetailDTO> getLstFlowDetailDTO();

    public String getNameRole(Long vofficeRoleId);
}
