package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockTransDetailKcs is a Querydsl query type for StockTransDetailKcs
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransDetailKcs extends EntityPathBase<StockTransDetailKcs> {

    private static final long serialVersionUID = -1903852442L;

    public static final QStockTransDetailKcs stockTransDetailKcs = new QStockTransDetailKcs("stockTransDetailKcs");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> stockTransDetailKcsId = createNumber("stockTransDetailKcsId", Long.class);

    public QStockTransDetailKcs(String variable) {
        super(StockTransDetailKcs.class, forVariable(variable));
    }

    public QStockTransDetailKcs(Path<? extends StockTransDetailKcs> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransDetailKcs(PathMetadata<?> metadata) {
        super(StockTransDetailKcs.class, metadata);
    }

}

