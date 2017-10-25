package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockRequestOrder;
import com.viettel.bccs.inventory.repo.ProductOfferingRepo;
import com.viettel.bccs.inventory.repo.StockRequestOrderDetailRepo;
import com.viettel.bccs.inventory.repo.StockRequestOrderRepo;
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
public class StockRequestOrderServiceImpl extends BaseServiceImpl implements StockRequestOrderService {

    public static final String STOCK_TRANS_VOFFICE_SEQ = "STOCK_TRANS_VOFFICE_SEQ";
    public static final String TEN_ZEZO = "0000000000";
    private final BaseMapper<StockRequestOrder, StockRequestOrderDTO> mapper = new BaseMapper<>(StockRequestOrder.class, StockRequestOrderDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private StockRequestOrderDetailService stockRequestOrderDetailService;

    @Autowired
    private ProductOfferingRepo productOfferingRepo;

    @Autowired
    private ProductOfferingService productOfferingService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private SignFlowDetailService signFlowDetailService;
    @Autowired
    private StockTransVofficeService stockTransVofficeService;
    @Autowired
    private StockRequestOrderDetailRepo stockRequestOrderDetailRepo;

    @Autowired
    private StockRequestOrderRepo repository;

    @Autowired
    private StockRequestOrderBaseService stockRequestOrderBaseService;

    public static final Logger logger = Logger.getLogger(StockRequestOrderService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockRequestOrderDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockRequestOrderDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockRequestOrderDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockRequestOrderDTO save(StockRequestOrderDTO stockRequestOrderDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockRequestOrderDTO)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockRequestOrderDTO update(StockRequestOrderDTO stockRequestOrderDTO) throws Exception {
        stockRequestOrderDTO.setUpdateDatetime(getSysDate(em));
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockRequestOrderDTO)));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockRequestOrderDTO createGoodOrderFromProvince(StockRequestOrderDTO stockRequestOrderDTO) throws LogicException, Exception {

        if (stockRequestOrderDTO == null || DataUtil.isNullOrEmpty(stockRequestOrderDTO.getLsRequestOrderDetailDTO())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.request.importData.empty");
        }

        Date sysdate = getSysDate(em);

        //insert StockRequestOrderDTO
        StockRequestOrderDTO orderDTOImport = DataUtil.cloneBean(stockRequestOrderDTO);
        orderDTOImport.setStatus(Const.STOCK_REQUEST_ORDER.STATUS_0_CREATE_REQUEST);
        orderDTOImport.setCreateDatetime(sysdate);
        orderDTOImport.setUpdateDatetime(sysdate);
        orderDTOImport.setOrderType(1L);
        StockRequestOrderDTO orderDTOSave = save(orderDTOImport);

        orderDTOSave.setOrderCode("RC_" + orderDTOSave.getStockRequestOrderId());
        save(orderDTOSave);

        //insert StockRequestOrderDetailDTO
        List<StockRequestOrderDetailDTO> detailDTOs = stockRequestOrderDTO.getLsRequestOrderDetailDTO();
        for (StockRequestOrderDetailDTO detailDTO : detailDTOs) {

            List<ProductOfferingTotalDTO> lsTotal = productOfferingRepo.getLsRequestProductByShop(detailDTO.getFromOwnerType(),
                    detailDTO.getFromOwnerId(), detailDTO.getProdOfferTypeId(), detailDTO.getProdOfferId(), detailDTO.getStateId());
            if (DataUtil.isNullOrEmpty(lsTotal)) {
                ProductOfferingDTO offeringDTO = productOfferingService.findOne(detailDTO.getProdOfferId());
                ShopDTO shopDTO = shopService.findOne(detailDTO.getFromOwnerId());
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.request.not.found.empty", offeringDTO.getCode(), shopDTO.getShopCode());
            }

            Long requireQuantityDB = DataUtil.safeToLong(lsTotal.get(0).getAvailableQuantity()) - DataUtil.safeToLong(lsTotal.get(0).getRequestQuantity());
            if (requireQuantityDB.compareTo(detailDTO.getRequestQuantity()) < 0) {
                ProductOfferingDTO offeringDTO = productOfferingService.findOne(detailDTO.getProdOfferId());
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.request.quantity.over",
                        offeringDTO.getCode(), requireQuantityDB, detailDTO.getRequestQuantity());
            }
            detailDTO.setApprovedQuantity(detailDTO.getRequestQuantity());
            detailDTO.setStockRequestOrderId(orderDTOSave.getStockRequestOrderId());
            detailDTO.setCreateDatetime(sysdate);
            detailDTO.setLastDatetime(sysdate);
            detailDTO.setStatus(DataUtil.safeToLong(orderDTOSave.getStatus()));
            stockRequestOrderDetailService.save(detailDTO);
        }
        return orderDTOSave;
    }

    @Override
    public List<StockRequestOrderDTO> findSearhStockOrder(String orderCode, Date fromDate, Date endDate, String status, Long ownerId, Long ownerType) throws Exception {
        return repository.findSearhStockOrder(orderCode, fromDate, endDate, status, ownerId, ownerType);
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public void approveStockRequestOrder(StockRequestOrderDTO stockRequestOrderDTO) throws LogicException, Exception {
        Date sysdate = getSysDate(em);
        stockRequestOrderDTO.setUpdateDatetime(sysdate);
        save(stockRequestOrderDTO);
        //neu la phe duyet thi validate ky vOffice
        if (Const.STOCK_REQUEST_ORDER.STATUS_1_APPROVE.equals(stockRequestOrderDTO.getStatus())) {
            SignOfficeDTO signOfficeDTO = stockRequestOrderDTO.getSignOfficeDTO();
            if (signOfficeDTO == null) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "mn.stock.voffice.empty");
            }
            List<SignFlowDetailDTO> lstSignFlowDetail = signFlowDetailService.findBySignFlowId(signOfficeDTO.getSignFlowId());

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
                String stVofficeId = DateUtil.dateToStringWithPattern(sysdate, "yyMMdd") + DataUtil.customFormat(TEN_ZEZO, getSequence(em, STOCK_TRANS_VOFFICE_SEQ));
                stockTransVoffice.setStockTransVofficeId(stVofficeId);
                stockTransVoffice.setAccountName(signOfficeDTO.getUserName().trim());
                stockTransVoffice.setAccountPass(signOfficeDTO.getPassWord());
                stockTransVoffice.setSignUserList(lstUser.toString());
                stockTransVoffice.setCreateDate(sysdate);
                stockTransVoffice.setLastModify(sysdate);
                stockTransVoffice.setCreateUserId(stockRequestOrderDTO.getActionStaffId());
                stockTransVoffice.setStatus(Const.STATUS.NO_ACTIVE);
                stockTransVoffice.setStockTransActionId(stockRequestOrderDTO.getStockRequestOrderId());
                //check template
                stockTransVoffice.setPrefixTemplate("GOODS_REVOKE");
                stockTransVoffice.setReceiptNo(stockRequestOrderDTO.getOrderCode());
                stockTransVoffice.setActionCode(stockRequestOrderDTO.getOrderCode());
                stockTransVofficeService.save(stockTransVoffice);
            }
            if (!DataUtil.isNullOrEmpty(stockRequestOrderDTO.getLsRequestOrderDetailDTO())) {
                for (StockRequestOrderDetailDTO orderDetailDTO : stockRequestOrderDTO.getLsRequestOrderDetailDTO()) {
                    if (orderDetailDTO.isEditApproveQuantity()) {
                        StockRequestOrderDetailDTO orderDetailTmp = stockRequestOrderDetailService.findOne(orderDetailDTO.getStockRequestOrderDetailId());
                        orderDetailTmp.setLastDatetime(sysdate);
                        orderDetailTmp.setApprovedQuantity(orderDetailDTO.getApprovedQuantity());
                        stockRequestOrderDetailService.save(orderDetailTmp);
                    }
                }
            }
        } else if (Const.STOCK_REQUEST_ORDER.STATUS_2_REJECT.equals(stockRequestOrderDTO.getStatus())
                || Const.STOCK_REQUEST_ORDER.STATUS_3_CANCEL.equals(stockRequestOrderDTO.getStatus())) {
            stockRequestOrderDetailRepo.deleteStockRequestOrderDetai(stockRequestOrderDTO.getStockRequestOrderId(), stockRequestOrderDTO.getStatus());
        }
    }

    public List<StockRequestOrderDTO> getLstApproveAndSignVoffice() throws Exception {
        return repository.getLstApproveAndSignVoffice();
    }

    @Transactional(rollbackFor = Exception.class)
    public void processCreateExpNote(Long stockRequestOrderId, Long fromOwnerId, Long toOwnerId) throws LogicException, Exception {
        stockRequestOrderBaseService.processCreateExpNote(stockRequestOrderId, fromOwnerId, toOwnerId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void processImportStock(Long stockTransId) throws LogicException, Exception {
        stockRequestOrderBaseService.processImportStock(stockTransId);
    }
}
