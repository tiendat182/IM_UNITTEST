package com.viettel.bccs.inventory.dto;

import com.viettel.fw.dto.BaseDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luannt23.
 * @comment
 * @date 2/20/2016.
 */
public class ProfileDTO extends BaseDTO implements Serializable {
    private Long id;
    private String name;
    private List<String> lstColumnName = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLstColumnName() {
        return lstColumnName;
    }

    public void setLstColumnName(List<String> lstColumnName) {
        this.lstColumnName = lstColumnName;
    }

}
