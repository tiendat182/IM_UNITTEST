package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockOrder is a Querydsl query type for StockOrder
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockOrder extends EntityPathBase<StockOrder> {

    private static final long serialVersionUID = 1129895082L;

    public static final QStockOrder stockOrder = new QStockOrder("stockOrder");

    public final DateTimePath<java.util.Date> approveDate = createDateTime("approveDate", java.util.Date.class);

    public final NumberPath<Long> approveStaffId = createNumber("approveStaffId", Long.class);

    public final DateTimePath<java.util.Date> cancelDate = createDateTime("cancelDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> refuseDate = createDateTime("refuseDate", java.util.Date.class);

    public final NumberPath<Long> refuseStaffId = createNumber("refuseStaffId", Long.class);

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final StringPath stockOrderCode = createString("stockOrderCode");

    public final NumberPath<Long> stockOrderId = createNumber("stockOrderId", Long.class);

    public QStockOrder(String variable) {
        super(StockOrder.class, forVariable(variable));
    }

    public QStockOrder(Path<? extends StockOrder> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockOrder(PathMetadata<?> metadata) {
        super(StockOrder.class, metadata);
    }

}

