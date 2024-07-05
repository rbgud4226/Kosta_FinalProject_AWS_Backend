//package com.example.demo.mariadb.mail;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.stereotype.Component;
//
//import java.util.Properties;
//
//@Component
//public class mailSenderFactoryImpl implements mailSenderFactory{
//  @Value("192.168.0.9")
//  private String host;
//  @Value("587")
//  private String port;
//  @Value("true")
//  private String auth;
//  @Value("true")
//  private String debug;
//  @Value("true")
//  private String enable;
//  @Value("UTF-8")
//  private String charset;
//  @Value("smtp")
//  private String protocol;
//
//  @Override
//  public JavaMailSender getSender(final String email, final String password) {
//    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//    mailSender.setHost(host);
//    mailSender.setUsername(email);
//    mailSender.setPassword(password);
//    mailSender.setPort(Integer.parseInt(port));
//
//    Properties properties = mailSender.getJavaMailProperties();
//    // smtp setting
//    properties.put("mail.smtp.ssl.trust", "*");
//    properties.put("mail.smtp.auth", auth);
//    properties.put("mail.debug", debug);
//    properties.put("mail.mime.charset", charset);
//    properties.put("mail.transport.protocol", protocol);
//
//    // Disable SSL/TLS
//    properties.put("mail.smtp.starttls.enable", enable);
//    properties.put("mail.smtp.ssl.enable", false);
//    properties.put("server.ssl.trust-store-type","none");
//
//    return mailSender;
//  }
//}
