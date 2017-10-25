package com.viettel.im.ws.unittest;


import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.InputDistributeByRangeDTO;
import com.viettel.bccs.inventory.service.StockNumberService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.web.common.security.CustomAuthenticationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by HaiNT41 on 1/22/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
public class JunitDistributeNumber {
    @Autowired
    StockNumberService stockNumberService;
    private RequiredRoleMap requiredRoleMap = new RequiredRoleMap();
    InputDistributeByRangeDTO input = prepareInput();
    List<Object[]> list = prepareListIsdn();

    @Test
    public void TestPreviewNullTelecomService() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            input.setTelecomServiceId(null);
            stockNumberService.previewDistriButeNumber(input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "ws.service.type.not.null");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestPreviewNullStartRange() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            input.setStartRange(null);
            stockNumberService.previewDistriButeNumber(input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "from.number.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestPreviewInvalidStartRange() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            input.setStartRange("asdf");
            stockNumberService.previewDistriButeNumber(input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "from.number.invalid.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestPreviewStartRangeOverLength() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            input.setStartRange("281000000000000");
            stockNumberService.previewDistriButeNumber(input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "mn.stock.status.isdn.from.length");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestPreviewNullEndRange() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            input.setEndRange(null);
            stockNumberService.previewDistriButeNumber(input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "to.number.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestPreviewInvalidEndRange() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            input.setEndRange("asdad");
            stockNumberService.previewDistriButeNumber(input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "to.number.invalid.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestPreviewEndRangeOverLength() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            input.setEndRange("281000000000000");
            stockNumberService.previewDistriButeNumber(input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "mn.stock.status.isdn.to.length");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestPreviewValidStartEndRangeLength() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            input.setEndRange("2810000000");
            input.setStartRange("281000001");
            stockNumberService.previewDistriButeNumber(input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "from.to.number.length.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestPreviewValidStartEndRange() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            input.setEndRange("281000000");
            input.setStartRange("281000001");
            stockNumberService.previewDistriButeNumber(input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "from.to.number.invalid.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestPreviewOverNumIsdn() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            input.setStartRange("281000000");
            input.setEndRange("283000000");
            stockNumberService.previewDistriButeNumber(input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "ws.number.per.range.over");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestPreviewValid(){
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            stockNumberService.previewDistriButeNumber(input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "from.to.number.invalid.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestListIsdnNull() {
        try {
            stockNumberService.distriButeNumber(null, null, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "distribute.must.preview");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestTelecomServiceNull() {
        try {
            input.setTelecomServiceId(null);
            stockNumberService.distriButeNumber(list, input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "ws.service.type.not.null");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestCheckPermition() {
        try {
            List<Object[]> list = prepareListIsdn();
            stockNumberService.distriButeNumber(list, input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "isdn.distribute.norole");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestCheckOverNoteLength() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            input.setNote("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1");
            stockNumberService.distriButeNumber(list, input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "stockTrans.validate.note.overLength");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestCheckNullKhoNhan() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            input.setToOwnerId(null);
            stockNumberService.distriButeNumber(list, input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "id.receive.stock.not.null");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestCheckNullLoaiKhoNhan() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            input.setToOwnerType(null);
            stockNumberService.distriButeNumber(list, input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "type.receive.stock.not.null");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestCheckNullLydo() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            input.setReasonId(null);
            stockNumberService.distriButeNumber(list, input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "reason.not.null.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void TestCapNhatKhongThanhCong() {
        try {
            requiredRoleMap.setValues(Lists.newArrayList("niceIsdnRole", "normalIsdnRole", "Distribution_role"));
            list.get(0)[3] = "0";
            list.get(0)[4] = "0";
            stockNumberService.distriButeNumber(list, input, requiredRoleMap);
        } catch (LogicException lex) {
            TestCase.assertEquals(lex.getKeyMsg(), "isdn.distribute.fail.change");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    public List<Object[]> prepareListIsdn() {
        List<Object[]> lst = Lists.newArrayList();
        Object[] ob = new Object[12];
        ob[0] = "VT";
        ob[1] = "Tổng Công ty Viễn thông Viettel";
        ob[2] = 1;
        ob[3] = "281000000";
        ob[4] = "281000000";
        ob[5] = 1;
        ob[6] = "1";
        ob[7] = 2;
        ob[8] = 7282;
        ob[9] = "Máy điện thoại V6406 Grey Blue";
        ob[10] = " ";
        ob[11] = 1;
        lst.add(ob);
        return lst;
    }

    public InputDistributeByRangeDTO prepareInput() {
        InputDistributeByRangeDTO input = new InputDistributeByRangeDTO();
        input.setTelecomServiceId(1L);
        input.setTelecomServiceName("Mobile");
        input.setStartRange("281000000");
        input.setEndRange("281000001");
        input.setToOwnerType("1");
        input.setToOwnerId(7282L);
        input.setToOwnerCode("VT");
        input.setToOwnerName("Tổng Công ty Viễn thông Viettel");
        input.setReasonId(200301L);
        input.setReasonCode("DISTRIBUTE_ISDN_NORM");
        input.setReasonName("Điều chuyển số thông thường");
        input.setFromOwnerType("1");
        input.setFromOwnerId(null);
        input.setFromOwnerCode("VT");
        input.setFromOwnerName("Tổng Công ty Viễn thông Viettel");
        input.setNote("");
        input.setUserCreate("product1");
        input.setUserIp("127.0.0.1");
        input.setUserCode("product");
        input.setUserId(9999999993L);
        input.setCurrentShopid(7282L);
        input.setCurrentShopPath("_7281_7282");
        return input;
    }
}
