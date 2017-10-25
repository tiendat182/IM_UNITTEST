package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QLockUserInfo is a Querydsl query type for LockUserInfo
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QLockUserInfo extends EntityPathBase<LockUserInfo> {

    private static final long serialVersionUID = 1315274678L;

    public static final QLockUserInfo lockUserInfo = new QLockUserInfo("lockUserInfo");

    public final NumberPath<Short> checkErp = createNumber("checkErp", Short.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> lockTypeId = createNumber("lockTypeId", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final StringPath serial = createString("serial");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final StringPath transCode = createString("transCode");

    public final DateTimePath<java.util.Date> transDate = createDateTime("transDate", java.util.Date.class);

    public final NumberPath<Long> transId = createNumber("transId", Long.class);

    public final NumberPath<Short> transStatus = createNumber("transStatus", Short.class);

    public final NumberPath<Long> transType = createNumber("transType", Long.class);

    public QLockUserInfo(String variable) {
        super(LockUserInfo.class, forVariable(variable));
    }

    public QLockUserInfo(Path<? extends LockUserInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLockUserInfo(PathMetadata<?> metadata) {
        super(LockUserInfo.class, metadata);
    }

}

