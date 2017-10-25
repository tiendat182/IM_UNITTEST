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
 * QDebitRequestDetail is a Querydsl query type for DebitRequestDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDebitRequestDetail extends EntityPathBase<DebitRequestDetail> {

    private static final long serialVersionUID = -1236939002L;

    public static final QDebitRequestDetail debitRequestDetail = new QDebitRequestDetail("debitRequestDetail");

    public final StringPath debitDayType = createString("debitDayType");

    public final NumberPath<Long> debitValue = createNumber("debitValue", Long.class);

    public final StringPath financeType = createString("financeType");

    public final StringPath paymentType = createString("paymentType");

    public final StringPath note = createString("note");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> paymentGroupId = createNumber("paymentGroupId", Long.class);

    public final NumberPath<Long> paymentGroupServiceId = createNumber("paymentGroupServiceId", Long.class);

    public final StringPath ownerType = createString("ownerType");

    public final DateTimePath<java.util.Date> requestDate = createDateTime("requestDate", java.util.Date.class);

    public final NumberPath<Long> requestDetailId = createNumber("requestDetailId", Long.class);

    public final NumberPath<Long> requestId = createNumber("requestId", Long.class);

    public final StringPath requestType = createString("requestType");

    public final StringPath status = createString("status");

    public QDebitRequestDetail(String variable) {
        super(DebitRequestDetail.class, forVariable(variable));
    }

    public QDebitRequestDetail(Path<? extends DebitRequestDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDebitRequestDetail(PathMetadata<?> metadata) {
        super(DebitRequestDetail.class, metadata);
    }

}

