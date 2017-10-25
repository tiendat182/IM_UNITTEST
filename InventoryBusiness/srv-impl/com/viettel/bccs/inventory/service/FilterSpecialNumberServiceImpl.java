package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.common.IMCommonUtils;
import com.viettel.bccs.inventory.dto.FilterSpecialNumberDTO;
import com.viettel.bccs.inventory.dto.NumberFilterRuleDTO;
import com.viettel.bccs.inventory.dto.StockNumberDTO;
import com.viettel.bccs.inventory.model.FilterSpecialNumber;
import com.viettel.bccs.inventory.repo.FilterSpecialNumberRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.dto.BaseMessage;
import com.viettel.fw.service.BaseServiceImpl;
import com.viettel.web.notify.NotifyService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class FilterSpecialNumberServiceImpl extends BaseServiceImpl implements FilterSpecialNumberService {

    private final BaseMapper<FilterSpecialNumber, FilterSpecialNumberDTO> mapper = new BaseMapper(FilterSpecialNumber.class, FilterSpecialNumberDTO.class);

    @Autowired
    private NumberFilterRuleService numberFilterRuleService;
    @Autowired
    private StockNumberService stockNumberService;
    @Autowired
    private FilterSpecialNumberRepo repository;
    @Autowired
    private OptionSetValueService optionSetValueService;
    @Autowired
    private NotifyService notifyService;
    private boolean inProcess = false;
    private boolean inProcessUpdate = false;
    public static final Logger logger = Logger.getLogger(FilterSpecialNumberService.class);

    @WebMethod
    public Long count(List<FilterRequest> filters) throws Exception {
        return repository.count(repository.toPredicate(filters));
    }

    @WebMethod
    public FilterSpecialNumberDTO findOne(Long id) throws Exception {
        return mapper.toDtoBean(repository.findOne(id));
    }

    @WebMethod
    public List<FilterSpecialNumberDTO> findAll() throws Exception {
        return mapper.toDtoBean(repository.findAll());
    }

    @WebMethod
    public List<FilterSpecialNumberDTO> findByFilter(List<FilterRequest> filters) throws Exception {

        return mapper.toDtoBean(repository.findAll(repository.toPredicate(filters)));
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage create(FilterSpecialNumberDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage update(FilterSpecialNumberDTO dto) throws Exception {
        throw new NotImplementedException();
    }

    @WebMethod
    @Transactional(rollbackFor = Exception.class)
    public BaseMessage insertBatch(List<StockNumberDTO> listNumber) throws Exception {
        BaseMessage result = new BaseMessage(true);
        try {
            Connection con = IMCommonUtils.getDBIMConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO FILTER_SPECIAL_NUMBER " +
                    "(filter_special_number_id,isdn,filter_rule_id,status,create_datetime,prod_offer_id) " +
                    "values(filter_special_number_seq.nextval,?,?,?,?,?)");
            int count = 0;
            for (StockNumberDTO stockNumber : listNumber) {
                ps.setString(1, stockNumber.getIsdn());
                ps.setLong(2, stockNumber.getFilterRuleId());
                ps.setString(3, Const.STATUS.ACTIVE);
                ps.setDate(4, new Date(optionSetValueService.getSysdateFromDB(false).getTime()));
                if (!DataUtil.isNullObject(stockNumber.getProdOfferId())) {
                    ps.setLong(5, stockNumber.getProdOfferId());
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                ps.addBatch();
                count += 1;
                if (count % 1000 == 0) {
                    try {
                        ps.executeBatch();
                    } catch (Exception e) {
                        result.setSuccess(false);
                        logger.error(e.getMessage(), e);
                    }
                }
            }
            ps.executeBatch();
            ps.close();
            con.close();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    private void truncate() throws LogicException, Exception {
        Statement stmtTrunc = null;
        Connection con = IMCommonUtils.getDBIMConnection();
        try {
            StringBuilder strTrunc = new StringBuilder();
            strTrunc.append("TRUNCATE TABLE FILTER_SPECIAL_NUMBER");
            stmtTrunc = con.createStatement();
            stmtTrunc.executeUpdate(strTrunc.toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (stmtTrunc != null) {
                stmtTrunc.close();
            }
            con.close();
        }
    }

    @Override
    public BaseMessage filterNumber(final String userCode, final boolean refilter, final Long minNumber, final List<NumberFilterRuleDTO> lstSelectedRule, final Long ownerId, final List<String> lstStatus, final Long telecomServiceId, final BigDecimal startNumberConvert, final BigDecimal endNumberConvert) throws Exception, LogicException {
        BaseMessage result = new BaseMessage(true);
        Thread t = new Thread(new Runnable() {
            boolean isSuccess = false;

            @Override
            public void run() {
                inProcess = true;
                List<NumberFilterRuleDTO> lstHightOrderRule = new ArrayList();
                try {
                    truncate();
                    if (refilter) {
                        //lay danh sach luat co do uu tien cao hon
                        lstHightOrderRule = numberFilterRuleService.searchHightOrderRule(telecomServiceId, minNumber);
                        //update rule cac so de loc lai
                        List<Long> lstFilterRuleId = new ArrayList<>();
                        for (NumberFilterRuleDTO numberFilterRuleDTO : lstSelectedRule) {
                            lstFilterRuleId.add(numberFilterRuleDTO.getFilterRuleId());
                        }
                        stockNumberService.updateRuleForRefilter(startNumberConvert, endNumberConvert, lstFilterRuleId);
                    }
                    //Lay danh sach so can loc
                    List<StockNumberDTO> listNumberToFilter = stockNumberService.getListNumberFilter(telecomServiceId, startNumberConvert, endNumberConvert, minNumber, ownerId, lstStatus);
                    List<StockNumberDTO> listResultNumber = Lists.newArrayList();
                    String numberValue;
                    Long filterRuleId;
                    Long ruleOrder;
                    List<NumberFilterRuleDTO> lstRuleFilter = Lists.newArrayList();
                    // sap  xep lai danh sach luat theo rule_order
                    Collections.sort(lstSelectedRule, new Comparator<NumberFilterRuleDTO>() {
                        public int compare(NumberFilterRuleDTO o1, NumberFilterRuleDTO o2) {
                            return Long.valueOf(o1.getRuleOrder()).compareTo(Long.valueOf(o2.getRuleOrder()));
                        }
                    });
                    if (refilter) lstRuleFilter.addAll(lstHightOrderRule);
                    else lstRuleFilter.addAll(lstSelectedRule);

                    for (StockNumberDTO stockNumber : listNumberToFilter) {
                        NumberFilterRuleDTO numberFilter = null;
                        numberValue = stockNumber.getIsdn();
                        filterRuleId = stockNumber.getFilterRuleId();
                        ruleOrder = stockNumber.getRuleOrder();

                        //kiem tra mask va condition
                        try {
                            numberFilter = IMCommonUtils.checkSpecialNumber(numberValue, lstRuleFilter);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                        if (numberFilter != null) {
                            if (filterRuleId == 0L || numberFilter.getRuleOrder() < ruleOrder) {
                                StockNumberDTO number = new StockNumberDTO();
                                number.setIsdn(numberValue);
                                number.setFilterRuleId(numberFilter.getFilterRuleId());
                                number.setProdOfferId(numberFilter.getProdOfferId());
                                listResultNumber.add(number);
                            }
                        }
                    }
                    if (listResultNumber.size() > 0) {
                        insertBatch(listResultNumber);
                        isSuccess = true;
                    }

                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    isSuccess = false;
                    notifyService.makePush(userCode, getText("mn.isdn.filter.isdn"), getText("mn.isdn.filter.isdn.err"));
                } finally {
                    inProcess = false;
                    if (isSuccess) {
                        notifyService.makePush(userCode, getText("mn.isdn.filter.isdn"), getText("mn.isdn.filter.isdn.filter.success"));
                    } else {
                        notifyService.makePush(userCode, getText("mn.isdn.filter.isdn"), getText("mn.isdn.filter.isdn.no.number"));
                    }
                }
            }
        });
        if (inProcess) {
            throw new LogicException("", "mn.isdn.number.nice.inprocess");
        } else {
            t.start();
        }
        return result;
    }

    @Override
    public List<Object[]> getResultFilter() throws LogicException, Exception {
        if (inProcess) {
            throw new LogicException("", "mn.isdn.number.nice.inprocess.no.result");
        } else {
            return repository.getResultFilter();
        }
    }

    @Override
    public List<FilterSpecialNumberDTO> getListSprecialNumberByRule(Long ruleId) throws LogicException, Exception {
//        List<FilterRequest> listRQ = Lists.newArrayList();
//        FilterRequest rq1 = new FilterRequest(FilterSpecialNumber.COLUMNS.FILTERRULEID.name(), FilterRequest.Operator.EQ,ruleId);
//        listRQ.add(rq1);
//        return mapper.toDtoBean(repository.findAll(repository.toPredicate(listRQ)));
        return repository.getListSprecialNumberByRule(ruleId);
    }

    @Override
    public BaseMessage updateFiltered(List<FilterSpecialNumberDTO> listNumberFiltered, List<FilterSpecialNumberDTO> listNumberOK, String userUpdateCode, String userIp) throws LogicException, Exception {
        return repository.updateFiltered(listNumberFiltered, listNumberOK, userUpdateCode, userIp);
    }

    @Override
    public BaseMessage updateAllFiltered(final String userUpdateCode, final String userIp, final List<Long> listFilterId) throws LogicException, Exception {
        Thread t = new Thread(new Runnable() {
            PreparedStatement stmtTrunc = null;
            Connection con = null;
            boolean isSuccess = false;

            @Override
            public void run() {
                inProcessUpdate = true;
                try {
                    Connection con = IMCommonUtils.getDBIMConnection();
                    StringBuilder updateTemp = new StringBuilder("UPDATE filter_special_number SET status='0' WHERE filter_rule_id IN (0");
                    for (int i = 0; i < listFilterId.size(); i++) {
                        updateTemp.append(",?");
                    }
                    updateTemp.append(")");
                    stmtTrunc = con.prepareStatement(updateTemp.toString());
                    for (int i = 0; i < listFilterId.size(); i++) {
                        stmtTrunc.setLong(i + 1, listFilterId.get(i));
                    }
                    stmtTrunc.executeUpdate();
                    StringBuilder updateStock = new StringBuilder();
                    updateStock.append(" UPDATE   stock_number s");
                    //").append(userUpdateCode).append("
                    //'").append(userIp).append("'
                    updateStock.append("   SET   s.last_update_user=? ,s.last_update_time=sysdate,s.last_update_ip_address=?, s.filter_rule_id =");
                    updateStock.append("             (SELECT   filter_rule_id");
                    updateStock.append("                FROM   filter_special_number f");
                    updateStock.append("               WHERE   s.isdn = f.isdn)");

                    //add Prod_Offer_Id
                    updateStock.append(", s.prod_offer_id = ");
                    updateStock.append("             (SELECT   prod_offer_id");
                    updateStock.append("                FROM   filter_special_number f");
                    updateStock.append("               WHERE   s.isdn = f.isdn)");

                    updateStock.append(" WHERE   isdn IN (SELECT   TO_NUMBER (isdn)");
                    updateStock.append("                    FROM   filter_special_number m");
                    updateStock.append("                   WHERE   filter_rule_id IN (0");
                    for (int i = 0; i < listFilterId.size(); i++) {
//                        updateStock.append(filterId);
                        updateStock.append(",?");
                    }
                    updateStock.append("))");
                    stmtTrunc = con.prepareStatement(updateStock.toString());
                    stmtTrunc.setString(1, userUpdateCode);
                    stmtTrunc.setString(2, userIp);
                    for (int i = 0; i < listFilterId.size(); i++) {
                        stmtTrunc.setLong(i + 3, listFilterId.get(i));
                    }
                    stmtTrunc.executeUpdate(updateStock.toString());
                    con.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    notifyService.makePush(userUpdateCode, getText("mn.isdn.filter.isdn.update"), getText("mn.isdn.filter.isdn.update.err"));
                } finally {
                    inProcessUpdate = false;
                    notifyService.makePush(userUpdateCode, getText("mn.isdn.filter.isdn.update"), getText("mn.isdn.filter.isdn.update.success"));
                    try {
                        stmtTrunc.close();
                        con.close();
                    } catch (SQLException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        });
        if (inProcessUpdate) {
            throw new LogicException("", "mn.isdn.number.nice.update.inprocess");
        } else {
            t.start();
        }
        BaseMessage msg = new BaseMessage();
        msg.setSuccess(true);
        return msg;
    }
}
