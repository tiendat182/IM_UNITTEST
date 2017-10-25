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
 * QProductOffering is a Querydsl query type for ProductOffering
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QProductOffering extends EntityPathBase<ProductOffering> {

    private static final long serialVersionUID = -546038141L;

    public static final QProductOffering productOffering = new QProductOffering("productOffering");

    public final StringPath code = createString("code");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath description = createString("description");

    public final DateTimePath<java.util.Date> effectDatetime = createDateTime("effectDatetime", java.util.Date.class);

    public final DateTimePath<java.util.Date> expireDatetime = createDateTime("expireDatetime", java.util.Date.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> productOfferingId = createNumber("productOfferingId", Long.class);

    public final NumberPath<Long> productOfferTypeId = createNumber("productOfferTypeId", Long.class);

    public final NumberPath<Long> productSpecId = createNumber("productSpecId", Long.class);

    public final StringPath status = createString("status");

    public final StringPath subType = createString("subType");

    public final NumberPath<Long> telecomServiceId = createNumber("telecomServiceId", Long.class);

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");

    public final NumberPath<Long> checkSerial = createNumber("checkSerial", Long.class);

    public QProductOffering(String variable) {
        super(ProductOffering.class, forVariable(variable));
    }

    public QProductOffering(Path<? extends ProductOffering> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductOffering(PathMetadata<?> metadata) {
        super(ProductOffering.class, metadata);
    }

}