package com.viettel.bccs.im1.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

/**
 * QStockTotal is a Querydsl query type for StockTotal
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTotalIm1 extends EntityPathBase<StockTotalIm1> {

    private static final long serialVersionUID = 1134438560L;

    public static final QStockTotalIm1 stockTotal = new QStockTotalIm1("stockTotal");


    public final DateTimePath<java.util.Date> modifiedDate = createDateTime("modifiedDate", java.util.Date.class);

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);


    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> stockTotalId = createNumber("stockTotalId", Long.class);


    public NumberPath<Long> stockModelId = createNumber("stockModelId", Long.class);
    public NumberPath<Long> stateId = createNumber("stateId", Long.class);
    public NumberPath<Long> quantity = createNumber("quantity", Long.class);
    public NumberPath<Long> quantityIssue = createNumber("quantityIssue", Long.class);
    public NumberPath<Long> quantityDial = createNumber("quantityDial", Long.class);
    public NumberPath<Long> maxDebit = createNumber("maxDebit", Long.class);
    public NumberPath<Long> currentDebit = createNumber("currentDebit", Long.class);
    public DateTimePath<java.util.Date> dateReset = createDateTime("dateReset", java.util.Date.class);
    public NumberPath<Long> limitQuantity = createNumber("limitQuantity", Long.class);
    public NumberPath<Long> quantityHang = createNumber("quantityHang", Long.class);


    public QStockTotalIm1(String variable) {
        super(StockTotalIm1.class, forVariable(variable));
    }

    public QStockTotalIm1(Path<? extends StockTotalIm1> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTotalIm1(PathMetadata<?> metadata) {
        super(StockTotalIm1.class, metadata);
    }

}

