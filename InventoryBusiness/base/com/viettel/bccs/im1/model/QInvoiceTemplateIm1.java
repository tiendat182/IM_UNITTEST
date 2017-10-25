package com.viettel.bccs.im1.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QInvoiceTemplateIm1 is a Querydsl query type for InvoiceTemplateIm1
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QInvoiceTemplateIm1 extends EntityPathBase<InvoiceTemplateIm1> {

    private static final long serialVersionUID = -215666440L;

    public static final QInvoiceTemplateIm1 invoiceTemplateIm1 = new QInvoiceTemplateIm1("invoiceTemplateIm1");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Long> bookTypeId = createNumber("bookTypeId", Long.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath description = createString("description");

    public final NumberPath<Long> invoiceTemplateId = createNumber("invoiceTemplateId", Long.class);

    public final DateTimePath<java.util.Date> lastUpdateDate = createDateTime("lastUpdateDate", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final NumberPath<Long> preAmount = createNumber("preAmount", Long.class);

    public final StringPath serialNo = createString("serialNo");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final NumberPath<Long> type = createNumber("type", Long.class);

    public final NumberPath<Long> usedAmount = createNumber("usedAmount", Long.class);

    public QInvoiceTemplateIm1(String variable) {
        super(InvoiceTemplateIm1.class, forVariable(variable));
    }

    public QInvoiceTemplateIm1(Path<? extends InvoiceTemplateIm1> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInvoiceTemplateIm1(PathMetadata<?> metadata) {
        super(InvoiceTemplateIm1.class, metadata);
    }

}

