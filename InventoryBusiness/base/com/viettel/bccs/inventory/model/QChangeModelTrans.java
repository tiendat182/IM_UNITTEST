package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;

import javax.annotation.Generated;

import com.mysema.query.types.Path;


/**
 * QChangeModelTrans is a Querydsl query type for ChangeModelTrans
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QChangeModelTrans extends EntityPathBase<ChangeModelTrans> {

    private static final long serialVersionUID = 606666273L;

    public static final QChangeModelTrans changeModelTrans = new QChangeModelTrans("changeModelTrans");

    public final DateTimePath<java.util.Date> approveDate = createDateTime("approveDate", java.util.Date.class);

    public final NumberPath<Long> approveUserId = createNumber("approveUserId", Long.class);

    public final NumberPath<Long> changeModelTransId = createNumber("changeModelTransId", Long.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> createUserId = createNumber("createUserId", Long.class);

    public final NumberPath<Long> fromOwnerId = createNumber("fromOwnerId", Long.class);

    public final NumberPath<Long> fromOwnerType = createNumber("fromOwnerType", Long.class);

    public final StringPath listStockTransId = createString("listStockTransId");

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> toOwnerId = createNumber("toOwnerId", Long.class);

    public final NumberPath<Long> toOwnerType = createNumber("toOwnerType", Long.class);

    public final NumberPath<Long> requestType = createNumber("requestType", Long.class);

    public QChangeModelTrans(String variable) {
        super(ChangeModelTrans.class, forVariable(variable));
    }

    public QChangeModelTrans(Path<? extends ChangeModelTrans> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChangeModelTrans(PathMetadata<?> metadata) {
        super(ChangeModelTrans.class, metadata);
    }

}

