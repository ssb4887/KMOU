package rbs.egovframework.web.scheduling;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import rbs.modules.majorInfo.service.MajorInfoService;
import rbs.modules.studPlan.service.StudPlanService;
import rbs.modules.major.serviceOra.MajorServiceOra;


public class StudConfirmInterface extends QuartzJobBean{

    @Override
    protected void executeInternal(JobExecutionContext jobContext) throws JobExecutionException {
        ApplicationContext context = (ApplicationContext) jobContext.getJobDetail().getJobDataMap().get("applicationContext");
        StudPlanService studPlanService = (StudPlanService) context.getBean("studPlanService");
        
        try {
			studPlanService.interfaceStudPlanConfirmation();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
