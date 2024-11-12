package rbs.modules.basket.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.AcademicAbstractMapper;
import rbs.egovframework.mapper.RbsAbstractMapper;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.util.StringUtil;


@Repository("basketMapper")
public class BasketMapper extends RbsAbstractMapper{
	
	/**
     * 장바구니 목록(교과목)
     */
	public List<Object> getSbjtBasket(Map<String, Object> param) {
		return super.selectList("rbs.modules.basket.basketMapper.getSbjtBasket", param);
	}
	
	/**
     * 장바구니 목록(마이페이지)
     */
	public List<Object> getMyBasketList(Map<String, Object> param) {
		return super.selectList("rbs.modules.basket.basketMapper.getMyBasketList", param);
	}
	
	/**
     * 장바구니 총 수
     * @param param 검색조건
     * @return int 총갯수
     */
    public int getBasketCount(Map<String, Object> param) {
    	return (Integer)selectOne("rbs.modules.basket.basketMapper.selectBasketCount", param);
    }
    
	
	 /**
     * 장바구니 등록
     */
    public int insert(Map<String, Object> param){
    	return super.insert("rbs.modules.basket.basketMapper.insert", param);
    }
    
    /**
     * 장바구니 삭제
     */
    public int cdelete(Map<String, Object> param){
    	return super.delete("rbs.modules.basket.basketMapper.cdelete", param);
    }
        
}