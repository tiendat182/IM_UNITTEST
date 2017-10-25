package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;

import javax.annotation.Generated;

import com.mysema.query.types.Path;


/**
 * QNumberAction is a Querydsl query type for NumberAction
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QNumberAction extends EntityPathBase<NumberAction> {
    public final StringPath serviceType = createString("serviceType");

    public final StringPath status = createString("status");

    private static final long serialVersionUID = 983151921L;

    public static final QNumberAction numberAction = new QNumberAction("numberAction");

    public final StringPath actionType = createString("actionType");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath fromIsdn = createString("fromIsdn");

    public final StringPath note = createString("note");

    public final NumberPath<Long> numberActionId = createNumber("numberActionId", Long.class);

    public final NumberPath<Long> telecomServiceId = createNumber("telecomServiceId", Long.class);

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public final StringPath toIsdn = createString("toIsdn");

    public final StringPath userCreate = createString("userCreate");

    public final StringPath userIpAddress = createString("userIpAddress");

    public QNumberAction(String variable) {
        super(NumberAction.class, forVariable(variable));
    }

    public QNumberAction(Path<? extends NumberAction> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNumberAction(PathMetadata<?> metadata) {
        super(NumberAction.class, metadata);
    }


}

