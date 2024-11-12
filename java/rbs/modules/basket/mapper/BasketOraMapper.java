package rbs.modules.basket.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.AcademicAbstractMapper;
import rbs.egovframework.mapper.MartAbstractMapper;
import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.util.StringUtil;


@Repository("basketOraMapper")
public class BasketOraMapper extends MartAbstractMapper{
    /**
	 * 마이페이지 장바구니 상세정보
	 * @param param
	 * @return
	 */
    public List<Object> getMyBasketView(Map<String, Object> param) {
    		
    	return (List<Object>)selectList("mart.modules.basket.basketOraMapper.getMyBasketView", param);
    }
    
	/**
	 * 현재 학기 정보
	 * @param param
	 * @return
	 */
	public DataMap getCurInfo() {
		return (DataMap)selectOne("mart.modules.basket.basketOraMapper.getCurInfo");
	}
    
    /**
	 * 신청학점 및 평점 정보
	 * @param param
	 * @return
	 */
    public List<Object> getMrksList(String stuno) {
    	Map<String, Object> param = new HashMap<String, Object>();
    	
		param.put("stuno", stuno);

    		
    	return (List<Object>)selectList("rbs.modules.basket.basketOraMapper.getMrksList", param);
    }

    
    /**
   	 * 부전공 & 복수전공 체크
   	 * @param param
   	 * @return
   	 */
    public List<Object> getMajorChk(String stuno, String mjGbn) {
	   Map<String, Object> param = new HashMap<String, Object>();
   	
	   param.put("stuno", stuno);
	   param.put("mjGbn", mjGbn);

	
	   return (List<Object>)selectList("rbs.modules.basket.basketOraMapper.getMajorChk", param);
    }
   
    /**
   	 * 부전공 & 복수전공 과목 이수구분별 성적
   	 * @param param
   	 * @return
   	 */
    public List<Object> getMajorCptnHpList(String stuno, String cptnGbn) {
	   Map<String, Object> param = new HashMap<String, Object>();
   	
	   param.put("stuno", stuno);
	   param.put("cptnGbn", cptnGbn);

	
	   return (List<Object>)selectList("rbs.modules.basket.basketOraMapper.getMajorCptnHpList", param);
    }
    
    
    /**
   	 * 학기별 이수 현황
   	 * @param param
   	 * @return
   	 */
    public List<Object> getTermHpList(String stuno, String yy, String tmGbn) {
	   Map<String, Object> param = new HashMap<String, Object>();
   	
	   param.put("stuno", stuno);
	   param.put("yy", yy);
	   param.put("tmGbn", tmGbn);

	
	   return (List<Object>)selectList("rbs.modules.basket.basketOraMapper.getTermHpList", param);
    }
    
    /**
   	 * 입학년도 정보
   	 * @param param
   	 * @return
   	 */
    public List<Object> getEntrYearInfo(String stuno) {
	   Map<String, Object> param = new HashMap<String, Object>();
   	
	   param.put("stuno", stuno);

	
	   return (List<Object>)selectList("rbs.modules.basket.basketOraMapper.getEntrYearInfo", param);
    }
    
    public List<Object> getGrdtHpInfoOra(String entrYy, String entrTmGbn, String hgMjCd) {
 	   Map<String, Object> param = new HashMap<String, Object>();
    	
 	   param.put("entrYy", entrYy);
 	   param.put("hgMjCd", hgMjCd);
 	  param.put("entrTmGbn", entrTmGbn);
 	
 	   return (List<Object>)selectList("rbs.modules.basket.basketOraMapper.getGrdtHpInfoOra", param);
     }
    
    
    public List<Object> getGrdtMjHpInfoOra(String entrYy, String entrTmGbn, String hgMjCd) {
  	   Map<String, Object> param = new HashMap<String, Object>();
     	
  	   param.put("entrYy", entrYy);
  	   param.put("hgMjCd", hgMjCd);
  	  param.put("entrTmGbn", entrTmGbn);
  	
  	   return (List<Object>)selectList("rbs.modules.basket.basketOraMapper.getGrdtMjHpInfoOra", param);
      }
    
    public int selectMyTmCount(Map<String, Object> param){
		return selectOne("rbs.modules.basket.basketOraMapper.selectMyTmCount",param);
	}
    
    public int selectEqualTmAvgHpCount(Map<String, Object> param){
		return selectOne("rbs.modules.basket.basketOraMapper.selectEqualTmAvgHpCount",param);
	}
    
    public int selectEqualTmAvgHp(Map<String, Object> param){
		return selectOne("rbs.modules.basket.basketOraMapper.selectEqualTmAvgHp",param);
	}
    
    public List<Object> getCptnFld101Sum(String stuno) {
    	Map<String, Object> param = new HashMap<String, Object>();
       	
 	    param.put("stuno", stuno);

    	
		return selectList("rbs.modules.basket.basketOraMapper.getCptnFld101Sum", param);
	}
    
    public int getCptnFld111to7Count(Map<String, Object> param){
		return selectOne("rbs.modules.basket.basketOraMapper.getCptnFld111to7Count",param);
	}
    
    public int getCptn21Count(Map<String, Object> param){
		return selectOne("rbs.modules.basket.basketOraMapper.getCptn21Count",param);
	}
    
    /**
     * 장바구니 등록
     */
    public int insert(Map<String, Object> param){
    	return super.insert("rbs.modules.basket.basketOraMapper.insert", param);
    }
    
    /**
     * 장바구니 삭제
     */
    public int cdelete(Map<String, Object> param){
    	return super.delete("rbs.modules.basket.basketOraMapper.cdelete", param);
    }

	public List<Object> getPreApplSbjt(Map<String, Object> param) {
		return selectList("mart.modules.basket.basketOraMapper.getPreApplSbjt", param);
	}
	
	
	
	
	
	  /**
     * [예비수강신청] 학생 기본 정보 조회
     */
    public Map<String, Object> getSukangStudentInfo(Map<String, Object> param) {
        return selectOne("mart.modules.basket.basketOraMapper.getSukangStudentInfo", param);
    }

    /**
     * [예비수강신청] 학년도 및 학기 정보 조회
     */
    public Map<String, Object> getSukangAcademicYear(Map<String, Object> param) {
        return selectOne("mart.modules.basket.basketOraMapper.getSukangAcademicYear", param);
    }

    /**
     * [예비수강신청] 기간 체크
     */
    public String getSukangRegistrationPeriod(Map<String, Object> param) {
        return selectOne("mart.modules.basket.basketOraMapper.getSukangRegistrationPeriod", param);
    }

    /**
     * [예비수강신청] 학생 상태 확인
     */
    public Map<String, Object> getSukangStudentStatus(Map<String, Object> param) {
        return selectOne("mart.modules.basket.basketOraMapper.getSukangStudentStatus", param);
    }

    /**
     * [예비수강신청] 복학 상태 확인
     */
    public Map<String, Object> getSukangReturnStatus(Map<String, Object> param) {
        return selectOne("mart.modules.basket.basketOraMapper.getSukangReturnStatus", param);
    }

    /**
     * [예비수강신청] 수강 가능 학점 계산
     */
    public int getSukangMaxCredits(Map<String, Object> param) {
        return selectOne("mart.modules.basket.basketOraMapper.getSukangMaxCredits", param);
    }

    /**
     * [예비수강신청] 내역 조회
     */
    public Map<String, Object> getSukangRegisteredCredits(Map<String, Object> param) {
        return selectOne("mart.modules.basket.basketOraMapper.getSukangRegisteredCredits", param);
    }

    /**
     * [예비수강신청] 로그인 정보 기록
     */
    public int insertSukangLoginInfo(Map<String, Object> param) {
        return super.insert("mart.modules.basket.basketOraMapper.insertSukangLoginInfo", param);
    }
}