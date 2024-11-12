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
@Repository("usrMainRbsMapper")
public class MainRbsMapper extends RbsAbstractMapper{

	
	/**
	 * 정보제공동의 여부 체크
	 * @param param
	 * @return
	 */
	public String getIsPrivacyPolicy(Map<String, Object> param) {
		return (String)selectOne("rbs.usr.main.mainMapper.getIsPrivacyAgree", param);
	}	
	
	/**
	 * 정보제공동의
	 * @param param
	 * @return
	 */
	public int insertPrivacyPolicy(Map<String, Object> param) {
		return insert("rbs.usr.main.mainMapper.insertPrivacyPolicy", param);
	}
	

}