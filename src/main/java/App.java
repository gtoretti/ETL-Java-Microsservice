package main.java;

import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class App {

	public static void main(String[] args) {

		try {

			Map<String, String> env = System.getenv();
			int intervalHours = 6;
			if (env.get("DRC_OFFLINE_EXEC_INTERVAL_HOURS")!=null) {
				intervalHours = Integer.parseInt(env.get("DRC_OFFLINE_EXEC_INTERVAL_HOURS"));
			}
			
			JobDetail job = JobBuilder.newJob(DRCOfflineJob.class).withIdentity("DRCOfflineJob", "group1").build();

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("DRCOfflineTrigger", "group1")
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(intervalHours).repeatForever())
					.build();

			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);

			Logger logger = LogManager.getRootLogger();
			
			if (env.get("DRC_OFFLINE_LOG_PATH")!=null) {
				logger.setAdditivity(false);
				logger.removeAllAppenders();
				RollingFileAppender file = new RollingFileAppender();
				file.setName("file");
				file.setFile(env.get("DRC_OFFLINE_LOG_PATH"));
				file.setAppend(true);
				file.setMaxFileSize("10KB");
				file.setMaxBackupIndex(10);
				PatternLayout layout = new PatternLayout();
				layout.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
				file.setLayout(layout);
				file.activateOptions();
				logger.addAppender(file);
				logger.setLevel(Level.INFO);
			}
			logger.info("DRCOfflineService started.");

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
