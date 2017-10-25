package com.viettel.bccs.inventory.model;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

/**
 * QOptionSetValue is a Querydsl query type for OptionSetValue
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QOptionSetValue extends EntityPathBase<OptionSetValue> {

    private static final long serialVersionUID = 368164554;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QOptionSetValue optionSetValue = new QOptionSetValue("optionSetValue");

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.util.Date> creationDate = createDateTime("creationDate", java.util.Date.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> displayOrder = createNumber("displayOrder", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> lastUpdateDate = createDateTime("lastUpdateDate", java.util.Date.class);

    public final StringPath lastUpdatedBy = createString("lastUpdatedBy");

    public final StringPath name = createString("name");

    public final NumberPath<Long> optionSetId = createNumber("optionSetId", Long.class);

    public final StringPath status = createString("status");

    public final StringPath value = createString("value");

    public QOptionSetValue(String variable) {
        this(OptionSetValue.class, forVariable(variable), INITS);
    }

    public QOptionSetValue(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QOptionSetValue(PathMetadata<?> metadata, PathInits inits) {
        this(OptionSetValue.class, metadata, inits);
    }

    public QOptionSetValue(Class<? extends OptionSetValue> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
    }

}

