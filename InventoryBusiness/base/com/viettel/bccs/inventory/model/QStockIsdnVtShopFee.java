package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockIsdnVtShopFee is a Querydsl query type for StockIsdnVtShopFee
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockIsdnVtShopFee extends EntityPathBase<StockIsdnVtShopFee> {

    private static final long serialVersionUID = 1290049690L;

    public static final QStockIsdnVtShopFee stockIsdnVtShopFee = new QStockIsdnVtShopFee("stockIsdnVtShopFee");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final StringPath feeType = createString("feeType");

    public final StringPath isdn = createString("isdn");

    public QStockIsdnVtShopFee(String variable) {
        super(StockIsdnVtShopFee.class, forVariable(variable));
    }

    public QStockIsdnVtShopFee(Path<? extends StockIsdnVtShopFee> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockIsdnVtShopFee(PathMetadata<?> metadata) {
        super(StockIsdnVtShopFee.class, metadata);
    }

}

