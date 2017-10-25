package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.expr.BooleanExpression;
import com.viettel.bccs.inventory.dto.StockDeviceTransferDTO;
import com.viettel.bccs.inventory.model.QStockDeviceTransfer;
import com.viettel.bccs.inventory.model.StockDeviceTransfer;
import com.viettel.bccs.inventory.repo.DeviceConfigRepo;
import com.viettel.bccs.inventory.repo.StockDeviceTransferRepo;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import java.util.List;

@Service
public class StockDeviceTransferServiceImpl extends BaseServiceImpl implements StockDeviceTransferService {

    private final BaseMapper<StockDeviceTransfer,StockDeviceTransferDTO> mapper = new BaseMapper<>(StockDeviceTransfer.class,StockDeviceTransferDTO.class);

    @Autowired
    private StockDeviceTransferRepo repo;
    @Autowired
    private DeviceConfigRepo deviceConfigRepo;
    public static final Logger logger = Logger.getLogger(StockDeviceTransferService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repo.count(repo.toPredicate(filters));
    }

    @WebMethod
    public StockDeviceTransferDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repo.findOne(id));
    }

    @WebMethod
    public List<StockDeviceTransferDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repo.findAll(repo.toPredicate(filters)));
    }

    @WebMethod
    @Override
    public String getDeviceConfigStateStrBy(Long probOfferId, Short probOfferStatus, Long probNewOfferId) {

        BooleanExpression predicate = QStockDeviceTransfer.stockDeviceTransfer.prodOfferId.eq(probOfferId);
        predicate = predicate.and(QStockDeviceTransfer.stockDeviceTransfer.stateId.eq(probOfferStatus));
        predicate = predicate.and(QStockDeviceTransfer.stockDeviceTransfer.newProdOfferId.eq(probNewOfferId));

        List<StockDeviceTransfer> temp = Lists.newArrayList(repo.findAll(predicate));

        if(DataUtil.isNullOrEmpty(temp))
            return "";
        return deviceConfigRepo.getStateStr(Long.valueOf(temp.get(0).getNewStateId().toString()));
    }

}
