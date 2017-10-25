package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTransRescue is a Querydsl query type for StockTransRescue
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransRescue extends EntityPathBase<StockTransRescue> {

    private static final long serialVersionUID = -998596937L;

    public static final QStockTransRescue stockTransRescue = new QStockTransRescue("stockTransRescue");

    public final NumberPath<Long> approveStaffId = createNumber("approveStaffId", Long.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> fromOwnerId = createNumber("fromOwnerId", Long.class);

    public final NumberPath<Long> fromOwnerType = createNumber("fromOwnerType", Long.class);

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public final StringPath requestCode = createString("requestCode");

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> toOwnerId = createNumber("toOwnerId", Long.class);

    public final NumberPath<Long> toOwnerType = createNumber("toOwnerType", Long.class);

    public QStockTransRescue(String variable) {
        super(StockTransRescue.class, forVariable(variable));
    }

    public QStockTransRescue(Path<? extends StockTransRescue> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransRescue(PathMetadata<?> metadata) {
        super(StockTransRescue.class, metadata);
    }

}

