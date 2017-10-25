package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ReceiptExpenseDTO;
import com.viettel.bccs.inventory.model.ReceiptExpense;
import com.viettel.bccs.inventory.repo.ReceiptExpenseRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
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
public class ReceiptExpenseServiceImpl extends BaseServiceImpl implements ReceiptExpenseService {

    private final BaseMapper<ReceiptExpense, ReceiptExpenseDTO> mapper = new BaseMapper(ReceiptExpense.class, ReceiptExpenseDTO.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    @Autowired
    private ReceiptExpenseRepo repository;
    public static final Logger logger = Logger.getLogger(ReceiptExpenseService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public ReceiptExpenseDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<ReceiptExpenseDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<ReceiptExpenseDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ReceiptExpenseDTO create(ReceiptExpenseDTO dto, String staffCode) throws Exception {
        if (isDulicateReceiptNo(dto.getReceiptNo(), dto.getReceiptId())) {
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_DUPLICATE, "agency.deposit.duplicate.receipt.no", dto.getReceiptNo());
        }
        try {
            Date currentDate = getSysDate(em);
            if (!DataUtil.isNullObject(dto)) {
                dto.setCreateDatetime(currentDate);
                dto.setReceiptDate(currentDate);
            }
            dto.setUsername(staffCode);
            ReceiptExpense receiptExpense = repository.save(mapper.toPersistenceBean(dto));
            return mapper.toDtoBean(receiptExpense);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public ReceiptExpenseDTO update(ReceiptExpenseDTO dto, String status) throws Exception {
        try {
            ReceiptExpenseDTO receiptExpenseDTO = findOne(dto.getReceiptId());
            receiptExpenseDTO.setStatus(status);
            ReceiptExpense receiptExpense = repository.save(mapper.toPersistenceBean(receiptExpenseDTO));
            return mapper.toDtoBean(receiptExpense);
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    private boolean isDulicateReceiptNo(String receiptNo, Long receiptId) throws Exception {
        try {
            List<FilterRequest> lst = Lists.newArrayList();
            lst.add(new FilterRequest(ReceiptExpense.COLUMNS.RECEIPTNO.name(), FilterRequest.Operator.EQ, receiptNo));
            lst.add(new FilterRequest(ReceiptExpense.COLUMNS.STATUS.name(), FilterRequest.Operator.EQ, Const.STATUS_ACTIVE));
            if (!DataUtil.isNullOrZero(receiptId)) {
                lst.add(new FilterRequest(ReceiptExpense.COLUMNS.RECEIPTID.name(), FilterRequest.Operator.NE, receiptId));
            }
            return repository.count(repository.toPredicate(lst)) > 0 ? true : false;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "common.error.happened");
        }
    }

    @Override
    public ReceiptExpenseDTO findByStockTransIdAndType(Long stockTransId, String type) throws Exception, LogicException {
        return repository.findByStockTransIdAndType(stockTransId, type);
    }

    @Override
    public String generateReceiptNo(String shopCode) throws Exception, LogicException {
        String receiptNoFormmat = Const.STOCK_TRANS.TRANS_CODE_DEPOSIT_PT + "_" + shopCode + "_" + DateUtil.dateToStringWithPattern(new Date(), "yyMM");
        List<ReceiptExpense> receiptExpenses = repository.getReceiptExpenseFromReceiptNo(receiptNoFormmat);
        List<ReceiptExpenseDTO> receiptExpenseDTOs = mapper.toDtoBean(receiptExpenses);
        String strMaxReceiptNo = "";
        Long maxReceiptNo = 0L;
        if (!DataUtil.isNullOrEmpty(receiptExpenseDTOs)) {
            for (ReceiptExpenseDTO receiptExpenseDTO : receiptExpenseDTOs) {
                String strTemp = receiptExpenseDTO.getReceiptNo().trim().replace(receiptNoFormmat, "");
                Long longTemp = DataUtil.safeToLong(strTemp);
                if (longTemp > maxReceiptNo) {
                    maxReceiptNo = longTemp;
                }
            }
            strMaxReceiptNo = String.valueOf(maxReceiptNo + 1L);
        }
        if (strMaxReceiptNo.length() < 3) {
            if (strMaxReceiptNo.length() == 0) {
                strMaxReceiptNo = "0";
            }
            for (int j = 0; j <= 3 - strMaxReceiptNo.length(); j++) {
                strMaxReceiptNo = "0" + strMaxReceiptNo;
            }
        }
        return receiptNoFormmat + strMaxReceiptNo;
    }

}
