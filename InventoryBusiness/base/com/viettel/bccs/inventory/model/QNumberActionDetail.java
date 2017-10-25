package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QNumberActionDetail is a Querydsl query type for NumberActionDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QNumberActionDetail extends EntityPathBase<NumberActionDetail> {

    private static final long serialVersionUID = -2053222174L;

    public static final QNumberActionDetail numberActionDetail = new QNumberActionDetail("numberActionDetail");

    public final StringPath columnName = createString("columnName");

    public final StringPath newDetailValue = createString("newDetailValue");

    public final StringPath newValue = createString("newValue");

    public final NumberPath<Long> numberActionDetailId = createNumber("numberActionDetailId", Long.class);

    public final NumberPath<Long> numberActionId = createNumber("numberActionId", Long.class);

    public final StringPath oldDetailValue = createString("oldDetailValue");

    public final StringPath oldValue = createString("oldValue");

    public QNumberActionDetail(String variable) {
        super(NumberActionDetail.class, forVariable(variable));
    }

    public QNumberActionDetail(Path<? extends NumberActionDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNumberActionDetail(PathMetadata<?> metadata) {
        super(NumberActionDetail.class, metadata);
    }

}

