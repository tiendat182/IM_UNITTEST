package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QSignFlowDetail is a Querydsl query type for SignFlowDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSignFlowDetail extends EntityPathBase<SignFlowDetail> {

    private static final long serialVersionUID = 1855249038L;

    public static final QSignFlowDetail signFlowDetail = new QSignFlowDetail("signFlowDetail");

    public final StringPath email = createString("email");

    public final NumberPath<Long> signFlowDetailId = createNumber("signFlowDetailId", Long.class);

    public final NumberPath<Long> signFlowId = createNumber("signFlowId", Long.class);

    public final NumberPath<Long> signOrder = createNumber("signOrder", Long.class);

    public final StringPath status = createString("status");

    public final NumberPath<Long> vofficeRoleId = createNumber("vofficeRoleId", Long.class);

    public QSignFlowDetail(String variable) {
        super(SignFlowDetail.class, forVariable(variable));
    }

    public QSignFlowDetail(Path<? extends SignFlowDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSignFlowDetail(PathMetadata<?> metadata) {
        super(SignFlowDetail.class, metadata);
    }

}

