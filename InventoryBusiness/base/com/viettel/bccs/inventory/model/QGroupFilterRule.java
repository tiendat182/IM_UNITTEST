package com.viettel.bccs.inventory.model;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import java.util.Date;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QGroupFilterRule is a Querydsl query type for GroupFilterRule
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGroupFilterRule extends EntityPathBase<GroupFilterRule> {
    public final StringPath groupType = createString("groupType");

    private static final long serialVersionUID = -304310559L;

    public static final QGroupFilterRule groupFilterRule = new QGroupFilterRule("groupFilterRule");

    public final StringPath groupFilterRuleCode = createString("groupFilterRuleCode");

    public final NumberPath<Long> groupFilterRuleId = createNumber("groupFilterRuleId", Long.class);

    public final StringPath name = createString("name");

    public final StringPath notes = createString("notes");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final NumberPath<Long> telecomServiceId = createNumber("telecomServiceId", Long.class);

    public final DatePath<Date> lastUpdateTime = createDate("lastUpdateTime", Date.class);

    public final ComparablePath<Character> status = createComparable("status", Character.class);

    public QGroupFilterRule(String variable) {
        super(GroupFilterRule.class, forVariable(variable));
    }

    public QGroupFilterRule(Path<? extends GroupFilterRule> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGroupFilterRule(PathMetadata<?> metadata) {
        super(GroupFilterRule.class, metadata);
    }



}

