package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;

import javax.annotation.Generated;

import com.mysema.query.types.Path;


/**
 * QStockTransExt is a Querydsl query type for StockTransExt
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockTransExt extends EntityPathBase<StockTransExt> {

    private static final long serialVersionUID = 1092330365L;

    public static final QStockTransExt stockTransExt = new QStockTransExt("stockTransExt");

    public final StringPath extKey = createString("extKey");

    public final StringPath extValue = createString("extValue");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public QStockTransExt(String variable) {
        super(StockTransExt.class, forVariable(variable));
    }

    public QStockTransExt(Path<? extends StockTransExt> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockTransExt(PathMetadata<?> metadata) {
        super(StockTransExt.class, metadata);
    }

}

