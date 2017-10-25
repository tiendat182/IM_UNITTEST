package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockHandsetRescue is a Querydsl query type for StockHandsetRescue
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockHandsetRescue extends EntityPathBase<StockHandsetRescue> {

    private static final long serialVersionUID = 2065390178L;

    public static final QStockHandsetRescue stockHandsetRescue = new QStockHandsetRescue("stockHandsetRescue");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> lastModify = createDateTime("lastModify", java.util.Date.class);

    public final NumberPath<Long> newProdOfferlId = createNumber("newProdOfferlId", Long.class);

    public final NumberPath<Long> oldProdOfferId = createNumber("oldProdOfferId", Long.class);

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final StringPath serial = createString("serial");

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public QStockHandsetRescue(String variable) {
        super(StockHandsetRescue.class, forVariable(variable));
    }

    public QStockHandsetRescue(Path<? extends StockHandsetRescue> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockHandsetRescue(PathMetadata<?> metadata) {
        super(StockHandsetRescue.class, metadata);
    }

}

