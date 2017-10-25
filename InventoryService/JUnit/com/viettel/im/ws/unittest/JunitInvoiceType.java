package com.viettel.im.ws.unittest;

import com.viettel.bccs.inventory.dto.InvoiceTypeDTO;
import com.viettel.bccs.inventory.service.InvoiceTypeService;
import com.viettel.fw.Exception.LogicException;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by hoangnt14 on 12/11/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
public class JunitInvoiceType{

    @Autowired
    InvoiceTypeService invoiceTypeService;

    @Test
    public void testValidate_objectNull() {
        try {
            invoiceTypeService.createNewInvoiceType(null, "");
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals("Chưa nhập thông tin loại hóa đơn", desc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //require

    @Test
    public void testValidate_requireInvoiceName() {
        try {
            InvoiceTypeDTO input = new InvoiceTypeDTO();
            input.setInvoiceName(null);
            invoiceTypeService.createNewInvoiceType(input, "");
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals("Tên loại hóa đơn bắt buộc nhập", desc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testValidate_requireInvoiceType() {
        try {
            InvoiceTypeDTO input = new InvoiceTypeDTO();
            input.setInvoiceName("test");
            input.setInvoiceType(null);
            invoiceTypeService.createNewInvoiceType(input, "");
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals("Loại hóa đơn bắt buộc nhập", desc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testValidate_requireTypeInv() {
        try {
            InvoiceTypeDTO input = new InvoiceTypeDTO();
            input.setInvoiceName("test");
            input.setInvoiceType("1");
            input.setTypeInv(null);
            invoiceTypeService.createNewInvoiceType(input, "");
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals("Mẫu số bắt buộc nhập", desc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testValidate_requireInvoiceNoLength() {
        try {
            InvoiceTypeDTO input = new InvoiceTypeDTO();
            input.setInvoiceName("test");
            input.setInvoiceType("1");
            input.setTypeInv("1");
            input.setType("1");
            input.setInvoiceNoLength(null);
            invoiceTypeService.createNewInvoiceType(input, "");
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals("Độ dài hóa đơn bắt buộc nhập", desc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testValidate_requireType() {
        try {
            InvoiceTypeDTO input = new InvoiceTypeDTO();
            input.setInvoiceName("test");
            input.setInvoiceType("Test");
            input.setTypeInv("1");
            input.setType(null);
            input.setInvoiceNoLength(7L);
            invoiceTypeService.createNewInvoiceType(input, "");
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals("Hình thức hóa đơn bắt buộc nhập", desc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //maxLength
    @Test
    public void testValidate_maxLengthInvoiceName() {
        try {
            InvoiceTypeDTO input = new InvoiceTypeDTO();
            input.setInvoiceName("Tên hóa đơn quá dài dàiTên hóa đơn quá dàiTên hóa đơn quá dàiTên hóa đơn quá dàiTên hóa đơn quá dài");
            input.setInvoiceType("1");
            input.setTypeInv("1");
            input.setType("1");
            input.setInvoiceNoLength(7L);
            invoiceTypeService.createNewInvoiceType(input, "PRODUCT");
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals("Tên hóa đơn quá dài", desc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testValidate_maxLengthTypeInv() {
        try {
            InvoiceTypeDTO input = new InvoiceTypeDTO();
            input.setInvoiceName("test");
            input.setInvoiceType("1");
            input.setTypeInv("Tên hóa đơn quá dài dàiTên hóa đơn quá dàiTên hóa đơn quá dàiTên hóa đơn quá dài");
            input.setType("1");
            input.setInvoiceNoLength(7L);
            invoiceTypeService.createNewInvoiceType(input, "PRODUCT");
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals("Mẫu số quá dài", desc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testValidate_maxLengthInvoiceNoLength() {
        try {
            InvoiceTypeDTO input = new InvoiceTypeDTO();
            input.setInvoiceName("test");
            input.setInvoiceType("1");
            input.setTypeInv("1");
            input.setType("1");
            input.setInvoiceNoLength(1000000000000000000L);
            invoiceTypeService.createNewInvoiceType(input, "PRODUCT ");
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals("Mã nhân viên quá dài", desc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testValidate_maxLengthStaffCode() {
        try {
            InvoiceTypeDTO input = new InvoiceTypeDTO();
            input.setInvoiceName("test");
            input.setInvoiceType("1");
            input.setTypeInv("1");
            input.setType("1");
            input.setInvoiceNoLength(7L);
            invoiceTypeService.createNewInvoiceType(input, "Tên hóa đơn quá dài dàiTên hóa đơn quá dàiTên hóa đơn quá dàiTên hóa đơn quá dài");
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals("Độ dài hóa đơn quá dài", desc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //contains in database

    @Test
    public void testValidate_containsStaff() {
        try {
            InvoiceTypeDTO input = new InvoiceTypeDTO();
            input.setInvoiceName("test");
            input.setInvoiceType("1");
            input.setTypeInv("1");
            input.setType("1");
            input.setInvoiceNoLength(7L);
            invoiceTypeService.createNewInvoiceType(input, "HH");
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals("Mã nhân viên không có trên hệ thống", desc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testValidate_containsInvoiceType() {
        try {
            InvoiceTypeDTO input = new InvoiceTypeDTO();
            input.setInvoiceName("test");
            input.setInvoiceType("5");
            input.setTypeInv("1");
            input.setType("1");
            input.setInvoiceNoLength(7L);
            invoiceTypeService.createNewInvoiceType(input, "PRODUCT");
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals("Loại hóa đơn không có trên hệ thống", desc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testValidate_containsType() {
        try {
            InvoiceTypeDTO input = new InvoiceTypeDTO();
            input.setInvoiceName("test");
            input.setInvoiceType("1");
            input.setTypeInv("1");
            input.setType("5");
            input.setInvoiceNoLength(7L);
            invoiceTypeService.createNewInvoiceType(input, "PRODUCT");
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals("Hình thức hóa đơn không có trên hệ thống", desc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testDuplicate() {
        try {
            InvoiceTypeDTO input = new InvoiceTypeDTO();
            input.setInvoiceName("test");
            input.setInvoiceType("1");
            input.setTypeInv("1");
            input.setType("1");
            input.setInvoiceNoLength(7L);
            invoiceTypeService.createNewInvoiceType(input, "PRODUCT");
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals("Mẫu số 1 đã tồn tại", desc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
