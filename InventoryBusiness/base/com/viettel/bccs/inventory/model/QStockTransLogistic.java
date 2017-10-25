package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTransLogistic is a Querydsl query type for StockTransLogistic
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransLogistic extends EntityPathBase<StockTransLogistic> {

    private static final long serialVersionUID = 501142148L;

    public static final QStockTransLogistic stockTransLogistic = new QStockTransLogistic("stockTransLogistic");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath logisticDescription = createString("logisticDescription");

    public final StringPath logisticResponseCode = createString("logisticResponseCode");

    public final NumberPath<Long> logisticRetry = createNumber("logisticRetry", Long.class);

    public final StringPath printUserList = createString("printUserList");

    public final StringPath rejectNoteDescription = createString("rejectNoteDescription");

    public final StringPath rejectNoteResponse = createString("rejectNoteResponse");

    public final NumberPath<Long> requestTypeLogistic = createNumber("requestTypeLogistic", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> stockTransLogisticId = createNumber("stockTransLogisticId", Long.class);

    public final NumberPath<Long> stockTransType = createNumber("stockTransType", Long.class);

    public final NumberPath<Long> updateLogistic = createNumber("updateLogistic", Long.class);

    public QStockTransLogistic(String variable) {
        super(StockTransLogistic.class, forVariable(variable));
    }

    public QStockTransLogistic(Path<? extends StockTransLogistic> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransLogistic(PathMetadata<?> metadata) {
        super(StockTransLogistic.class, metadata);
    }

}

