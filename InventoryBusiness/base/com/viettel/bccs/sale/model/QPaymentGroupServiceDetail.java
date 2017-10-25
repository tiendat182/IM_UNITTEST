package com.viettel.bccs.sale.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QPaymentGroupServiceDetail is a Querydsl query type for PaymentGroupServiceDetail
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPaymentGroupServiceDetail extends EntityPathBase<PaymentGroupServiceDetail> {

    private static final long serialVersionUID = -1758802372L;

    public static final QPaymentGroupServiceDetail paymentGroupServiceDetail = new QPaymentGroupServiceDetail("paymentGroupServiceDetail");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> maxDayDebit = createNumber("maxDayDebit", Long.class);

    public final NumberPath<Long> paymentGroupServiceId = createNumber("paymentGroupServiceId", Long.class);

    public final NumberPath<Long> telecomServiceId = createNumber("telecomServiceId", Long.class);

    public QPaymentGroupServiceDetail(String variable) {
        super(PaymentGroupServiceDetail.class, forVariable(variable));
    }

    public QPaymentGroupServiceDetail(Path<? extends PaymentGroupServiceDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentGroupServiceDetail(PathMetadata<?> metadata) {
        super(PaymentGroupServiceDetail.class, metadata);
    }

}

