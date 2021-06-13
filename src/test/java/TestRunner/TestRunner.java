/**
 * 
 */
package TestRunner;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.sun.mail.smtp.SMTPTransport;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * @author padmanabha.das
 *
 */
@CucumberOptions(features = "src/test/java/Features", glue = "StepDefinitions", tags = "@tag", monochrome = true)
public class TestRunner extends AbstractTestNGCucumberTests {

	@BeforeSuite
	public void init() {
		System.out.println("Hello");
	}

	// Create the attachment
	private static final String SMTP_SERVER = "smtp.gmail.com";
	private static final String USERNAME = "padmanabhadas7319@gmail.com";
	private static final String PASSWORD = "9647100133";

	private static final String EMAIL_FROM = "padmanabhadas7319@gmail.com";
	private static final String EMAIL_TO = "padmanabhadas9647@gmail.com";
	private static final String EMAIL_TO_CC = "chayan19062000@gmail.com";

	private static final String EMAIL_SUBJECT = "Test Send Email via SMTP";
	private static final String EMAIL_TEXT = "Make My Trip Automation Report";

	@AfterSuite
	public void Mail() {

		Properties prop = System.getProperties();
		prop.put("mail.smtp.host", SMTP_SERVER); // optional, defined in SMTPTransport
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.port", "465"); // default port 25

		Session session = Session.getInstance(prop, null);
		Message msg = new MimeMessage(session);
		try {
			// from
			msg.setFrom(new InternetAddress(EMAIL_FROM));
			// to
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_TO, false));
			// cc
			msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(EMAIL_TO_CC, false));
			// subject
			msg.setSubject(EMAIL_SUBJECT);
			BodyPart messageBodyPart1 = new MimeBodyPart();
			// Set the body of email
			messageBodyPart1.setText("This is message body");
			// Create another object to add another content
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();
			// Mention the file which you want to send
			String filename = "C:\\Chayan\\HRC TECH TRACK INTERNSHIP\\Eclipse\\HRC\\Assignment\\hrcassignment4\\test-output\\emailable-report.html";
			// Create data source and pass the filename
			DataSource source = new FileDataSource(filename);
			// set the handler
			messageBodyPart2.setDataHandler(new DataHandler(source));
			// set the file
			messageBodyPart2.setFileName(filename);
			// Create object of MimeMultipart class
			Multipart multipart = new MimeMultipart();
			// add body part 1
			multipart.addBodyPart(messageBodyPart2);
			// add body part 2
			multipart.addBodyPart(messageBodyPart1);
			// set the content
			msg.setContent(multipart);
			// finally send the email
			SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
			// connect
			t.connect(SMTP_SERVER, USERNAME, PASSWORD);
			// send
			t.sendMessage(msg, msg.getAllRecipients());
			t.close();
			System.out.println("=====Email Sent=====");
			// System.out.println("Response: " + t.getLastServerResponse());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
