package rbs.modules.search.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;


import rbs.egovframework.LoginVO;
import rbs.modules.sample.service.SampleService;

import rbs.modules.search.mapper.SearchMapper;

import rbs.modules.search.service.SearchService;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.ClientUtil;
import com.woowonsoft.egovframework.util.FileUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * 통합검색 모듈에 관한 구현 클래스를 정의한다.
 * @author 석수빈, 이동근
 *
 */
@Service("searchService")
public class SearchServiceImpl extends EgovAbstractServiceImpl implements SearchService {
	@Resource(name="searchMapper")
	private SearchMapper searchDAO;


	@Override
	public int insertPoint(JSONObject reqJsonObj, HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		param.put("POINT_TYPE", reqJsonObj.getString("POINT_TYPE"));
		param.put("POINT", reqJsonObj.getString("POINT"));
		param.put("CONTENTS", reqJsonObj.getString("CONTENTS"));
		
		param.put("COLL_CD", loginVO.getCollCd());
		param.put("COLL_NM", loginVO.getCollNm());
		param.put("DEPT_CD", loginVO.getDeptCd());
		param.put("DEPT_NM", loginVO.getDeptNm());
		param.put("MAJOR_CD", loginVO.getMajorCd());
		param.put("MAJOR_NM", loginVO.getMajorNm());
		param.put("USERTYPE_IDX", loginVO.getUsertypeIdx());
		param.put("REGI_ID", loginVO.getMemberId());
		param.put("REGI_NAME", loginVO.getMemberName());
		param.put("REGI_IP", request.getRemoteAddr());
		    	
		return searchDAO.insertPoint(param);
	}


	@Override
	public int insertSearchLog(String endpoint, JSONObject reqJsonObj, String response, HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		param.put("SEARCH_TYPE", endpoint.replace("/search/", ""));
		param.put("KEYWORD", reqJsonObj.getString("keyword"));
//		param.put("RESPONSE", response);
		param.put("GRADE", loginVO.getGrade());
		param.put("COLL_CD", loginVO.getCollCd());
		param.put("COLL_NM", loginVO.getCollNm());
		param.put("DEPT_CD", loginVO.getDeptCd());
		param.put("DEPT_NM", loginVO.getDeptNm());
		param.put("MAJOR_CD", loginVO.getMajorCd());
		param.put("MAJOR_NM", loginVO.getMajorNm());
		param.put("GRADE", loginVO.getGrade());
		param.put("USER_TYPE", loginVO.getUsertypeIdx());
		param.put("REGI_ID", loginVO.getMemberId());
		param.put("REGI_NAME", loginVO.getMemberName());
		param.put("REGI_IP", request.getRemoteAddr());
		param.put("SESSION_ID", request.getRequestedSessionId());
		
		return searchDAO.insertSearchLog(param);
		
	}
}