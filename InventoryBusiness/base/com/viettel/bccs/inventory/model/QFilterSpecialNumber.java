package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QFilterSpecialNumber is a Querydsl query type for FilterSpecialNumber
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QFilterSpecialNumber extends EntityPathBase<FilterSpecialNumber> {

    private static final long serialVersionUID = 679081528L;

    public static final QFilterSpecialNumber filterSpecialNumber = new QFilterSpecialNumber("filterSpecialNumber");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final NumberPath<Long> filterRuleId = createNumber("filterRuleId", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> filterSpecialNumberId = createNumber("filterSpecialNumberId", Long.class);

    public final StringPath isdn = createString("isdn");

    public final ComparablePath<Character> status = createComparable("status", Character.class);

    public QFilterSpecialNumber(String variable) {
        super(FilterSpecialNumber.class, forVariable(variable));
    }

    public QFilterSpecialNumber(Path<? extends FilterSpecialNumber> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFilterSpecialNumber(PathMetadata<?> metadata) {
        super(FilterSpecialNumber.class, metadata);
    }

}

