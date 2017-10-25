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
public class QKcsLog extends EntityPathBase<KcsLog>  {

    private static final long serialVersionUID = 1L;

    public static final QKcsLog kcsLog = new QKcsLog("kcsLog");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);
    public final NumberPath<Long> ownerType = createNumber("ownerType", Long.class);
    public final DateTimePath<Date> createDate = createDateTime("createDate", Date.class);
    public final NumberPath<Long> status = createNumber("status", Long.class);

    public QKcsLog(String variable) {
        super(KcsLog.class, forVariable(variable));
    }

    public QKcsLog(Path<? extends KcsLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKcsLog(PathMetadata<?> metadata) {
        super(KcsLog.class, metadata);
    }
}
