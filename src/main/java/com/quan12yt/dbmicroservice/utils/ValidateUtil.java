package com.quan12yt.dbmicroservice.utils;

import java.time.LocalDateTime;
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

    public static boolean isValidMonthAndYear(String month, String year){
        try {
            Integer monthTemp = Integer.parseInt(month);
            Integer yearTemp = Integer.parseInt(year);
            return ((monthTemp > 0 && monthTemp < 13)) && (yearTemp > 2000 && yearTemp <= LocalDateTime.now().getYear());
        }catch (Exception e) {
            return false;
        }
    }

    private ValidateUtil() {
    }
}
