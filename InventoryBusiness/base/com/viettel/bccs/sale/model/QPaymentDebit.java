package com.viettel.bccs.sale.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QPaymentDebit is a Querydsl query type for PaymentDebit
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPaymentDebit extends EntityPathBase<PaymentDebit> {

    private static final long serialVersionUID = 1593513911L;

    public static final QPaymentDebit paymentDebit = new QPaymentDebit("paymentDebit");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final DateTimePath<java.util.Date> lastUpdateDate = createDateTime("lastUpdateDate", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final NumberPath<Long> paymentDebitId = createNumber("paymentDebitId", Long.class);

    public final NumberPath<Long> paymentGroupId = createNumber("paymentGroupId", Long.class);

    public final NumberPath<Long> paymentGroupServiceId = createNumber("paymentGroupServiceId", Long.class);

    public QPaymentDebit(String variable) {
        super(PaymentDebit.class, forVariable(variable));
    }

    public QPaymentDebit(Path<? extends PaymentDebit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentDebit(PathMetadata<?> metadata) {
        super(PaymentDebit.class, metadata);
    }

}

