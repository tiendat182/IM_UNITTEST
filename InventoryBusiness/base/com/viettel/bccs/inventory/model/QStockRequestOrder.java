package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockRequestOrder is a Querydsl query type for StockRequestOrder
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QStockRequestOrder extends EntityPathBase<StockRequestOrder> {

    private static final long serialVersionUID = -1185933021L;

    public static final QStockRequestOrder stockRequestOrder = new QStockRequestOrder("stockRequestOrder");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath description = createString("description");

    public final StringPath orderCode = createString("orderCode");

    public final NumberPath<Long> orderType = createNumber("orderType", Long.class);

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final StringPath status = createString("status");

    public final NumberPath<Long> stockRequestOrderId = createNumber("stockRequestOrderId", Long.class);

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");

    public QStockRequestOrder(String variable) {
        super(StockRequestOrder.class, forVariable(variable));
    }

    public QStockRequestOrder(Path<? extends StockRequestOrder> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockRequestOrder(PathMetadata<?> metadata) {
        super(StockRequestOrder.class, metadata);
    }

}

