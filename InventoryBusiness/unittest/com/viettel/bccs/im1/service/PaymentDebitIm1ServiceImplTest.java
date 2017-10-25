package com.viettel.bccs.im1.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.PaymentDebitIm1DTO;
import com.viettel.bccs.im1.model.PaymentDebitIm1;
import com.viettel.bccs.im1.repo.PaymentDebitIm1Repo;
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
@PrepareForTest({DbUtil.class, PaymentDebitIm1ServiceImpl.class, BaseServiceImpl.class, PaymentDebitIm1Service.class})
public class PaymentDebitIm1ServiceImplTest {
    @InjectMocks
    PaymentDebitIm1ServiceImpl paymentDebitIm1Service;
    @Mock
    private final BaseMapper<PaymentDebitIm1, PaymentDebitIm1DTO> mapper = new BaseMapper<>(PaymentDebitIm1.class, PaymentDebitIm1DTO.class);
    @Mock
    private PaymentDebitIm1Repo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method PaymentDebitIm1ServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws  Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, paymentDebitIm1Service.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method PaymentDebitIm1ServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        PaymentDebitIm1ServiceImpl spyService = PowerMockito.spy( paymentDebitIm1Service);
        PaymentDebitIm1 paymentDebitIm1 = mock(PaymentDebitIm1.class);
        PaymentDebitIm1DTO paymentDebitIm1DTO = new PaymentDebitIm1DTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(paymentDebitIm1);
        setFinalStatic(PaymentDebitIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(paymentDebitIm1)).thenReturn(paymentDebitIm1DTO);
        spyService.findOne(1L);
        Assert.assertNotNull(paymentDebitIm1DTO);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method PaymentDebitIm1ServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        PaymentDebitIm1ServiceImpl spyService = PowerMockito.spy( paymentDebitIm1Service);
        PaymentDebitIm1 paymentDebitIm1 = mock(PaymentDebitIm1.class);
        PaymentDebitIm1DTO paymentDebitIm1DTO = new PaymentDebitIm1DTO();
        List<PaymentDebitIm1DTO> numberFilterRuleDTOList = Lists.newArrayList(paymentDebitIm1DTO);
        List<PaymentDebitIm1> numberFilterRuleList = Lists.newArrayList(paymentDebitIm1);
        Mockito.when(repository.findAll()).thenReturn(numberFilterRuleList);
        Mockito.when(mapper.toDtoBean(numberFilterRuleList)).thenReturn(numberFilterRuleDTOList);
        setFinalStatic(PaymentDebitIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
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
        PaymentDebitIm1ServiceImpl spyService = PowerMockito.spy( paymentDebitIm1Service);
        PaymentDebitIm1 paymentDebitIm1 = mock(PaymentDebitIm1.class);
        PaymentDebitIm1DTO paymentDebitIm1DTO = new PaymentDebitIm1DTO();
        List<PaymentDebitIm1DTO> paymentDebitIm1DTOList = Lists.newArrayList(paymentDebitIm1DTO);
        List<PaymentDebitIm1> paymentDebitIm1List = Lists.newArrayList(paymentDebitIm1);
        Mockito.when(mapper.toDtoBean(paymentDebitIm1List)).thenReturn(paymentDebitIm1DTOList);
        setFinalStatic(PaymentDebitIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method PaymentDebitIm1ServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCreate_1() throws  Exception {
        PaymentDebitIm1ServiceImpl spyService = PowerMockito.spy( paymentDebitIm1Service);
        PaymentDebitIm1 paymentDebitIm1 = mock(PaymentDebitIm1.class);
        PaymentDebitIm1DTO paymentDebitIm1DTO = new PaymentDebitIm1DTO();

        setFinalStatic(PaymentDebitIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toPersistenceBean(paymentDebitIm1DTO)).thenReturn(paymentDebitIm1);
        spyService.create(new PaymentDebitIm1DTO());
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method PaymentDebitIm1ServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        PaymentDebitIm1ServiceImpl spyService = PowerMockito.spy( paymentDebitIm1Service);
        spyService.update(new PaymentDebitIm1DTO());
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method PaymentDebitIm1ServiceImpl.getPaymentDebit
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetPaymentDebit_1() throws  Exception {
        PaymentDebitIm1ServiceImpl spyService = PowerMockito.spy( paymentDebitIm1Service);
        PaymentDebitIm1 paymentDebitIm1 = mock(PaymentDebitIm1.class);
        PaymentDebitIm1DTO paymentDebitIm1DTO = new PaymentDebitIm1DTO();
        List<PaymentDebitIm1DTO> paymentDebitIm1DTOList = Lists.newArrayList(paymentDebitIm1DTO);
        List<PaymentDebitIm1> paymentDebitIm1List = Lists.newArrayList(paymentDebitIm1);

        setFinalStatic(PaymentDebitIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toPersistenceBean(paymentDebitIm1DTO)).thenReturn(paymentDebitIm1);
        Mockito.when(mapper.toDtoBean(paymentDebitIm1List)).thenReturn(paymentDebitIm1DTOList);
        when(spyService.findByFilter(any())).thenReturn(paymentDebitIm1DTOList);
        spyService.getPaymentDebit(1L, 1L);
    }

    @Test
    public void testGetPaymentDebit_2() throws  Exception {
        PaymentDebitIm1ServiceImpl spyService = PowerMockito.spy( paymentDebitIm1Service);
        PaymentDebitIm1 paymentDebitIm1 = mock(PaymentDebitIm1.class);
        PaymentDebitIm1DTO paymentDebitIm1DTO = new PaymentDebitIm1DTO();
        List<PaymentDebitIm1DTO> paymentDebitIm1DTOList = Lists.newArrayList();
        List<PaymentDebitIm1> paymentDebitIm1List = Lists.newArrayList(paymentDebitIm1);

        setFinalStatic(PaymentDebitIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toPersistenceBean(paymentDebitIm1DTO)).thenReturn(paymentDebitIm1);
        Mockito.when(mapper.toDtoBean(paymentDebitIm1List)).thenReturn(paymentDebitIm1DTOList);
        when(spyService.findByFilter(any())).thenReturn(paymentDebitIm1DTOList);

        spyService.getPaymentDebit(1L, 1L);
    }






    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}