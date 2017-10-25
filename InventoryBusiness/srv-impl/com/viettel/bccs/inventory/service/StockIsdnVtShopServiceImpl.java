package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.model.StockIsdnVtShop;
import com.viettel.bccs.inventory.model.StockIsdnVtShopFee;
import com.viettel.bccs.inventory.model.StockIsdnVtShopFeeId;
import com.viettel.bccs.inventory.repo.StockIsdnVtShopFeeRepo;
import com.viettel.bccs.inventory.repo.StockIsdnVtShopRepo;
import com.viettel.bccs.inventory.repo.StockNumberRepo;
import com.viettel.bccs.partner.model.AccountBalance;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockIsdnVtShopServiceImpl extends BaseServiceImpl implements StockIsdnVtShopService {
    public static final Logger logger = Logger.getLogger(StockIsdnVtShopService.class);
    private final BaseMapper<StockIsdnVtShop, StockIsdnVtShopDTO> mapper = new BaseMapper<>(StockIsdnVtShop.class, StockIsdnVtShopDTO.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private StockIsdnVtShopRepo repository;
    @Autowired
    private StockIsdnVtShopFeeRepo stockIsdnVtShopFeeRepo;
    @Autowired
    private StockNumberRepo stockNumberRepo;
    @Autowired
    private StockIsdnVtShopLockService stockIsdnVtShopLockService;
    @Autowired
    private StockIsdnVtShopFeeService stockIsdnVtShopFeeService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private OptionSetValueService optionSetValueService;

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public StockIsdnVtShopDTO findOne(String isdn) throws Exception {
        return mapper.toDtoBean(repository.findOne(isdn));
    }

    @WebMethod
    public List<StockIsdnVtShopDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<StockIsdnVtShopDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public StockIsdnVtShopDTO save(StockIsdnVtShopDTO dto) throws Exception {
        return mapper.toDtoBean(repository.save(mapper.toPersistenceBean(dto)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderIsdn(String isdnOrder, String otp, String idNo, String viettelshopId, String customerName, String contactIsdn, String address,
                              Date expiredDate, String payStatus, List<VtShopFeeDTO> vtShopFeeeDTOs, List<String> lstShopVtPost) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(isdnOrder)) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.require");
        }

        if (!DataUtil.isNumber(isdnOrder)) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.notNumber", isdnOrder);
        }

        if (isdnOrder.trim().length() > 12) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.overlength", isdnOrder);
        }

        isdnOrder = isdnOrder.trim();

        //Chuan hoa so thue bao
        if (isdnOrder.startsWith("0")) {
            isdnOrder = isdnOrder.substring(1);
        }

        if (isdnOrder.startsWith("84")) {
            isdnOrder = isdnOrder.substring(2);
        }

        if (DataUtil.isNullOrEmpty(otp)) {
            throw new LogicException("ERR_VALIDATE_OTP", "vtshop.validate.otp.require");
        }

        if (otp.trim().getBytes("UTF-8").length > 50) {
            throw new LogicException("ERR_VALIDATE_OTP", "vtshop.validate.otp.overlength", otp);
        }

        otp = otp.trim();

        if (!DataUtil.isNullOrEmpty(idNo)) {
            if (idNo.trim().getBytes("UTF-8").length > 15) {
                throw new LogicException("ERR_VALIDATE_IDNO", "vtshop.validate.idno.overlength", idNo);
            }
            idNo = idNo.trim();
        }

        if (DataUtil.isNullOrEmpty(viettelshopId)) {
            throw new LogicException("ERR_VALIDATE_VIETTELSHOPID", "vtshop.validate.viettelshopId.require");
        }

        if (viettelshopId.trim().getBytes("UTF-8").length > 50) {
            throw new LogicException("ERR_VALIDATE_VIETTELSHOPID", "vtshop.validate.viettelshopId.overlength", viettelshopId);
        }

        viettelshopId = viettelshopId.trim();

        if (DataUtil.isNullOrEmpty(customerName)) {
            throw new LogicException("ERR_VALIDATE_CUSTOMERNAME", "vtshop.validate.customerName.require");
        }

        if (customerName.trim().getBytes("UTF-8").length > 200) {
            throw new LogicException("ERR_VALIDATE_CUSTOMERNAME", "vtshop.validate.customerName.overlength", customerName);
        }

        customerName = customerName.trim();

        if (DataUtil.isNullOrEmpty(contactIsdn)) {
            throw new LogicException("ERR_VALIDATE_CONTACTISDN", "vtshop.validate.contactIsdn.require");
        }

        if (!DataUtil.isNumber(contactIsdn)) {
            throw new LogicException("ERR_VALIDATE_CONTACTISDN", "vtshop.validate.contactIsdn.notNumber", contactIsdn);
        }

        if (contactIsdn.trim().length() > 50) {
            throw new LogicException("ERR_VALIDATE_CONTACTISDN", "vtshop.validate.contactIsdn.overlength", contactIsdn);
        }

        contactIsdn = contactIsdn.trim();

        if (DataUtil.isNullOrEmpty(address)) {
            throw new LogicException("ERR_VALIDATE_ADDRESS", "vtshop.validate.address.require");
        }

        if (address.trim().getBytes("UTF-8").length > 200) {
            throw new LogicException("ERR_VALIDATE_ADDRESS", "vtshop.validate.address.overlength", address);
        }

        address = address.trim();

        if (expiredDate == null) {
            throw new LogicException("ERR_VALIDATE_EXPIRED_DATE", "vtshop.validate.expiredDate.require");
        }

        if (DataUtil.isNullOrEmpty(payStatus)) {
            throw new LogicException("ERR_VALIDATE_PAY_STATUS", "vtshop.validate.payStatus.require");
        }

        if (!Const.STATUS_INACTIVE.equals(payStatus) && !Const.STATUS_ACTIVE.equals(payStatus)) {
            throw new LogicException("ERR_VALIDATE_PAY_STATUS", "vtshop.validate.payStatus.invalid");
        }

        List<OptionSetValueDTO> lstOptionSet = optionSetValueService.getByOptionSetCode("VTSHOP_FEE");

        if (DataUtil.isNullOrEmpty(lstOptionSet)) {
            throw new LogicException("ERR_VALIDATE_OPTION_SET", "vtshop.validate5.optionSet.require");
        }

        List<String> lsKey = lstOptionSet.stream().map(OptionSetValueDTO::getValue).collect(Collectors.toList());
        List<String> lsFeeType = new ArrayList<>();

        if (!DataUtil.isNullOrEmpty(vtShopFeeeDTOs)) {
            for (VtShopFeeDTO vtShopFeeDTO : vtShopFeeeDTOs) {
                if (DataUtil.isNullOrEmpty(vtShopFeeDTO.getFeeType())) {
                    throw new LogicException("ERR_VALIDATE_FEE", "vtshop.validate.fee.null");
                }
                if (!lsKey.contains(vtShopFeeDTO.getFeeType().toUpperCase())) {
                    throw new LogicException("ERR_VALIDATE_FEE", "vtshop.validate.fee.notMap", vtShopFeeDTO.getFeeType());
                }

                if ((vtShopFeeDTO.getAmount() == null) || vtShopFeeDTO.getAmount() < 0L || vtShopFeeDTO.getAmount() > 1000000000) {
                    throw new LogicException("ERR_VALIDATE_FEE", "vtshop.validate.fee.invalid", vtShopFeeDTO.getFeeType());
                }

                if (lsFeeType.contains(vtShopFeeDTO.getFeeType())) {
                    throw new LogicException("ERR_VALIDATE_FEE", "vtshop.validate.fee.duplicate", vtShopFeeDTO.getFeeType());
                }

                lsFeeType.add(vtShopFeeDTO.getFeeType());
            }
        }

        boolean isdnIsValid = stockNumberRepo.checkValidIsdn(isdnOrder);

        if (!isdnIsValid) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.used", isdnOrder);
        }

        Date createDate = getSysDate(em);
        StockIsdnVtShopDTO stockIsdnVtShopDTO = findOne(isdnOrder);

        if (!DataUtil.isNullObject(stockIsdnVtShopDTO) && Const.STATUS_INACTIVE.equals(stockIsdnVtShopDTO.getStatus())) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.order.used", isdnOrder);
        }

        stockIsdnVtShopDTO = new StockIsdnVtShopDTO();
        stockIsdnVtShopDTO.setCreateDate(createDate);
        stockIsdnVtShopDTO.setIsdn(isdnOrder);
        stockIsdnVtShopDTO.setOtp(otp);
        stockIsdnVtShopDTO.setIdNo(idNo);
        stockIsdnVtShopDTO.setViettelshopId(viettelshopId);
        stockIsdnVtShopDTO.setCustomerName(customerName);
        stockIsdnVtShopDTO.setContactIsdn(contactIsdn);
        stockIsdnVtShopDTO.setAddress(address);
        stockIsdnVtShopDTO.setLastModify(createDate);
        stockIsdnVtShopDTO.setExpiredDate(expiredDate);
        stockIsdnVtShopDTO.setPayStatus(payStatus);
        stockIsdnVtShopDTO.setStatus(Const.STATUS_INACTIVE);

        save(stockIsdnVtShopDTO);

        //Xoa cac phi neu ton tai
        stockIsdnVtShopFeeService.deleteFee(isdnOrder);

        //Xoa cac shop lock
        stockIsdnVtShopLockService.deleteShopLock(isdnOrder);

        //Luu phi dat so
        if (!DataUtil.isNullOrEmpty(vtShopFeeeDTOs)) {
            for (VtShopFeeDTO vtShopFeeDTO : vtShopFeeeDTOs) {
                StockIsdnVtShopFeeDTO stockIsdnVtShopFeeDTO = new StockIsdnVtShopFeeDTO();
                stockIsdnVtShopFeeDTO.getStockIsdnVtShopFeeId().setIsdn(isdnOrder);
                stockIsdnVtShopFeeDTO.getStockIsdnVtShopFeeId().setFeeType(vtShopFeeDTO.getFeeType().toUpperCase());
                stockIsdnVtShopFeeDTO.setAmount(vtShopFeeDTO.getAmount());

                stockIsdnVtShopFeeService.save(stockIsdnVtShopFeeDTO);
            }
        }

        //Luu cac shop lock
        if (!DataUtil.isNullOrEmpty(lstShopVtPost)) {
            for (String shopCode : lstShopVtPost) {
                ShopDTO shop = shopService.getShopByCodeAndActiveStatus(shopCode);
                if (shop == null || DataUtil.isNullOrZero(shop.getShopId())) {
                    throw new LogicException("ERR_VALIDATE_SHOP_LOCK", "vtshop.validate.shop.lock.invalid", shopCode);
                }

                StockIsdnVtShopLockDTO stockIsdnVtShopLockDTO = new StockIsdnVtShopLockDTO();
                stockIsdnVtShopLockDTO.getStockIsdnVtShopLockId().setIsdn(isdnOrder);
                stockIsdnVtShopLockDTO.getStockIsdnVtShopLockId().setOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                stockIsdnVtShopLockDTO.getStockIsdnVtShopLockId().setOwnerId(shop.getShopId());

                stockIsdnVtShopLockService.save(stockIsdnVtShopLockDTO);
            }
        }

    }

    public void increaseExpiredDateOrder(String isdnOrder, Date expiredDate) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(isdnOrder)) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.require");
        }

        if (!DataUtil.isNumber(isdnOrder)) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.notNumber", isdnOrder);
        }

        if (isdnOrder.trim().length() > 12) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.overlength", isdnOrder);
        }

        isdnOrder = isdnOrder.trim();

        //Chuan hoa so thue bao
        if (isdnOrder.startsWith("0")) {
            isdnOrder = isdnOrder.substring(1);
        }

        if (isdnOrder.startsWith("84")) {
            isdnOrder = isdnOrder.substring(2);
        }

        if (expiredDate == null) {
            throw new LogicException("ERR_VALIDATE_EXPIRED_DATE", "vtshop.validate.expiredDate.require");
        }

//        if (expiredDate.before(getSysDate(em))) {
//            throw new LogicException("ERR_VALIDATE_EXPIRED_DATE", "vtshop.validate.expiredDate.invalid");
//        }

        StockIsdnVtShopDTO stockIsdnVtShopDTO = findOne(isdnOrder);

        if (DataUtil.isNullObject(stockIsdnVtShopDTO)) {
            throw new LogicException("ERR_ORDER_NOT_EXISTS", "vtshop.validate.isdn.used", isdnOrder);
        }

        if (!Const.STATUS_INACTIVE.equals(stockIsdnVtShopDTO.getStatus())) {
            throw new LogicException("ERR_ORDER_NOT_EXISTS", "vtshop.validate.order.invalid");
        }
        stockIsdnVtShopDTO.setExpiredDate(expiredDate);

        save(stockIsdnVtShopDTO);

    }

    public List<StockIsdnVtShopFeeDTO> findVtShopFeeByIsdn(String isdn) throws Exception {
        return stockIsdnVtShopFeeRepo.findVtShopFeeByIsdn(isdn);
    }

    @Transactional(rollbackFor = Exception.class)
    public void lockIsdnVtshop(String isdn, Long requestId, String staffCode) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(isdn)) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.require");
        }

        if (!DataUtil.isNumber(isdn)) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.notNumber", isdn);
        }

        if (isdn.trim().length() > 12) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.overlength", isdn);
        }

        isdn = isdn.trim();

        if (DataUtil.isNullOrZero(requestId)) {
            throw new LogicException("ERR_VALIDATE_REQUEST", "vtshop.validate.request.require");
        }

        StaffDTO staff = null;
        if (!DataUtil.isNullOrEmpty(staffCode)) {
            staff = staffService.getStaffByStaffCode(staffCode);
            if (staff == null) {
                throw new LogicException("STAFF_CODE_INVALID", "common.valid.staff", staffCode);
            }
        }

        StockIsdnVtShopDTO stockIsdnVtShopDTO = findOne(isdn);

        if (stockIsdnVtShopDTO == null || DataUtil.isNullOrEmpty(stockIsdnVtShopDTO.getIsdn())) {
            throw new LogicException("ORDER_INVALID", "vtshop.validate.request.notExists", isdn);
        }

        if (!Const.STATUS_INACTIVE.equals(stockIsdnVtShopDTO.getStatus())) {
            throw new LogicException("ORDER_INVALID", "vtshop.validate.request.invalid");
        }

        if (!requestId.equals(stockIsdnVtShopDTO.getRequestId())) {
            stockIsdnVtShopDTO.setRequestId(requestId);
            stockIsdnVtShopDTO.setValid(null);//set null
            save(stockIsdnVtShopDTO);
        }

        //Thuc hien lock so
        List<Long> lstStatus = Arrays.asList(Const.NEW, Const.SUSPEND);

        if (staff != null) {
            lstStatus = Arrays.asList(Const.LOCKED);
        }
        stockNumberRepo.lockOrUnlockIsdn(isdn, Const.LOCKED, lstStatus, staff);
    }

    @Transactional(rollbackFor = Exception.class)
    public void lockIsdnHotline(String isdn, String staffCode) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(isdn)) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.require");
        }

        if (!DataUtil.isNumber(isdn)) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.notNumber", isdn);
        }

        if (isdn.trim().length() > 12) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.overlength", isdn);
        }

        isdn = isdn.trim();

        StaffDTO staff = null;
        if (!DataUtil.isNullOrEmpty(staffCode)) {
            staff = staffService.getStaffByStaffCode(staffCode);
            if (staff == null) {
                throw new LogicException("STAFF_CODE_INVALID", "common.valid.staff", staffCode);
            }
        }

        //Thuc hien lock so
        List<Long> lstStatus = Arrays.asList(Const.NEW, Const.SUSPEND);

        if (staff != null) {
            lstStatus = Arrays.asList(Const.LOCKED);
        }
        stockNumberRepo.lockOrUnlockIsdn(isdn, Const.LOCKED, lstStatus, staff);
    }

    @Transactional(rollbackFor = Exception.class)
    public void unlockIsdnHotline(String isdn) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(isdn)) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.require");
        }

        if (!DataUtil.isNumber(isdn)) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.notNumber", isdn);
        }

        if (isdn.trim().length() > 12) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.overlength", isdn);
        }

        isdn = isdn.trim();

        //Thuc hien unlock so
        List<Long> lstStatus = Arrays.asList(Const.LOCKED);
        stockNumberRepo.lockOrUnlockIsdn(isdn, Const.NEW, lstStatus, null);
    }

    public List<StockIsdnVtShopDTO> findRequestList(List<Long> requestList) {

        if (DataUtil.isNullOrEmpty(requestList) || requestList.size() > 1000) {
            return null;
        }

        return mapper.toDtoBean(repository.findRequestList(requestList));
    }

    public void cancelOrderIsdn(String isdnOrder) throws LogicException, Exception {

        if (DataUtil.isNullOrEmpty(isdnOrder)) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.require");
        }

        if (!DataUtil.isNumber(isdnOrder)) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.notNumber", isdnOrder);
        }

        if (isdnOrder.trim().length() > 12) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.overlength", isdnOrder);
        }

        isdnOrder = isdnOrder.trim();

        //Chuan hoa so thue bao
        if (isdnOrder.startsWith("0")) {
            isdnOrder = isdnOrder.substring(1);
        }

        if (isdnOrder.startsWith("84")) {
            isdnOrder = isdnOrder.substring(2);
        }

        StockIsdnVtShopDTO stockIsdnVtShopDTO = findOne(isdnOrder);

        if (DataUtil.isNullObject(stockIsdnVtShopDTO)) {
            throw new LogicException("ERR_ORDER_NOT_EXISTS", "vtshop.validate.isdn.used", isdnOrder);
        }

        if (!Const.STATUS_INACTIVE.equals(stockIsdnVtShopDTO.getStatus())) {
            throw new LogicException("ERR_ORDER_NOT_EXISTS", "vtshop.validate.order.invalid.cancel");
        }
        stockIsdnVtShopDTO.setStatus(Const.STATUS_DELETE);

        save(stockIsdnVtShopDTO);

        if (stockIsdnVtShopDTO.getRequestId() != null) {
            List<Long> lstStatus = Arrays.asList(Const.LOCKED);
            stockNumberRepo.lockOrUnlockIsdn(stockIsdnVtShopDTO.getIsdn(), Const.NEW, lstStatus, null);
        }

    }

    public List<StockTotalFullDTO> findStockByArea(Long shopId, Long parentShopId, Long prodOfferId) throws Exception {
        return stockIsdnVtShopFeeRepo.findStockByArea(shopId, parentShopId, prodOfferId);
    }

    @Override
    public List<StockTotalFullDTO> findStockDigital(Long shopId, Long prodOfferId) throws Exception {
        return stockIsdnVtShopFeeRepo.findStockDigital(shopId, prodOfferId);
    }

    public void editOrderIsdn(String isdnOrder, String oldIdNo, String newIdNo) throws LogicException, Exception {

        //Validate so CMT cu
        if (DataUtil.isNullOrEmpty(oldIdNo)) {
            throw new LogicException("ERR_VALIDATE_IDNO", "vtshop.validate.oldIdNo.require");
        } else {
            if (oldIdNo.trim().getBytes("UTF-8").length > 15) {
                throw new LogicException("ERR_VALIDATE_IDNO", "vtshop.validate.idno.overlength", oldIdNo);
            }
            oldIdNo = oldIdNo.trim();
        }

        //Validate so CMT moi
        if (DataUtil.isNullOrEmpty(newIdNo)) {
            throw new LogicException("ERR_VALIDATE_IDNO", "vtshop.validate.newIdNo.require");
        } else {
            if (newIdNo.trim().getBytes("UTF-8").length > 15) {
                throw new LogicException("ERR_VALIDATE_IDNO", "vtshop.validate.idno.overlength", newIdNo);
            }
            newIdNo = newIdNo.trim();
        }

        if (DataUtil.isNullOrEmpty(isdnOrder)) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.require");
        }

        if (!DataUtil.isNumber(isdnOrder)) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.notNumber", isdnOrder);
        }

        if (isdnOrder.trim().length() > 12) {
            throw new LogicException("ERR_VALIDATE_ISDN", "vtshop.validate.isdn.overlength", isdnOrder);
        }

        isdnOrder = isdnOrder.trim();

        //Chuan hoa so thue bao
        if (isdnOrder.startsWith("0")) {
            isdnOrder = isdnOrder.substring(1);
        }

        if (isdnOrder.startsWith("84")) {
            isdnOrder = isdnOrder.substring(2);
        }

        StockIsdnVtShopDTO stockIsdnVtShopDTO = findOne(isdnOrder);

        if (DataUtil.isNullObject(stockIsdnVtShopDTO) || !DataUtil.safeEqual(stockIsdnVtShopDTO.getIdNo(), oldIdNo)) {
            throw new LogicException("ERR_ORDER_NOT_EXISTS", "vtshop.validate.isdn.used", isdnOrder);
        }

        if (!Const.STATUS_INACTIVE.equals(stockIsdnVtShopDTO.getStatus())) {
            throw new LogicException("ERR_ORDER_NOT_EXISTS", "vtshop.validate.order.invalid.edit");
        }

        stockIsdnVtShopDTO.setIdNo(newIdNo);
        stockIsdnVtShopDTO.setOtp(newIdNo);
        stockIsdnVtShopDTO.setValid(null);//dong bo ve bccs1

        save(stockIsdnVtShopDTO);
    }
}
