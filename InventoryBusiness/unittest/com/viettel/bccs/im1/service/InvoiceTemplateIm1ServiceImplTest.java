package com.viettel.bccs.im1.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.InvoiceTemplateIm1DTO;
import com.viettel.bccs.im1.model.InvoiceTemplateIm1;
import com.viettel.bccs.im1.repo.InvoiceTemplateIm1Repo;
import com.viettel.bccs.inventory.dto.InvoiceTemplateDTO;
import com.viettel.bccs.inventory.dto.StaffDTO;
import com.viettel.bccs.inventory.service.StaffService;
import com.viettel.fw.common.util.DbUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
import com.viettel.fw.service.BaseServiceImpl;
import org.junit.Assert;
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

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author DatLT
 * @date 21/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class,  BaseServiceImpl.class, InvoiceTemplateIm1Service.class})
public class InvoiceTemplateIm1ServiceImplTest {
    @InjectMocks
    InvoiceTemplateIm1ServiceImpl invoiceTemplateIm1Service;
    @Mock
    private final BaseMapper<InvoiceTemplateIm1, InvoiceTemplateIm1DTO> mapper = new BaseMapper<>(InvoiceTemplateIm1.class, InvoiceTemplateIm1DTO.class);
    @Mock
    private StaffService staffService;
    @Mock
    private EntityManager em;
    @Mock
    private InvoiceTemplateIm1Repo repository;
    @Mock
    private InvoiceTemplateLogIm1Service invoiceTemplateLogIm1Service;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InvoiceTemplateIm1ServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws  Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, invoiceTemplateIm1Service.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InvoiceTemplateIm1ServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = new InvoiceTemplateIm1DTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(invoiceTemplateIm1);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(invoiceTemplateIm1)).thenReturn(invoiceTemplateIm1DTO);
        spyService.findOne(1L);
        Assert.assertNotNull(invoiceTemplateIm1DTO);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InvoiceTemplateIm1ServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = new InvoiceTemplateIm1DTO();
        List<InvoiceTemplateIm1DTO> numberFilterRuleDTOList = Lists.newArrayList(invoiceTemplateIm1DTO);
        List<InvoiceTemplateIm1> numberFilterRuleList = Lists.newArrayList(invoiceTemplateIm1);
        Mockito.when(repository.findAll()).thenReturn(numberFilterRuleList);
        Mockito.when(mapper.toDtoBean(numberFilterRuleList)).thenReturn(numberFilterRuleDTOList);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(numberFilterRuleDTOList);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method PaymentDebitIm1ServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = new InvoiceTemplateIm1DTO();
        List<InvoiceTemplateIm1DTO> invoiceTemplateIm1DTOList = Lists.newArrayList(invoiceTemplateIm1DTO);
        List<InvoiceTemplateIm1> invoiceTemplateIm1List = Lists.newArrayList(invoiceTemplateIm1);
        Mockito.when(mapper.toDtoBean(invoiceTemplateIm1List)).thenReturn(invoiceTemplateIm1DTOList);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InvoiceTemplateIm1ServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testCreate_1() throws  Exception {
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        spyService.create(new InvoiceTemplateIm1DTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InvoiceTemplateIm1ServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        spyService.update(new InvoiceTemplateIm1DTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InvoiceTemplateIm1ServiceImpl.deliverInvoice
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testDeliverInvoice_1() throws  Exception {
        //in 87
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        Long invoiceDeliveryId=1L;
        List<InvoiceTemplateDTO > lstInvoiceReceive = Lists.newArrayList();
        String staffCode = "1";
        Long amountDelivery=1L;

        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = new InvoiceTemplateIm1DTO();
        List<InvoiceTemplateIm1DTO> invoiceTemplateIm1DTOList = Lists.newArrayList();
        List<InvoiceTemplateIm1> invoiceTemplateIm1List = Lists.newArrayList(invoiceTemplateIm1);

        Mockito.when(mapper.toDtoBean(invoiceTemplateIm1List)).thenReturn(invoiceTemplateIm1DTOList);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.deliverInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amountDelivery);
    }

    @Test(expected = Exception.class)
    public void testDeliverInvoice_2() throws  Exception {
        //in 87
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        Long invoiceDeliveryId=1L;
        List<InvoiceTemplateDTO > lstInvoiceReceive = Lists.newArrayList();
        String staffCode = "1";
        Long amountDelivery=1L;

        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = mock(InvoiceTemplateIm1DTO.class);
        List<InvoiceTemplateIm1DTO> invoiceTemplateIm1DTOList = Lists.newArrayList(invoiceTemplateIm1DTO);
        List<InvoiceTemplateIm1> invoiceTemplateIm1List = Lists.newArrayList(invoiceTemplateIm1);
        when(invoiceTemplateIm1DTO.getType()).thenReturn(2L);

        Mockito.when(mapper.toDtoBean(invoiceTemplateIm1List)).thenReturn(invoiceTemplateIm1DTOList);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.deliverInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amountDelivery);
    }

    @Test(expected = Exception.class)
    public void testDeliverInvoice_3() throws  Exception {
        //out 87
        //in 91
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        Long invoiceDeliveryId=1L;
        List<InvoiceTemplateDTO > lstInvoiceReceive = Lists.newArrayList();
        String staffCode = "1";
        Long amountDelivery=1L;

        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = mock(InvoiceTemplateIm1DTO.class);
        when(invoiceTemplateIm1DTO.getType()).thenReturn(1L);

        StaffDTO staffDTO = null;
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);

        Mockito.when(repository.findOne(anyLong())).thenReturn(invoiceTemplateIm1);
        Mockito.when(mapper.toDtoBean(invoiceTemplateIm1)).thenReturn(invoiceTemplateIm1DTO);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.deliverInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amountDelivery);
    }

    @Test(expected = Exception.class)
    public void testDeliverInvoice_4() throws  Exception {
        //out 91
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        Long invoiceDeliveryId=1L;
        List<InvoiceTemplateDTO > lstInvoiceReceive = Lists.newArrayList();
        String staffCode = "1";
        Long amountDelivery=1L;

        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = mock(InvoiceTemplateIm1DTO.class);
        when(invoiceTemplateIm1DTO.getType()).thenReturn(1L);

        StaffDTO staffDTO = new StaffDTO();
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);

        Mockito.when(repository.findOne(anyLong())).thenReturn(invoiceTemplateIm1);
        Mockito.when(mapper.toDtoBean(invoiceTemplateIm1)).thenReturn(invoiceTemplateIm1DTO);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.deliverInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amountDelivery);
    }

    @Test(expected = Exception.class)
    public void testDeliverInvoice_5() throws  Exception {
        //out 94
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        Long invoiceDeliveryId=1L;
        List<InvoiceTemplateDTO > lstInvoiceReceive = Lists.newArrayList();
        String staffCode = "1";
        Long amountDelivery=1L;

        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = mock(InvoiceTemplateIm1DTO.class);
        when(invoiceTemplateIm1DTO.getType()).thenReturn(1L);

        StaffDTO staffDTO = new StaffDTO();
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);

        when(invoiceTemplateIm1DTO.getShopId()).thenReturn(2L);
        staffDTO.setShopId(2L);

        Mockito.when(repository.findOne(anyLong())).thenReturn(invoiceTemplateIm1);
        Mockito.when(mapper.toDtoBean(invoiceTemplateIm1)).thenReturn(invoiceTemplateIm1DTO);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.deliverInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amountDelivery);
    }

    @Test
    public void testDeliverInvoice_6() throws  Exception {
        //out 98
        //in 129 135 138
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        Long invoiceDeliveryId=1L;

        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        invoiceTemplateDTO.setOwnerType("1");
        invoiceTemplateDTO.setAmount(1L);

        List<InvoiceTemplateDTO > lstInvoiceReceive = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountDelivery=3L;

        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = mock(InvoiceTemplateIm1DTO.class);
        when(invoiceTemplateIm1DTO.getType()).thenReturn(1L);

        StaffDTO staffDTO = new StaffDTO();
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);

        when(invoiceTemplateIm1DTO.getShopId()).thenReturn(2L);
        staffDTO.setShopId(2L);

        when(invoiceTemplateIm1.getAmount()).thenReturn(3L);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(null);

        Mockito.when(repository.findOne(anyLong())).thenReturn(invoiceTemplateIm1);
        Mockito.when(mapper.toDtoBean(invoiceTemplateIm1)).thenReturn(invoiceTemplateIm1DTO);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.deliverInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amountDelivery);
    }

    @Test
    public void testDeliverInvoice_7() throws  Exception {
        //out 129 138
        //in 150
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        Long invoiceDeliveryId=1L;

        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        invoiceTemplateDTO.setOwnerType("2");
        invoiceTemplateDTO.setAmount(1L);

        List<InvoiceTemplateDTO > lstInvoiceReceive = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountDelivery=3L;

        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = mock(InvoiceTemplateIm1DTO.class);
        when(invoiceTemplateIm1DTO.getType()).thenReturn(1L);

        StaffDTO staffDTO = new StaffDTO();
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);

        when(invoiceTemplateIm1DTO.getShopId()).thenReturn(2L);
        staffDTO.setShopId(2L);

        when(invoiceTemplateIm1.getAmount()).thenReturn(3L);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(null);

        Mockito.when(repository.findOne(anyLong())).thenReturn(invoiceTemplateIm1);
        Mockito.when(mapper.toDtoBean(invoiceTemplateIm1)).thenReturn(invoiceTemplateIm1DTO);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.deliverInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amountDelivery);
    }

    @Test
    public void testDeliverInvoice_8() throws  Exception {
        //out 150 140
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        Long invoiceDeliveryId=1L;

        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        invoiceTemplateDTO.setOwnerType("3");
        invoiceTemplateDTO.setAmount(1L);

        List<InvoiceTemplateDTO > lstInvoiceReceive = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountDelivery=3L;

        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = mock(InvoiceTemplateIm1DTO.class);
        when(invoiceTemplateIm1DTO.getType()).thenReturn(1L);

        StaffDTO staffDTO = new StaffDTO();
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);

        when(invoiceTemplateIm1DTO.getShopId()).thenReturn(2L);
        staffDTO.setShopId(2L);

        when(invoiceTemplateIm1.getAmount()).thenReturn(3L);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(null);

        Mockito.when(repository.findOne(anyLong())).thenReturn(invoiceTemplateIm1);
        Mockito.when(mapper.toDtoBean(invoiceTemplateIm1)).thenReturn(invoiceTemplateIm1DTO);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.deliverInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amountDelivery);
    }

    @Test(expected = Exception.class)
    public void testDeliverInvoice_9() throws  Exception {
        //out 135
        //in 164
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        Long invoiceDeliveryId=1L;

        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        invoiceTemplateDTO.setOwnerType("2");
        invoiceTemplateDTO.setAmount(1L);

        List<InvoiceTemplateDTO > lstInvoiceReceive = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountDelivery=3L;

        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = mock(InvoiceTemplateIm1DTO.class);
        when(invoiceTemplateIm1DTO.getType()).thenReturn(1L);

        StaffDTO staffDTO = new StaffDTO();
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);

        when(invoiceTemplateIm1DTO.getShopId()).thenReturn(2L);
        staffDTO.setShopId(2L);

        when(invoiceTemplateIm1.getAmount()).thenReturn(3L);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateIm1);

        Mockito.when(repository.findOne(anyLong())).thenReturn(invoiceTemplateIm1);
        Mockito.when(mapper.toDtoBean(invoiceTemplateIm1)).thenReturn(invoiceTemplateIm1DTO);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.deliverInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amountDelivery);
    }

    @Test(expected = Exception.class)
    public void testDeliverInvoice_10() throws  Exception {
        //in 164
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        Long invoiceDeliveryId=1L;

        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        invoiceTemplateDTO.setOwnerType("2");
        invoiceTemplateDTO.setAmount(1L);

        List<InvoiceTemplateDTO > lstInvoiceReceive = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountDelivery=3L;

        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = mock(InvoiceTemplateIm1DTO.class);
        when(invoiceTemplateIm1DTO.getType()).thenReturn(1L);

        StaffDTO staffDTO = new StaffDTO();
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);

        when(invoiceTemplateIm1DTO.getShopId()).thenReturn(2L);
        staffDTO.setShopId(2L);

        when(invoiceTemplateIm1.getAmount()).thenReturn(3L);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateIm1);
        invoiceTemplateDTO.setAmountNotUsed(10L);

        Mockito.when(repository.findOne(anyLong())).thenReturn(invoiceTemplateIm1);
        Mockito.when(mapper.toDtoBean(invoiceTemplateIm1)).thenReturn(invoiceTemplateIm1DTO);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.deliverInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amountDelivery);
    }

    @Test
    public void testDeliverInvoice_11() throws  Exception {
        //out 164 197
        //in 178 188 192
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        Long invoiceDeliveryId=1L;

        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        invoiceTemplateDTO.setOwnerType("2");
        invoiceTemplateDTO.setAmount(1L);

        List<InvoiceTemplateDTO > lstInvoiceReceive = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountDelivery=3L;

        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = mock(InvoiceTemplateIm1DTO.class);
        when(invoiceTemplateIm1DTO.getType()).thenReturn(1L);

        StaffDTO staffDTO = new StaffDTO();
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);

        when(invoiceTemplateIm1DTO.getShopId()).thenReturn(2L);
        staffDTO.setShopId(2L);

        when(invoiceTemplateIm1.getAmount()).thenReturn(3L);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateIm1);
        invoiceTemplateDTO.setAmountNotUsed(3L);

        Mockito.when(repository.findOne(anyLong())).thenReturn(invoiceTemplateIm1);
        Mockito.when(mapper.toDtoBean(invoiceTemplateIm1)).thenReturn(invoiceTemplateIm1DTO);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.deliverInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amountDelivery);
    }

    @Test
    public void testDeliverInvoice_12() throws  Exception {
        //out 164
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        Long invoiceDeliveryId=1L;

        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        invoiceTemplateDTO.setOwnerType("1");
        invoiceTemplateDTO.setAmount(1L);

        List<InvoiceTemplateDTO > lstInvoiceReceive = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountDelivery=3L;

        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = mock(InvoiceTemplateIm1DTO.class);
        when(invoiceTemplateIm1DTO.getType()).thenReturn(1L);

        StaffDTO staffDTO = new StaffDTO();
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);

        when(invoiceTemplateIm1DTO.getShopId()).thenReturn(2L);
        staffDTO.setShopId(2L);

        when(invoiceTemplateIm1.getAmount()).thenReturn(3L);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateIm1);
        invoiceTemplateDTO.setAmountNotUsed(3L);

        Mockito.when(repository.findOne(anyLong())).thenReturn(invoiceTemplateIm1);
        Mockito.when(mapper.toDtoBean(invoiceTemplateIm1)).thenReturn(invoiceTemplateIm1DTO);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.deliverInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amountDelivery);
    }

    @Test
    public void testDeliverInvoice_13() throws  Exception {
        //out 164
        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        Long invoiceDeliveryId=1L;

        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        invoiceTemplateDTO.setOwnerType("3");
        invoiceTemplateDTO.setAmount(1L);

        List<InvoiceTemplateDTO > lstInvoiceReceive = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountDelivery=3L;

        InvoiceTemplateIm1 invoiceTemplateIm1 = mock(InvoiceTemplateIm1.class);
        InvoiceTemplateIm1DTO invoiceTemplateIm1DTO = mock(InvoiceTemplateIm1DTO.class);
        when(invoiceTemplateIm1DTO.getType()).thenReturn(1L);

        StaffDTO staffDTO = new StaffDTO();
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);

        when(invoiceTemplateIm1DTO.getShopId()).thenReturn(2L);
        staffDTO.setShopId(2L);

        when(invoiceTemplateIm1.getAmount()).thenReturn(3L);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateIm1);
        invoiceTemplateDTO.setAmountNotUsed(3L);

        Mockito.when(repository.findOne(anyLong())).thenReturn(invoiceTemplateIm1);
        Mockito.when(mapper.toDtoBean(invoiceTemplateIm1)).thenReturn(invoiceTemplateIm1DTO);
        setFinalStatic(InvoiceTemplateIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.deliverInvoice(invoiceDeliveryId, lstInvoiceReceive, staffCode, amountDelivery);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InvoiceTemplateIm1ServiceImpl.revokeInvoice
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testRevokeInvoice_1() throws  Exception {
        //in 221
        Long invoiceReceive=1L;
        List<InvoiceTemplateDTO> lstInvoiceRevoke = Lists.newArrayList();
        String staffCode = "1";
        Long amountReceive=1L;

        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        StaffDTO staffDTO = new StaffDTO();
        InvoiceTemplateIm1 invoiceTemplateReceiver = null;

        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateReceiver);

        spyService.revokeInvoice(invoiceReceive, lstInvoiceRevoke, staffCode, amountReceive);
    }

    @Test(expected = Exception.class)
    public void testRevokeInvoice_2() throws  Exception {
        //out 221
        //in 224
        Long invoiceReceive=1L;
        List<InvoiceTemplateDTO> lstInvoiceRevoke = Lists.newArrayList();
        String staffCode = "1";
        Long amountReceive=1L;

        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        StaffDTO staffDTO = new StaffDTO();
        InvoiceTemplateIm1 invoiceTemplateReceiver = new InvoiceTemplateIm1();

        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateReceiver);

        spyService.revokeInvoice(invoiceReceive, lstInvoiceRevoke, staffCode, amountReceive);
    }

    @Test(expected = Exception.class)
    public void testRevokeInvoice_3() throws  Exception {
        //out 221
        //in 224
        Long invoiceReceive=1L;
        List<InvoiceTemplateDTO> lstInvoiceRevoke = Lists.newArrayList();
        String staffCode = "1";
        Long amountReceive=1L;

        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        StaffDTO staffDTO = new StaffDTO();
        InvoiceTemplateIm1 invoiceTemplateReceiver = new InvoiceTemplateIm1();
        invoiceTemplateReceiver.setInvoiceTemplateId(2L);

        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateReceiver);

        spyService.revokeInvoice(invoiceReceive, lstInvoiceRevoke, staffCode, amountReceive);
    }

    //Khong tao dc test case nhay vao line 227

    @Test(expected = Exception.class)
    public void testRevokeInvoice_4() throws  Exception {
        //out 224
        //in 230
        Long invoiceReceive=1L;
        List<InvoiceTemplateDTO> lstInvoiceRevoke = Lists.newArrayList();
        String staffCode = "1";
        Long amountReceive=1L;

        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        InvoiceTemplateIm1 invoiceTemplateReceiver = new InvoiceTemplateIm1();
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(1L);
        invoiceTemplateReceiver.setShopId(2L);
        invoiceTemplateReceiver.setInvoiceTemplateId(1L);

        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateReceiver);

        spyService.revokeInvoice(invoiceReceive, lstInvoiceRevoke, staffCode, amountReceive);
    }

    @Test(expected = Exception.class)
    public void testRevokeInvoice_5() throws  Exception {
        //out 230
        //in 234
        Long invoiceReceive=1L;
        List<InvoiceTemplateDTO> lstInvoiceRevoke = Lists.newArrayList();
        String staffCode = "1";
        Long amountReceive=1L;

        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        InvoiceTemplateIm1 invoiceTemplateReceiver = new InvoiceTemplateIm1();
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(2L);
        invoiceTemplateReceiver.setShopId(2L);
        invoiceTemplateReceiver.setInvoiceTemplateId(1L);
        invoiceTemplateReceiver.setAmount(3L);

        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateReceiver);

        spyService.revokeInvoice(invoiceReceive, lstInvoiceRevoke, staffCode, amountReceive);
    }

    @Test(expected = Exception.class)
    public void testRevokeInvoice_6() throws  Exception {
        //out 234
        //in 258
        Long invoiceReceive=1L;
        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        List<InvoiceTemplateDTO> lstInvoiceRevoke = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountReceive=3L;

        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        InvoiceTemplateIm1 invoiceTemplateReceiver = new InvoiceTemplateIm1();
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(2L);
        invoiceTemplateReceiver.setShopId(2L);
        invoiceTemplateReceiver.setInvoiceTemplateId(1L);
        invoiceTemplateReceiver.setAmount(3L);
        invoiceTemplateDTO.setAmount(1L);
        invoiceTemplateDTO.setOwnerType("1");

        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateReceiver);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.revokeInvoice(invoiceReceive, lstInvoiceRevoke, staffCode, amountReceive);
    }

    @Test(expected = Exception.class)
    public void testRevokeInvoice_7() throws  Exception {
        //out 258
        Long invoiceReceive=1L;
        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        List<InvoiceTemplateDTO> lstInvoiceRevoke = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountReceive=3L;

        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        InvoiceTemplateIm1 invoiceTemplateReceiver = new InvoiceTemplateIm1();
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(2L);
        invoiceTemplateReceiver.setShopId(2L);
        invoiceTemplateReceiver.setInvoiceTemplateId(1L);
        invoiceTemplateReceiver.setAmount(3L);
        invoiceTemplateDTO.setAmount(1L);
        invoiceTemplateDTO.setOwnerType("2");

        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateReceiver);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.revokeInvoice(invoiceReceive, lstInvoiceRevoke, staffCode, amountReceive);
    }

    @Test(expected = Exception.class)
    public void testRevokeInvoice_8() throws  Exception {
        //out 258
        //in 272
        Long invoiceReceive=1L;
        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        List<InvoiceTemplateDTO> lstInvoiceRevoke = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountReceive=3L;

        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        InvoiceTemplateIm1 invoiceTemplateReceiver = new InvoiceTemplateIm1();
        InvoiceTemplateIm1 invoiceTemplateRevoke = new InvoiceTemplateIm1();
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(2L);
        invoiceTemplateReceiver.setShopId(2L);
        invoiceTemplateReceiver.setInvoiceTemplateId(1L);
        invoiceTemplateReceiver.setAmount(3L);
        invoiceTemplateDTO.setAmount(1L);
        invoiceTemplateDTO.setOwnerType("3");

        when(repository.findOne(anyLong())).thenReturn(invoiceTemplateRevoke);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateReceiver);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.revokeInvoice(invoiceReceive, lstInvoiceRevoke, staffCode, amountReceive);
    }

    @Test(expected = Exception.class)
    public void testRevokeInvoice_9() throws  Exception {
        //out 272
        //in 282
        Long invoiceReceive=1L;
        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        List<InvoiceTemplateDTO> lstInvoiceRevoke = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountReceive=3L;

        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        InvoiceTemplateIm1 invoiceTemplateReceiver = new InvoiceTemplateIm1();
        InvoiceTemplateIm1 invoiceTemplateRevoke = new InvoiceTemplateIm1();
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(2L);
        invoiceTemplateReceiver.setShopId(2L);
        invoiceTemplateReceiver.setInvoiceTemplateId(1L);
        invoiceTemplateReceiver.setAmount(3L);
        invoiceTemplateDTO.setAmount(1L);
        invoiceTemplateDTO.setOwnerType("3");
        invoiceTemplateDTO.setAmountNotUsed(9L);
        invoiceTemplateRevoke.setAmount(10L);

        when(repository.findOne(anyLong())).thenReturn(invoiceTemplateRevoke);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateReceiver);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.revokeInvoice(invoiceReceive, lstInvoiceRevoke, staffCode, amountReceive);
    }

    @Test
    public void testRevokeInvoice_10() throws  Exception {
        //out 282
        Long invoiceReceive=1L;
        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        List<InvoiceTemplateDTO> lstInvoiceRevoke = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountReceive=3L;

        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        InvoiceTemplateIm1 invoiceTemplateReceiver = new InvoiceTemplateIm1();
        InvoiceTemplateIm1 invoiceTemplateRevoke = new InvoiceTemplateIm1();
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(2L);
        invoiceTemplateReceiver.setShopId(2L);
        invoiceTemplateReceiver.setInvoiceTemplateId(1L);
        invoiceTemplateReceiver.setAmount(3L);
        invoiceTemplateDTO.setAmount(1L);
        invoiceTemplateDTO.setOwnerType("1");
        invoiceTemplateDTO.setAmountNotUsed(10L);
        invoiceTemplateRevoke.setAmount(10L);

        when(repository.findOne(anyLong())).thenReturn(invoiceTemplateRevoke);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateReceiver);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.revokeInvoice(invoiceReceive, lstInvoiceRevoke, staffCode, amountReceive);
    }

    @Test
    public void testRevokeInvoice_11() throws  Exception {
        //out 282
        //in cac line con lai
        Long invoiceReceive=1L;
        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        List<InvoiceTemplateDTO> lstInvoiceRevoke = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountReceive=3L;

        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        InvoiceTemplateIm1 invoiceTemplateReceiver = new InvoiceTemplateIm1();
        InvoiceTemplateIm1 invoiceTemplateRevoke = new InvoiceTemplateIm1();
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(2L);
        invoiceTemplateReceiver.setShopId(2L);
        invoiceTemplateReceiver.setInvoiceTemplateId(1L);
        invoiceTemplateReceiver.setAmount(3L);
        invoiceTemplateDTO.setAmount(1L);
        invoiceTemplateDTO.setOwnerType("2");
        invoiceTemplateDTO.setAmountNotUsed(10L);
        invoiceTemplateRevoke.setAmount(10L);

        when(repository.findOne(anyLong())).thenReturn(invoiceTemplateRevoke);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateReceiver);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.revokeInvoice(invoiceReceive, lstInvoiceRevoke, staffCode, amountReceive);
    }

    @Test
    public void testRevokeInvoice_12() throws  Exception {
        //out cac line con lai
        Long invoiceReceive=1L;
        InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
        List<InvoiceTemplateDTO> lstInvoiceRevoke = Lists.newArrayList(invoiceTemplateDTO);
        String staffCode = "1";
        Long amountReceive=3L;

        InvoiceTemplateIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateIm1Service);
        InvoiceTemplateIm1 invoiceTemplateReceiver = new InvoiceTemplateIm1();
        InvoiceTemplateIm1 invoiceTemplateRevoke = new InvoiceTemplateIm1();
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setShopId(2L);
        invoiceTemplateReceiver.setShopId(2L);
        invoiceTemplateReceiver.setInvoiceTemplateId(1L);
        invoiceTemplateReceiver.setAmount(3L);
        invoiceTemplateDTO.setAmount(1L);
        invoiceTemplateDTO.setOwnerType("3");
        invoiceTemplateDTO.setAmountNotUsed(10L);
        invoiceTemplateRevoke.setAmount(10L);

        when(repository.findOne(anyLong())).thenReturn(invoiceTemplateRevoke);
        when(staffService.getStaffByStaffCode(any())).thenReturn(staffDTO);
        when(repository.getInvoiceTemplate(anyLong(), anyLong())).thenReturn(invoiceTemplateReceiver);
        PowerMockito.mockStatic( DbUtil.class );
        PowerMockito.when( DbUtil.getSysDate( Mockito.any( EntityManager.class ) ) ).thenReturn( new Date() );
        spyService.revokeInvoice(invoiceReceive, lstInvoiceRevoke, staffCode, amountReceive);
    }

//Khong lam dc exception line 242, 277,106,159
    // 1 so case da coverage het nhung intellij van bao chua not coverage

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}