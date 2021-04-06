package com.quan12yt.trackingcronjob.unittest.service;

import com.quan12yt.trackingcronjob.exception.DataNotFoundException;
import com.quan12yt.trackingcronjob.model.UserActivity;
import com.quan12yt.trackingcronjob.model.Users;
import com.quan12yt.trackingcronjob.repository.UserActivityRepository;
import com.quan12yt.trackingcronjob.repository.UserRepository;
import com.quan12yt.trackingcronjob.service.imp.PdfServiceImp;
import com.quan12yt.trackingcronjob.service.imp.UserServiceImp;
import com.quan12yt.trackingcronjob.util.CsvUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.util.Collections;

import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PdfServiceTest {

    @Autowired
    private PdfServiceImp pdfServiceImp;
    @MockBean
    CsvUtils csvUtils;
    @MockBean
    private UserActivityRepository activityRepository;

    @TestConfiguration
    public static class EmailServiceConfiguration {
        @Bean
        PdfServiceImp pdfServiceImp() {
            return new PdfServiceImp();
        }
    }

    @Test
    public void emptyExportTest() {
        when(activityRepository.findAll()).thenReturn(Collections.emptyList());
        Throwable ex = Assert.assertThrows(DataNotFoundException.class, () -> pdfServiceImp.exportCsv());
        Assert.assertSame("Cant find any user activities to export", ex.getMessage());
    }

    @Test
    public void exportToCsvSucceedTest() {
        when(activityRepository.findAll()).thenReturn(Collections.singletonList(new UserActivity("", "", "", 4L, 6)));
        Assert.assertTrue(pdfServiceImp.exportCsv() != null);
    }

}
