package com.viettel.web.activity.timeunit;


import com.viettel.web.activity.TimeUnit;

public class YearTimeUnit
        implements TimeUnit {
    private static final Long MILLIS_PER_YEAR = Long.valueOf(31536000000L);

    public Long getNumberOfMillis() {
        return MILLIS_PER_YEAR;
    }

    public String getMessageKey(Long numberOfUnits) {
        if (numberOfUnits.longValue() == 1L) {
            return "time.unit.year";
        }
        return "time.unit.years";
    }
}
