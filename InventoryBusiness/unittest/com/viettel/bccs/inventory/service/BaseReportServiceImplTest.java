package com.viettel.bccs.inventory.service;

import com.google.common.collect.Lists;
import com.viettel.bccs.inventory.dto.ReportDTO;
import com.viettel.fw.service.BaseServiceImpl;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.HashMap;
import java.util.List;
import static org.mockito.Mockito.*;

/**
 * @author DatLT
 * @date 9/25/2017
 */
@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({BaseServiceImpl.class, BaseReportServiceImpl.class, BaseReportService.class, JasperFillManager.class})
public class BaseReportServiceImplTest {
    @InjectMocks
    BaseReportServiceImpl baseReportService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
    /**
     * -----------------------------------------------------------------------------------------------------------------
     * Test for method BaseReportServiceImpl.exportWithDataSource
     * -----------------------------------------------------------------------------------------------------------------
     *
     * @throws Exception
     */
    @Test
    public void testExportWithDataSource()  throws  Exception {
        ReportDTO reportDTO = new ReportDTO();
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("TITLE", "Lorem Ipsum Szalek");
        reportDTO.setParams(parameters);
        reportDTO.setJasperFilePath("/home/datlt/dev/Projects/shopReports/templates/test.jrxml");
        List<String> stringList = Lists.newArrayList();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stringList);
        baseReportService.exportWithDataSource(reportDTO, dataSource);
    }
}