package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QPartner is a Querydsl query type for Partner
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPartner extends EntityPathBase<Partner> {

    private static final long serialVersionUID = 1359111990L;

    public static final QPartner partner = new QPartner("partner");

    public final StringPath address = createString("address");

    public final StringPath contactName = createString("contactName");

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    public final StringPath fax = createString("fax");

    public final StringPath partnerCode = createString("partnerCode");

    public final NumberPath<Long> partnerId = createNumber("partnerId", Long.class);

    public final StringPath partnerName = createString("partnerName");

    public final NumberPath<Long> partnerType = createNumber("partnerType", Long.class);

    public final StringPath phone = createString("phone");

    public final DateTimePath<java.util.Date> staDate = createDateTime("staDate", java.util.Date.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public QPartner(String variable) {
        super(Partner.class, forVariable(variable));
    }

    public QPartner(Path<? extends Partner> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPartner(PathMetadata<?> metadata) {
        super(Partner.class, metadata);
    }

}

