package net.asher.book.util;

import java.util.Properties;
import java.util.Random;

import javax.annotation.Resource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.asher.book.domain.Email;
import net.asher.book.service.UserService;


@Component("mailUtil")
public class MailUtil {
	
	@Resource(name="userService")
	UserService userService;
	
	@Value("#{mailCfg['host']}")
	private String host;
	
	@Value("#{mailCfg['userName']}")
	private String userName;
	
	@Value("#{mailCfg['password']}")
	private String password;
	
	@Value("#{mailCfg['port']}")
	private int port;
	
	@Value("#{mailCfg['from']}")
	private String from;
	
	
	public void sendMail(Email email) throws AddressException, MessagingException {
		
		if(email.getAccount() == null)  return;
		
		String recipient = email.getAccount().getEmail();
		String subject = email.getSubject();
		String body = email.getEmailBody();
		
		Properties props = System.getProperties();
		// SMTP 서버 정보 설정
		props.put("mail.smtp.host", host); 
		props.put("mail.smtp.port", port); 
		props.put("mail.smtp.auth", "true"); 
				
		if("smtp.naver.com".equals(host)) {
			props.put("mail.smtp.starttls.enable", "true"); 
		}
		else if("smtp.gmail.com".equals(host)) {
			props.put("mail.smtp.ssl.enable", "true"); 
			props.put("mail.smtp.ssl.trust", host);
		}
		
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			String un = userName;
			String pw = password;
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(un, pw);
			}
		});
		
		session.setDebug(true);
		
		Message mimeMessage = new MimeMessage(session);
		mimeMessage.setFrom(new InternetAddress(from)); 
		
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
		
		mimeMessage.setSubject(subject);
		mimeMessage.setContent(body, "text/html; charset=UTF-8");
		Transport.send(mimeMessage);
	}
}
