package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.OptionSetDTO;
import com.viettel.bccs.inventory.dto.OptionSetValueDTO;
import com.viettel.bccs.inventory.model.OptionSet;
import com.viettel.bccs.inventory.model.OptionSetValue;
import com.viettel.bccs.inventory.repo.OptionSetRepo;
import com.viettel.bccs.inventory.repo.OptionSetValueRepo;
import com.viettel.fw.common.util.extjs.FilterRequest;
import com.viettel.fw.common.util.mapper.BaseMapper;
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
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DatLT
 * @date 04/10/2017
 */
@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({OptionSetServiceImpl.class, BaseServiceImpl.class, OptionSetService.class})
public class OptionSetServiceImplTest {

    @InjectMocks
    OptionSetServiceImpl optionSetService;

    @Mock
    private final BaseMapper<OptionSet, OptionSetDTO> mapper = new BaseMapper(OptionSet.class, OptionSetDTO.class);

    @Mock
    private final BaseMapper<OptionSetValue, OptionSetValueDTO> optionSetValueMapper = new BaseMapper(OptionSetValue.class, OptionSetValueDTO.class);

    @Mock
    private OptionSetRepo repository;

    @Mock
    private OptionSetValueRepo optionSetValueRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    /** ------------------------------------------------------------------------------ **/
    /** --- Test for optionSetService.findByFilter method ------- **/
    /*** -----------------------------------------------------------------------------**/
    @Test
    public void testFindByFilter_1() {
        FilterRequest filterRequest = new FilterRequest();
        List<FilterRequest> filterRequestList = Lists.newArrayList(filterRequest);
        OptionSet optionSet = new OptionSet();
        List<OptionSet> optionSetList = Lists.newArrayList(optionSet);
        List<OptionSetDTO> optionSetDTOList = new ArrayList<>();
        Mockito.when(mapper.toDtoBean(optionSetList)).thenReturn(optionSetDTOList);
        optionSetService.findByFilter(filterRequestList);
    }

    /** ------------------------------------------------------------------------------ **/
    /** --- Test for optionSetService.getRequestListByCondition method ------- **/
    /*** -----------------------------------------------------------------------------**/
    @Test
    public void testGetRequestListByCondition_1() {
        OptionSetDTO optionSetDTO = new OptionSetDTO();
        optionSetDTO.setName("NAME");
        optionSetDTO.setCode("CODE");
        optionSetDTO.setStatus("STATUS");
        optionSetService.getRequestListByCondition(optionSetDTO);
    }

    /** ------------------------------------------------------------------------------ **/
    /** --- Test for optionSetService.findById method ------- **/
    /*** -----------------------------------------------------------------------------**/
    @Test
    public void testFindById_1() {
        Long optionSetId = 1L;
        OptionSet optionSet = new OptionSet();
        OptionSetDTO optionSetDto = new OptionSetDTO();
        optionSetDto.setStatus("status");
        OptionSetValue optionSetValue = new OptionSetValue();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        List<OptionSetValue> optionSetValueList = Lists.newArrayList(optionSetValue);
        List<OptionSetValueDTO> optionSetDTOList = Lists.newArrayList(optionSetValueDTO);
        Mockito.when(repository.findOne(optionSetId)).thenReturn(optionSet);
        Mockito.when(mapper.toDtoBean(optionSet)).thenReturn(optionSetDto);
        Mockito.when(optionSetValueMapper.toDtoBean(optionSetValueList)).thenReturn(optionSetDTOList);
        optionSetService.findById(optionSetId);
    }

    @Test
    public void testFindById_2() {
        Long optionSetId = 1L;
        OptionSet optionSet = new OptionSet();
        OptionSetValue optionSetValue = new OptionSetValue();
        OptionSetValueDTO optionSetValueDTO = new OptionSetValueDTO();
        List<OptionSetValue> optionSetValueList = Lists.newArrayList(optionSetValue);
        List<OptionSetValueDTO> optionSetDTOList = Lists.newArrayList(optionSetValueDTO);
        Mockito.when(mapper.toDtoBean(optionSet)).thenReturn(null);
        Mockito.when(optionSetValueMapper.toDtoBean(optionSetValueList)).thenReturn(optionSetDTOList);
        optionSetService.findById(optionSetId);
    }
}