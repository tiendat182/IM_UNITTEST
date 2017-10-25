package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QExchange is a Querydsl query type for Exchange
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QExchange extends EntityPathBase<Exchange> {

    private static final long serialVersionUID = -16428683L;

    public static final QExchange exchange = new QExchange("exchange");

    public final StringPath description = createString("description");

    public final NumberPath<Long> exchangeId = createNumber("exchangeId", Long.class);

    public final NumberPath<Long> maxGroup = createNumber("maxGroup", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> serverId = createNumber("serverId", Long.class);

    public final StringPath status = createString("status");

    public final NumberPath<Long> typeId = createNumber("typeId", Long.class);

    public QExchange(String variable) {
        super(Exchange.class, forVariable(variable));
    }

    public QExchange(Path<? extends Exchange> path) {
        super(path.getType(), path.getMetadata());
    }

    public QExchange(PathMetadata<?> metadata) {
        super(Exchange.class, metadata);
    }

}

