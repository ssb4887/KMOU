package rbs.egovframework.service;


import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.woowonsoft.egovframework.service.WebsiteDetailsService;

@Service("websiteDetailsService")
public class WebsiteDetailsSessionServiceImpl extends EgovAbstractServiceImpl implements WebsiteDetailsService {

	@Override
	public Object getWebsiteInfo() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return null;
		}

		return RequestContextHolder.getRequestAttributes().getAttribute("usrSiteVO", RequestAttributes.SCOPE_SESSION);

	}
}
