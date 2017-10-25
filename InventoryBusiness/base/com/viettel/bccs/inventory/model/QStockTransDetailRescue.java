package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTransDetailRescue is a Querydsl query type for StockTransDetailRescue
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransDetailRescue extends EntityPathBase<StockTransDetailRescue> {

    private static final long serialVersionUID = 1872361320L;

    public static final QStockTransDetailRescue stockTransDetailRescue = new QStockTransDetailRescue("stockTransDetailRescue");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> stockTransDetailId = createNumber("stockTransDetailId", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public QStockTransDetailRescue(String variable) {
        super(StockTransDetailRescue.class, forVariable(variable));
    }

    public QStockTransDetailRescue(Path<? extends StockTransDetailRescue> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransDetailRescue(PathMetadata<?> metadata) {
        super(StockTransDetailRescue.class, metadata);
    }

}

