package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QStockDeliver is a Querydsl query type for StockDeliver
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStockDeliver extends EntityPathBase<StockDeliver> {

    private static final long serialVersionUID = 1960144769L;

    public static final QStockDeliver stockDeliver = new QStockDeliver("stockDeliver");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> createUserId = createNumber("createUserId", Long.class);

    public final StringPath createUserName = createString("createUserName");

    public final NumberPath<Long> newStaffId = createNumber("newStaffId", Long.class);

    public final NumberPath<Long> newStaffOwnerId = createNumber("newStaffOwnerId", Long.class);

    public final StringPath note = createString("note");

    public final NumberPath<Long> oldStaffId = createNumber("oldStaffId", Long.class);

    public final NumberPath<Long> oldStaffOwnerId = createNumber("oldStaffOwnerId", Long.class);

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final NumberPath<Long> stockDeliverId = createNumber("stockDeliverId", Long.class);

    public final DateTimePath<java.util.Date> updateDate = createDateTime("updateDate", java.util.Date.class);

    public QStockDeliver(String variable) {
        super(StockDeliver.class, forVariable(variable));
    }

    public QStockDeliver(Path<? extends StockDeliver> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockDeliver(PathMetadata<?> metadata) {
        super(StockDeliver.class, metadata);
    }

}

