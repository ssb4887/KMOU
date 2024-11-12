package rbs.usr.main.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import rbs.egovframework.LoginVO;
import rbs.egovframework.mapper.MartAbstractMapper;
import rbs.egovframework.mapper.RbsAbstractMapper;

/**
 * 다기능게시판에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("usrMainMapper")
public class MainMapper extends MartAbstractMapper{
	
	public List<Object> getModuleList(String moduleId, Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.dashboard.dashboardMapper." + moduleId + "List", param);
    }

	public Map<Object, List<Object>> getBoardHashMap(Map<String, Object> param){
		List<Object> list = (List<Object>)selectList("rbs.usr.main.mainMapper.boardHashMap", getLangParam(param));
        return selectMapList(list, "MENU_IDX", 1);
    }

	public Map<Object, List<Object>> getContentsHashMap(Map<String, Object> param){
		List<Object> list = (List<Object>)selectList("rbs.usr.main.mainMapper.contentsHashMap", getLangParam(param));
        return selectMapList(list, "MENU_IDX", 1);
    }
	
	public DataMap getMyCurriculum(Map<String, Object> param){
		return (DataMap)selectOne("mart.main.mainDataOracleMapper.selectMyCurriculum", param);
    }

	public DataMap getMyInfomation(Map<String, Object> param){
		return (DataMap)selectOne("mart.main.mainDataOracleMapper.selectMyInfo", param);
    }	

	public List<Object> getMyGoalCDT(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMyGoalCDT", param);
    }
	
	public List<Object> getTransferMyGoalCDT(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectTransferMyGoalCDT", param);
    }
	
	public DataMap getMyCDT(Map<String, Object> param){
		return (DataMap)selectOne("mart.main.mainDataOracleMapper.selectMyCDT", param);
    }
	
	public List<Object> getMyGPA(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMyGPA", param);
    }
	
	public List<Object> getMyCdtDetail(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMyCdtDetail", param);
	}
	
	public List<Object> getMyScholAmt(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMyScholAmt", param);
	}
	
	public List<Object> getMyMajorReq(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMyMajorReq", param);
	}
	
	public List<Object> getMyMinorReq(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMyMinorReq", param);
	}
	
	public List<Object> getMyCurrMinor(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMyCurrMinor", param);
	}
	
	public List<Object> getTransferMyCurrMinor(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectTransferMyCurrMinor", param);
	}
	
	public List<Object> getAiMajorSubject(List<Map<String, Object>> paramsList){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectAiMajorSubject", paramsList);
	}
	
	public List<Object> getAcademicCalendar(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectAcademicCalendar", param);
    }
	
	public String loginTis(Map<String, Object> param){
		return (String)selectOne("mart.main.mainDataOracleMapper.loginTis", param);
    }

	public LoginVO setUser(Map<String, Object> param) {		
		return (LoginVO)selectOne("mart.main.mainDataOracleMapper.setUser", param);
	}

	public List<Object> getEmploymentRate() {
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectEmploymentRate");
	}
	

}