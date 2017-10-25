package com.viettel.bccs.inventory.service.BaseStock;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.PartnerContractDetailService;
import com.viettel.bccs.inventory.service.ProductOfferingService;
import com.viettel.bccs.inventory.service.StockTransDetailService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.*;

/**
 * Created by hoangnt14 on 6/19/2017.
 */
@Service
public class BaseStockImportFromPartnerByFileService extends BaseStockPartnerService {

    public static final Logger logger = Logger.getLogger(BaseStockImportFromPartnerByFileService.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private ProductOfferingService productOfferingService;

    @Autowired
    private PartnerContractDetailService partnerContractDetailService;

    @Autowired
    private StockTransDetailService stockTransDetailService;

    int countTotal;
    Date sysdate;

    @Transactional(rollbackFor = Exception.class)
    public List<ImportPartnerDetailDTO> executeStockTransForPartner(ImportPartnerRequestDTO importPartnerRequestDTO, StaffDTO staffDTO) throws LogicException, Exception {

        sysdate = getSysDate(em);

        List<ImportPartnerDetailDTO> lstImportPartnerDetailDTOs = importPartnerRequestDTO.getListImportPartnerDetailDTOs();
        List<ImportPartnerDetailDTO> listEror = Lists.newArrayList();
        List<ImportPartnerDetailDTO> listTmp;
        HashMap<String, List<ImportPartnerDetailDTO>> listImport = new HashMap<String, List<ImportPartnerDetailDTO>>();

        for (ImportPartnerDetailDTO importPartnerDetailDTO : lstImportPartnerDetailDTOs) {
            //Put HashMap
            if (listImport == null || listImport.isEmpty()) {
                listTmp = Lists.newArrayList();
                listTmp.add(importPartnerDetailDTO);
                listImport.put(importPartnerDetailDTO.getProdOfferCode(), listTmp);
            } else {
                listTmp = listImport.get(importPartnerDetailDTO.getProdOfferCode());
                if (listTmp == null || listTmp.isEmpty()) {
                    listTmp = Lists.newArrayList();
                }
                listTmp.add(importPartnerDetailDTO);
                listImport.put(importPartnerDetailDTO.getProdOfferCode(), listTmp);
            }
        }

        HashMap<String, List<ImportPartnerDetailDTO>> listImportErr = validateObjImportFromPartnerByFile(listImport);
        if (listImportErr != null && !listImportErr.isEmpty()) {
            listEror = new ArrayList<ImportPartnerDetailDTO>();
            Set<String> key = listImportErr.keySet();
            Iterator<String> iteratorKey = key.iterator();
            while (iteratorKey.hasNext()) {
                String propertyName = iteratorKey.next();
                listEror.addAll(listImportErr.get(propertyName));
            }
        }
        //Thuc hien nhap hang
        if (!DataUtil.isNullOrEmpty(listImport)) {
            importStockPartner(importPartnerRequestDTO, listImport, listEror, staffDTO);
        }
        return listEror;
    }

    public String importStockPartner(ImportPartnerRequestDTO importPartnerRequestDTO, HashMap<String, List<ImportPartnerDetailDTO>> listImport, List<ImportPartnerDetailDTO> listEror, StaffDTO staffDTO) throws Exception {
        Connection conn = IMCommonUtils.getDBIMConnection();
        conn.setAutoCommit(false);
        PreparedStatement stmInsert = null;
        PreparedStatement insertStockTransSerial = null;
        PreparedStatement insertStockTransSerialRegion = null;
        int numberSuccessSerialTotal = 0;
        try {
            //Khoi tao nhap kho 3 mien
            StockTransDTO stockTransRegion = new StockTransDTO();
            if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                stockTransRegion.setStockTransId(DbUtil.getSequence(em, "STOCK_TRANS_SEQ"));
                stockTransRegion.setFromOwnerId(Const.SHOP.SHOP_VTT_ID);
                stockTransRegion.setFromOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                stockTransRegion.setToOwnerId(importPartnerRequestDTO.getRegionStockId());
                stockTransRegion.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
                stockTransRegion.setStockTransType(Const.STOCK_TRANS_TYPE.UNIT);
                stockTransRegion.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                stockTransRegion.setNote(GetTextFromBundleHelper.getText("mn.stock.import.stock.from.partner.to.region"));
                stockTransRegion.setCreateDatetime(sysdate);
                stockTransRegion.setIsAutoGen(Const.STOCK_TRANS.IS_AUTO_GEN_LOGISTIC_THREE_REGION);
                stockTransRegion.setCheckErp(Const.STOCK_TRANS.IS_NOT_ERP);
                stockTransRegion.setStaffId(importPartnerRequestDTO.getImportStaffId());
                insertStockTrans(stockTransRegion, conn);
                //1.2. Insert stock_trans_action status 1->6
                StockTransActionDTO stockTransActionRegion = new StockTransActionDTO();
                stockTransActionRegion.setActionCode(Const.STOCK_TRANS.TRANS_CODE_LX + "_" + importPartnerRequestDTO.getActionCode());
                stockTransActionRegion.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_ORDER);
                stockTransRegion.setNote(GetTextFromBundleHelper.getText("mn.stock.import.stock.from.partner.to.region.order"));
                stockTransActionRegion.setCreateUser(importPartnerRequestDTO.getImportStaffCode());
                stockTransActionRegion.setActionStaffId(importPartnerRequestDTO.getImportStaffId());
                stockTransActionRegion.setCreateDatetime(stockTransRegion.getCreateDatetime());
                insertStockTransAction(stockTransRegion, stockTransActionRegion, conn);

                stockTransActionRegion.setActionCode(Const.STOCK_TRANS.TRANS_CODE_PX + "_" + importPartnerRequestDTO.getActionCode());
                stockTransActionRegion.setStatus(Const.STOCK_TRANS_STATUS.EXPORT_NOTE);
                stockTransRegion.setNote(GetTextFromBundleHelper.getText("mn.stock.import.stock.from.partner.to.region.note"));
                insertStockTransAction(stockTransRegion, stockTransActionRegion, conn);

                stockTransActionRegion.setActionCode(null);
                stockTransActionRegion.setStatus(Const.STOCK_TRANS_STATUS.EXPORTED);
                insertStockTransAction(stockTransRegion, stockTransActionRegion, conn);

                stockTransActionRegion.setActionCode(null);
                stockTransActionRegion.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_ORDER);
                stockTransRegion.setNote(null);
                insertStockTransAction(stockTransRegion, stockTransActionRegion, conn);

                stockTransActionRegion.setActionCode(Const.STOCK_TRANS.TRANS_CODE_PN + "_" + importPartnerRequestDTO.getActionCode());
                stockTransActionRegion.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
                stockTransRegion.setNote(GetTextFromBundleHelper.getText("mn.stock.import.stock.from.partner.to.region.import.note"));
                insertStockTransAction(stockTransRegion, stockTransActionRegion, conn);

                stockTransActionRegion.setActionCode(null);
                stockTransActionRegion.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
                stockTransRegion.setNote(null);
                insertStockTransAction(stockTransRegion, stockTransActionRegion, conn);
            }
            //STOCK_TRANS
            //Khoi tao doi tuong nhap hang tu doi tac
            StockTransDTO stockTransImpVT = new StockTransDTO();
            stockTransImpVT.setStockTransId(DbUtil.getSequence(em, "STOCK_TRANS_SEQ"));
            stockTransImpVT.setFromOwnerId(importPartnerRequestDTO.getPartnerId());
            stockTransImpVT.setFromOwnerType(Const.OWNER_TYPE.PARTNER_LONG);
            stockTransImpVT.setToOwnerId(importPartnerRequestDTO.getToOwnerId());
            stockTransImpVT.setToOwnerType(Const.OWNER_TYPE.SHOP_LONG);
            stockTransImpVT.setStockTransType(Const.STOCK_TRANS_TYPE.UNIT);
            stockTransImpVT.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransImpVT.setCreateDatetime(sysdate);
            stockTransImpVT.setReasonId(Const.REASON_ID.IMPORT_PARTNER);
            stockTransImpVT.setNote(GetTextFromBundleHelper.getText("mn.stock.partner.import.stock.by.file"));
            stockTransImpVT.setStaffId(importPartnerRequestDTO.getImportStaffId());
            //check erp=0(chua dong bo) neu dong bo erp, nguoc lai de = null(khong dong bo erp)
            stockTransImpVT.setCheckErp(importPartnerRequestDTO.isSyncERP() ? null : Const.STOCK_TRANS.IS_NOT_ERP);
            stockTransImpVT.setUserCreate(importPartnerRequestDTO.getImportStaffCode());
            stockTransImpVT.setRegionStockTransId(stockTransRegion.getStockTransId());
            // 3.1 Stocktran
            insertStockTrans(stockTransImpVT, conn);
            //3.2 Insert bang stock_trans_action, status = 2
            StockTransActionDTO stockTransActionImpVT = new StockTransActionDTO();
            stockTransActionImpVT.setActionCode(importPartnerRequestDTO.getActionCode());
            stockTransActionImpVT.setStatus(Const.STOCK_TRANS_STATUS.IMPORT_NOTE);
            stockTransActionImpVT.setCreateUser(importPartnerRequestDTO.getImportStaffCode());
            stockTransActionImpVT.setActionStaffId(importPartnerRequestDTO.getImportStaffId());
            stockTransActionImpVT.setCreateDatetime(stockTransImpVT.getCreateDatetime());
            stockTransActionImpVT.setRegionOwnerId(importPartnerRequestDTO.getRegionStockId());

            if (importPartnerRequestDTO.isSignVoffice()) {
                stockTransActionImpVT.setSignCaStatus(Const.SIGN_VOFFICE);
            }
            insertStockTransAction(stockTransImpVT, stockTransActionImpVT, conn);
            //status = 6
            stockTransActionImpVT.setStatus(Const.STOCK_TRANS_STATUS.IMPORTED);
            stockTransActionImpVT.setActionCode(null);
            stockTransImpVT.setNote(null);
            insertStockTransAction(stockTransImpVT, stockTransActionImpVT, conn);
            stockTransImpVT.setActionCode(importPartnerRequestDTO.getActionCode());
            //3.2. insert vao bang stock trans voffice
            if (importPartnerRequestDTO.isSignVoffice()) {
                insertVoffice(conn, importPartnerRequestDTO, stockTransImpVT, null);
            }
            //PARTNER_CONTRACT
            Long partnerContractId = DbUtil.getSequence(em, "partner_contract_seq");
            importPartnerRequestDTO.setPartnerContractId(partnerContractId);
            doSavePartnerContract(importPartnerRequestDTO, conn, stockTransImpVT);

            //insert STOCK_TRANS_DETAIL
            List<ImportPartnerDetailDTO> listTmp = Lists.newArrayList();
            List<ImportPartnerDetailDTO> listTmpErr = Lists.newArrayList();
            if (listImport != null && !listImport.isEmpty()) {
                Set<String> key = listImport.keySet();
                Iterator<String> iteratorKey = key.iterator();
                int count = 0;
                int numberErrorSerial = 0;
                int numberSuccessSerial = 0;
                while (iteratorKey.hasNext()) {
                    String stockModelCode = iteratorKey.next();
                    listTmp = listImport.get(stockModelCode);

                    if (listTmp != null && !listTmp.isEmpty()) {
                        StockTransDetailDTO stockTransDetail = new StockTransDetailDTO();
                        StockTransDetailDTO stockTransDetailRegion = new StockTransDetailDTO();
                        ProductOfferingDTO productOfferingDTO = productOfferingService.getProductByCode(stockModelCode.trim());
                        PartnerContractDetailDTO partnerContractDetail = new PartnerContractDetailDTO();
                        partnerContractDetail.setPartnerContractDetailId(getSequence(em, "PARTNER_CONTRACT_DETAIL_SEQ"));
                        partnerContractDetail.setPartnerContractId(partnerContractId);
                        partnerContractDetail.setCreateDate(sysdate);
                        partnerContractDetail.setProdOfferId(productOfferingDTO.getProductOfferingId());
                        partnerContractDetail.setStateId(listTmp.get(0).getStateId());
                        partnerContractDetail.setQuantity(0L);//Cap nhat lai sau
                        partnerContractDetail.setCurrencyType(importPartnerRequestDTO.getCurrencyType());
                        partnerContractDetail.setUnitPrice(listTmp.get(0).getUnitPrice().longValue());
                        partnerContractDetail.setOrderCode(listTmp.get(0).getOrderCode().trim());
                        partnerContractDetail.setStartDateWarranty(listTmp.get(0).getStartDateWarranty());
                        partnerContractDetailService.save(partnerContractDetail);

                        //insert du lieu vao bang stock_trans_detail
                        Long stockTransDetailId = getSequence(em, "STOCK_TRANS_DETAIL_SEQ");
                        stockTransDetail.setStockTransDetailId(stockTransDetailId);
                        stockTransDetail.setStockTransId(stockTransImpVT.getStockTransId());
                        stockTransDetail.setProdOfferId(productOfferingDTO.getProductOfferingId());
                        stockTransDetail.setStateId(Long.valueOf(listTmp.get(0).getStrStateId().trim()));
                        stockTransDetail.setQuantity(0L);
                        stockTransDetail.setCreateDatetime(sysdate);
                        insertStockTransDetail(stockTransImpVT, Lists.newArrayList(stockTransDetail), conn);

                        Long stockTransDetailRegionId = 0L;
                        if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                            //GD XU?T tu VT--> BA MIEN
                            stockTransDetailRegionId = getSequence(em, "STOCK_TRANS_DETAIL_SEQ");
                            stockTransDetailRegion.setStockTransDetailId(stockTransDetailRegionId);
                            stockTransDetailRegion.setStockTransId(stockTransRegion.getStockTransId());
                            stockTransDetailRegion.setProdOfferId(productOfferingDTO.getProductOfferingId());
                            stockTransDetailRegion.setStateId(Long.valueOf(listTmp.get(0).getStrStateId().trim()));
                            stockTransDetailRegion.setQuantity(0L);
                            stockTransDetailRegion.setCreateDatetime(sysdate);
                            insertStockTransDetail(stockTransRegion, Lists.newArrayList(stockTransDetailRegion), conn);

                        }
                        //Neu mat hang la DIEN THOAI
                        if (productOfferingDTO.getProductOfferTypeId().compareTo(Const.PRODUCT_OFFER_TYPE.PHONE) == 0
                                || (productOfferingDTO.getProductOfferTypeId().compareTo(Const.PRODUCT_OFFER_TYPE.ACCESSORIES) == 0 && productOfferingDTO.getCheckSerial().compareTo(1L) == 0)) {
                            //Cau lenh Insert Serial vao bang STOCK_HANDSET
                            StringBuffer strInsertQuery = new StringBuffer();
                            strInsertQuery.append(" insert into BCCS_IM.STOCK_HANDSET ");
                            strInsertQuery.append(" (ID, OWNER_TYPE, OWNER_ID, STATUS, STATE_ID, PROD_OFFER_ID, CREATE_DATE, CREATE_USER,TELECOM_SERVICE_ID, STOCK_TRANS_ID, CONTRACT_CODE, PO_CODE, SERIAL ) ");
                            strInsertQuery.append(" values (STOCK_HANDSET_SEQ.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE, ?, ?, ? , ? , ?, ? ) ");
                            strInsertQuery.append(" log errors reject limit unlimited"); //chuyen cac ban insert loi ra bang log
                            stmInsert = conn.prepareStatement(strInsertQuery.toString());
                            //Cau lenh Insert Serial vao bang Stock_trans_serial
                            StringBuffer strQueryInsertStockTransSerial = new StringBuffer("");
                            strQueryInsertStockTransSerial.append("insert into BCCS_IM.STOCK_TRANS_SERIAL (STOCK_TRANS_SERIAL_ID, STATE_ID, STOCK_TRANS_ID, PROD_OFFER_ID, FROM_SERIAL, TO_SERIAL, QUANTITY, CREATE_DATETIME,STOCK_TRANS_DETAIL_ID) ");
                            strQueryInsertStockTransSerial.append("values (STOCK_TRANS_SERIAL_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, SYSDATE,?) ");
                            insertStockTransSerial = conn.prepareStatement(strQueryInsertStockTransSerial.toString());
                            insertStockTransSerialRegion = conn.prepareStatement(strQueryInsertStockTransSerial.toString());
                            count = 0;
                            numberErrorSerial = 0;
                            numberSuccessSerial = 0;
                            for (int i = 0; i < listTmp.size(); i++) {
                                ImportPartnerDetailDTO obj = listTmp.get(i);
                                //Insert Serial vao bang STOCK_HANDSET
                                if (obj.getQuantity().compareTo(1L) == 0) {
                                    stmInsert.setLong(1, Const.OWNER_TYPE.SHOP_LONG); //thiet lap truong ownerType
                                    stmInsert.setLong(2, importPartnerRequestDTO.getToOwnerId()); //thiet lap truong ownerId
                                    stmInsert.setLong(3, DataUtil.safeToLong(Const.STATUS_ACTIVE)); //thiet lap truong status
                                    stmInsert.setLong(4, obj.getStateId()); //trang thai hang (moi, cu, hong)
                                    stmInsert.setLong(5, productOfferingDTO.getProductOfferingId()); //id cua mat hang can import
                                    stmInsert.setString(6, staffDTO.getStaffCode()); //nguoi tao
//                                    stmInsert.setString(7, staffDTO.getStaffCode()); //session id (phuc vu viec log loi)
                                    stmInsert.setLong(7, productOfferingDTO.getTelecomServiceId());
                                    stmInsert.setLong(8, stockTransImpVT.getStockTransId());
                                    stmInsert.setString(9, (importPartnerRequestDTO.getContractCode() == null) ? "" : importPartnerRequestDTO.getContractCode().trim().toUpperCase());
                                    stmInsert.setString(10, (importPartnerRequestDTO.getPoCode() == null) ? "" : importPartnerRequestDTO.getPoCode().trim().toUpperCase());
                                    stmInsert.setString(11, obj.getFromSerial());
                                    stmInsert.addBatch();
                                    //Insert Serial vao bang Stock_trans_serial
                                    insertStockTransSerial.setLong(1, obj.getStateId());
                                    insertStockTransSerial.setLong(2, stockTransImpVT.getStockTransId());
                                    insertStockTransSerial.setLong(3, productOfferingDTO.getProductOfferingId());
                                    insertStockTransSerial.setString(4, obj.getFromSerial());
                                    insertStockTransSerial.setString(5, obj.getToSerial());
                                    insertStockTransSerial.setLong(6, 1L);
                                    insertStockTransSerial.setLong(7, stockTransDetailId);
                                    insertStockTransSerial.addBatch();
                                    if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                                        //Insert Serial vao bang Stock_trans_serial //GD XU?T tu VT--> BA MIEN
                                        insertStockTransSerialRegion.setLong(1, obj.getStateId());
                                        insertStockTransSerialRegion.setLong(2, stockTransRegion.getStockTransId());
                                        insertStockTransSerialRegion.setLong(3, productOfferingDTO.getProductOfferingId());
                                        insertStockTransSerialRegion.setString(4, obj.getFromSerial());
                                        insertStockTransSerialRegion.setString(5, obj.getToSerial());
                                        insertStockTransSerialRegion.setLong(6, 1L);
                                        insertStockTransSerial.setLong(7, stockTransDetailRegionId);
                                        insertStockTransSerialRegion.addBatch();
                                    }
                                    count++;
                                } else {
                                    BigInteger fromSerial = null;
                                    BigInteger toSerial = null;
                                    try {
                                        fromSerial = new BigInteger(obj.getFromSerial().trim());
                                        toSerial = new BigInteger(obj.getToSerial().trim());
                                    } catch (NumberFormatException ex) {
                                        return getTextParam("partner.by.file.import.serial.invalid", obj.getFromSerial(), obj.getToSerial());
                                    }
                                    while (fromSerial.compareTo(toSerial) <= 0) {
                                        stmInsert.setLong(1, Const.OWNER_TYPE.SHOP_LONG); //thiet lap truong ownerType
                                        stmInsert.setLong(2, importPartnerRequestDTO.getToOwnerId()); //thiet lap truong ownerId
                                        stmInsert.setLong(3, DataUtil.safeToLong(Const.STATUS_ACTIVE)); //thiet lap truong status
                                        stmInsert.setLong(4, obj.getStateId()); //trang thai hang (moi, cu, hong)
                                        stmInsert.setLong(5, productOfferingDTO.getProductOfferingId()); //id cua mat hang can import
                                        stmInsert.setString(6, staffDTO.getStaffCode()); //nguoi tao
                                        stmInsert.setString(7, staffDTO.getStaffCode()); //session id (phuc vu viec log loi)
                                        stmInsert.setLong(8, productOfferingDTO.getTelecomServiceId());
                                        stmInsert.setLong(9, stockTransImpVT.getStockTransId());
                                        stmInsert.setString(10, (importPartnerRequestDTO.getContractCode() == null) ? "" : importPartnerRequestDTO.getContractCode().trim().toUpperCase());
                                        stmInsert.setString(11, (importPartnerRequestDTO.getPoCode() == null) ? "" : importPartnerRequestDTO.getPoCode().trim().toUpperCase());
                                        stmInsert.setString(12, fromSerial.toString());
                                        stmInsert.addBatch();

                                        //Insert Serial vao bang Stock_trans_serial
                                        insertStockTransSerial.setLong(1, obj.getStateId());
                                        insertStockTransSerial.setLong(2, stockTransImpVT.getStockTransId());
                                        insertStockTransSerial.setLong(3, productOfferingDTO.getProductOfferingId());
                                        insertStockTransSerial.setString(4, fromSerial.toString());
                                        insertStockTransSerial.setString(5, fromSerial.toString());
                                        insertStockTransSerial.setLong(6, 1L);
                                        insertStockTransSerial.setLong(7, stockTransDetailId);
                                        insertStockTransSerial.addBatch();
                                        if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                                            //Insert Serial vao bang Stock_trans_serial //GD XU?T tu VT--> BA MIEN
                                            insertStockTransSerialRegion.setLong(1, obj.getStateId());
                                            insertStockTransSerialRegion.setLong(2, stockTransRegion.getStockTransId());
                                            insertStockTransSerialRegion.setLong(3, productOfferingDTO.getProductOfferingId());
                                            insertStockTransSerialRegion.setString(4, fromSerial.toString());
                                            insertStockTransSerialRegion.setString(5, fromSerial.toString());
                                            insertStockTransSerialRegion.setLong(6, 1L);
                                            insertStockTransSerial.setLong(7, stockTransDetailRegionId);
                                            insertStockTransSerialRegion.addBatch();
                                        }

                                        fromSerial = fromSerial.add(BigInteger.ONE);
                                        count++;
                                        if (count % Const.DEFAULT_BATCH_SIZE == 0) {
                                            insertStockTransSerial.executeBatch();
                                            stmInsert.executeBatch();
                                            insertStockTransSerialRegion.executeBatch();
                                        }
                                    }
                                }
                                if (count % Const.DEFAULT_BATCH_SIZE == 0) {
                                    insertStockTransSerial.executeBatch();
                                    stmInsert.executeBatch();
                                    insertStockTransSerialRegion.executeBatch();
                                }
                            }
                            insertStockTransSerial.executeBatch();
                            stmInsert.executeBatch();
                            insertStockTransSerialRegion.executeBatch();
                            //Dem so luong ban ghi loi trong cac bang ERR
                            if (productOfferingDTO.getProductOfferTypeId().compareTo(Const.PRODUCT_OFFER_TYPE.PHONE) == 0
                                    || (productOfferingDTO.getProductOfferTypeId().compareTo(Const.PRODUCT_OFFER_TYPE.ACCESSORIES) == 0 && productOfferingDTO.getCheckSerial().compareTo(1L) == 0)) {
                                numberErrorSerial = countNumberError("STOCK_HANDSET", conn, stockTransImpVT.getStockTransId());
                            }
                            numberSuccessSerial = count - numberErrorSerial;
                            if (numberSuccessSerial > 0) {
//                            boolean noError = StockTotalAuditDAO.changeStockTotal(conn, toOwnerId, Constant.OWNER_TYPE_SHOP, stockModel.getStockModelId(), listTmp.get(0).getStateId(), Long.valueOf(numberSuccessSerial), Long.valueOf(numberSuccessSerial), 0L, userToken.getUserID(),
//                                    Constant.IMPORT_STOCK_FROM_PARTNER_REASON_ID, stockTransId, this.importStockForm.getActionCode().trim(), Constant.TRANS_IMPORT.toString(), StockTotalAuditDAO.SourceType.STOCK_TRANS, null).isSuccess();
//                            if (!noError) {
//                                numberErrorSerial = count;
//                                numberSuccessSerial = 0;
//                            }
                            }
                            numberSuccessSerialTotal += numberSuccessSerial;
                        } else {
//                        boolean noError = StockTotalAuditDAO.changeStockTotal(conn, toOwnerId, Constant.OWNER_TYPE_SHOP, stockModel.getStockModelId(), listTmp.get(0).getStateId(), listTmp.get(0).getQuantity(), listTmp.get(0).getQuantity(), 0L, userToken.getUserID(),
//                                Constant.IMPORT_STOCK_FROM_PARTNER_REASON_ID, stockTransId, this.importStockForm.getActionCode().trim(), Constant.TRANS_IMPORT.toString(), StockTotalAuditDAO.SourceType.STOCK_TRANS, null).isSuccess();
//                        if (!noError) {
//                            numberErrorSerial = listTmp.get(0).getQuantity().intValue();
//                            numberSuccessSerial = 0;
//                        } else {
                            numberErrorSerial = 0;
                            numberSuccessSerial = listTmp.get(0).getQuantity().intValue();
//                        }
                            numberSuccessSerialTotal += numberSuccessSerial;
                        }
                        if (numberSuccessSerial > 0) {//Neu co serial thanh cong
                            //Update so luong serial thanh cong vao Stock_trans_detail
                            stockTransDetail.setQuantity(Long.valueOf(numberSuccessSerial));
                            stockTransDetailService.save(stockTransDetail);
                            if (!DataUtil.isNullOrZero(importPartnerRequestDTO.getRegionStockId())) {
                                //GD XU?T tu VT--> BA MIEN
                                stockTransDetailRegion.setQuantity(Long.valueOf(numberSuccessSerial));
                            }
                            partnerContractDetail.setQuantity(Long.valueOf(numberSuccessSerial));
                            partnerContractDetailService.save(partnerContractDetail);
                        } else {
//                        mySession.delete(stockTransDetail);
//                        mySession.delete(partnerContractDetail);
                        }
                    }
                }
            }
        } finally {
            DbUtil.close(stmInsert);
            DbUtil.close(insertStockTransSerial);
            DbUtil.close(insertStockTransSerialRegion);
            DbUtil.close(conn);
        }
        return null;
    }


    public HashMap<String, List<ImportPartnerDetailDTO>> validateObjImportFromPartnerByFile
            (HashMap<String, List<ImportPartnerDetailDTO>> listImport) {
        HashMap<String, List<ImportPartnerDetailDTO>> listImportErr = new HashMap<String, List<ImportPartnerDetailDTO>>();
        HashMap<String, List<ImportPartnerDetailDTO>> listImportErrStockModel = new HashMap<String, List<ImportPartnerDetailDTO>>();
        List<ImportPartnerDetailDTO> listEror = Lists.newArrayList();
        List<ImportPartnerDetailDTO> listTmp = Lists.newArrayList();
        try {
            Set<String> key = listImport.keySet();
            Iterator<String> iteratorKey = key.iterator();
            while (iteratorKey.hasNext()) {
                String prodOfferCode = iteratorKey.next();
                listTmp = listImport.get(prodOfferCode);
                //check mat hang
                if (DataUtil.isNullOrEmpty(prodOfferCode)) {
                    listEror = setErrorMessage(listTmp, getText("partner.by.file.prod.not.null"));
                    listImportErr.put(prodOfferCode, listEror);
                    listImportErrStockModel.put(prodOfferCode, listTmp);
                } else {
                    ProductOfferingDTO productOfferingDTO = productOfferingService.getProductByCode(prodOfferCode);
                    if (DataUtil.isNullObject(productOfferingDTO) || !DataUtil.safeEqual(productOfferingDTO.getStatus(), Const.STATUS_ACTIVE)) {
                        listEror = setErrorMessage(listTmp, getText("partner.by.file.prod.not.exsit"));
                        listImportErr.put(prodOfferCode, listEror);
                        listImportErrStockModel.put(prodOfferCode, listTmp);
                    } else if (!DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.ACCESSORIES)
                            && !DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.PHONE)
                            && !DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL)) {
                        listEror = setErrorMessage(listTmp, getText("partner.by.file.prod.type.invalid"));
                        listImportErr.put(prodOfferCode, listEror);
                        listImportErrStockModel.put(prodOfferCode, listTmp);
                    } else {
                        Long quantityTotal = 0L;
                        //Check tung ban ghi serial
                        listEror = Lists.newArrayList();
                        for (int i = 0; i < listTmp.size(); i++) {
                            ImportPartnerDetailDTO obj = listTmp.get(i);
                            //Chi cho phep nhap mat hang MOI = 1 va HONG = 3
                            if (!DataUtil.safeEqual(obj.getStrStateId(), Const.STATE_STATUS.NEW.toString())
                                    && !DataUtil.safeEqual(obj.getStrStateId(), Const.STATE_STATUS.DAMAGE.toString())) {
                                obj.setError(getText("partner.by.file.state.invalid"));
                                listEror.add(obj);
                                continue;
                            } else if (!obj.getStrStateId().equals(listTmp.get(0).getStrStateId())) {//check stateId phai trung nhau trong 1 list serial
                                obj.setError(getText("partner.by.file.state.duplicate"));
                                listEror.add(obj);
                                continue;
                            } else {
                                obj.setStateId(DataUtil.safeToLong(obj.getStrStateId()));
                            }
                            //Validate so luong, don gia va ngay het han bao hanh
                            Long quantity = DataUtil.safeToLong(obj.getStrQuantity());
                            if (quantity.compareTo(0L) <= 0) {
                                countTotal += 1;
                                obj.setError(getText("partner.by.file.quantity.invalid"));
                                listEror.add(obj);
                                continue;
                            } else {
                                countTotal += quantity.intValue();
                                obj.setQuantity(quantity);
                            }
                            //Don gia trong 1 mat hang phai dong nhat
                            Double totalPrice = DataUtil.safeToDouble(obj.getStrTotalPrice());
                            if (totalPrice == null || totalPrice.compareTo(0D) <= 0) {
                                obj.setError(getText("partner.by.file.total.price.invalid"));
                                listEror.add(obj);
                                continue;
                            } else {
                                obj.setTotalPrice(totalPrice);
                                Double dPrice = Math.round(totalPrice / quantity * 100.0) / 100.0;//lam tron 2 chu so
                                if (dPrice == null || dPrice.compareTo(0D) <= 0) {
                                    obj.setError(getText("partner.by.file.price.invalid"));
                                    listEror.add(obj);
                                    continue;

                                } else if (!DataUtil.safeEqual(Math.round(totalPrice / quantity),
                                        Math.round(DataUtil.safeToDouble(listTmp.get(0).getStrTotalPrice()) / listTmp.get(0).getQuantity()))) {
                                    obj.setError(getText("partner.by.file.price.not.same"));
                                    listEror.add(obj);
                                    continue;
                                } else {
                                    obj.setUnitPrice(dPrice);
                                }
                            }
                            //Ma lo hang va ngay het han bao hanh phai dong nhat
                            if (DataUtil.isNullOrEmpty(obj.getOrderCode())) {
                                obj.setError(getText("partner.by.file.order.code.null"));
                                listEror.add(obj);
                                continue;
                            } else if (!obj.getOrderCode().trim().equals(listTmp.get(0).getOrderCode().trim())) {
                                obj.setError(getText("partner.by.file.order.code.same"));
                                listEror.add(obj);
                                continue;
                            }
                            Date date = DateUtil.convertStringToTime(obj.getStrStartDateWarranty(), "dd/MM/yyyy");
                            Date now = new Date();
                            if (date == null) {
                                obj.setError(getText("partner.by.file.date.invalid"));
                                listEror.add(obj);
                                continue;
                            } else if (!obj.getStrStartDateWarranty().trim().equals(listTmp.get(0).getStrStartDateWarranty().trim())) {
                                obj.setError(getText("partner.by.file.date.same"));
                                listEror.add(obj);
                                continue;
                            } else if (!date.after(now)) {
                                obj.setError(getText("partner.by.file.date.out.of"));
                                listEror.add(obj);
                                continue;
                            } else {
                                obj.setStartDateWarranty(date);
                            }
                            //Neu la mat hang dien thoai thi check serial (bat buoc nhap)
                            if (DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.PHONE)) {
                                String fromSerial = obj.getFromSerial();
                                String toSerial = obj.getToSerial();
                                if (fromSerial == null || fromSerial.equals("") || toSerial == null || toSerial.equals("")) {
                                    obj.setError(getText("partner.by.file.handset.serial"));
                                    listEror.add(obj);
                                    continue;
                                }
                                String msg = validateSerialHandset(fromSerial, toSerial, obj.getStrQuantity(), listTmp, i);
                                if (msg != null && !msg.equals("")) {
                                    obj.setError(msg);
                                    listEror.add(obj);
                                    continue;
                                }
                            } else if (DataUtil.safeEqual(productOfferingDTO.getProductOfferTypeId(), Const.PRODUCT_OFFER_TYPE.ACCESSORIES)) {
                                String fromSerial = obj.getFromSerial();
                                String toSerial = obj.getToSerial();
                                String msg = validateSerialAccessories(productOfferingDTO.getCheckSerial(), fromSerial, toSerial, obj.getStrQuantity(), listTmp, i);
                                if (msg != null && !msg.equals("")) {
                                    obj.setError(msg);
                                    listEror.add(obj);
                                    continue;
                                }
                            }
                            quantityTotal += obj.getQuantity();
                        }
                        if (listEror != null && !listEror.isEmpty()) {
                            listImportErr.put(prodOfferCode, listEror);
                        }
                        listTmp.removeAll(listEror);
                        //Neu la mat hang No-serial thi gom so luong ve 1 ban ghi
                        if ((productOfferingDTO.getProductOfferTypeId().compareTo(Const.PRODUCT_OFFER_TYPE.ACCESSORIES) == 0
                                || productOfferingDTO.getProductOfferTypeId().compareTo(Const.PRODUCT_OFFER_TYPE.PRODUCT_NO_SERIAL) == 0)
                                && (productOfferingDTO.getCheckSerial().compareTo(1L) != 0) && listTmp != null && !listTmp.isEmpty()) {
                            ImportPartnerDetailDTO obj = listTmp.get(0);
                            obj.setQuantity(quantityTotal);
                            listTmp.removeAll(listTmp);
                            listTmp.add(obj);
                        }
                    }
                }
            }
            //Chuyen cac hashmap loi validate mat hang tu list ban dau sang list Err
            if (listImportErrStockModel != null && !listImportErrStockModel.isEmpty()) {
                Set<String> keyErr = listImportErrStockModel.keySet();
                Iterator<String> iteratorKeyErr = keyErr.iterator();
                while (iteratorKeyErr.hasNext()) {
                    String stockModelCode = iteratorKeyErr.next();
                    listImport.remove(stockModelCode);
                }
            }
            //Them cac hashmap loi validate mat hang cao DS Hasdmap loi
            listImportErr.putAll(listImportErrStockModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listImportErr;
    }

    public List<ImportPartnerDetailDTO> setErrorMessage(List<ImportPartnerDetailDTO> lst, String msg) {
        for (int i = 0; i < lst.size(); i++) {
            ImportPartnerDetailDTO tmp = lst.get(i);
            tmp.setError(msg);
        }
        return lst;
    }

    public String validateSerialAccessories(Long checkSerial, String fromSerial, String toSerial, String strQuantity, List<ImportPartnerDetailDTO> listTmp, int index) {
        try {
            Long quantity = DataUtil.safeToLong(strQuantity);
            if (quantity == null || quantity.compareTo(0L) <= 0) {
                return getText("partner.by.file.quantity.invalid");
            }
            if (checkSerial != null && checkSerial.compareTo(1L) == 0) {
                Long lFromSerial = DataUtil.safeToLong(fromSerial);
                Long lToSerial = DataUtil.safeToLong(toSerial);
                if (fromSerial != null && !fromSerial.equals("") && toSerial != null && !toSerial.equals("")) {
                    if (lFromSerial != null && lToSerial != null) {
                        if (lFromSerial.compareTo(lToSerial) > 0) {
                            return getText("partner.by.file.handset.from.serial.more.than");
                        }
                        Long quantityTmp = lToSerial - lFromSerial + 1;
                        if (quantityTmp.compareTo(quantity) != 0) {
                            return getText("partner.by.file.handset.quantity.not.equal");
                        }
                    } else {
                        return getText("partner.by.file.accessories.serial");
                    }
                }
                //Check trung serial
                if (index > 0) {//Neu la obj thu 2 thi moi check trung serial
                    for (int i = 0; i < index; i++) {
                        ImportPartnerDetailDTO obj = listTmp.get(i);
                        BigInteger fromSerialBig = new BigInteger(fromSerial);
                        BigInteger toSerialBig = new BigInteger(toSerial);
                        BigInteger fromSerialTmp = new BigInteger(obj.getFromSerial());
                        BigInteger toSerialTmp = new BigInteger(obj.getToSerial());
                        if ((fromSerialBig.compareTo(fromSerialTmp) >= 0 && fromSerialBig.compareTo(toSerialTmp) <= 0)
                                || (toSerialBig.compareTo(fromSerialTmp) >= 0 && toSerialBig.compareTo(toSerialTmp) <= 0)) {
                            return getTextParam("partner.by.file.handset.range", fromSerial, toSerial);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getTextParam("partner.by.file.handset.range.error", fromSerial, toSerial);
        }
        return null;
    }


    public String validateSerialHandset(String fromSerial, String toSerial, String strQuantity, List<ImportPartnerDetailDTO> listTmp, int index) {
        try {
            Long quantity = DataUtil.safeToLong(strQuantity);
            Long lFromSerial = DataUtil.safeToLong(fromSerial);
            Long lToSerial = DataUtil.safeToLong(toSerial);
            if (quantity == null || quantity.compareTo(0L) <= 0) {
                return getText("partner.by.file.quantity.invalid");
            }
            if (lFromSerial != null && lToSerial != null) {
                if (lFromSerial.compareTo(lToSerial) > 0) {
                    return getText("partner.by.file.handset.from.serial.more.than");
                }
                Long quantityTmp = lToSerial - lFromSerial + 1;
                if (quantityTmp.compareTo(quantity) != 0) {
                    return getText("partner.by.file.handset.quantity.not.equal");
                }
            } else {
                if (!fromSerial.equals(toSerial)) {
                    return getText("partner.by.file.handset.nchar");
                }
                if (quantity.compareTo(1L) != 0) {
                    return getText("partner.by.file.handset.nchar.quantity");
                }
            }
            //Check trung serial
            if (index > 0) {//Neu la obj thu 2 thi moi check trung serial
                for (int i = 0; i < index; i++) {
                    ImportPartnerDetailDTO obj = listTmp.get(i);
                    if (obj.getQuantity() != null && obj.getQuantity().compareTo(1L) == 0) {
                        if (fromSerial.compareTo(obj.getFromSerial()) <= 0 && toSerial.compareTo(obj.getToSerial()) >= 0) {
                            return getTextParam("partner.by.file.handset.range", fromSerial, toSerial);
                        }
                    } else if (obj.getQuantity() != null && obj.getQuantity().compareTo(1L) > 0) {
                        BigInteger fromSerialBig = new BigInteger(fromSerial);
                        BigInteger toSerialBig = new BigInteger(toSerial);
                        BigInteger fromSerialTmp = new BigInteger(obj.getFromSerial());
                        BigInteger toSerialTmp = new BigInteger(obj.getToSerial());
                        if ((fromSerialBig.compareTo(fromSerialTmp) >= 0 && fromSerialBig.compareTo(toSerialTmp) <= 0)
                                || (toSerialBig.compareTo(fromSerialTmp) >= 0 && toSerialBig.compareTo(toSerialTmp) <= 0)) {
                            return getTextParam("partner.by.file.handset.range", fromSerial, toSerial);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getTextParam("partner.by.file.handset.range.error", fromSerial, toSerial);
        }
        return null;
    }

}
