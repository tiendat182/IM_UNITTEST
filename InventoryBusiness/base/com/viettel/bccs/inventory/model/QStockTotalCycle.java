package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTotalCycle is a Querydsl query type for StockTotalCycle
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTotalCycle extends EntityPathBase<StockTotalCycle> {

    private static final long serialVersionUID = -1511332826L;

    public static final QStockTotalCycle stockTotalCycle = new QStockTotalCycle("stockTotalCycle");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final NumberPath<Long> priceCost = createNumber("priceCost", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> productOfferTypeId = createNumber("productOfferTypeId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> quantityCycle1 = createNumber("quantityCycle1", Long.class);

    public final NumberPath<Long> quantityCycle2 = createNumber("quantityCycle2", Long.class);

    public final NumberPath<Long> quantityCycle3 = createNumber("quantityCycle3", Long.class);

    public final NumberPath<Long> quantityCycle4 = createNumber("quantityCycle4", Long.class);

    public final NumberPath<Long> retailPrice = createNumber("retailPrice", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> stockTotalCycleId = createNumber("stockTotalCycleId", Long.class);

    public QStockTotalCycle(String variable) {
        super(StockTotalCycle.class, forVariable(variable));
    }

    public QStockTotalCycle(Path<? extends StockTotalCycle> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTotalCycle(PathMetadata<?> metadata) {
        super(StockTotalCycle.class, metadata);
    }

}

