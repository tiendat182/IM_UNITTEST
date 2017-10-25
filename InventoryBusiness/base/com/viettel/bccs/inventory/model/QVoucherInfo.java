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
 * Created by HoangAnh on 8/31/2016.
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QVoucherInfo extends EntityPathBase<QVoucherInfo> {

    private static final long serialVersionUID = 1369314517L;

    public static final QVoucherInfo voucherInfo = new QVoucherInfo("voucherInfo");

    public final NumberPath<Long> voucherInfoId = createNumber("voucherInfoId", Long.class);

    public final StringPath serial = createString("serial");

    public final StringPath pass = createString("pass");

    public final NumberPath<Short> status = createNumber("status", Short.class);

    public final DateTimePath<Date> fromDate = createDateTime("fromDate", Date.class);

    public final DateTimePath<Date> toDate = createDateTime("toDate", Date.class);

    public final StringPath object = createString("object");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final DateTimePath<Date> createDate = createDateTime("createDate", Date.class);

    public final DateTimePath<Date> lastUpdate = createDateTime("lastUpdate", Date.class);

    public final StringPath staffCode = createString("staffCode");

    public final NumberPath<Long> statusConnect = createNumber("statusConnect", Long.class);

    public final NumberPath<Short> bonusStatus = createNumber("bonusStatus", Short.class);

    public QVoucherInfo(String variable) {
        super(QVoucherInfo.class, forVariable(variable));
    }

    public QVoucherInfo(Path<? extends QVoucherInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVoucherInfo(PathMetadata<?> metadata) {
        super(QVoucherInfo.class, metadata);
    }
}
