//package com.example.demo.config;
//
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
//import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//    basePackages = "com.example.demo.mariadb",
//    entityManagerFactoryRef = "mariadbEntityManagerFactory",
//    transactionManagerRef = "mariadbTransactionManager"
//)
//public class mariaDBConfig {
//
//  @Bean
//  public PlatformTransactionManager mariadbTransactionManager(){
//    JpaTransactionManager transactionManager = new JpaTransactionManager();
//    transactionManager.setEntityManagerFactory(mariadbEntityManagerFactory().getObject());
//
//    return transactionManager;
//  }
//
//  @Bean
//  public LocalContainerEntityManagerFactoryBean mariadbEntityManagerFactory(){
//    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//
//    em.setDataSource(mariaDataSource());
//    em.setPackagesToScan("com.example.demo.mariadb");
//    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//
//    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//    vendorAdapter.setShowSql(true);
//    vendorAdapter.setGenerateDdl(true);
//    em.setJpaVendorAdapter(vendorAdapter);
//
//    HashMap<String, Object> properties = new HashMap<>();
//    properties.put("hibernate.dialect","org.hibernate.dialect.MariaDB103Dialect");
//    properties.put("hibernate.hbm2ddl.auto","update");
//
//    em.setJpaPropertyMap(properties);
//
//    return em;
//  }
//
//  @Bean
//  public DataSource mariaDataSource(){
//    return DataSourceBuilder.create()
//        .driverClassName("org.mariadb.jdbc.Driver")
//        .url("jdbc:mariadb://192.168.0.9:3306/mailbox")
//        .username("ys")
//        .password("1234")
//        .build();
//  }
//}
