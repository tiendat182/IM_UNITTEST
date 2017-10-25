package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.DivideSerialActionDTO;
import com.viettel.bccs.inventory.dto.ShopDTO;
import com.viettel.bccs.inventory.dto.StockTransFullDTO;
import com.viettel.bccs.inventory.dto.StockTransSerialDTO;
import com.viettel.bccs.inventory.model.DivideSerialAction;
import com.viettel.bccs.inventory.repo.DivideSerialActionRepo;
import com.viettel.bccs.inventory.service.common.IMServiceUtil;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.ErrorCode;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;

@Service
public class DivideSerialActionServiceImpl extends BaseServiceImpl implements DivideSerialActionService {
    public static final Logger logger = Logger.getLogger(DivideSerialActionServiceImpl.class);
    @Autowired
    StockTransService stockTransService;
    @Autowired
    ShopService shopService;
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    List<Long> lstShopId;
    private final BaseMapper<DivideSerialAction, DivideSerialActionDTO> mapper = new BaseMapper<>(DivideSerialAction.class, DivideSerialActionDTO.class);

    @Autowired
    private DivideSerialActionRepo repository;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public DivideSerialActionDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<DivideSerialActionDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<DivideSerialActionDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public DivideSerialActionDTO save(DivideSerialActionDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(DivideSerialActionDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public List<StockTransFullDTO> divideSerial(DivideSerialActionDTO divideSerialActionDTO) throws LogicException, Exception {
        List<Long> lstStockTransActionId = divideSerialActionDTO.getLstStockTransActionId();
        List<StockTransSerialDTO> lstStockTransSerial = divideSerialActionDTO.getLstStockTransSerial();
        List<String> lstStockImpInFile = divideSerialActionDTO.getLstStockImpInFile();
        if (DataUtil.isNullOrEmpty(lstStockTransSerial)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "Thong tin serial khong hop le");
        }
        lstShopId = new ArrayList();
        Collections.sort(lstStockTransSerial, new Comparator<StockTransSerialDTO>() {
                    @Override
                    public int compare(StockTransSerialDTO o1, StockTransSerialDTO o2) {
                        return o1.getFromSerial().compareTo(o2.getFromSerial());
                    }
                }
        );
        doValidate(lstStockTransActionId, lstStockTransSerial, lstStockImpInFile);

        List<StockTransFullDTO> lstStockTransFullDTO = stockTransService.searchStockTransDetail(lstStockTransActionId);
        //Tim cac giao dich theo thu tu kho can chia serial
        Date sysdate = DbUtil.getSysDate(em);
        for (int i = 0; i < lstShopId.size(); i++) {
            for (StockTransFullDTO stockTransFullDTO : lstStockTransFullDTO) {
                List<StockTransSerialDTO> lstSerialDivide = new ArrayList();
                Long quantityInTrans = stockTransFullDTO.getQuantity();
                if (DataUtil.safeEqual(stockTransFullDTO.getToOwnerId(), lstShopId.get(i))) {
                    boolean nextSerial = true;
//                    for (int j = 0; j < lstStockTransSerial.size(); j++) {
                    while (nextSerial) {
                        StockTransSerialDTO stockTransSerialDTO = lstStockTransSerial.get(0);
                        Long quantityInFile ;
                        if (DataUtil.safeEqual(Const.PRODUCT_OFFER_TYPE.PHONE, stockTransFullDTO.getProductOfferTypeId())) {
                            quantityInFile = 1L;
                        } else {
                            BigInteger fromSerial = new BigInteger(stockTransSerialDTO.getFromSerial());
                            BigInteger toSerial = new BigInteger(stockTransSerialDTO.getToSerial());
                            quantityInFile = Long.valueOf(toSerial.subtract(fromSerial).add(BigInteger.ONE).toString());
                        }

                        if (DataUtil.safeEqual(quantityInTrans, quantityInFile)) {
                            //add ban ghi vao list duoc chon
                            //remove serial khoi list
                            lstSerialDivide.add(stockTransSerialDTO);
                            lstStockTransSerial.remove(0);
                            nextSerial = false;
                        } else if (quantityInTrans > quantityInFile) {
                            //dai serial chua du so luong cho lenh xuat, insert vao
                            lstSerialDivide.add(stockTransSerialDTO);
                            quantityInTrans += -quantityInFile;
                            lstStockTransSerial.remove(0);
                            if (lstStockTransSerial.isEmpty()) {
                                nextSerial = true;
                            }
                        } else if (quantityInTrans < quantityInFile) {
                            //cat dai serial de bang voi dai trong lenh xuat
                            BigInteger tmpToSerial = new BigInteger(stockTransSerialDTO.getFromSerial()).add(new BigInteger(quantityInTrans.toString())).subtract(BigInteger.ONE);
                            BigInteger tmpFromSerialNew = tmpToSerial.add(BigInteger.ONE);
                            StockTransSerialDTO newSockTransSerialDTO = DataUtil.cloneBean(stockTransSerialDTO);
                            //format lai truong toSerial de add vao list
                            stockTransSerialDTO.setToSerial(formatSerial(tmpToSerial, stockTransSerialDTO.getFromSerial().length()));
                            lstSerialDivide.add(stockTransSerialDTO);
                            //tao serial con lai, add vao list
                            newSockTransSerialDTO.setFromSerial(formatSerial(tmpFromSerialNew, stockTransSerialDTO.getFromSerial().length()));
                            lstStockTransSerial.set(0, newSockTransSerialDTO);
                            nextSerial = false;
                        }
                    }
                    if (!DataUtil.isNullOrEmpty(lstSerialDivide)) {
                        //remove serial cu truoc khi save
                        removeOldSerial(stockTransFullDTO.getActionId());
                        //goi ham save ban ghi
                        for (StockTransSerialDTO stockTransSerialDTO : lstSerialDivide) {
                            //tim kiem cac dai serial khong hop le
                            String serialContent = "";
                            List<String> lstSerialErr = getSerialInvalid(stockTransFullDTO.getFromOwnerId(), stockTransFullDTO.getFromOwnerType(), stockTransFullDTO.getProductOfferTypeId(),
                                    stockTransFullDTO.getProdOfferId(), stockTransSerialDTO.getFromSerial(), stockTransSerialDTO.getToSerial(), stockTransFullDTO.getStateId());
                            if (!DataUtil.isNullOrEmpty(lstSerialErr)) {
                                serialContent = String.join(",", lstSerialErr);
                            }
                            //save
                            DivideSerialActionDTO divideSerialAction = new DivideSerialActionDTO();
                            divideSerialAction.setStockTransActionId(stockTransFullDTO.getActionId());
                            divideSerialAction.setSerialContent(serialContent);
                            divideSerialAction.setCreateUser(divideSerialActionDTO.getCreateUser());
                            divideSerialAction.setCreateDatetime(sysdate);
                            divideSerialAction.setFromSerial(stockTransSerialDTO.getFromSerial());
                            divideSerialAction.setToSerial(stockTransSerialDTO.getToSerial());
                            save(divideSerialAction);
                        }
                        //xoa ban ghi khoi list
                        lstStockTransFullDTO.remove(stockTransFullDTO);
                        break;
                    }
                }
            }
        }
        //return lai danh sach ban ghi bi loi
        return getListSerial(divideSerialActionDTO);
    }

    private void doValidate(List<Long> lstStockTransActionId, List<StockTransSerialDTO> lstStockTransSerial, List<String> lstStockImpInFile) throws LogicException, Exception {
        if (DataUtil.isNullOrEmpty(lstStockTransActionId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.divide.stock.trans.empty");
        }
        if (DataUtil.isNullOrEmpty(lstStockTransSerial)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.import.stock.error.no.serial");
        }
        if (DataUtil.isNullOrEmpty(lstStockImpInFile)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.divide.input.stock.infile.empty");
        }
        //validate cac giao dich hop le
        //lay danh sach cac giao dich
        List<StockTransFullDTO> lstStockTransFullDTO = stockTransService.searchStockTransDetail(lstStockTransActionId);
        if (DataUtil.isNullOrEmpty(lstStockTransFullDTO)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.divide.stock.trans.invalid");
        }
        Long prodOfferTypeId = lstStockTransFullDTO.get(0).getProductOfferTypeId();
        //Mat hang no-serial khong duoc phep chia
        if (DataUtil.safeEqual(Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL, prodOfferTypeId)) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "divide.serial.noserial");
        }
        //cac giao dich chi duoc co 1 mat hang
        Set<Long> s1 = new HashSet();
        Set<Long> s2 = new HashSet();
        Long totalQuantity = 0L;
        List<Long> lstImportStock = new ArrayList();
        for (StockTransFullDTO stockTransFullDTO : lstStockTransFullDTO) {
            s1.add(stockTransFullDTO.getProdOfferId());
            s2.add(stockTransFullDTO.getStateId());
            totalQuantity += stockTransFullDTO.getQuantity();
            lstImportStock.add(stockTransFullDTO.getToOwnerId());
        }
        if (s1.size() > 1 || s2.size() > 1) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.divide.stock.trans.product.duplicate");
        }

        //Validate thong tin serial
        Long totalSerialInFile = 0L;
        for (StockTransSerialDTO stockTransSerialDTO : lstStockTransSerial) {
            String fromSerial = stockTransSerialDTO.getFromSerial();
            String toSerial = stockTransSerialDTO.getToSerial();
            if (DataUtil.isNullObject(fromSerial) || DataUtil.isNullObject(toSerial)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.divide.serial.infile.empty");
            }
            if (!DataUtil.safeEqual(fromSerial.length(), toSerial.length())) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.divide.serial.infile.length");
            }
            if (DataUtil.safeEqual(Const.PRODUCT_OFFER_TYPE.PHONE, prodOfferTypeId)) {
                if (!DataUtil.safeEqual(fromSerial, toSerial)) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.divide.serial.infile.equal");
                }
                totalSerialInFile += 1L;
            } else {
                BigInteger fromSerialNumber;
                BigInteger toSerialNumber;
                try {
                    fromSerialNumber = new BigInteger(fromSerial);
                    toSerialNumber = new BigInteger(toSerial);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.fromSerial.negative");
                }
                if (fromSerialNumber.compareTo(toSerialNumber) > 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.validate.inspect.fromSerial.less.toSerial");
                }
                totalSerialInFile += Long.valueOf(toSerialNumber.subtract(fromSerialNumber).add(BigInteger.ONE).toString());
            }
        }

        if (totalQuantity > totalSerialInFile) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.partner.range.serial.invalid.number");
        }
        //Danh sach kho trong file nhap vao khong duoc trung
        Set sStockImp = new HashSet();
        sStockImp.addAll(lstStockImpInFile);
        if (sStockImp.size() < lstStockImpInFile.size()) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.divide.input.stock.duplicate");
        }
        //Danh sach kho trong file phai ton tai
        List<Long> lstShopIdInFile = new ArrayList();
        HashMap<Long, String> mapShopName = new HashMap();
        for (int i = 0; i < lstStockImpInFile.size(); i++) {
            ShopDTO shopDTO = shopService.getShopByCodeAndActiveStatus(lstStockImpInFile.get(i));
            if (shopDTO != null) {
                lstShopIdInFile.add(shopDTO.getShopId());
                mapShopName.put(shopDTO.getShopId(), shopDTO.getShopCode());
            } else {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.divide.shop.code.invalid", lstStockImpInFile.get(i));
            }
        }
        lstShopId = lstShopIdInFile;
        //Danh sach kho nhap vao phai bang danh sach kho trong lenh xuat
        List<Long> tmpList = new ArrayList();
        tmpList.addAll(lstShopIdInFile);
        tmpList.removeAll(lstImportStock);
        if (tmpList.size() != 0) {
            List<String> lstShopInvalid = new ArrayList();
            for (int i = 0; i < tmpList.size(); i++) {
                lstShopInvalid.add(mapShopName.get(tmpList.get(i)));
            }
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.divide.not.in.divide.serial.list", lstShopInvalid);
        }
        tmpList = new ArrayList(lstImportStock);
        tmpList.removeAll(lstShopIdInFile);
        if (tmpList.size() != 0) {
            List<String> lstShopInvalid = new ArrayList();
            for (int i = 0; i < tmpList.size(); i++) {
                ShopDTO shopDTO = shopService.findOne(tmpList.get(i));
                lstShopInvalid.add(shopDTO.getShopCode());
            }
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.divide.not.order", lstShopInvalid);
        }

        //Check trung danh sach serial
        for (int i = 0; i < lstStockTransSerial.size(); i++) {
            if (i == 0) {
                continue;
            }
            if (DataUtil.safeEqual(Const.PRODUCT_OFFER_TYPE.PHONE, prodOfferTypeId)) {
                if (DataUtil.safeEqual(lstStockTransSerial.get(i).getFromSerial(), lstStockTransSerial.get(i - 1).getFromSerial())) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.divide.serial.duplicate", lstStockTransSerial.get(i).getFromSerial(), lstStockTransSerial.get(i - 1).getFromSerial());
                }
            } else {
                BigInteger currFromSerial = new BigInteger(lstStockTransSerial.get(i).getFromSerial());
                BigInteger prevFromSerial = new BigInteger(lstStockTransSerial.get(i - 1).getFromSerial());
                BigInteger prevToSerial = new BigInteger(lstStockTransSerial.get(i - 1).getToSerial());
                if (currFromSerial.compareTo(prevFromSerial) >= 0 && currFromSerial.compareTo(prevToSerial) <= 0) {
                    throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.divide.serial.duplicate", lstStockTransSerial.get(i).getFromSerial(), lstStockTransSerial.get(i - 1).getFromSerial());
                }
            }
        }
    }

    private String formatSerial(BigInteger serial, int length) {
        int prefix = length - serial.toString().length();
        String serialFormat = prefix == 0 ? "%d" : ("%0" + String.valueOf(length) + "d");
        return String.format(serialFormat, serial);
    }

    private List<String> getSerialInvalid(Long ownerId, String ownerType, Long prodOfferTypeId, Long prodOfferId, String fromSerial, String toSerial, Long stateId) {
        StringBuilder strQuery = new StringBuilder();
        String tableName = IMServiceUtil.getTableNameByOfferType(prodOfferTypeId);
        if (!DataUtil.safeEqual(Const.PRODUCT_OFFER_TYPE.PHONE, prodOfferTypeId)) {
            strQuery.append("SELECT   TO_CHAR(serial) serial ");
            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)) {
                strQuery.append("  FROM   (    SELECT   TO_CHAR(TO_NUMBER (#fromSerial) + LEVEL - 1) AS serial ");
            } else {
                strQuery.append("  FROM   (    SELECT   TO_NUMBER (#fromSerial) + LEVEL - 1 AS serial ");
            }
            strQuery.append("                FROM   DUAL ");
            strQuery.append("          CONNECT BY   LEVEL <= (TO_NUMBER (#toSerial) - TO_NUMBER (#fromSerial) + 1)) a ");
            strQuery.append(" WHERE   NOT EXISTS ");
            strQuery.append("             (SELECT   1 ");
            strQuery.append("                FROM   ").append(tableName);
            strQuery.append("               WHERE       owner_id = #ownerId ");
            strQuery.append("                       AND owner_type = #ownerType ");
            if (DataUtil.safeEqual(Const.STOCK_TYPE.STOCK_HANDSET_NAME, tableName)) {
                strQuery.append("                       AND serial = a.serial ");
            } else {
                strQuery.append("                       AND TO_NUMBER (serial) = a.serial ");
            }
            strQuery.append("                       AND prod_offer_id = #prodOfferId ");
            strQuery.append("                       AND status = #status ");
            strQuery.append("                       AND state_id = #stateId) ");
            Query query = em.createNativeQuery(strQuery.toString());
            query.setParameter("fromSerial", fromSerial);
            query.setParameter("toSerial", toSerial);
            query.setParameter("ownerId", ownerId);
            query.setParameter("ownerType", ownerType);
            query.setParameter("prodOfferId", prodOfferId);
            query.setParameter("status", Const.STATUS_ACTIVE);
            query.setParameter("stateId", stateId);
            return query.getResultList();
        } else {
            strQuery.append(" SELECT   TO_CHAR(serial) serial ");
            strQuery.append("   FROM   stock_handset ");
            strQuery.append("  WHERE       owner_id = #ownerId ");
            strQuery.append("          AND owner_type = #ownerType ");
            strQuery.append("          AND serial = #fromSerial ");
            strQuery.append("          AND prod_offer_id = #prodOfferId ");
            strQuery.append("          AND status = #status ");
            strQuery.append("          AND state_id = #stateId ");
            Query query = em.createNativeQuery(strQuery.toString());
            query.setParameter("ownerId", ownerId);
            query.setParameter("ownerType", ownerType);
            query.setParameter("fromSerial", fromSerial);
            query.setParameter("prodOfferId", prodOfferId);
            query.setParameter("status", Const.STATUS_ACTIVE);
            query.setParameter("stateId", stateId);
            List<String> lst = query.getResultList();
            if (DataUtil.isNullOrEmpty(lst)) {
                lst.add(fromSerial);
            } else {
                lst = new ArrayList();
            }
            return lst;
        }
    }

    private int removeOldSerial(Long stockTransActionId) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" delete divide_serial_action ");
        strQuery.append(" where stock_trans_action_id = #stockTransActionId ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("stockTransActionId", stockTransActionId);
        return query.executeUpdate();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public List<StockTransFullDTO> searchStockTrans(DivideSerialActionDTO divideSerialActionDTO) throws LogicException, Exception {
        //validate gia tri nhap vao
        if (DataUtil.isNullOrEmpty(divideSerialActionDTO.getActionCode())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "export.order.code.require.msg");
        }
        if (divideSerialActionDTO.getFromDate() == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.from.date.not.blank");
        }
        if (divideSerialActionDTO.getToDate() == null) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "mn.stock.to.date.not.blank");
        }
        if (divideSerialActionDTO.getFromDate().after(divideSerialActionDTO.getToDate())) {
            throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "stock.trans.from.than.to");
        }

        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT   a.stock_trans_action_id, a.to_owner_id, b.owner_code, b.owner_name, a.prod_offer_id, ");
        strQuery.append("          a.prod_offer_name, a.quantity, a.create_datetime, a.stock_trans_status, ");
        strQuery.append("          (CASE ");
        strQuery.append("               WHEN EXISTS ");
        strQuery.append("                        (SELECT   'X' ");
        strQuery.append("                           FROM   divide_serial_action ");
        strQuery.append("                          WHERE   stock_trans_action_id = a.stock_trans_action_id) ");
        strQuery.append("               THEN 1 ");
        strQuery.append("               ELSE 0 ");
        strQuery.append("           END) serialStatus, a.action_code ");
        strQuery.append("   FROM   v_stock_trans_detail a, v_shop_staff b ");
        strQuery.append("  WHERE       a.to_owner_id = b.owner_id ");
        strQuery.append("          AND a.to_owner_type = b.owner_type ");
        strQuery.append("          AND a.stock_trans_status IN (1, 2) ");
        strQuery.append("          AND a.action_type = 1 ");
        strQuery.append("          AND a.action_code = #actionCode ");
        strQuery.append("          AND a.create_datetime >= #fromDate ");
        strQuery.append("          AND a.create_datetime <= #toDate + 1 ");
        if (!DataUtil.isNullObject(divideSerialActionDTO.getStockTransStatus())) {
            strQuery.append("          AND a.stock_trans_status = #stockTransStatus ");
        }
        if (DataUtil.safeEqual(divideSerialActionDTO.getSerialStatus(), Const.SERIAL_STATUS)) {
            strQuery.append("          AND EXISTS (SELECT 'X' from divide_serial_action where stock_trans_action_id = a.stock_trans_action_id) ");
        } else if (DataUtil.safeEqual(divideSerialActionDTO.getSerialStatus(), Const.SERIAL_STATUS_NOT_DIVIDE)) {
            strQuery.append("          AND NOT EXISTS (SELECT 'X' from divide_serial_action where stock_trans_action_id = a.stock_trans_action_id) ");
        }
        strQuery.append("          AND EXISTS ");
        strQuery.append("                 (  SELECT   'X' ");
        strQuery.append("                      FROM   stock_trans_detail ");
        strQuery.append("                     WHERE   create_datetime >= #fromDate ");
        strQuery.append("                             AND create_datetime <= #toDate + 1 ");
        strQuery.append("                             AND stock_trans_id = a.stock_trans_id ");
        strQuery.append("                  GROUP BY   stock_trans_id, state_id ");
        strQuery.append("                    HAVING   COUNT ( * ) = 1) ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("actionCode", divideSerialActionDTO.getActionCode().trim());
        query.setParameter("fromDate", divideSerialActionDTO.getFromDate());
        query.setParameter("toDate", divideSerialActionDTO.getToDate());
        if (!DataUtil.isNullObject(divideSerialActionDTO.getStockTransStatus())) {
            query.setParameter("stockTransStatus", divideSerialActionDTO.getStockTransStatus());
        }
        List<Object[]> lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            List<StockTransFullDTO> lstStockTransFullDTO = new ArrayList();
            for (Object[] ob : lst) {
                StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
                stockTransFullDTO.setActionId(DataUtil.safeToLong(ob[0]));
                stockTransFullDTO.setToOwnerId(DataUtil.safeToLong(ob[1]));
                stockTransFullDTO.setToOwnerCode(DataUtil.safeToString(ob[2]));
                stockTransFullDTO.setToOwnerName(DataUtil.safeToString(ob[3]));
                stockTransFullDTO.setProdOfferId(DataUtil.safeToLong(ob[4]));
                stockTransFullDTO.setProdOfferName(DataUtil.safeToString(ob[5]));
                stockTransFullDTO.setQuantity(DataUtil.safeToLong(ob[6]));
                stockTransFullDTO.setCreateDatetime((Date) (ob[7]));
                stockTransFullDTO.setStockTransStatus(DataUtil.safeToString(ob[8]));
                stockTransFullDTO.setSerialStatus(DataUtil.safeToLong(ob[9]));
                stockTransFullDTO.setActionCode(DataUtil.safeToString(ob[10]));
                lstStockTransFullDTO.add(stockTransFullDTO);
            }
            return lstStockTransFullDTO;
        }
        return null;
    }

    @WebMethod
    public List<StockTransFullDTO> getListSerial(DivideSerialActionDTO divideSerialActionDTO) throws LogicException, Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" SELECT   a.stock_trans_action_id, a.to_owner_id, b.owner_code, b.owner_name, a.prod_offer_id, ");
        strQuery.append("          a.prod_offer_name, (case when c.to_serial = c.from_serial then 1 else (c.to_serial - c.from_serial + 1) end) quantity, a.create_datetime, a.stock_trans_status, ");
        strQuery.append("          (case when length(c.serial_content) > 1000 then to_char(substr(c.serial_content,0,1000) || '...') else to_char(c.serial_content) end) serialContent ");
        strQuery.append("          , a.action_code, a.prod_offer_code, c.from_serial, c.to_serial ");
        strQuery.append("   FROM   v_stock_trans_detail a, v_shop_staff b, divide_serial_action c ");
        strQuery.append("  WHERE       a.to_owner_id = b.owner_id ");
        strQuery.append("          AND a.to_owner_type = b.owner_type ");
        strQuery.append("          AND a.stock_trans_action_id = c.stock_trans_action_id ");
        strQuery.append("          AND a.stock_trans_status IN (1, 2) ");
        strQuery.append("          AND a.action_type = 1 ");
        strQuery.append("          AND a.create_datetime >= #fromDate ");
        strQuery.append("          AND a.create_datetime <= #toDate + 1 ");
        strQuery.append("          AND a.stock_trans_action_id " + DbUtil.createInQuery("stock_trans_action_id", divideSerialActionDTO.getLstStockTransActionId()));
        strQuery.append("  ORDER BY c.stock_trans_action_id, c.from_serial ");
        Query query = em.createNativeQuery(strQuery.toString());
        query.setParameter("fromDate", divideSerialActionDTO.getFromDate());
        query.setParameter("toDate", divideSerialActionDTO.getToDate());
        DbUtil.setParamInQuery(query, "stock_trans_action_id", divideSerialActionDTO.getLstStockTransActionId());

        List<Object[]> lst = query.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            List<StockTransFullDTO> lstStockTransFullDTO = new ArrayList();
            for (Object[] ob : lst) {
                StockTransFullDTO stockTransFullDTO = new StockTransFullDTO();
                stockTransFullDTO.setActionId(DataUtil.safeToLong(ob[0]));
                stockTransFullDTO.setToOwnerId(DataUtil.safeToLong(ob[1]));
                stockTransFullDTO.setToOwnerCode(DataUtil.safeToString(ob[2]));
                stockTransFullDTO.setToOwnerName(DataUtil.safeToString(ob[3]));
                stockTransFullDTO.setProdOfferId(DataUtil.safeToLong(ob[4]));
                stockTransFullDTO.setProdOfferName(DataUtil.safeToString(ob[5]));
                stockTransFullDTO.setQuantity(DataUtil.safeToLong(ob[6]));
                stockTransFullDTO.setCreateDatetime((Date) (ob[7]));
                stockTransFullDTO.setStockTransStatus(DataUtil.safeToString(ob[8]));
                stockTransFullDTO.setSerialContent(DataUtil.safeToString(ob[9]));
                stockTransFullDTO.setActionCode(DataUtil.safeToString(ob[10]));
                stockTransFullDTO.setProdOfferCode(DataUtil.safeToString(ob[11]));
                stockTransFullDTO.setFromSerial(DataUtil.safeToString(ob[12]));
                stockTransFullDTO.setToSerial(DataUtil.safeToString(ob[13]));
                lstStockTransFullDTO.add(stockTransFullDTO);
            }
            return lstStockTransFullDTO;
        }
        return null;
    }
}
