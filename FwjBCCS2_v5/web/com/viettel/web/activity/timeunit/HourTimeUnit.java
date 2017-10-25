package com.viettel.web.activity.timeunit;


import com.viettel.web.activity.TimeUnit;

public class HourTimeUnit
        implements TimeUnit {
    private static final Long MILLIS_PER_HOUR = Long.valueOf(3600000L);

    public Long getNumberOfMillis() {
        return MILLIS_PER_HOUR;
    }

    public String getMessageKey(Long numberOfUnits) {
        if (numberOfUnits.longValue() == 1L) {
            return "time.unit.hour";
        }
        return "time.unit.hours";
    }
}
