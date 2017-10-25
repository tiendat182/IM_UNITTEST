package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QChangeModelTransDetail is a Querydsl query type for ChangeModelTransDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QChangeModelTransDetail extends EntityPathBase<ChangeModelTransDetail> {

    private static final long serialVersionUID = 544155090L;

    public static final QChangeModelTransDetail changeModelTransDetail = new QChangeModelTransDetail("changeModelTransDetail");

    public final NumberPath<Long> changeModelTransDetailId = createNumber("changeModelTransDetailId", Long.class);

    public final NumberPath<Long> changeModelTransId = createNumber("changeModelTransId", Long.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> newProdOfferId = createNumber("newProdOfferId", Long.class);

    public final StringPath note = createString("note");

    public final NumberPath<Long> oldProdOfferId = createNumber("oldProdOfferId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public QChangeModelTransDetail(String variable) {
        super(ChangeModelTransDetail.class, forVariable(variable));
    }

    public QChangeModelTransDetail(Path<? extends ChangeModelTransDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChangeModelTransDetail(PathMetadata<?> metadata) {
        super(ChangeModelTransDetail.class, metadata);
    }

}

