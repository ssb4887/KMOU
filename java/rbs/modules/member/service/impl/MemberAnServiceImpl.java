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
import rbs.modules.member.service.MemberAnLogService;
import rbs.modules.member.service.MemberAnService;
import rbs.modules.menu.service.MenuVerService;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 */
@Service("memberAnService")
public class MemberAnServiceImpl extends EgovAbstractServiceImpl implements MemberAnService {

	@Resource(name="memberAnMapper")
	private MemberAnMapper memberAnDAO;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	@Resource(name = "memberAnLogService")
	private MemberAnLogService memberLogService;

	@Resource(name="menuVerService")
	protected MenuVerService menuVerService;
	
	private Logger logger = LoggerFactory.getLogger("admMember");

	//private Logger mlogger = LoggerFactory.getLogger("manageContentsAuth");
    /**
     * 전체 목록 수
	 * @param mapFlag
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(Map<String, Object> param) {
    	return memberAnDAO.getTotalCount(param);
    }

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param) {
		return memberAnDAO.getList(param);
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getView(Map<String, Object> param) {
		DataMap viewDAO = memberAnDAO.getView(param);
		return viewDAO;
	}

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(Map<String, Object> param) {
		DataMap viewDAO = memberAnDAO.getModify(param);
		return viewDAO;
	}

	@Override
	public List<Object> getMemberGrupList(Map<String, Object> param) {
		return memberAnDAO.getMemberGrupList(param);
		
	}
    
	/**
	 * 기본정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param Map 등록정보
	 * @return result 등록결과
	 * @throws Exception
	 */
	@Override
	public int insert(String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception  {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		Map<String, Object> applyWebSiteMenuMbrMap = menuVerService.setMenuMbrMap();
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		
		//고유아이디 셋팅
		String mbrCd = memberAnDAO.getNextId(null);//idgenService.getNextStringId();

		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(null, parameterMap, items, itemOrder);

		if(dataMap == null || dataMap.size() == 0) return -1;
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 저장 항목
    	dataList.add(new DTForm("MEMBER_IDX", mbrCd));
    	
    	String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		    	
    	dataList.add(new DTForm("REGI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("REGI_ID", loginMemberId));
    	dataList.add(new DTForm("REGI_NAME", loginMemberName));
    	dataList.add(new DTForm("REGI_IP", regiIp));
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));

		
    	param.put("dataList", dataList);
    	
		int result = memberAnDAO.insert(param);
		
		int result1 = 0;
		if(result > 0) {
			// 사용자그룹 등록
			String[] mbrGrps = StringUtil.getStringArray(parameterMap.get("mbrGrp"));
			
			if(mbrGrps != null) {
				int grpCdLen = mbrGrps.length;
				for(int i = 0 ; i < grpCdLen ; i ++) {
					param = new HashMap<String, Object>();
					dataList = new ArrayList<DTForm>();
					
			    	dataList.add(new DTForm("MEMBER_IDX", mbrCd));
			    	dataList.add(new DTForm("GROUP_CODE", mbrGrps[i]));
			    	param.put("dataList", dataList);
			    	
			    	result1 = memberAnDAO.insertGrp(param);
				}
			}

			memberLogService.setEprivacy(logger, rbsMessageSource.getMessage("message.member.log.insert"), null, null, items, mbrCd, loginMemberIdx, loginMemberId, loginMemberName);
			Map<String, Object> webSiteMenuMbrMap = menuVerService.setMenuMbrMap();
			menuVerService.setMenuManageAuthLog(webSiteMenuMbrMap, applyWebSiteMenuMbrMap);
		}
		return result;
	}

	/**
	 * 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param param 수정정보
	 * @throws Exception
	 */
	@Override
	public int update(String submitType, String mbrCd, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		Map<String, Object> applyWebSiteMenuMbrMap = menuVerService.setMenuMbrMap();
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		searchList.add(new DTForm("MEMBER_IDX", mbrCd));
		
		// 항목설정으로 저장항목 setting
		//commandMap.put("mbrId", loginVO.getMbrId()); - 내정보수정시에만 사용
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(null, parameterMap, items, itemOrder);
		
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 저장 항목
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}

    	dataList.add(new DTForm("LOGIN_FAIL", 0));
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));

    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
    	int result = memberAnDAO.update(param);
		int result1 = 0;
		if(result > 0 && !StringUtil.isEquals(submitType, "myinfo")) {
			// 사용자그룹 삭제
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
	    	searchList.add(new DTForm("MEMBER_IDX", mbrCd));
	    	param.put("searchList", searchList);
	    	
	    	result1 = memberAnDAO.cdeleteGrp(param);

			// 사용자그룹 등록
			String[] mbrGrps = StringUtil.getStringArray(parameterMap.get("mbrGrp"));
			
			if(mbrGrps != null){
				int grpCdLen = mbrGrps.length;
				for(int i = 0 ; i < grpCdLen ; i ++) {
					param = new HashMap<String, Object>();
					dataList = new ArrayList<DTForm>();
					
			    	dataList.add(new DTForm("MEMBER_IDX", mbrCd));
			    	dataList.add(new DTForm("GROUP_CODE", mbrGrps[i]));
			    	param.put("dataList", dataList);
			    	
			    	result1 = memberAnDAO.insertGrp(param);
				}
			}
			
			Map<String, Object> webSiteMenuMbrMap = menuVerService.setMenuMbrMap();
			menuVerService.setMenuManageAuthLog(webSiteMenuMbrMap, applyWebSiteMenuMbrMap);
			
		}
		
		memberLogService.setEprivacy(logger, rbsMessageSource.getMessage("message.member.log.modify"), null, null, items, mbrCd, loginMemberIdx, loginMemberId, loginMemberName);
		return result;
	}
	/*
	@Override
	public int update(String mbrCd, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		searchList.add(new DTForm("MEMBER_IDX", mbrCd));
		
		// 항목설정으로 저장항목 setting
		//commandMap.put("mbrId", loginVO.getMbrId()); - 내정보수정시에만 사용
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(null, parameterMap, items, itemOrder);
		
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 저장 항목
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));

    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
		DataMap dt = memberAnDAO.getModify(param);
    	
    	int result = memberAnDAO.update(param);
		
		if(result > 0) {
			String authName = rbsMessageSource.getMessage("item.menu.manager.auth.name");
			String grant = rbsMessageSource.getMessage("message.auth.grant");
			String cancel = rbsMessageSource.getMessage("message.auth.cancel");
			String usertypeAuthType= rbsMessageSource.getMessage("message.auth.usertype");
			String groupAuthType= rbsMessageSource.getMessage("message.auth.group");
			String departAuthType= rbsMessageSource.getMessage("message.auth.depart");
			String memberAuthType= rbsMessageSource.getMessage("message.auth.member");

			DataMap modifyDt = memberAnDAO.getModify(param);
			
			int usertypeIdx = StringUtil.getInt(dt.get("USERTYPE_IDX"));
			int modifyUsertypeIdx = StringUtil.getInt(modifyDt.get("USERTYPE_IDX"));
			if(usertypeIdx != modifyUsertypeIdx){
				Map<String, Object> menuParam = new HashMap<String, Object>();
				List<DTForm> menuSearchList = new ArrayList<DTForm>();
				
				menuSearchList.add(new DTForm("A.MANAGER_USERTYPE_IDX", usertypeIdx, ">="));
				menuParam.put("searchList", menuSearchList);
				
				List<?> menuList = memberAnDAO.allMenuApplyList(menuParam);

				int menuLen = 0;
				if(menuList != null) menuLen = menuList.size();
				
				for(int i = 0; i < menuLen; i++){
					DataMap menuDt = (DataMap)menuList.get(i);
					int menuManageUsertypeIdx = StringUtil.getInt(menuDt.get("MANAGER_USERTYPE_IDX"));
					if(menuManageUsertypeIdx > modifyUsertypeIdx){
						mlogger.info(departAuthType + ", " + StringUtil.getString(modifyDt.get("USERTYPE_NAME")) + ", " + modifyDt.get("MEMBER_IDX") + ", " + modifyDt.get("MEMBER_ID") + ", " + modifyDt.get("MEMBER_NAME") 
								+ ", " + menuDt.get("SITE_ID") + ", " + menuDt.get("VER_IDX") + ", " + menuDt.get("MENU_IDX") + ", " + menuDt.get("SITE_NAME") + " " + menuDt.get("MENU_NAME") + ", " + authName + ", " + cancel
								+ ", " + loginMemberIdx + ", " + loginMemberId + ", " + loginMemberName + ", " + DateUtil.getThisDate("yyyy-MM-dd HH:mm:ss") + ", " + regiIp);
						
					}
				}
			}
			
			int departIdx = StringUtil.getInt(dt.get("DEPART_IDX"));
			int modifyDepartIdx = StringUtil.getInt(modifyDt.get("DEPART_IDX"));
			if(departIdx != modifyDepartIdx){
				Map<String, Object> menuParam = new HashMap<String, Object>();
				List<DTForm> menuSearchList = new ArrayList<DTForm>();
				
				menuSearchList.add(new DTForm("MULTI.ITEM_ID", "managerDepartIdxs"));
				menuSearchList.add(new DTForm("MULTI.ITEM_KEY", new int[]{departIdx, modifyDepartIdx}));
				menuParam.put("searchList", menuSearchList);
				
				List<?> menuList = memberAnDAO.allMenuApplyMultiList(menuParam);

				int menuLen = 0;
				if(menuList != null) menuLen = menuList.size();
				
				for(int i = 0; i < menuLen; i++){
					DataMap menuDt = (DataMap)menuList.get(i);
					int menuManageDepartIdx = StringUtil.getInt(menuDt.get("ITEM_KEY"));
					if(menuManageDepartIdx == departIdx){
						mlogger.info(departAuthType + ", " + StringUtil.getString(modifyDt.get("DEPART_NAME")) + ", " + modifyDt.get("MEMBER_IDX") + ", " + modifyDt.get("MEMBER_ID") + ", " + modifyDt.get("MEMBER_NAME") 
								+ ", " + menuDt.get("SITE_ID") + ", " + menuDt.get("VER_IDX") + ", " + menuDt.get("MENU_IDX") + ", " + menuDt.get("SITE_NAME") + " " + menuDt.get("MENU_NAME") + ", " + authName + ", " + cancel
								+ ", " + loginMemberIdx + ", " + loginMemberId + ", " + loginMemberName + ", " + DateUtil.getThisDate("yyyy-MM-dd HH:mm:ss") + ", " + regiIp);
						
					}
					else if(menuManageDepartIdx ==  modifyDepartIdx){
						mlogger.info(departAuthType + ", " + StringUtil.getString(modifyDt.get("DEPART_NAME")) + ", " + modifyDt.get("MEMBER_IDX") + ", " + modifyDt.get("MEMBER_ID") + ", " + modifyDt.get("MEMBER_NAME") 
								+ ", " + menuDt.get("SITE_ID") + ", " + menuDt.get("VER_IDX") + ", " + menuDt.get("MENU_IDX") + ", " + menuDt.get("SITE_NAME") + " " + menuDt.get("MENU_NAME") + ", " + authName + ", " + grant
								+ ", " + loginMemberIdx + ", " + loginMemberId + ", " + loginMemberName + ", " + DateUtil.getThisDate("yyyy-MM-dd HH:mm:ss") + ", " + regiIp);
					}
				}
			}

			List<Object> memberGrupList = memberAnDAO.getMemberGrupList(param);
			int memberGrupLen = 0;
			String[] memberGrups = null;
			if(memberGrupList != null) {
				memberGrupLen = memberGrupList.size();
				memberGrups = new String[memberGrupLen];
			}
			
			for (int i = 0; i < memberGrupLen; i++) {
				String grupCode = StringUtil.getString(memberGrupList.get(i));
				memberGrups[i] = StringUtil.getString(grupCode);
			}
			
			Map<String, Object> menuParam = new HashMap<String, Object>();
			List<DTForm> menuSearchList = new ArrayList<DTForm>();
			
			menuSearchList.add(new DTForm("MULTI.ITEM_ID", "managerGroupIdxs"));
			menuSearchList.add(new DTForm("MULTI.ITEM_KEY", memberGrups));
			menuParam.put("searchList", menuSearchList);
			
			List<?> menuList = memberAnDAO.allMenuApplyMultiList(menuParam);
			
			// 사용자그룹 삭제
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
	    	searchList.add(new DTForm("MEMBER_IDX", mbrCd));
	    	param.put("searchList", searchList);
	    	
			memberAnDAO.cdeleteGrp(param);

			// 사용자그룹 등록
			String[] mbrGrps = StringUtil.getStringArray(parameterMap.get("mbrGrp"));
			int grpCdLen = 0;
			if(mbrGrps != null) grpCdLen = mbrGrps.length;
			for(int i = 0 ; i < grpCdLen ; i ++) {
				param = new HashMap<String, Object>();
				dataList = new ArrayList<DTForm>();
				
		    	dataList.add(new DTForm("MEMBER_IDX", mbrCd));
		    	dataList.add(new DTForm("GROUP_CODE", mbrGrps[i]));
		    	param.put("dataList", dataList);
		    	
		    	memberAnDAO.insertGrp(param);
			}

			int menuLen = 0;
			if(menuList != null) menuLen = menuList.size();
			
			groupMenuFor :  for(int i = 0; i < menuLen; i++){
				DataMap menuDt = (DataMap)menuList.get(i);
				for (int j = 0; j < grpCdLen; j++) {
					if(StringUtil.isEquals(menuDt.get("ITEM_KEY"), mbrGrps[j])) continue groupMenuFor;
					else break;
				}
				
				mlogger.info(groupAuthType + ", " + menuDt.get("GROUP_NAME") + ", " + modifyDt.get("MEMBER_IDX") + ", " + modifyDt.get("MEMBER_ID") + ", " + modifyDt.get("MEMBER_NAME") 
						+ ", " + menuDt.get("SITE_ID") + ", " + menuDt.get("VER_IDX") + ", " + menuDt.get("MENU_IDX") + ", " + menuDt.get("SITE_NAME") + " " + menuDt.get("MENU_NAME") + ", " + authName + ", " + cancel
						+ ", " + loginMemberIdx + ", " + loginMemberId + ", " + loginMemberName + ", " + DateUtil.getThisDate("yyyy-MM-dd HH:mm:ss") + ", " + regiIp);
					
			}
		}
		return result;
	}
*/
    /**
     * 삭제 목록 수
	 * @param mapFlag
	 * @param param
	 * @return
	 */
    @Override
	public int getDeleteCount(Map<String, Object> param) {
    	return memberAnDAO.getDeleteCount(param);
    }

	/**
	 * 삭제 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(Map<String, Object> param) {
		return memberAnDAO.getDeleteList(param);
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param param 삭제정보
	 * @throws Exception
	 */
	@Override
	public int delete(String[] deleteIdxs, String regiIp) throws Exception {
		if(deleteIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		
		// 저장조건
		searchList.add(new DTForm("MEMBER_IDX", deleteIdxs));
		
		// 저장 항목
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));

		param.put("searchList", searchList);
		param.put("dataList", dataList);
		
		return memberAnDAO.delete(param);
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param param 복원정보
	 * @throws Exception
	 */
	@Override
	public int restore(String[] restoreIdxs, String regiIp) throws Exception {
		if(restoreIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		// 저장조건
		searchList.add(new DTForm("MEMBER_IDX", restoreIdxs));
		
		// 저장 항목
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));

		param.put("searchList", searchList);
		param.put("dataList", dataList);
		
		return memberAnDAO.restore(param);
	}

	/**
	 * 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param param 완전삭제정보
	 * @throws Exception
	 */
	@Override
	public int cdelete(JSONObject items, String[] deleteIdxs) throws Exception {
		if(deleteIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 저장조건
		searchList.add(new DTForm("MEMBER_IDX", deleteIdxs));
		param.put("searchList", searchList);

		List<?> list = memberAnDAO.getDeleteList(param);
		String maxMemberIdx = memberAnDAO.getMaxMemberIdx();
		int result = memberAnDAO.cdelete(param);
		if(result > 0){
			LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
			
			String regiIdx = null;
			String regiId = null;
			String regiName = null;
			if(loginVO != null){
				regiIdx = loginVO.getMemberIdx();
				regiId = loginVO.getMemberIdOrg();
				regiName = loginVO.getMemberNameOrg();
			}		

			int listLen = 0;
			if(list != null) {
				DataMap deleteMaxDt = (DataMap)list.get(0);
				if(StringUtil.isEquals(deleteMaxDt.get("MEMBER_IDX"), maxMemberIdx)) {
					// 마지막 memberIdx가 삭제된 경우 - 빈 data 등록 (자신 정보 가져오는 키가 잘 못 매칭되는 것 방지)
					param = new HashMap<String, Object>();
					List<DTForm> dataList = new ArrayList<DTForm>();
					
					// 저장조건
					dataList.add(new DTForm("MEMBER_IDX", maxMemberIdx));
					dataList.add(new DTForm("MEMBER_ID", "-"));
					dataList.add(new DTForm("MEMBER_NAME", "-"));
					dataList.add(new DTForm("MEMBER_STATE", "2"));
					dataList.add(new DTForm("ISDELETE", "2"));
					param.put("dataList", dataList);
					result = memberAnDAO.insert(param);
				}
				
				listLen = list.size();
				for (int i = 0; i < listLen; i++) {
					DataMap dt = (DataMap)list.get(i);
					if(dt == null) continue;
					memberLogService.setEprivacy(logger, rbsMessageSource.getMessage("message.member.log.delete"), dt, null, items, StringUtil.getString(dt.get("MEMBER_IDX")), regiIdx, regiId, regiName);
				}
			}
		}
		
		return result;
	}
}