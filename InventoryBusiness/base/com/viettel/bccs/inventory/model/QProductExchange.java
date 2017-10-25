package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QProductExchange is a Querydsl query type for ProductExchange
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QProductExchange extends EntityPathBase<ProductExchange> {

    private static final long serialVersionUID = -2082693984L;

    public static final QProductExchange productExchange = new QProductExchange("productExchange");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath description = createString("description");

    public final DateTimePath<java.util.Date> endDatetime = createDateTime("endDatetime", java.util.Date.class);

    public final NumberPath<Long> newProdOfferId = createNumber("newProdOfferId", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> prodOfferTypeId = createNumber("prodOfferTypeId", Long.class);

    public final NumberPath<Long> productExchangeId = createNumber("productExchangeId", Long.class);

    public final DateTimePath<java.util.Date> startDatetime = createDateTime("startDatetime", java.util.Date.class);

    public final StringPath status = createString("status");

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");

    public QProductExchange(String variable) {
        super(ProductExchange.class, forVariable(variable));
    }

    public QProductExchange(Path<? extends ProductExchange> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductExchange(PathMetadata<?> metadata) {
        super(ProductExchange.class, metadata);
    }

}

