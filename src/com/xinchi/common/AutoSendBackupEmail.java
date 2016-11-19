package com.xinchi.common;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import org.springframework.stereotype.Service;

import com.xinchi.tools.PropertiesUtil;
import com.xinchi.tools.main.MailSender;
import com.xinchi.tools.main.MailSenderInfo;

@Service
public class AutoSendBackupEmail {

	public void sendBackup(String[] param) throws MessagingException {
		// 这个类主要是设置邮件
		String serverHost = PropertiesUtil.getProperty("mailServerHost");
		String serverPort = PropertiesUtil.getProperty("mailServerPort");
		String sendUser = PropertiesUtil.getProperty("mailSendUser");
		String sendPassword = PropertiesUtil.getProperty("mailSendPwd");
	
		String subject = "sql backup";
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(serverHost);
		mailInfo.setMailServerPort(serverPort);
		mailInfo.setValidate(true);
		mailInfo.setUserName(sendUser);
		String pwd = new String(sendPassword);
		mailInfo.setPassword(pwd);// 您的邮箱密码
		mailInfo.setFromAddress(sendUser);
		mailInfo.setToAddress("290760943@qq.com");

		mailInfo.setSubject(subject);
		// 设置附件

		MimeBodyPart attachmentPart = new MimeBodyPart();
		String fileName = DateUtil.getDateStr("yyyyMMdd") + ".sql";
		File file = new File(PropertiesUtil.getProperty("backupFolder")
				+ fileName);
		FileDataSource source = new FileDataSource(file);
		attachmentPart.setDataHandler(new DataHandler(source));
		attachmentPart.setFileName(file.getName());

		mailInfo.setContent("");
		mailInfo.setAttachmentPart(attachmentPart);
		// mailInfo.setContent(html);
		// 这个类主要来发送邮件
		MailSender sms = new MailSender();
		// sms.sendTextMail(mailInfo);//发送文体格式
		sms.sendMail(mailInfo);// 发送html格式
	}
}
