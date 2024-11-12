package rbs.modules.contact.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import rbs.egovframework.mapper.RbsAbstractMapper;

/**
 * 접속정보에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("contactMapper")
public class ContactMapper extends RbsAbstractMapper{

	public List<Object> getBackupYearList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.contact.contactMapper.backupYearList", param);
    }
	public int getSiteTotalSum(Map<String, Object> param){
        return (Integer)selectOne("rbs.modules.contact.contactMapper.siteTotalSum", param);
    }
	public List<Object> getSiteList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.contact.contactMapper.siteList", param);
    }
	public int getPageTotalSum(Map<String, Object> param){
        return (Integer)selectOne("rbs.modules.contact.contactMapper.pageTotalSum", param);
    }
	public List<Object> getPageList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.contact.contactMapper.pageList", param);
    }
	
	public int getSitePageMaxCount(Map<String, Object> param){
        return (Integer)selectOne("rbs.modules.contact.contactMapper.sitePageMaxCount", param);
    }
	public DataMap getSitePageTotalView(Map<String, Object> param){
        return (DataMap)selectOne("rbs.modules.contact.contactMapper.sitePageTotalView", param);
    }
	public List<Object> getSitePageList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.contact.contactMapper.sitePageList", param);
    }
	

	public int getMenuTotalSum(Map<String, Object> param){
        return (Integer)selectOne("rbs.modules.contact.contactMapper.menuTotalSum", param);
    }
	public Map<Object, Object> getMenuMap(Map<String, Object> param){
        return (Map<Object, Object>)selectMap("rbs.modules.contact.contactMapper.menuList", param, "GUBUN_DATA");
    }
	
	public int getUserTotalCount(Map<String, Object> param){
        return (Integer)selectOne("rbs.modules.contact.contactMapper.userCount", param);
    }

	public List<Object> getUserList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.contact.contactMapper.userList", param);
    }
	
    public int insert(Map<String, Object> param){
    	int result = 0;
		result = super.update("rbs.modules.contact.contactMapper.insert", param);
        return result;
    }

	/**
	 * 통합관리 시스템 접속로그
	 * @param param
	 * @return
	 */
    public int insertMng(Map<String, Object> param){
    	int result = 0;
		result = super.update("rbs.modules.contact.contactMapper.insertMng", param);
        return result;
    }
    
    /**
     * 접속자 메뉴로그정보
     * @param param
     * @return
     */
    public int insertMl(Map<String, Object> param){
    	int result = 0;
		result = super.update("rbs.modules.contact.contactMapper.insertMl", param);
        return result;
    }
	
    public int delete(Map<String, Object> param){
    	int result = 0;
		result = super.update("rbs.modules.contact.contactMapper.delete", param);
        return result;
    }
	
    public int deleteAll(){
    	int result = 0;
		result = super.update("rbs.modules.contact.contactMapper.deleteAll");
        return result;
    }
	
    /**
     * 등록된 정보를 데이터베이스에서 읽어와 화면에 출력
     * @param param 검색조건
     * @return List<Object> 목록정보
     */
	public List<Object> getMLList(Map<String, Object> param){
        return (List<Object>)selectList("rbs.modules.contact.contactMapper.selectMlList", param);
    }
	
    /**
     * 총 갯수를 조회한다.
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getMLTotalCount(Map<String, Object> param){
        return (Integer)selectOne("rbs.modules.contact.contactMapper.selectMlCount", param);
    }
}