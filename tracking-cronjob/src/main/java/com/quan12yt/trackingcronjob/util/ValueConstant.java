package com.quan12yt.trackingcronjob.util;

public final class ValueConstant {
    public static final String EMAIL = "quan26pv@gmail.com";
    public static final String EMAIL_SUBJECT = "FACEBOOK ACCESS LIMITATION AWARE !!";
    public static final String EMAIL_TEXT = "You have passed facebook accesses limitation for today. Please focus on your work";

    //start job every 30seconds
    public static final String CRON_EXPRESSION = "0/5 0/1 * 1/1 * ? *";

    //start job at 11am and 16pm everday
 //   public static final String CRON_EXPRESSION = "0 0 11,16 * * ?";

    private ValueConstant() {
    }
}
