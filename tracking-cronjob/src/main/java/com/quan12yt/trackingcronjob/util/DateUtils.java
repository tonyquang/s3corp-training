package com.quan12yt.trackingcronjob.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String convertDateToString(Date date){
        return new SimpleDateFormat("MM-dd-yyyy").format(date);
    }
}
