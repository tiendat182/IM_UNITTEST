package com.viettel.im.ws.unittest;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.service.*;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockNoteOrderService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DataUtil;
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
public class JunitViewInforWarehouse {

    private StockTransDTO stockTransDTO;
    private String ownerType;
    private String ownerId;
    private String productOfferType;
    private String prodOfferId;
    private String stateId;
    private WareHouseInfoDTO infoDTO = new WareHouseInfoDTO();
    private List<StockTransSerialDTO> lsSerial = Lists.newArrayList();

    @Autowired
    ProductOfferingService productOfferingService;

    @Autowired
    StockTransService stockTransService;
    @Autowired
    StockTransSerialService stockTransSerialService;

    @Test
    public void testSearch() {
        try {
            //lay danh sach productOfferType
            List<ProductOfferingDTO> productOfferingList = productOfferingService.getListProductOfferTypeViewer(ownerType, ownerId, DataUtil.safeToLong(prodOfferId), null);

            System.out.println("test:" + productOfferingList.size());
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert ("Loại kho không được trống".equals(desc));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testSearchOffering() {
        try {
            //lay danh sach productOfferType
            List<ProductOfferingTotalDTO> productOfferingList = productOfferingService.getListProductOfferingViewer(ownerType, ownerId, DataUtil.safeToLong(prodOfferId));

            System.out.println("test:" + productOfferingList.size());
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert ("Loại kho không được trống".equals(desc));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testViewDetail() {
        try {
            lsSerial = stockTransSerialService.findStockTransSerialByProductOfferType(infoDTO);

            System.out.println("test:" + lsSerial.size());
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            assert ("Hàng hóa chưa được chọn".equals(desc));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Before
    public void prepareData() {
        ownerType = "1";
        ownerId = "7282";
        productOfferType = "7";
        prodOfferId = "5180";
        stateId = "1";

    }
}
