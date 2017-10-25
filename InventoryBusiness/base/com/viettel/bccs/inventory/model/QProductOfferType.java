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
 * QProductOfferType is a Querydsl query type for ProductOfferType
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QProductOfferType extends EntityPathBase<ProductOfferType> {

    private static final long serialVersionUID = 252072153L;

    public static final QProductOfferType productOfferType = new QProductOfferType("productOfferType");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final NumberPath<Long> productOfferTypeId = createNumber("productOfferTypeId", Long.class);

    public final StringPath status = createString("status");

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");

    public final StringPath tableName = createString("tableName");

    public QProductOfferType(String variable) {
        super(ProductOfferType.class, forVariable(variable));
    }

    public QProductOfferType(Path<? extends ProductOfferType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductOfferType(PathMetadata<?> metadata) {
        super(ProductOfferType.class, metadata);
    }

}

