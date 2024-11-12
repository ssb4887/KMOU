package rbs.egovframework.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

/**
 * 공통코드에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("rbsCodeOptnMapper")
public class CodeOptnMapper extends RbsAbstractMapper{

	/**
	 * 다차원 분류 master 목록
	 * @param param
	 * @return
	 */
	public List<Object> getClassMstrList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.classMasterList", getLangParam(param));
    }
	
	/**
	 * 다차원 분류
	 * @param param
	 * @return
	 */
	public List<Object> getClassOptnList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.classOptionList", getLangParam(param));
    }
	
	/**
	 * 기초코드
	 * @param param
	 * @return
	 */
    public Map<String, List<Object>> getClassOptnMapList(Map<String, Object> param) {
        List<Object> list = (List<Object>)selectList("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.classOptionMapList", getLangParam(param));
    	return selectMapList(list, "MASTER_CODE");
    }
	/**
	 * 다차원 분류 maxLevel
	 * @param param
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> getClassMaxLevelOptnMap(Map<String, Object> param){
		Map<Object, Object> resultMap = (Map<Object, Object>)selectMap("rbs.egovframework.rbsCodeOptnMapper.classMaxLevelMap", param, "MASTER_CODE");
		return (Map)Collections.checkedMap(resultMap, Object.class, Object.class);
    }
	
	/**
	 * 다차원 분류 maxLevel
	 * @param param
	 * @return
	 */
	public int getClassMaxLevel(Map<String, Object> param){
        return (Integer)selectOne("rbs.egovframework.rbsCodeOptnMapper.classMaxLevel", param);
    }
	
	/**
	 * 기초코드 master 목록
	 * @param param
	 * @return
	 */
	public List<Object> getMstrList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.masterList", getLangParam(param));
    }
	
	/**
	 * 코드명
	 * @param param
	 * @return
	 */
	public String getOptnName(Map<String, Object> param){
        return (String)selectOne("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.optionName", getLangParam(param));
    }
	
	/**
	 * 코드명
	 * @param param
	 * @return
	 */
	public String getUstpName(Map<String, Object> param){
        return (String)selectOne("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.usertypeName", getLangParam(param));
    }
	
	/**
	 * 기초코드
	 * @param param
	 * @return
	 */
	public List<Object> getOptnList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.optionList", getLangParam(param));
    }
	
	/**
	 * 기초코드 : JSON에서 사용
	 * @param param
	 * @return
	 */
	public List<Object> getOptnJSONList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.optionJSONList", getLangParam(param));
    }
	
	/**
	 * 기초코드
	 * @param param
	 * @return
	 */
    public Map<String, List<Object>> getOptnMapList(Map<String, Object> param) {
        List<Object> list = (List<Object>)selectList("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.optionMapList", getLangParam(param));
    	return selectMapList(list, "MASTER_CODE");
    }

	/**
	 * 회원
	 * @param param
	 * @return
	 */
	public List<Object> getMemberList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.memberList", getLangParam(param));
    }

	/**
	 * 회원
	 * @param param
	 * @return
	 */
	public List<Object> getMemberJSONList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.memberJSONList", getLangParam(param));
    }
	
	/**
	 * 사용자유형
	 * @param param
	 * @return
	 */
	public List<Object> getUstpList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.usertypeList", getLangParam(param));
    }
	
	/**
	 * 그룹
	 * @param param
	 * @return
	 */
	public List<Object> getGrupList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.groupList", getLangParam(param));
    }
	
	/**
	 * 부서
	 * @param param
	 * @return
	 */
	public List<Object> getDprtList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.departList", getLangParam(param));
    }
	
	/**
	 * 메뉴에서 사용하는 모듈
	 * @param param
	 * @return
	 */
	public List<Object> getMenuModuleList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.egovframework" + getLangFlag() + ".rbsCodeOptnMapper.menuModuleList", param);
    }
}