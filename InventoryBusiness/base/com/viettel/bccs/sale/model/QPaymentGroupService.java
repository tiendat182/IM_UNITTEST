package com.viettel.bccs.sale.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QPaymentGroupService is a Querydsl query type for PaymentGroupService
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPaymentGroupService extends EntityPathBase<PaymentGroupService> {

    private static final long serialVersionUID = -1724006901L;

    public static final QPaymentGroupService paymentGroupService = new QPaymentGroupService("paymentGroupService");

    public final StringPath code = createString("code");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> lastUpdateDate = createDateTime("lastUpdateDate", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final StringPath name = createString("name");

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public QPaymentGroupService(String variable) {
        super(PaymentGroupService.class, forVariable(variable));
    }

    public QPaymentGroupService(Path<? extends PaymentGroupService> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentGroupService(PathMetadata<?> metadata) {
        super(PaymentGroupService.class, metadata);
    }

}

