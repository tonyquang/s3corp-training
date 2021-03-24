package com.quan12yt.trackingcronjob.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private DateUtils(){}

    public static String convertDateToString(Date date) {
        if (date != null) {
            return new SimpleDateFormat("MM-dd-yyyy").format(date);
        }
        return "";
    }
}
