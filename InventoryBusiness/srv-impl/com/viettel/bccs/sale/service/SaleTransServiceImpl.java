package com.viettel.bccs.sale.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.sale.dto.SaleTransDTO;
import com.viettel.bccs.sale.model.SaleTrans;
import com.viettel.bccs.sale.repo.SaleTransRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Service
public class SaleTransServiceImpl extends BaseServiceImpl implements SaleTransService {
    private static final Logger logger = Logger.getLogger(SaleTransService.class);

    private final BaseMapper<SaleTrans, SaleTransDTO> mapper = new BaseMapper<>(SaleTrans.class, SaleTransDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_SALE)
    private EntityManager em;

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.ANYPAY, type = PersistenceContextType.TRANSACTION)
    private EntityManager emAnypay;

    @Autowired
    private SaleTransRepo repository;

    @Override
    @WebMethod
    public SaleTransDTO findOne(Long id) {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @Override
    public SaleTransDTO save(SaleTransDTO saleTransDTO) throws Exception, LogicException {
        return mapper.toDtoBean(repository.saveAndFlush(mapper.toPersistenceBean(saleTransDTO)));
    }

    @Override
    public SaleTransDTO update(SaleTransDTO saleTransDTO) throws Exception, LogicException {
        return mapper.toDtoBean(repository.saveAndFlush(mapper.toPersistenceBean(saleTransDTO)));
    }

    @Override
    public List<SaleTransDTO> findSaleTransByStockTransId(Long stockTransId, Date saleTransDate) throws Exception, LogicException {
        return mapper.toDtoBean(repository.findSaleTransByStockTransId(stockTransId, saleTransDate));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInTransIdById(Long saleTransId, Long inTransId) throws Exception, LogicException {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" UPDATE   bccs_sale.sale_trans ");
        strQuery.append("    SET   in_trans_id = #inTransId ");
        strQuery.append("  WHERE   sale_trans_id = #saleTransId ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("inTransId", inTransId);
        query.setParameter("saleTransId", saleTransId);
        int count = query.executeUpdate();
        if (count > 0) {
            return true;
        }
        return false;
    }
}