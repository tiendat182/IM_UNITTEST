package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTransDetail is a Querydsl query type for StockTransDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransDetail extends EntityPathBase<StockTransDetail> {

    private static final long serialVersionUID = -1399377547L;

    public static final QStockTransDetail stockTransDetail = new QStockTransDetail("stockTransDetail");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> stockTransDetailId = createNumber("stockTransDetailId", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public QStockTransDetail(String variable) {
        super(StockTransDetail.class, forVariable(variable));
    }

    public QStockTransDetail(Path<? extends StockTransDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransDetail(PathMetadata<?> metadata) {
        super(StockTransDetail.class, metadata);
    }

}

