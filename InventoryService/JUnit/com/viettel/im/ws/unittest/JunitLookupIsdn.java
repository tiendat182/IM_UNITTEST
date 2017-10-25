package com.viettel.im.ws.unittest;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.InfoSearchIsdnDTO;
import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DateUtil;
import com.viettel.fw.common.util.RequiredRoleMap;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by sinhhv on 2/23/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
public class JunitLookupIsdn {
    @Autowired
    StockNumberService stockNumberService;
    InfoSearchIsdnDTO infoSearch = prepareInput();
    int rownum = 100;

    @Test
    public void checkPermissionStock() {
        try {
            infoSearch.setOwnerCode("VT");
            stockNumberService.searchIsdn(infoSearch, rownum,0);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "search.isdn.permision.error");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void checkStartRange() {
        try {
            infoSearch.setStartRange("abc");
            stockNumberService.searchIsdn(infoSearch, rownum,0);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "search.isdn.start.range.invalid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void checkEndRange() {
        try {
            infoSearch.setEndRange("abc");
            stockNumberService.searchIsdn(infoSearch, rownum,0);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "search.isdn.end.range.invalid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void checkValidStartEndRange() {
        try {
            infoSearch.setStartRange("10");
            infoSearch.setEndRange("9");
            stockNumberService.searchIsdn(infoSearch, rownum,0);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "search.isdn.start.end.range.invalid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void checkOverStartEndRange() {
        try {
            infoSearch.setStartRange("1000000");
            infoSearch.setEndRange("9000000");
            stockNumberService.searchIsdn(infoSearch, rownum,0);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "search.isdn.over.range");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void validateFromPrice() {
        try {
            infoSearch.setFromPrice("abc");
            stockNumberService.searchIsdn(infoSearch, rownum,0);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "search.isdn.from.price.invalid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void validToPrice() {
        try {
            infoSearch.setToPrice("anc");
            stockNumberService.searchIsdn(infoSearch, rownum,0);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "search.isdn.to.price.invalid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void validateFromToPrice() {
        try {
            infoSearch.setFromPrice("10");
            infoSearch.setToPrice("9");
            stockNumberService.searchIsdn(infoSearch, rownum,0);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "search.isdn.from.to.price.invalid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void validateFromToDate() {
        try {
            infoSearch.setFromDate(DateUtil.convertStringToTime("01012016", "ddMMyyyy"));
            infoSearch.setToDate(DateUtil.convertStringToTime("01012014", "ddMMyyyy"));
            stockNumberService.searchIsdn(infoSearch, rownum,0);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "stock.trans.from.to.valid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void validOverDate() {
        try {
            infoSearch.setFromDate(DateUtil.convertStringToTime("01012014", "ddMMyyyy"));
            infoSearch.setToDate(DateUtil.convertStringToTime("01012016", "ddMMyyyy"));
            stockNumberService.searchIsdn(infoSearch, rownum,0);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "stock.trans.from.to.valid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void notInputFile() {
        try {
            infoSearch.setInputFile(true);
            infoSearch.setAttachFileName(null);
            stockNumberService.searchIsdn(infoSearch, rownum,0);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "mn.stock.status.isdn.update.file.noselect");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void fileNotValidRow() {
        try {
            infoSearch.setInputFile(true);
            infoSearch.setAttachFileName("abc.excel");
            infoSearch.setListIsdnReadFromFile(null);
            stockNumberService.searchIsdn(infoSearch, rownum,0);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "search.isdn.file.empty");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void fileOverRow() {
        try {
            infoSearch.setInputFile(true);
            infoSearch.setAttachFileName("abc.excel");
            List<Long> rows = Lists.newArrayList();
            for (int i = 0; i < 1001; i++) {
                rows.add(980000000L + i);
            }
            infoSearch.setListIsdnReadFromFile(rows);
            stockNumberService.searchIsdn(infoSearch, rownum,0);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "mn.stock.status.isdn.delete.maxline");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void Success() {
        try {
            stockNumberService.searchIsdn(infoSearch, rownum,0);
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    public InfoSearchIsdnDTO prepareInput() {
        infoSearch = new InfoSearchIsdnDTO();
        infoSearch.setTelecomServiceId(1L);
        infoSearch.setInputFile(false);
        infoSearch.setAttachFileName(null);
        infoSearch.setOwnerType("1");
        infoSearch.setOwnerId("105643");
        infoSearch.setOwnerCode("CN_HN2");
        infoSearch.setOwnerName("Chi nhánh 2 Hà Nội");
        infoSearch.setStartRange("281000003");
        infoSearch.setEndRange("281000005");
        infoSearch.setStatus("1");
        infoSearch.setFilterrRuleId(60000120L);
        infoSearch.setGroupFilterRuleId(10000141L);
        infoSearch.setRuleNiceIsdnId(10004628L);
        infoSearch.setFromPrice("10");
        infoSearch.setToPrice("111111111110");
        infoSearch.setProOfferId(22097L);
        infoSearch.setIsdnLike("16378%");
        infoSearch.setCheckIsdnLike(true);
        infoSearch.setRequiredRoleMap(new RequiredRoleMap());
        RequiredRoleMap roleTYpeKho = new RequiredRoleMap();
        roleTYpeKho.setValues(Lists.newArrayList("LOOK_UP_ISDN_ROLE_TYPE_KHO", "LOOK_UP_ISDN_ROLE_TYPE_KHO_1"));
        infoSearch.setRoleTypeKho(roleTYpeKho);
        return infoSearch;
    }
}
