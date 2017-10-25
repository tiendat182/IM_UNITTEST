package com.viettel.bccs.inventory.dto;

/**
 * Created by hoangnt14 on 12/3/2015.
 */
public class DomainDTO {
    private Long id;
    private String domain;
    private String description;
    private String status;
    private String domainUpper;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDomainUpper() {
        return domainUpper;
    }

    public void setDomainUpper(String domainUpper) {
        this.domainUpper = domainUpper;
    }
}
