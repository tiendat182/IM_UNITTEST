package com.viettel.web.activity.timeunit;


import com.viettel.web.activity.TimeUnit;

public class DayTimeUnit
        implements TimeUnit {
    private static final Long MILLIS_PER_DAY = Long.valueOf(86400000L);

    public Long getNumberOfMillis() {
        return MILLIS_PER_DAY;
    }

    public String getMessageKey(Long numberOfUnits) {
        if (numberOfUnits.longValue() == 1L) {
            return "time.unit.day";
        }
        return "time.unit.days";
    }
}
