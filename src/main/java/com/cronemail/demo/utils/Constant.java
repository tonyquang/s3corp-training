package com.cronemail.demo.utils;

public interface Constant {
    String HOST_NAME = "facebook.com";
    int MAX_TIME_ACCESS_FB = 20;

    String EMAIL_SUBJECT = "[WARNING ACCESS FACEBOOK]";
    String EMAIL_CONTENT = "You had access to Facebook over allow time, please stop and let back to your work. Regard!";

    // Every day at 12h and 16h
    String CRONJOB_EXPRESSION = "0/10 0/1 * 1/1 * ? *";
    String GROUP_ID = "quangbui";
}
