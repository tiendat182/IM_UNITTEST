package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QRequestLiquidateDetail is a Querydsl query type for RequestLiquidateDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRequestLiquidateDetail extends EntityPathBase<RequestLiquidateDetail> {

    private static final long serialVersionUID = 751191306L;

    public static final QRequestLiquidateDetail requestLiquidateDetail = new QRequestLiquidateDetail("requestLiquidateDetail");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> requestLiquidateDetailId = createNumber("requestLiquidateDetailId", Long.class);

    public final NumberPath<Long> requestLiquidateId = createNumber("requestLiquidateId", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public QRequestLiquidateDetail(String variable) {
        super(RequestLiquidateDetail.class, forVariable(variable));
    }

    public QRequestLiquidateDetail(Path<? extends RequestLiquidateDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRequestLiquidateDetail(PathMetadata<?> metadata) {
        super(RequestLiquidateDetail.class, metadata);
    }

}

