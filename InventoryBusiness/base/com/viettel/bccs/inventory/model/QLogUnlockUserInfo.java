package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

/**
 * Created by Hellpoethero on 29/08/2016.
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QLogUnlockUserInfo extends EntityPathBase<LogUnlockUserInfo> {

    private static final long serialVersionUID = 1315274678L;

    public static final QLogUnlockUserInfo logUnlockUserInfo = new QLogUnlockUserInfo("logUnlockUserInfo");

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

    public QLogUnlockUserInfo(String variable) {
        super(LogUnlockUserInfo.class, forVariable(variable));
    }

    public QLogUnlockUserInfo(Path<? extends LogUnlockUserInfo> path) { super(path.getType(), path.getMetadata()); }

    public QLogUnlockUserInfo(PathMetadata<?> metadata) {
        super(LogUnlockUserInfo.class, metadata);
    }
}
