package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QVofficeRole is a Querydsl query type for VofficeRole
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QVofficeRole extends EntityPathBase<VofficeRole> {

    private static final long serialVersionUID = -640582218L;

    public static final QVofficeRole vofficeRole = new QVofficeRole("vofficeRole");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final DateTimePath<java.util.Date> lastUpdateTime = createDateTime("lastUpdateTime", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final StringPath roleCode = createString("roleCode");

    public final StringPath roleName = createString("roleName");

    public final StringPath status = createString("status");

    public final NumberPath<Long> vofficeRoleId = createNumber("vofficeRoleId", Long.class);

    public QVofficeRole(String variable) {
        super(VofficeRole.class, forVariable(variable));
    }

    public QVofficeRole(Path<? extends VofficeRole> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVofficeRole(PathMetadata<?> metadata) {
        super(VofficeRole.class, metadata);
    }

}

