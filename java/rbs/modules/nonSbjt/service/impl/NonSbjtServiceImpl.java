package rbs.modules.nonSbjt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.util.ClientUtil;
import com.woowonsoft.egovframework.util.StringUtil;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.modules.nonSbjt.mapper.NonSbjtMapper;
import rbs.modules.nonSbjt.service.NonSbjtService;

/**
 * 비교과에 관한 구현 클래스를 정의한다.
 * @author 이동근
 *
 */
@Service("nonSbjtService")
public class NonSbjtServiceImpl extends EgovAbstractServiceImpl implements NonSbjtService {
	@Resource(name="nonSbjtMapper")
	private NonSbjtMapper nonSbjtDAO;	
	
	@Override
	public List<Object> getInitNonSbjtList(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		return nonSbjtDAO.getInitNonSbjtList(param);
	}
	
	@Override
	public DataMap getInitNonSbjtListCount(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		return nonSbjtDAO.getInitNonSbjtListCount(param);
	}
	
	/**
	 * 비교과 ELSearch 결과의 키값으로 표시할 리스트(카드) 정보
	 * @param idList
	 * @return
	 */
	@Override
	public List<Object> getNonSbjtList(Map<String, Object> param) throws Exception {
		if(param.get("IS_EMPTY_SEARCH").equals("Y")) {
			//DB 쿼리 호출 값(빈값 검색)
			return nonSbjtDAO.getNonSbjtListEmptySearchY(param);
		}else {
			//SEARCH API 호출값(키워드 검색)
			return nonSbjtDAO.getNonSbjtListEmptySearchN(param);
		}
	}
	
	/**
	 * 비교과 핵심역량 분류
	 * @param parent
	 * @return
	 */
	@Override
	public List<Object> getCategory(Map<String, Object> param) throws Exception{		
		return nonSbjtDAO.getCategory(param);
	}

	/**
	 * 비교과 태그
	 * @return
	 */
	@Override
	public List<Object> getTag() throws Exception {
		return nonSbjtDAO.getTag();
	}
	
	/**
	 * 비교과 프로그램 상세조회
	 * @param idx
	 * @param tidx 
	 * @return
	 */
	@Override
	public DataMap getNonsbjtInfo(Map<String, Object> param) throws Exception {
		return nonSbjtDAO.getNonsbjtInfo(param);
	}

	/**
	 * 비교과 프로그램 태그
	 * @param tidx 
	 * @return
	 */
	@Override
	public List<Object> getNonSbjtTag(Map<String, Object> param) throws Exception {
		return nonSbjtDAO.getNonSbjtTag(param);
	}

	/**
	 * 비교과 프로그램 핵심역량 지수
	 * @param tidx 
	 * @return
	 */
	@Override
	public List<Object> getNonSbjtEssential(Map<String, Object> param) throws Exception {
		return nonSbjtDAO.getNonSbjtEssential(param);
	}

	/**
	 * 비교과 프로그램 첨부파일
	 * @return filesList
	 */
	@Override
	public List<Object> getNonSbjtAttachmentFile(Map<String, Object> param) throws Exception{
		return nonSbjtDAO.getNonSbjtAttachmentFile(param);
	}

	@Override
	public List<Object> getNonSbjtHist(Map<String, Object> param) throws Exception {
		return nonSbjtDAO.getNonSbjtHist(param);
	}

	@Override
	public List<Object> getMyNonSbjtSigninHist(Map<String, Object> param) throws Exception {
		return nonSbjtDAO.getMyNonSbjtSigninHist(param);
	}

	@Override
	public List<Object> getProgramType() throws Exception {
		return nonSbjtDAO.getProgramType();
	}

	@Override
	public List<Object> getMethod() throws Exception {
		return nonSbjtDAO.getMethod();
	}

}