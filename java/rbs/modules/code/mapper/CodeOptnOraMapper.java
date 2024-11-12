package rbs.modules.code.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.AcademicAbstractMapper;

/**
 * 일반회원 관리에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("codeOptnOraMapper")
public class CodeOptnOraMapper extends AcademicAbstractMapper {
    /**
	 * 학사 정보 유틸
	 * @param param
	 * @return
	 */
    public List<Object> getHaksaCode(String upCode) {
    	return (List<Object>)selectList("rbs.modules.code.optionMapper.getHaksaCode", upCode);
    }

    public List<Object> getHaksaPartCode(String upCode) {
    	return (List<Object>)selectList("rbs.modules.code.optionMapper.getHaksaPartCode", upCode);
    }

    public List<Object> getHaksaAllCode(Map<String, Object> param) {
    	return (List<Object>)selectList("rbs.modules.code.optionMapper.getHaksaAllCode", param);
    }
    
    public List<Object> getHaksaAllContrCode(Map<String, Object> param) {
    	return (List<Object>)selectList("rbs.modules.code.optionMapper.getHaksaAllContrCode", param);
    }
    
    public List<Object> getHaksaAllColgCode(Map<String, Object> param) {
    	return (List<Object>)selectList("rbs.modules.code.optionMapper.getHaksaAllColgCode", param);
    }
    
    public List<Object> getHaksaAllContrColgCode(Map<String, Object> param) {
    	return (List<Object>)selectList("rbs.modules.code.optionMapper.getHaksaAllContrColgCode", param);
    }
    
    public List<Object> getHaksaAllClsfCode(Map<String, Object> param) {
    	return (List<Object>)selectList("rbs.modules.code.optionMapper.getHaksaAllClsfCode", param);
    }
    
    public List<Object> getHaksaAllEtcCode(Map<String, Object> param) {
    	return (List<Object>)selectList("rbs.modules.code.optionMapper.getHaksaAllEtcCode", param);
    }

    public List<Object> getDeptList(Map<String, Object> param) {
    	return (List<Object>)selectList("rbs.modules.code.optionMapper.selectDeptList", param);
    }
    
    public List<Object> getMajorList(Map<String, Object> param) {
    	return (List<Object>)selectList("rbs.modules.code.optionMapper.selectMajorList", param);
    }

    /**
	 * 소속 분류 : 대학, 계약학과
	 * @param param
	 * @return
	 */
	public List<Object> getContractGbnCode(String upCode) {
		return (List<Object>)selectList("rbs.modules.code.optionMapper.getContractGbnCode", upCode);
	}

	/**
	 * 소속 분류 : 대학 > 단과대학
	 * @param param
	 * @return
	 */
	public List<Object> getCollegeCode(String upCode) {
		return (List<Object>)selectList("rbs.modules.code.optionMapper.getCollegeCode", upCode);
	}

	/**
	 * 학과(부) : 대학 > 단과대학 > 학과/전공
	 * @param param
	 * @return
	 */
	public List<Object> getDepartmentCode(String upCode) {
		return (List<Object>)selectList("rbs.modules.code.optionMapper.getDepartmentCode", upCode);
	}

	/**
	 * 소속 분류 : 계약학과 > 단과대학
	 * @param param
	 * @return
	 */
	public List<Object> getContractCollegeCode(String upCode) {
		return (List<Object>)selectList("rbs.modules.code.optionMapper.getContractCollegeCode", upCode);
	}

	/**
	 * 학과(부) : 계약학과 > 단과대학 > 학과/전공
	 * @param param
	 * @return
	 */
	public List<Object> getContractDepartCode(String upCode) {
		return (List<Object>)selectList("rbs.modules.code.optionMapper.getContractDepartCode", upCode);
	}

	/**
	 * 이수 구분 ex) 전공 필수, 교양 필수
	 * @param param
	 * @return
	 */
	public List<Object> getCptnCode() {
		return (List<Object>)selectList("rbs.modules.code.optionMapper.getCptnCode");
	}

	/**
	 * 과정 구분 ex) 학사, 석사
	 * @param param
	 * @return
	 */
	public List<Object> getCorsCode() {
		return (List<Object>)selectList("rbs.modules.code.optionMapper.getCorsCode");
	}

	/**
	 * 핵심 역량 ex) 지식 탐구, 의사소통
	 * @param param
	 * @return
	 */
	public List<Object> get6CoreAbtyCode() {
		return (List<Object>)selectList("rbs.modules.code.optionMapper.get6CoreAbtyCode");
	}

	/**
	 * 연계 전공 ex)나노디그리,매트릭스
	 * @param param
	 * @return
	 */
	public List<Object> getTrackCode() {
		return (List<Object>)selectList("rbs.modules.code.optionMapper.getTrackCode");
	}
	
	/**
	 * 학년 구분
	 * @param param
	 * @return
	 */
	public List<Object> getHySeqGbn() {
		return (List<Object>)selectList("rbs.modules.code.optionMapper.getHySeqGbn");
	}

	/**
	 * 학기 구분
	 * @param param
	 * @return
	 */
	public List<Object> getTmGbnCode() {
		return (List<Object>)selectList("rbs.modules.code.optionMapper.getTmGbnCode");
	}
	
    /**
	 * 이수 구분/영역
	 * @param param
	 * @return
	 */
    public List<Object> getCompleteCode(Map<String, Object> param) {
    	return (List<Object>)selectList("rbs.modules.code.optionMapper.getCompleteCode", param);
    }
    
    /**
	 * 교과목 영역 유틸
	 * @param param
	 * @return
	 */
    public List<Object> getSubjectCode(Map<String, Object> param) {
    	return (List<Object>)selectList("rbs.modules.code.optionMapper.getSubjectCode", param);
    }
}