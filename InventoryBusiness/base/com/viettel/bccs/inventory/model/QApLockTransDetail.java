package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QApLockTransDetail is a Querydsl query type for ApLockTransDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QApLockTransDetail extends EntityPathBase<ApLockTransDetail> {

    private static final long serialVersionUID = -222846035L;

    public static final QApLockTransDetail apLockTransDetail = new QApLockTransDetail("apLockTransDetail");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Short> lockType = createNumber("lockType", Short.class);

    public final NumberPath<Long> posId = createNumber("posId", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final StringPath requestId = createString("requestId");

    public final StringPath serial = createString("serial");

    public QApLockTransDetail(String variable) {
        super(ApLockTransDetail.class, forVariable(variable));
    }

    public QApLockTransDetail(Path<? extends ApLockTransDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QApLockTransDetail(PathMetadata<?> metadata) {
        super(ApLockTransDetail.class, metadata);
    }

}

