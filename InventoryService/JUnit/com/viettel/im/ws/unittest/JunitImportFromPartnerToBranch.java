package com.viettel.im.ws.unittest;

import com.viettel.bccs.inventory.dto.ImportPartnerDetailDTO;
import com.viettel.bccs.inventory.dto.ImportPartnerRequestDTO;
import com.viettel.bccs.inventory.service.BaseStock.BaseStockImportFromPartnerToBranchService;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.RequiredRoleMap;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HaiNT41 on 2/22/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
public class JunitImportFromPartnerToBranch {
    @Autowired
    private BaseStockImportFromPartnerToBranchService baseStockImportFromPartnerToBranchService;

    private ImportPartnerRequestDTO importPartnerRequestDTO;
    private RequiredRoleMap requiredRoleMap;

    @Test
    public void testValidate_OK() {
        try {
            initImportPartnerRequestDTO();
            baseStockImportFromPartnerToBranchService.executeStockTrans(importPartnerRequestDTO, requiredRoleMap);
        } catch (LogicException lex) {
            String desc = lex.getDescription();
            TestCase.assertEquals(lex.getKeyMsg(), "export.order.stock.require.msg");
        } catch (Exception ex) {
            TestCase.assertEquals("Loi he thong", ex.getMessage());
        }
    }

    private void initImportPartnerRequestDTO() {

        importPartnerRequestDTO = new ImportPartnerRequestDTO();
        importPartnerRequestDTO.setImportPartnerRequestId(126L);
        importPartnerRequestDTO.setActionCode("PN_127");
        importPartnerRequestDTO.setApproveStaffId(998877L);
        importPartnerRequestDTO.setContractCode("HD_02");
        importPartnerRequestDTO.setRequestCode("ABC");
        importPartnerRequestDTO.setPartnerId(123L);
        importPartnerRequestDTO.setToOwnerId(24926L);
//        importPartnerRequestDTO.setContractDate();
//        importPartnerRequestDTO.setContractImportDate();
//        importPartnerRequestDTO.setCreateDate();
        importPartnerRequestDTO.setCreateShopId(24926L);
        importPartnerRequestDTO.setCreateStaffId(998877L);
        importPartnerRequestDTO.setImpType(1L);
        importPartnerRequestDTO.setImportStaffCode("HAINT41");
        importPartnerRequestDTO.setImportStaffId(998877L);
        List<ImportPartnerDetailDTO> lstImportPartnerDetailDTO = new ArrayList();
        lstImportPartnerDetailDTO.add(initImportPartnerDetailDTO());
        List<String> lstProfile = new ArrayList();
        lstProfile.add("SERIAL");
        importPartnerRequestDTO.setListProfile(lstProfile);
        importPartnerRequestDTO.setListImportPartnerDetailDTOs(lstImportPartnerDetailDTO);

    }

    private ImportPartnerDetailDTO initImportPartnerDetailDTO () {
        ImportPartnerDetailDTO importPartnerDetailDTO = new ImportPartnerDetailDTO();
        importPartnerDetailDTO.setImportPartnerDetailId(126L);
        importPartnerDetailDTO.setProdOfferId(1050L);
        importPartnerDetailDTO.setProdOfferCode("DT7");
        importPartnerDetailDTO.setQuantity(10L);
        importPartnerDetailDTO.setStateId(1L);
        List<String> lstParam = new ArrayList();
//        lstParam.add("123312");
        lstParam.add("124512");
        lstParam.add("124612");
        lstParam.add("124712");
        importPartnerDetailDTO.setLstParam(lstParam);
        return importPartnerDetailDTO;
    }


}
