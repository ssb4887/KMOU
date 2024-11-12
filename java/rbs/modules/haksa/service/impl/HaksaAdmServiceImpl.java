package rbs.modules.haksa.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.modules.haksa.mapper.HaksaAdmMapper;
import rbs.modules.haksa.service.HaksaAdmService;

/**
 * 샘플모듈에 관한 구현클래스를 정의한다.
 * @author user
 *
 */
@Service("haksaAdmService")
public class HaksaAdmServiceImpl extends EgovAbstractServiceImpl implements HaksaAdmService {

	@Resource(name="haksaAdmMapper")
	private HaksaAdmMapper haksaAdmDAO;
	
	/**
	 * 교수정보 리스트(count)
	 * @param param
	 * @return
	 */
	@Override
	public int getTotalCount(Map<String, Object> param) throws Exception{
		return haksaAdmDAO.getTotalCount(param);
	}

	/**
	 * 교수정보 리스트
	 * @param param
	 * @return
	 */
	@Override
	public List<?> getList(Map<String, Object> param) throws Exception{
		return haksaAdmDAO.getList(param);
	}

	/**
	 * 교수 상세정보(수정용)
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(int fnIdx, Map<String, Object> param) throws Exception{
		return haksaAdmDAO.getModify(param);
	}


	/**
	 * 교수 상세정보 update
	 * @param param
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int update(String uploadModulePath, int fnIdx, Map<String,Object> param, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1; 
			
		List<DTForm> dataList = new ArrayList<DTForm>();							// 저장항목
    	
		// 1. 검색조건 setting

		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

		// 2.2 등록자 정보
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		String loginMemberId = null;
		if(loginVO != null) {
			loginMemberId = loginVO.getMemberId();
		}
		String fileSavedName = "";
		String fileOriginName = "";
		
		fileSavedName = StringUtil.getString(parameterMap.get("fileSavedName"));
		fileOriginName = StringUtil.getString(parameterMap.get("fileOriginName"));
		
		if(!fileSavedName.isEmpty()) {
			dataList.add(new DTForm("FILE_SAVED_NAME", fileSavedName));					
			dataList.add(new DTForm("FILE_ORIGIN_NAME", fileOriginName));					
		}
		
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));
    	
    	param.put("dataList", dataList);
    	param.put("fnIdx", fnIdx);

    	// 3. DB 저장
    	// 3.1 기본 정보 테이블
		int result = haksaAdmDAO.update(param);
		

		return result;
	}

}