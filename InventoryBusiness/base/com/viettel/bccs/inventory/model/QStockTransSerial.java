package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTransSerial is a Querydsl query type for StockTransSerial
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransSerial extends EntityPathBase<StockTransSerial> {

    private static final long serialVersionUID = -969992424L;

    public static final QStockTransSerial stockTransSerial = new QStockTransSerial("stockTransSerial");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath fromSerial = createString("fromSerial");

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> stockTransDetailId = createNumber("stockTransDetailId", Long.class);

    public final NumberPath<Long> stockTransSerialId = createNumber("stockTransSerialId", Long.class);

    public final StringPath toSerial = createString("toSerial");

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public QStockTransSerial(String variable) {
        super(StockTransSerial.class, forVariable(variable));
    }

    public QStockTransSerial(Path<? extends StockTransSerial> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransSerial(PathMetadata<?> metadata) {
        super(StockTransSerial.class, metadata);
    }

}

