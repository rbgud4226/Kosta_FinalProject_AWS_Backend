//package com.example.demo.mariadb.mail;
//
//import com.example.demo.mariadb.users.virtual_users;
//import com.example.demo.mariadb.users.virtual_users_dao;
//import com.example.demo.mariadb.users.virtual_users_dto;
//import com.example.demo.mariadb.users.virtual_users_service;
//import jakarta.mail.*;
//import jakarta.mail.internet.MimeMessage;
//import jakarta.mail.internet.MimeMultipart;
//import jakarta.mail.search.MessageIDTerm;
//import jakarta.mail.search.SearchTerm;
//import org.apache.tomcat.util.http.fileupload.FileItem;
//import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.text.SimpleDateFormat;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//@Service
//public class mail_service {
//  @Autowired
//  private virtual_users_service service;
//
//  @Autowired
//  private mailSenderFactory mailSenderFactory;
//
//  @Async
//  public void sendMail(String loginid, String title, String content, String ref, String receiver){
//    System.out.println("********************************");
//    System.out.println("loginid : " + loginid);
//    System.out.println("title : " + title);
//    virtual_users user = service.getbybox(loginid);
//    String recievemail = null;
//    if (receiver.contains("@")){
//      recievemail = receiver;
//    } else {
//      recievemail = service.getbybox(receiver).getEmail();
//    }
//    virtual_users ref_user = service.getbybox(ref);
//    System.out.println("reciever : " + recievemail);
//    System.out.println("********************************");
//
//    JavaMailSender emailSender = mailSenderFactory.getSender(user.getEmail(), "1234");
//
//    try {
//      MimeMessage message = emailSender.createMimeMessage();
//      MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//      helper.setSubject(title);
//      helper.setText(content);
//      helper.setFrom(user.getEmail());
////      helper.setCc(ref_user.getEmail());
//      // set to 에 문자열 사용 가능
//      helper.setTo(recievemail);
//
//      // 파일 첨부
////      File file = new File("");
////      FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(file.toPath()),
////          false, file.getName(), (int) file.length(), file.getParentFile());
//
//      emailSender.send(message);
//    } catch (MessagingException e) {
//      e.printStackTrace();
//    }
//  }
//
//  public ArrayList<Map<String, Object>> recieveMail(String loginId){
//    virtual_users user = service.getbybox(loginId);
//    Properties properties = new Properties();
//    properties.put("mail.imap.host", "192.168.0.9");
//    properties.put("mail.imap.port", "143");
//    properties.put("mail.imap.ssl.enable", "false");
//    properties.put("mail.store.protocol","imap");
//    ArrayList<Map<String, Object>> maillist = new ArrayList<>();
//    Map<String, Object> spaceMap = new HashMap<>();
//    Session session = Session.getDefaultInstance(properties);
//    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
//    int i = 0;
//
//    try{
//      Store store = session.getStore("imap");
//      store.connect(user.getEmail(), "1234");
//
//      Folder inbox = store.getFolder("INBOX");
//      inbox.open(Folder.READ_WRITE);
//
//      Message[] messages = inbox.getMessages();
//      for(Message message : messages){
//        Map<String, Object> rmail = new HashMap<>();
//        rmail.put("Message-ID",message.getHeader("Message-ID")[0]);
//        rmail.put("Subject", message.getSubject());
//        String from = String.valueOf(message.getFrom()[0]).split("@")[0];
//        rmail.put("From", from);
//        rmail.put("From-mail", message.getFrom()[0]);
//        rmail.put("To", message.getAllRecipients()[0]);
//        rmail.put("Format-Date", formatter.format(message.getSentDate()));
//        rmail.put("Date", message.getSentDate());
//        rmail.put("Content", getEmailContent(message.getContent()));
//        maillist.add(rmail);
//        i++;
//      }
//      // 메일 순서 뒤집기
//      Collections.reverse(maillist);
//
//      // 여백 채우기용도
//      for(int j=0; j<20-i; j++){
//        maillist.add(spaceMap);
//      }
//
//      inbox.close();
//      store.close();
//    } catch (NoSuchProviderException e) {
//      e.printStackTrace();
//    } catch (MessagingException e) {
//      e.printStackTrace();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    return maillist;
//  }
//
//  public Map<String, Object> selectMail(String loginId, String messageID){
//    virtual_users user = service.getbybox(loginId);
//    Properties properties = new Properties();
//    properties.put("mail.imap.host", "192.168.0.9");
//    properties.put("mail.imap.port", "143");
//    properties.put("mail.imap.ssl.enable", "false");
//    properties.put("mail.store.protocol","imap");
//    Session session = Session.getDefaultInstance(properties);
//    Map<String, Object> rmail = new HashMap<>();
//    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
//
//    try{
//      Store store = session.getStore("imap");
//      store.connect(user.getEmail(), "1234");
//
//      Folder inbox = store.getFolder("INBOX");
//      inbox.open(Folder.READ_WRITE);
//
//      SearchTerm searchTerm = new MessageIDTerm(messageID);
//
//      Message[] message = inbox.search(searchTerm);
//      rmail.put("Message-ID",message[0].getHeader("Message-ID")[0]);
//      rmail.put("Subject", message[0].getSubject());
//      String from = String.valueOf(message[0].getFrom()[0]).split("@")[0];
//      rmail.put("From", from);
//      rmail.put("From-mail", message[0].getFrom()[0]);
//      rmail.put("To", message[0].getAllRecipients()[0]);
//      rmail.put("Format-Date", formatter.format(message[0].getSentDate()));
//      rmail.put("Date", message[0].getSentDate());
//      rmail.put("Content", getEmailContent(message[0].getContent()));
//
//      inbox.close();
//      store.close();
//    } catch (NoSuchProviderException e) {
//      e.printStackTrace();
//    } catch (MessagingException e) {
//      e.printStackTrace();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    return rmail;
//  }
//
//  public void delMail(String loginId, String messageID){
//    virtual_users user = service.getbybox(loginId);
//    Properties properties = new Properties();
//    properties.put("mail.imap.host", "192.168.0.9");
//    properties.put("mail.imap.port", "143");
//    properties.put("mail.imap.ssl.enable", "false");
//    properties.put("mail.store.protocol","imap");
//    Session session = Session.getDefaultInstance(properties);
//    Map<String, Object> rmail = new HashMap<>();
//
//    try{
//      Store store = session.getStore("imap");
//      store.connect(user.getEmail(), "1234");
//
//      Folder inbox = store.getFolder("INBOX");
//      inbox.open(Folder.READ_WRITE);
//
//      SearchTerm searchTerm = new MessageIDTerm(messageID);
//
//      Message[] messages = inbox.search(searchTerm);
//
//      for(Message message : messages) {
//        System.out.println("Deleting message with subject : " + message);
//        message.setFlag(Flags.Flag.DELETED, true);
//      }
//
//      inbox.close();
//      store.close();
//    } catch (NoSuchProviderException e) {
//      e.printStackTrace();
//    } catch (MessagingException e) {
//      e.printStackTrace();
//    }
//  }
//
//  private String getEmailContent(Object content) throws MessagingException, IOException {
//    String result = "";
//    if (content instanceof MimeMultipart) {
//      result = getTextFromMimeMultipart((MimeMultipart) content);
//    } else if (content instanceof String) {
//      result = (String) content;
//    } else {
//      // 기타 경우 처리
//      result = content.toString();
//    }
//    return result;
//  }
//
//  private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
//    StringBuilder result = new StringBuilder();
//    int count = mimeMultipart.getCount();
//    for (int i = 0; i < count; i++) {
//      BodyPart bodyPart = mimeMultipart.getBodyPart(i);
//      String disposition = bodyPart.getDisposition();
//
//      if (disposition != null && (disposition.equalsIgnoreCase(Part.ATTACHMENT))) {
//        continue; // 첨부 파일은 무시
//      } else if (bodyPart.isMimeType("text/plain")) {
//        result.append(bodyPart.getContent());
//      } else if (bodyPart.isMimeType("text/html")) {
//        String html = (String) bodyPart.getContent();
//        result.append(org.jsoup.Jsoup.parse(html).text());
//      } else if (bodyPart.getContent() instanceof MimeMultipart) {
//        result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
//      }
//    }
//    return result.toString();
//  }
//}
