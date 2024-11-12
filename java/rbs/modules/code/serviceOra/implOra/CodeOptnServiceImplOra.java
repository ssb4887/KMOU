package rbs.modules.code.serviceOra.implOra;

import java.util.List;
import java.util.Map;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import rbs.modules.code.mapper.CodeOptnOraMapper;
import rbs.modules.code.serviceOra.CodeOptnServiceOra;

/**
 * 기관 정보 관리에 관한 인터페이스 클래스를 정의한다.
 * @author user
 */
@Service("codeOptnServiceOra")
public class CodeOptnServiceImplOra extends EgovAbstractServiceImpl implements CodeOptnServiceOra {
	@Value("${Globals.locale.lang.use}")
	protected int useLang; // 언어 사용 여부

	@Resource(name="codeOptnOraMapper")
	private CodeOptnOraMapper codeOptnOraDAO;

	/**
	 * 학사 정보 유틸
	 * @param param
	 * @return
	 */	
	@Override
	public List<Object> getHaksaCode(String upCode) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getHaksaCode(upCode);
		
		return codeInfo;
	}

	@Override
	public List<Object> getHaksaPartCode(String upCode) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getHaksaPartCode(upCode);
		
		return codeInfo;
	}

	@Override
	public List<Object> getHaksaAllCode(Map<String, Object> param) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getHaksaAllCode(param);
		
		return codeInfo;
	}
	
	@Override
	public List<Object> getHaksaAllContrCode(Map<String, Object> param) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getHaksaAllContrCode(param);
		
		return codeInfo;
	}
	
	@Override
	public List<Object> getHaksaAllColgCode(Map<String, Object> param) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getHaksaAllColgCode(param);
		
		return codeInfo;
	}
	
	@Override
	public List<Object> getHaksaAllContrColgCode(Map<String, Object> param) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getHaksaAllContrColgCode(param);
		
		return codeInfo;
	}
	
	@Override
	public List<Object> getHaksaAllClsfCode(Map<String, Object> param) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getHaksaAllClsfCode(param);
		
		return codeInfo;
	}
	
	@Override
	public List<Object> getHaksaAllEtcCode(Map<String, Object> param) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getHaksaAllEtcCode(param);
		
		return codeInfo;
	}

	@Override
	public List<Object> getDeptList(Map<String, Object> param) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getDeptList(param);
		
		return codeInfo;
	}
	
	@Override
	public List<Object> getMajorList(Map<String, Object> param) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getMajorList(param);
		
		return codeInfo;
	}

	/**
	 * 소속 분류 : 대학, 계약학과
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getContractGbnCode(String upCode) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getContractGbnCode(upCode);
		
		return codeInfo;
	}

	/**
	 * 소속 분류 : 대학 > 단과대학
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getCollegeCode(String upCode) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getCollegeCode(upCode);
		
		return codeInfo;
	}

	/**
	 * 학과(부) : 대학 > 단과대학 > 학과/전공
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDepartmentCode(String upCode) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getDepartmentCode(upCode);
		
		return codeInfo;
	}

	/**
	 * 소속 분류 : 계약학과 > 단과대학
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getContractCollegeCode(String upCode) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getContractCollegeCode(upCode);
		
		return codeInfo;
	}

	/**
	 * 학과(부) : 계약학과 > 단과대학 > 학과/전공
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getContractDepartCode(String upCode) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getContractDepartCode(upCode);
		
		return codeInfo;
	}

	/**
	 * 이수 구분 ex) 전공 필수, 교양 필수
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getCptnCode() throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getCptnCode();
		
		return codeInfo;
	}

	/**
	 * 과정 구분 ex) 학사, 석사
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getCorsCode() throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getCorsCode();
		
		return codeInfo;
	}

	/**
	 * 핵심 역량 ex) 지식 탐구, 의사소통
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> get6CoreAbtyCode() throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.get6CoreAbtyCode();
		
		return codeInfo;
	}

	/**
	 * 연계 전공 ex) 나노디그리, 매트릭스
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getTrackCode() throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getTrackCode();
		
		return codeInfo;
	}
	
	/**
	 * 학년 구분
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getHySeqGbn() throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getHySeqGbn();
		
		return codeInfo;
	}

	/**
	 * 연계 전공 학기 구분
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getTmGbnCode() throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getTmGbnCode();
		
		return codeInfo;
	}
	
	/**
	 * 이수 영역 유틸
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getCompleteCode(Map<String, Object> param) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getCompleteCode(param);
		
		return codeInfo;
	}
	
	/**
	 * 교과목 영역 유틸
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getSubjectCode(Map<String, Object> param) throws Exception {
		List<Object> codeInfo = codeOptnOraDAO.getSubjectCode(param);
		
		return codeInfo;
	}
}