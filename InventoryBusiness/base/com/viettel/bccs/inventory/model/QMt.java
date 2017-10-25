package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QMt is a Querydsl query type for Mt
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QMt extends EntityPathBase<Mt> {

    private static final long serialVersionUID = 671954553L;

    public static final QMt mt = new QMt("mt");

    public final StringPath appId = createString("appId");

    public final StringPath appName = createString("appName");

    public final StringPath channel = createString("channel");

    public final StringPath message = createString("message");

    public final NumberPath<Long> moHisId = createNumber("moHisId", Long.class);

    public final StringPath msisdn = createString("msisdn");

    public final NumberPath<Long> mtId = createNumber("mtId", Long.class);

    public final DateTimePath<java.util.Date> receiveTime = createDateTime("receiveTime", java.util.Date.class);

    public final NumberPath<Long> retryNum = createNumber("retryNum", Long.class);

    public QMt(String variable) {
        super(Mt.class, forVariable(variable));
    }

    public QMt(Path<? extends Mt> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMt(PathMetadata<?> metadata) {
        super(Mt.class, metadata);
    }

}

