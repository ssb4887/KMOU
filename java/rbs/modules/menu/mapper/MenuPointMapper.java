package rbs.modules.menu.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import rbs.egovframework.mapper.RbsAbstractMapper;

/**
 * 접속정보에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("menuPointMapper")
public class MenuPointMapper extends RbsAbstractMapper{
	
	public DataMap getTotalView(Map<String, Object> param){
        return (DataMap)selectOne("rbs.modules.menu.menuPointMapper.menuPointTotalView", param);
    }
	public Map<Object, Object> getMenuPointMap(Map<String, Object> param){
        return (Map<Object, Object>)selectMap("rbs.modules.menu.menuPointMapper.menuPointList", param, "GUBUN_DATA");
    }

	public int getPointPTotalCount(Map<String, Object> param){
        return (Integer)selectOne("rbs.modules.menu.menuPointMapper.pointPTotalCount", param);
    }
	public List<Object> getPointPList(String localeLang, Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.menu.menuPointMapper.pointPList", getLangParam(localeLang, param));
	}

	public int getPointMTotalCount(Map<String, Object> param){
        return (Integer)selectOne("rbs.modules.menu.menuPointMapper.pointMTotalCount", param);
    }
	public List<Object> getPointMList(Map<String, Object> param){
		return (List<Object>)selectList("rbs.modules.menu.menuPointMapper.pointMList", param);
	}

    /**
     * 게시물 key 조회한다.
     * @return int key
     */
    public int getNextId(Map<String, Object> param) {
        return (Integer)selectOne("rbs.modules.menu.menuPointMapper.nextId", param);
    }
	
    public int insert(Map<String, Object> param){
    	int result = 0;
		result = super.update("rbs.modules.menu.menuPointMapper.insert", param);
        return result;
    }
}