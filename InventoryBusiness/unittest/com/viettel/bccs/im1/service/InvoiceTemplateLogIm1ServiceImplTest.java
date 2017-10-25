package com.viettel.bccs.im1.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.im1.dto.InvoiceTemplateLogIm1DTO;
import com.viettel.bccs.im1.dto.PaymentDebitIm1DTO;
import com.viettel.bccs.im1.model.InvoiceTemplateLogIm1;
import com.viettel.bccs.im1.model.PaymentDebitIm1;
import com.viettel.bccs.im1.repo.InvoiceTemplateLogIm1Repo;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
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
@PrepareForTest({DbUtil.class, InvoiceTemplateLogIm1ServiceImpl.class, BaseServiceImpl.class, InvoiceTemplateLogIm1Service.class})
public class InvoiceTemplateLogIm1ServiceImplTest {
    @InjectMocks
    InvoiceTemplateLogIm1ServiceImpl invoiceTemplateLogIm1Service;
    @Mock
    private final BaseMapper<InvoiceTemplateLogIm1, InvoiceTemplateLogIm1DTO> mapper = new BaseMapper<>(InvoiceTemplateLogIm1.class, InvoiceTemplateLogIm1DTO.class);
    @Mock
    private InvoiceTemplateLogIm1Repo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InvoiceTemplateLogIm1ServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws  Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, invoiceTemplateLogIm1Service.count(filters).longValue());
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InvoiceTemplateLogIm1ServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        InvoiceTemplateLogIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateLogIm1Service);
        InvoiceTemplateLogIm1 invoiceTemplateLogIm1 = mock(InvoiceTemplateLogIm1.class);
        InvoiceTemplateLogIm1DTO invoiceTemplateLogIm1DTO = new InvoiceTemplateLogIm1DTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(invoiceTemplateLogIm1);
        setFinalStatic(InvoiceTemplateLogIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(invoiceTemplateLogIm1)).thenReturn(invoiceTemplateLogIm1DTO);
        spyService.findOne(1L);
        Assert.assertNotNull(invoiceTemplateLogIm1DTO);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InvoiceTemplateLogIm1ServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        InvoiceTemplateLogIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateLogIm1Service);
        InvoiceTemplateLogIm1 invoiceTemplateLogIm1 = mock(InvoiceTemplateLogIm1.class);
        InvoiceTemplateLogIm1DTO invoiceTemplateLogIm1DTO = new InvoiceTemplateLogIm1DTO();
        List<InvoiceTemplateLogIm1DTO> invoiceTemplateLogIm1DTOList = Lists.newArrayList(invoiceTemplateLogIm1DTO);
        List<InvoiceTemplateLogIm1> invoiceTemplateLogIm1List = Lists.newArrayList(invoiceTemplateLogIm1);
        Mockito.when(repository.findAll()).thenReturn(invoiceTemplateLogIm1List);
        Mockito.when(mapper.toDtoBean(invoiceTemplateLogIm1List)).thenReturn(invoiceTemplateLogIm1DTOList);
        setFinalStatic(InvoiceTemplateLogIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(invoiceTemplateLogIm1DTOList);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InvoiceTemplateLogIm1ServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        InvoiceTemplateLogIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateLogIm1Service);
        InvoiceTemplateLogIm1 invoiceTemplateLogIm1 = mock(InvoiceTemplateLogIm1.class);
        InvoiceTemplateLogIm1DTO invoiceTemplateLogIm1DTO = new InvoiceTemplateLogIm1DTO();
        List<InvoiceTemplateLogIm1DTO> invoiceTemplateLogIm1DTOList = Lists.newArrayList(invoiceTemplateLogIm1DTO);
        List<InvoiceTemplateLogIm1> invoiceTemplateLogIm1List = Lists.newArrayList(invoiceTemplateLogIm1);
        Mockito.when(mapper.toDtoBean(invoiceTemplateLogIm1List)).thenReturn(invoiceTemplateLogIm1DTOList);
        setFinalStatic(InvoiceTemplateLogIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InvoiceTemplateLogIm1ServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCreate_1() throws  Exception {
        InvoiceTemplateLogIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateLogIm1Service);
        InvoiceTemplateLogIm1 invoiceTemplateLogIm1 = mock(InvoiceTemplateLogIm1.class);
        InvoiceTemplateLogIm1DTO invoiceTemplateLogIm1DTO = new InvoiceTemplateLogIm1DTO();
        setFinalStatic(InvoiceTemplateLogIm1ServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        Mockito.when(mapper.toPersistenceBean(invoiceTemplateLogIm1DTO)).thenReturn(invoiceTemplateLogIm1);
        spyService.create(new InvoiceTemplateLogIm1DTO());
    }

    @Test(expected = Exception.class)
    public void testCreate_2() throws  Exception {
        InvoiceTemplateLogIm1ServiceImpl spyService = PowerMockito.spy( invoiceTemplateLogIm1Service);
        spyService.create(new InvoiceTemplateLogIm1DTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method InvoiceTemplateLogIm1ServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        invoiceTemplateLogIm1Service.update(new InvoiceTemplateLogIm1DTO());
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}