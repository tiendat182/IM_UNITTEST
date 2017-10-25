package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.dto.StockSimDTO;
import com.viettel.fw.common.util.extjs.FilterRequest;

import java.util.List;

public interface StockSimRepoCustom {
    public Predicate toPredicate(List<FilterRequest> filters);

    public String findSerialSimBySub(String msisdn) throws Exception;

    public StockSimDTO findStockSim(String serial) throws Exception;

    public boolean isCaSim(String serial);

    boolean checkSimElegibleExists(String fromSerial, String toSerial) throws Exception;

    boolean checkSimElegibleExistsIm1(String fromSerial, String toSerial) throws Exception;
}