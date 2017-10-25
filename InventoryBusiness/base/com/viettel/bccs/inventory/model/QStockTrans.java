package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTrans is a Querydsl query type for StockTrans
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTrans extends EntityPathBase<StockTrans> {

    private static final long serialVersionUID = 1134510084L;

    public static final QStockTrans stockTrans = new QStockTrans("stockTrans");

    public final StringPath checkErp = createString("checkErp");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath depositStatus = createString("depositStatus");

    public final NumberPath<Long> fromOwnerId = createNumber("fromOwnerId", Long.class);

    public final NumberPath<Long> fromOwnerType = createNumber("fromOwnerType", Long.class);

    public final NumberPath<Long> isAutoGen = createNumber("isAutoGen", Long.class);

    public final StringPath note = createString("note");

    public final StringPath payStatus = createString("payStatus");

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public final NumberPath<Long> regionStockTransId = createNumber("regionStockTransId", Long.class);

    public final NumberPath<Long> requestId = createNumber("requestId", Long.class);

    public final StringPath status = createString("status");

    public final StringPath stockBase = createString("stockBase");

    public final StringPath transport = createString("transport");


    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> toOwnerId = createNumber("toOwnerId", Long.class);

    public final NumberPath<Long> toOwnerType = createNumber("toOwnerType", Long.class);

    public final NumberPath<Long> totalAmount = createNumber("totalAmount", Long.class);

    public QStockTrans(String variable) {
        super(StockTrans.class, forVariable(variable));
    }

    public QStockTrans(Path<? extends StockTrans> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTrans(PathMetadata<?> metadata) {
        super(StockTrans.class, metadata);
    }

}

