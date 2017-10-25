package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QFinanceType is a Querydsl query type for FinanceType
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QFinanceType extends EntityPathBase<FinanceType> {

    private static final long serialVersionUID = 1963686306L;

    public static final QFinanceType financeType1 = new QFinanceType("financeType1");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> dayNum = createNumber("dayNum", Long.class);

    public final StringPath financeType = createString("financeType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> lastUpdateTime = createDateTime("lastUpdateTime", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final StringPath status = createString("status");

    public QFinanceType(String variable) {
        super(FinanceType.class, forVariable(variable));
    }

    public QFinanceType(Path<? extends FinanceType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFinanceType(PathMetadata<?> metadata) {
        super(FinanceType.class, metadata);
    }

}

