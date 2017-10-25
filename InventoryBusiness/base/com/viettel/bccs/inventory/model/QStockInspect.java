package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockInspect is a Querydsl query type for StockInspect
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockInspect extends EntityPathBase<StockInspect> {

    private static final long serialVersionUID = -1927952272L;

    public static final QStockInspect stockInspect = new QStockInspect("stockInspect");

    public final DateTimePath<java.util.Date> approveDate = createDateTime("approveDate", java.util.Date.class);

    public final StringPath approveNote = createString("approveNote");

    public final NumberPath<Long> approveStatus = createNumber("approveStatus", Long.class);

    public final StringPath approveUser = createString("approveUser");

    public final NumberPath<Long> approveUserId = createNumber("approveUserId", Long.class);

    public final StringPath approveUserName = createString("approveUserName");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> createUserId = createNumber("createUserId", Long.class);

    public final StringPath createUserName = createString("createUserName");

    public final NumberPath<Long> isFinished = createNumber("isFinished", Long.class);

    public final NumberPath<Long> isFinishedAll = createNumber("isFinishedAll", Long.class);

    public final NumberPath<Long> isScan = createNumber("isScan", Long.class);

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> prodOfferTypeId = createNumber("prodOfferTypeId", Long.class);

    public final NumberPath<Long> quantityReal = createNumber("quantityReal", Long.class);

    public final NumberPath<Long> quantitySys = createNumber("quantitySys", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final NumberPath<Long> stockInspectId = createNumber("stockInspectId", Long.class);

    public final DateTimePath<java.util.Date> updateDate = createDateTime("updateDate", java.util.Date.class);

    public QStockInspect(String variable) {
        super(StockInspect.class, forVariable(variable));
    }

    public QStockInspect(Path<? extends StockInspect> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockInspect(PathMetadata<?> metadata) {
        super(StockInspect.class, metadata);
    }

}

