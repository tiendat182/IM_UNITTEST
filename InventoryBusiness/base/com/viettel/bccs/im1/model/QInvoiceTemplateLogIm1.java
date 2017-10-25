package com.viettel.bccs.im1.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QInvoiceTemplateLogIm1 is a Querydsl query type for InvoiceTemplateLogIm1
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QInvoiceTemplateLogIm1 extends EntityPathBase<InvoiceTemplateLogIm1> {

    private static final long serialVersionUID = 441577566L;

    public static final QInvoiceTemplateLogIm1 invoiceTemplateLogIm1 = new QInvoiceTemplateLogIm1("invoiceTemplateLogIm1");

    public final NumberPath<Long> amountApply = createNumber("amountApply", Long.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> fromShopId = createNumber("fromShopId", Long.class);

    public final NumberPath<Long> invoiceTemplateId = createNumber("invoiceTemplateId", Long.class);

    public final NumberPath<Long> invoiceTemplateLogId = createNumber("invoiceTemplateLogId", Long.class);

    public final NumberPath<Long> logType = createNumber("logType", Long.class);

    public final NumberPath<Long> orgAmount = createNumber("orgAmount", Long.class);

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final NumberPath<Long> valid = createNumber("valid", Long.class);

    public QInvoiceTemplateLogIm1(String variable) {
        super(InvoiceTemplateLogIm1.class, forVariable(variable));
    }

    public QInvoiceTemplateLogIm1(Path<? extends InvoiceTemplateLogIm1> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInvoiceTemplateLogIm1(PathMetadata<?> metadata) {
        super(InvoiceTemplateLogIm1.class, metadata);
    }

}

