package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockBalanceDetail is a Querydsl query type for StockBalanceDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockBalanceDetail extends EntityPathBase<StockBalanceDetail> {

    private static final long serialVersionUID = -152432727L;

    public static final QStockBalanceDetail stockBalanceDetail = new QStockBalanceDetail("stockBalanceDetail");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> quantityBccs = createNumber("quantityBccs", Long.class);

    public final NumberPath<Long> quantityErp = createNumber("quantityErp", Long.class);

    public final NumberPath<Long> quantityReal = createNumber("quantityReal", Long.class);

    public final NumberPath<Long> stockBalanceDetailId = createNumber("stockBalanceDetailId", Long.class);

    public final NumberPath<Long> stockRequestId = createNumber("stockRequestId", Long.class);

    public final NumberPath<Long> type = createNumber("type", Long.class);

    public QStockBalanceDetail(String variable) {
        super(StockBalanceDetail.class, forVariable(variable));
    }

    public QStockBalanceDetail(Path<? extends StockBalanceDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockBalanceDetail(PathMetadata<?> metadata) {
        super(StockBalanceDetail.class, metadata);
    }

}

