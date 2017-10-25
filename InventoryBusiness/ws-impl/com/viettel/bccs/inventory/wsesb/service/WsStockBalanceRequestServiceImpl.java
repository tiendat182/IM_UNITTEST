package com.viettel.bccs.inventory.wsesb.service;

import com.viettel.bccs.inventory.dto.ProductOfferingTotalDTO;
import com.viettel.bccs.inventory.dto.StockBalanceDetailDTO;
import com.viettel.bccs.inventory.dto.StockBalanceRequestDTO;
import com.viettel.bccs.inventory.dto.StockBalanceSerialDTO;
import com.viettel.bccs.inventory.service.StockBalanceRequestService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.ws.provider.CxfWsClientFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import java.util.List;

@Service("WsStockBalanceRequestServiceImpl")
public class WsStockBalanceRequestServiceImpl implements StockBalanceRequestService {

    public static final Logger logger = Logger.getLogger(WsStockBalanceRequestServiceImpl.class);

    @Autowired
    @Qualifier("cxfWsClientFactory")
    CxfWsClientFactory wsClientFactory;

    private StockBalanceRequestService client;

    @PostConstruct
    public void init() throws Exception {
        try {
            client = wsClientFactory.createWsClient(StockBalanceRequestService.class);
        } catch (Exception ex) {
            logger.error("init", ex);
        }
    }

    @WebMethod
    @Override
    public StockBalanceRequestDTO findOne(Long id) throws Exception {
        return client.findOne(id);
    }

    @WebMethod
    @Override
    public List<StockBalanceRequestDTO> findAll() throws Exception {
        return client.findAll();
    }

    @WebMethod
    @Override
    public List<StockBalanceRequestDTO> findByFilter(List<FilterRequest> filterList) throws Exception {
        return client.findByFilter(filterList);
    }

    @WebMethod
    @Override
    public Long count(List<FilterRequest> filterList) throws Exception {
        return client.count(filterList);
    }

    @WebMethod
    @Override
    public BaseMessage create(StockBalanceRequestDTO dto) throws Exception {
        return client.create(dto);
    }

    @Override
    @WebMethod
    public StockBalanceRequestDTO save(StockBalanceRequestDTO stockBalanceRequestDTO) throws Exception {
        return client.save(stockBalanceRequestDTO);
    }

    @Override
    @WebMethod
    public List<StockBalanceRequestDTO> searchStockBalanceRequest(StockBalanceRequestDTO stockBalanceRequestDTO) throws LogicException, Exception {
        return client.searchStockBalanceRequest(stockBalanceRequestDTO);
    }

    @Override
    @WebMethod
    public void saveStockBalanceRequest(StockBalanceRequestDTO stockBalanceRequestDTO, List<StockBalanceDetailDTO> lstDetailDTO) throws LogicException, Exception {
        client.saveStockBalanceRequest(stockBalanceRequestDTO, lstDetailDTO);
    }

    @Override
    @WebMethod
    public void validateStockBalanceRequest(StockBalanceRequestDTO stockBalanceRequestDTO, List<StockBalanceDetailDTO> lstDetailDTO) throws LogicException, Exception {
        client.validateStockBalanceRequest(stockBalanceRequestDTO, lstDetailDTO);
    }

    @Override
    @WebMethod
    public List<ProductOfferingTotalDTO> getProductOfferingExport(Long ownerId, Long ownerType, Long prodOfferId) throws LogicException, Exception {
        return client.getProductOfferingExport(ownerId, ownerType, prodOfferId);
    }

    @Override
    @WebMethod
    public List<ProductOfferingTotalDTO> getProductOfferingImport(Long ownerType, Long ownerId, Long prodOfferId) throws Exception {
        return client.getProductOfferingImport(ownerType, ownerId, prodOfferId);
    }

    @Override
    @WebMethod
    public void doApproveStockBalanceRequest(StockBalanceRequestDTO stockBalanceRequestDTO) throws LogicException, Exception {
        client.doApproveStockBalanceRequest(stockBalanceRequestDTO);
    }

    @Override
    @WebMethod
    public Long getMaxId() throws LogicException, Exception {
        return client.getMaxId();
    }

    @Override
    @WebMethod
    public BaseMessage update(StockBalanceRequestDTO productSpecCharacterDTO) throws Exception {
        return client.update(productSpecCharacterDTO);
    }

    @Override
    @WebMethod
    public void updateRequest(StockBalanceRequestDTO stockBalanceRequestDTO) throws LogicException, Exception {
        client.updateRequest(stockBalanceRequestDTO);
    }
}