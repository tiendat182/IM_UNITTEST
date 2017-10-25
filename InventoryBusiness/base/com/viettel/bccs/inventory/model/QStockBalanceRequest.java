package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockBalanceRequest is a Querydsl query type for StockBalanceRequest
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockBalanceRequest extends EntityPathBase<StockBalanceRequest> {

    private static final long serialVersionUID = -892475849L;

    public static final QStockBalanceRequest stockBalanceRequest = new QStockBalanceRequest("stockBalanceRequest");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath listEmailSign = createString("listEmailSign");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> stockRequestId = createNumber("stockRequestId", Long.class);

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");

    public QStockBalanceRequest(String variable) {
        super(StockBalanceRequest.class, forVariable(variable));
    }

    public QStockBalanceRequest(Path<? extends StockBalanceRequest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockBalanceRequest(PathMetadata<?> metadata) {
        super(StockBalanceRequest.class, metadata);
    }

}

