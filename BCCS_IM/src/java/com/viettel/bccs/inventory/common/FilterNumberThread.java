/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bccs.inventory.common;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.NumberFilterRuleDTO;
import com.viettel.bccs.inventory.dto.StockNumberDTO;
import com.viettel.bccs.inventory.service.FilterSpecialNumberService;
import com.viettel.bccs.inventory.service.NumberFilterRuleService;
import com.viettel.bccs.inventory.service.StockNumberService;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anhnv33
 */
public class FilterNumberThread extends Thread {

    public static final Logger logger = Logger.getLogger(FilterNumberThread.class);

    private NumberFilterRuleService numberFilterRuleService;
    private StockNumberService stockNumberService;
    private FilterSpecialNumberService filterSpecialNumberService;
    private boolean refilter;
    private Long minNumber;
    private List<NumberFilterRuleDTO> lstSelectedRule;
    private Long ownerId;
    private List<String> lstStatus;
    private Long telecomServiceId;
    private BigDecimal startNumberConvert;
    private BigDecimal endNumberConvert;

    public FilterNumberThread(boolean refilter, Long minNumber, List<NumberFilterRuleDTO> lstSelectedRule, Long ownerId,
            List<String> lstStatus, Long telecomServiceId, BigDecimal startNumberConvert, BigDecimal endNumberConvert,
            NumberFilterRuleService numberFilterRuleService, StockNumberService stockNumberService, FilterSpecialNumberService filterSpecialNumberService) {
        this.refilter = refilter;
        this.minNumber = minNumber;
        this.lstSelectedRule = lstSelectedRule;
        this.ownerId = ownerId;
        this.lstStatus = lstStatus;
        this.telecomServiceId = telecomServiceId;
        this.startNumberConvert = startNumberConvert;
        this.endNumberConvert = endNumberConvert;
        this.numberFilterRuleService = numberFilterRuleService;
        this.stockNumberService = stockNumberService;
        this.filterSpecialNumberService = filterSpecialNumberService;
    }

    @Override
    public void run() {
        List<NumberFilterRuleDTO> lstHightOrderRule = new ArrayList();
        try {
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
            System.out.println("Tim duoc " + listNumberToFilter.size() + " so de loc.");
            //kiem tra tung so trong dai
            List<StockNumberDTO> listNumber = Lists.newArrayList();
            String numberValue ;
            Long filterRuleId;
            Long ruleOrder ;
            List<NumberFilterRuleDTO> lstRuleFilter = Lists.newArrayList();
            if(refilter) lstRuleFilter.addAll(lstHightOrderRule);
            else lstRuleFilter.addAll(lstSelectedRule);

            System.out.println("Bat dau loc.");
            int count = 0;
            for(StockNumberDTO stockNumber : listNumberToFilter){
                NumberFilterRuleDTO numberFilter = null;
                numberValue = stockNumber.getIsdn();
                filterRuleId = stockNumber.getFilterRuleId();
                ruleOrder = stockNumber.getRuleOrder();

                //kiem tra mask va condition
                try{
                    numberFilter = IMCommonUtils.checkSpecialNumber(numberValue,lstRuleFilter);
                } catch (Exception e){
                    logger.error(e.getMessage(), e);
                }
                if(numberFilter != null){
                    if(filterRuleId == 0L || numberFilter.getRuleOrder() < ruleOrder){
                        StockNumberDTO number = new StockNumberDTO();
                        number.setIsdn(numberValue);
                        number.setFilterRuleId(numberFilter.getFilterRuleId());
                        listNumber.add(number);
                    }
                }
                count += 1;
                if(count % 1000 == 0){
                    System.out.println("Da loc " + count + " so. Co " + listNumber.size() + " so dep.");
                }
            }
            System.out.println("Tim duoc " + listNumber.size() + " so dep.");
            //Thuc hien insert batch
            if(listNumber.size() > 0) {
                filterSpecialNumberService.insertBatch(listNumber);
            }
            //ket thuc insert batch

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);

        } finally {
        }
    }

}
