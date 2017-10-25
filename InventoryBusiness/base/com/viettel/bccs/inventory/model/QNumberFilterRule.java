package com.viettel.bccs.inventory.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QNumberFilterRule is a Querydsl query type for NumberFilterRule
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QNumberFilterRule extends EntityPathBase<NumberFilterRule> {

    private static final long serialVersionUID = 1956486159L;

    public static final QNumberFilterRule numberFilterRule = new QNumberFilterRule("numberFilterRule");

    public final StringPath condition = createString("condition");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath createUser = createString("createUser");

    public final NumberPath<Long> filterRuleId = createNumber("filterRuleId", Long.class);

    public final NumberPath<Long> groupFilterRuleId = createNumber("groupFilterRuleId", Long.class);

    public final DateTimePath<java.util.Date> lastUpdateTime = createDateTime("lastUpdateTime", java.util.Date.class);

    public final StringPath lastUpdateUser = createString("lastUpdateUser");

    public final StringPath maskMapping = createString("maskMapping");

    public final StringPath name = createString("name");

    public final StringPath notes = createString("notes");

    public final NumberPath<Long> prodOfferId = createNumber("prodOfferId", Long.class);

    public final NumberPath<Long> ruleOrder = createNumber("ruleOrder", Long.class);

    public final StringPath status = createString("status");

    public QNumberFilterRule(String variable) {
        super(NumberFilterRule.class, forVariable(variable));
    }

    public QNumberFilterRule(Path<? extends NumberFilterRule> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNumberFilterRule(PathMetadata<?> metadata) {
        super(NumberFilterRule.class, metadata);
    }

}

