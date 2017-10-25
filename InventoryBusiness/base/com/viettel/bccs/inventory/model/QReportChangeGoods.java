package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QReportChangeGoods is a Querydsl query type for ReportChangeGoods
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QReportChangeGoods extends EntityPathBase<ReportChangeGoods> {

    private static final long serialVersionUID = -867171168L;

    public static final QReportChangeGoods reportChangeGoods = new QReportChangeGoods("reportChangeGoods");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> dateSendSms = createDateTime("dateSendSms", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath isdn = createString("isdn");

    public final StringPath prodOfferCode = createString("prodOfferCode");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final StringPath prodOfferName = createString("prodOfferName");

    public final NumberPath<Long> reasonId = createNumber("reasonId", Long.class);

    public final StringPath serialNew = createString("serialNew");

    public final StringPath serialOld = createString("serialOld");

    public final StringPath shopCode = createString("shopCode");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final StringPath shopName = createString("shopName");

    public final StringPath staffCode = createString("staffCode");

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final StringPath staffName = createString("staffName");

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public final NumberPath<Long> telecomServiceId = createNumber("telecomServiceId", Long.class);

    public final NumberPath<Long> totalSendSms = createNumber("totalSendSms", Long.class);

    public QReportChangeGoods(String variable) {
        super(ReportChangeGoods.class, forVariable(variable));
    }

    public QReportChangeGoods(Path<? extends ReportChangeGoods> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportChangeGoods(PathMetadata<?> metadata) {
        super(ReportChangeGoods.class, metadata);
    }

}

