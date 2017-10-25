package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QActionLogDetail is a Querydsl query type for ActionLogDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QActionLogDetail extends EntityPathBase<ActionLogDetail> {

    private static final long serialVersionUID = -54288499L;

    public static final QActionLogDetail actionLogDetail = new QActionLogDetail("actionLogDetail");

    public final DateTimePath<java.util.Date> actionDate = createDateTime("actionDate", java.util.Date.class);

    public final NumberPath<Long> actionDetailId = createNumber("actionDetailId", Long.class);

    public final NumberPath<Long> actionId = createNumber("actionId", Long.class);

    public final StringPath columnName = createString("columnName");

    public final StringPath description = createString("description");

    public final StringPath newValue = createString("newValue");

    public final StringPath oldValue = createString("oldValue");

    public final StringPath tableName = createString("tableName");

    public QActionLogDetail(String variable) {
        super(ActionLogDetail.class, forVariable(variable));
    }

    public QActionLogDetail(Path<? extends ActionLogDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QActionLogDetail(PathMetadata<?> metadata) {
        super(ActionLogDetail.class, metadata);
    }

}

