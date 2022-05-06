package mailSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
public class MailSender  {
	private final Properties properties = new Properties();

	private Session session;

	public MailSender() {
	}

	private void init(String password, String emisor) {

		properties.put("mail.smtp.host", "smtp-mail.outlook.com");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.debug", "true");

		Authenticator auth = new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emisor, password);
			}
		};

		session = Session.getInstance(properties, auth);
	}

	private boolean sendEmail(String emisor, String password, String HTML_Message, String subject, String filePath,
			String recipient, String nameFrom) {
		boolean connection = false;
		try {
			Message msg = new MimeMessage(session);

			try {
				msg.setFrom(new InternetAddress(emisor, nameFrom));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			InternetAddress[] toAddresses = { new InternetAddress(recipient) };
			msg.setRecipients(Message.RecipientType.TO, toAddresses);
			msg.setSubject(subject);

			// creates message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();

			// BODY
			messageBodyPart.setContent(HTML_Message, "text/html; charset=ISO_8859_1");
			multipart.addBodyPart(messageBodyPart);

			// ATTACHMENT
			if (filePath != null) {
				MimeBodyPart attachPart = new MimeBodyPart();

				try {
					attachPart.attachFile(filePath);
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				multipart.addBodyPart(attachPart);
			} else {
				System.out.println("no hay adjunto");
			}

			// join parts
			msg.setContent(multipart);
			try {
				Transport t = session.getTransport("smtp");
				t.connect(emisor, password);
				if (t.isConnected()) {
					t.sendMessage(msg, msg.getAllRecipients());
					connection = true;
				} else {
					connection = false;
				}
				t.close();
			} catch (Exception e) {
				throw e;
			}

		} catch (MessagingException me) {
			me.printStackTrace();

		}
		return connection;

	}

	public String loadHTML_Template(String filePath) {

		//byte HTML_Template[];

		try {
			return new String(Files.readAllBytes(Path.of(filePath)),StandardCharsets.ISO_8859_1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}

	}

	public static ArrayList<Agency> loadTemplateArguments(String filePath) {
		ArrayList<Agency> empresas = new ArrayList<Agency>();

		String line;
		try {
			BufferedReader readFile = Files.newBufferedReader(Paths.get(filePath));
			
			//BufferedReader in = new BufferedReader( new InputStreamReader( new FileInputStream(filePath), "UTF8"));

			while ((line = readFile.readLine()) != null) {

				String elements[] = line.split("#");
				String email = elements[0];
				ArrayList<String> arguments = new ArrayList<String>();
				for (int i = 1; i < elements.length; i++)
					arguments.add(elements[i]);

				Agency empresa = new Agency(email, arguments);
				empresas.add(empresa);
			}
			readFile.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return empresas;
	}

	public boolean sendEmailToAgencies(String emisor, String password, String pathToTemplate, String PathToArgumentsCSV,
			String pathToAttachment, String subject, String nameFrom) {

		String template = loadHTML_Template(pathToTemplate);

		ArrayList<Agency> elements = loadTemplateArguments(PathToArgumentsCSV);
		init(emisor, password);

		for (Agency agency : elements) {

			String formatedTemplate = template;

			ArrayList<String> formattingElements = agency.getArguments();

			int index = 0;
			for (String string : formattingElements) {
				formatedTemplate = formatedTemplate.replace("{" + index + "}", string);
				index++;
			}

			 System.out.println(formatedTemplate);
			try {
				if (!sendEmail(emisor, password, formatedTemplate, subject, pathToAttachment, agency.getEmail(),
						nameFrom)) {
					return false;
				}
				
			} catch (Exception e) {
				throw e;
			}

		}
		return true;
	}


	/*
	 * public static void main(String args[]) throws IOException { MailSender
	 * mailSender = new MailSender();
	 * String password = "";
	 * 
	 * String emisor = "";
	 * 
	 * mailSender.init(password, emisor);
	 * 
	 * mailSender.sendEmailToAgencies(emisor, password, "",
	 * "", "", "");
	 * 
	 * }
	 */

}