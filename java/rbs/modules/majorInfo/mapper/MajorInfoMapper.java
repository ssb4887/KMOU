package rbs.modules.majorInfo.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * 샘플모듈에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("majorInfoMapper")
public class MajorInfoMapper extends EgovAbstractMapper{

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectList", param);
    }
	
	/**
	 * 주관 대학 목록
	 * @param param
	 * @return
	 */
	public List<Object> getCollegeList() {
		return selectList("rbs.modules.majorInfo.majorInfoMapper.getCollegeList");
	}
	
	/**
	 * 주관 대학 - 학부(과) 목록
	 * @param param
	 * @return
	 */
	public List<Object> getDepartList(Map<String, Object> param) {
		return selectList("rbs.modules.majorInfo.majorInfoMapper.getDepartList", param);
	}
	
	/**
	 * 주관 대학 - 학부(과) - 전공 목록
	 * @param param
	 * @return
	 */
	public List<Object> getMajorList(Map<String, Object> param) {
		return selectList("rbs.modules.majorInfo.majorInfoMapper.getMajorList", param);
	}
	
	/**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getDeptList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectDeptList", param);
    }
	public List<Object> getTalentAbtyList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectTalentList", param);
	}
	public List<Object> getAbtyMngList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectAbtyMngList", param);
	}
	
	public List<Object> getFieldList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectFieldList", param);
	}
	public List<Object> getNonSbjtList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectNonSbjtList", param);
	}
	public List<Object> getLicenseList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectLicenseList", param);
	}
	
	public List<Object> getTrackList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectTrackList", param);
	}
	public List<Object> getAbilityList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectAbtyList", param);
	}
	public List<Object> getDtlList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectDtlList", param);
	}
	public List<Object> getCourMajorList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectCourMajorList", param);
	}
	public List<Object> getAddMajorList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectAddMajorList", param);
	}
	public List<Object> getRcmdCultList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectRcmdCultList", param);
	}
	public List<Object> getHaksaRcmdCultList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectHaksaRcmdCultList", param);
	}	
	
	public List<Object> insertAddMajorList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.insertAddMajorList", param);
	}
	
	

	/**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insertMajor(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.insertMajor", param);
    }
	
	
    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getTotalCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.selectCount", param);
    }
    
    /**
     * 학부/학과 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDeptCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.selectDeptCount", param);
    }
    
    /**
     * 트랙 총 갯수를 조회한다.
     * @param param 검색조건
     */
    public int getTrackCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.selectTrackCount", param);
    }
    public int getAbilityCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.selectAbtyCount", param);
    }
    public int getMajorCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.selectMajorCount", param);
    }
    public int getMajorEtcCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.selectMajorEtcCount", param);
    }
    public int getMajorYearCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.selectMajorYearCount", param);
    }
    
    /**
     * 파일항목 목록
     * @param param
     * @return
     */
	public List<Object> getFileList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectFileList", param);
    }
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getFileView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.majorInfo.majorInfoMapper.selectFileView", param);
	}
	
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getView(Map<String, Object> param) {
         return (DataMap)selectOne("rbs.modules.majorInfo.majorInfoMapper.selectView", param);
	}

	/**
     * 다운로드수 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int updateFileDown(Map<String, Object> param){
        return super.update("rbs.modules.majorInfo.majorInfoMapper.updateFileDown",param);
    }
    
    /**
     * 권한여부 조회
     * @param param
     * @return
     */
    public int getAuthCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.authCount", param);
    }
    
    /**
     * 등록된 정보 중 검색조건에 맞는 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return 상세정보
     */
	public DataMap getModify(Map<String, Object> param) {
		return (DataMap)selectOne("rbs.modules.majorInfo.majorInfoMapper.selectModify", param);
	}
	public DataMap getDeptModify(Map<String, Object> param) {
		return (DataMap)selectOne("rbs.modules.majorInfo.majorInfoMapper.selectDeptModify", param);
	}
	public DataMap getModifyTrack(Map<String, Object> param) {
		return (DataMap)selectOne("rbs.modules.majorInfo.majorInfoMapper.selectModifyTrack", param);
	}
	public DataMap getModifyAbility(Map<String, Object> param) {
		return (DataMap)selectOne("rbs.modules.majorInfo.majorInfoMapper.selectModifyAbty", param);
	}

    /**
     * 게시물 key 조회한다.
     * @return int key
     */
	public int getMajorIdx(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.getMajorIdx", param);
    }
	public int getStatisticIdx(Map<String, Object> param) {
		return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.getStatisticIdx", param);
	}
	public int getTrackIdx(Map<String, Object> param) {
		return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.getTrackIdx", param);
	}
	public int getJobIdx(Map<String, Object> param) {
		return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.getJobIdx", param);
	}
    public int getNextId(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.nextId", param);
    }
    public int getNextMajorId(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.nextMajorId", param);
    }
    public int getNextYearId(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.nextYearId", param);
    }
    public int getNextFileId(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.nextFileId", param);
    }
    public int getNextJobId(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.nextJobId", param);
    }
    public int getNextDtlId(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.nextDtlId", param);
    }
    public int getNextStatisticId(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.nextStatisticId", param);
    }
    public int getNextTrackId(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.nextTrackId", param);
    }
    
    
    public int getCopyYear(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.getCopyYear", param);
    }
    
    public int getNextOrd(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.nextOrd", param);
    }
    
    public int getCourCultCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.CourCultCount", param);
    }
    
    
    public int getNextMajorOrdId(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.nextMajorOrdId", param);
    }
    

    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insert(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.insert", param);
    }
    
    public int insertCourCult(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.insertCourCult", param);
    }
    
    /**
     * 학부/학과 관리
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insertDept(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.insertDept", param);
    }
    
    
    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insertMajorYear(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.insertMajorYear", param);
    }
    
    public int copyMajorYear(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.copyMajorYear", param);
    }
    public int copyMajorStatistic(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.copyMajorStatistic", param);
    }
    public int copyMajorTrack(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.copyMajorTrack", param);
    }
    public int copyMajorJob(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.copyMajorJob", param);
    }
    public int copyMajorDtl(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.copyMajorDtl", param);
    }
    
    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insertMajorFile(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.insertMajorFile", param);
    }
    
    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insertMajorJob(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.insertMajorJob", param);
    }
    
    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insertMajorJobDtl(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.insertMajorJobDtl", param);
    }
    
    public int deleteMajorJobDtl(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.deleteMajorJobDtl", param);
    }
    
    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insertMajorStatistic(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.insertMajorStatistic", param);
    }
    
    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int insertMajorTrack(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.insertMajorTrack", param);
    }

    /**
     * 화면에 조회된일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
     * @param param 수정정보
     */
    public int update(Map<String, Object> param){
    	return super.update("rbs.modules.majorInfo.majorInfoMapper.update",param);
    }
    public int updateMajor(Map<String, Object> param){
    	return super.update("rbs.modules.majorInfo.majorInfoMapper.updateMajor",param);
    }
    public int updateDept(Map<String, Object> param){
    	return super.update("rbs.modules.majorInfo.majorInfoMapper.updateDept",param);
    }
    public int updateYear(Map<String, Object> param){
    	return super.update("rbs.modules.majorInfo.majorInfoMapper.updateYear",param);
    }
    public int updateStatistic(Map<String, Object> param){
    	return super.update("rbs.modules.majorInfo.majorInfoMapper.updateStatistic",param);
    }
    public int updateTrack(Map<String, Object> param){
    	return super.update("rbs.modules.majorInfo.majorInfoMapper.updateTrack",param);
    }
    public int updateJob(Map<String, Object> param){
    	return super.update("rbs.modules.majorInfo.majorInfoMapper.updateJob",param);
    }
    
    
    /**
     * 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
     * @param param 등록정보
     * @return String 등록결과
     */
    public int abtyInsert(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.abtyInsert", param);
    }
    
    public int abtyUpdate(Map<String, Object> param){
    	return super.update("rbs.modules.majorInfo.majorInfoMapper.abtyUpdate",param);
    }
    
    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int abtyDelete(Map<String, Object> param){
    	return super.update("rbs.modules.majorInfo.majorInfoMapper.abtyDelete",param);
    }
    
    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int parentAbtyDelete(Map<String, Object> param){
    	return super.update("rbs.modules.majorInfo.majorInfoMapper.parentAbtyDelete",param);
    }

    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 삭제목록정보
     */
	public List<Object> getDeleteList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.deleteList", param);
    }

    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getDeleteCount(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.majorInfo.majorInfoMapper.deleteCount", param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
     * @param param 삭제정보
     */
    public int delete(Map<String, Object> param){
    	return super.update("rbs.modules.majorInfo.majorInfoMapper.delete",param);
    }
    
    public int deleteTrack(Map<String, Object> param){
    	return super.update("rbs.modules.majorInfo.majorInfoMapper.deleteTrack",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
     * @param param 복원정보
     */
    public int restore(Map<String, Object> param){
    	return super.update("rbs.modules.majorInfo.majorInfoMapper.restore",param);
    }

    /**
     * 화면에 조회된 정보를 데이터베이스에서 삭제
     * @param param 완전삭제정보
     */
    public int cdelete(Map<String, Object> param){
    	return super.delete("rbs.modules.majorInfo.majorInfoMapper.cdelete", param);
    }

	/**
	 * 전공별 등록된 기본정보의 연도 가져오기
	 * @param mjCd 
	 * @return YY
	 */
	public List<Object> getRegisteredYear(String mjCd) {
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.getRegisteredYear", mjCd);
	}
	
	/**
	 * 전공능력 정보 delete
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int deleteMajorAbility(Map<String, Object> param) {
		return super.delete("rbs.modules.majorInfo.majorInfoMapper.deleteMajorAbility", param);
		
	}
	
	/**
	 * 전공능력 정보 delete
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int deleteNonSbjt(Map<String, Object> param) {
		return super.delete("rbs.modules.majorInfo.majorInfoMapper.deleteNonSbjt", param);
		
	}
	
	/**
	 * 전공능력 정보 delete
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int deleteLicense(Map<String, Object> param) {
		return super.delete("rbs.modules.majorInfo.majorInfoMapper.deleteLicense", param);
		
	}
	
	/**
	 * 전공능력 정보 insert
	 * @param param
	 * @return
	 * @throws Exception
	 */
    public int insertMajorAbility(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.insertMajorAbility", param);
    }
    
    /**
	 * 비교과 insert
	 * @param param
	 * @return
	 * @throws Exception
	 */
    public int insertNonSbjt(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.insertNonSbjt", param);
    }
    
    /**
	 * 자격증 insert
	 * @param param
	 * @return
	 * @throws Exception
	 */
    public int insertLicense(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.insertLicense", param);
    }
    
	public DataMap getModifyCourMajor(Map<String, Object> param) {
		return (DataMap)selectOne("rbs.modules.majorInfo.majorInfoMapper.selectModifyCourMajor", param);
	}
	
	/**
	 * 학부 교육과정 update
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int updateMajorCour(Map<String, Object> param) {
		return super.update("rbs.modules.majorInfo.majorInfoMapper.updateCourMajor",param);
	}
	
	/**
	 * 학부 교육과정 delete
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int deleteMajorCour(HashMap<String, Object> param) {
		return super.update("rbs.modules.majorInfo.majorInfoMapper.deleteCourMajorList",param);
		
	}
	
	/**
	 * 추천균형교양교과목 delete
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int deleteRcmdCult(HashMap<String, Object> param) {
		return super.update("rbs.modules.majorInfo.majorInfoMapper.deleteRcmdCultList",param);
		
	}   
	
	/**
	 * 학부 교육과정 insert
	 * @param param
	 * @return
	 * @throws Exception
	 */
    public int insertMajorCour(Map<String, Object> param){
    	return super.insert("rbs.modules.majorInfo.majorInfoMapper.insertMajorCour", param);
    }
	
	public List<Object> getIsCourMajor(HashMap<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.selectIsCourMajorList", param);
	}
	public List<Object> getFrontCourMajorList(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.getFrontCourMajorList", param);
	}
	public List<Object> getMicroMajorList(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.getMicroMajorList", param);
	}
	public List<Object> getMicroMajorSubjectList(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.getMicroMajorSubjectList", param);
	}
	public List<Object> getMjCdList(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.getMjCdList", param);
	}
	public int copyMajorInfo(Map<String, Object> param) {
		return super.update("rbs.modules.majorInfo.majorInfoMapper.copyMajorInfo",param) == -1 ? 1 : 0;
	}
	public List<Object> getFrontRcmdCultList(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.getFrontRcmdCultList", param);
	}
	public List<Object> getPermSustCdList(Map<String, Object> param) {
		return (List<Object>)selectList("rbs.modules.majorInfo.majorInfoMapper.getPermSustCdList", param);
	}
	
	 
}