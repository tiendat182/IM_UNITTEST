package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.StockModelIm1DTO;
import com.viettel.bccs.im1.dto.StockTotalIm1DTO;
import com.viettel.bccs.im1.dto.StockTypeIm1DTO;
import com.viettel.bccs.im1.service.StockTotalAuditIm1Service;
import com.viettel.bccs.im1.service.StockTotalIm1Service;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.*;
import com.viettel.bccs.inventory.repo.OptionSetValueRepo;
import com.viettel.bccs.inventory.repo.ProductOfferingRepo;
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
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 15/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, StockTransMaterialServiceImpl.class, BaseServiceImpl.class, StockTransMaterialService.class, ShopDTO.class})
public class StockTransMaterialServiceImplTest {
    @InjectMocks
    StockTransMaterialServiceImpl stockTransMaterialService;
    @Mock
    private EntityManager em;
    @Mock
    private EntityManager emLib;
    @Mock
    private OptionSetValueRepo optionSetValueRepo;
    @Mock
    private StockTransService stockTransService;
    @Mock
    private StockTransActionService stockTransActionService;
    @Mock
    private StockTransDetailService stockTransDetailService;
    @Mock
    private StockTransSerialService stockTransSerialService;
    @Mock
    private StockTotalService stockTotalService;
    @Mock
    private StockTotalIm1Service stockTotalIm1Service;
    @Mock
    private ProductOfferingService productOfferingService;
    @Mock
    private OptionSetValueService optionSetValueService;
    @Mock
    private StockTotalAuditService stockTotalAuditService;
    @Mock
    private StaffService staffService;
    @Mock
    private ShopService shopService;
    @Mock
    private StockTotalAuditIm1Service stockTotalAuditIm1Service;
    @Mock
    private ReasonService reasonService;
    @Mock
    private StockTransDocumentService stockTransDocumentService;
    @Mock
    private ProductOfferingRepo productOfferingRepo;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransMaterialServiceImpl.excuteCreateTransMaterial
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_1() throws Exception {
        VStockTransDTO vStockTransDTO = null;
        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_2() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_3() throws Exception {
        //88
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList();
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFromOwnerID(null);

        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_4() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFromOwnerID(1L);
        vStockTransDTO.setFromOwnerType(1L);
        vStockTransDTO.setFileAttach(null);

        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_5() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFromOwnerID(1L);
        vStockTransDTO.setFromOwnerType(1L);
        vStockTransDTO.setFileAttach(new byte[0]);

        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_6() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList();
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFromOwnerType(1L);

        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_7() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList();
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFromOwnerID(1L);

        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_8() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFileAttach(new byte[0]);

        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_9() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFileAttach(new byte[1]);
        vStockTransDTO.setFromOwnerType(1L);
        vStockTransDTO.setFromOwnerID(1L);

        when(shopService.findOne(anyLong())).thenReturn(null);
        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_10() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFileAttach(new byte[1]);
        vStockTransDTO.setFromOwnerType(1L);
        vStockTransDTO.setFromOwnerID(1L);
        ShopDTO shopDTO = mock(ShopDTO.class);

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getStatus()).thenReturn("1");
        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_11() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFileAttach(new byte[1]);
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setFromOwnerID(1L);
        vStockTransDTO.setStockBase("basic");
        ShopDTO shopDTO = mock(ShopDTO.class);
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getStatus()).thenReturn("1");
        when( stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn("1");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_12() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFileAttach(new byte[1]);
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setFromOwnerID(1L);
        vStockTransDTO.setStockBase("basic");

        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);

        ShopDTO shopDTO = mock(ShopDTO.class);
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getStatus()).thenReturn("1");
        when( stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn("1");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_13() throws Exception {
        //88
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFromOwnerID(1L);

        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_14() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList();
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFromOwnerType(2L);

        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_15() throws Exception {
        //98
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFileAttach(new byte[1]);
        vStockTransDTO.setFromOwnerType(1L);
        vStockTransDTO.setFromOwnerID(1L);
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setStatus("2");

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_16() throws Exception {
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFileAttach(new byte[1]);
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setFromOwnerID(1L);
        vStockTransDTO.setStockBase("basic");
        ShopDTO shopDTO = mock(ShopDTO.class);
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getStatus()).thenReturn("1");
        when( stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn("1");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_17() throws Exception {
        //out-149
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFileAttach(new byte[1]);
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setFromOwnerID(1L);
        vStockTransDTO.setStockBase("basic");
        ShopDTO shopDTO = mock(ShopDTO.class);
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getStatus()).thenReturn("1");
        when( stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn(null);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_18() throws Exception {
        //out-170
        //in-175
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFileAttach(new byte[1]);
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setFromOwnerID(1L);
        vStockTransDTO.setStockBase("basic");
        ShopDTO shopDTO = mock(ShopDTO.class);
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getStatus()).thenReturn("1");
        when( stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn(null);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_19() throws Exception {
        //out-170
        //in-175
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFileAttach(new byte[1]);
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setFromOwnerID(1L);
        vStockTransDTO.setStockBase("basic");
        ShopDTO shopDTO = mock(ShopDTO.class);
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId(1L);
        productOfferingDTO.setStatus("1");

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getStatus()).thenReturn("1");
        when( stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn(null);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_20() throws Exception {
        //out-170
        //in-175
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFileAttach(new byte[1]);
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setFromOwnerID(1L);
        vStockTransDTO.setStockBase("basic");
        ShopDTO shopDTO = mock(ShopDTO.class);
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId(null);

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getStatus()).thenReturn("1");
        when( stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn(null);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( any( EntityManager.class ) ) ).thenReturn( new Date() );
        stockTransMaterialService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransMaterial_21() throws Exception {
        //out-170
        //in-175
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFileAttach(new byte[1]);
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setFromOwnerID(1L);
        vStockTransDTO.setStockBase("basic");
        ShopDTO shopDTO = mock(ShopDTO.class);
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId(1L);
        productOfferingDTO.setStatus("2");

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getStatus()).thenReturn("1");
        when( stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn(null);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.excuteCreateTransMaterial(vStockTransDTO);
    }

    @Test
    public void testExcuteCreateTransMaterial_22() throws Exception {
        //out-175
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        VStockTransDTO vStockTransDTO = new VStockTransDTO();
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> transDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        vStockTransDTO.setTransDetailDTOs(transDetailDTOs);
        vStockTransDTO.setFileAttach(new byte[1]);
        vStockTransDTO.setFromOwnerType(2L);
        vStockTransDTO.setFromOwnerID(1L);
        vStockTransDTO.setStockBase("basic");
        ShopDTO shopDTO = mock(ShopDTO.class);
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId(1L);
        productOfferingDTO.setStatus("1");

        ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
        List<ProductOfferingTotalDTO> lsTotal = Lists.newArrayList(productOfferingTotalDTO);
        when(productOfferingRepo.getLsRequestProductByShop(any(), any(), any(), any(), any())).thenReturn(lsTotal);

        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotalDTO> lstStockTotal = Lists.newArrayList(stockTotalDTO);
        when(stockTotalService.searchStockTotal(any())).thenReturn(lstStockTotal);
//        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
//        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        Query queryObject = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.executeUpdate()).thenReturn(1);

        when(shopService.findOne(anyLong())).thenReturn(shopDTO);
        when(shopDTO.getStatus()).thenReturn("1");
        when( stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn(null);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);
        when(productOfferingService.findOne(anyLong())).thenReturn(productOfferingDTO);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.excuteCreateTransMaterial(vStockTransDTO);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransMaterialServiceImpl.validateStockTotal
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testValidateStockTotal_1() throws  Exception {
        //in-204
        String staffCode = "404";
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList();
        stockTransMaterialService.validateStockTotal(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testValidateStockTotal_2() throws  Exception {
        //in-210
        //out-204
        String staffCode = "404";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();

        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(stockTransDetailDTO.getQuantity()).thenReturn(null);
        stockTransMaterialService.validateStockTotal(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testValidateStockTotal_3() throws  Exception {
        //in-210
        //out-204
        String staffCode = "404";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();

        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(stockTransDetailDTO.getQuantity()).thenReturn(-1L);
        stockTransMaterialService.validateStockTotal(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testValidateStockTotal_4() throws  Exception {
        //out-210
        //in-215:productOfferingDTO == null
        String staffCode = "404";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();

        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);
        when(productOfferingService.getProdOfferDtoByCodeAndStockNew(stockTransDetailDTO.getProdOfferCode(), staffDTO.getStaffId(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransDetailDTO.getStateId())).thenReturn(null);
        stockTransMaterialService.validateStockTotal(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testValidateStockTotal_5() throws  Exception {
        //out-210
        //in-215: productOfferingDTO.getProductOfferTypeId() == null
        String staffCode = "404";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId(null);

        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);
        when(productOfferingService.getProdOfferDtoByCodeAndStockNew(stockTransDetailDTO.getProdOfferCode(), staffDTO.getStaffId(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransDetailDTO.getStateId())).thenReturn(productOfferingDTO);
        stockTransMaterialService.validateStockTotal(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testValidateStockTotal_6() throws  Exception {
        //out-210
        //in-215: productOfferingDTO.getStatus() == null
        String staffCode = "404";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId(1L);
        productOfferingDTO.setStatus(null);

        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);
        when(productOfferingService.getProdOfferDtoByCodeAndStockNew(stockTransDetailDTO.getProdOfferCode(), staffDTO.getStaffId(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransDetailDTO.getStateId())).thenReturn(productOfferingDTO);
        stockTransMaterialService.validateStockTotal(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testValidateStockTotal_7() throws  Exception {
        //out-210
        //in-215: productOfferingDTO.getStatus() == null
        String staffCode = "404";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId(1L);
        productOfferingDTO.setStatus("2");

        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);
        when(productOfferingService.getProdOfferDtoByCodeAndStockNew(stockTransDetailDTO.getProdOfferCode(), staffDTO.getStaffId(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransDetailDTO.getStateId())).thenReturn(productOfferingDTO);
        stockTransMaterialService.validateStockTotal(staffCode, lsTransDetailDTOs);
    }
    @Test(expected = Exception.class)
    public void testValidateStockTotal_8() throws  Exception {
        //out-215
        String staffCode = "404";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId(1L);
        productOfferingDTO.setStatus("1");

        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(stockTransDetailDTO.getQuantity()).thenReturn(1L);
        when(productOfferingService.getProdOfferDtoByCodeAndStockNew(stockTransDetailDTO.getProdOfferCode(), staffDTO.getStaffId(),
                Const.OWNER_TYPE.STAFF_LONG, stockTransDetailDTO.getStateId())).thenReturn(productOfferingDTO);
        stockTransMaterialService.validateStockTotal(staffCode, lsTransDetailDTOs);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransMaterialServiceImpl.excuteCreateTransGift
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testExcuteCreateTransGift_1() throws  Exception {
        //in-228
        String staffCode = "468";
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList();
        stockTransMaterialService.excuteCreateTransGift(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransGift_2() throws  Exception {
        //out-228
        //in-231
        String staffCode = "";
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        stockTransMaterialService.excuteCreateTransGift(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransGift_3() throws  Exception {
        //out-231
        //in-237
        String staffCode = "468";
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = null;

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        stockTransMaterialService.excuteCreateTransGift(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransGift_4() throws  Exception {
        //out-237
        //in-253: reasonDTO!=null
        String staffCode = "468";
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        ReasonDTO reasonDTO = new ReasonDTO();
        reasonDTO.setReasonId(2L);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(reasonService.getReason(Const.REASON_CODE.EXP_MATERIAL_GIFT, Const.REASON_CODE.EXP_MATERIAL_GIFT)).thenReturn(reasonDTO);
        stockTransMaterialService.excuteCreateTransGift(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransGift_5() throws  Exception {
        //out-237
        //in-253: reasonDTO!=null
        //in-279, in_1: 300
        String staffCode = "468";
        StockTransDetailDTO stockTransDetailDTO = new StockTransDetailDTO();
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        ReasonDTO reasonDTO = null;
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList(optionSetValueDTO);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(reasonService.getReason(Const.REASON_CODE.EXP_MATERIAL_GIFT, Const.REASON_CODE.EXP_MATERIAL_GIFT)).thenReturn(reasonDTO);
        when(stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn("1");
        stockTransMaterialService.excuteCreateTransGift(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransGift_6() throws  Exception {
        //out-279
        //in_2-300
        String staffCode = "468";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        ReasonDTO reasonDTO = null;
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(reasonService.getReason(Const.REASON_CODE.EXP_MATERIAL_GIFT, Const.REASON_CODE.EXP_MATERIAL_GIFT)).thenReturn(reasonDTO);
        when(stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn("1");
        when(stockTransDetailDTO.getQuantity()).thenReturn(-1L);
        stockTransMaterialService.excuteCreateTransGift(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransGift_7() throws  Exception {
        //out-300
        //in_1:305
        String staffCode = "468";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        ReasonDTO reasonDTO = null;
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        //case productOfferingDTO=null
        ProductOfferingDTO productOfferingDTO = null;

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(reasonService.getReason(Const.REASON_CODE.EXP_MATERIAL_GIFT, Const.REASON_CODE.EXP_MATERIAL_GIFT)).thenReturn(reasonDTO);
        when(stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn("1");
        when(stockTransDetailDTO.getQuantity()).thenReturn(2L);
        when(productOfferingService.getProdOfferDtoByCodeAndStockNew(anyString(), anyLong(),anyLong(), anyLong())).thenReturn(productOfferingDTO);
        stockTransMaterialService.excuteCreateTransGift(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransGift_8() throws  Exception {
        //out-300
        //in_2:305
        String staffCode = "468";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        ReasonDTO reasonDTO = null;
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        //case productOfferingDTO.getProductOfferTypeId()=null
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId(null);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(reasonService.getReason(Const.REASON_CODE.EXP_MATERIAL_GIFT, Const.REASON_CODE.EXP_MATERIAL_GIFT)).thenReturn(reasonDTO);
        when(stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn("1");
        when(stockTransDetailDTO.getQuantity()).thenReturn(2L);
        when(productOfferingService.getProdOfferDtoByCodeAndStockNew(anyString(), anyLong(),anyLong(), anyLong())).thenReturn(productOfferingDTO);
        stockTransMaterialService.excuteCreateTransGift(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransGift_9() throws  Exception {
        //out-300
        //in_3:305
        String staffCode = "468";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        ReasonDTO reasonDTO = null;
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        //case productOfferingDTO.getStatus() == null
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId(1L);
        productOfferingDTO.setStatus(null);

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(reasonService.getReason(Const.REASON_CODE.EXP_MATERIAL_GIFT, Const.REASON_CODE.EXP_MATERIAL_GIFT)).thenReturn(reasonDTO);
        when(stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn("1");
        when(stockTransDetailDTO.getQuantity()).thenReturn(2L);
        when(productOfferingService.getProdOfferDtoByCodeAndStockNew(anyString(), anyLong(),anyLong(), anyLong())).thenReturn(productOfferingDTO);
        stockTransMaterialService.excuteCreateTransGift(staffCode, lsTransDetailDTOs);
    }

    @Test(expected = Exception.class)
    public void testExcuteCreateTransGift_10() throws  Exception {
        //out-300
        //in_4:305
        String staffCode = "468";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        ReasonDTO reasonDTO = null;
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        //case productOfferingDTO.getStatus() != "1"
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId(1L);
        productOfferingDTO.setStatus("2");

        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(reasonService.getReason(Const.REASON_CODE.EXP_MATERIAL_GIFT, Const.REASON_CODE.EXP_MATERIAL_GIFT)).thenReturn(reasonDTO);
        when(stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn("1");
        when(stockTransDetailDTO.getQuantity()).thenReturn(2L);
        when(productOfferingService.getProdOfferDtoByCodeAndStockNew(anyString(), anyLong(),anyLong(), anyLong())).thenReturn(productOfferingDTO);
        stockTransMaterialService.excuteCreateTransGift(staffCode, lsTransDetailDTOs);
    }

    @Test
    public void testExcuteCreateTransGift_11() throws  Exception {
        //out-305
        String staffCode = "468";
        StockTransDetailDTO stockTransDetailDTO = mock(StockTransDetailDTO.class);
        List<StockTransDetailDTO> lsTransDetailDTOs = Lists.newArrayList(stockTransDetailDTO);
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffId(1L);
        ReasonDTO reasonDTO = null;
        StockTransDTO stockTransNew = new StockTransDTO();
        OptionSetValueDTO optionSetValueDTO = mock(OptionSetValueDTO.class);
        List<OptionSetValueDTO> lstConfigEnableBccs1 = Lists.newArrayList();
        //305
        ProductOfferingDTO productOfferingDTO = new ProductOfferingDTO();
        productOfferingDTO.setProductOfferTypeId(1L);
        productOfferingDTO.setStatus("1");


        ProductOfferingTotalDTO productOfferingTotalDTO = new ProductOfferingTotalDTO();
        List<ProductOfferingTotalDTO> lsTotal = Lists.newArrayList(productOfferingTotalDTO);
        when(productOfferingRepo.getLsRequestProductByShop(any(), any(), any(), any(), any())).thenReturn(lsTotal);

        StockTotalDTO stockTotalDTO = new StockTotalDTO();
        List<StockTotalDTO> lstStockTotal = Lists.newArrayList(stockTotalDTO);
        when(stockTotalService.searchStockTotal(any())).thenReturn(lstStockTotal);
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        Query queryObject = mock(Query.class);
        when(em.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.executeUpdate()).thenReturn(1);


        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        when(staffService.getStaffByStaffCode(anyString())).thenReturn(staffDTO);
        when(reasonService.getReason(Const.REASON_CODE.EXP_MATERIAL_GIFT, Const.REASON_CODE.EXP_MATERIAL_GIFT)).thenReturn(reasonDTO);
        when(stockTransService.save(any())).thenReturn(stockTransNew);
        when(optionSetValueService.getByOptionSetCode("ENABLE_UPDATE_BCCS1")).thenReturn(lstConfigEnableBccs1);
        when(optionSetValueDTO.getValue()).thenReturn("1");
        when(stockTransDetailDTO.getQuantity()).thenReturn(2L);
        when(productOfferingService.getProdOfferDtoByCodeAndStockNew(anyString(), anyLong(),anyLong(), anyLong())).thenReturn(productOfferingDTO);
        stockTransMaterialService.excuteCreateTransGift(staffCode, lsTransDetailDTOs);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransMaterialServiceImpl.getAvailableQuantity
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testGetAvailableQuantity_1() throws  Exception {
        //in-622
        stockTransMaterialService.getAvailableQuantity(1L, 1L, 1L, 1L);
    }

    @Test(expected = Exception.class)
    public void testGetAvailableQuantity_2() throws  Exception {
        //out-622
        // in-625
        StockTotalDTO stockTotalDTOs = new StockTotalDTO();
        stockTotalDTOs.setAvailableQuantity(0L);
        when(stockTotalService.getStockTotalForProcessStock(anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(stockTotalDTOs);
        stockTransMaterialService.getAvailableQuantity(1L, 1L, 1L, 1L);
    }

    @Test(expected = Exception.class)
    public void testGetAvailableQuantity_3() throws  Exception {
        //out-622
        // in-625
        StockTotalDTO stockTotalDTOs = new StockTotalDTO();
        stockTotalDTOs.setAvailableQuantity(1L);
        stockTotalDTOs.setCurrentQuantity(0L);
        when(stockTotalService.getStockTotalForProcessStock(anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(stockTotalDTOs);
        stockTransMaterialService.getAvailableQuantity(1L, 1L, 1L, 1L);
    }

    @Test
    public void testGetAvailableQuantity_4() throws  Exception {
        // out-625
        StockTotalDTO stockTotalDTOs = new StockTotalDTO();
        stockTotalDTOs.setAvailableQuantity(1L);
        stockTotalDTOs.setCurrentQuantity(1L);
        when(stockTotalService.getStockTotalForProcessStock(anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(stockTotalDTOs);
        stockTransMaterialService.getAvailableQuantity(1L, 1L, 1L, 1L);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransMaterialServiceImpl.validateExistProdStockTotal
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testValidateExistProdStockTotal_1() throws  Exception {
        //in-335,337,339(true)
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        Long ownerType=2L;
        Long ownerId=1L;
        Long stateId=1L;
        Long prodOfferId=2L;
        Long requestQuantity=3L;
        ProductOfferingTotalDTO productOfferingTotalDTO = mock(ProductOfferingTotalDTO.class);
        List<ProductOfferingTotalDTO> lsTotal = Lists.newArrayList();
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffCode("405");

        when(productOfferingRepo.getLsRequestProductByShop(anyLong(), anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(lsTotal);
        when(staffService.findOne(any())).thenReturn(staffDTO);
        Whitebox.invokeMethod(spyService, "validateExistProdStockTotal", ownerType,ownerId,stateId,prodOfferId,requestQuantity);
    }

    @Test(expected = Exception.class)
    public void testValidateExistProdStockTotal_2() throws  Exception {
        //in-335,337,339(false)
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        Long ownerType=2L;
        Long ownerId=1L;
        Long stateId=1L;
        Long prodOfferId=2L;
        Long requestQuantity=3L;
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        offeringDTO.setProductOfferTypeId(2L);
        List<ProductOfferingTotalDTO> lsTotal = Lists.newArrayList();
        StaffDTO staffDTO = null;

        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        when(productOfferingRepo.getLsRequestProductByShop(anyLong(), anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(lsTotal);
        when(staffService.findOne(any())).thenReturn(staffDTO);
        Whitebox.invokeMethod(spyService, "validateExistProdStockTotal", ownerType,ownerId,stateId,prodOfferId,requestQuantity);
    }

    @Test(expected = Exception.class)
    public void testValidateExistProdStockTotal_3() throws  Exception {
        //in-335,out-337
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        Long ownerType=1L;
        Long ownerId=1L;
        Long stateId=1L;
        Long prodOfferId=2L;
        Long requestQuantity=3L;
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        offeringDTO.setProductOfferTypeId(2L);
        List<ProductOfferingTotalDTO> lsTotal = Lists.newArrayList();
        StaffDTO staffDTO = null;

        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        when(productOfferingRepo.getLsRequestProductByShop(anyLong(), anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(lsTotal);
        when(staffService.findOne(any())).thenReturn(staffDTO);
        Whitebox.invokeMethod(spyService, "validateExistProdStockTotal", ownerType,ownerId,stateId,prodOfferId,requestQuantity);
    }

    @Test(expected = Exception.class)
    public void testValidateExistProdStockTotal_4() throws  Exception {
        //out-335
        //in-348
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        Long ownerType=1L;
        Long ownerId=1L;
        Long stateId=1L;
        Long prodOfferId=2L;
        Long requestQuantity=3L;
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        offeringDTO.setProductOfferTypeId(2L);
        ProductOfferingTotalDTO productOfferingTotalDTO = mock(ProductOfferingTotalDTO.class);
        List<ProductOfferingTotalDTO> lsTotal = Lists.newArrayList(productOfferingTotalDTO);
        StaffDTO staffDTO = null;

        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        when(productOfferingRepo.getLsRequestProductByShop(anyLong(), anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(lsTotal);
        when(staffService.findOne(any())).thenReturn(staffDTO);
        when(productOfferingTotalDTO.getAvailableQuantity()).thenReturn(2L);
        when(productOfferingTotalDTO.getRequestQuantity()).thenReturn(2L);
        Whitebox.invokeMethod(spyService, "validateExistProdStockTotal", ownerType,ownerId,stateId,prodOfferId,requestQuantity);
    }

    @Test(expected = Exception.class)
    public void testValidateExistProdStockTotal_5() throws  Exception {
        //out-348
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        Long ownerType=1L;
        Long ownerId=1L;
        Long stateId=1L;
        Long prodOfferId=2L;
        Long requestQuantity=3L;
        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        offeringDTO.setProductOfferTypeId(2L);
        ProductOfferingTotalDTO productOfferingTotalDTO = mock(ProductOfferingTotalDTO.class);
        List<ProductOfferingTotalDTO> lsTotal = Lists.newArrayList(productOfferingTotalDTO);
        StaffDTO staffDTO = null;

        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        when(productOfferingRepo.getLsRequestProductByShop(anyLong(), anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(lsTotal);
        when(staffService.findOne(any())).thenReturn(staffDTO);
        when(productOfferingTotalDTO.getAvailableQuantity()).thenReturn(2L);
        when(productOfferingTotalDTO.getRequestQuantity()).thenReturn(2L);
        Whitebox.invokeMethod(spyService, "validateExistProdStockTotal", ownerType,ownerId,stateId,prodOfferId,requestQuantity);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransMaterialServiceImpl.debitTotal
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testDebitTotal_1() throws  Exception {
        //in:368,370
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        Long ownerType =1L;
        Long ownerId   =1L;
        Long prodOfferId =1L;
        Long oldStateId  =1L;
        Long quantity    =1L;
        Date sysdate = new Date();
        boolean isCheckIm1 = true;

        StockTotalDTO stockTotalDTO = mock(StockTotalDTO.class);
        StockTotalDTO stockTotalDTO2 = mock(StockTotalDTO.class);
        List<StockTotalDTO> lstStockTotal = Lists.newArrayList(stockTotalDTO);
        lstStockTotal.add(stockTotalDTO2);

        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();

        when(stockTotalService.searchStockTotal(any())).thenReturn(lstStockTotal);
        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        Whitebox.invokeMethod(spyService, "debitTotal", totalDTOSearch, totalAuditInput, ownerType, ownerId, prodOfferId, oldStateId, quantity, sysdate, isCheckIm1);
    }

    @Test(expected = Exception.class)
    public void testDebitTotal_2() throws  Exception {
        //in:368
        //out:370
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        Long ownerType =1L;
        Long ownerId   =1L;
        Long prodOfferId =1L;
        Long oldStateId  =1L;
        Long quantity    =1L;
        Date sysdate = new Date();
        boolean isCheckIm1 = true;

        StockTotalDTO stockTotalDTO = mock(StockTotalDTO.class);
        StockTotalDTO stockTotalDTO2 = mock(StockTotalDTO.class);
        List<StockTotalDTO> lstStockTotal = Lists.newArrayList(stockTotalDTO);
        lstStockTotal.add(stockTotalDTO2);

        ProductOfferingDTO offeringDTO = null;

        when(stockTotalService.searchStockTotal(any())).thenReturn(lstStockTotal);
        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        Whitebox.invokeMethod(spyService, "debitTotal", totalDTOSearch, totalAuditInput, ownerType, ownerId, prodOfferId, oldStateId, quantity, sysdate, isCheckIm1);
    }

    @Test(expected = Exception.class)
    public void testDebitTotal_3() throws  Exception {
        //out:368:lstStockTotal=null
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        Long ownerType =1L;
        Long ownerId   =1L;
        Long prodOfferId =1L;
        Long oldStateId  =1L;
        Long quantity    =1L;
        Date sysdate = new Date();
        boolean isCheckIm1 = true;

        List<StockTotalDTO> lstStockTotal = null;
        ProductOfferingDTO offeringDTO = null;

        when(stockTotalService.searchStockTotal(any())).thenReturn(lstStockTotal);
        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        Whitebox.invokeMethod(spyService, "debitTotal", totalDTOSearch, totalAuditInput, ownerType, ownerId, prodOfferId, oldStateId, quantity, sysdate, isCheckIm1);
    }

    @Test(expected = Exception.class)
    public void testDebitTotal_4() throws  Exception {
        //out:368:lstStockTotal!=null and lstStockTotal.size<=1
        //in:407
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        Long ownerType =1L;
        Long ownerId   =1L;
        Long prodOfferId =1L;
        Long oldStateId  =1L;
        Long quantity    =1L;
        Date sysdate = new Date();
        boolean isCheckIm1 = true;

        StockTotalDTO stockTotalDTO = mock(StockTotalDTO.class);
        List<StockTotalDTO> lstStockTotal = Lists.newArrayList(stockTotalDTO);
        ProductOfferingDTO offeringDTO = null;

        Query queryObject = mock(Query.class);

        when(em.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.executeUpdate()).thenReturn(0);
        when(stockTotalService.searchStockTotal(any())).thenReturn(lstStockTotal);
        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        Whitebox.invokeMethod(spyService, "debitTotal", totalDTOSearch, totalAuditInput, ownerType, ownerId, prodOfferId, oldStateId, quantity, sysdate, isCheckIm1);
    }

    @Test(expected = Exception.class)
    public void testDebitTotal_5() throws  Exception {
        //out:407
        //in:411
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        Long ownerType =1L;
        Long ownerId   =1L;
        Long prodOfferId =1L;
        Long oldStateId  =1L;
        Long quantity    =1L;
        Date sysdate = new Date();
        boolean isCheckIm1 = true;

        StockTotalDTO stockTotalDTO = mock(StockTotalDTO.class);
        List<StockTotalDTO> lstStockTotal = Lists.newArrayList(stockTotalDTO);
        ProductOfferingDTO offeringDTO = null;

        Query queryObject = mock(Query.class);

        when(em.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.executeUpdate()).thenReturn(1);
        when(stockTotalService.searchStockTotal(any())).thenReturn(lstStockTotal);
        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        Whitebox.invokeMethod(spyService, "debitTotal", totalDTOSearch, totalAuditInput, ownerType, ownerId, prodOfferId, oldStateId, quantity, sysdate, isCheckIm1);
    }

    @Test
    public void testDebitTotal_6() throws  Exception {
        //out:411
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        Long ownerType =1L;
        Long ownerId   =1L;
        Long prodOfferId =1L;
        Long oldStateId  =1L;
        Long quantity    =1L;
        Date sysdate = new Date();
        boolean isCheckIm1 = false;

        StockTotalDTO stockTotalDTO = mock(StockTotalDTO.class);
        List<StockTotalDTO> lstStockTotal = Lists.newArrayList(stockTotalDTO);
        ProductOfferingDTO offeringDTO = null;

        Query queryObject = mock(Query.class);

        when(em.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.executeUpdate()).thenReturn(1);
        when(stockTotalService.searchStockTotal(any())).thenReturn(lstStockTotal);
        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        Whitebox.invokeMethod(spyService, "debitTotal", totalDTOSearch, totalAuditInput, ownerType, ownerId, prodOfferId, oldStateId, quantity, sysdate, isCheckIm1);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransMaterialServiceImpl.debitTotalIm1
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testDebitTotalIm1_1() throws  Exception {
        //in:438,440
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        totalDTOSearch.setProdOfferId(1L);
        totalDTOSearch.setOwnerId(1L);
        totalDTOSearch.setOwnerType(1L);
        totalDTOSearch.setStateId(1L);
        totalDTOSearch.setStatus(1L);
        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        Long ownerType =1L;
        Long ownerId   =1L;
        Long prodOfferId =1L;
        Long oldStateId  =1L;
        Long quantity    =1L;
        Date sysdate = new Date();

        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        StockTotalIm1DTO stockTotalIm1DTO2 = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList(stockTotalIm1DTO);
        lstStockTotal.add(stockTotalIm1DTO2);

        StockModelIm1DTO offeringDTO = new StockModelIm1DTO();

        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        when(stockTotalIm1Service.findOneStockModel(anyLong())).thenReturn(offeringDTO);
        Whitebox.invokeMethod(spyService, "debitTotalIm1", totalDTOSearch, totalAuditInput, ownerId,ownerType, prodOfferId, oldStateId, quantity, sysdate);
    }

    @Test(expected = Exception.class)
    public void testDebitTotalIm1_2() throws  Exception {
        //in:438
        //out:440
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        totalDTOSearch.setProdOfferId(1L);
        totalDTOSearch.setOwnerId(1L);
        totalDTOSearch.setOwnerType(1L);
        totalDTOSearch.setStateId(1L);
        totalDTOSearch.setStatus(1L);
        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        Long ownerType =1L;
        Long ownerId   =1L;
        Long prodOfferId =1L;
        Long oldStateId  =1L;
        Long quantity    =1L;
        Date sysdate = new Date();

        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        StockTotalIm1DTO stockTotalIm1DTO2 = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList(stockTotalIm1DTO);
        lstStockTotal.add(stockTotalIm1DTO2);

        StockModelIm1DTO offeringDTO = null;

        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        when(stockTotalIm1Service.findOneStockModel(anyLong())).thenReturn(offeringDTO);
        Whitebox.invokeMethod(spyService, "debitTotalIm1", totalDTOSearch, totalAuditInput, ownerId,ownerType, prodOfferId, oldStateId, quantity, sysdate);
    }

    @Test(expected = Exception.class)
    public void testDebitTotalIm1_3() throws  Exception {
        //out:438:lstStockTotal=null
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        totalDTOSearch.setProdOfferId(1L);
        totalDTOSearch.setOwnerId(1L);
        totalDTOSearch.setOwnerType(1L);
        totalDTOSearch.setStateId(1L);
        totalDTOSearch.setStatus(1L);
        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        Long ownerType =1L;
        Long ownerId   =1L;
        Long prodOfferId =1L;
        Long oldStateId  =1L;
        Long quantity    =1L;
        Date sysdate = new Date();

        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        StockTotalIm1DTO stockTotalIm1DTO2 = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList(stockTotalIm1DTO);
        lstStockTotal.add(stockTotalIm1DTO2);

        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        Whitebox.invokeMethod(spyService, "debitTotalIm1", totalDTOSearch, totalAuditInput, ownerId,ownerType, prodOfferId, oldStateId, quantity, sysdate);
    }

    @Test(expected = Exception.class)
    public void testDebitTotalIm1_4() throws  Exception {
        //out:438:lstStockTotal!=null and size <=1
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        totalDTOSearch.setProdOfferId(1L);
        totalDTOSearch.setOwnerId(1L);
        totalDTOSearch.setOwnerType(1L);
        totalDTOSearch.setStateId(1L);
        totalDTOSearch.setStatus(1L);
        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        Long ownerType =1L;
        Long ownerId   =1L;
        Long prodOfferId =1L;
        Long oldStateId  =1L;
        Long quantity    =1L;
        Date sysdate = new Date();

        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList(stockTotalIm1DTO);

        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        Whitebox.invokeMethod(spyService, "debitTotalIm1", totalDTOSearch, totalAuditInput, ownerId,ownerType, prodOfferId, oldStateId, quantity, sysdate);
    }

    @Test
    public void testDebitTotalIm1_5() throws  Exception {
        //in:470,474
        //out:492
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        totalDTOSearch.setProdOfferId(1L);
        totalDTOSearch.setOwnerId(1L);
        totalDTOSearch.setOwnerType(1L);
        totalDTOSearch.setStateId(1L);
        totalDTOSearch.setStatus(1L);
        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        Long ownerType =1L;
        Long ownerId   =1L;
        Long prodOfferId =1L;
        Long oldStateId  =1L;
        Long quantity    =1L;
        Date sysdate = new Date();

        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList(stockTotalIm1DTO);

        StaffDTO staffDTO = new StaffDTO();
        ShopDTO shopDTO = new ShopDTO();
        Query queryObject = mock(Query.class);

        when(emLib.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.executeUpdate()).thenReturn(1);
        when(staffService.findOne(any())).thenReturn(staffDTO);
        when(shopService.findOne(any())).thenReturn(shopDTO);
        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        Whitebox.invokeMethod(spyService, "debitTotalIm1", totalDTOSearch, totalAuditInput, ownerId,ownerType, prodOfferId, oldStateId, quantity, sysdate);
    }

    @Test(expected = Exception.class)
    public void testDebitTotalIm1_6() throws  Exception {
        //out:470,474
        //in:492
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockTotalDTO totalDTOSearch = new StockTotalDTO();
        totalDTOSearch.setProdOfferId(1L);
        totalDTOSearch.setOwnerId(1L);
        totalDTOSearch.setOwnerType(1L);
        totalDTOSearch.setStateId(1L);
        totalDTOSearch.setStatus(1L);
        StockTotalAuditDTO totalAuditInput = new StockTotalAuditDTO();
        Long ownerType =1L;
        Long ownerId   =1L;
        Long prodOfferId =1L;
        Long oldStateId  =1L;
        Long quantity    =1L;
        Date sysdate = new Date();

        StockTotalIm1DTO stockTotalIm1DTO = mock(StockTotalIm1DTO.class);
        List<StockTotalIm1DTO> lstStockTotal = Lists.newArrayList(stockTotalIm1DTO);

        StaffDTO staffDTO = null;
        ShopDTO shopDTO = null;

        Query queryObject = mock(Query.class);

        when(emLib.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.executeUpdate()).thenReturn(0);
        when(staffService.findOne(any())).thenReturn(staffDTO);
        when(shopService.findOne(any())).thenReturn(shopDTO);
        when(stockTotalIm1Service.findStockTotal(any())).thenReturn(lstStockTotal);
        Whitebox.invokeMethod(spyService, "debitTotalIm1", totalDTOSearch, totalAuditInput, ownerId,ownerType, prodOfferId, oldStateId, quantity, sysdate);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransMaterialServiceImpl.updateStockX
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdateStockX_1() throws  Exception {
        //in:509,520
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        Long productOfferTypeId = 7L;
        boolean isCheckIm1 = true;

        Whitebox.invokeMethod(spyService, "updateStockX", "1", "1", new Date(), 1L, productOfferTypeId, 1L, 1L, isCheckIm1);
    }

    @Test(expected = Exception.class)
    public void testUpdateStockX_2() throws  Exception {
        //in:509,520,528
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        Long productOfferTypeId = 10L;
        boolean isCheckIm1 = true;

        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        offeringDTO.setName("dto");
        Query queryObject = mock(Query.class);

        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        when(em.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.executeUpdate()).thenReturn(0);
        Whitebox.invokeMethod(spyService, "updateStockX", "1", "1", new Date(), 1L, productOfferTypeId, 1L, 1L, isCheckIm1);
    }

    @Test(expected = Exception.class)
    public void testUpdateStockX_3() throws  Exception {
        //out:509,520,528
        //in:511,523,539
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        Long productOfferTypeId = 5L;
        boolean isCheckIm1 = true;

        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        offeringDTO.setName("dto");
        Query queryObject = mock(Query.class);

        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        when(em.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.executeUpdate()).thenReturn(1);
        Whitebox.invokeMethod(spyService, "updateStockX", "1", "1", new Date(), 1L, productOfferTypeId, 1L, 1L, isCheckIm1);
    }

    @Test
    public void testUpdateStockX_4() throws  Exception {
        //out:539
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        Long productOfferTypeId = 5L;
        boolean isCheckIm1 = false;

        ProductOfferingDTO offeringDTO = new ProductOfferingDTO();
        offeringDTO.setName("dto");
        Query queryObject = mock(Query.class);

        when(productOfferingService.findOne(any())).thenReturn(offeringDTO);
        when(em.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.executeUpdate()).thenReturn(1);
        Whitebox.invokeMethod(spyService, "updateStockX", "1", "1", new Date(), 1L, productOfferTypeId, 1L, 1L, isCheckIm1);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransMaterialServiceImpl.updateStockXIM1
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdateStockXIM1_1() throws  Exception {
        //in:556
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockModelIm1DTO offeringDTO =null;

        when(stockTotalIm1Service.findOneStockModel(anyLong())).thenReturn(offeringDTO);
        Whitebox.invokeMethod(spyService, "updateStockXIM1", "1", "1", 1L, 1L, 1L, 1L);
    }

    @Test(expected = Exception.class)
    public void testUpdateStockXIM1_2() throws  Exception {
        //out:556
        //in:559
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockModelIm1DTO offeringDTO =new StockModelIm1DTO();
        offeringDTO.setCheckSerial(2L);

        when(stockTotalIm1Service.findOneStockModel(anyLong())).thenReturn(offeringDTO);
        Whitebox.invokeMethod(spyService, "updateStockXIM1", "1", "1", 1L, 1L, 1L, 1L);
    }

    @Test(expected = Exception.class)
    public void testUpdateStockXIM1_3() throws  Exception {
        //out:559
        //in:567
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockModelIm1DTO offeringDTO =new StockModelIm1DTO();
        offeringDTO.setCheckSerial(1L);
        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();

        when(stockTotalIm1Service.findOneStockModel(anyLong())).thenReturn(offeringDTO);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        Whitebox.invokeMethod(spyService, "updateStockXIM1", "1", "1", 1L, 1L, 1L, 1L);
    }

    @Test(expected = Exception.class)
    public void testUpdateStockXIM1_4() throws  Exception {
        //out:567
        //in:569
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockModelIm1DTO offeringDTO =new StockModelIm1DTO();
        offeringDTO.setCheckSerial(1L);
        StockTypeIm1DTO stockTypeIm1DTO = null;

        when(stockTotalIm1Service.findOneStockModel(anyLong())).thenReturn(offeringDTO);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        Whitebox.invokeMethod(spyService, "updateStockXIM1", "1", "1", 1L, 1L, 1L, 1L);
    }

    @Test(expected = Exception.class)
    public void testUpdateStockXIM1_5() throws  Exception {
        //in:577,587,595
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockModelIm1DTO offeringDTO =new StockModelIm1DTO();
        offeringDTO.setCheckSerial(1L);
        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();

        Query queryObject = mock(Query.class);

        when(emLib.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.executeUpdate()).thenReturn(0);
        when(stockTotalIm1Service.findOneStockModel(anyLong())).thenReturn(offeringDTO);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        Whitebox.invokeMethod(spyService, "updateStockXIM1", "1", "1", 1L, 7L, 1L, 1L);
    }

    @Test
    public void testUpdateStockXIM1_6() throws  Exception {
        //out:577,587,595
        //in:579,590
        StockTransMaterialServiceImpl spyService = PowerMockito.spy(stockTransMaterialService);
        StockModelIm1DTO offeringDTO =new StockModelIm1DTO();
        offeringDTO.setCheckSerial(1L);
        StockTypeIm1DTO stockTypeIm1DTO = new StockTypeIm1DTO();

        Query queryObject = mock(Query.class);

        when(emLib.createNativeQuery(anyString())).thenReturn(queryObject);
        when(queryObject.executeUpdate()).thenReturn(1);
        when(stockTotalIm1Service.findOneStockModel(anyLong())).thenReturn(offeringDTO);
        when(stockTotalIm1Service.findOneStockType(anyLong())).thenReturn(stockTypeIm1DTO);
        Whitebox.invokeMethod(spyService, "updateStockXIM1", "1", "1", 1L, 2L, 1L, 1L);
    }
}