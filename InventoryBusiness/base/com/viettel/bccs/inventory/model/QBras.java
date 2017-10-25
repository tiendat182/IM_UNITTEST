package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import java.util.Date;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QBras is a Querydsl query type for Bras
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QBras extends EntityPathBase<Bras> {

    private static final long serialVersionUID = 1502904532L;

    public static final QBras bras = new QBras("bras");

    public final StringPath aaaIp = createString("aaaIp");

    public final NumberPath<Long> brasId = createNumber("brasId", Long.class);

    public final StringPath code = createString("code");

    public final StringPath description = createString("description");

    public final NumberPath<Long> equipmentId = createNumber("equipmentId", Long.class);

    public final StringPath ip = createString("ip");

    public final StringPath name = createString("name");

    public final StringPath port = createString("port");

    public final StringPath slot = createString("slot");

    public final StringPath status = createString("status");

    public final DateTimePath<Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");


    public QBras(String variable) {
        super(Bras.class, forVariable(variable));
    }

    public QBras(Path<? extends Bras> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBras(PathMetadata<?> metadata) {
        super(Bras.class, metadata);
    }

}

