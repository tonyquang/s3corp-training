package com.quan12yt.trackingcronjob.unittest.utils;

import com.quan12yt.trackingcronjob.exception.VariablesUnacceptedException;
import com.quan12yt.trackingcronjob.util.CsvUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class CsvUtilsTest {

    @Test
    public void writeEmptyListCsv(){
        Throwable ex = Assert.assertThrows(VariablesUnacceptedException.class, () -> CsvUtils.toCSV(Collections.emptyList()));
        Assert.assertSame("List activities cant be empty or null", ex.getMessage());
    }
    @Test
    public void writeNullListCsv(){
        Throwable ex = Assert.assertThrows(VariablesUnacceptedException.class, () -> CsvUtils.toCSV(null));
        Assert.assertSame("List activities cant be empty or null", ex.getMessage());
    }
}
