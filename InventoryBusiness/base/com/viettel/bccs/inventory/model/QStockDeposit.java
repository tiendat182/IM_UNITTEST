package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockDeposit is a Querydsl query type for StockDeposit
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockDeposit extends EntityPathBase<StockDeposit> {

    private static final long serialVersionUID = 1964014842L;

    public static final QStockDeposit stockDeposit = new QStockDeposit("stockDeposit");

    public final NumberPath<Long> chanelTypeId = createNumber("chanelTypeId", Long.class);

    public final DateTimePath<java.util.Date> dateFrom = createDateTime("dateFrom", java.util.Date.class);

    public final DateTimePath<java.util.Date> dateTo = createDateTime("dateTo", java.util.Date.class);

    public final DateTimePath<java.util.Date> lastModify = createDateTime("lastModify", java.util.Date.class);

    public final NumberPath<Long> maxStock = createNumber("maxStock", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> stockDepositId = createNumber("stockDepositId", Long.class);

    public final NumberPath<Long> transType = createNumber("transType", Long.class);

    public final StringPath userModify = createString("userModify");

    public QStockDeposit(String variable) {
        super(StockDeposit.class, forVariable(variable));
    }

    public QStockDeposit(Path<? extends StockDeposit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockDeposit(PathMetadata<?> metadata) {
        super(StockDeposit.class, metadata);
    }

}

