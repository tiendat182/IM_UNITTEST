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
 * QBrasIppool is a Querydsl query type for BrasIppool
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QBrasIppool extends EntityPathBase<BrasIppool> {

    private static final long serialVersionUID = 1238251447L;

    public static final QBrasIppool brasIppool = new QBrasIppool("brasIppool");

    public final NumberPath<Long> brasIp = createNumber("brasIp", Long.class);

    public final StringPath center = createString("center");

    public final NumberPath<Long> domain = createNumber("domain", Long.class);

    public final StringPath ipLabel = createString("ipLabel");

    public final StringPath ipMask = createString("ipMask");

    public final StringPath ipPool = createString("ipPool");

    public final StringPath ipType = createString("ipType");

    public final NumberPath<Long> poolId = createNumber("poolId", Long.class);

    public final StringPath poolName = createString("poolName");

    public final StringPath poolUniq = createString("poolUniq");

    public final StringPath province = createString("province");

    public final StringPath status = createString("status");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");

    public QBrasIppool(String variable) {
        super(BrasIppool.class, forVariable(variable));
    }

    public QBrasIppool(Path<? extends BrasIppool> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBrasIppool(PathMetadata<?> metadata) {
        super(BrasIppool.class, metadata);
    }

}

