package com.viettel.bccs.partner.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockOwnerTmp is a Querydsl query type for StockOwnerTmp
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockOwnerTmp extends EntityPathBase<StockOwnerTmp> {

    private static final long serialVersionUID = 420926772L;

    public static final QStockOwnerTmp stockOwnerTmp = new QStockOwnerTmp("stockOwnerTmp");

    public final NumberPath<Long> agentId = createNumber("agentId", Long.class);

    public final NumberPath<Long> channelTypeId = createNumber("channelTypeId", Long.class);

    public final StringPath code = createString("code");

    public final NumberPath<Long> currentDebit = createNumber("currentDebit", Long.class);

    public final NumberPath<Long> dateReset = createNumber("dateReset", Long.class);

    public final NumberPath<Long> maxDebit = createNumber("maxDebit", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final StringPath ownerType = createString("ownerType");

    public final NumberPath<Long> stockId = createNumber("stockId", Long.class);

    public QStockOwnerTmp(String variable) {
        super(StockOwnerTmp.class, forVariable(variable));
    }

    public QStockOwnerTmp(Path<? extends StockOwnerTmp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockOwnerTmp(PathMetadata<?> metadata) {
        super(StockOwnerTmp.class, metadata);
    }

}

