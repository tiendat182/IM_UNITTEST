package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockBalanceSerial is a Querydsl query type for StockBalanceSerial
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockBalanceSerial extends EntityPathBase<StockBalanceSerial> {

    private static final long serialVersionUID = 276952396L;

    public static final QStockBalanceSerial stockBalanceSerial = new QStockBalanceSerial("stockBalanceSerial");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath fromSerial = createString("fromSerial");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> stockBalanceDetail = createNumber("stockBalanceDetail", Long.class);

    public final NumberPath<Long> stockBalanceSerialId = createNumber("stockBalanceSerialId", Long.class);

    public final NumberPath<Long> stockRequestId = createNumber("stockRequestId", Long.class);

    public final StringPath toSerial = createString("toSerial");

    public QStockBalanceSerial(String variable) {
        super(StockBalanceSerial.class, forVariable(variable));
    }

    public QStockBalanceSerial(Path<? extends StockBalanceSerial> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockBalanceSerial(PathMetadata<?> metadata) {
        super(StockBalanceSerial.class, metadata);
    }

}

