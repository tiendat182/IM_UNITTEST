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
public class QKcsLogSerial extends EntityPathBase<KcsLogSerial>  {

    private static final long serialVersionUID = 1L;

    public static final QKcsLogSerial kcsLogSerial = new QKcsLogSerial("kcsLogSerial");

    public final NumberPath<Long> kcsLogDetailId = createNumber("kcsLogDetailId", Long.class);
    public final DateTimePath<Date> createDate = createDateTime("createDate", Date.class);
    public final NumberPath<Long> serial = createNumber("serial", Long.class);

    public QKcsLogSerial(String variable) {
        super(KcsLogSerial.class, forVariable(variable));
    }

    public QKcsLogSerial(Path<? extends KcsLogSerial> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKcsLogSerial(PathMetadata<?> metadata) {
        super(KcsLogSerial.class, metadata);
    }
}
