package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QApnIp is a Querydsl query type for ApnIp
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QApnIp extends EntityPathBase<ApnIp> {

    private static final long serialVersionUID = -655571564L;

    public static final QApnIp apnIp = new QApnIp("apnIp");

    public final NumberPath<Long> apnId = createNumber("apnId", Long.class);

    public final NumberPath<Long> apnIpId = createNumber("apnIpId", Long.class);

    public final StringPath centerCode = createString("centerCode");

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath ip = createString("ip");

    public final DateTimePath<java.util.Date> lastUpdateTime = createDateTime("lastUpdateTime", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final StringPath subNet = createString("subNet");

    public QApnIp(String variable) {
        super(ApnIp.class, forVariable(variable));
    }

    public QApnIp(Path<? extends ApnIp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QApnIp(PathMetadata<?> metadata) {
        super(ApnIp.class, metadata);
    }

}

