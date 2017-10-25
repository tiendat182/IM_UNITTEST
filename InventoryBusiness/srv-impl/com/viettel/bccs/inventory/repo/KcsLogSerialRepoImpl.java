package com.viettel.bccs.inventory.repo;

import com.viettel.bccs.inventory.common.Const;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Hellpoethero on 06/09/2016.
 */
public class KcsLogSerialRepoImpl implements KcsLogSerialRepoCustom {
    public static final Logger logger = Logger.getLogger(KcsLogSerialRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    /*@Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        return null;
    }*/
}
