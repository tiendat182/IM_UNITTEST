package com.viettel.bccs.inventory.service;


import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StaffIm1DTO;
import com.viettel.bccs.im1.repo.StaffIm1Repo;
import com.viettel.bccs.im1.service.StaffIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.controller.SolrController;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.Staff;
import com.viettel.bccs.inventory.repo.StaffRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import java.text.MessageFormat;
import java.util.List;

@Service
public class StaffServiceImpl extends BaseServiceImpl implements StaffService {

    private final BaseMapper<Staff, StaffDTO> mapper = new BaseMapper(Staff.class, StaffDTO.class);
    private final static String PATERN_NOTE_CODE = "^[a-zA-Z_]+$";


    @Autowired
    private StaffRepo repository;

    @Autowired
    private StaffIm1Repo staffIm1Repo;

    @Autowired
    private ShopService shopService;

    @Autowired
    private OptionSetValueService optionSetValueService;

    public static final Logger logger = Logger.getLogger(StaffService.class);

    @Autowired
    private StaffIm1Service staffIm1Service;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception, LogicException {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StaffDTO findOne(Long id) throws Exception, LogicException {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StaffDTO> findByFilter(List<FilterRequest> filters) throws Exception, LogicException {
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    public StaffDTO save(StaffDTO staffDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(staffDTO)));
    }

    @Override
    @WebMethod
    public StaffDTO getStaffByStaffCode(String staffCode) throws Exception {
        return repository.getStaffByStaffCode(staffCode);
    }

    @Override
    @WebMethod
    public List<Staff> getStaffList(Long staffOwnerId) throws Exception, LogicException {
        return repository.getStaffList(staffOwnerId);
    }

    @Override
    public List<StaffDTO> searchStaffByShopId(Long shopId) throws LogicException, Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(Staff.COLUMNS.SHOPID.name(), FilterRequest.Operator.EQ, shopId));
        return findByFilter(lst);
    }

    @WebMethod
    @Override
    public List<VShopStaffDTO> getStaffByShopId(String txtSearch, String shopId) throws LogicException, Exception {
        String query = "owner_type:" + Const.OWNER_TYPE.STAFF + " AND parent_shop_id:" + shopId;
        if (!DataUtil.isNullOrEmpty(txtSearch)) {
            query += " AND (owner_code:*{0}* OR owner_name:*{1}*)";
            query = MessageFormat.format(query, DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch));
        }
        List<VShopStaffDTO> lstStaff = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, query, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstStaff;
    }

    @WebMethod
    @Override
    public List<VShopStaffDTO> getStaffIsdnMngt(String txtSearch, String shopId) throws LogicException, Exception {

        String query = "owner_type:" + Const.OWNER_TYPE.STAFF + " AND point_of_sale:2 AND (parent_shop_id:" + shopId + " OR shop_path:*_" + shopId + "_*) ";
        if (!DataUtil.isNullOrEmpty(txtSearch)) {
            query += " AND (owner_code:*{0}* OR owner_name:*{1}*)";
            query = MessageFormat.format(query, DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch));
        }
        List<VShopStaffDTO> lstStaff = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, query, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstStaff;
    }

    @WebMethod
    @Override
    public List<VShopStaffDTO> getStaffIsdnMngtUsrCrt(String txtSearch, String shopId) throws LogicException, Exception {
        StringBuilder query = new StringBuilder(" status:1 AND owner_type:2 AND channel_type_id:14 ");
//                "owner_type:" + Const.OWNER_TYPE.STAFF + " AND point_of_sale:2 AND (parent_shop_id:" + shopId + " OR shop_path:*_" + shopId + "_*) " ;
        if (!DataUtil.isNullOrEmpty(shopId)) {
            query.append(" AND parent_shop_id:" + shopId);
        }
        if (!DataUtil.isNullOrEmpty(txtSearch)) {
            query.append(" AND (owner_code:*{0}* OR owner_name:*{1}*) ");
            query = new StringBuilder(MessageFormat.format(query.toString(), DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch)));
        }
        List<VShopStaffDTO> lstStaff = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, query.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstStaff;
    }

    @Override
    public List<VShopStaffDTO> getStaffDistribute(String txtSearch, String shopId) throws LogicException, Exception {
        String query = "owner_type:" + Const.OWNER_TYPE.STAFF + " AND point_of_sale:2 AND (parent_shop_id:" + shopId + " OR shop_path:*_" + shopId + "_*) "
                + " AND status:" + Const.STATUS_ACTIVE + " AND staff_owner_id:[* TO *] AND channel_type_id:10 ";
        if (!DataUtil.isNullOrEmpty(txtSearch)) {
            query += " AND (owner_code:*{0}* OR owner_name:*{1}*)";
            query = MessageFormat.format(query, DataUtil.replaceSpaceSolr(txtSearch), DataUtil.replaceSpaceSolr(txtSearch));
        }
        List<VShopStaffDTO> lstStaff = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, query, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstStaff;
    }

    @Override
    public List<VShopStaffDTO> getStaffByShopIdAndChanelType(StaffTagConfigDTO staffTagConfigDTO) throws LogicException, Exception {
        String query = "owner_type:" + Const.OWNER_TYPE.STAFF + " AND status:1 ";
        if (!DataUtil.isNullOrEmpty(staffTagConfigDTO.getShopID())) {
            VShopStaffDTO shopStaffDTO = shopService.getShopByOwnerId(staffTagConfigDTO.getShopID(), Const.OWNER_TYPE.SHOP);
            if (staffTagConfigDTO.getSearchMode() == Const.StaffSearchMode.allChild.value()) {
                query += " AND ( parent_shop_id:" + staffTagConfigDTO.getShopID()
                        + " OR shop_path:" + shopStaffDTO.getShopPath() + "_*)";
            } else if (staffTagConfigDTO.getSearchMode() == Const.StaffSearchMode.allChildNotCurrentStaff.value()) {
                Long branchId = shopService.getBranchId(DataUtil.safeToLong(staffTagConfigDTO.getShopID()));
                if (!DataUtil.isNullOrZero(branchId)) {
                    query += " AND ( parent_shop_id:" + staffTagConfigDTO.getShopID()
                            + " OR shop_path:*_" + branchId + "*)";
                } else {
                    query += " AND parent_shop_id:" + staffTagConfigDTO.getShopID();
                }
                if (!DataUtil.isNullObject(staffTagConfigDTO.getStaffID())) {
                    query += " AND NOT owner_id:" + staffTagConfigDTO.getStaffID();
                }
            } else if (staffTagConfigDTO.getSearchMode() == Const.StaffSearchMode.allChildShopAndParentShop.value()) {
                query += " AND (parent_shop_id:" + staffTagConfigDTO.getShopID() + " OR parent_shop_id:" + shopStaffDTO.getParentShopId() + ")";
            } else {
                query += " AND parent_shop_id:" + staffTagConfigDTO.getShopID();
            }
        }
        if (!DataUtil.isNullOrEmpty(staffTagConfigDTO.getChannelTypes())) {
            query += " AND (channel_type_id: (";
            for (String channelType : staffTagConfigDTO.getChannelTypes()) {
                query += channelType + " OR ";
            }
            if (query.endsWith("OR ")) {
                query = query.substring(0, query.length() - 4);
                query += ")";
            }
            query += ")";
        }

        if (!DataUtil.isNullOrEmpty(staffTagConfigDTO.getInputSearch())) {
            query += " AND (owner_code:*{0}* OR owner_name:*{1}*)";
            query = MessageFormat.format(query, DataUtil.replaceSpaceSolr(staffTagConfigDTO.getInputSearch()), DataUtil.replaceSpaceSolr(staffTagConfigDTO.getInputSearch()));
        }
        List<VShopStaffDTO> lstStaff = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, query, VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        return lstStaff;
    }

    @Override
    public String getTransCode(String prefixTransCode, String transType, StaffDTO staffDTO) throws Exception {
        // Tao trans_code tu IM1.
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            return staffIm1Repo.getTransCode(prefixTransCode, transType, staffDTO);
        } else {
            return repository.getTransCode(prefixTransCode, transType, staffDTO);
        }
    }

    public Boolean validateTransCode(String noteCode, Long staffId, String transType) throws Exception {

        StaffDTO staffDTO = new StaffDTO();

        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1) && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            StaffIm1DTO staffIm1DTO = staffIm1Service.findOne(staffId);
            staffDTO.setShopId(staffIm1DTO.getShopId());
            staffDTO.setStockNumImp(staffIm1DTO.getStockNumImp());
            staffDTO.setStockNum(staffIm1DTO.getStockNum());
        } else {
            staffDTO = findOne(staffId);
        }
        if (staffDTO == null) {
            return false;
        }
        if (!Const.SHOP.SHOP_VTT_ID.equals(staffDTO.getShopId())) {
            return true;
        }
        int index = noteCode.lastIndexOf("_");
        if (index == -1) {
            return false;
        }
        if (index + 7 > noteCode.length()) {
            return false;
        }
        String lastCode = noteCode.substring(noteCode.lastIndexOf("_") + 1, noteCode.lastIndexOf("_") + 7);
        Long noteNumber;
        Long currNum;
        if (Const.STOCK_TRANS_TYPE.IMPORT.equals(transType)) {
            currNum = staffDTO.getStockNumImp() != null ? staffDTO.getStockNumImp() + 1L : 1L;
        } else {
            currNum = staffDTO.getStockNum() != null ? staffDTO.getStockNum() + 1L : 1L;
        }

        try {
            noteNumber = Long.valueOf(lastCode);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }

        if (noteNumber.compareTo(currNum) > 0) {
            return false;
        }
        String noteCodeNotDetail = noteCode.substring(0, noteCode.lastIndexOf("_") + 7);
        String detail = noteCode.substring(noteCodeNotDetail.length(), noteCode.length());

        if (noteNumber.compareTo(currNum) < 0 && (detail == null || "".equals(detail))) {
            return false;
        }
        if (detail != null && !"".equals(detail) && !DataUtil.validateStringByPattern(detail.trim(), PATERN_NOTE_CODE)) {
            return false;
        }
        return true;
    }

    @Override
    public VShopStaffDTO findStaffById(String staffId) throws Exception {
        StringBuilder builder = new StringBuilder("owner_type:").append(Const.OWNER_TYPE.STAFF).append(" AND owner_id:").append(staffId);
        List<VShopStaffDTO> lstStaff = SolrController.doSearch(Const.SOLR_CORE.VSHOPSTAFF, builder.toString(), VShopStaffDTO.class, Const.NUMBER_SEARCH.NUMBER_ALL, "owner_name", Const.SORT_ORDER.ASC);
        if (!DataUtil.isNullOrEmpty(lstStaff)) {
            return lstStaff.get(0);
        }
        return null;
    }

    public String getTransCodeDeposit(String prefixTransCode, StaffDTO staffDTO) throws Exception {
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            return staffIm1Repo.getTransCodeDeposit(prefixTransCode, staffDTO);
        } else {
            return repository.getTransCodeDeposit(prefixTransCode, staffDTO);
        }
    }

    public String getTransCodeByStaffId(Long staffId) throws LogicException, Exception {
        List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
        if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
            return staffIm1Repo.getTransCodeByStaffId(staffId);
        } else {
            return repository.getTransCodeByStaffId(staffId);
        }
    }

    public StaffDTO getStaffByOwnerId(Long shopId, String staffCode) throws LogicException, Exception {
        List<FilterRequest> lst = Lists.newArrayList();
        lst.add(new FilterRequest(Staff.COLUMNS.SHOPID.name(), FilterRequest.Operator.EQ, shopId));
        lst.add(new FilterRequest(Staff.COLUMNS.STAFFCODE.name(), FilterRequest.Operator.EQ, DataUtil.safeToString(staffCode).trim().toLowerCase(), false));
        lst.add(new FilterRequest(Staff.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
        List<StaffDTO> lstResult = findByFilter(lst);
        if (!DataUtil.isNullOrEmpty(lstResult)) {
            return lstResult.get(0);
        }
        return null;
    }
}
