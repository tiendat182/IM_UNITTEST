package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;

import javax.annotation.Generated;

import com.mysema.query.types.Path;


/**
 * QStockInspectReal is a Querydsl query type for StockInspectReal
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockInspectReal extends EntityPathBase<StockInspectReal> {

    private static final long serialVersionUID = 54713902L;

    public static final QStockInspectReal stockInspectReal = new QStockInspectReal("stockInspectReal");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath fromSerial = createString("fromSerial");

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> stockInspectId = createNumber("stockInspectId", Long.class);

    public final NumberPath<Long> stockInspectRealId = createNumber("stockInspectRealId", Long.class);

    public final StringPath toSerial = createString("toSerial");

    public QStockInspectReal(String variable) {
        super(StockInspectReal.class, forVariable(variable));
    }

    public QStockInspectReal(Path<? extends StockInspectReal> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockInspectReal(PathMetadata<?> metadata) {
        super(StockInspectReal.class, metadata);
    }

}

