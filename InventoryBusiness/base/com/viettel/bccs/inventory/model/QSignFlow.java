package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QSignFlow is a Querydsl query type for SignFlow
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSignFlow extends EntityPathBase<SignFlow> {

    private static final long serialVersionUID = -1695289379L;

    public static final QSignFlow signFlow = new QSignFlow("signFlow");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final DateTimePath<java.util.Date> lastUpdateTime = createDateTime("lastUpdateTime", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final StringPath name = createString("name");

    public final StringPath shopCode = createString("shopCode");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> signFlowId = createNumber("signFlowId", Long.class);

    public final StringPath status = createString("status");

    public QSignFlow(String variable) {
        super(SignFlow.class, forVariable(variable));
    }

    public QSignFlow(Path<? extends SignFlow> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSignFlow(PathMetadata<?> metadata) {
        super(SignFlow.class, metadata);
    }

}

