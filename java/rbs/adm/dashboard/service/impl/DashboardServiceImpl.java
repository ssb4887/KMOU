package rbs.adm.dashboard.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DataMap;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.adm.dashboard.mapper.DashboardMapper;
import rbs.adm.dashboard.service.DashboardService;

@Service("admDashboardService")
public class DashboardServiceImpl extends EgovAbstractServiceImpl implements DashboardService {

	@Resource(name="admDashboardMapper")
	private DashboardMapper dashboardDao;
	
	@Override
	public List<Object> selectChart1Data(Map<String, Object> param) {
		return dashboardDao.selectChart1Data(param);
	}

	@Override
	public List<Object> selectChart2Data(Map<String, Object> param) {
		return dashboardDao.selectChart2Data(param);
	}

	@Override
	public List<Object> selectChart3Data(Map<String, Object> param) {
		return dashboardDao.selectChart3Data(param);
	}

	@Override
	public List<Object> selectChart4Data(Map<String, Object> param) {
		return dashboardDao.selectChart4Data(param);
	}

	@Override
	public List<Object> selectChart5Data(Map<String, Object> param) {
		return dashboardDao.selectChart5Data(param);
	}

	@Override
	public List<Object> selectChart6Data(Map<String, Object> param) {
		return dashboardDao.selectChart6Data(param);
	}
}