package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QProductOfferPrice is a Querydsl query type for ProductOfferPrice
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QProductOfferPrice extends EntityPathBase<ProductOfferPrice> {

    private static final long serialVersionUID = -779607158L;

    public static final QProductOfferPrice productOfferPrice = new QProductOfferPrice("productOfferPrice");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath cronExpression = createString("cronExpression");

    public final StringPath description = createString("description");

    public final DateTimePath<java.util.Date> effectDatetime = createDateTime("effectDatetime", java.util.Date.class);

    public final StringPath effectType = createString("effectType");

    public final DateTimePath<java.util.Date> expireDatetime = createDateTime("expireDatetime", java.util.Date.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> pledgeAmount = createNumber("pledgeAmount", Long.class);

    public final NumberPath<Long> pledgeTime = createNumber("pledgeTime", Long.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final NumberPath<Long> pricePolicyId = createNumber("pricePolicyId", Long.class);

    public final NumberPath<Long> priceTypeId = createNumber("priceTypeId", Long.class);

    public final NumberPath<Short> priority = createNumber("priority", Short.class);

    public final NumberPath<Long> priorPay = createNumber("priorPay", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> productOfferPriceId = createNumber("productOfferPriceId", Long.class);

    public final StringPath status = createString("status");

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");

    public final NumberPath<Long> vat = createNumber("vat", Long.class);

    public QProductOfferPrice(String variable) {
        super(ProductOfferPrice.class, forVariable(variable));
    }

    public QProductOfferPrice(Path<? extends ProductOfferPrice> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductOfferPrice(PathMetadata<?> metadata) {
        super(ProductOfferPrice.class, metadata);
    }

}

