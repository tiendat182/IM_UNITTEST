package com.viettel.bccs.inventory.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author luannt23.
 * @comment
 * @date 3/7/2016.
 */
public class ReportDTO {

    private Date icReportDate;
    private String type;
    private String jasperFilePath;
    private String header;
    private String footer;
    private String format;
    private HashMap<String, Object> params;
    private List lstParammerter;

    public ReportDTO() {

    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Date getIcReportDate() {
        return icReportDate;
    }

    public void setIcReportDate(Date icReportDate) {
        this.icReportDate = icReportDate;
    }

    //
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getJasperFilePath() {
        return jasperFilePath;
    }

    public void setJasperFilePath(String jasperFilePath) {
        this.jasperFilePath = jasperFilePath;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    public List getLstParammerter() {
        return lstParammerter;
    }

    public void setLstParammerter(List lstParammerter) {
        this.lstParammerter = lstParammerter;
    }
}
