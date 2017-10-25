package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QRequestLiquidate is a Querydsl query type for RequestLiquidate
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRequestLiquidate extends EntityPathBase<RequestLiquidate> {

    private static final long serialVersionUID = -1959401639L;

    public static final QRequestLiquidate requestLiquidate = new QRequestLiquidate("requestLiquidate");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final NumberPath<Long> createShopId = createNumber("createShopId", Long.class);

    public final NumberPath<Long> createStaffId = createNumber("createStaffId", Long.class);

    public final StringPath requestCode = createString("requestCode");

    public final NumberPath<Long> requestLiquidateId = createNumber("requestLiquidateId", Long.class);

    public final StringPath status = createString("status");

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public QRequestLiquidate(String variable) {
        super(RequestLiquidate.class, forVariable(variable));
    }

    public QRequestLiquidate(Path<? extends RequestLiquidate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRequestLiquidate(PathMetadata<?> metadata) {
        super(RequestLiquidate.class, metadata);
    }

}

