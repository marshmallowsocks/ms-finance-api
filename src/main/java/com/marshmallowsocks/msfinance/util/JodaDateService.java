package com.marshmallowsocks.msfinance.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.validation.constraints.NotNull;
import java.util.TimeZone;

public class JodaDateService implements DateService {

    private final DateTimeZone timeZone;

    public JodaDateService(@NotNull final DateTimeZone timeZone) {
        this.timeZone = timeZone;
        System.setProperty("user.timezone", timeZone.getID());
        TimeZone.setDefault(timeZone.toTimeZone());
        DateTimeZone.setDefault(timeZone);
    }
    @Override
    public DateTime now() {
        return DateTime.now(timeZone);
    }
}
