package com.viettel.bccs.inventory.repo;

import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Hellpoethero on 29/08/2016.
 */
public class LogUnlockUserInfoRepoImpl implements LogUnlockUserInfoRepoCustom {

    public final static Logger logger = Logger.getLogger(LogUnlockUserInfoRepoCustom.class);
    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager emIM;

    /*@Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        return null;
    }*/

}
