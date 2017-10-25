package com.viettel.bccs.anypay.service;

import com.viettel.anypay.database.BO.AnypayMsg;
import com.viettel.bccs.anypay.dto.AgentDTO;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.DataUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by PM2-LAMNV5 on 2/2/2016.
 */
@Service
public class AnypayTransServiceImpl implements AnypayTransService {
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.ANYPAY)
    EntityManager em;

    @Override
    public AgentDTO findAgentById(Long agentId) {
        AgentDTO agentDTO = null;
        String sqlQuery = "select AGENT_ID, MSISDN, ICCID, TRADE_NAME, STATUS from anypay_owner.Agent where agent_id= #agentId";
        Query e = this.em.createNativeQuery(sqlQuery);
        e.setParameter("agentId", agentId);
        List lst = e.getResultList();
        if (lst != null && !lst.isEmpty()) {
            Object[] arrObj = (Object[]) lst.get(0);
            agentDTO = new AgentDTO();
            agentDTO.setAgentId(agentId);
            int index = 0;
            agentDTO.setStaffStkId(DataUtil.safeToLong(arrObj[index++], null));
            agentDTO.setMsisdn(DataUtil.safeToString(arrObj[index++], null));
            agentDTO.setIccid(DataUtil.safeToString(arrObj[index++], null));
            agentDTO.setTradeName(DataUtil.safeToString(arrObj[index++], null));
            agentDTO.setStatus(DataUtil.safeToLong(arrObj[index], null));
        }

        return agentDTO;
    }

    @Override
    public boolean findTransAnypay(Long transId) throws Exception {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("SELECT     trans_id");
        strQuery.append("               FROM anypay_owner.trans_anypay");
        strQuery.append("              WHERE trans_id = #transId");
        strQuery.append("                AND trans_type = 'SALE'");
        strQuery.append("                AND process_status = 3");
        Query e = this.em.createNativeQuery(strQuery.toString());
        e.setParameter("transId", transId);
        List lst = e.getResultList();
        if (!DataUtil.isNullOrEmpty(lst)) {
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AnypayMsg saleAnypay(Long saleTransId, Long stockSmId, Long agentId, Long amount, Date date, String userName, String host) throws Exception {
        String ex = "anypay_owner.AIR_SALE_ANYPAY_PKG.proc_sale_anypay_from_sm";
        StoredProcedureQuery procedure = em.createStoredProcedureQuery(ex);

        procedure.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);//"ANY"
        procedure.registerStoredProcedureParameter(2, Long.class, ParameterMode.IN);//saleTransId
        procedure.registerStoredProcedureParameter(3, Long.class, ParameterMode.IN);//stockSmId
        procedure.registerStoredProcedureParameter(4, Long.class, ParameterMode.IN);//agentId
        procedure.registerStoredProcedureParameter(5, Long.class, ParameterMode.IN);//amount
        procedure.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);//null
        procedure.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);//user
        procedure.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);//host
        procedure.registerStoredProcedureParameter(9, String.class, ParameterMode.OUT);//errCode
        procedure.registerStoredProcedureParameter(10, String.class, ParameterMode.OUT);//errMsg
        procedure.registerStoredProcedureParameter(11, Long.class, ParameterMode.OUT);//transAnypayId

        procedure.setParameter(1, "ANY");
        procedure.setParameter(2, saleTransId);
        procedure.setParameter(3, stockSmId);
        procedure.setParameter(4, agentId);
        procedure.setParameter(5, amount);
        procedure.setParameter(6, "");
        procedure.setParameter(7, userName);
        procedure.setParameter(8, DataUtil.safeToString(host, ""));
        procedure.execute();
        String errCode = (String) procedure.getOutputParameterValue(9);
        String errMsg = (String) procedure.getOutputParameterValue(10);
        Long transAnypayId = DataUtil.safeToLong(procedure.getOutputParameterValue(11), null);
        AnypayMsg msg = new AnypayMsg(errCode, errMsg, transAnypayId);

        return msg;
    }
}
