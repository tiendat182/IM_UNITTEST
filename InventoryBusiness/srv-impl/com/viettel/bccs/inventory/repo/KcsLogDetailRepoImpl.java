package com.viettel.bccs.inventory.repo;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import com.viettel.bccs.inventory.common.Const;
import com.viettel.bccs.inventory.dto.ProductInStockDTO;
import com.viettel.fw.common.util.DataUtil;
import com.viettel.fw.common.util.extjs.FilterRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Hellpoethero on 06/09/2016.
 */
public class KcsLogDetailRepoImpl implements KcsLogDetailRepoCustom {
    public static final Logger logger = Logger.getLogger(KcsLogDetailRepoCustom.class);

    @PersistenceContext(unitName = Const.PERSISTENT_UNIT.BCCS_IM)
    private EntityManager em;

    @Override
    public Predicate toPredicate(List<FilterRequest> filters) {
        return null;
    }
}
