package com.viettel.im.ws.unittest;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.StockTransActionDTO;
import com.viettel.bccs.inventory.dto.StockTransDTO;
import com.viettel.bccs.inventory.dto.StockTransDetailDTO;
import com.viettel.bccs.inventory.service.ExecuteStockTransService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import com.viettel.fw.dto.BaseMessage;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by sinhhv on 2/24/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
public class JunitOrderStockIsdn {
    StockTransDTO stockTransDTO;
    StockTransActionDTO stockTransActionDTO;
    List<StockTransDetailDTO> lstStockTransDetails;
    RequiredRoleMap requiredRoleMap;
    @Autowired
    private ExecuteStockTransService executeStockTransService;

    @Test
    public void fromStockIdNull() {
        try {
            prepareInput();
            stockTransDTO.setFromOwnerId(null);
            BaseMessage message=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "transfer.isdn.from.stock.not.null");
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "transfer.isdn.from.stock.not.null");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void fromStockTypeNull() {
        try {
            prepareInput();
            stockTransDTO.setFromOwnerType(null);
            BaseMessage message=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "transfer.isdn.from.stock.not.null");
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "transfer.isdn.from.stock.not.null");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void fromOwnerIdInvalid() {
        try {
            prepareInput();
            stockTransDTO.setFromOwnerId(-10L);
            BaseMessage message=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
            TestCase.assertEquals(message.getKeyMsg(), "transfer.isdn.from.stock.invalid");
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "transfer.isdn.from.stock.invalid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void toOwnerIdNull() {
        try {
            prepareInput();
            stockTransDTO.setToOwnerId(null);
            BaseMessage msg=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "transfer.isdn.to.stock.not.null");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void toAnfFromStockSame() {
        try {
            prepareInput();
            stockTransDTO.setFromOwnerId(7282L);
            stockTransDTO.setToOwnerId(7282L);
            BaseMessage msg=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
            TestCase.assertEquals(msg.getKeyMsg(), "stockTrans.validate.fromtoStock.equal");
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "stockTrans.validate.fromtoStock.equal");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void reasonNull() {
        try {
            prepareInput();
            stockTransDTO.setReasonId(null);
            BaseMessage msg=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
            TestCase.assertEquals(msg.getKeyMsg(), "create.note.reason.export.require.msg");
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "create.note.reason.export.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void toOwnerIdInvalid() {
        try {
            prepareInput();
            stockTransDTO.setToOwnerId(-10L);
            BaseMessage msg=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
            TestCase.assertEquals(msg.getKeyMsg(), "transfer.isdn.to.stock.invalid");
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "transfer.isdn.to.stock.invalid");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void toOwnerTypeNull() {
        try {
            prepareInput();
            stockTransDTO.setToOwnerType(null);
            BaseMessage msg=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
            TestCase.assertEquals(msg.getKeyMsg(), "transfer.isdn.to.stock.not.null");
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "transfer.isdn.to.stock.not.null");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void noteOverLength() {
        try {
            prepareInput();
            stockTransDTO.setNote("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            BaseMessage msg=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
            TestCase.assertEquals(msg.getKeyMsg(), "stockTrans.validate.note.overLength");
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "stockTrans.validate.note.overLength");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void listProductNull() {
        try {
            prepareInput();
            lstStockTransDetails = null;
            BaseMessage msg=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
            TestCase.assertEquals(msg.getKeyMsg(), "stockTrans.validate.lstStockTransDetail.empty");
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "stockTrans.validate.lstStockTransDetail.empty");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void productNull() {
        try {
            prepareInput();
            lstStockTransDetails.get(0).setProdOfferId(null);
            BaseMessage msg=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
            TestCase.assertEquals(msg.getKeyMsg(), "export.order.stock.inputText.require.msg");
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "export.order.stock.inputText.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void stateIdNull() {
        try {
            prepareInput();
            lstStockTransDetails.get(0).setStateId(null);
            BaseMessage msg=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
            TestCase.assertEquals(msg.getKeyMsg(), "stockTrans.validate.state.empty");
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "stockTrans.validate.state.empty");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void quantityNull() {
        try {
            prepareInput();
            lstStockTransDetails.get(0).setQuantity(null);
            BaseMessage msg=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
            TestCase.assertEquals(msg.getKeyMsg(), "export.order.stock.number.format.msg");
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "export.order.stock.number.format.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void quantityNegative() {
        try {
            prepareInput();
            lstStockTransDetails.get(0).setQuantity(-10L);
            BaseMessage msg=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
            TestCase.assertEquals(msg.getKeyMsg(), "stockTrans.validate.quantity.negative");
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "stockTrans.validate.quantity.negative");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    @Test
    public void quantityOver() {
        try {
            prepareInput();
            lstStockTransDetails.get(0).setQuantity(2000000000L);
            BaseMessage msg=executeStockTransService.executeStockTrans(Const.STOCK_TRANS.ORDER, Const.STOCK_TRANS_TYPE.ISDN, stockTransDTO, stockTransActionDTO, lstStockTransDetails, requiredRoleMap);
            TestCase.assertEquals(msg.getKeyMsg(), "from.to.number.over.maxlength");
        } catch (LogicException msg) {
            TestCase.assertEquals(msg.getKeyMsg(), "from.to.number.over.maxlength");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    public void prepareInput() {
        //cb du lieu stocktrans
        stockTransDTO = new StockTransDTO();
        stockTransDTO.setFromOwnerId(7282L);
        stockTransDTO.setFromOwnerType(1L);
        stockTransDTO.setIsAutoGen(1L);
        stockTransDTO.setNote("xuat kho");
        stockTransDTO.setReasonId(4456L);
        stockTransDTO.setStatus("1");
        stockTransDTO.setToOwnerId(24926L);
        stockTransDTO.setToOwnerType(1L);
        stockTransDTO.setToOwnerName("CN_HNI-Chi nhánh Hà Nội");
        stockTransDTO.setLogicstic("1");
        stockTransDTO.setTransport("1");
        stockTransDTO.setStockTransType("6");

        //cb du lieu stocktransaction
        stockTransActionDTO = new StockTransActionDTO();
        stockTransActionDTO.setActionCode("ABC002");
        stockTransActionDTO.setActionStaffId(9999999993L);
        stockTransActionDTO.setCreateUser("product");
        stockTransActionDTO.setStatus("1");
        stockTransActionDTO.setIsRegionExchange(false);

        //cb du lieu list stocktransdetail
        lstStockTransDetails = Lists.newArrayList();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        stockTransDetailDTO.setProdOfferId(11992L);
        stockTransDetailDTO.setQuantity(5L);
        stockTransDetailDTO.setStateId(1L);
        stockTransDetailDTO.setProdOfferTypeId(2L);
        stockTransDetailDTO.setTableName("STOCK_NUMBER");
        stockTransDetailDTO.setProdOfferTypeName("So dep Mobile 10,500,000");
        stockTransDetailDTO.setUnit("Đồng");
        stockTransDetailDTO.setStateName("Mới");
        stockTransDetailDTO.setCheckSerial(0L);
        stockTransDetailDTO.setBasisPrice(0L);
        stockTransDetailDTO.setOwnerID(7282L);
        stockTransDetailDTO.setOwnerType(1L);
        lstStockTransDetails.add(stockTransDetailDTO);

        //cb role
        requiredRoleMap = new RequiredRoleMap();
        requiredRoleMap.setValues(Lists.newArrayList("role_stockNumShop"));
    }
}
