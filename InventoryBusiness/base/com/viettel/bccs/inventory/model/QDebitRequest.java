package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QDebitRequest is a Querydsl query type for DebitRequest
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDebitRequest extends EntityPathBase<DebitRequest> {

    private static final long serialVersionUID = -441213355L;

    public static final QDebitRequest debitRequest = new QDebitRequest("debitRequest");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final ListPath<DebitRequestDetail, QDebitRequestDetail> debitRequestDetailList = this.<DebitRequestDetail, QDebitRequestDetail>createList("debitRequestDetailList", DebitRequestDetail.class, QDebitRequestDetail.class, PathInits.DIRECT2);

    public final StringPath description = createString("description");

    public final SimplePath<java.io.Serializable> fileContent = createSimple("fileContent", java.io.Serializable.class);

    public final StringPath fileName = createString("fileName");

    public final DateTimePath<java.util.Date> lastUpdateTime = createDateTime("lastUpdateTime", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final StringPath requestCode = createString("requestCode");

    public final NumberPath<Long> requestId = createNumber("requestId", Long.class);

    public final StringPath requestObjectType = createString("requestObjectType");

    public final StringPath status = createString("status");

    public QDebitRequest(String variable) {
        super(DebitRequest.class, forVariable(variable));
    }

    public QDebitRequest(Path<? extends DebitRequest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDebitRequest(PathMetadata<?> metadata) {
        super(DebitRequest.class, metadata);
    }

}

