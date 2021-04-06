package com.quan12yt.trackingcronjob.util;

import com.quan12yt.trackingcronjob.exception.VariablesUnacceptedException;
import com.quan12yt.trackingcronjob.model.UserActivity;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CsvUtils {

    public static ByteArrayInputStream toCSV(List<UserActivity> activities) {
        if(activities == null || activities.isEmpty()){
            throw new VariablesUnacceptedException("List activities cant be empty or null");
        }
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (UserActivity activity : activities) {
                List<String> data = Arrays.asList(
                        String.valueOf(activity.getUserId()),
                        activity.getUrl(),
                        activity.getDate(),
                        String.valueOf(activity.getTotalTime()),
                        String.valueOf(activity.getCount())
                );
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }

}

