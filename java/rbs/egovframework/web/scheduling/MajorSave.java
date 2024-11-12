package rbs.egovframework.web.scheduling;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import rbs.modules.majorInfo.service.MajorInfoService;
import rbs.modules.major.serviceOra.MajorServiceOra;


public class MajorSave extends QuartzJobBean{

	//private ApplicationContext context;
	
	@Override
	protected void executeInternal(JobExecutionContext jobContext) throws JobExecutionException {
			ApplicationContext context = (ApplicationContext)jobContext.getJobDetail().getJobDataMap().get("applicationContext");
			MajorInfoService majorInfoService = (MajorInfoService)context.getBean("majorInfoService");
			MajorServiceOra majorServiceOra = (MajorServiceOra)context.getBean("majorServiceOra");
			List<Map<String, Object>> list = null;
			List<Map<String, Object>> newMajorList = null;
			try {
				newMajorList = majorServiceOra.getNewMajorList();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(!newMajorList.isEmpty()){
				
				for (Map<String, Object> map : newMajorList) {
	
					try {
						int result = majorInfoService.insertDept(map);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					
					
				}
						
				
			}
			
			//System.out.println("list : "+list);
			
			/*if(list != null){
				
			}*/
			
			try{
				//majorService.getInitMajorList(null);
			}
			catch(Exception e){}
		
	}
}
