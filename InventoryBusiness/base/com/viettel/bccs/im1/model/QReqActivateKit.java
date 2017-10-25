package com.viettel.bccs.im1.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QReqActivateKit is a Querydsl query type for ReqActivateKit
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QReqActivateKit extends EntityPathBase<ReqActivateKit> {

    private static final long serialVersionUID = 1178753510L;

    public static final QReqActivateKit reqActivateKit = new QReqActivateKit("reqActivateKit");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath errorDescription = createString("errorDescription");

    public final StringPath fromSerial = createString("fromSerial");

    public final NumberPath<Long> processCount = createNumber("processCount", Long.class);

    public final DateTimePath<java.util.Date> processDate = createDateTime("processDate", java.util.Date.class);

    public final NumberPath<Long> processStatus = createNumber("processStatus", Long.class);

    public final NumberPath<Long> reqId = createNumber("reqId", Long.class);

    public final DateTimePath<java.util.Date> saleTransDate = createDateTime("saleTransDate", java.util.Date.class);

    public final NumberPath<Long> saleTransId = createNumber("saleTransId", Long.class);

    public final NumberPath<Long> saleTransType = createNumber("saleTransType", Long.class);

    public final NumberPath<Long> saleType = createNumber("saleType", Long.class);

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final StringPath toSerial = createString("toSerial");

    public QReqActivateKit(String variable) {
        super(ReqActivateKit.class, forVariable(variable));
    }

    public QReqActivateKit(Path<? extends ReqActivateKit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReqActivateKit(PathMetadata<?> metadata) {
        super(ReqActivateKit.class, metadata);
    }

}

