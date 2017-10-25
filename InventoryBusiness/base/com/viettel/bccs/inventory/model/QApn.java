package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QApn is a Querydsl query type for Apn
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QApn extends EntityPathBase<Apn> {

    private static final long serialVersionUID = -644256883L;

    public static final QApn apn = new QApn("apn");

    public final StringPath apnCode = createString("apnCode");

    public final NumberPath<Long> apnId = createNumber("apnId", Long.class);

    public final StringPath apnName = createString("apnName");

    public final StringPath centerCode = createString("centerCode");

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath description = createString("description");

    public final DateTimePath<java.util.Date> lastUpdateTime = createDateTime("lastUpdateTime", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public QApn(String variable) {
        super(Apn.class, forVariable(variable));
    }

    public QApn(Path<? extends Apn> path) {
        super(path.getType(), path.getMetadata());
    }

    public QApn(PathMetadata<?> metadata) {
        super(Apn.class, metadata);
    }

}

