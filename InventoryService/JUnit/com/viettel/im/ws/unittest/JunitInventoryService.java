package com.viettel.im.ws.unittest;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.RevokeMessage;
import com.viettel.bccs.inventory.dto.StockWarrantyDTO;
import com.viettel.bccs.inventory.service.InventoryService;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by thetm1 on 14/01/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
public class JunitInventoryService {

    @Autowired
    InventoryService inventoryService;


    @Before
    public void prepareData() {


    }

    @Test
    public void testValidate_revoke() {
        try {
            String telecomService = "FTTH";
            String posCodeVTN = "HTY02";
            String staffIdNo = "23119";
            List<StockWarrantyDTO> listStock = Lists.newArrayList();
            StockWarrantyDTO stockWarrantyDTO = new StockWarrantyDTO();
            stockWarrantyDTO.setProdOfferId(1137L);
            stockWarrantyDTO.setQuantity(2);
            stockWarrantyDTO.setOldSerial("8790110010000002585");
            stockWarrantyDTO.setRevokedSerial("8790110010000002585");

            String contractNo= "1434235345";
            String accountId = "7567556";
            listStock.add(stockWarrantyDTO);

            RevokeMessage message =  inventoryService.revoke(telecomService,posCodeVTN,staffIdNo,listStock,contractNo,accountId);
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

}
