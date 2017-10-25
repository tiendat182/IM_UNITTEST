package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QRevokeTrans is a Querydsl query type for RevokeTrans
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRevokeTrans extends EntityPathBase<RevokeTrans> {

    private static final long serialVersionUID = 1390852912L;

    public static final QRevokeTrans revokeTrans = new QRevokeTrans("revokeTrans");

    public final StringPath accountId = createString("accountId");

    public final NumberPath<Long> amountNeedRevoke = createNumber("amountNeedRevoke", Long.class);

    public final NumberPath<Long> amountRevoked = createNumber("amountRevoked", Long.class);

    public final StringPath cmRequest = createString("cmRequest");

    public final StringPath contractNo = createString("contractNo");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> posId = createNumber("posId", Long.class);

    public final NumberPath<Long> revokeTransId = createNumber("revokeTransId", Long.class);

    public final DateTimePath<java.util.Date> saleTransDate = createDateTime("saleTransDate", java.util.Date.class);

    public final NumberPath<Long> saleTransId = createNumber("saleTransId", Long.class);

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final StringPath status = createString("status");

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> telecomServiceId = createNumber("telecomServiceId", Long.class);

    public QRevokeTrans(String variable) {
        super(RevokeTrans.class, forVariable(variable));
    }

    public QRevokeTrans(Path<? extends RevokeTrans> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRevokeTrans(PathMetadata<?> metadata) {
        super(RevokeTrans.class, metadata);
    }

}

