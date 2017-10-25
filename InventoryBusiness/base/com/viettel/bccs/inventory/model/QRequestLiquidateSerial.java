package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QRequestLiquidateSerial is a Querydsl query type for RequestLiquidateSerial
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRequestLiquidateSerial extends EntityPathBase<RequestLiquidateSerial> {

    private static final long serialVersionUID = 1180576429L;

    public static final QRequestLiquidateSerial requestLiquidateSerial = new QRequestLiquidateSerial("requestLiquidateSerial");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath fromSerial = createString("fromSerial");

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> requestLiquidateId = createNumber("requestLiquidateId", Long.class);

    public final NumberPath<Long> requestLiquidateSerialId = createNumber("requestLiquidateSerialId", Long.class);

    public final NumberPath<Long> requestLiquidateDetailId = createNumber("requestLiquidateDetailId", Long.class);

    public final StringPath toSerial = createString("toSerial");

    public QRequestLiquidateSerial(String variable) {
        super(RequestLiquidateSerial.class, forVariable(variable));
    }

    public QRequestLiquidateSerial(Path<? extends RequestLiquidateSerial> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRequestLiquidateSerial(PathMetadata<?> metadata) {
        super(RequestLiquidateSerial.class, metadata);
    }

}

