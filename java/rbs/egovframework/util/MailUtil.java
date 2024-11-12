package rbs.egovframework.util;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.StringUtil;

/**
 * 메일전송 util
 * @author 이동근
 *
 */
public class MailUtil {

	private static String mailHost = StringUtil.getString(RbsProperties.getProperty("Globals.mail.smtp.host"), "localhost");
	private static String mailPort = StringUtil.getString(RbsProperties.getProperty("Globals.mail.smtp.port"), "25");
	private static String protocols = StringUtil.getString(RbsProperties.getProperty("Globals.mail.smtp.ssl.protocols"), "TLSv1.2");
	private static String mailUser = StringUtil.getString(RbsProperties.getProperty("Globals.mail.smtp.user"), "");
	private static String mailPassword = StringUtil.getString(RbsProperties.getProperty("Globals.mail.smtp.password"), "");
	private static String charset = StringUtil.getString(RbsProperties.getProperty("Globals.mail.smtp.charset"), "");
	private static String startTls = StringUtil.getString(RbsProperties.getProperty("Globals.mail.smtp.starttls"), "");
	private static String ssl = StringUtil.getString(RbsProperties.getProperty("Globals.mail.smtp.ssl"), "");

	/**
	 * 메일 전송
	 * @param fromMail
	 * @param fromName
	 * @param toMail
	 * @param toName
	 * @param title
	 * @param content
	 * @return
	 */
	public static boolean sendMail(String fromMail, String fromName, String toMail, String toName, String title, String content){

		Properties props = new Properties();
		props.put("mail.smtp.host", mailHost);
		props.put("mail.smtp.port", mailPort);
		props.put("mail.smtp.ssl.protocols", protocols);

		Authenticator authenticator = null;
		if(!StringUtil.isEmpty(mailUser) && !StringUtil.isEmpty(mailPassword))
		{
			props.put("mail.smtp.auth", true);
			//authenticator = new SMTPAuthenticator(mailUser, mailPassword);
			authenticator = new javax.mail.Authenticator() {
		        protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
		            return new javax.mail.PasswordAuthentication(mailUser, mailPassword);
		        }
		    };
		}
		
		if(StringUtil.isEquals(startTls, "1")) props.put("mail.smtp.starttls.enable", true);
		if(StringUtil.isEquals(ssl, "1")){
			props.put("mail.smtp.ssl.enable", true);
			props.put("mail.smtp.ssl.trust", mailHost);
		}
		
		try{
			Session session = Session.getDefaultInstance(props, authenticator);
			
			/*
			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
		        protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
		            return new javax.mail.PasswordAuthentication(mailUser, mailPassword);
		        }
		    });
		    */
		    //session.setDebug(true);
			MimeMessage mailMessage = new MimeMessage(session);
			
			/*mailMessage.setFrom(new InternetAddress(fromMail)); // 보내는 EMAIL (정확히 적어야 SMTP 서버에서 인증 실패되지 않음)
*/			mailMessage.setFrom(new InternetAddress(fromMail, fromName, charset));
			/*mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toMail)); //수신자 셋팅
*/			mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail, toName, charset));
			
			// Message Setting
			mailMessage.setSubject(MimeUtility.encodeText(title,"UTF-8", "B"));
		    
			mailMessage.setContent(content, "text/html; charset=UTF-8");
		    //utf-8
			mailMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
	    	
		    
			//mailMessage.setFrom(new InternetAddress(fromMail, fromName, charset));
			//mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail, toName, charset));
			//mailMessage.setSubject(title, charset);
			//mailMessage.setContent(content, "text/html;charset=" + charset);
			
			//mailMessage.setContent(content, "text/plain; charset=UTF-8");
			
			Transport.send(mailMessage);
		
		}catch(UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 복수의 사람에게 메일 발송
	 * @param fromMail
	 * @param fromName
	 * @param recipients
	 * @return
	 */
	public static boolean sendMail(String fromMail, String fromName, Map<String, String> recipients, String title, String content){

		Properties props = new Properties();
		props.put("mail.smtp.host", mailHost);
		props.put("mail.smtp.port", mailPort);
		props.put("mail.smtp.ssl.protocols", protocols);

		Authenticator authenticator = null;
		if(!StringUtil.isEmpty(mailUser) && !StringUtil.isEmpty(mailPassword))
		{
			props.put("mail.smtp.auth", true);
			//authenticator = new SMTPAuthenticator(mailUser, mailPassword);
			authenticator = new javax.mail.Authenticator() {
		        protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
		            return new javax.mail.PasswordAuthentication(mailUser, mailPassword);
		        }
		    };
		}
		
		if(StringUtil.isEquals(startTls, "1")) props.put("mail.smtp.starttls.enable", true);
		if(StringUtil.isEquals(ssl, "1")){
			props.put("mail.smtp.ssl.enable", true);
			props.put("mail.smtp.ssl.trust", mailHost);
		}
		
		try{
			// 받는사람들 설정
			Set<String> recipientKeys = recipients.keySet();
			
			for (String toMail : recipientKeys) {
				Session session = Session.getDefaultInstance(props, authenticator);			
				MimeMessage mailMessage = new MimeMessage(session);
				
				// 보내는사람 설정
				mailMessage.setFrom(new InternetAddress(fromMail, fromName, charset));			
				
				String toName = recipients.get(toMail);
				mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail, toName, charset));
				
				// Message Setting
				mailMessage.setSubject(MimeUtility.encodeText(title,"UTF-8", "B"));		    
				mailMessage.setContent(content, "text/html; charset="+charset);
				
			    //utf-8
				mailMessage.setHeader("Content-Type", "text/html; charset="+charset);
				Transport.send(mailMessage);
			}			
		
		}catch(UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 여러 사람에게 메일 전송
	 * @param fromMail
	 * @param fromName
	 * @param toArray
	 * @return
	 */
	public static boolean sendMail(String fromMail, String fromName, String[][] toArray){
		
		Properties props = new Properties();
		props.put("mail.smtp.host", mailHost);
		props.put("mail.smtp.port", mailPort);
		props.put("mail.smtp.ssl.protocols", protocols);
		
		Authenticator authenticator = null;
		if(!StringUtil.isEmpty(mailUser) && !StringUtil.isEmpty(mailPassword))
		{
			props.put("mail.smtp.auth", "true");
			//authenticator = new SMTPAuthenticator(mailUser, mailPassword);
			authenticator = new javax.mail.Authenticator() {
		        protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
		            return new javax.mail.PasswordAuthentication(mailUser, mailPassword);
		        }
		    };
		}
		
		if(StringUtil.isEquals(startTls, "1")) props.put("mail.smtp.starttls.enable", true);
		if(StringUtil.isEquals(ssl, "1")){
			props.put("mail.smtp.ssl.enable", true);
			props.put("mail.smtp.ssl.trust", mailHost);
		}

		//Session mailSession = null;
		MimeMessage mailMessage = null;
		try{
			//mailSession = Session.getInstance(props, authenticator);
			Session mailSession = Session.getDefaultInstance(props, authenticator);

			if(toArray != null)
			{
				
				for (int i = 0; i < toArray.length; i++){
					mailMessage = new MimeMessage(mailSession);
					mailMessage.setFrom(new InternetAddress(fromMail, fromName, charset));
					mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toArray[i][0], toArray[i][1], charset));
					mailMessage.setSubject(MimeUtility.encodeText(toArray[i][2], charset, "B"));
					mailMessage.setContent(toArray[i][3], "text/html;charset=" + charset);
					mailMessage.setHeader("Content-Type", "text/html; charset=" + charset);
					Transport.send(mailMessage);
				}
			}
			
		}catch(UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static String[][] setToArray(String toMailKey, String toNameKey, String titleKey, String[] titleKeys, String contentKey, String[] contentKeys, List toInfoList){
		if(toInfoList == null) return null;
		
		int toInfoListLen = toInfoList.size();
		if(toInfoListLen < 1) return null;
		
		String[][] result = new String[toInfoListLen][4];
		for (int i = 0; i < toInfoListLen; i++) {
			DataMap dt = (DataMap)toInfoList.get(i);
			
			result[i][0] = StringUtil.getString(dt.get(toMailKey));
			result[i][1] = StringUtil.getString(dt.get(toNameKey));
			int titleKeysLen = 0;
			if(titleKeys != null) titleKeysLen = titleKeys.length;
			if(titleKeysLen > 0)
			{
				String[] tempTitleKeys = new String[titleKeysLen];
				for (int j = 0; j < titleKeysLen; j++) {
					tempTitleKeys[j] = StringUtil.getString(dt.get(titleKeys[j]));
				}
				result[i][2] = MessageFormat.format(RbsProperties.getProperty(titleKey), tempTitleKeys);
			}
			else result[i][2] = RbsProperties.getProperty(titleKey);

			int contentKeysLen = 0;
			if(contentKeys != null) contentKeysLen = contentKeys.length;
			if(contentKeysLen > 0)
			{
				String[] tempContentKeys = new String[contentKeysLen];
				for (int j = 0; j < contentKeysLen; j++) {
					tempContentKeys[j] = StringUtil.getString(dt.get(contentKeys[j]));
				}
				result[i][3] = MessageFormat.format(RbsProperties.getProperty(contentKey), tempContentKeys);
			}
			else result[i][3] = RbsProperties.getProperty(contentKey);
		}
		return result;
	}
}
/*
class SMTPAuthenticator extends Authenticator{
	PasswordAuthentication passwordAuthentication = null;
	
	SMTPAuthenticator(String mailUser, String mailPassword){
		passwordAuthentication = new PasswordAuthentication(mailUser, mailPassword);
	}
	
	public PasswordAuthentication getPasswordAuthentication(){
		return passwordAuthentication;
	}
}*/