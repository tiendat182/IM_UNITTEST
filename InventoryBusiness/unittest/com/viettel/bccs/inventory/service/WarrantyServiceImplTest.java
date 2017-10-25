package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.repo.ProductOfferTypeRepo;
import com.viettel.bccs.inventory.repo.StockTotalRepo;
import com.viettel.fw.Exception.LogicException;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.service.BaseServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author DatLT
 * @date 18/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, WarrantyServiceImpl.class, BaseServiceImpl.class, WarrantyService.class})
public class WarrantyServiceImplTest {
    @InjectMocks
    WarrantyServiceImpl warrantyService;
    @Mock
    private EntityManager em;
    @Mock
    private StockHandsetService stockHandsetService;
    @Mock
    private ProductOfferingService productOfferingService;
    @Mock
    private StaffService staffService;
    @Mock
    private ShopService shopService;
    @Mock
    private ProductOfferTypeRepo productOfferTypeRepo;
    @Mock
    private StockTotalRepo stockTotalRepo;
    @Mock
    private WarrantyServiceExecute warrantyServiceExecute;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.findStockHandSet
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindStockHandSet_1() throws Exception {
        //in:52
        Long stockHandsetId = 1L;
        Long prodOfferId = 1L;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = 1L;
        Long stateId = 1L;
        Long status = 2L;

        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_2() throws Exception {
        //out:52
        //in:55,57
        Long stockHandsetId = 1L;
        Long prodOfferId = 1L;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = 1L;
        Long stateId = 1L;
        Long status = null;

        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_3() throws Exception {
        //out:52
        Long stockHandsetId = 1L;
        Long prodOfferId = 1L;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = 1L;
        Long stateId = 1L;
        Long status = 0L;

        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_4() throws Exception {
        //in:55,57
        Long stockHandsetId = 1L;
        Long prodOfferId = 1L;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = 1L;
        Long stateId = 1L;
        Long status = 1L;
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        offeringDTO.setStatus("2");

        when(productOfferingService.findOne(anyLong())).thenReturn(offeringDTO);
        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_5() throws Exception {
        //out:57
        Long stockHandsetId = 1L;
        Long prodOfferId = 1L;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = 1L;
        Long stateId = 1L;
        Long status = 1L;
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        offeringDTO.setStatus("1");

        when(productOfferingService.findOne(anyLong())).thenReturn(offeringDTO);
        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_6() throws Exception {
        //out:55
        //in:61,62,64
        Long stockHandsetId = 1L;
        Long prodOfferId = null;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = 1L;
        Long stateId = 1L;
        Long status = 1L;

        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_7() throws Exception {
        //out:55
        //in:61,62,64
        Long stockHandsetId = 1L;
        Long prodOfferId = null;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = 1L;
        Long stateId = 1L;
        Long status = 1L;
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus("2");

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_8() throws Exception {
        //out:64
        Long stockHandsetId = 1L;
        Long prodOfferId = null;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = 1L;
        Long stateId = 1L;
        Long status = 1L;
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus("1");

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_9() throws Exception {
        //out:62
        //in:67,69
        Long stockHandsetId = 1L;
        Long prodOfferId = null;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = 2L;
        Long stateId = 1L;
        Long status = 1L;
        StaffDTO staffDTO = null;

        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_10() throws Exception {
        //out:62
        //in:67,69
        Long stockHandsetId = 1L;
        Long prodOfferId = null;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = 2L;
        Long stateId = 1L;
        Long status = 1L;
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("2");

        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_11() throws Exception {
        //out:69
        Long stockHandsetId = 1L;
        Long prodOfferId = null;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = 2L;
        Long status = 1L;
        Long stateId = 1L;
        StaffDTO staffDTO = new StaffDTO();

        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_12() throws Exception {
        //out:69
        Long stockHandsetId = 1L;
        Long prodOfferId = null;
        String serial = "2";
        Long ownerId = 1L;
        Long stateId = 1L;
        Long ownerType = 2L;
        Long status = 1L;
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");

        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_13() throws Exception {
        //out:67
        Long stockHandsetId = 1L;
        Long prodOfferId = null;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = 3L;
        Long stateId = 1L;
        Long status = 1L;

        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_14() throws Exception {
        //out:61
        Long stockHandsetId = 1L;
        Long prodOfferId = null;
        String serial = "2";
        Long ownerId = null;
        Long ownerType = 2L;
        Long stateId = 1L;
        Long status = 1L;
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");

        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_15() throws Exception {
        //out:61
        //in:75
        Long stockHandsetId = 1L;
        Long prodOfferId = null;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = null;
        Long stateId = 1L;
        Long status = 1L;
        StaffDTO staffDTO = new StaffDTO();

        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_16() throws Exception {
        //out:61,75
        Long stockHandsetId = 1L;
        Long prodOfferId = null;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = null;
        Long stateId = 1L;
        Long status = 1L;
        StockHandsetDTO stockHandsetDTO = new StockHandsetDTO();
        List<StockHandsetDTO> lsResult = Lists.newArrayList(stockHandsetDTO);

        when(stockHandsetService.findStockHandSet(anyLong(), anyLong(), anyString(), anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(lsResult);
        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    @Test
    public void testFindStockHandSet_17() throws Exception {
        //Exception
        Long stockHandsetId = 1L;
        Long prodOfferId = null;
        String serial = "2";
        Long ownerId = 1L;
        Long ownerType = null;
        Long stateId = 1L;
        Long status = 1L;

        when(stockHandsetService.findStockHandSet(anyLong(), anyLong(), anyString(), anyLong(), anyLong(), anyLong(), anyLong())).thenThrow(new Exception());
        warrantyService.findStockHandSet(stockHandsetId, prodOfferId, serial, ownerId, ownerType, stateId, status);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.findExportDateBySerial
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindExportDateBySerial_1() throws Exception {
        //99
        warrantyService.findExportDateBySerial("1","1");
    }

    @Test
    public void testFindExportDateBySerial_2() throws Exception {
        //100
        when(stockHandsetService.findExportDateBySerial(anyString(), anyString())).thenThrow(new Exception());
        warrantyService.findExportDateBySerial("1","1");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.viewStockDetail
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testViewStockDetail_1() throws Exception {
        //in 110
        Long ownerId = null;
        Long ownerType = 1L;
        Long prodOfferId = 1L;
        Long stateId = 1L;

        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_2() throws Exception {
        //in 110
        Long ownerId = 0L;
        Long ownerType = 1L;
        Long prodOfferId = 1L;
        Long stateId = 1L;

        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_3() throws Exception {
        //out 110
        //in 113
        Long ownerId = 1L;
        Long ownerType = null;
        Long stateId = 1L;
        Long prodOfferId = 1L;

        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_4() throws Exception {
        //out 110
        //in 113
        Long ownerId = 1L;
        Long ownerType = 0L;
        Long stateId = 1L;
        Long prodOfferId = 1L;

        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_5() throws Exception {
        //out 113
        //in 115,117
        Long ownerId = 1L;
        Long ownerType = 1L;
        Long prodOfferId = 1L;
        Long stateId = 1L;
//shopDTO=null
        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_6() throws Exception {
        //out 113
        //in 115,117
        Long ownerId = 1L;
        Long prodOfferId = 1L;
        Long ownerType = 1L;
        Long stateId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus("2");

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_7() throws Exception {
        //out 117
        Long ownerId = 1L;
        Long ownerType = 1L;
        Long prodOfferId = 1L;
        Long stateId = 1L;
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus("1");

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_8() throws Exception {
        //out 115
        //in 120, 122
        Long ownerId = 1L;
        Long ownerType = 2L;
        Long prodOfferId = 1L;
        Long stateId = 1L;
        StaffDTO staffDTO = null;

        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_9() throws Exception {
        //out 115
        //in 120, 122
        Long ownerId = 1L;
        Long ownerType = 2L;
        Long prodOfferId = 1L;
        Long stateId = 1L;
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("2");

        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_10() throws Exception {
        //out 122
        Long ownerId = 1L;
        Long ownerType = 2L;
        Long prodOfferId = 1L;
        Long stateId = 1L;
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStatus("1");

        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_11() throws Exception {
        //out 120
        //in 126, 128
        Long ownerId = 1L;
        Long ownerType = 3L;
        Long prodOfferId = 1L;
        Long stateId = 1L;

        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_12() throws Exception {
        //out 128
        Long ownerId = 1L;
        Long ownerType = 3L;
        Long prodOfferId = 1L;
        Long stateId = 1L;
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();

        when(productOfferingService.findOne(anyLong())).thenReturn(offeringDTO);
        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_13() throws Exception {
        //out 126
        //in133
        Long ownerId = 1L;
        Long ownerType = 3L;
        Long prodOfferId = null;
        Long stateId = 1L;
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();

        when(productOfferingService.findOne(anyLong())).thenReturn(offeringDTO);
        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_14() throws Exception {
        //out 133
        //in136
        Long ownerId = 1L;
        Long ownerType = 3L;
        Long prodOfferId = 4L;
        Long stateId = 1L;
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        List<ProductOfferTypeDTO> lsProOfferType = Lists.newArrayList(productOfferTypeDTO);

        when(productOfferingService.findOne(anyLong())).thenReturn(offeringDTO);
        when(productOfferTypeRepo.getListProdOffeTypeForWarranty(anyLong(), anyLong(), anyLong())).thenReturn(lsProOfferType);
        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_15() throws Exception {
        //out 136
        Long ownerType = 3L;
        Long ownerId = 1L;
        Long prodOfferId = null;
        Long stateId = 1L;
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        ProductOfferTypeDTO productOfferTypeDTO = new ProductOfferTypeDTO();
        List<ProductOfferTypeDTO> lsProOfferType = Lists.newArrayList(productOfferTypeDTO);

        when(productOfferTypeRepo.getListProdOffeTypeForWarranty(anyLong(), anyLong(), anyLong())).thenReturn(lsProOfferType);
        when(productOfferingService.findOne(anyLong())).thenReturn(offeringDTO);
        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    @Test
    public void testViewStockDetail_16() throws Exception {
        //Exception
        Long ownerId = 1L;
        Long ownerType = 2L;
        Long prodOfferId = 1L;
        Long stateId = 1L;

        when(staffService.findOne(anyLong())).thenThrow(new Exception());
        warrantyService.viewStockDetail(ownerId, ownerType, prodOfferId, stateId);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.exportStock
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testExportStock_1() throws Exception {
        warrantyService.exportStock(Lists.newArrayList(), 1L, 1L, 1L, 1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.impportStock
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testImpportStock_1() throws Exception {
        warrantyService.impportStock(Lists.newArrayList(), 1L, 1L, 1L, 1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.exportImportStock
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testExportImportStock_1() throws Exception {
        warrantyService.exportImportStock(Lists.newArrayList(), 1L, 1L, 1L, 1L,"1");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.exportStockWarranty
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testExportStockWarranty_1() throws Exception {
        warrantyService.exportStockWarranty(Lists.newArrayList(), 1L, 1L, 1L, 1L,1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.importStockGPON
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testImportStockGPON_1() throws Exception {
        warrantyService.importStockGPON(Lists.newArrayList(), 1L, 1L, 1L, 1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.exportImpoStockByTypeAction
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void tesTexportImpoStockByTypeAction_1() throws Exception {
        List<ImpExpStockDTO> listSerial = Lists.newArrayList();
        Long fromOwnerId  =1L;
        Long fromOwnerType=1L;
        Long toOwnerId    =1L;
        Long toOwnerType  =1L;
        Long newStateId   =1L;
        String typeAction = "ins";
        String staffCode = "404";
        String methodName = "text";

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        Whitebox.invokeMethod(warrantyService, "exportImpoStockByTypeAction", listSerial,fromOwnerId,fromOwnerType,toOwnerId,toOwnerType,newStateId,typeAction,staffCode, methodName);
    }

    @Test
    public void tesTexportImpoStockByTypeAction_2() throws Exception {
        List<ImpExpStockDTO> listSerial = Lists.newArrayList();
        Long fromOwnerId  =1L;
        Long fromOwnerType=1L;
        Long toOwnerId    =1L;
        Long toOwnerType  =1L;
        Long newStateId   =1L;
        String typeAction = "ins";
        String staffCode = "404";
        String methodName = "text";

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(warrantyServiceExecute.importExportStock(anyList(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyString(), anyString())).thenThrow(new LogicException());
        Whitebox.invokeMethod(warrantyService, "exportImpoStockByTypeAction", listSerial,fromOwnerId,fromOwnerType,toOwnerId,toOwnerType,newStateId,typeAction,staffCode, methodName);
    }

    @Test
    public void tesTexportImpoStockByTypeAction_3() throws Exception {
        List<ImpExpStockDTO> listSerial = Lists.newArrayList();
        Long fromOwnerId  =1L;
        Long fromOwnerType=1L;
        Long toOwnerId    =1L;
        Long toOwnerType  =1L;
        Long newStateId   =1L;
        String typeAction = "ins";
        String staffCode = "404";
        String methodName = "text";

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        doThrow(new Exception()).when(warrantyServiceExecute).saveLog(anyString(), anyString(), anyString(), anyString(), anyString(), any(), anyLong());
        Whitebox.invokeMethod(warrantyService, "exportImpoStockByTypeAction", listSerial,fromOwnerId,fromOwnerType,toOwnerId,toOwnerType,newStateId,typeAction,staffCode, methodName);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.getWarrantyStockLog
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void tesGetWarrantyStockLog_1() throws Exception {
        //in 250
        Long logId =null;
        String methodName="";
        String paramsInput = "";
        String responseCode = "";
        String description = "";
        Date fromDate = null;
        Date endDate = null;
        warrantyService.getWarrantyStockLog(logId, methodName,paramsInput , responseCode, description, fromDate, endDate);
    }

    @Test
    public void tesGetWarrantyStockLog_2() throws Exception {
        //out 250
        Long logId =1L;
        String methodName="";
        String paramsInput = "";
        String responseCode = "";
        String description = "";
        Date fromDate = null;
        Date endDate = null;
        warrantyService.getWarrantyStockLog(logId, methodName,paramsInput , responseCode, description, fromDate, endDate);
    }

    @Test
    public void tesGetWarrantyStockLog_3() throws Exception {
        //out 250
        //in 259
        Long logId =null;
        String methodName="";
        String paramsInput = "";
        String responseCode = "";
        String description = "";
        Date fromDate = new Date();
        Date endDate = null;
        warrantyService.getWarrantyStockLog(logId, methodName,paramsInput , responseCode, description, fromDate, endDate);
    }

    @Test
    public void tesGetWarrantyStockLog_4() throws Exception {
        //out 250
        //in259
        Long logId =null;
        String methodName="";
        String paramsInput = "";
        String responseCode = "";
        String description = "";
        Date fromDate = null;
        Date endDate = new Date();
        warrantyService.getWarrantyStockLog(logId, methodName,paramsInput , responseCode, description, fromDate, endDate);
    }

    @Test
    public void tesGetWarrantyStockLog_5() throws Exception {
        //out 250
        Long logId =null;
        String methodName="1";
        String paramsInput = "";
        String responseCode = "";
        String description = "";
        Date fromDate = null;
        Date endDate = null;
        warrantyService.getWarrantyStockLog(logId, methodName,paramsInput , responseCode, description, fromDate, endDate);
    }

    @Test
    public void tesGetWarrantyStockLog_6() throws Exception {
        //out 250
        Long logId =null;
        String methodName="";
        String paramsInput = "1";
        String responseCode = "";
        String description = "";
        Date fromDate = null;
        Date endDate = null;
        warrantyService.getWarrantyStockLog(logId, methodName,paramsInput , responseCode, description, fromDate, endDate);
    }

    @Test
    public void tesGetWarrantyStockLog_7() throws Exception {
        //out 250
        Long logId =null;
        String methodName="";
        String paramsInput = "";
        String responseCode = "1";
        String description = "";
        Date fromDate = null;
        Date endDate = null;
        warrantyService.getWarrantyStockLog(logId, methodName,paramsInput , responseCode, description, fromDate, endDate);
    }

    @Test
    public void tesGetWarrantyStockLog_8() throws Exception {
        //out 250
        Long logId =null;
        String methodName="";
        String paramsInput = "";
        String responseCode = "";
        String description = "1";
        Date fromDate = null;
        Date endDate = null;
        warrantyService.getWarrantyStockLog(logId, methodName,paramsInput , responseCode, description, fromDate, endDate);
    }

    @Test
    public void tesGetWarrantyStockLog_9() throws Exception {
        //out 250
        Long logId =null;
        String methodName="";
        String paramsInput = "";
        String responseCode = "";
        String description = "1";
        Date fromDate = new Date();
        Date endDate = new Date();
        warrantyService.getWarrantyStockLog(logId, methodName,paramsInput , responseCode, description, fromDate, endDate);
    }

    @Test
    public void tesGetWarrantyStockLog_10() throws Exception {
        //in 265
        Long logId =null;
        String methodName="";
        String paramsInput = "";
        String responseCode = "";
        String description = "1";
        Date fromDate = new Date();
        Date endDate = new Date(16, 10,10);
        warrantyService.getWarrantyStockLog(logId, methodName,paramsInput , responseCode, description, fromDate, endDate);
    }

    @Test
    public void tesGetWarrantyStockLog_11() throws Exception {
        //out 271
        String methodName="";
        Long logId =null;
        String paramsInput = "";
        String responseCode = "";
        String description = "1";
        Date  endDate= new Date();
        Date fromDate = new Date(16, 10,10);
        WarrantyStockLog warrantyStockLog = new WarrantyStockLog();
        List<WarrantyStockLog> lsStockLog = Lists.newArrayList(warrantyStockLog) ;

        when(warrantyServiceExecute.getWarrantyStockLog(anyLong(), anyString(), anyString(), anyString(), anyString(), any(), any())).thenReturn(lsStockLog);
        warrantyService.getWarrantyStockLog(logId, methodName,paramsInput , responseCode, description, fromDate, endDate);
    }

    @Test
    public void tesGetWarrantyStockLog_12() throws Exception {
        //out 271
        String methodName="";
        Long logId =null;
        String paramsInput = "";
        String responseCode = "";
        String description = "1";
        Date  endDate= new Date();
        Date fromDate = new Date(0, 0,0);
        WarrantyStockLog warrantyStockLog = new WarrantyStockLog();
        List<WarrantyStockLog> lsStockLog = Lists.newArrayList(warrantyStockLog) ;

        when(warrantyServiceExecute.getWarrantyStockLog(anyLong(), anyString(), anyString(), anyString(), anyString(), any(), any())).thenReturn(lsStockLog);
        warrantyService.getWarrantyStockLog(logId, methodName,paramsInput , responseCode, description, fromDate, endDate);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.viewStockKV
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void tesViewStockKV_1() throws Exception {
        //in 300
        String shopCode ="";
        Long prodOfferId=1L;
        Long stateId=2L;
        warrantyService.viewStockKV(shopCode, prodOfferId, stateId);
    }

    @Test
    public void tesViewStockKV_2() throws Exception {
        //out 300
        //in 304
        String shopCode ="2";
        Long prodOfferId=1L;
        Long stateId=2L;
        ShopDTO shopDTO = null;

        when(shopService.getShopByCodeAndActiveStatus(anyString())).thenReturn(shopDTO);
        warrantyService.viewStockKV(shopCode, prodOfferId, stateId);
    }

    @Test
    public void tesViewStockKV_3() throws Exception {
        //out 304
        String shopCode ="2";
        Long prodOfferId=1L;
        Long stateId=2L;
        ShopDTO shopDTO = new ShopDTO();

        when(shopService.getShopByCodeAndActiveStatus(anyString())).thenReturn(shopDTO);
        warrantyService.viewStockKV(shopCode, prodOfferId, stateId);
    }

    @Test
    public void tesViewStockKV_4() throws Exception {
        //out 304
        String shopCode ="2";
        Long prodOfferId=1L;
        Long stateId=2L;

        when(shopService.getShopByCodeAndActiveStatus(anyString())).thenThrow(new Exception());
        warrantyService.viewStockKV(shopCode, prodOfferId, stateId);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.safeToStringValue
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testSafeToStringValue_1() throws Exception {
        Whitebox.invokeMethod(warrantyService, "safeToStringValue", "?");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.safeToStringValue
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testSafeToStringValue_2() throws Exception {
        Whitebox.invokeMethod(warrantyService, "safeToStringValue", "khac");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.viewStockModelWarranty
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testViewStockModelWarranty_1() throws Exception {
        //in 329
        Long ownerId=null;
        Long ownerType=1L;
        String stockModelCode = "1";
        String stockModelName = "1";
        Long stateId=1L;

        warrantyService.viewStockModelWarranty(ownerType, ownerId, stockModelCode, stockModelName, stateId);
    }

    @Test
    public void testViewStockModelWarranty_2() throws Exception {
        //out 329
        //in 333
        Long ownerId=1L;
        Long ownerType=1L;
        String stockModelCode = "1";
        String stockModelName = "1";
        Long stateId=null;

        warrantyService.viewStockModelWarranty(ownerType, ownerId, stockModelCode, stockModelName, stateId);
    }

    @Test
    public void testViewStockModelWarranty_3() throws Exception {
        //out 333
        //in 336, 338
        Long ownerType=1L;
        Long ownerId=1L;
        String stockModelCode = "1";
        String stockModelName = "1";
        Long stateId=1L;
        ShopDTO shopDTO = null;

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        warrantyService.viewStockModelWarranty(ownerType, ownerId, stockModelCode, stockModelName, stateId);
    }

    @Test
    public void testViewStockModelWarranty_4() throws Exception {
        //out 338
        Long ownerType=1L;
        Long ownerId=1L;
        String stockModelCode = "1";
        String stockModelName = "1";
        Long stateId=1L;
        ShopDTO shopDTO = new ShopDTO();

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        warrantyService.viewStockModelWarranty(ownerType, ownerId, stockModelCode, stockModelName, stateId);
    }

    @Test
    public void testViewStockModelWarranty_5() throws Exception {
        //out 336
        //in 342, 344
        Long ownerType=2L;
        Long ownerId=1L;
        String stockModelCode = "1";
        String stockModelName = "1";
        Long stateId=1L;
        StaffDTO staffDTO = null;

        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        warrantyService.viewStockModelWarranty(ownerType, ownerId, stockModelCode, stockModelName, stateId);
    }

    @Test
    public void testViewStockModelWarranty_6() throws Exception {
        //out 344
        Long ownerType=2L;
        Long ownerId=1L;
        String stockModelCode = "1";
        String stockModelName = "1";
        Long stateId=1L;
        StaffDTO staffDTO = new StaffDTO();

        when(staffService.findOne(anyLong())).thenReturn(staffDTO);
        warrantyService.viewStockModelWarranty(ownerType, ownerId, stockModelCode, stockModelName, stateId);
    }

    @Test
    public void testViewStockModelWarranty_7() throws Exception {
        //out 342
        Long ownerType=3L;
        Long ownerId=1L;
        String stockModelCode = "1";
        String stockModelName = "1";
        Long stateId=1L;

        warrantyService.viewStockModelWarranty(ownerType, ownerId, stockModelCode, stockModelName, stateId);
    }

    @Test
    public void testViewStockModelWarranty_8() throws Exception {
        //in 349
        Long ownerType=3L;
        Long ownerId=1L;
        Long stateId=1L;
        String stockModelCode = "";
        String stockModelName = "";

        warrantyService.viewStockModelWarranty(ownerType, ownerId, stockModelCode, stockModelName, stateId);
    }

    @Test
    public void testViewStockModelWarranty_9() throws Exception {
        //out 349
        //in 355
        Long ownerType=3L;
        Long ownerId=1L;
        Long stateId=1L;
        String stockModelCode = "1";
        String stockModelName = "";

        warrantyService.viewStockModelWarranty(ownerType, ownerId, stockModelCode, stockModelName, stateId);
    }

    @Test
    public void testViewStockModelWarranty_10() throws Exception {
        //out 349 355
        Long ownerType=3L;
        Long ownerId=1L;
        Long stateId=1L;
        String stockModelCode = "1";
        String stockModelName = "1";
        StockManagementForWarranty stockManagementForWarranty = new StockManagementForWarranty();
        List<StockManagementForWarranty> lsWarranty = Lists.newArrayList(stockManagementForWarranty);

        when(warrantyServiceExecute.viewStockModelWarranty(anyLong(), anyLong(), anyString(), anyString(), anyLong())).thenReturn(lsWarranty);
        warrantyService.viewStockModelWarranty(ownerType, ownerId, stockModelCode, stockModelName, stateId);
    }


    @Test
    public void testViewStockModelWarranty_11() throws Exception {
        //exception
        Long ownerType=2L;
        Long ownerId=1L;
        String stockModelCode = "1";
        String stockModelName = "1";
        Long stateId=1L;

        when(staffService.findOne(anyLong())).thenThrow(new Exception());
        warrantyService.viewStockModelWarranty(ownerType, ownerId, stockModelCode, stockModelName, stateId);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method WarrantyServiceImpl.getInventoryInfoByListProdCode
     * -----------------------------------------------------------------------------------------------------------------
     */
    @Test
    public void testGetInventoryInfoByListProdCode_1() throws Exception {
        //in 388
        List<String> listProdOfferCode = Lists.newArrayList();
        warrantyService.getInventoryInfoByListProdCode(listProdOfferCode);
    }

    @Test
    public void testGetInventoryInfoByListProdCode_2() throws Exception {
        //out 388
        //in 401
        List<String> listProdOfferCode = Lists.newArrayList();
        listProdOfferCode.add("");
        warrantyService.getInventoryInfoByListProdCode(listProdOfferCode);
    }

    @Test
    public void testGetInventoryInfoByListProdCode_3() throws Exception {
        //out 401
        List<String> listProdOfferCode = Lists.newArrayList();
        listProdOfferCode.add("1");
        warrantyService.getInventoryInfoByListProdCode(listProdOfferCode);
    }

    @Test
    public void testGetInventoryInfoByListProdCode_4() throws Exception {
        //out 409
        //in 417
        List<String> listProdOfferCode = Lists.newArrayList();
        listProdOfferCode.add("!=");
        warrantyService.getInventoryInfoByListProdCode(listProdOfferCode);
    }

    @Test
    public void testGetInventoryInfoByListProdCode_5() throws Exception {
        //out 417
        List<String> listProdOfferCode = Lists.newArrayList();
        listProdOfferCode.add("cd");
        ProductOfferingDTO product = new ProductOfferingDTO();
        product.setName("a");
        product.setProductOfferingId(1L);

        when(productOfferingService.findByProductOfferCode(anyString(), anyString())).thenReturn(product);
        warrantyService.getInventoryInfoByListProdCode(listProdOfferCode);
    }

    @Test
    public void testGetInventoryInfoByListProdCode_6() throws Exception {
        //in 434
        List<String> listProdOfferCode = Lists.newArrayList();
        listProdOfferCode.add("cd");
        ProductOfferingDTO product = new ProductOfferingDTO();
        product.setName("a");
        product.setProductOfferingId(2L);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        List<ProductOfferingDTO> lstProduct = Lists.newArrayList(productOfferingDTO) ;

        when(productOfferingDTO.getProductOfferingId()).thenReturn(2L);
        when(productOfferingService.getInventoryInfoWarranty(anyList())).thenReturn(lstProduct);
        when(productOfferingService.findByProductOfferCode(anyString(), anyString())).thenReturn(product);
        warrantyService.getInventoryInfoByListProdCode(listProdOfferCode);
    }

    @Test
    public void testGetInventoryInfoByListProdCode_7() throws Exception {
        //out 437
        List<String> listProdOfferCode = Lists.newArrayList();
        listProdOfferCode.add("cd");
        ProductOfferingDTO product = new ProductOfferingDTO();
        product.setName("a");
        product.setProductOfferingId(1L);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        List<ProductOfferingDTO> lstProduct = Lists.newArrayList(productOfferingDTO) ;

        when(productOfferingDTO.getProductOfferingId()).thenReturn(2L);
        when(productOfferingService.getInventoryInfoWarranty(anyList())).thenReturn(lstProduct);
        when(productOfferingService.findByProductOfferCode(anyString(), anyString())).thenReturn(product);
        warrantyService.getInventoryInfoByListProdCode(listProdOfferCode);
    }

    @Test
    public void testGetInventoryInfoByListProdCode_8() throws Exception {
        //out 434
        List<String> listProdOfferCode = Lists.newArrayList();
        listProdOfferCode.add("cd");
        ProductOfferingDTO product = new ProductOfferingDTO();
        product.setName("a");
        product.setProductOfferingId(1L);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        List<ProductOfferingDTO> lstProduct = Lists.newArrayList() ;

        when(productOfferingDTO.getProductOfferingId()).thenReturn(2L);
        when(productOfferingService.getInventoryInfoWarranty(anyList())).thenReturn(lstProduct);
        when(productOfferingService.findByProductOfferCode(anyString(), anyString())).thenReturn(product);
        warrantyService.getInventoryInfoByListProdCode(listProdOfferCode);
    }

    @Test
    public void testGetInventoryInfoByListProdCode_9() throws Exception {
        //out 434
        List<String> listProdOfferCode = Lists.newArrayList();
        listProdOfferCode.add("cd");
        ProductOfferingDTO product = new ProductOfferingDTO();
        product.setName("a");
        product.setProductOfferingId(1L);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        List<ProductOfferingDTO> lstProduct = Lists.newArrayList(productOfferingDTO) ;

        when(productOfferingDTO.getProductOfferingId()).thenReturn(2L);
        when(productOfferingService.getInventoryInfoWarranty(anyList())).thenReturn(lstProduct);
        when(productOfferingService.findByProductOfferCode(anyString(), anyString())).thenReturn(product);
        warrantyService.getInventoryInfoByListProdCode(listProdOfferCode);
    }

    @Test
    public void testGetInventoryInfoByListProdCode_10() throws Exception {
        //out 434
        List<String> listProdOfferCode = Lists.newArrayList();
        listProdOfferCode.add("cd");
        ProductOfferingDTO product = new ProductOfferingDTO();
        product.setName("a");
        product.setProductOfferingId(1L);
        ProductOfferingDTO productOfferingDTO = mock(ProductOfferingDTO.class);
        List<ProductOfferingDTO> lstProduct = Lists.newArrayList(productOfferingDTO) ;

        when(productOfferingDTO.getProductOfferingId()).thenReturn(2L);
        when(productOfferingService.getInventoryInfoWarranty(anyList())).thenThrow(new Exception());
        when(productOfferingService.findByProductOfferCode(anyString(), anyString())).thenReturn(product);
        warrantyService.getInventoryInfoByListProdCode(listProdOfferCode);
    }
}