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
 * QArea is a Querydsl query type for Area
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QArea extends EntityPathBase<Area> {

    private static final long serialVersionUID = 1502874847L;

    public static final QArea area = new QArea("area");

    public final StringPath areaCode = createString("areaCode");

    public final StringPath areaGroup = createString("areaGroup");

    public final StringPath center = createString("center");

    public final DateTimePath<java.util.Date> createDatetime = createDateTime("createDatetime", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath district = createString("district");

    public final StringPath fullName = createString("fullName");

    public final StringPath name = createString("name");

    public final StringPath parentCode = createString("parentCode");

    public final StringPath precinct = createString("precinct");

    public final StringPath province = createString("province");

    public final StringPath provinceCode = createString("provinceCode");

    public final StringPath pstnCode = createString("pstnCode");

    public final NumberPath<Short> status = createNumber("status", Short.class);

    public final StringPath streetBlock = createString("streetBlock");

    public final DateTimePath<java.util.Date> updateDatetime = createDateTime("updateDatetime", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");

    public QArea(String variable) {
        super(Area.class, forVariable(variable));
    }

    public QArea(Path<? extends Area> path) {
        super(path.getType(), path.getMetadata());
    }

    public QArea(PathMetadata<?> metadata) {
        super(Area.class, metadata);
    }

}

