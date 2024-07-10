//package com.example.demo.mariadb.mail;
//
//import jakarta.persistence.criteria.From;
//import jakarta.servlet.http.HttpSession;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/mail")
//public class mail_controller {
//  @Autowired
//  private mail_service service;
//
//  @GetMapping("/post") // 메일 발송 페이지
//  public void mailform(ModelMap map){
//    map.addAttribute("title","");
//    map.addAttribute("From","");
//    map.addAttribute("Content","");
//  }
//
//  @PostMapping("/sendmail") // 메일 발송
//  public String sendmail(HttpSession session, @RequestParam String title, @RequestParam String content,
//                       @RequestParam String ref, @RequestParam String receiver){
//    String loginId = (String) session.getAttribute("loginId");
//    // 비동기 메일발신 처리
//    service.sendMail(loginId, title, content, ref, receiver);
//    return "redirect:/mail/list";
//  }
//
//  @GetMapping("/list") // 받은메일 리스트
//  public void maillist(HttpSession session, ModelMap map){
//    String loginid = (String) session.getAttribute("loginId");
//    ArrayList<Map<String, Object>> list = service.recieveMail(loginid);
//    map.addAttribute("list", list);
//  }
//
//  @GetMapping("/page") // 메일 상세페이지
//  public void maildetail(HttpSession session, @RequestParam String messageId, ModelMap map){
//    System.out.println("message id : " + messageId + "*******************************");
//    String loginId = (String) session.getAttribute("loginId");
//    map.addAttribute("mail", service.selectMail(loginId, messageId));
//  }
//
//  @GetMapping("/del") //메일 삭제
//  public String delmail(HttpSession session, @RequestParam String messageId){
//    String loginId = (String) session.getAttribute("loginId");
//    service.delMail(loginId, messageId);
//    return "redirect:/mail/list";
//  }
//
//  @GetMapping("/reply") // 메일 회신
//  public String replymail(@RequestParam String Subject, @RequestParam String From, ModelMap map){
//    String title = "Re:"+Subject;
//    map.addAttribute("title", title);
//    map.addAttribute("From", From);
//    return "/mail/post";
//  }
//
//  @GetMapping("/tp") // 메일 전달
//  public String transportmail(@RequestParam String Content, @RequestParam String From, ModelMap map){
//    map.addAttribute("Content", Content);
//    return "/mail/post";
//  }
//
//}
