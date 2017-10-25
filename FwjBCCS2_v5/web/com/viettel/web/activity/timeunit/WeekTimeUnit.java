package com.viettel.web.activity.timeunit;


import com.viettel.web.activity.TimeUnit;

public class WeekTimeUnit
        implements TimeUnit {
    private static final Long MILLIS_PER_WEEK = Long.valueOf(604800000L);

    public Long getNumberOfMillis() {
        return MILLIS_PER_WEEK;
    }

    public String getMessageKey(Long numberOfUnits) {
        if (numberOfUnits.longValue() == 1L) {
            return "time.unit.week";
        }
        return "time.unit.weeks";
    }
}
