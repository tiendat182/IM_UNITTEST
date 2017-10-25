package com.viettel.bccs.im1.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.viettel.bccs.sale.model.PaymentDebit;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QPaymentDebit is a Querydsl query type for PaymentDebit
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPaymentDebitIm1 extends EntityPathBase<PaymentDebitIm1> {

    private static final long serialVersionUID = 1593513911L;

    public static final QPaymentDebitIm1 paymentDebit = new QPaymentDebitIm1("paymentDebit");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final DateTimePath<java.util.Date> lastUpdateDate = createDateTime("lastUpdateDate", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);

    public final NumberPath<Long> paymentDebitId = createNumber("paymentDebitId", Long.class);

    public final NumberPath<Long> paymentGroupId = createNumber("paymentGroupId", Long.class);

    public final NumberPath<Long> paymentGroupServiceId = createNumber("paymentGroupServiceId", Long.class);

    public QPaymentDebitIm1(String variable) {
        super(PaymentDebitIm1.class, forVariable(variable));
    }

    public QPaymentDebitIm1(Path<? extends PaymentDebitIm1> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentDebitIm1(PathMetadata<?> metadata) {
        super(PaymentDebitIm1.class, metadata);
    }

}

