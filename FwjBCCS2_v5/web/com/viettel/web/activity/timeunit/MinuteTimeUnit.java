package com.viettel.web.activity.timeunit;


import com.viettel.web.activity.TimeUnit;

public class MinuteTimeUnit
        implements TimeUnit {
    private static final Long MILLIS_PER_MINUTE = Long.valueOf(60000L);

    public Long getNumberOfMillis() {
        return MILLIS_PER_MINUTE;
    }

    public String getMessageKey(Long numberOfUnits) {
        if (numberOfUnits.longValue() == 1L) {
            return "time.unit.minute";
        }
        return "time.unit.minutes";
    }
}
