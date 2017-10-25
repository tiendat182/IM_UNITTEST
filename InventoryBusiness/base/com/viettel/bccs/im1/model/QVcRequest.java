package com.viettel.bccs.im1.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QVcRequest is a Querydsl query type for VcRequest
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QVcRequest extends EntityPathBase<VcRequest> {

    private static final long serialVersionUID = 32961025L;

    public static final QVcRequest vcRequest = new QVcRequest("vcRequest");

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final StringPath fromSerial = createString("fromSerial");

    public final DateTimePath<java.util.Date> lastProcessTime = createDateTime("lastProcessTime", java.util.Date.class);

    public final NumberPath<Long> rangeId = createNumber("rangeId", Long.class);

    public final NumberPath<Long> requestId = createNumber("requestId", Long.class);

    public final NumberPath<Long> requestType = createNumber("requestType", Long.class);

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final DateTimePath<java.util.Date> startProcessTime = createDateTime("startProcessTime", java.util.Date.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final DateTimePath<java.util.Date> sysCreateTime = createDateTime("sysCreateTime", java.util.Date.class);

    public final DateTimePath<java.util.Date> sysProcessTime = createDateTime("sysProcessTime", java.util.Date.class);

    public final StringPath toSerial = createString("toSerial");

    public final NumberPath<Long> transId = createNumber("transId", Long.class);

    public final StringPath userId = createString("userId");

    public QVcRequest(String variable) {
        super(VcRequest.class, forVariable(variable));
    }

    public QVcRequest(Path<? extends VcRequest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVcRequest(PathMetadata<?> metadata) {
        super(VcRequest.class, metadata);
    }

}

