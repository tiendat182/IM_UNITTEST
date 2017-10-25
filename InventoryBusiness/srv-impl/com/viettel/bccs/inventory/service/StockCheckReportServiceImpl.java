package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ShopDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.dto.StockCheckReportDTO;
import com.viettel.bccs.inventory.model.StockCheckReport;
import com.viettel.bccs.inventory.repo.StockCheckReportRepo;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class StockCheckReportServiceImpl extends BaseServiceImpl implements StockCheckReportService {

    private final BaseMapper<StockCheckReport, StockCheckReportDTO> mapper = new BaseMapper(StockCheckReport.class, StockCheckReportDTO.class);
    private String[] fileType = {".pdf"};
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private StockCheckReportRepo repository;
    public static final Logger logger = Logger.getLogger(StockCheckReportService.class);

    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    private ShopDTO shopDTO;
    private StaffDTO staffDTO;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockCheckReportDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockCheckReportDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockCheckReportDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    private void checkValidate(StockCheckReportDTO dto, String staffCode) throws Exception {
        try {
            //check validate
            if (DataUtil.isNullObject(dto)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.check.report.validate.null");
            }
            //require
            if (DataUtil.isNullOrZero(dto.getOwnerId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.check.report.validate.require.ownerId");
            }
            if (DataUtil.isNullOrEmpty(dto.getFileName()) || DataUtil.isNullObject(dto.getFileContent())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.check.report.validate.require.fileName");
            }
            if (DataUtil.isNullObject(dto.getCheckDate())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.check.report.validate.require.checkDate");
            }
            if (DataUtil.isNullOrEmpty(staffCode)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "staffCode.validate.require");
            }
            //maxlength
            if (dto.getFileName().length() > 200) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.check.report.validate.maxlength.fileName");
            }
            if (staffCode.length() > 50) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate.maxlength");
            }
            // contains in database - selectItems
            staffDTO = staffService.getStaffByStaffCode(staffCode);
            if (DataUtil.isNullObject(staffDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "staffCode.validate");
            }
            shopDTO = shopService.findOne(dto.getOwnerId());
            if (DataUtil.isNullObject(shopDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "shop.validate");
            }
            //file type
            if (!isCorrectExtension(dto.getFileName())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.limit.createFile.invalid.msg");
            }
            //Max size file
            if (dto.getFileContent().length > Const.FILE_MAX) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.check.report.validate.invalid.size");
            }
            //trim space
            dto.setFileName(dto.getFileName().trim());
        } catch (LogicException ex) {
            throw ex;
        }
    }

    private boolean isCorrectExtension(String extension) {
        boolean test = false;
        for (int i = 0; i < fileType.length; i++) {
            if (extension.contains(fileType[i])) {
                test = true;
                break;
            }
        }
        return test;
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockCheckReportDTO uploadFileStockCheckReport(StockCheckReportDTO dto, String staffCode) throws Exception, LogicException {
        checkValidate(dto, staffCode);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dto.getCheckDate());
        int month = cal.get(Calendar.MONTH) + 1;
        if (!repository.isDuplicate(dto.getOwnerId(), dto.getCheckDate())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "stock.check.report.duplicate", shopDTO.getShopCode(), shopDTO.getName(), month + "/" + cal.get(Calendar.YEAR));
        }
        try {
            Date currentDate = getSysDate(em);
            dto.setCreateUser(staffCode);
            dto.setCreateDate(currentDate);
            dto.setOwnerCode(shopDTO.getShopCode());
            dto.setOwnerName(shopDTO.getName());
            dto.setOwnerType(Short.parseShort(Const.BRAS.SHOP_TYPE));
            StockCheckReport stockCheckReport = mapper.toPersistenceBean(dto);
            stockCheckReport = repository.save(stockCheckReport);
            return mapper.toDtoBean(stockCheckReport);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @Override
    public List<StockCheckReportDTO> onSearch(Long shopId, Date checkDate) throws Exception, LogicException {
        return repository.onSearch(shopId, checkDate);
    }

    @Override
    public byte[] getFileStockCheckReport(Long stockCheckReportId) throws Exception, LogicException {
        try {
            StockCheckReportDTO stockCheckReportDTO = findOne(stockCheckReportId);
            if (DataUtil.isNullObject(stockCheckReportDTO)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "stock.check.report.not.exsit");
            }
            byte[] fileContent = stockCheckReportDTO.getFileContent();
            if (DataUtil.isNullObject(fileContent)) {
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "stock.check.report.file.not.exsit");
            }
            return fileContent;
        } catch (LogicException e) {
            throw e;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }
}
