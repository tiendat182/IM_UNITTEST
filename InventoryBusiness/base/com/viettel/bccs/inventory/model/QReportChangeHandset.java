package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QReportChangeHandset is a Querydsl query type for ReportChangeHandset
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QReportChangeHandset extends EntityPathBase<ReportChangeHandset> {

    private static final long serialVersionUID = 357938269L;

    public static final QReportChangeHandset reportChangeHandset = new QReportChangeHandset("reportChangeHandset");

    public final NumberPath<Long> adjustAmount = createNumber("adjustAmount", Long.class);

    public final NumberPath<Long> changeType = createNumber("changeType", Long.class);

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath custName = createString("custName");

    public final StringPath custTel = createString("custTel");

    public final StringPath damageGoodStatus = createString("damageGoodStatus");

    public final NumberPath<Long> devStaffId = createNumber("devStaffId", Long.class);

    public final NumberPath<Long> prodOfferIdNew = createNumber("prodOfferIdNew", Long.class);

    public final NumberPath<Long> prodOfferIdOld = createNumber("prodOfferIdOld", Long.class);

    public final NumberPath<Long> reportChangeHandsetId = createNumber("reportChangeHandsetId", Long.class);

    public final StringPath serialNew = createString("serialNew");

    public final StringPath serialOld = createString("serialOld");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final NumberPath<Long> staffId = createNumber("staffId", Long.class);

    public final NumberPath<Long> stockTransId = createNumber("stockTransId", Long.class);

    public QReportChangeHandset(String variable) {
        super(ReportChangeHandset.class, forVariable(variable));
    }

    public QReportChangeHandset(Path<? extends ReportChangeHandset> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportChangeHandset(PathMetadata<?> metadata) {
        super(ReportChangeHandset.class, metadata);
    }

}

