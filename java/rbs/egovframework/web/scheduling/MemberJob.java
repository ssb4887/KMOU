package rbs.egovframework.web.scheduling;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.woowonsoft.egovframework.prop.RbsProperties;

import rbs.egovframework.util.MailUtil;
import rbs.modules.member.service.MemberService;


public class MemberJob extends QuartzJobBean{

	//private ApplicationContext context;
	
	@Override
	protected void executeInternal(JobExecutionContext jobContext) throws JobExecutionException {
		if(RbsProperties.getPropertyInt("Globals.personal.info.disuse") == 1){
			ApplicationContext context = (ApplicationContext)jobContext.getJobDetail().getJobDataMap().get("applicationContext");
			MemberService memberService = (MemberService)context.getBean("memberService");
			List<Object> list = null;
			List<Object> reAgree30List = memberService.getReAgreeList("30");
			if(!reAgree30List.isEmpty()){
				if(list == null) list = new ArrayList<Object>();
				list.addAll(reAgree30List);
			}
			List<Object> reAgree15List = memberService.getReAgreeList("15");
			if(!reAgree15List.isEmpty()){
				if(list == null) list = new ArrayList<Object>();
				list.addAll(reAgree15List);
			}
			
			//System.out.println("list : "+list);
			
			if(list != null){
				String[][] toArray = MailUtil.setToArray("MEMBER_EMAIL", "MEMBER_NAME", "Globals.mail.reagree.title", null, "Globals.mail.reagree.content", new String[]{"AUTO_REGOUT_DATE", "MEMBER_NAME"}, list);
				MailUtil.sendMail(RbsProperties.getProperty("Globals.mail.fromMail"), RbsProperties.getProperty("Globals.mail.fromName"), toArray);
			}
			
			try{
				memberService.reAgreeCdelete();
			}
			catch(Exception e){}
		}
	}
}
