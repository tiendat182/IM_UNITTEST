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
 * QInvoiceType is a Querydsl query type for InvoiceType
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QInvoiceType extends EntityPathBase<InvoiceType> {

    private static final long serialVersionUID = 1092455285L;

    public static final QInvoiceType invoiceType1 = new QInvoiceType("invoiceType1");

    public final NumberPath<Long> blockNoLength = createNumber("blockNoLength", Long.class);

    public final StringPath bookType = createString("bookType");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath invoiceName = createString("invoiceName");

    public final NumberPath<Long> invoiceNoLength = createNumber("invoiceNoLength", Long.class);

    public final StringPath invoiceType = createString("invoiceType");

    public final NumberPath<Long> invoiceTypeId = createNumber("invoiceTypeId", Long.class);

    public final NumberPath<Long> numInvoice = createNumber("numInvoice", Long.class);

    public final StringPath status = createString("status");

    public final StringPath type = createString("type");

    public final StringPath typeInv = createString("typeInv");

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");

    public QInvoiceType(String variable) {
        super(InvoiceType.class, forVariable(variable));
    }

    public QInvoiceType(Path<? extends InvoiceType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInvoiceType(PathMetadata<?> metadata) {
        super(InvoiceType.class, metadata);
    }

}

