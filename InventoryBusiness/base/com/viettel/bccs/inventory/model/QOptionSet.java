package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QOptionSet is a Querydsl query type for OptionSet
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QOptionSet extends EntityPathBase<OptionSet> {

    private static final long serialVersionUID = -774399257;

    public static final QOptionSet optionSet = new QOptionSet("optionSet");

    public final StringPath code = createString("code");

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.util.Date> creationDate = createDateTime("creationDate", java.util.Date.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> lastUpdateDate = createDateTime("lastUpdateDate", java.util.Date.class);

    public final StringPath lastUpdatedBy = createString("lastUpdatedBy");

    public final StringPath name = createString("name");

    public final ListPath<OptionSetValue, QOptionSetValue> optionSetValueCollection = this.<OptionSetValue, QOptionSetValue>createList("optionSetValueCollection", OptionSetValue.class, QOptionSetValue.class, PathInits.DIRECT);

    public final StringPath status = createString("status");

    public QOptionSet(String variable) {
        super(OptionSet.class, forVariable(variable));
    }

    public QOptionSet(Path<? extends OptionSet> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QOptionSet(PathMetadata<?> metadata) {
        super(OptionSet.class, metadata);
    }

}

