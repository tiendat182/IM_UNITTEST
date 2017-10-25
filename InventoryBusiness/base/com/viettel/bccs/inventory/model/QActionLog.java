package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QActionLog is a Querydsl query type for ActionLog
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QActionLog extends EntityPathBase<ActionLog> {

    private static final long serialVersionUID = -1864490980L;

    public static final QActionLog actionLog = new QActionLog("actionLog");

    public final DateTimePath<java.util.Date> actionDate = createDateTime("actionDate", java.util.Date.class);

    public final NumberPath<Long> actionId = createNumber("actionId", Long.class);

    public final StringPath actionIp = createString("actionIp");

    public final StringPath actionType = createString("actionType");

    public final StringPath actionUser = createString("actionUser");

    public final StringPath description = createString("description");

    public final NumberPath<Long> objectId = createNumber("objectId", Long.class);

    public QActionLog(String variable) {
        super(ActionLog.class, forVariable(variable));
    }

    public QActionLog(Path<? extends ActionLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QActionLog(PathMetadata<?> metadata) {
        super(ActionLog.class, metadata);
    }

}

