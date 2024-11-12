package rbs.modules.member.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.MartAbstractMapper;
import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;


@Repository("memberInfoMapper")
public class MemberInfoMapper extends MartAbstractMapper{
	
	public List<Object> getMyCurriculum(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMyCurriculum", param);
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
	
	public List<Object> getMyReqCDT(Map<String, Object> param) {
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMyReqCDT", param);
	}
	
	public List<Object> getMyCumCDT(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMyCumCDT", param);
	}
	
	public List<Object> getMyMajorReq(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMyMajorReq", param);
	}
	
	public int getChkMinorReq(Map<String, Object> param) {
		return (Integer)selectOne("mart.main.mainDataOracleMapper.selectChkMinorReq", param);
	}
	public List<Object> getMyMinorReq(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMyMinorReq", param);
	}
	
	public List<Object> getMyGradReq(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMyGradReq", param);
	}

	public List<Object> getMySubjectCDT(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMySubjectCDT", param);
	}
	
	public List<Object> getMyRecordHistory(Map<String, Object> param){
		return (List<Object>)selectList("mart.main.mainDataOracleMapper.selectMyRecordHistory", param);
	}




}