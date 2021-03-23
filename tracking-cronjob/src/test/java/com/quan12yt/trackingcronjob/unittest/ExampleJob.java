package com.quan12yt.trackingcronjob.unittest;

import com.quan12yt.trackingcronjob.model.JobMapData;
import lombok.Data;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class ExampleJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(ExampleJob.class);

    private Integer integer;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobMapData jobInfo = (JobMapData) jobExecutionContext.getMergedJobDataMap().get("mapData");
        jobInfo.setAccessCount(6);
        logger.info("asdasd");
    }

}
