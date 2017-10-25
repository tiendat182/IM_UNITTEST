package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QLockUserType is a Querydsl query type for LockUserType
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QLockUserType extends EntityPathBase<LockUserType> {

    private static final long serialVersionUID = 1315613250L;

    public static final QLockUserType lockUserType = new QLockUserType("lockUserType");

    public final StringPath actionUrl = createString("actionUrl");

    public final ComparablePath<Character> auto = createComparable("auto", Character.class);

    public final NumberPath<Long> checkRange = createNumber("checkRange", Long.class);

    public final StringPath description = createString("description");

    public final StringPath effectRole = createString("effectRole");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath params = createString("params");

    public final StringPath redirectUrl = createString("redirectUrl");

    public final StringPath sqlCheckTrans = createString("sqlCheckTrans");

    public final StringPath sqlCmd = createString("sqlCmd");

    public final ComparablePath<Character> status = createComparable("status", Character.class);

    public final NumberPath<Long> validRange = createNumber("validRange", Long.class);

    public final StringPath warningContent = createString("warningContent");

    public QLockUserType(String variable) {
        super(LockUserType.class, forVariable(variable));
    }

    public QLockUserType(Path<? extends LockUserType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLockUserType(PathMetadata<?> metadata) {
        super(LockUserType.class, metadata);
    }

}

