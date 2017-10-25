package com.viettel.bccs.inventory.service;

import com.viettel.bccs.inventory.dto.ReportDTO;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

/**
 * @author luannt23.
 * @comment
 * @date 3/7/2016.
 */
@Service
public interface BaseReportService {
    public byte[] exportWithDataSource(ReportDTO reportDTO, JRBeanCollectionDataSource dataSource);
}
