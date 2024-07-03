package com.example.demo.oracledb.chat.Manager;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.oracledb.chat.Room.ChatRoom;
import com.example.demo.oracledb.chat.Room.ChatRoomService;
import com.example.demo.oracledb.chat.RoomUser.RoomUserService;

import jakarta.transaction.Transactional;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfiguration {

    @Autowired
    @Lazy
    private JobRepository jobRepository;

    @Autowired
    @Lazy
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private RoomUserService roomUserService;

    @Autowired
    @Qualifier("oracleDataSource")
    private DataSource dataSource;

    @Bean
    @Override
    public JobRepository jobRepository() {
        return super.jobRepository();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Bean
    public Job chatManageJob(Step chatStep, PlatformTransactionManager transactionManager) {
        return new JobBuilder("chatJob", jobRepository).start(chatStep(jobRepository, transactionManager)).build();
    }

    @Bean
    public Step chatStep(JobRepository jobRepository2, PlatformTransactionManager transactionManager) {
        return new StepBuilder("chatStep", jobRepository).tasklet((contribution, chunkContext) -> {
            deleteOldMessages();
            deleNotUseRoom();
            return RepeatStatus.FINISHED;
        }, transactionManager).build();
    }

    @Transactional
    private void deleteOldMessages() {
        String sql = "DELETE FROM MESSAGE WHERE TO_TIMESTAMP(SENDDATE, 'YYYY\"년 \"MM\"월 \"DD\"일 \"HH24:MI:SS') < SYSDATE - INTERVAL '3' MONTH";
        jdbcTemplate.execute(sql);
    }
    
    @Transactional
    private void deleNotUseRoom() {
        List<ChatRoom> list = chatRoomService.getChatRoomByStatusF();
        for (ChatRoom c : list) {
            try {
                roomUserService.deleteRoomUsersByChatroomid(c.getChatroomid());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        chatRoomService.delChatRoomBychatroomid();
    }
}
