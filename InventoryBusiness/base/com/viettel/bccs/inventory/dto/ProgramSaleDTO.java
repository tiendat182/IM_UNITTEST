package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.util.Date;

/**
 * Created by DungPT16 on 1/14/2016.
 */
public class ProgramSaleDTO extends BaseDTO {
    private String code;
    private String name;
    private Long saleProgramId;
    private Date createDatetime;
    private String status;

    public ProgramSaleDTO() {

    }

    public ProgramSaleDTO(String code, String name, Long saleProgramId, Date createDatetime, String status) {
        this.code = code;
        this.name = name;
        this.saleProgramId = saleProgramId;
        this.createDatetime = createDatetime;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSaleProgramId() {
        return saleProgramId;
    }

    public void setSaleProgramId(Long saleProgramId) {
        this.saleProgramId = saleProgramId;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
