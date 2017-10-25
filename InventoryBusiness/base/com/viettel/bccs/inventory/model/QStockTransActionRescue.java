package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTransActionRescue is a Querydsl query type for StockTransActionRescue
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransActionRescue extends EntityPathBase<StockTransActionRescue> {

    private static final long serialVersionUID = -1443717747L;

    public static final QStockTransActionRescue stockTransActionRescue = new QStockTransActionRescue("stockTransActionRescue");

    public final NumberPath<Long> actionId = createNumber("actionId", Long.class);

    public final NumberPath<Long> actionStaffId = createNumber("actionStaffId", Long.class);

    public final NumberPath<Long> actionType = createNumber("actionType", Long.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public QStockTransActionRescue(String variable) {
        super(StockTransActionRescue.class, forVariable(variable));
    }

    public QStockTransActionRescue(Path<? extends StockTransActionRescue> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransActionRescue(PathMetadata<?> metadata) {
        super(StockTransActionRescue.class, metadata);
    }

}

