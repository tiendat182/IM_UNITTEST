package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QInvoiceTemplateType is a Querydsl query type for InvoiceTemplateType
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QInvoiceTemplateType extends EntityPathBase<InvoiceTemplateType> {

    private static final long serialVersionUID = 1237619727L;

    public static final QInvoiceTemplateType invoiceTemplateType = new QInvoiceTemplateType("invoiceTemplateType");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> invoiceTemplateTypeId = createNumber("invoiceTemplateTypeId", Long.class);

    public final StringPath invoiceType = createString("invoiceType");

    public final StringPath name = createString("name");

    public final StringPath status = createString("status");

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");

    public QInvoiceTemplateType(String variable) {
        super(InvoiceTemplateType.class, forVariable(variable));
    }

    public QInvoiceTemplateType(Path<? extends InvoiceTemplateType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInvoiceTemplateType(PathMetadata<?> metadata) {
        super(InvoiceTemplateType.class, metadata);
    }

}

