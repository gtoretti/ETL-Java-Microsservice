package main.java;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DRCOfflineJob implements Job {

	public void execute(JobExecutionContext context)
		      throws JobExecutionException{
		
    	Logger logger = LogManager.getRootLogger();
    	{
    		try {
    			logger.info("starting GetWatsonData");
    			GetPredictData o = new GetPredictData();
    			o.main();
    			logger.info("finished GetWatsonData");
    		}catch (Exception e) {
    			e.printStackTrace();
    			logger.error(e);
    		}
    	}
    }
}
