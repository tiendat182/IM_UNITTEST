package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.LookupInvoiceUsedDTO;
import com.viettel.bccs.inventory.model.QInvoiceUsed;
import com.viettel.bccs.inventory.repo.InvoiceUsedRepo;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pham on 9/26/2016.
 */
@Service
public class InvoiceUsedServiceImpl extends BaseServiceImpl implements InvoiceUsedService {
    public final static Logger logger = Logger.getLogger(InvoiceUsedService.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager em;

    @Autowired
    private InvoiceUsedRepo invoiceUsedRepo;

    @Override
    public LookupInvoiceUsedDTO getInvoiceUsed(String serial, Date fromDate, Date toDate) throws Exception {
        if (!DataUtil.isNullOrEmpty(serial) && fromDate != null && toDate != null) {
                return invoiceUsedRepo.getInvoiceUsed(serial, fromDate, toDate);
        }
        return null;
    }

    @Override
    public List<LookupInvoiceUsedDTO> getAllSerial() throws Exception {
        return invoiceUsedRepo.getAllSerial();
    }
}