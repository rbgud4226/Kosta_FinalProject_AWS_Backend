package com.example.demo.oracledb.chat.Manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.oracledb.chat.Room.ChatRoom;
import com.example.demo.oracledb.chat.Room.ChatRoomService;
import com.example.demo.oracledb.chat.RoomUser.RoomUserService;

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
	@Lazy
	private ChatRoomService chatRoomService;

	@Autowired
	@Lazy
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
	public Job chatManageJob(Step chatStep) {
		return new JobBuilder("chatJob", jobRepository).start(chatStep).build();
	}

	@Bean
	public Step chatStep() {
		return new StepBuilder("chatStep", jobRepository).tasklet((contribution, chunkContext) -> {
			deleteOldMessages();
			deleteNotUseRoom();
			return RepeatStatus.FINISHED;
		}, transactionManager()).build();
	}

	private void deleteOldMessages() {
		String sql = "DELETE FROM MESSAGE WHERE sendDate < SYSDATE - INTERVAL '3' MONTH";
		jdbcTemplate.execute(sql);
	}

	private void deleteNotUseRoom() {
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

	@Bean
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
		DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("org/springframework/batch/core/schema-oracle.sql"));
		initializer.setDatabasePopulator(populator);

		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
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
