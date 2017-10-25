package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QDomain is a Querydsl query type for Domain
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDomain extends EntityPathBase<Domain> {

    private static final long serialVersionUID = 1237075094L;

    public static final QDomain domain1 = new QDomain("domain1");

    public final StringPath description = createString("description");

    public final StringPath domain = createString("domain");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath status = createString("status");

    public QDomain(String variable) {
        super(Domain.class, forVariable(variable));
    }

    public QDomain(Path<? extends Domain> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDomain(PathMetadata<?> metadata) {
        super(Domain.class, metadata);
    }

}

