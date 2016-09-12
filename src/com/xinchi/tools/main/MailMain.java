package com.xinchi.tools.main;



import org.apache.commons.codec.binary.Base64;

import com.xinchi.tools.PropertiesUtil;



public class MailMain {
	public static void main(String[] args){
	         //这个类主要是设置邮件
		  MailSenderInfo mailInfo = new MailSenderInfo(); 
		  mailInfo.setMailServerHost(PropertiesUtil.getProperty("mailServerHost")); 
		  mailInfo.setMailServerPort(PropertiesUtil.getProperty("mailServerPort")); 
		  mailInfo.setValidate(true); 
		  mailInfo.setUserName(PropertiesUtil.getProperty("mailSendUser")); 
		  String pwd=new String(Base64.decodeBase64(PropertiesUtil.getProperty("mailSendPwd")));
		  mailInfo.setPassword(pwd);//您的邮箱密码 
		  mailInfo.setFromAddress(PropertiesUtil.getProperty("mailSendUser")); 
		  mailInfo.setToAddress("273707977@qq.com"); 
		  String subject=PropertiesUtil.getProperty("mailSubject");
		  mailInfo.setSubject(subject); 
		  String html="<div id=\"mailContentContainer\" > "+
				  	" <p>亲爱的loveljy119 您好:<br><br>恭喜您在道客巴巴上注册成功.<br><br>" +
				  	"您的会员帐号是:loveljy119, 请您点击下面的邮件认证链接.<br><br>您邮件认证的链接是:" +
				  	"<a href=\"http://www.doc88.com/member.php?act=authemail&amp;p=bG92ZWxqeTExOS4z" +
				  	"OTUwNWQxZmRhYWI4OWNkZDUzNmIwNzJkY2E1OWM4MQ%3D%3D\" target=\"_blank\">" +
				  	"http://www.d<wbr>oc88.com/mem<wbr>ber.php?act=<wbr>authemail&amp;am<wbr>" +
				  	"p;p=bG92ZWxq<wbr>eTExOS4zOTUw<wbr>NWQxZmRhYWI4<wbr>OWNkZDUzNmIw" +
				  	"<wbr>NzJkY2E1OWM4<wbr>MQ%3D%3D</a>,<br><br>请<a href=\"http://www.doc88.com/member.php?" +
				  	"act=authemail&amp;p=bG92ZWxqeTExOS4zOTUwNWQxZmRhYWI4OWNkZDUzNmIwNzJkY2E1OWM4MQ%3D%3D\" " +
				  	"target=\"_blank\">点击此链接</a>或在浏览器中输入此链接.<br><br>道客巴巴 客服中心.</p> </div>";
		  mailInfo.setContent(html); 
	         //这个类主要来发送邮件
		  MailSender sms = new MailSender();
//	          sms.sendTextMail(mailInfo);//发送文体格式 
	          sms.sendHtmlMail(mailInfo);//发送html格式
		}
	
}

