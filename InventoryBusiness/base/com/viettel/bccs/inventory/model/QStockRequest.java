package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockRequest is a Querydsl query type for StockRequest
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockRequest extends EntityPathBase<StockRequest> {

    private static final long serialVersionUID = 1505253611L;

    public static final QStockRequest stockRequest = new QStockRequest("stockRequest");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath note = createString("note");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final StringPath requestCode = createString("requestCode");

    public final NumberPath<Long> requestType = createNumber("requestType", Long.class);

    public final StringPath status = createString("status");

    public final NumberPath<Long> stockRequestId = createNumber("stockRequestId", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");

    public QStockRequest(String variable) {
        super(StockRequest.class, forVariable(variable));
    }

    public QStockRequest(Path<? extends StockRequest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockRequest(PathMetadata<?> metadata) {
        super(StockRequest.class, metadata);
    }

}

