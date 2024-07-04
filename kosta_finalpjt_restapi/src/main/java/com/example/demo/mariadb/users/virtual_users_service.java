//package com.example.demo.mariadb.users;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.util.Base64;
//import java.util.Random;
//
//@Service
//public class virtual_users_service {
//  @Autowired
//  private virtual_users_dao dao;
//
//  public void save(String id) {
//    String mail = id+"@mail.yserver.iptime.org";
//    dao.insertVirtualUser(1, "1234", mail, id);
//  }
//
//  public virtual_users getbybox (String box){
//    return dao.findByBox(box);
//  }
//
//}
