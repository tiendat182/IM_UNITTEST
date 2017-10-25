package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QEquipmentVendor is a Querydsl query type for EquipmentVendor
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEquipmentVendor extends EntityPathBase<EquipmentVendor> {

    private static final long serialVersionUID = 782487780L;

    public static final QEquipmentVendor equipmentVendor = new QEquipmentVendor("equipmentVendor");

    public final StringPath description = createString("description");

    public final NumberPath<Long> equipmentVendorId = createNumber("equipmentVendorId", Long.class);

    public final StringPath status = createString("status");

    public final StringPath vendorCode = createString("vendorCode");

    public final StringPath vendorName = createString("vendorName");

    public QEquipmentVendor(String variable) {
        super(EquipmentVendor.class, forVariable(variable));
    }

    public QEquipmentVendor(Path<? extends EquipmentVendor> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEquipmentVendor(PathMetadata<?> metadata) {
        super(EquipmentVendor.class, metadata);
    }

}

