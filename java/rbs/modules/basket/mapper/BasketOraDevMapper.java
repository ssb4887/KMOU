package rbs.modules.basket.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.SukangLoginVO;
import rbs.egovframework.mapper.MartAbstractDevMapper;


@Repository("basketOraDevMapper")
public class BasketOraDevMapper extends MartAbstractDevMapper{

	public int updateOrder(Map<String, Object> param) {
		return update("mart.modules.basket.basketOraDevMapper.updateOrder", param);		
	}
	
	public List<Object> getPreApplSbjt(Map<String, Object> param) {
		return selectList("mart.modules.basket.basketOraDevMapper.getPreApplSbjt", param);
	}
	
	public List<HashMap<String, Object>> getPreApplDate() {
		return selectList("mart.modules.basket.basketOraDevMapper.getPreApplYN");
	}

	public void sukangLogin(Map<String, Object> param) {
		update("mart.modules.basket.basketOraDevMapper.sukangLogin",param);
	}

	public void sukangSin(Map<String, Object> param) {
		update("mart.modules.basket.basketOraDevMapper.sukangSin",param);	
	}

	public void sukangDel(Map<String, Object> param) {
		update("mart.modules.basket.basketOraDevMapper.sukangDel",param);		
	}


}