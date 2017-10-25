package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.dto.ws.PayNoteAndReportDTO;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.ErrorCode;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PayNoteAndReportServiceImpl extends BaseServiceImpl implements PayNoteAndReportService {

    public static final Logger logger = Logger.getLogger(PayNoteAndReportService.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Autowired
    private StockDocumentService stockDocumentService;
    @Autowired
    private StockTransActionService stockTransActionService;
    @Autowired
    private MtService mtService;
    @Autowired
    private OptionSetValueService optionSetValueService;
    private Date currentDate;

    @Override
    public List<VStockTransDTO> getListStockTransForPay(PayNoteAndReportDTO payNoteAndReportDTO,
                                                        String exportOwnerId, String importOwnerId, boolean flag) {
        List lstParam = new ArrayList();
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append("select stock_trans_id, action_code ,action_id, stock_trans_type, action_type,  ")
                .append(" create_datetime, user_create,from_owner_id, from_owner_type, ")
                .append(" from_owner_name, to_owner_id, to_owner_type, to_owner_name, ")
                .append(" note, reason_id, reason_name, stock_trans_status, ")
                .append(" deposit_status, document_status, stock_base, ")
                .append(" status_name ")
                .append(" from v_stock_trans ")
                .append(" where action_code <> 'SYSTEM' ")
                .append(" and action_type = 2 ")
//                .append(" and (stock_trans_status = 4 or ((stock_trans_status = 3 or stock_trans_status = 2) and stock_trans_type = 1)) ")
                .append(" and from_owner_type = 1 and to_owner_type = 1 ");
        if (!DataUtil.isNullOrEmpty(payNoteAndReportDTO.getActionCode())) {
            strQuery.append(" and upper(action_code) like ? ");
            lstParam.add("%" + payNoteAndReportDTO.getActionCode().toUpperCase().trim() + "%");
        }
        if (!DataUtil.isNullObject(payNoteAndReportDTO.getFromDate())) {
            strQuery.append(" and create_datetime >=  ? ");
            lstParam.add(payNoteAndReportDTO.getFromDate());
        }
        if (!DataUtil.isNullObject(payNoteAndReportDTO.getToDate())) {
            strQuery.append(" and create_datetime <=  ? ");
            lstParam.add(DateUtil.addDay(payNoteAndReportDTO.getToDate(),1));
        }
        if (!DataUtil.isNullObject(payNoteAndReportDTO.getStatus())) {
            if(payNoteAndReportDTO.getStatus().equals(Const.STATUS.NO_ACTIVE)) {
                strQuery.append(" and (document_status is null or document_status =  ? ) ");
            }else {
                strQuery.append(" and document_status =   ? ");
            }
            lstParam.add(payNoteAndReportDTO.getStatus());
        }
        if (!DataUtil.isNullObject(exportOwnerId)) {
            strQuery.append(" and from_owner_id =  ? ");
            lstParam.add(exportOwnerId);
        }
        if (!DataUtil.isNullObject(importOwnerId)) {
            strQuery.append(" and to_owner_id =  ? ");
            lstParam.add(importOwnerId);
        }
        if (flag) {
            strQuery.append(" and action_id in (select b.action_id from bccs_im.stock_document b where b.status = 4  ");
            if (!DataUtil.isNullObject(payNoteAndReportDTO.getFromDate())) {
                strQuery.append(" and b.create_date  >=  ? ");
                lstParam.add(payNoteAndReportDTO.getFromDate());
            }
            if (!DataUtil.isNullObject(payNoteAndReportDTO.getToDate())) {
                strQuery.append(" and b.create_date <=  ? ) ");
                lstParam.add(DateUtil.addDay(payNoteAndReportDTO.getToDate(),1));
            }
        }

        strQuery.append("order by create_datetime desc ");


        List<VStockTransDTO> result = Lists.newArrayList();
        Query query = em.createNativeQuery(strQuery.toString());
        if (query != null) {
            for (int i = 0; i < lstParam.size(); i++) {
                query.setParameter(i + 1, lstParam.get(i));
            }

            List queryResult = query.getResultList();
            for (Object obj : queryResult) {
                Object[] objects = (Object[]) obj;
                VStockTransDTO vStockTransDTO = new VStockTransDTO();
                vStockTransDTO.setStockTransID(DataUtil.safeToLong(objects[0]));
                vStockTransDTO.setActionCode(DataUtil.safeToString(objects[1]));
                vStockTransDTO.setActionID(DataUtil.safeToLong(objects[2]));
                vStockTransDTO.setStockTransType(DataUtil.safeToLong(objects[3]));
                vStockTransDTO.setActionType(DataUtil.safeToString(objects[4]));
                vStockTransDTO.setCreateDateTime((Date) objects[5]);
                vStockTransDTO.setUserCreate(DataUtil.safeToString(objects[6]));
                vStockTransDTO.setFromOwnerID(DataUtil.safeToLong(objects[7]));
                vStockTransDTO.setFromOwnerType(DataUtil.safeToLong(objects[8]));
                vStockTransDTO.setFromOwnerName(DataUtil.safeToString(objects[9]));
                vStockTransDTO.setToOwnerID(DataUtil.safeToLong(objects[10]));
                vStockTransDTO.setToOwnerType(DataUtil.safeToLong(objects[11]));
                vStockTransDTO.setToOwnerName(DataUtil.safeToString(objects[12]));
                vStockTransDTO.setNote(DataUtil.safeToString(objects[13]));
                vStockTransDTO.setReasonID(DataUtil.safeToLong(objects[14]));
                vStockTransDTO.setReasonName(DataUtil.safeToString(objects[15]));
                vStockTransDTO.setStockTransStatus(DataUtil.safeToString(objects[16]));
                vStockTransDTO.setDepositStatus(DataUtil.safeToString(objects[17]));
                vStockTransDTO.setDocumentStatus(DataUtil.safeToString(objects[18]));
                vStockTransDTO.setStockBase(DataUtil.safeToString(objects[19]));
                vStockTransDTO.setStatusName(DataUtil.safeToString(objects[20]));
                result.add(vStockTransDTO);
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage createStockDocumentAndUpdateAction(StockDocumentDTO stockDocumentDTO, Long actionId) throws Exception, LogicException {

        BaseMessage result = stockDocumentService.create(stockDocumentDTO);
        if (result.isSuccess()) {
            try {
                List lstParam = new ArrayList();
                StringBuilder strQuery = new StringBuilder("");
                strQuery.append("update stock_Trans_action set document_status = 1 where   ")
                        .append(" (document_status is null or document_status <> 1) ");
                if (!DataUtil.isNullObject(actionId)) {
                    strQuery.append(" and stock_trans_action_id = ? ");
                    lstParam.add(actionId);
                }
                Query query = em.createNativeQuery(strQuery.toString());
                if (query != null) {
                    for (int i = 0; i < lstParam.size(); i++) {
                        query.setParameter(i + 1, lstParam.get(i));
                    }
                    query.executeUpdate();
                }
            } catch (Exception ex) {
                logger.error("", ex);
                result.setSuccess(false);
                throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "payNoteAndReport.errorUpdate.stockTransAction");
            }
        } else {
            result.setSuccess(true);
        }
        return result;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage cancelStockDocument(List<VStockTransDTO> selectForCancel, String strCancelPay, String user) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        try {
            if (DataUtil.isNullObject(strCancelPay)) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_REQUIRE, "payNoteAndReport.reason.cancelPay.required");
            }
            if (strCancelPay.getBytes("UTF-8").length > 200) {
                throw new LogicException(ErrorCode.ERROR_USER.ERROR_USER_INVALID_FORMAT, "payNoteAndReport.reason.cancelPay.maxlength");
            }

            for (VStockTransDTO v : selectForCancel) {
                StockTransActionDTO stockTransActionDTO = stockTransActionService.getStockTransActionDTOById(v.getActionID());
                if (!DataUtil.isNullObject(stockTransActionDTO)) {
                    stockTransActionDTO.setDocumentStatus(Const.STATUS.NO_ACTIVE);
                    stockTransActionService.save(stockTransActionDTO);
                }
                //Lay so dien thoai va insert vao bang MT
                getListStockDocument(v,strCancelPay,user);
                //update stock_document
                List lstParam = new ArrayList();
                StringBuilder strQuery = new StringBuilder("");
                strQuery.append("update stock_document set status = 4, destroy_date = sysdate, export_note = null, delivery_records = null    ");
                if (!DataUtil.isNullObject(user)) {
                    strQuery.append(" ,destroy_user = ? ");
                    lstParam.add(user);
                }
                if (!DataUtil.isNullObject(strCancelPay)) {
                    strQuery.append(" ,REASON = ? ");
                    lstParam.add(strCancelPay);
                }
                strQuery.append(" where status is null");

                if (!DataUtil.isNullObject(v.getActionID())) {
                    strQuery.append(" and action_id =  ? ");
                    lstParam.add(v.getActionID());
                }
                Query query = em.createNativeQuery(strQuery.toString());
                if (query != null) {
                    for (int i = 0; i < lstParam.size(); i++) {
                        query.setParameter(i + 1, lstParam.get(i));
                    }
                    query.executeUpdate();
                }


            }

        } catch (LogicException ex) {
            logger.error("", ex);
            result.setSuccess(false);
            throw ex;
        }catch (Exception ex) {
            logger.error("", ex);
            result.setSuccess(false);
            throw new LogicException(ErrorCode.ERROR_STANDARD.ERROR_EXCEPTION, "payNoteAndReport.cancelAndPay.error");
        }

        return result;
    }

    private void getListStockDocument(VStockTransDTO v, String strCancelPay, String user ) throws Exception,LogicException {
        List lstParam = new ArrayList();
        StringBuilder strQuery = new StringBuilder("");
        strQuery.append("select sd.action_id actionId ,sd.user_create userCreate, st.tel   ")
                .append(" from bccs_im.stock_document sd,bccs_im.staff st where ")
                .append(" st.staff_code = sd.user_create and st.tel is not null and sd.status is null ");

        if (!DataUtil.isNullObject(v.getActionID())) {
            strQuery.append(" and sd.action_id = ? ");
            lstParam.add(v.getActionID());
        }
        Query query = em.createNativeQuery(strQuery.toString());
        if (query != null) {
            for (int i = 0; i < lstParam.size(); i++) {
                query.setParameter(i + 1, lstParam.get(i));
            }
            currentDate = optionSetValueService.getSysdateFromDB(false);
            List queryResult = query.getResultList();
            for (Object obj : queryResult) {
                Object[] objects = (Object[]) obj;
                MtDTO mtDTO = new MtDTO();
                mtDTO.setMsisdn(DataUtil.safeToString(objects[2]));
                String message = "Nhan vien " + user + " da huy phieu xuat va BBBG cua phieu " + v.getActionCode() +
                        " cua d/c voi ly do:" + strCancelPay + ", d/c co the vao he thong de kiem tra.";
                mtDTO.setMessage(message);
                mtDTO.setMoHisId(Const.AppProperties4Sms.moHisID.getLongValue());
                mtDTO.setRetryNum(Const.AppProperties4Sms.retryNum.getLongValue());
                mtDTO.setAppId(Const.AppProperties4Sms.appID.getValue().toString());
                mtDTO.setReceiveTime(currentDate);
                mtDTO.setChannel(Const.AppProperties4Sms.channel.getValue().toString());
                mtDTO.setAppName(Const.AppProperties4Sms.appName.getValue().toString());
                mtService.create(mtDTO);
            }
        }
    }
}
