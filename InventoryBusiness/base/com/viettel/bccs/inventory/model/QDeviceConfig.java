package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QDeviceConfig is a Querydsl query type for DeviceConfig
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDeviceConfig extends EntityPathBase<DeviceConfig> {

    private static final long serialVersionUID = 685775242L;

    public static final QDeviceConfig deviceConfig = new QDeviceConfig("deviceConfig");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> newProdOfferId = createNumber("newProdOfferId", Long.class);

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> stateId = createNumber("stateId", Long.class);

    public final StringPath status = createString("status");

    public final DateTimePath<java.util.Date> updateDate = createDateTime("updateDate", java.util.Date.class);

    public final StringPath updateUser = createString("updateUser");

    public QDeviceConfig(String variable) {
        super(DeviceConfig.class, forVariable(variable));
    }

    public QDeviceConfig(Path<? extends DeviceConfig> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDeviceConfig(PathMetadata<?> metadata) {
        super(DeviceConfig.class, metadata);
    }

}

