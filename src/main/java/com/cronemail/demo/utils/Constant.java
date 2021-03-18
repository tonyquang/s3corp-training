package com.cronemail.demo.utils;

public enum Constant {
    HOST_NAME("facebook.com"),
    MAX_TIME_ACCESS_FB(20),

    EMAIL_SUBJECT("[WARNING ACCESS FACEBOOK]"),
    EMAIL_CONTENT("You had access to Facebook over allow time, please stop and let back to your work. Regard!"),

    // start at 12h, and cron job with run every 4 hours
    CRONJOB_EXPRESSION("0 0 12/4 1/1 * ? *");

    Object myConstant;

    Constant(Object myConstant) {
        this.myConstant = myConstant;
    }
}
