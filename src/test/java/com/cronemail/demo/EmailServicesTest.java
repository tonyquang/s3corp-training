package com.cronemail.demo;

import com.cronemail.demo.model.EmailModel;
import com.cronemail.demo.service.IEmailService;
import com.cronemail.demo.utils.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


@SpringBootTest(classes = DemoApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("/TestEmailService.properties")
class EmailServicesTest {
	@Autowired
	IEmailService iEmailService;

	private String[] emailListValid;
	private String[] emailListInValid;

	@BeforeEach
	public void setup(){
		emailListValid = new String[]{"quangbui14041999@gmail.com", "tonyquang9x@gmail.com"};
		emailListInValid = new String[]{"quang@gmail.com","quanggmail.com"};
	}

	// You must changing correct username and password in TestEmailService.properties before run test
	@Test
	void sendMailSuccessTest() {
		iEmailService.sendMail(EmailModel.builder()
				.subject(Constant.EMAIL_SUBJECT)
				.content(Constant.EMAIL_CONTENT)
				.email(Arrays.asList(emailListValid))
				.build());
	}

	// You must changing wrong username and password in TestEmailService.properties before run test
	@Test
	void sendMailAuthenticationFailTest() {
		Throwable ex = assertThrows(MailAuthenticationException.class, () -> iEmailService.sendMail(
				EmailModel.builder()
						.subject(Constant.EMAIL_SUBJECT)
						.content(Constant.EMAIL_CONTENT)
						.email(Arrays.asList(emailListValid))
						.build()
		));
		assertThat(ex.getMessage(), containsString("Too many login attempts"));
	}

	@Test
	void sendMailInvalidEmailTest() {
		Throwable ex = assertThrows(MailSendException.class, () -> iEmailService.sendMail(
				EmailModel.builder()
						.subject(Constant.EMAIL_SUBJECT)
						.content(Constant.EMAIL_CONTENT)
						.email(Arrays.asList(emailListInValid))
						.build()
		));
		System.out.println(ex.getMessage());
		assertThat(ex.getMessage(), containsString("Invalid Addresses"));
	}

}
