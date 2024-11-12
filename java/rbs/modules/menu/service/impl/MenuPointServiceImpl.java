package rbs.modules.menu.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.modules.menu.mapper.MenuPointMapper;
import rbs.modules.menu.service.MenuPointService;

@Service("menuPointService")
public class MenuPointServiceImpl extends EgovAbstractServiceImpl implements MenuPointService {

	@Resource(name="menuPointMapper")
	private MenuPointMapper menuPointDao;
	
	@Override
	public DataMap getTotalView(Map<String, Object> param){
		return menuPointDao.getTotalView(param);
	}
	@Override
	public Map<Object, Object> getMenuPointMap(Map<String, Object> param){
		return menuPointDao.getMenuPointMap(param);
	}
	
	@Override
	public int getPointPTotalCount(Map<String, Object> param){
        return menuPointDao.getPointPTotalCount(param);
    }
	@Override
	public List<Object> getPointPList(String localeLang, Map<String, Object> param){
		return menuPointDao.getPointPList(localeLang, param);
	}
	
	@Override
	public int getPointMTotalCount(Map<String, Object> param){
        return menuPointDao.getPointMTotalCount(param);
    }
	@Override
	public List<Object> getPointMList(Map<String, Object> param){
		return menuPointDao.getPointMList(param);
	}
	
	@Override
	public int insert(String siteId, int verIdx, int menuIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception{
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 저장
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
    	
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		
		List<DTForm> defaultDataList = new ArrayList<DTForm>();
		defaultDataList.add(new DTForm("SITE_ID", siteId));
		defaultDataList.add(new DTForm("VER_IDX", verIdx));
		defaultDataList.add(new DTForm("MENU_IDX", menuIdx));
		
		// 등록자 중복확인
		Map<String, Object> sparam1 = new HashMap<String, Object>();
		List<DTForm> searchList1 = new ArrayList<DTForm>();
		searchList1.add(new DTForm("A.SITE_ID", siteId));
		searchList1.add(new DTForm("A.VER_IDX", verIdx));
		searchList1.add(new DTForm("A.MENU_IDX", menuIdx));
		searchList1.add(new DTForm("A.REGI_IDX", loginMemberIdx));
		sparam1.put("searchList", searchList1);
		int duplicate = menuPointDao.getPointMTotalCount(sparam1);
		if(duplicate > 0) {
			return -2;
		}

		//고유아이디 셋팅
		Map<String, Object> nextIdParam = new HashMap<String, Object>();
		List<DTForm> nextIdSearchList = new ArrayList<DTForm>();
		nextIdSearchList.addAll(defaultDataList);
		nextIdParam.put("searchList", nextIdSearchList);
		int pIdx = menuPointDao.getNextId(nextIdParam);
		

		// insert 항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

		dataList.addAll(defaultDataList);
    	dataList.add(new DTForm("PIDX", pIdx));
		    	
    	dataList.add(new DTForm("REGI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("REGI_ID", loginMemberId));
    	dataList.add(new DTForm("REGI_NAME", loginMemberName));
    	dataList.add(new DTForm("REGI_IP", regiIp));
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));
    	
    	param.put("dataList", dataList);
    	
		// insert
		return menuPointDao.insert(param);
	}
}