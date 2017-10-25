package com.viettel.im.ws.unittest;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.dto.StockTransSerialDTO;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockNoteOrderService;
import com.viettel.bccs.inventory.service.ExecuteStockTransService;
import com.viettel.bccs.inventory.service.InvoiceTypeService;
import com.viettel.bccs.inventory.service.StockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thetm1 on 14/01/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
public class JunitExportShop {

    private StockTransDTO stockTransDTO;
    private StockTransActionDTO stockTransActionDTO;
    private RequiredRoleMap requiredRoleMap;
    private List<StockTransDetailDTO> stockTransDetailDTOs;

    @Autowired
    ExecuteStockTransService executeStockTransService;

    @Autowired
    StockTransService stockTransService;

    @Test
    public void testValidate_exportShop() {
        try {
//            BaseMessage message = executeStockTransService.executeStockTrans(Const.STOCK_TRANS.EXPORT_NOTE, Const.STOCK_TRANS_TYPE.EXPORT,
//                    stockTransDTO, stockTransActionDTO, stockTransDetailDTOs, requiredRoleMap);

            stockTransService.checkAndLockReceipt(2009083468L);
            System.out.println("test");

        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert("Chưa nhập thông tin loại hóa đơn".equals(desc));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Before
    public void prepareData() {
        stockTransDTO = new StockTransDTO();
        stockTransActionDTO = new StockTransActionDTO();
        stockTransDetailDTOs = new ArrayList<>();
        stockTransDTO.setStockTransId(148L);
        stockTransActionDTO.setActionStaffId(9999999993L);
        stockTransActionDTO.setCreateUser("product");

        List<StockTransSerialDTO> lstSerial = new ArrayList<>();
        StockTransSerialDTO stockTransSerial = new StockTransSerialDTO();

        stockTransSerial.setFromSerial("1002747");
        stockTransSerial.setToSerial("1002747");
        stockTransSerial.setQuantity(1L);
        lstSerial.add(stockTransSerial);

        StockTransDetailDTO stockTransDetail = new StockTransDetailDTO();
        stockTransDetail.setProdOfferId(5180L);
        stockTransDetail.setTableName("STOCK_HANDSET");
        stockTransDetail.setLstStockTransSerial(lstSerial);
        stockTransDetailDTOs.add(stockTransDetail);

    }
}
