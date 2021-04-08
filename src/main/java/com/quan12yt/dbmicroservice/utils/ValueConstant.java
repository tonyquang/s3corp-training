package com.quan12yt.dbmicroservice.utils;

public class ValueConstant {
    public static final String EMAIL = "quan23pv@gmail.com";
    public static final String EMAIL_SUBJECT = "FACEBOOK ACCESS LIMITATION AWARE !!";
    public static final String EMAIL_TEXT = "You have passed facebook accesses limitation for today. Please focus on your work";

    //start job every 30seconds
    public static final String CRON_EXPRESSION = "0/30 * * * * ? *";

    //start job at 11am and 16pm everday
    //   public static final String CRON_EXPRESSION = "0 0 11,16 * * ?";

    private ValueConstant() {
    }
}

