package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QEquipment is a Querydsl query type for Equipment
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEquipment extends EntityPathBase<Equipment> {

    private static final long serialVersionUID = -986411908L;

    public static final QEquipment equipment = new QEquipment("equipment");

    public final StringPath code = createString("code");

    public final StringPath description = createString("description");

    public final NumberPath<Long> equipmentId = createNumber("equipmentId", Long.class);

    public final StringPath equipmentType = createString("equipmentType");

    public final NumberPath<Long> equipmentVendorId = createNumber("equipmentVendorId", Long.class);

    public final StringPath name = createString("name");

    public final StringPath status = createString("status");

    public QEquipment(String variable) {
        super(Equipment.class, forVariable(variable));
    }

    public QEquipment(Path<? extends Equipment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEquipment(PathMetadata<?> metadata) {
        super(Equipment.class, metadata);
    }

}

