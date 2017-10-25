package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QProductErp is a Querydsl query type for ProductErp
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QProductErp extends EntityPathBase<ProductErp> {

    private static final long serialVersionUID = -2063294266L;

    public static final QProductErp productErp = new QProductErp("productErp");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> productErpId = createNumber("productErpId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");

    public QProductErp(String variable) {
        super(ProductErp.class, forVariable(variable));
    }

    public QProductErp(Path<? extends ProductErp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductErp(PathMetadata<?> metadata) {
        super(ProductErp.class, metadata);
    }

}

