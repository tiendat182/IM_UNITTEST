package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.LookupSerialCardAndKitByFileDTO;
import com.viettel.bccs.inventory.dto.StockKitDTO;
import com.viettel.bccs.inventory.model.StockKit;
import com.viettel.bccs.inventory.repo.StockKitRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import java.util.List;

@Service
public class StockKitServiceImpl extends BaseServiceImpl implements StockKitService {

    private final BaseMapper<StockKit, StockKitDTO> mapper = new BaseMapper<>(StockKit.class, StockKitDTO.class);

    @Autowired
    private StockKitRepo repository;
    public static final Logger logger = Logger.getLogger(StockKitService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockKitDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockKitDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockKitDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(StockKitDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockKitDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public StockKitDTO getStockKitBySerial(String serial) throws Exception {
        return repository.getStockKitBySerial(serial);
    }

    @Override
    @WebMethod
    public List<StockKitDTO> getStockKitBySerialAndProdOfferId(Long staffId, String fromSerial, String toSerial, Long prodOfferId) throws Exception, LogicException {
        if (DataUtil.isNullOrEmpty(fromSerial)
                || DataUtil.isNullOrEmpty(toSerial)) {
            throw new LogicException("", getText("mn.product.stockKit.validate.input.service.serial"));
        }
        if (DataUtil.isNullOrZero(prodOfferId)) {
            throw new LogicException("", getText("mn.product.stockKit.validate.input.service.prodOfferId"));
        }
        return repository.getStockKitBySerialAndProdOfferId(staffId, fromSerial, toSerial, prodOfferId);
    }

    @WebMethod
    @Override
    public List<LookupSerialCardAndKitByFileDTO> getSerialList(List<String> serial) throws Exception {
        if (!DataUtil.isNullOrEmpty(serial)) {
            //try {
            return repository.getSerialList(serial);
            //} catch (Exception e) {
            //    logger.debug(e.getMessage(), e);
            //    return null;
            //}
        }
        return null;
    }
}
