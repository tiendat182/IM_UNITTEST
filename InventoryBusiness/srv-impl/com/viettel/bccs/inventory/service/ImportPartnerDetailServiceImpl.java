package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.model.ProductOffering;
import com.viettel.bccs.inventory.repo.ImportPartnerDetailRepo;
import com.viettel.bccs.inventory.dto.ImportPartnerDetailDTO;
import com.viettel.bccs.inventory.model.ImportPartnerDetail;
import com.viettel.bccs.inventory.repo.ProductOfferingRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

import org.apache.log4j.Logger;
import com.viettel.fw.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;

@Service
public class ImportPartnerDetailServiceImpl extends BaseServiceImpl implements ImportPartnerDetailService {

    private final BaseMapper<ImportPartnerDetail, ImportPartnerDetailDTO> mapper = new BaseMapper<>(ImportPartnerDetail.class, ImportPartnerDetailDTO.class);

    @Autowired
    private ProductOfferingRepo productOfferingRepo;

    @Autowired
    private ImportPartnerDetailRepo repository;
    public static final Logger logger = Logger.getLogger(ImportPartnerDetailService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ImportPartnerDetailDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ImportPartnerDetailDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ImportPartnerDetailDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage saveImportPartnerDetail(ImportPartnerDetailDTO dto) throws Exception {
        BaseMessage msg = new BaseMessage();
        repository.save(mapper.toPersistenceBean(dto));
        return msg;
    }

    @Override
    public List<ImportPartnerDetailDTO> findImportPartnerDetail(ImportPartnerDetailDTO detailDTO) throws Exception {
        return mapper.toDtoBean(repository.findImportPartnerDetail(detailDTO));
    }

    @Override
    public void doValidateImportPartnerDetail(ImportPartnerDetailDTO importPartnerDetailDTO) throws LogicException, Exception {
        if (DataUtil.isNullObject(importPartnerDetailDTO.getProdOfferId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.stock.inputText.require.msg");
        }
        if (DataUtil.isNullObject(importPartnerDetailDTO.getStateId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stockTrans.validate.state.empty");
        }

        if (DataUtil.isNullObject(importPartnerDetailDTO.getQuantity())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "common.number.not.emty");
        }

        if (importPartnerDetailDTO.getQuantity() <= 0) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "export.order.stock.number.format.msg");
        }

        ProductOffering prod = productOfferingRepo.findOne(importPartnerDetailDTO.getProdOfferId());
        if (DataUtil.isNullObject(prod) || prod.getStatus() == null || !prod.getStatus().equals(Const.STATUS_ACTIVE)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.request.valid.prod");
        }

        if (!Const.ConfigIDCheck.importPartnerStateIDs.validate(importPartnerDetailDTO.getStateId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "import.partner.request.valid.state");
        }

    }
}
