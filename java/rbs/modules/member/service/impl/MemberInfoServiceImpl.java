package rbs.modules.member.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import rbs.egovframework.LoginVO;
import rbs.modules.member.mapper.MemberAnMapper;
import rbs.modules.member.mapper.MemberInfoMapper;
import rbs.modules.member.mapper.MemberInfoRbsMapper;
import rbs.modules.member.service.MemberAnLogService;
import rbs.modules.member.service.MemberInfoService;
import rbs.modules.menu.service.MenuVerService;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 */
@Service("MemberInfoService")
public class MemberInfoServiceImpl extends EgovAbstractServiceImpl implements MemberInfoService {

	@Resource(name="memberInfoMapper")
	private MemberInfoMapper memberInfoDAO;
	
	@Resource(name="memberInfoRbsMapper")
	private MemberInfoRbsMapper memberInfoRbsDAO;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	@Resource(name = "memberAnLogService")
	private MemberAnLogService memberLogService;

	@Resource(name="menuVerService")
	protected MenuVerService menuVerService;

	@Override
	public DataMap getMyCurriculum(Map<String, Object> param) {
		List<Object> myCurriculum = memberInfoDAO.getMyCurriculum(param);
		if(myCurriculum == null || myCurriculum.size() < 1) return null;
		else return (DataMap) myCurriculum.get(myCurriculum.size()-1);
	}
	
	@Override
	public DataMap getMyInfomation(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return memberInfoDAO.getMyInfomation(param);
	}
	
	@Override
	public List<Object> getMyGoalCDT(Map<String, Object> param) {
		List<Object> list = memberInfoDAO.getMyGoalCDT(param);
		if(list == null || list.size() < 1) list = memberInfoDAO.getTransferMyGoalCDT(param);
		return list;	
	}
	
	@Override
	public DataMap getMyCDT(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (DataMap) memberInfoDAO.getMyCDT(param);
	}
	
	@Override
	public List<Object> getMyGPA(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return memberInfoDAO.getMyGPA(param);
	}
	
	@Override
	public List<Object> getMyCdtDetail(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return memberInfoDAO.getMyCdtDetail(param);
	}
	
	@Override
	public List<Object> getMyReqCDT(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return memberInfoDAO.getMyReqCDT(param);
	}
	
	@Override
	public List<Object> getMyCumCDT(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return memberInfoDAO.getMyCumCDT(param);
	}
	
	@Override
	public List<Object> getMyMajorReq(Map<String, Object> param) {
		return memberInfoDAO.getMyMajorReq(param);
	}
	
	@Override
	public int getChkMinorReq(Map<String, Object> param) {
		return memberInfoDAO.getChkMinorReq(param);
	}
	
	@Override
	public List<Object> getMyMinorReq(Map<String, Object> param) {
		return memberInfoDAO.getMyMinorReq(param);
	}
	
	@Override
	public List<Object> getMyGradReq(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return memberInfoDAO.getMyGradReq(param);
	}
	
	@Override
	public List<Object> getMySubjectCDT(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return memberInfoDAO.getMySubjectCDT(param);
	}
	
	@Override
	public List<Object> getMyRecordHistory(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return memberInfoDAO.getMyRecordHistory(param);
	}
	
	@Override
	public List<Object> getMyHashtag(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return memberInfoRbsDAO.getMyHashtag(param);
	}
	
	@Override
	public int hashtagInsert(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		return memberInfoRbsDAO.hashtagInsert(param);
	}
	
	@Override
	public int hashtagDelete(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		return memberInfoRbsDAO.hashtagDelete(param);
	}



}