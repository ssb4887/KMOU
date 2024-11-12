package rbs.egovframework.web.scheduling;

import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;


public class SchedulerFactoryBeanWidthShutdownDelay extends SchedulerFactoryBean{

	@Override
	public void destroy() throws SchedulerException {
		super.destroy();
	    try {
	      Thread.sleep( 1000 );
	    } catch( InterruptedException e ) {
	      throw new RuntimeException( e );
	    }
	}
}
