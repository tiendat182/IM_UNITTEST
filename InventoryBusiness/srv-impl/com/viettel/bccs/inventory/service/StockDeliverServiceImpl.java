package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.SignFlowDetailDTO;
import com.viettel.bccs.inventory.dto.StockDeliverDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransVofficeDTO;
import com.viettel.bccs.inventory.repo.StockDeliverRepo;
import com.viettel.bccs.inventory.dto.StockDeliverDTO;
import com.viettel.bccs.inventory.model.StockDeliver;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.viettel.fw.service.BaseServiceImpl;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

@Service
public class StockDeliverServiceImpl extends BaseServiceImpl implements StockDeliverService {

    public static final String STOCK_TRANS_VOFFICE_SEQ = "STOCK_TRANS_VOFFICE_SEQ";
    public static final String TEN_ZEZO = "0000000000";

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;
    private final BaseMapper<StockDeliver, StockDeliverDTO> mapper = new BaseMapper<>(StockDeliver.class, StockDeliverDTO.class);

    @Autowired
    private StockDeliverRepo repository;
    public static final Logger logger = Logger.getLogger(StockDeliverService.class);

    @Autowired
    private StockDeliverDetailService stockDeliverDetailService;

    @Autowired
    private SignFlowDetailService signFlowDetailService;

    @Autowired
    private StockTransVofficeService stockTransVofficeService;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockDeliverDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockDeliverDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockDeliverDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockDeliverDTO save(StockDeliverDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(StockDeliverDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public void deliverStock(StockDeliverDTO stockDeliverDTO) throws LogicException, Exception {
        Date currentDate = getSysDate(em);
        //save stock_deliver
        StockDeliverDTO stockDeliver = DataUtil.cloneBean(stockDeliverDTO);
        stockDeliver.setStatus(Const.STOCK_DELIVER.STOCK_DELIVER_STATUS_NEW);
        stockDeliver.setCreateDate(currentDate);
        stockDeliver.setUpdateDate(currentDate);
        if (!repository.isDulicateCode(stockDeliverDTO.getCode())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.deliver.validate.code");
        }
        StockDeliver stockDeliverSave = repository.save(mapper.toPersistenceBean(stockDeliver));
        //save stock_deliver_detail
        List<StockDeliverDetailDTO> lstDetail = stockDeliverDetailService.viewStockTotalFull(stockDeliverDTO.getOwnerId(), Const.OWNER_TYPE.SHOP_LONG);
        if (!DataUtil.isNullOrEmpty(lstDetail)) {
            for (StockDeliverDetailDTO stockDeliverDetailDTO : lstDetail) {
                stockDeliverDetailDTO.setStockDeliverId(stockDeliverSave.getStockDeliverId());
                stockDeliverDetailDTO.setCreateDate(currentDate);
                stockDeliverDetailService.save(stockDeliverDetailDTO);
            }
        }
        //sign voffice
        List<SignFlowDetailDTO> lstSignFlowDetail = signFlowDetailService.findBySignFlowId(stockDeliverDTO.getSignFlowId());
        if (DataUtil.isNullOrEmpty(lstSignFlowDetail)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.signFlow.emptyDetail");
        }
        StringBuilder lstUser = new StringBuilder("");
        if (!DataUtil.isNullOrEmpty(lstSignFlowDetail)) {
            for (SignFlowDetailDTO signFlowDetailDTO : lstSignFlowDetail) {
                if (DataUtil.isNullOrEmpty(signFlowDetailDTO.getEmail())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stockTrans.validate.signFlow.emptyEmail");
                }
                if (!DataUtil.isNullOrEmpty(lstUser.toString())) {
                    lstUser.append(",");
                }
                lstUser.append(signFlowDetailDTO.getEmail().trim().substring(0, signFlowDetailDTO.getEmail().lastIndexOf("@")));
            }

            StockTransVofficeDTO stockTransVoffice = new StockTransVofficeDTO();
            String stVofficeId = DateUtil.dateToStringWithPattern(currentDate, "yyMMdd") + DataUtil.customFormat(TEN_ZEZO, getSequence(em, STOCK_TRANS_VOFFICE_SEQ));
            stockTransVoffice.setStockTransVofficeId(stVofficeId);
            stockTransVoffice.setAccountName(stockDeliverDTO.getUserName().trim());
            stockTransVoffice.setAccountPass(stockDeliverDTO.getPassWord());
            stockTransVoffice.setSignUserList(lstUser.toString());
            stockTransVoffice.setCreateDate(currentDate);
            stockTransVoffice.setLastModify(currentDate);
            stockTransVoffice.setStatus(Const.STATUS.NO_ACTIVE);
            stockTransVoffice.setStockTransActionId(stockDeliverSave.getStockDeliverId());
            stockTransVoffice.setCreateUserId(stockDeliver.getCreateUserId());
            //check template
            stockTransVoffice.setPrefixTemplate(Const.STOCK_DELIVER_PREFIX_TEMPLATE);
            stockTransVofficeService.save(stockTransVoffice);
        }
    }

    public StockDeliverDTO getStockDeliverByOwnerIdAndStatus(Long ownerId, Long status) throws LogicException, Exception {
        return repository.getStockDeliverByOwnerIdAndStatus(ownerId, status);
    }

    public List<Long> getAllStockForDelete() throws LogicException, Exception {
        return repository.getAllStockForDelete();
    }

    public List<StockDeliverDTO> searchHistoryStockDeliver(String code, Date fromDate, Date toDate, String status, Long ownerId, Long ownerType) throws Exception {
        return repository.searchHistoryStockDeliver(code, fromDate, toDate, status, ownerId, ownerType);
    }

    @WebMethod
    public Long getMaxId() throws LogicException, Exception{
        return repository.getMaxId();
    }
}
