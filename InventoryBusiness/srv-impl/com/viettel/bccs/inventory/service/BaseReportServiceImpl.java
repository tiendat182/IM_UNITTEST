package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ReportDTO;
import com.viettel.fw.service.BaseServiceImpl;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.type.OrientationEnum;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * @author luannt23.
 * @comment
 * @date 3/7/2016.
 */
@Service
public class BaseReportServiceImpl extends BaseServiceImpl implements BaseReportService {
    Logger logger = Logger.getLogger(BaseReportService.class);

    @Override
    public byte[] exportWithDataSource(ReportDTO reportDTO, JRBeanCollectionDataSource dataSource) {
        try {
            reportDTO.getParams().put(Const.REPORT_PARAMS.DATASOURCE.toString(), dataSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportDTO.getJasperFilePath(), reportDTO.getParams(), new JREmptyDataSource());
            jasperPrint.setOrientation(OrientationEnum.PORTRAIT);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }
}
