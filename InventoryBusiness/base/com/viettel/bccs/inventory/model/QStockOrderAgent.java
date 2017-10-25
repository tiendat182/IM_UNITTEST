package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockOrderAgent is a Querydsl query type for StockOrderAgent
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockOrderAgent extends EntityPathBase<StockOrderAgent> {

    private static final long serialVersionUID = -51914629L;

    public static final QStockOrderAgent stockOrderAgent = new QStockOrderAgent("stockOrderAgent");

    public final StringPath bankCode = createString("bankCode");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> createStaffId = createNumber("createStaffId", Long.class);

    public final DateTimePath<java.util.Date> lastModify = createDateTime("lastModify", java.util.Date.class);

    public final StringPath note = createString("note");

    public final NumberPath<Long> orderType = createNumber("orderType", Long.class);

    public final StringPath requestCode = createString("requestCode");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> stockOrderAgentId = createNumber("stockOrderAgentId", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> updateStaffId = createNumber("updateStaffId", Long.class);

    public QStockOrderAgent(String variable) {
        super(StockOrderAgent.class, forVariable(variable));
    }

    public QStockOrderAgent(Path<? extends StockOrderAgent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockOrderAgent(PathMetadata<?> metadata) {
        super(StockOrderAgent.class, metadata);
    }

}

