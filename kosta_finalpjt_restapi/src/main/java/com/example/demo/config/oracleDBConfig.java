package com.example.demo.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.example.demo.oracledb",
    entityManagerFactoryRef = "oracleEntityManagerFactory",
    transactionManagerRef = "oracleTransactionManager"
)
@EnableScheduling
public class oracleDBConfig {

  @Primary
  @Bean
  public PlatformTransactionManager oracleTransactionManager(){
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(oracleEntityManagerFactory().getObject());

    return transactionManager;
  }

  @Primary
  @Bean
  public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(){
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

    em.setDataSource(oracleDataSource());
    em.setPackagesToScan("com.example.demo.oracledb");
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setShowSql(true);
    vendorAdapter.setGenerateDdl(true);
    em.setJpaVendorAdapter(vendorAdapter);

    HashMap<String, Object> properties = new HashMap<>();
    properties.put("hibernate.dialect","org.hibernate.dialect.Oracle12cDialect");
    properties.put("hibernate.hbm2ddl.auto","update");
    properties.put("spring.batch.jdbc.initialize-schema", "always");
    em.setJpaPropertyMap(properties);

    return em;
  }

  @Primary
  @Bean(name = "oracleDataSource")
  public DataSource oracleDataSource(){
    return DataSourceBuilder.create()
        .driverClassName("oracle.jdbc.driver.OracleDriver")
        .url("jdbc:oracle:thin:@192.168.0.36:1521/xe")
//        .url("jdbc:oracle:thin:@localhost:1521/xe")
        .username("hr")
        .password("hr")
        .build();
  }
  
  //spring.batch.jdbc.initialize-schema=always 용도
  @Bean
  public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
    DataSourceInitializer initializer = new DataSourceInitializer();
    initializer.setDataSource(dataSource);
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(new ClassPathResource("org/springframework/batch/core/schema-oracle.sql"));
    initializer.setDatabasePopulator(populator);
    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement()) {
        statement.execute("SELECT 1 FROM BATCH_JOB_INSTANCE WHERE 1=0");
        initializer.setEnabled(false);
    } catch (SQLException e) {
        initializer.setEnabled(true);
    }

    return initializer;
  }
  
  @Configuration
  public static class SchedulerConfig {

      @Autowired
      private JobLauncher jobLauncher;

      @Autowired
      private Job chatManageJob;

      @Scheduled(fixedRate = 6000000)
      public void perform() throws Exception {
          JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
          jobParametersBuilder.addLong("time", System.currentTimeMillis());
          jobLauncher.run(chatManageJob, jobParametersBuilder.toJobParameters());
      }
  }
}
