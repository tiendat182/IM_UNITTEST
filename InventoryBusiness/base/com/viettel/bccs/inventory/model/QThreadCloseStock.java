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
 * QThreadCloseStock is a Querydsl query type for ThreadCloseStock
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QThreadCloseStock extends EntityPathBase<ThreadCloseStock> {

    private static final long serialVersionUID = 8575738L;

    public static final QThreadCloseStock threadCloseStock = new QThreadCloseStock("threadCloseStock");

    public final StringPath description = createString("description");

    public final DateTimePath<java.util.Date> endActionTime = createDateTime("endActionTime", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> processDate = createDateTime("processDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> startActionTime = createDateTime("startActionTime", java.util.Date.class);

    public final StringPath startHour = createString("startHour");
    public final StringPath startMinute = createString("startMinute");
    public final StringPath startSecond = createString("startSecond");

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final StringPath threadName = createString("threadName");

    public QThreadCloseStock(String variable) {
        super(ThreadCloseStock.class, forVariable(variable));
    }

    public QThreadCloseStock(Path<? extends ThreadCloseStock> path) {
        super(path.getType(), path.getMetadata());
    }

    public QThreadCloseStock(PathMetadata<?> metadata) {
        super(ThreadCloseStock.class, metadata);
    }

}

