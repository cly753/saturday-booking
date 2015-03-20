package notification;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import booking.Conf;

public class Notifier {
	public static boolean notify(String msg) throws MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", Conf.getEmailHost());
		props.put("mail.smtp.port", String.valueOf(Conf.getEmailPort()));

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Conf.getEmailUser(), Conf.getEmailPass());
			}
		});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(Conf.getEmailUser()));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Conf.getEmailTo()));
		message.setSubject("Hello Subject!");
		message.setText("Hello Body!\n\n" + msg);

		Transport.send(message);
		return true;
	}
}
