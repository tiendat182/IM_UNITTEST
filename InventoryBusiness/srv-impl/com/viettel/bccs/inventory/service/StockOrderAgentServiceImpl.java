package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockOrderAgent;
import com.viettel.bccs.inventory.repo.StockOrderAgentRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Service
public class StockOrderAgentServiceImpl extends BaseServiceImpl implements StockOrderAgentService {

    private final BaseMapper<StockOrderAgent, StockOrderAgentDTO> mapper = new BaseMapper<>(StockOrderAgent.class, StockOrderAgentDTO.class);

    @Autowired
    private StockOrderAgentRepo repository;
    public static final Logger logger = Logger.getLogger(StockOrderAgentService.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private StockOrderAgentDetailService stockOrderAgentDetailService;
    @Autowired
    private OptionSetValueService optionSetValueService;


    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockOrderAgentDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockOrderAgentDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockOrderAgentDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public void createRequestStockOrder(StockOrderAgentDTO dto, List<StockOrderAgentDetailDTO> listDTODetail, StaffDTO currentStaff) throws Exception, LogicException {
        if (!DataUtil.isNullObject(dto)) {
            if (!DataUtil.isNullObject(dto.getBankCode())) {
                dto.setBankCode(dto.getBankCode().trim());
            }
            if (!DataUtil.isNullObject(dto.getRequestCode())) {
                dto.setRequestCode(dto.getRequestCode().trim());
            }
        }
        checkValidate(dto, false);
        checkValidateDetail(listDTODetail);
        // Validate ma la duy nhat
        if (isDulicate(dto, dto.getStockOrderAgentId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "stockOrderAgent.duplicate.requestCode", dto.getRequestCode());
        }

        Date currentDate = getSysDate(em);
        dto.setCreateDate(currentDate);
        dto.setLastModify(currentDate);
        dto.setShopId(currentStaff.getShopId());
        dto.setCreateStaffId(currentStaff.getStaffId());
        dto = mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
        //insert vao bang stock_order_agent_detail;
        for (StockOrderAgentDetailDTO detailDTO : listDTODetail) {
            detailDTO.setCreateDate(currentDate);
            detailDTO.setNote(dto.getNote());
            detailDTO.setStockOrderAgentId(dto.getStockOrderAgentId());
            stockOrderAgentDetailService.create(detailDTO);
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockOrderAgentDTO update(StockOrderAgentDTO dto) throws Exception, LogicException {
        if (!DataUtil.isNullObject(dto)) {
            if (!DataUtil.isNullObject(dto.getBankCode())) {
                dto.setBankCode(dto.getBankCode().trim());
            }
            if (!DataUtil.isNullObject(dto.getRequestCode())) {
                dto.setRequestCode(dto.getRequestCode().trim());
            }
        }
        checkValidate(dto, true);
        // Validate ma la duy nhat
        if (isDulicate(dto, dto.getStockOrderAgentId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "stockOrderAgent.duplicate.requestCode", dto.getRequestCode());
        }
        Date currentDate = getSysDate(em);
        dto.setLastModify(currentDate);
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @WebMethod
    public Long getMaxStockOrderAgentId() throws Exception {
        return repository.getMaxStockOrderAgentId();
    }

    private void checkValidate(StockOrderAgentDTO dto, boolean isUpdate) throws Exception {
        try {
            //check validate
            if (DataUtil.isNullObject(dto)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stockOrderAgent.validate.null");
            }
            //require
            if (isUpdate && DataUtil.isNullOrZero(dto.getStockOrderAgentId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stockOrderAgent.require.stockOrderAgentId");
            }
            if (DataUtil.isNullOrEmpty(dto.getRequestCode())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "create.request.import.request.code.require.msg");
            }
            if (DataUtil.isNullOrEmpty(dto.getBankCode())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stockOrderAgent.require.bankCode");
            }
            if (DataUtil.isNullObject(dto.getCreateDate())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.invoice.invoiceType.dateCreate.not.blank");
            }

            //maxlength
            if (dto.getRequestCode().getBytes("UTF-8").length > 25) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockOrderAgent.validate.maxlength.requestCode");
            }
            if (dto.getBankCode().getBytes("UTF-8").length > 50) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockOrderAgent.validate.maxlength.bankCode");
            }
            if (!DataUtil.isNullObject(dto.getNote()) && dto.getNote().getBytes("UTF-8").length > 500) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockOrderAgent.note.validate.maxlength");
            }
//            // contains in database - selectItems
//            staffDTO = staffService.getStaffByStaffCode(currentStaff.getStaffCode());
//            if (DataUtil.isNullObject(staffDTO)) {
//                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate");
//            }
        } catch (LogicException ex) {
            throw ex;
        }
    }

    private void checkValidateDetail(List<StockOrderAgentDetailDTO> listDTODetail) throws Exception {
        try {
            //check validate
            if (DataUtil.isNullOrEmpty(listDTODetail)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "export.order.stock.not.empty.list.product");
            } else {
                for (StockOrderAgentDetailDTO dto : listDTODetail) {
                    if (DataUtil.isNullObject(productOfferingService.findOne(dto.getProdOfferId()))) {
                        throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "import.partner.request.valid.prod");
                    }
                }
            }
        } catch (LogicException ex) {
            throw ex;
        }
    }

    private boolean isDulicate(StockOrderAgentDTO dto, Long stockOrderAgentId) throws Exception {
        try {
            List<FilterRequest> lst = Lists.newArrayList();
            lst.add(new FilterRequest(StockOrderAgent.COLUMNS.REQUESTCODE.name(), FilterRequest.Operator.EQ, dto.getRequestCode(), false));
            if (!DataUtil.isNullOrZero(stockOrderAgentId)) {
                lst.add(new FilterRequest(StockOrderAgent.COLUMNS.STOCKORDERAGENTID.name(), FilterRequest.Operator.NE, stockOrderAgentId));
            }
            return repository.count(repository.toPredicate(lst)) > 0 ? true : false;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    public List<StockOrderAgentDTO> search(StockOrderAgentDTO stockOrderAgentDTO) throws Exception, LogicException {
        return mapper.toDtoBean(repository.search(stockOrderAgentDTO));
    }

    @Override
    public List<StockOrderAgentDTO> getStockOrderAgent(Long shopId, Long stockTransId) throws Exception, LogicException {
        List<StockOrderAgentDTO> result ;
        StockOrderAgentDTO stockOrderAgentDTO = new StockOrderAgentDTO();
        stockOrderAgentDTO.setShopId(shopId);
        stockOrderAgentDTO.setStockTransId(stockTransId);
        result = mapper.toDtoBean(repository.getStockOrderAgent(stockOrderAgentDTO));
        if (DataUtil.isNullOrEmpty(result)) {
            List<OptionSetValueDTO> lstConfigEnableBccs1 = optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1");
            if (!DataUtil.isNullOrEmpty(lstConfigEnableBccs1)
                    && !DataUtil.isNullObject(lstConfigEnableBccs1.get(0).getValue())
                    && Const.ENABLE_UPDATE_BCCS1.equals(DataUtil.safeToLong(lstConfigEnableBccs1.get(0).getValue()))) {
                result = mapper.toDtoBean(repository.getStockOrderAgentIM1(stockOrderAgentDTO));
            }
        }
        return result;
    }

    @Override
    public List<StockOrderAgentForm> getStockOrderAgentForm(StockOrderAgentDTO searchForm, StaffDTO currentStaff) {
        return repository.getStockOrderAgentForm(searchForm, currentStaff);
    }

    @Override
    public List checkShopAgent(Long shopId) {
        return repository.checkShopAgent(shopId);
    }
}
