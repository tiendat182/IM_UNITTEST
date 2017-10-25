package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockOrderDetail is a Querydsl query type for StockOrderDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockOrderDetail extends EntityPathBase<StockOrderDetail> {

    private static final long serialVersionUID = -1524878949L;

    public static final QStockOrderDetail stockOrderDetail = new QStockOrderDetail("stockOrderDetail");

    public final NumberPath<Long> quantityOrder = createNumber("quantityOrder", Long.class);

    public final NumberPath<Long> quantityReal = createNumber("quantityReal", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> stockOrderDetailId = createNumber("stockOrderDetailId", Long.class);

    public final NumberPath<Long> stockOrderId = createNumber("stockOrderId", Long.class);

    public QStockOrderDetail(String variable) {
        super(StockOrderDetail.class, forVariable(variable));
    }

    public QStockOrderDetail(Path<? extends StockOrderDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockOrderDetail(PathMetadata<?> metadata) {
        super(StockOrderDetail.class, metadata);
    }

}

