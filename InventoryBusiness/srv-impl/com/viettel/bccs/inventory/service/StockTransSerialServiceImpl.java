package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.bccs.inventory.model.StockTransSerial;
import com.viettel.bccs.inventory.repo.StockTransSerialRepo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import java.math.BigInteger;
import java.util.*;

@Service
public class StockTransSerialServiceImpl extends BaseServiceImpl implements StockTransSerialService {

    private final BaseMapper<StockTransSerial, StockTransSerialDTO> mapper = new BaseMapper(StockTransSerial.class, StockTransSerialDTO.class);

    @Autowired
    private StockTransSerialRepo repository;

    @Autowired
    private StockTransService transService;

    @Autowired
    private StockTransActionService actionService;

    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private OptionSetValueService optionSetValueService;

    public static final Logger logger = Logger.getLogger(StockTransSerialService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockTransSerialDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<StockTransSerialDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockTransSerialDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    public StockTransSerialDTO save(StockTransSerialDTO stockTransSerialDTO) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(stockTransSerialDTO)));
    }

    @Override
    public List<StockTransSerialDTO> findByStockTransDetailId(Long stockTransDetaiId) throws LogicException, Exception {
        List<FilterRequest> requests = Lists.newArrayList();
        requests.add(new FilterRequest(StockTransSerial.COLUMNS.STOCKTRANSDETAILID.name(), FilterRequest.Operator.EQ, stockTransDetaiId));
        return findByFilter(requests);
    }

    public List<StockTransSerialDTO> findByStockTransId(Long stockTransId) throws LogicException, Exception {
        return mapper.toDtoBean(repository.getSerialByStockTransId(stockTransId));
    }

    @Override
    public List<StockTransSerialDTO> findStockTransSerial(List<Long> stockActionIds) throws LogicException, Exception {
//        if (stockActionIds == null || stockActionIds.length == 0) {
//            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.selecttofindserial");
//        }
        List<StockTransSerialDTO> result = Lists.newArrayList();
        for (Long actionId : stockActionIds) {
            if (DataUtil.isNullObject(actionId)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.validStockTrans");
            }

            StockTransActionDTO stockTransAction = actionService.findOne(actionId);
            if (DataUtil.isNullObject(stockTransAction)
                    || DataUtil.isNullObject(stockTransAction.getStockTransId())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "stock.trans.search.validate.validStockTrans");
            }
            StockTransDTO stockTransDTO = transService.findStockTransByActionId(actionId);
            if (DataUtil.isNullObject(stockTransDTO)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.search.validate.validCode", stockTransAction.getActionCode());
            } else {
                if (DataUtil.isNullOrEmpty(stockTransDTO.getStatus())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.search.validate.validStockTranse");
                }
            }
            if (!Const.STOCK_TRANS_STATUS.NONE_SERIAL.contains(stockTransDTO.getStatus())) {
                List<StockTransSerialDTO> serialDTOs = repository.getListStockTransSerial(actionId);
                if (DataUtil.isNullOrEmpty(serialDTOs)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.search.validate.notSerialResult", stockTransAction.getActionCode());
                }
                result.addAll(serialDTOs);
            } else {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_OBJECT_IS_EMPTY, "stock.trans.search.validate.validAction", stockTransAction.getActionCode());
            }
        }
        return result;
    }

    @Override
    public List<StockTransSerialDTO> getRangeSerial(Long ownerType, Long ownerId, Long productOfferId, String tableName, Long stateId, String channelTypeId, Long serialStatus) throws Exception {
        return repository.getRangeSerial(ownerType, ownerId, tableName, productOfferId, stateId, channelTypeId, serialStatus);
    }

    @Override
    public List<StockTransSerialDTO> getRangeSerialFifo(Long ownerType, Long ownerId, Long productOfferId, String tableName, Long stateId, String channelTypeId, Long serialStatus) throws Exception {
        return repository.getRangeSerialFifo(ownerType, ownerId, tableName, productOfferId, stateId, channelTypeId, serialStatus);
    }

    @Override
    public List<StockTransSerialDTO> findStockTransSerialByDTO(StockTransSerialDTO stockTransSerialDTO) throws LogicException, Exception {
        return repository.findStockTransSerial(stockTransSerialDTO);
    }

    @Override
    public List<StockTransSerialDTO> findStockTransSerialByProductOfferType(WareHouseInfoDTO infoDTO) throws Exception {
        //validate
        if (DataUtil.isNullObject(infoDTO.getOwnerType())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.type.not.null");
        }
        if (Const.OWNER_TYPE.SHOP.equals(infoDTO.getOwnerType()) && DataUtil.isNullObject(infoDTO.getOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.invoice.info.require.shop");
        }
        if (Const.OWNER_TYPE.STAFF.equals(infoDTO.getOwnerType()) && DataUtil.isNullObject(infoDTO.getOwnerId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.invoice.info.require.staff");
        }
        if (DataUtil.isNullObject(infoDTO.getProductOfferingId())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "validate.agent.retrieve.order.product");
        }
        return repository.findStockTransSerialByProductOfferType(infoDTO);
    }

    @Override
    public List<StockTransSerialDTO> getListSerialFromTable(Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String tableName) throws LogicException, Exception {
        return repository.getListSerialFromTable(prodOfferId, ownerId, ownerType, stateId, tableName);
    }

    @Override
    public List<StockInspectCheckDTO> getListGatherSerial(Long productOfferTypeId, Long prodOfferId, Long ownerId, Long ownerType,
                                                          Long stateId, String tableName, String fromSerial, String toSerial) throws LogicException, Exception {
        List<StockInspectCheckDTO> result = Lists.newArrayList();
        List<StockInspectCheckDTO> lstSerial = repository.getListGatherSerial(productOfferTypeId, prodOfferId, ownerId, ownerType, stateId, tableName, fromSerial, toSerial);
        if (!DataUtil.isNullOrEmpty(lstSerial)) {
            if (!DataUtil.isNumber(fromSerial) && !DataUtil.isNumber(toSerial)) {
                return lstSerial;
            } else {
                for (StockInspectCheckDTO stockInspectCheckDTO : lstSerial) {
                    if (DataUtil.safeEqual(stockInspectCheckDTO.getQuantity(), DataUtil.safeToLong(toSerial) - DataUtil.safeToLong(fromSerial) + 1)) {
                        result.add(stockInspectCheckDTO);
                    }
                }
                return result;
            }
        }
        return result;
    }

    @Override
    public List<StockInspectCheckDTO> getListSerialFromList(Long productOfferTypeId, Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String tableName, List<String> serial2D) throws LogicException, Exception {
        return repository.getListSerialFromList(productOfferTypeId, prodOfferId, ownerId, ownerType, stateId, tableName, serial2D);
    }

    @Override
    public List<String> getListSerialValid(Long ownerType, Long ownerId, String tableName, Long productOfferId, Long stateId, String fromSerial, String toSerial, Long rowNum, Long serialStatus) throws Exception {
        return repository.getListSerialValid(ownerType, ownerId, tableName, productOfferId, stateId, fromSerial, toSerial, rowNum, serialStatus);
    }

    @Override
    public List<String> getRangeSerialStockCardValid(Long ownerType, Long ownerId, Long productOfferId, Long stateId,
                                                     String fromSerial, String toSerial, Long quantity, String status) throws Exception {
        if (DataUtil.safeEqual(stateId, Const.STATUS_ACTIVE)) {
            return repository.getRangeSerialStockCardValid(ownerType, ownerId, productOfferId, stateId, fromSerial, toSerial, quantity, status);
        }
        return repository.getRangeSerialStockCardValidWithoutOwner(productOfferId, stateId, fromSerial, toSerial, quantity, status);

    }

    @Override
    public List<StockTransSerialDTO> getListSerialSelect(StockTransFullDTO stockTransFullDTO) throws LogicException, Exception {
        if (stockTransFullDTO == null) {
            return new ArrayList<StockTransSerialDTO>();
        }
        String fromSerial = stockTransFullDTO.getFromSerial();
        String toSerial = stockTransFullDTO.getToSerial();
        String tableName = stockTransFullDTO.getTableName();
        Long quantity = stockTransFullDTO.getQuantityRemain();//so luong serial con lai
        if (DataUtil.isNullOrEmpty(fromSerial) || DataUtil.isNullOrEmpty(toSerial)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.require");
        }
        Long fromOwnerId = stockTransFullDTO.getFromOwnerId();
        if (!DataUtil.isNullOrZero(stockTransFullDTO.getRegionStockTransId())) {
            Long stockTransId = stockTransFullDTO.getRegionStockTransId();
            StockTransDTO stockTransDTO = transService.findOne(stockTransId);
            fromOwnerId = stockTransDTO.getFromOwnerId();
        }
        List<StockTransSerialDTO> lsSerialResult = Lists.newArrayList();
        if (Const.STOCK_TYPE.STOCK_HANDSET.equals(stockTransFullDTO.getProductOfferTypeId())) {
            //validate neu fromSerial toSerial cua mat hang handset khac nhau
            if (!fromSerial.equals(toSerial)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.hanset.valid", stockTransFullDTO.getProdOfferName());
            }
            //get danh sach serial hop le

            List<String> lsSerialValid = getListSerialValid(DataUtil.safeToLong(stockTransFullDTO.getFromOwnerType()), fromOwnerId,
                    stockTransFullDTO.getTableName(), stockTransFullDTO.getProdOfferId(), stockTransFullDTO.getStateId(), fromSerial, toSerial, quantity, stockTransFullDTO.getSerialStatus());
            //bao loi neu ko co bat ky serial nao hop le
            if (DataUtil.isNullOrEmpty(lsSerialValid)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.notExist", fromSerial, toSerial);
            }
            lsSerialResult.add(new StockTransSerialDTO(fromSerial, toSerial, 1L));
        } else {
            if (!DataUtil.validateStringByPattern(fromSerial, Const.REGEX.NUMBER_REGEX) || !DataUtil.validateStringByPattern(toSerial, Const.REGEX.NUMBER_REGEX)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.format", stockTransFullDTO.getProductOfferTypeName());
            }
            BigInteger fromSerialNum = new BigInteger(fromSerial);
            BigInteger toSerialNum = new BigInteger(toSerial);
            BigInteger result = toSerialNum.subtract(fromSerialNum);
            //neu serial ket thuc nho hon serial bat dau or serial ket thuc lon hon 500000 thi bao loi
            if (result.compareTo(new BigInteger("0")) < 0 || result.compareTo(new BigInteger("500000")) > 0) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.range");
            }
            //neu la stock_card
            List<String> lsSerialNumber = Lists.newArrayList();
            // tuydv1 neu la state_id =5 --> gan status =0 con lai la 1
            String status = Const.STATUS.ACTIVE;
            if (Const.GOODS_STATE.RESCUE.equals(stockTransFullDTO.getStateId())) {
                status = Const.STATUS.NO_ACTIVE;
            }

            Long configStockCard = 0L;
            List<OptionSetValueDTO> lstConfigStockCard = optionSetValueService.getByOptionSetCode("STOCK_CARD_STRIP");
            if (!DataUtil.isNullOrEmpty(lstConfigStockCard) && !DataUtil.isNullObject(lstConfigStockCard.get(0).getValue())) {
                configStockCard = DataUtil.safeToLong(lstConfigStockCard.get(0).getValue());
            }
            if (Const.STOCK_TYPE.STOCK_CARD_NAME.equalsIgnoreCase(tableName) && Const.STOCK_CARD_STRIP.equals(configStockCard)) {
                lsSerialNumber = repository.getRangeSerialStockCardValid(DataUtil.safeToLong(stockTransFullDTO.getFromOwnerType()), fromOwnerId,
                        stockTransFullDTO.getProdOfferId(), stockTransFullDTO.getStateId(), fromSerial, toSerial, quantity, status);
            } else {
                //get danh sach serial hop le
                List<String> lsSerialValid = getListSerialValid(DataUtil.safeToLong(stockTransFullDTO.getFromOwnerType()), fromOwnerId,
                        stockTransFullDTO.getTableName(), stockTransFullDTO.getProdOfferId(), stockTransFullDTO.getStateId(), fromSerial, toSerial, quantity, stockTransFullDTO.getSerialStatus());
                //bao loi neu ko co bat ky serial nao hop le
                if (DataUtil.isNullOrEmpty(lsSerialValid)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "mn.stock.partner.serial.valid.notExist", fromSerial, toSerial);
                }

                lsSerialNumber.addAll(lsSerialValid);
            }
            //xu ly ghep dai serial
            lsSerialResult = getListSerialGroup(lsSerialNumber);
        }

        return lsSerialResult;
    }

    @Override
    public List<StockTransSerialDTO> getListSerialGroup(List<String> listSerials) throws Exception {
        List<StockTransSerialDTO> listStockTransSerials = new ArrayList<>();
        if (listSerials == null || listSerials.size() == 0) {
            return listStockTransSerials;
        }

        StockTransSerialDTO stockTransSerial = new StockTransSerialDTO();
        int listSize = listSerials.size();
        BigInteger fromSerial = DataUtil.safeToBigInteger(DataUtil.safeToString(listSerials.get(0)).trim());
        BigInteger toSerial = DataUtil.safeToBigInteger(DataUtil.safeToString(listSerials.get(listSize - 1)).trim());
        BigInteger num = toSerial.subtract(fromSerial);
        int serialLength0 = listSerials.get(0).length();
        if (num.intValue() == (listSize - 1)) {
            stockTransSerial.setFromSerial(DataUtil.formatSerial(serialLength0, fromSerial));
            stockTransSerial.setToSerial(DataUtil.formatSerial(serialLength0, toSerial));
            stockTransSerial.setQuantity(DataUtil.safeToLong(listSize));
            listStockTransSerials.add(stockTransSerial);
            return listStockTransSerials;
        }


        BigInteger currentSerial = DataUtil.safeToBigInteger(listSerials.get(0));
        BigInteger beforeSerial = currentSerial;
        int currentLength = listSerials.get(0).length();
        int beforeLength = currentLength;
        stockTransSerial.setFromSerial(DataUtil.formatSerial(serialLength0, currentSerial));

        for (int i = 1; i < listSerials.size(); i++) {
            currentLength = listSerials.get(i).length();
            currentSerial = DataUtil.safeToBigInteger(listSerials.get(i));
            if (!currentSerial.equals(beforeSerial.add(BigInteger.ONE)) || (beforeLength != currentLength)) {
                //neu khong lien ke nhau, luu tru doi tuong hien co
                stockTransSerial.setToSerial(DataUtil.formatSerial(beforeLength, beforeSerial));
                BigInteger from = new BigInteger(stockTransSerial.getFromSerial());
                BigInteger to = new BigInteger(stockTransSerial.getToSerial());

                long quantity = to.subtract(from).longValue() + 1;
                stockTransSerial.setQuantity(quantity);
                listStockTransSerials.add(stockTransSerial);

                //tao ra doi tuong stockTransSerial moi de luu tru diem dau + diem cuoi
                stockTransSerial = new StockTransSerialDTO();
                stockTransSerial.setFromSerial(DataUtil.formatSerial(currentLength, currentSerial));
            }
            beforeSerial = currentSerial;
            beforeLength = currentLength;
        }

        stockTransSerial.setToSerial(DataUtil.formatSerial(beforeLength, beforeSerial));
        BigInteger from = new BigInteger(stockTransSerial.getFromSerial());
        BigInteger to = new BigInteger(stockTransSerial.getToSerial());
        long quantity = to.subtract(from).longValue() + 1;
        stockTransSerial.setQuantity(quantity);
        listStockTransSerials.add(stockTransSerial);
        return listStockTransSerials;
    }

    @Override
    public int updateStatusStockSerial(Long ownerId, Long ownerType, Long prodOfferId, String fromSerial, String toSerial, Long newStatus, Long oldStatus) throws Exception {
        return repository.updateStatusStockSerial(ownerId, ownerType, prodOfferId, fromSerial, toSerial, newStatus, oldStatus);
    }

    @Override
    public Long getDepostiPriceFromStockX(Long ownerId, Long ownerType, Long prodOfferId, String serial) throws Exception {
        return repository.getDepostiPriceFromStockX(ownerId, ownerType, prodOfferId, serial);
    }

    @Override
    public Long getDepostiPriceForRetrieve(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String serial) throws Exception {
        return repository.getDepostiPriceForRetrieve(ownerId, ownerType, prodOfferId, stateId, serial);
    }

    @Override
    public Long getDepostiPriceByRangeSerial(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String fromSerial, String toSerial) throws Exception {
        return repository.getDepostiPriceByRangeSerial(ownerId, ownerType, prodOfferId, stateId, fromSerial, toSerial);
    }

    @Override
    public List<StockTransSerialDTO> getListSerialQuantity(Long prodOfferId, Long ownerId, Long ownerType, Long stateId, String status, String tableName, String fromSerial, String toSerial) throws LogicException, Exception {
        return repository.getListSerialQuantity(prodOfferId, ownerId, ownerType, stateId, status, tableName, fromSerial, toSerial);
    }

    @Override
    public List<LookupSerialByFileDTO> searchSerialExact(Long productOfferTypeId, List<String> serial, String serialGpon, boolean isSearchNew) throws Exception {
        return repository.searchSerialExact(productOfferTypeId, serial, serialGpon, isSearchNew);
    }

    @Override
    public List<ViewSerialHistoryDTO> listLookUpSerialHistory(String serial, Long prodOfferId, Date fromDate, Date toDate, boolean isOffline) throws Exception {
        return repository.listLookUpSerialHistory(serial, prodOfferId, fromDate, toDate, isOffline);
    }

    @Override
    public List<ChangeDamagedProductDTO> getListSerialProduct(String serial, Long ownerType, Long ownerId) throws LogicException, Exception {
        return repository.getListSerialProduct(serial, ownerType, ownerId);
    }

    @Override
    public List<StockTransSerialDTO> getListBySerial(Long prodOfferId, String serial, Long ownerType, Long ownerId, Long status, String tableName) throws LogicException, Exception {
        return repository.getListBySerial(prodOfferId, serial, ownerType, ownerId, status, tableName);
    }

    @Override
    public List<StockTransSerialDTO> getListStockCardSaledBySerial(Long prodOfferId, String serial) throws LogicException, Exception {
        return repository.getListStockCardSaledBySerial(prodOfferId, serial);
    }

    @Override
    public List<StockTransSerialDTO> getStockTransSerialByStockTransId(Long stockTransId, Long prodOfferId, Long toOwnerType) throws Exception {
        return mapper.toDtoBean(repository.getStockTransSerialByStockTransId(stockTransId, prodOfferId, toOwnerType));
    }

    @Override
    public int updateStockBalance(Long ownerId, Long ownerType, Long prodOfferId, Long stateId, String strFromSerial, String strToSerial, Long stockTransId, Long newStatus, Long oldStatus) throws Exception {
        return repository.updateStockBalance(ownerId, ownerType, prodOfferId, stateId, strFromSerial, strToSerial, stockTransId, newStatus, oldStatus);
    }

    @Override
    public List<StockDeviceDTO> getLstStockDevice(Long stockTransActionId) throws LogicException, Exception {
        return repository.getLstStockDevice(stockTransActionId);
    }

    @Override
    public List<StockDeviceTransferDTO> getLstDeviceTransfer(Long prodOfferId, Long stateId, Long stockRequestId) throws LogicException, Exception {
        return repository.getLstDeviceTransfer(prodOfferId, stateId, stockRequestId);
    }
}
