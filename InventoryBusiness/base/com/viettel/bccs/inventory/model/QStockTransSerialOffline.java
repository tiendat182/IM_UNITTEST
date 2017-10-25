package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTransSerialOffline is a Querydsl query type for StockTransSerialOffline
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransSerialOffline extends EntityPathBase<StockTransSerialOffline> {

    private static final long serialVersionUID = -1241521237L;

    public static final QStockTransSerialOffline stockTransSerialOffline = new QStockTransSerialOffline("stockTransSerialOffline");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final DateTimePath<java.util.Date> demoDate = createDateTime("demoDate", java.util.Date.class);

    public final StringPath fromSerial = createString("fromSerial");

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> stockModelId = createNumber("stockModelId", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> stockTransSerialId = createNumber("stockTransSerialId", Long.class);

    public final StringPath toSerial = createString("toSerial");

    public QStockTransSerialOffline(String variable) {
        super(StockTransSerialOffline.class, forVariable(variable));
    }

    public QStockTransSerialOffline(Path<? extends StockTransSerialOffline> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransSerialOffline(PathMetadata<?> metadata) {
        super(StockTransSerialOffline.class, metadata);
    }

}

