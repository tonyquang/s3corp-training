package com.quan12yt.trackingcronjob.util;

import org.thymeleaf.util.DateUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {

    public static boolean isEmptyOrNull(String date, String url) {
        return (date == null || url == null || date.isEmpty() || url.isEmpty());
    }

    public static boolean isEmail(String email) {
        Matcher matcher = Pattern
                .compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}").matcher(email);
        return matcher.find();
    }

    public static boolean isValidMonth(String month){
        try {
            Integer temp = Integer.parseInt(month);
            return (temp > 0 && temp < 13);
        }catch (Exception e) {
            return false;
        }
    }

    private ValidateUtil() {
    }
}
