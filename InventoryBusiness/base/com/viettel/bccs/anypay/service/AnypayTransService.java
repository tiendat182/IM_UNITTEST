package com.viettel.bccs.anypay.service;

import com.viettel.anypay.database.BO.AnypayMsg;
import com.viettel.bccs.anypay.dto.AgentDTO;

import java.util.Date;

/**
 * Created by PM2-LAMNV5 on 2/2/2016.
 */
public interface AnypayTransService {
    /**
     * Xly giao dich ban Anypay
     *
     * @param saleTransId
     * @param stockSmId
     * @param agentId
     * @param amount
     * @param date
     * @param userName
     * @param host
     * @return
     * @throws Exception
     */
    public AnypayMsg saleAnypay(Long saleTransId, Long stockSmId, Long agentId, Long amount, Date date, String userName, String host) throws Exception;

    public AgentDTO findAgentById(Long agentId);

    public boolean findTransAnypay(Long transId) throws Exception;
}
