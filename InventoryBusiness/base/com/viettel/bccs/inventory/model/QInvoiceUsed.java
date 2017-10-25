package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;
import java.util.Date;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

/**
 * Created by pham on 9/26/2016.
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QInvoiceUsed extends EntityPathBase<InvoiceUsed> {
    private static final long serialVersionUID = 1830475631L;

    public static final QInvoiceUsed invoiceUsed = new QInvoiceUsed("invoiceUsed");

    public final StringPath InvoiceMin = createString("InvoiceMin");

    public final StringPath InvoiceMax= createString("InvoiceMax");

    public final NumberPath<Long> InvoiceId = createNumber("InvoiceId", Long.class);

    public final StringPath SerialNo = createString("SerialNo");

    public final DateTimePath<Date> fromdate = createDateTime("fromdate", Date.class);

    public final DateTimePath<Date> todate = createDateTime("todate", Date.class);

    public QInvoiceUsed(String variable) {
        super(InvoiceUsed.class, forVariable(variable));
    }

    public QInvoiceUsed(Path<? extends InvoiceUsed> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInvoiceUsed(PathMetadata<?> metadata) {
        super(InvoiceUsed.class, metadata);
    }
}