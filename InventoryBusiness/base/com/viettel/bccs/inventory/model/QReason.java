package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QReason is a Querydsl query type for Reason
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QReason extends EntityPathBase<Reason> {

    private static final long serialVersionUID = 1628307990L;

    public static final QReason reason = new QReason("reason");

    public final NumberPath<Short> haveMoney = createNumber("haveMoney", Short.class);

    public final StringPath reasonCode = createString("reasonCode");

    public final StringPath reasonDescription = createString("reasonDescription");

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public final StringPath reasonName = createString("reasonName");

    public final StringPath reasonStatus = createString("reasonStatus");

    public final StringPath reasonType = createString("reasonType");

    public final StringPath service = createString("service");

    public QReason(String variable) {
        super(Reason.class, forVariable(variable));
    }

    public QReason(Path<? extends Reason> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReason(PathMetadata<?> metadata) {
        super(Reason.class, metadata);
    }

}

