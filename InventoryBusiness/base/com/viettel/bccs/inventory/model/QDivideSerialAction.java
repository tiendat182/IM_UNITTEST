package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QDivideSerialAction is a Querydsl query type for DivideSerialAction
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDivideSerialAction extends EntityPathBase<DivideSerialAction> {

    private static final long serialVersionUID = 585976117L;

    public static final QDivideSerialAction divideSerialAction = new QDivideSerialAction("divideSerialAction");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("serialContent");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath serialContent = createString("serialContent");

    public final StringPath stockTransActionId = createString("stockTransActionId");

    public QDivideSerialAction(String variable) {
        super(DivideSerialAction.class, forVariable(variable));
    }

    public QDivideSerialAction(Path<? extends DivideSerialAction> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDivideSerialAction(PathMetadata<?> metadata) {
        super(DivideSerialAction.class, metadata);
    }

}

