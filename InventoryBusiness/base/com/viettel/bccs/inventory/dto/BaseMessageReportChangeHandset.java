package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseMessage;

/**
 * Created by DungPT16 on 3/19/2016.
 */
public class BaseMessageReportChangeHandset extends BaseMessage {

    private ReportChangeHandsetDTO reportChangeHandsetDTO;



    public ReportChangeHandsetDTO getReportChangeHandsetDTO() {
        return reportChangeHandsetDTO;
    }

    public void setReportChangeHandsetDTO(ReportChangeHandsetDTO reportChangeHandsetDTO) {
        this.reportChangeHandsetDTO = reportChangeHandsetDTO;
    }
}
