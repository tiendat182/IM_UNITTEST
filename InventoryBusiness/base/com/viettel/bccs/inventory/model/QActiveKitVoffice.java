package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QActiveKitVoffice is a Querydsl query type for ActiveKitVoffice
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QActiveKitVoffice extends EntityPathBase<ActiveKitVoffice> {

    private static final long serialVersionUID = 915040244L;

    public static final QActiveKitVoffice activeKitVoffice = new QActiveKitVoffice("activeKitVoffice");

    public final StringPath accountName = createString("accountName");

    public final StringPath accountPass = createString("accountPass");

    public final StringPath actionCode = createString("actionCode");

    public final NumberPath<Long> activeKitVofficeId = createNumber("activeKitVofficeId", Long.class);

    public final NumberPath<Long> changeModelTransId = createNumber("changeModelTransId", Long.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> createUserId = createNumber("createUserId", Long.class);

    public final DateTimePath<java.util.Date> lastModify = createDateTime("lastModify", java.util.Date.class);

    public final StringPath note = createString("note");

    public final StringPath prefixTemplate = createString("prefixTemplate");

    public final StringPath signUserList = createString("signUserList");

    public final StringPath status = createString("status");

    public final NumberPath<Long> stockTransActionId = createNumber("stockTransActionId", Long.class);

    public QActiveKitVoffice(String variable) {
        super(ActiveKitVoffice.class, forVariable(variable));
    }

    public QActiveKitVoffice(Path<? extends ActiveKitVoffice> path) {
        super(path.getType(), path.getMetadata());
    }

    public QActiveKitVoffice(PathMetadata<?> metadata) {
        super(ActiveKitVoffice.class, metadata);
    }

}

