package com.viettel.bccs.sale.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QPaymentGroup is a Querydsl query type for PaymentGroup
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPaymentGroup extends EntityPathBase<PaymentGroup> {

    private static final long serialVersionUID = 1596684618L;

    public static final QPaymentGroup paymentGroup = new QPaymentGroup("paymentGroup");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> debitDayType = createNumber("debitDayType", Long.class);

    public final DateTimePath<java.util.Date> lastUpdateDate = createDateTime("lastUpdateDate", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final NumberPath<Long> maxDayDebit = createNumber("maxDayDebit", Long.class);

    public final NumberPath<Long> maxMoneyDebit = createNumber("maxMoneyDebit", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> paymentGroupId = createNumber("paymentGroupId", Long.class);

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final StringPath type = createString("type");

    public QPaymentGroup(String variable) {
        super(PaymentGroup.class, forVariable(variable));
    }

    public QPaymentGroup(Path<? extends PaymentGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentGroup(PathMetadata<?> metadata) {
        super(PaymentGroup.class, metadata);
    }

}

