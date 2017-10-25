package com.viettel.web.activity.timeunit;


import com.viettel.web.activity.TimeUnit;

public class MonthTimeUnit
        implements TimeUnit {
    private static final Long MILLIS_PER_MONTH = Long.valueOf(2592000000L);

    public Long getNumberOfMillis() {
        return MILLIS_PER_MONTH;
    }

    public String getMessageKey(Long numberOfUnits) {
        if (numberOfUnits.longValue() == 1L) {
            return "time.unit.month";
        }
        return "time.unit.months";
    }
}
