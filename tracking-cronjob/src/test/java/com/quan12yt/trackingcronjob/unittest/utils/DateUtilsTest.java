package com.quan12yt.trackingcronjob.unittest.utils;

import com.quan12yt.trackingcronjob.util.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtilsTest {

    @Test
    public void convertDateToStringSuccessTest() {
        Date date = new Date();
        String expect = new SimpleDateFormat("MM-dd-yyyy").format(date);
        String actual = DateUtils.convertDateToString(date);
        Assert.assertEquals(expect, actual);
    }

    @Test
    public void nullDateTest() {
        String actual = DateUtils.convertDateToString(null);
        Assert.assertEquals("", actual);
    }
}
