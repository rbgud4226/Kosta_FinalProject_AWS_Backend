//package com.example.demo.mariadb.users;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.ToString;
//
//@Entity
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString
//public class virtual_users {
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private int id;
//
//  @Column(name = "domain_id", nullable = false)
//  private int domainId;
//
//  @Column(name = "password", nullable = false, length = 255)
//  private String password;
//
//  @Column(name = "email", nullable = false, length = 120, unique = true)
//  private String email;
//
//  @Column(name = "box", nullable = false, length = 120)
//  private String box;
//
//}
