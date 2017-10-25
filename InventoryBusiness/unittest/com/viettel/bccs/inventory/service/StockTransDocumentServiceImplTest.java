package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.StockTransDocumentDTO;
import com.viettel.bccs.inventory.model.StockTransDocument;
import com.viettel.bccs.inventory.repo.StockTransDocumentRepo;
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
 * @date 22/10/2017
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({DbUtil.class, BaseServiceImpl.class, StockTransDocumentService.class})
public class StockTransDocumentServiceImplTest {
    @InjectMocks
    StockTransDocumentServiceImpl stockTransDocumentService;
    @Mock
    private final BaseMapper<StockTransDocument, StockTransDocumentDTO> mapper = new BaseMapper<>(StockTransDocument.class, StockTransDocumentDTO.class);
    @Mock
    private StockTransDocumentRepo repository;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDocumentServiceImpl.count
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testCount_1() throws Exception {
        List<FilterRequest> filters = new ArrayList<>();
        when(repository.count(any())).thenReturn(1L);
        Assert.assertEquals(1L, stockTransDocumentService.count(filters).longValue());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDocumentServiceImpl.findOne
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindOne_1() throws  Exception {
        StockTransDocumentServiceImpl spyService = PowerMockito.spy( stockTransDocumentService);
        StockTransDocument stockTransDocument = mock(StockTransDocument.class);
        StockTransDocumentDTO stockTransDocumentDTO = new StockTransDocumentDTO();
        Mockito.when(repository.findOne(anyLong())).thenReturn(stockTransDocument);
        setFinalStatic(StockTransDocumentServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        when(mapper.toDtoBean(stockTransDocument)).thenReturn(stockTransDocumentDTO);
        spyService.findOne(1L);
        Assert.assertNotNull(stockTransDocumentDTO);
    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDocumentServiceImpl.findAll
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindAll_1() throws  Exception {
        StockTransDocumentServiceImpl spyService = PowerMockito.spy( stockTransDocumentService);
        StockTransDocument stockTransDocument = mock(StockTransDocument.class);
        StockTransDocumentDTO stockTransDocumentDTO = new StockTransDocumentDTO();
        List<StockTransDocument> stockTransDocumentList = Lists.newArrayList(stockTransDocument);
        List<StockTransDocumentDTO> stockTransDocumentDTOList = Lists.newArrayList(stockTransDocumentDTO);
        Mockito.when(repository.findAll()).thenReturn(stockTransDocumentList);
        Mockito.when(mapper.toDtoBean(stockTransDocumentList)).thenReturn(stockTransDocumentDTOList);
        setFinalStatic(StockTransDocumentServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findAll();
        Assert.assertNotNull(stockTransDocumentDTOList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDocumentServiceImpl.findByFilter
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testFindByFilter_1() throws  Exception {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        StockTransDocumentServiceImpl spyService = PowerMockito.spy( stockTransDocumentService);
        StockTransDocument stockTransDocument = mock(StockTransDocument.class);
        StockTransDocumentDTO stockTransDocumentDTO = new StockTransDocumentDTO();
        List<StockTransDocument> stockTransDocumentList = Lists.newArrayList(stockTransDocument);
        List<StockTransDocumentDTO> stockTransDocumentDTOList = Lists.newArrayList(stockTransDocumentDTO);
        Mockito.when(mapper.toDtoBean(stockTransDocumentList)).thenReturn(stockTransDocumentDTOList);
        setFinalStatic(StockTransDocumentServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.findByFilter(filterRequestList);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDocumentServiceImpl.create
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testCreate_1() throws  Exception {
        stockTransDocumentService.create(new StockTransDocumentDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDocumentServiceImpl.update
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void testUpdate_1() throws  Exception {
        stockTransDocumentService.update(new StockTransDocumentDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDocumentServiceImpl.save
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testSave_1() throws  Exception {
        StockTransDocumentServiceImpl spyService = PowerMockito.spy( stockTransDocumentService);
        StockTransDocument stockTransDocument = mock(StockTransDocument.class);
        StockTransDocumentDTO stockTransDocumentDTO = new StockTransDocumentDTO();
        List<StockTransDocument> stockTransDocumentList = Lists.newArrayList(stockTransDocument);
        List<StockTransDocumentDTO> stockTransDocumentDTOList = Lists.newArrayList(stockTransDocumentDTO);
        Mockito.when(mapper.toDtoBean(stockTransDocumentList)).thenReturn(stockTransDocumentDTOList);
        Mockito.when(mapper.toPersistenceBean(stockTransDocumentDTO)).thenReturn(stockTransDocument);
        setFinalStatic(StockTransDocumentServiceImpl.class.getDeclaredField("mapper"), mapper, spyService);
        spyService.save(new StockTransDocumentDTO());
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method StockTransDocumentServiceImpl.getAttachFileContent
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testGetAttachFileContent_1() throws  Exception {
        stockTransDocumentService.getAttachFileContent(1L);
    }

    static void setFinalStatic(Field field, Object newValue, final Object targetObject) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(targetObject, newValue);
    }
}