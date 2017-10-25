package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QReasonGroup is a Querydsl query type for ReasonGroup
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QReasonGroup extends EntityPathBase<ReasonGroup> {

    private static final long serialVersionUID = -1425284599L;

    public static final QReasonGroup reasonGroup = new QReasonGroup("reasonGroup");

    public final StringPath description = createString("description");

    public final StringPath reasonGroupCode = createString("reasonGroupCode");

    public final NumberPath<Long> reasonGroupId = createNumber("reasonGroupId", Long.class);

    public final StringPath reasonGroupName = createString("reasonGroupName");

    public final StringPath status = createString("status");

    public QReasonGroup(String variable) {
        super(ReasonGroup.class, forVariable(variable));
    }

    public QReasonGroup(Path<? extends ReasonGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReasonGroup(PathMetadata<?> metadata) {
        super(ReasonGroup.class, metadata);
    }

}

