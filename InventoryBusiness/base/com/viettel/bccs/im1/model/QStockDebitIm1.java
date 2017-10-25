package com.viettel.bccs.im1.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.viettel.bccs.inventory.model.StockDebit;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QStockDebit is a Querydsl query type for StockDebit
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockDebitIm1 extends EntityPathBase<StockDebitIm1> {

    private static final long serialVersionUID = 1119347272L;

    public static final QStockDebitIm1 stockDebit = new QStockDebitIm1("stockDebit");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath debitDayType = createString("debitDayType");

    public final NumberPath<Long> debitValue = createNumber("debitValue", Long.class);

    public final StringPath financeType = createString("financeType");

    public final DateTimePath<java.util.Date> lastUpdateTime = createDateTime("lastUpdateTime", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final StringPath ownerType = createString("ownerType");

    public final NumberPath<Long> stockDebitId = createNumber("stockDebitId", Long.class);

    public QStockDebitIm1(String variable) {
        super(StockDebitIm1.class, forVariable(variable));
    }

    public QStockDebitIm1(Path<? extends StockDebitIm1> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockDebitIm1(PathMetadata<?> metadata) {
        super(StockDebitIm1.class, metadata);
    }

}

