package com.quan12yt.dbmicroservice.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private DateUtils(){}

    public static String convertDateToString(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("MM-dd-yyyy").format(date);
    }
}
