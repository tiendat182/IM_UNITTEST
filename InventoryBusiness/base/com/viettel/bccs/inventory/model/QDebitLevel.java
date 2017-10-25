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
 * QDebitLevel is a Querydsl query type for DebitLevel
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDebitLevel extends EntityPathBase<DebitLevel> {

    private static final long serialVersionUID = 1227522250L;

    public static final QDebitLevel debitLevel1 = new QDebitLevel("debitLevel1");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> debitAmount = createNumber("debitAmount", Long.class);

    public final StringPath debitDayType = createString("debitDayType");

    public final StringPath debitLevel = createString("debitLevel");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> lastUpdateTime = createDateTime("lastUpdateTime", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final StringPath status = createString("status");

    public QDebitLevel(String variable) {
        super(DebitLevel.class, forVariable(variable));
    }

    public QDebitLevel(Path<? extends DebitLevel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDebitLevel(PathMetadata<?> metadata) {
        super(DebitLevel.class, metadata);
    }

}

