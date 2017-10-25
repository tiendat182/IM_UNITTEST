package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QImsiInfo is a Querydsl query type for ImsiInfo
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QImsiInfo extends EntityPathBase<ImsiInfo> {

    private static final long serialVersionUID = 1945514394L;

    public static final QImsiInfo imsiInfo = new QImsiInfo("imsiInfo");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath docNo = createString("docNo");

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imsi = createString("imsi");

    public final DateTimePath<java.util.Date> lastUpdateTime = createDateTime("lastUpdateTime", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final DateTimePath<java.util.Date> startDate = createDateTime("startDate", java.util.Date.class);

    public final StringPath status = createString("status");

    public final StringPath userCreate = createString("userCreate");

    public QImsiInfo(String variable) {
        super(ImsiInfo.class, forVariable(variable));
    }

    public QImsiInfo(Path<? extends ImsiInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QImsiInfo(PathMetadata<?> metadata) {
        super(ImsiInfo.class, metadata);
    }

}

