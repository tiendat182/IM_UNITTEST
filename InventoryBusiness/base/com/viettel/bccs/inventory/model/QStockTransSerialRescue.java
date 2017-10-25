package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTransSerialRescue is a Querydsl query type for StockTransSerialRescue
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransSerialRescue extends EntityPathBase<StockTransSerialRescue> {

    private static final long serialVersionUID = 1569314507L;

    public static final QStockTransSerialRescue stockTransSerialRescue = new QStockTransSerialRescue("stockTransSerialRescue");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath fromSerial = createString("fromSerial");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> stockTransSerialId = createNumber("stockTransSerialId", Long.class);

    public final StringPath toSerial = createString("toSerial");

    public QStockTransSerialRescue(String variable) {
        super(StockTransSerialRescue.class, forVariable(variable));
    }

    public QStockTransSerialRescue(Path<? extends StockTransSerialRescue> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransSerialRescue(PathMetadata<?> metadata) {
        super(StockTransSerialRescue.class, metadata);
    }

}

