package com.viettel.web.activity;

import com.viettel.fw.common.util.BundleUtil;
import com.viettel.fw.common.util.GetTextFromBundleHelper;
import com.viettel.web.activity.timeunit.*;
import org.springframework.stereotype.Component;

import javax.faces.bean.ManagedBean;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@ManagedBean
public class HumanTime {

    private static final List<TimeUnit> timeUnits = Arrays.asList(new TimeUnit[]{new YearTimeUnit(), new MonthTimeUnit(), new WeekTimeUnit(), new DayTimeUnit(), new HourTimeUnit(), new MinuteTimeUnit()});
    private Long baseDate;

    public HumanTime() {
    }

    public String format(Date date) {
        boolean future = true;
        this.baseDate = Long.valueOf(new Date().getTime());
        Long difference = Long.valueOf(date.getTime() - this.baseDate.longValue());
        if (difference.longValue() < 0L) {
            future = false;
            difference = Long.valueOf(-difference.longValue());
        } else if (difference.longValue() == 0L) {
            return GetTextFromBundleHelper.getText("time.unit.just.now");
        }
        String unitMessage = getUnitMessage(difference);

        String messageKey = null;
        if (future) {
            messageKey = "time.unit.future";
        } else {
            messageKey = "time.unit.past";
        }
        return MessageFormat.format(GetTextFromBundleHelper.getText(messageKey), unitMessage);
    }

    private String getUnitMessage(Long difference) {
        String unitMessage = null;
        TimeUnit unitToUse = null;
        TimeUnit currentUnit = null;
        for (int i = 0; (i < timeUnits.size()) && (unitToUse == null); i++) {
            currentUnit = (TimeUnit) timeUnits.get(i);
            if (currentUnit.getNumberOfMillis().longValue() <= difference.longValue()) {
                unitToUse = currentUnit;
            }
        }
        if (unitToUse == null) {
            unitMessage = GetTextFromBundleHelper.getText("time.unit.moments");
        } else {
            Long numberOfUnits = Long.valueOf((difference.longValue() - difference.longValue() % unitToUse.getNumberOfMillis().longValue()) / unitToUse.getNumberOfMillis().longValue());
            unitMessage = MessageFormat.format(GetTextFromBundleHelper.getText(unitToUse.getMessageKey(numberOfUnits)), numberOfUnits);
        }
        return unitMessage;
    }


}
