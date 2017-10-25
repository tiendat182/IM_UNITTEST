package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QImsiMade is a Querydsl query type for ImsiMade
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QImsiMade extends EntityPathBase<ImsiMade> {

    private static final long serialVersionUID = 1945620993L;

    public static final QImsiMade imsiMade = new QImsiMade("imsiMade");

    public final StringPath content = createString("content");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath fromImsi = createString("fromImsi");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath opKey = createString("opKey");

    public final StringPath po = createString("po");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final StringPath serialNo = createString("serialNo");

    public final StringPath simType = createString("simType");

    public final StringPath toImsi = createString("toImsi");

    public final StringPath userCreate = createString("userCreate");

    public QImsiMade(String variable) {
        super(ImsiMade.class, forVariable(variable));
    }

    public QImsiMade(Path<? extends ImsiMade> path) {
        super(path.getType(), path.getMetadata());
    }

    public QImsiMade(PathMetadata<?> metadata) {
        super(ImsiMade.class, metadata);
    }

}

