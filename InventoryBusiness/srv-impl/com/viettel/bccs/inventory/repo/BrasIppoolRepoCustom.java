package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.BrasIppoolDTO;
import com.viettel.bccs.inventory.dto.DomainDTO;
import com.viettel.bccs.inventory.model.BrasIppool;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface BrasIppoolRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public List<DomainDTO> getAllDomain() throws Exception;

    public List<BrasIppoolDTO> search(BrasIppoolDTO brasIppoolDTO) throws Exception;

    public List<String> getListStaticIpProvince(String domain, String province, Long ipType, Long status, Long specialType) throws Exception;

    public void insertIppoolProvince(Long poolId, String province) throws Exception;

    public int deleteIpPool(Long poolId) throws Exception;

    public BrasIppoolDTO checkIpHaveUsedCM(String ip) throws Exception;

    public boolean checkIpAssignSubsubcriber(String ip) throws Exception;
}