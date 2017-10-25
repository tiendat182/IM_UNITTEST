package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTotal is a Querydsl query type for StockTotal
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTotal extends EntityPathBase<StockTotal> {

    private static final long serialVersionUID = 1134438560L;

    public static final QStockTotal stockTotal = new QStockTotal("stockTotal");

    public final NumberPath<Long> availableQuantity = createNumber("availableQuantity", Long.class);

    public final NumberPath<Long> currentQuantity = createNumber("currentQuantity", Long.class);

    public final NumberPath<Long> hangQuantity = createNumber("hangQuantity", Long.class);

    public final DateTimePath<java.util.Date> modifiedDate = createDateTime("modifiedDate", java.util.Date.class);

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> stockTotalId = createNumber("stockTotalId", Long.class);

    public QStockTotal(String variable) {
        super(StockTotal.class, forVariable(variable));
    }

    public QStockTotal(Path<? extends StockTotal> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTotal(PathMetadata<?> metadata) {
        super(StockTotal.class, metadata);
    }

}

