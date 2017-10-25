package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QDebitDayType is a Querydsl query type for DebitDayType
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDebitDayType extends EntityPathBase<DebitDayType> {

    private static final long serialVersionUID = -89455428L;

    public static final QDebitDayType debitDayType1 = new QDebitDayType("debitDayType1");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath debitDayType = createString("debitDayType");

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    public final SimplePath<java.io.Serializable> fileContent = createSimple("fileContent", java.io.Serializable.class);

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> lastUpdateTime = createDateTime("lastUpdateTime", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final DateTimePath<java.util.Date> startDate = createDateTime("startDate", java.util.Date.class);

    public final StringPath status = createString("status");

    public QDebitDayType(String variable) {
        super(DebitDayType.class, forVariable(variable));
    }

    public QDebitDayType(Path<? extends DebitDayType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDebitDayType(PathMetadata<?> metadata) {
        super(DebitDayType.class, metadata);
    }

}

