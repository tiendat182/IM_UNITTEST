package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.controller.SolrController;
import com.viettel.bccs.inventory.dto.*;
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

import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author DatLT
 * @date 11/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, BaseServiceImpl.class, VtShopService.class, SolrController.class})
public class VtShopServiceImplTest {
    @InjectMocks
    VtShopServiceImpl vtShopService;
    @Mock
    private StockIsdnVtShopService stockIsdnVtShopService;
    @Mock
    private ProductOfferingService productOfferingService;
    @Mock
    private ProductWs productWs;
    @Mock
    private ShopService shopService;
    @Mock
    private OptionSetValueService optionSetValueService;
    @Mock
    private AreaService areaService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VtShopServiceImpl.saveOrderIsdn
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSaveOrderIsdn_1() throws  Exception {
        vtShopService.saveOrderIsdn("isdnOrder", "otp", "idNo","viettelshopId","customerName","contactIsdn",
                "address",new Date(),"payStatus", Lists.newArrayList(), Lists.newArrayList());
    }

    @Test
    public void testSaveOrderIsdn_2() throws  Exception {
        doThrow(new LogicException()).when(stockIsdnVtShopService).saveOrderIsdn(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        vtShopService.saveOrderIsdn("isdnOrder", "otp", "idNo","viettelshopId","customerName","contactIsdn",
                "address",new Date(),"payStatus", Lists.newArrayList(), Lists.newArrayList());
    }

    @Test
    public void testSaveOrderIsdn_3() throws  Exception {
        doThrow(new Exception()).when(stockIsdnVtShopService).saveOrderIsdn(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        vtShopService.saveOrderIsdn("isdnOrder", "otp", "idNo","viettelshopId","customerName","contactIsdn",
                "address",new Date(),"payStatus", Lists.newArrayList(), Lists.newArrayList());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VtShopServiceImpl.increaseExpiredDateOrder
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testIncreaseExpiredDateOrder_1() throws  Exception {
        vtShopService.increaseExpiredDateOrder("id", new Date());
    }

    @Test
    public void testIncreaseExpiredDateOrder_2() throws  Exception {
        doThrow(new LogicException()).when(stockIsdnVtShopService).increaseExpiredDateOrder(any(),any());
        vtShopService.increaseExpiredDateOrder("id", new Date());
    }

    @Test
    public void testIncreaseExpiredDateOrder_3() throws  Exception {
        doThrow(new Exception()).when(stockIsdnVtShopService).increaseExpiredDateOrder(any(),any());
        vtShopService.increaseExpiredDateOrder("id", new Date());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VtShopServiceImpl.findVtShopFeeByIsdn
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindVtShopFeeByIsdn_1() throws  Exception {
        vtShopService.findVtShopFeeByIsdn("1");
    }

    @Test
    public void testFindVtShopFeeByIsdn_2() throws  Exception {
        when(stockIsdnVtShopService.findVtShopFeeByIsdn(anyString())).thenThrow(new Exception());
        vtShopService.findVtShopFeeByIsdn("1");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VtShopServiceImpl.cancelOrderIsdn
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCancelOrderIsdn_1() throws  Exception {
        vtShopService.cancelOrderIsdn("1");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VtShopServiceImpl.cancelOrderIsdn
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCancelOrderIsdn_2() throws  Exception {
        doThrow(new LogicException()).when(stockIsdnVtShopService).cancelOrderIsdn(any());
        vtShopService.cancelOrderIsdn("1");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VtShopServiceImpl.cancelOrderIsdn
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCancelOrderIsdn_3() throws  Exception {
        doThrow(new Exception()).when(stockIsdnVtShopService).cancelOrderIsdn(any());
        vtShopService.cancelOrderIsdn("1");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VtShopServiceImpl.findOrderIsdn
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOrderIsdn_1() throws  Exception {
        String isdn="";
        vtShopService.findOrderIsdn(isdn);
    }

    @Test
    public void testFindOrderIsdn_2() throws  Exception {
        String isdn="1";
        vtShopService.findOrderIsdn(isdn);
    }

    @Test
    public void testFindOrderIsdn_3() throws  Exception {
        String isdn="1";
        when(stockIsdnVtShopService.findOne(isdn)).thenThrow(new Exception());
        vtShopService.findOrderIsdn(isdn);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VtShopServiceImpl.findStockByArea
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindStockByArea_1() throws  Exception {
        String province="";
        String district="2";
        String productOfferCode="3";
        vtShopService.findStockByArea(province,district,productOfferCode);
    }

    @Test
    public void testFindStockByArea_2() throws  Exception {
        String province="1";
        String district="2";
        String productOfferCode="3";

        ProductOfferingDTO productOffer = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS_ACTIVE)).thenReturn(null);

        vtShopService.findStockByArea(province,district,productOfferCode);
    }

    @Test
    public void testFindStockByArea_3() throws  Exception {
        String province="1";
        String district="2";
        String productOfferCode="3";

        ProductOfferingDTO productOffer = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS_ACTIVE)).thenReturn(productOffer);
        when(productOffer.getProductOfferingId()).thenReturn(1L);
        when(productWs.findByShopCodeVTN(any())).thenReturn(null);

        vtShopService.findStockByArea(province,district,productOfferCode);
    }

    @Test
    public void testFindStockByArea_4() throws  Exception {
        String province="1";
        String district="2";
        String productOfferCode="3";

        ProductOfferingDTO productOffer = mock(ProductOfferingDTO.class);
        ShopMapDTO shopMapDTO = mock(ShopMapDTO.class);
        when(productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS_ACTIVE)).thenReturn(productOffer);
        when(productOffer.getProductOfferingId()).thenReturn(1L);
        when(productWs.findByShopCodeVTN(any())).thenReturn(shopMapDTO);
        when(shopMapDTO.getShopId()).thenReturn(1L);
        when(shopService.findOne(any())).thenReturn(null);

        vtShopService.findStockByArea(province,district,productOfferCode);
    }

    @Test
    public void testFindStockByArea_5() throws  Exception {
        String province="1";
        String district="2";
        String productOfferCode="3";

        ProductOfferingDTO productOffer = mock(ProductOfferingDTO.class);
        ShopMapDTO shopMapDTO = mock(ShopMapDTO.class);
        ShopDTO shopDTO = mock(ShopDTO.class);
        when(productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS_ACTIVE)).thenReturn(productOffer);
        when(productOffer.getProductOfferingId()).thenReturn(1L);
        when(productWs.findByShopCodeVTN(any())).thenReturn(shopMapDTO);
        when(shopMapDTO.getShopId()).thenReturn(1L);
        when(shopService.findOne(any())).thenReturn(shopDTO);
        when(shopDTO.getParentShopId()).thenReturn(1L);

        vtShopService.findStockByArea(province,district,productOfferCode);
    }

    @Test
    public void testFindStockByArea_6() throws  Exception {
        String province="1";
        String district="2";
        String productOfferCode="3";

        when(productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS_ACTIVE)).thenThrow(new Exception());

        vtShopService.findStockByArea(province,district,productOfferCode);
    }

    @Test
    public void testFindStockByArea_7() throws  Exception {
        String province="1";
        String district="";
        String productOfferCode="3";
        vtShopService.findStockByArea(province,district,productOfferCode);
    }

    @Test
    public void testFindStockByArea_8() throws  Exception {
        String province="1";
        String district="2";
        String productOfferCode="";
        vtShopService.findStockByArea(province,district,productOfferCode);
    }

    @Test
    public void testFindStockByArea_9() throws  Exception {
        String province="1";
        String district="2";
        String productOfferCode="3";

        ProductOfferingDTO productOffer = mock(ProductOfferingDTO.class);
        productOffer.setProductOfferingId(0L);
        when(productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS_ACTIVE)).thenReturn(productOffer);

        vtShopService.findStockByArea(province,district,productOfferCode);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VtShopServiceImpl.editOrderIsdn
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testEditOrderIsdn_1() throws  Exception {
        vtShopService.editOrderIsdn("1","2","3");
    }

    @Test
    public void testEditOrderIsdn_2() throws  Exception {
        doThrow(new LogicException()).when(stockIsdnVtShopService).editOrderIsdn(anyString(),anyString(),anyString());
        vtShopService.editOrderIsdn("1","2","3");
    }

    @Test
    public void testEditOrderIsdn_3() throws  Exception {
        doThrow(new Exception()).when(stockIsdnVtShopService).editOrderIsdn(anyString(),anyString(),anyString());
        vtShopService.editOrderIsdn("1","2","3");
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VtShopServiceImpl.findStockDigital
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindStockDigital_1() throws  Exception {
        String shopCode="";
        String productOfferCode = "2";
        vtShopService.findStockDigital(shopCode,productOfferCode);
    }

    @Test
    public void testFindStockDigital_2() throws  Exception {
        String shopCode="1";
        String productOfferCode = "2";

        when(productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS_ACTIVE)).thenReturn(null);

        vtShopService.findStockDigital(shopCode,productOfferCode);
    }

    @Test
    public void testFindStockDigital_3() throws  Exception {
        String shopCode="1";
        String productOfferCode = "2";

        ProductOfferingDTO productOffer = mock(ProductOfferingDTO.class);
        when(productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS_ACTIVE)).thenReturn(productOffer);
        when(productOffer.getProductOfferingId()).thenReturn(1L);
        when(shopService.getShopByCodeAndActiveStatus(any())).thenReturn(null);

        vtShopService.findStockDigital(shopCode,productOfferCode);
    }

    @Test
    public void testFindStockDigital_4() throws  Exception {
        String shopCode="1";
        String productOfferCode = "2";

        ProductOfferingDTO productOffer = mock(ProductOfferingDTO.class);
        ShopDTO shopDTO = mock(ShopDTO.class);
        when(productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS_ACTIVE)).thenReturn(productOffer);
        when(productOffer.getProductOfferingId()).thenReturn(1L);
        when(shopService.getShopByCodeAndActiveStatus(any())).thenReturn(shopDTO);
        when(shopDTO.getShopId()).thenReturn(1L);

        vtShopService.findStockDigital(shopCode,productOfferCode);
    }

    @Test
    public void testFindStockDigital_5() throws  Exception {
        String shopCode="1";
        String productOfferCode = "2";

        when(productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS_ACTIVE)).thenThrow(new Exception());

        vtShopService.findStockDigital(shopCode,productOfferCode);
    }

    @Test
    public void testFindStockDigital_6() throws  Exception {
        String shopCode="1";
        String productOfferCode = "";
        vtShopService.findStockDigital(shopCode,productOfferCode);
    }

    @Test
    public void testFindStockDigital_7() throws  Exception {
        String shopCode="1";
        String productOfferCode = "2";

        ProductOfferingDTO productOffer = new ProductOfferingDTO();
        productOffer.setProductOfferingId(0L);
        when(productOfferingService.findByProductOfferCode(productOfferCode, Const.STATUS_ACTIVE)).thenReturn(productOffer);
        when(shopService.getShopByCodeAndActiveStatus(any())).thenReturn(null);

        vtShopService.findStockDigital(shopCode,productOfferCode);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method VtShopServiceImpl.searchIsdn
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSearchIsdn_1() throws  Exception {
        String isdn = "";
        Long telecomServiceId = 2L;
        String areaCode = "3";
        Long startRow = 4L;
        Long maxRows = 5L;
        vtShopService.searchIsdn(isdn,telecomServiceId,areaCode,startRow,maxRows);
    }

    @Test
    public void testSearchIsdn_2() throws  Exception {
        String isdn = "1";
        Long telecomServiceId = null;
        String areaCode = "3";
        Long startRow = 4L;
        Long maxRows = 5L;
        vtShopService.searchIsdn(isdn,telecomServiceId,areaCode,startRow,maxRows);
    }

    @Test
    public void testSearchIsdn_3() throws  Exception {
        String isdn = "1";
        Long telecomServiceId = 2L;
        String areaCode = "3";
        Long startRow = null;
        Long maxRows = 5L;
        vtShopService.searchIsdn(isdn,telecomServiceId,areaCode,startRow,maxRows);
    }

    @Test
    public void testSearchIsdn_4() throws  Exception {
        String isdn = "1";
        Long telecomServiceId = 2L;
        String areaCode = "3";
        Long startRow = 4L;
        Long maxRows = null;
        vtShopService.searchIsdn(isdn,telecomServiceId,areaCode,startRow,maxRows);
    }

    @Test
    public void testSearchIsdn_5() throws  Exception {
        String isdn = "1";
        Long telecomServiceId = 3L;
        String areaCode = "";
        Long startRow = 4L;
        Long maxRows = 5L;
        vtShopService.searchIsdn(isdn,telecomServiceId,areaCode,startRow,maxRows);
    }

    @Test
    public void testSearchIsdn_6() throws  Exception {
        String isdn = "1";
        Long telecomServiceId = 3L;
        String areaCode = "3";
        Long startRow = 4L;
        Long maxRows = 5L;

        when(areaService.findByCode(areaCode)).thenReturn(null);
        vtShopService.searchIsdn(isdn,telecomServiceId,areaCode,startRow,maxRows);
    }

    @Test
    public void testSearchIsdn_7() throws  Exception {
        String isdn = "1";
        Long telecomServiceId = 3L;
        String areaCode = "3";
        Long startRow = 4L;
        Long maxRows = 5L;
        AreaDTO areaDTO = new AreaDTO();
        when(areaService.findByCode(areaCode)).thenReturn(areaDTO);
        vtShopService.searchIsdn(isdn,telecomServiceId,areaCode,startRow,maxRows);
    }

    @Test
    public void testSearchIsdn_8() throws  Exception {
        String isdn = "1";
        Long telecomServiceId = 1L;
        String areaCode = "3";
        Long startRow = 4L;
        Long maxRows = 5L;
        vtShopService.searchIsdn(isdn,telecomServiceId,areaCode,startRow,maxRows);
    }

    @Test
    public void testSearchIsdn_9() throws  Exception {
        String isdn = "1";
        Long telecomServiceId = 2L;
        String areaCode = "3";
        Long startRow = 4L;
        Long maxRows = 5L;
        vtShopService.searchIsdn(isdn,telecomServiceId,areaCode,startRow,maxRows);
    }

    @Test
    public void testSearchIsdn_10() throws  Exception {
        String isdn = "1";
        Long telecomServiceId = 4L;
        String areaCode = "3";
        Long startRow = 4L;
        Long maxRows = 5L;

        PowerMockito.mockStatic(SolrController.class);
        SolrController solrController = mock(SolrController.class);
        Mockito.when(SolrController.doSearch(any(),any(),any(),any(),anyString(),anyString(),anyLong())).thenReturn(null);

        vtShopService.searchIsdn(isdn,telecomServiceId,areaCode,startRow,maxRows);
    }

    @Test
    public void testSearchIsdn_11() throws  Exception {
        String isdn = "1";
        Long telecomServiceId = 4L;
        String areaCode = "3";
        Long startRow = 4L;
        Long maxRows = 5L;

        PowerMockito.mockStatic(SolrController.class);
        VStockNumberDTO vStockNumberDTO = new VStockNumberDTO();
        List<VStockNumberDTO> lstIsdn  = Lists.newArrayList(vStockNumberDTO);
        PowerMockito.doReturn(lstIsdn).when(SolrController.class, "doSearch",any(),any(),any(),any(),anyString(),anyString(),anyLong());

        vtShopService.searchIsdn(isdn,telecomServiceId,areaCode,startRow,maxRows);
    }

    @Test
    public void testSearchIsdn_12() throws  Exception {
        String isdn = "1";
        Long telecomServiceId = 2L;
        String areaCode = "3";
        Long startRow = 1L;
        Long maxRows = 51L;
        vtShopService.searchIsdn(isdn,telecomServiceId,areaCode,startRow,maxRows);
    }

    @Test
    public void testSearchIsdn_13() throws  Exception {
        String isdn = "1";
        Long telecomServiceId = 2L;
        String areaCode = "3";
        Long startRow = 1L;
        Long maxRows = -1L;
        vtShopService.searchIsdn(isdn,telecomServiceId,areaCode,startRow,maxRows);
    }

    @Test
    public void testSearchIsdn_14() throws  Exception {
        String isdn = "1";
        Long telecomServiceId = 2L;
        String areaCode = "3";
        Long startRow = -1L;
        Long maxRows = 51L;
        vtShopService.searchIsdn(isdn,telecomServiceId,areaCode,startRow,maxRows);
    }
}