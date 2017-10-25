package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.expr.BooleanExpression;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.ImsiInfoDTO;
import com.viettel.bccs.inventory.dto.ImsiInfoInputSearch;
import com.viettel.bccs.inventory.dto.UpdateImsiInfoDTO;
import com.viettel.bccs.inventory.model.ImsiInfo;
import com.viettel.bccs.inventory.model.QImsiInfo;
import com.viettel.bccs.inventory.repo.ImsiInfoRepo;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

@Service
public class ImsiInfoServiceImpl extends BaseServiceImpl implements ImsiInfoService {

    private final BaseMapper<ImsiInfo,ImsiInfoDTO> mapper = new BaseMapper<>(ImsiInfo.class,ImsiInfoDTO.class);

    @Autowired
    private ImsiInfoRepo repo;

    @PersistenceContext(unitName = "BCCS_IM")
    private EntityManager em;

    public static final Logger logger = Logger.getLogger(ImsiInfoService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repo.count(repo.toPredicate(filters));
    }

    @WebMethod
    public ImsiInfoDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repo.findOne(id));
    }

    @WebMethod
    public List<ImsiInfoDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repo.findAll(repo.toPredicate(filters)));
    }

    @WebMethod
    public List<ImsiInfoDTO> search(ImsiInfoInputSearch imsiInfoInputSearch) throws Exception {
        return repo.search(imsiInfoInputSearch);
    }

    @WebMethod
    public Long countImsiRange(String fromImsi, String toImsi) throws Exception {
        return repo.countImsiRange(fromImsi, toImsi);
    }

    @WebMethod
    public BaseMessage insertBatch(ImsiInfoDTO imsiInfoDTO) throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        Thread insertThread = new Thread(new Runnable() {
            public void run() {
                Connection ex = null;
                PreparedStatement e = null;
                try {
                    ex = IMCommonUtils.getDBIMConnection();
                    e = ex.prepareStatement("INSERT INTO IMSI_INFO(id,imsi,start_date, end_date, status, doc_no, user_create, create_date, last_update_time, last_update_user) " +
                            "values (imsi_info_seq.nextval," +
                            "?," + //1.imsi
                            "?," + //2.start_date
                            "?," + //3.end_date
                            "?," + //4.status
                            "?," + //5.doc_no
                            "?," + //6.user_create
                            "sysdate," + //.create_date
                            "sysdate," + //.last_update_time
                            "?)" + //7.last_update_user
                            "");
                    for (long ex1 = Long.valueOf(imsiInfoDTO.getFromImsi()); ex1 <= Long.valueOf(imsiInfoDTO.getToImsi()); ex1++) {
                        e.setString(1, ex1 + "");
                        e.setTimestamp(2, new java.sql.Timestamp(imsiInfoDTO.getStartDate().getTime()));
                        e.setTimestamp(3, new java.sql.Timestamp(imsiInfoDTO.getEndDate().getTime()));
                        e.setString(4, imsiInfoDTO.getStatus());
                        e.setString(5, imsiInfoDTO.getDocNo());
                        e.setString(6, imsiInfoDTO.getUserCreate());
                        e.setString(7, imsiInfoDTO.getUserCreate());
                        e.addBatch();
                        if (ex1 % 1000L == 0L) {
                            try {
                                e.executeBatch();
                            } catch (Exception var12) {
                                baseMessage.setSuccess(false);
                                logger.error(var12.getMessage(), var12);
                            }
                        }
                    }
                    try {
                        e.executeBatch();
                    } catch (Exception var11) {
                        baseMessage.setSuccess(false);
                        logger.error(var11.getMessage(), var11);
                    } finally {
                        try{e.close();} catch (Exception e1) {logger.error(e1.getMessage(), e1);}
                        try{ex.close();} catch (Exception e2) {logger.error(e2.getMessage(), e2);}
                    }
                } catch (Exception err) {
                    baseMessage.setSuccess(false);
                    logger.error(err.getMessage(), err);
                }
            }
            ;
        });

        try {
            insertThread.start();
        } catch (Exception e) {
            baseMessage.setSuccess(false);
            logger.error(e.getMessage(), e);
        }
        return baseMessage;
    }

    @WebMethod
    public BaseMessage updateBatch(ImsiInfoDTO imsiInfoDTO) throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        Thread threadUpdate = new Thread(
                new Runnable() {
                    public void run() {
                        Connection ex = null;
                        PreparedStatement psUpdate = null;
                        try {
                            StringBuilder sql = new StringBuilder("");
                            sql.append("UPDATE IMSI_INFO\n " +
                                    " SET START_DATE = ?, END_DATE = ?, LAST_UPDATE_TIME = ?, DOC_NO = ?, LAST_UPDATE_USER = ?" +
                                    " WHERE TO_NUMBER (imsi) >= ? AND TO_NUMBER (imsi) <= ?");
                            ex = IMCommonUtils.getDBIMConnection();
                            psUpdate = ex.prepareStatement(sql.toString());
                            psUpdate.setTimestamp(1, new java.sql.Timestamp(imsiInfoDTO.getStartDate().getTime()));
                            psUpdate.setTimestamp(2, new java.sql.Timestamp(imsiInfoDTO.getEndDate().getTime()));
                            psUpdate.setTimestamp(3, new java.sql.Timestamp(DbUtil.getSysDate(em).getTime()));
                            psUpdate.setString(4, imsiInfoDTO.getDocNo());
                            psUpdate.setString(5, imsiInfoDTO.getLastUpdateUser());
                            psUpdate.setLong(6, Long.valueOf(imsiInfoDTO.getFromImsi()));
                            psUpdate.setLong(7, Long.valueOf(imsiInfoDTO.getToImsi()));
                            psUpdate.executeUpdate();
                        } catch (Exception e) {
                            baseMessage.setSuccess(false);
                            logger.error(e.getMessage(), e);
                        } finally {
                            try{psUpdate.close();} catch (Exception e) {logger.error(e.getMessage(), e);}
                            try{ex.close();} catch (Exception e) {logger.error(e.getMessage(), e);}
                        }
                    }
                }
        );


        try {
            threadUpdate.start();
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            baseMessage.setSuccess(false);
        }
        return baseMessage;
    }

    @WebMethod
    public Long countImsiRangeToCheckDelete(String fromImsi, String toImsi) throws Exception {
        return repo.countImsiRangeToCheckDelete(fromImsi, toImsi);
    }

    @WebMethod
    public BaseMessage deleteImsi(String fromImsi, String toImsi) throws Exception {
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setSuccess(true);
        Thread threadDelete = new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            StringBuilder sql = new StringBuilder("");
                            sql.append("DELETE FROM   imsi_info \n" +
                                    "      WHERE   TO_NUMBER (imsi) >= ? \n" +
                                    "         AND TO_NUMBER (imsi) <= ? \n");
                            Connection ex = IMCommonUtils.getDBIMConnection();
                            PreparedStatement psUpdate = ex.prepareStatement(sql.toString());
                            psUpdate.setLong(1, Long.valueOf(fromImsi));
                            psUpdate.setLong(2, Long.valueOf(toImsi));
                            psUpdate.executeUpdate();
                        } catch (Exception e) {
                            baseMessage.setSuccess(false);
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
        );
        try {
            threadDelete.start();
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            baseMessage.setSuccess(false);
        }
        return baseMessage;
    }

    @WebMethod
    @Override
    public boolean checkImsiInfo(String imsi) throws Exception {

        BooleanExpression predicate = QImsiInfo.imsiInfo.imsi.eq(imsi);
        List<ImsiInfo> result = Lists.newArrayList(repo.findAll(predicate));
        if(DataUtil.isNullOrEmpty(result))
            return false;
        return true;
    }

    @Override
    public List<UpdateImsiInfoDTO> getTransactionToUpdateImsi() throws Exception {
        return repo.getTransactionToUpdateImsi();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateImsiInfoToHasUsedSim(UpdateImsiInfoDTO updateImsiInfoDTOS) throws Exception {
        repo.updateImsiInfoToHasUsedSim(updateImsiInfoDTOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateImsiInfoHasConnectSim(UpdateImsiInfoDTO updateImsiInfoDTOS) throws Exception {
        repo.updateImsiInfoHasConnectSim(updateImsiInfoDTOS);
    }

    @Override
    public List<UpdateImsiInfoDTO> getImsiHasFillPartner() throws Exception {
        return repo.getImsiHasFillPartner();
    }

}
