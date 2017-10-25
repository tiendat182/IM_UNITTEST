package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;

import javax.annotation.Generated;

import com.mysema.query.types.Path;


/**
 * QStockRequestOrderTrans is a Querydsl query type for StockRequestOrderTrans
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockRequestOrderTrans extends EntityPathBase<StockRequestOrderTrans> {

    private static final long serialVersionUID = -2107234491L;

    public static final QStockRequestOrderTrans stockRequestOrderTrans = new QStockRequestOrderTrans("stockRequestOrderTrans");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath errorCode = createString("errorCode");

    public final StringPath errorCodeDescription = createString("errorCodeDescription");

    public final NumberPath<Long> fromOwnerId = createNumber("fromOwnerId", Long.class);

    public final NumberPath<Long> fromOwnerType = createNumber("fromOwnerType", Long.class);

    public final NumberPath<Long> retry = createNumber("retry", Long.class);

    public final NumberPath<Long> type = createNumber("type", Long.class);

    public final NumberPath<Long> stockRequestOrderId = createNumber("stockRequestOrderId", Long.class);

    public final NumberPath<Long> stockRequestOrderTransId = createNumber("stockRequestOrderTransId", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> toOwnerId = createNumber("toOwnerId", Long.class);

    public final NumberPath<Long> toOwnerType = createNumber("toOwnerType", Long.class);

    public final NumberPath<Long> stockTransType = createNumber("stockTransType", Long.class);

    public QStockRequestOrderTrans(String variable) {
        super(StockRequestOrderTrans.class, forVariable(variable));
    }

    public QStockRequestOrderTrans(Path<? extends StockRequestOrderTrans> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockRequestOrderTrans(PathMetadata<?> metadata) {
        super(StockRequestOrderTrans.class, metadata);
    }

}

