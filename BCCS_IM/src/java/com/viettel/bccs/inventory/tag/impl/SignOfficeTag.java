package com.viettel.bccs.inventory.tag.impl;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.SignFlowDTO;
import com.viettel.bccs.inventory.dto.SignFlowDetailDTO;
import com.viettel.bccs.inventory.dto.SignOfficeDTO;
import com.viettel.bccs.inventory.dto.VofficeRoleDTO;
import com.viettel.bccs.inventory.service.SignFlowDetailService;
import com.viettel.bccs.inventory.service.SignFlowService;
import com.viettel.bccs.inventory.service.VofficeRoleService;
import com.viettel.bccs.inventory.tag.SignOfficeTagNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.voffice.autosign.Ver3AutoSign;
import com.viettel.voffice.autosign.ws.StaffBO;
import com.viettel.web.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * tag ky office
 * Created by thanhnt77 on 12/11/2015.
 */
@Service
@Scope("prototype")
public class SignOfficeTag extends BaseController implements SignOfficeTagNameable {

    private SignOfficeDTO signOfficeDTO = new SignOfficeDTO();
    private Object objecController;

    private List<SignFlowDTO> lsSignFlowDTOs = Lists.newArrayList();
    private List<SignFlowDetailDTO> lstFlowDetailDTO = Lists.newArrayList();
    private List<VofficeRoleDTO> listVofficeRole = Lists.newArrayList();

    @Autowired
    private SignFlowService signFlowService;

    @Autowired
    private Ver3AutoSign ver3AutoSign;

    @Autowired
    SignFlowDetailService signFlowDetailService;

    @Autowired
    VofficeRoleService vofficeRoleService;

    @Override
    public void init(Object objectController, Long shopId) {
        try {
            this.objecController = objectController;
            listVofficeRole = vofficeRoleService.search(new VofficeRoleDTO(), false);
            //lay ra danh sach luong trinh ky
            if (DataUtil.isNullOrEmpty(lsSignFlowDTOs)) {
                lsSignFlowDTOs = signFlowService.getSignFlowByShop(shopId);
            }
            signOfficeDTO = new SignOfficeDTO();
        } catch (Exception e) {
            this.reportError("", "common.error.happened", e);
        }
    }

    /**
     * load mac dinh
     *
     * @param writeOfficeDTO
     * @author ThanhNT
     */
    @Override
    public void load(SignOfficeDTO writeOfficeDTO) {
        if (writeOfficeDTO != null) {
            this.signOfficeDTO = writeOfficeDTO;
        }
    }

    /**
     * reset thong tin
     *
     * @param
     * @author ThanhNT
     */
    @Override
    public void resetOffice() {
        signOfficeDTO = new SignOfficeDTO();
    }

    public SignOfficeDTO getSignOfficeDTO() {
        return signOfficeDTO;
    }

    public void setSignOfficeDTO(SignOfficeDTO signOfficeDTO) {
        this.signOfficeDTO = signOfficeDTO;
    }

    public Object getObjecController() {
        return objecController;
    }

    public void setObjecController(Object objecController) {
        this.objecController = objecController;
    }

    public List<SignFlowDTO> getLsSignFlowDTOs() {
        return lsSignFlowDTOs;
    }

    public void setLsSignFlowDTOs(List<SignFlowDTO> lsSignFlowDTOs) {
        this.lsSignFlowDTOs = lsSignFlowDTOs;
    }

    @Override
    public SignOfficeDTO validateVofficeAccount() throws LogicException, Exception {
        SignOfficeDTO officeDTO = DataUtil.cloneBean(this.signOfficeDTO);
        if (DataUtil.isNullOrEmpty(officeDTO.getUserName())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "common.user.require.msg");
        }
        if (DataUtil.isNullOrEmpty(officeDTO.getPassWord())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "common.password.require.msg");
        }
        SignFlowDTO signFlowDTO = signFlowService.findOne(officeDTO.getSignFlowId());
        if (DataUtil.isNullObject(signFlowDTO) || !DataUtil.safeEqual(signFlowDTO.getStatus(), Const.STATUS_ACTIVE)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "common.signflow.not.exsit.msg");
        }
        List<StaffBO> staffBOs;
        try {
            staffBOs = ver3AutoSign.checkAccount(officeDTO.getUserName(), officeDTO.getPassWord());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.voffice.error.ws");
        }
        if (DataUtil.isNullOrEmpty(staffBOs)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_PASS_INVALID, "common.valid.voffice.account");
        } else if (staffBOs.size() > 1) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_DUPLICATE_OBJECT, "common.valid.voffice.2account", staffBOs.size(), officeDTO.getUserName());
        }
        officeDTO.setUserName(staffBOs.get(0).getLoginName());
        officeDTO.setPassWord(staffBOs.get(0).getPassWord());

        return officeDTO;
    }

    @Override
    public void showDetailSignOfficeFlow() throws Exception {
        if(!DataUtil.isNullObject(signOfficeDTO.getSignFlowId())) {
            lstFlowDetailDTO = signFlowDetailService.findBySignFlowId(signOfficeDTO.getSignFlowId());
        }else{
            lstFlowDetailDTO= Lists.newArrayList();
        }
    }

    public String getNameRole(Long vofficeRoleId) {
        String name = "";
        for (VofficeRoleDTO role : listVofficeRole) {
            if (DataUtil.safeEqual(role.getVofficeRoleId(), vofficeRoleId)) {
                name = role.getRoleName();
                break;
            }
        }
        return name;
    }

    public List<SignFlowDetailDTO> getLstFlowDetailDTO() {
        return lstFlowDetailDTO;
    }
}
