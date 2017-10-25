package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockIsdnVtShopLock is a Querydsl query type for StockIsdnVtShopLock
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockIsdnVtShopLock extends EntityPathBase<StockIsdnVtShopLock> {

    private static final long serialVersionUID = 1337023127L;

    public static final QStockIsdnVtShopLock stockIsdnVtShopLock = new QStockIsdnVtShopLock("stockIsdnVtShopLock");

    public final StringPath isdn = createString("isdn");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public QStockIsdnVtShopLock(String variable) {
        super(StockIsdnVtShopLock.class, forVariable(variable));
    }

    public QStockIsdnVtShopLock(Path<? extends StockIsdnVtShopLock> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockIsdnVtShopLock(PathMetadata<?> metadata) {
        super(StockIsdnVtShopLock.class, metadata);
    }

}

