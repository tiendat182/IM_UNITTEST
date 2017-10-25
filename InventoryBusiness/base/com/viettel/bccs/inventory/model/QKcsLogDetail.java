package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;
import java.util.Date;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

/**
 * Created by Hellpoethero on 07/09/2016.
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QKcsLogDetail extends EntityPathBase<QKcsLogDetail>  {

    private static final long serialVersionUID = 1L;

    public static final QKcsLogDetail kcsLogDetail = new QKcsLogDetail("kcsLogDetail");

    public final NumberPath<Long> kcsLogId = createNumber("kcsLogId", Long.class);
    public final NumberPath<Long> stockModelId = createNumber("stockModelId", Long.class);
    public final DateTimePath<Date> createDate = createDateTime("createDate", Date.class);
    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);
    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);
    public final NumberPath<Long> status = createNumber("status", Long.class);

    public QKcsLogDetail(String variable) {
        super(QKcsLogDetail.class, forVariable(variable));
    }

    public QKcsLogDetail(Path<? extends QKcsLogDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKcsLogDetail(PathMetadata<?> metadata) {
        super(QKcsLogDetail.class, metadata);
    }
}
