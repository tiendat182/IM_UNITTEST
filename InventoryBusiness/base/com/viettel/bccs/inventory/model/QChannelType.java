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
 * QChannelType is a Querydsl query type for ChannelType
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QChannelType extends EntityPathBase<ChannelType> {

    private static final long serialVersionUID = -2066355637L;

    public static final QChannelType channelType = new QChannelType("channelType");

    public final NumberPath<Long> allowAddBatch = createNumber("allowAddBatch", Long.class);

    public final NumberPath<Long> channelTypeId = createNumber("channelTypeId", Long.class);

    public final StringPath checkComm = createString("checkComm");

    public final StringPath code = createString("code");

    public final DateTimePath<Date> createDatetime = createDateTime("createDatetime", Date.class);

    public final StringPath createUser = createString("createUser");

    public final StringPath discountPolicyDefaut = createString("discountPolicyDefaut");

    public final NumberPath<Long> groupChannelId = createNumber("groupChannelId", Long.class);

    public final NumberPath<Long> groupChannelTypeId = createNumber("groupChannelTypeId", Long.class);

    public final NumberPath<Long> isCollChannel = createNumber("isCollChannel", Long.class);

    public final NumberPath<Long> isNotBlankCode = createNumber("isNotBlankCode", Long.class);

    public final NumberPath<Long> isVhrChannel = createNumber("isVhrChannel", Long.class);

    public final StringPath isVtUnit = createString("isVtUnit");

    public final StringPath name = createString("name");

    public final StringPath objectType = createString("objectType");

    public final StringPath pricePolicyDefaut = createString("pricePolicyDefaut");

    public final NumberPath<Long> status = createNumber("status", Long.class);

    public final StringPath stockReportTemplate = createString("stockReportTemplate");

    public final NumberPath<Long> stockType = createNumber("stockType", Long.class);

    public final StringPath suffixObjectCode = createString("suffixObjectCode");

    public final NumberPath<Long> totalDebit = createNumber("totalDebit", Long.class);

    public final StringPath updateBlankCodeRole = createString("updateBlankCodeRole");

    public final DateTimePath<Date> updateDatetime = createDateTime("updateDatetime", Date.class);

    public final StringPath updateObjectInfoRole = createString("updateObjectInfoRole");

    public final StringPath updateShopRole = createString("updateShopRole");

    public final StringPath updateStaffOwnerRole = createString("updateStaffOwnerRole");

    public final StringPath updateUser = createString("updateUser");

    public QChannelType(String variable) {
        super(ChannelType.class, forVariable(variable));
    }

    public QChannelType(Path<? extends ChannelType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChannelType(PathMetadata<?> metadata) {
        super(ChannelType.class, metadata);
    }

}

