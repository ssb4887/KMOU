package rbs.modules.member.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.MartAbstractMapper;
import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;


@Repository("memberInfoRbsMapper")
public class MemberInfoRbsMapper extends RbsAbstractMapper{
	
	public List<Object> getMyHashtag(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.member.memberInfoRbsMapper.selectMyHashtag", param);
    }
	
	public int hashtagInsert(Map<String, Object> param){
        return super.insert("rbs.modules.member.memberInfoRbsMapper.insertHashtag", param);
    }
	
	public int hashtagDelete(Map<String, Object> param){
        return super.update("rbs.modules.member.memberInfoRbsMapper.updateHashtag", param);
    }



}