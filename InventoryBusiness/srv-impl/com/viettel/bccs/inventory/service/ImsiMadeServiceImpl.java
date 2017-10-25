package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.mysema.query.types.expr.BooleanExpression;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.ImsiMadeDTO;
import com.viettel.bccs.inventory.dto.OutputImsiProduceDTO;
import com.viettel.bccs.inventory.model.ImsiMade;
import com.viettel.bccs.inventory.model.Partner;
import com.viettel.bccs.inventory.model.QImsiInfo;
import com.viettel.bccs.inventory.model.QImsiMade;
import com.viettel.bccs.inventory.repo.ImsiMadeRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.web.notify.NotifyService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.viettel.bccs.inventory.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
@Service
public class ImsiMadeServiceImpl extends BaseServiceImpl implements ImsiMadeService {

    private final BaseMapper<ImsiMade,ImsiMadeDTO> mapper = new BaseMapper<>(ImsiMade.class,ImsiMadeDTO.class);
    private final BaseMapper<Partner,PartnerDTO> partnerMapper = new BaseMapper<>(Partner.class,PartnerDTO.class);
    @PersistenceContext(unitName = "BCCS_IM")
    private EntityManager em;
    @Autowired
    private ImsiMadeRepo repo;
    @Autowired
    private ProductOfferingService productOfferingService;
    @Autowired
    private PartnerService partnerService;
    public static final Logger logger = Logger.getLogger(ImsiMadeService.class);
    @Autowired
    private NotifyService notifyService;


    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repo.count(repo.toPredicate(filters));
    }

    @WebMethod
    public ImsiMadeDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repo.findOne(id));
    }

    @WebMethod
    public List<ImsiMadeDTO> findByFilter(List<FilterRequest> filters) throws Exception {
        return mapper.toDtoBean(repo.findAll(repo.toPredicate(filters)));
    }

    @WebMethod
    @Override
    public List<OutputImsiProduceDTO> getListImsiRange(String startImsi, String endImsi, String docCode, Date fromDate, Date toDate, Long status) throws Exception {
        return repo.getListImsiRange(startImsi, endImsi, docCode, fromDate, toDate, status);
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doCreate(ImsiMadeDTO imsiMadeDTO, String username) throws LogicException, Exception {

        //validate IMSI range again

        //validate product offering
        ProductOfferingDTO productOfferingDTO = productOfferingService.findOne(imsiMadeDTO.getProdOfferId());
        if(productOfferingDTO == null || productOfferingDTO.getProductOfferTypeId() == null || productOfferingDTO.getStatus() == null || !"1".equals(productOfferingDTO.getStatus())) {
            throw new LogicException("105", "balance.valid.prodInfo");
        }
        Date sysdate = getSysDate(em);
        //repo.updateImsiInfoTo2Status(imsiMadeDTO.getFromImsi(), imsiMadeDTO.getToImsi());
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    Connection ex = IMCommonUtils.getDBIMConnection();
                    PreparedStatement psUpdate = ex.prepareStatement("UPDATE IMSI_INFO SET STATUS=?, LAST_UPDATE_TIME=?, LAST_UPDATE_USER=? WHERE IMSI=?");
                    int count = 0;
                    boolean error = false;
                    for(Long imsi = Long.valueOf(imsiMadeDTO.getFromImsi()); imsi <= Long.valueOf(imsiMadeDTO.getToImsi()); imsi++) {
                        psUpdate.setString(1, "2");
                        psUpdate.setTimestamp(2, new java.sql.Timestamp(sysdate.getTime()));
                        psUpdate.setString(3, username);
                        psUpdate.setString(4, String.valueOf(imsi));

                        psUpdate.addBatch();
                        count++;
                        if(count % 1000 == 0) {
                            try {
                                psUpdate.executeBatch();
                            } catch (Exception e) {
                                error = true;
                                ImsiMadeServiceImpl.logger.error(e.getMessage(), e);
                            }
                        }
                    }

                    try {
                        psUpdate.executeBatch();
                    } catch (Exception e) {
                        error = true;
                        ImsiMadeServiceImpl.logger.error(e.getMessage(), e);
                    }

                    try {
                        if(!error) {
                            ImsiMadeServiceImpl.this.notifyService.makePush(username, ImsiMadeServiceImpl.this.getText("mn.imsi.create"), ImsiMadeServiceImpl.this.getText("create.imsi.ranges.success"));
                        } else {
                            ImsiMadeServiceImpl.this.notifyService.makePush(username, ImsiMadeServiceImpl.this.getText("mn.imsi.create"), ImsiMadeServiceImpl.this.getText("create.imsi.ranges.fail"));
                        }
                    } catch (Exception var10) {
                        ImsiMadeServiceImpl.logger.error(var10.getMessage(), var10);
                    }

                    psUpdate.close();
                    ex.close();
                } catch (Exception e) {
                    ImsiMadeServiceImpl.logger.error(e.getMessage(), e);
                }

            }
        });

        try {
            t1.start();
        } catch (Exception e) {
            try {
                this.notifyService.makePush(username, this.getText("mn.imsi.create"), this.getText("create.imsi.ranges.fail"));
            } catch (Exception var12) {
                logger.error(e.getMessage(), e);
            }
            logger.error(e.getMessage(), e);
        }

        imsiMadeDTO.setUserCreate(username);
        imsiMadeDTO.setCreateDate(getSysDate(em));
        repo.saveAndFlush(mapper.toPersistenceBean(imsiMadeDTO));
    }

    @WebMethod
    @Override
    public List<ImsiMadeDTO> getImsiRangeByDate(Date fromDate, Date toDate) throws Exception {
        return repo.getImsiRangeByDate(fromDate, toDate);
    }

    @Override
    public Long checkImsiRange(String fromImsi, String toImsi) throws Exception {
        return repo.checkImsiRange(fromImsi, toImsi);
    }

    @Override
    public List<PartnerDTO> getListPartnerA4keyNotNull() throws Exception {
        return partnerMapper.toDtoBean(repo.getListPartnerA4keyNotNull());
    }

    @Override
    public boolean validateImsiRange(String fromImsi, String toImsi) throws Exception {
        return repo.validateImsiRange(fromImsi, toImsi);
    }

    @Override
    public Long getQuantityByImsi(String fromImsi, String toImsi) throws Exception {
        return repo.getQuantityByImsi(fromImsi, toImsi);
    }

}
