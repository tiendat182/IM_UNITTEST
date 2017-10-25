package com.viettel.bccs.inventory.controller.stock;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.viettel.bccs.fw.common.BccsLoginSuccessHandler;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.FinanceTypeDTO;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.VShopStaffDTO;
import com.viettel.bccs.inventory.service.FinanceTypeService;
import com.viettel.bccs.inventory.service.OptionSetValueService;
import com.viettel.bccs.inventory.tag.ShopInfoNameable;
import com.viettel.bccs.inventory.tag.StaffInfoNameable;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.web.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * controller cau hinh han muc nop tien
 * @author:thanhnt77
 */
@Component
@Scope("view")
@ManagedBean(name="limitFinanceTypeController")
public class LimitFinanceTypeController extends BaseController {

    private final static String DAYNUM_REGEX="^[0-9]{0,10}$";

    private List<OptionSetValueDTO> lsLimitOptionType = Lists.newArrayList();
    private Map<String, String> mapLimitOption = Maps.newHashMap();
    private List<FinanceTypeDTO> lsFinanceType = Lists.newArrayList();
    private List<FinanceTypeDTO> lsFinanceTypeSelect = Lists.newArrayList();

    private FinanceTypeDTO financeTypeDTOSearch;
    private FinanceTypeDTO financeTypeDTO = new FinanceTypeDTO();
    private FinanceTypeDTO financeTypeDTODelete = new FinanceTypeDTO();

    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private FinanceTypeService financeTypeService;
    @Autowired
    private ShopInfoNameable shopInfoTag;
    @Autowired
    private StaffInfoNameable staffInfoTag;

    @Secured("@")
    @PostConstruct
    public void init() {
        try {
//            shopInfoTag.initShop(this, "");
            financeTypeDTOSearch = new FinanceTypeDTO();
            lsLimitOptionType = DataUtil.defaultIfNull(optionSetValueService.getByOptionSetCode(Const.OPTION_SET.FINANCE_TYPE), new ArrayList<OptionSetValueDTO>());
            for (OptionSetValueDTO optionSetValueDTO : lsLimitOptionType) {
                mapLimitOption.put(optionSetValueDTO.getValue(), optionSetValueDTO.getName());
            }
            lsFinanceType = searchFinanceType();
            //xu ly sort theo name
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            reportError("", "common.error.happened", ex);
            logger.error(ex.getMessage(), ex);
            topPage();
        }
    }

    /**
     * Ham xu ly tim kiem thong tin nop tien tren giao dien
     * @author ThanhNT
     */
    @Secured("@")
    public void doSearchFinanceType() {
        try {
            if(!DataUtil.isNullObject(financeTypeDTOSearch.getStrDayNum()) &&
                    (!DataUtil.validateStringByPattern(financeTypeDTOSearch.getStrDayNum(), DAYNUM_REGEX) ||
                            DataUtil.safeToLong(financeTypeDTOSearch.getStrDayNum()) <= 0L)) {
                lsFinanceType = Lists.newArrayList();
                return;
            }
            financeTypeDTOSearch.setDayNum(DataUtil.isNullOrEmpty(financeTypeDTOSearch.getStrDayNum()) ? null : DataUtil.safeToLong(financeTypeDTOSearch.getStrDayNum()));
            lsFinanceType = searchFinanceType();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    /**
     * Ham xu ly tim kiem thong tin nop tien tren giao dien
     * @author ThanhNT
     */
    @Secured("@")
    public void doResetSearchFinanceType() {
        try {
            financeTypeDTOSearch = new FinanceTypeDTO();
            lsFinanceType = searchFinanceType();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    /**
     * Ham xu ly tim kiem thong tin nop tien
     * @author ThanhNT
     */
    private List<FinanceTypeDTO> searchFinanceType() throws LogicException, Exception{
        List<FinanceTypeDTO> result  = DataUtil.defaultIfNull(financeTypeService.searchLsFinanceTypeDto(financeTypeDTOSearch), new ArrayList<FinanceTypeDTO>());
        resetTable("frmFinanceType:tblFinance");
        return result;
    }

    public void doEditData() {
        doConfirmAdd();
    }

    /**
     * Ham xu ly tao moi data finance
     * @author ThanhNT
     */
    @Secured("@")
    public void doUpdateFinaceType(Boolean isClose) {
        try {
            if (financeTypeDTO == null) {
                return;
            }
            //validate ngay nhap phai la dinh dang so, va phai la so nguyen duong
            if (checkDayNum(this.financeTypeDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.limit.financeType.daynum.msg.err");
            }
            boolean isCreatenew  = financeTypeDTO.getId() == null;
            BaseMessage baseMessage;
            //xu ly insert update
            StaffDTO staffDTO = BccsLoginSuccessHandler.getStaffDTO();
            financeTypeDTO.setDayNum(DataUtil.safeToLong(financeTypeDTO.getStrDayNum()));
            financeTypeDTO.setStatus(Const.STATUS.ACTIVE);
            if (isCreatenew) {
                baseMessage = financeTypeService.create(financeTypeDTO, staffDTO);
            } else {
                baseMessage = financeTypeService.update(financeTypeDTO, staffDTO);
            }
            //neu co loi thi xu ly check de throw msg
            if (baseMessage != null && !DataUtil.isNullOrEmpty(baseMessage.getErrorCode())) {
                throw new LogicException(baseMessage.getErrorCode(), baseMessage.getKeyMsg());
            }
            reportSuccess("", isCreatenew ? "common.msg.success.create" : "common.msg.success.update");
            financeTypeDTOSearch = new FinanceTypeDTO();
            lsFinanceType = searchFinanceType();
            financeTypeDTO = new FinanceTypeDTO();
            if (isClose) {
                updateElemetId("frmFinanceType");
            } else {
                updateElemetId("frmFinanceType:tblFinance");
            }
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex);
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    /**
     * ham mo dialog add moi finance
     * @author ThanhNT
     */
    @Secured("@")
    public void doOpenAddFinanceType() {
        financeTypeDTO = new FinanceTypeDTO();
    }

    /**
     * ham mo dialog add moi finance
     * @author ThanhNT
     */
    @Secured("@")
    public void doOpenEditFinanceType(Long id) {
        try {
            financeTypeDTO = financeTypeService.findOne(id);
            financeTypeDTO.setStrDayNum(DataUtil.safeToString(financeTypeDTO.getDayNum()));
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getDescription());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    /**
     * ham xu ly xoa list record
     * @author ThanhNT
     * @param
     */
    @Secured("@")
    public void doDeleteListFinance() {
        try {
            if (DataUtil.isNullOrEmpty(lsFinanceTypeSelect)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.invoice.invoiceType.no.select");
            }
            BaseMessage baseMessage = deleteListFinanceType(lsFinanceTypeSelect);
            if (baseMessage != null && !DataUtil.isNullOrEmpty(baseMessage.getErrorCode())) {
                throw new LogicException(baseMessage.getErrorCode(), baseMessage.getKeyMsg());
            }
            reportSuccess("", "common.msg.success.delete");
            lsFinanceType = searchFinanceType();
            lsFinanceTypeSelect = Lists.newArrayList();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    /**
     * ham xu ly xoa 1 ban ghi
     * @author ThanhNT
     * @param
     */
    @Secured("@")
    public void doDeleteOneFinance() {
        try {
            BaseMessage baseMessage = deleteListFinanceType(Lists.newArrayList(financeTypeDTODelete));
            if (baseMessage != null && !DataUtil.isNullOrEmpty(baseMessage.getErrorCode())) {
                throw new LogicException(baseMessage.getErrorCode(), baseMessage.getKeyMsg());
            }
            reportSuccess("", "common.msg.success.delete");
            lsFinanceType = searchFinanceType();
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    /**
     * ham xu ly xoa data goi serviceer
     * @author ThanhNT
     * @param lsFinanceType
     * @return
     * @throws LogicException
     * @throws Exception
     */
    private BaseMessage  deleteListFinanceType(List<FinanceTypeDTO> lsFinanceType) throws LogicException, Exception{
        return financeTypeService.deleteListFinance(lsFinanceType, BccsLoginSuccessHandler.getStaffDTO());
    }

    /**
     * ham check validate dinh dang daynum
     * @author ThanhNT
     * @return true neu xay ra loi
     */
    private boolean checkDayNum(FinanceTypeDTO financeTypeDTO) {
        return DataUtil.isNullOrEmpty(financeTypeDTO.getStrDayNum()) || !DataUtil.validateStringByPattern(financeTypeDTO.getStrDayNum(), DAYNUM_REGEX)
                || DataUtil.safeToLong(financeTypeDTO.getStrDayNum()) <= 0L;
    }


    public void test() {

    }


    /**
     * ham xu ly xac nhan xem co add moi thog tin hay khong
     * @author ThanhNT
     */
    @Secured("@")
    public void doConfirmAdd() {
        try {
            //validate ngay nhap phai la dinh dang so, va phai la so nguyen duong
            if (checkDayNum(this.financeTypeDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.limit.financeType.daynum.msg.err");
            }
            financeTypeDTO.setDayNum(DataUtil.safeToLong(financeTypeDTO.getStrDayNum()));
        } catch (LogicException ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", ex.getErrorCode(), ex.getKeyMsg());
            topPage();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            reportError("", "common.error.happened", ex);
            topPage();
        }
    }

    /**
     * ham xu ly xoa thông tin
     * @author ThanhNT
     */
    public void doConfirmDelete(FinanceTypeDTO financeTypeDTODelete) {
        this.financeTypeDTODelete = financeTypeDTODelete;

    }

    /**
     * ham xu ly xoa thông tin
     * @author ThanhNT
     */
    public void doConfirmListDelete() {
        if (DataUtil.isNullOrEmpty(lsFinanceTypeSelect)) {
            reportError("",ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "common.error.noselect.record");
        }
    }

    /**
     * hien thi title dialog
     * @author ThanhNT
     */
    @Secured("@")
    public String getTitleDlgFinance() {
        if (financeTypeDTO != null && financeTypeDTO.getId() == null) {
            return getText("mn.stock.limit.financeType.dlg.insert.title");
        }
        return getText("mn.stock.limit.financeType.dlg.edit.title");
    }

    /**
     * @author ThanhNT ham xu ly lay ra ten trang thai
     * @param status
     * @return
     */
    @Secured("@")
    public String getStrStatus(String status) {
        String result = "";
        if (!DataUtil.isNullOrEmpty(status)) {
            switch (status) {
                case "1":
                    result = getText("common.type.status.active");
                    break;
                case "0":
                    result = getText("common.type.status.inactive");
                    break;
            }
        }
        return result;
    }

    public void receiveShop(VShopStaffDTO vShopStaffDTO) {
        System.out.println("r53");
        staffInfoTag.initStaff(this, vShopStaffDTO.getOwnerId());
    }

    //getter and setter
    public List<OptionSetValueDTO> getLsLimitOptionType() {
        return lsLimitOptionType;
    }
    public void setLsLimitOptionType(List<OptionSetValueDTO> lsLimitOptionType) {
        this.lsLimitOptionType = lsLimitOptionType;
    }
    public FinanceTypeDTO getFinanceTypeDTOSearch() {
        return financeTypeDTOSearch;
    }
    public void setFinanceTypeDTOSearch(FinanceTypeDTO financeTypeDTOSearch) {
        this.financeTypeDTOSearch = financeTypeDTOSearch;
    }
    public List<FinanceTypeDTO> getLsFinanceType() {
        return lsFinanceType;
    }
    public void setLsFinanceType(List<FinanceTypeDTO> lsFinanceType) {
        this.lsFinanceType = lsFinanceType;
    }
    public FinanceTypeDTO getFinanceTypeDTO() {
        return financeTypeDTO;
    }
    public void setFinanceTypeDTO(FinanceTypeDTO financeTypeDTO) {
        this.financeTypeDTO = financeTypeDTO;
    }

    public ShopInfoNameable getShopInfoTag() {
        return shopInfoTag;
    }

    public void setShopInfoTag(ShopInfoNameable shopInfoTag) {
        this.shopInfoTag = shopInfoTag;
    }

    public List<FinanceTypeDTO> getLsFinanceTypeSelect() {
        return lsFinanceTypeSelect;
    }

    public void setLsFinanceTypeSelect(List<FinanceTypeDTO> lsFinanceTypeSelect) {
        this.lsFinanceTypeSelect = lsFinanceTypeSelect;
    }

    public Map<String, String> getMapLimitOption() {
        return mapLimitOption;
    }

    public void setMapLimitOption(Map<String, String> mapLimitOption) {
        this.mapLimitOption = mapLimitOption;
    }
}
