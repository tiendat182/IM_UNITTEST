package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QStockDebit is a Querydsl query type for StockDebit
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockDebit extends EntityPathBase<StockDebit> {

    private static final long serialVersionUID = 1119347272L;

    public static final QStockDebit stockDebit = new QStockDebit("stockDebit");

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

    public QStockDebit(String variable) {
        super(StockDebit.class, forVariable(variable));
    }

    public QStockDebit(Path<? extends StockDebit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockDebit(PathMetadata<?> metadata) {
        super(StockDebit.class, metadata);
    }

}

