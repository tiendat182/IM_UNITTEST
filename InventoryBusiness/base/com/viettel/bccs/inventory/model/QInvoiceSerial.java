package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QInvoiceSerial is a Querydsl query type for InvoiceSerial
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QInvoiceSerial extends EntityPathBase<InvoiceSerial> {

    private static final long serialVersionUID = 1830475631L;

    public static final QInvoiceSerial invoiceSerial = new QInvoiceSerial("invoiceSerial");
    public final NumberPath<Long> shopUsedId = createNumber("shopUsedId", Long.class);
    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath description = createString("description");

    public final NumberPath<Long> invoiceSerialId = createNumber("invoiceSerialId", Long.class);

    public final NumberPath<Long> invoiceTypeId = createNumber("invoiceTypeId", Long.class);

    public final StringPath serialNo = createString("serialNo");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final StringPath status = createString("status");

    public final NumberPath<Long> invoiceTrans = createNumber("invoiceTrans", Long.class);

    public QInvoiceSerial(String variable) {
        super(InvoiceSerial.class, forVariable(variable));
    }

    public QInvoiceSerial(Path<? extends InvoiceSerial> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInvoiceSerial(PathMetadata<?> metadata) {
        super(InvoiceSerial.class, metadata);
    }

}

