package rbs.modules.statistics.service.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.modules.statistics.mapper.StatisticsMapper;
import rbs.modules.statistics.mapper.StatisticsOceanMapper;
import rbs.modules.statistics.mapper.StatisticsOraMapper;
import rbs.modules.statistics.service.StatisticsService;

/**
 * 통계모듈에 관한 구현클래스를 정의한다.
 * @author 이동근
 *
 */
@Service("statisticsService")
public class StatisticsServiceImpl extends EgovAbstractServiceImpl implements StatisticsService {


	@Resource(name="statisticsMapper")
	private StatisticsMapper statisticsDAO;
	
	@Resource(name="statisticsOraMapper")
	private StatisticsOraMapper statisticsOraDAO;
	
	@Resource(name="statisticsOceanMapper")
	private StatisticsOceanMapper statisticsOceanDAO;
	
	/**
	 * 검색 건수 추이
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getSearchCountList(Map<String, Object> param) throws Exception {
		return statisticsDAO.getSearchCountList(param);
		
	}
	
	/**
	 * 검색 사용자 추이
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getSearchUserList(Map<String, Object> param) throws Exception {
		return statisticsDAO.getSearchUserList(param);
		
	}

	/**
	 * 검색 사용자 추이(학년별)
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getSearchGradeList(Map<String, Object> param) throws Exception {
		return statisticsDAO.getSearchGradeList(param);
		
	}

	/**
	 * 검색어 통계
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> getSearchKeywordList(Map<String, Object> param) throws Exception {
		return statisticsDAO.getSearchKeywordList(param);
		
	}

	/**
	 * 검색어 통계 count
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getSearchKeywordCount(Map<String, Object> param) throws Exception {
		return statisticsDAO.getSearchKeywordCount(param);
	}

	
	@Override
	public double selectSrchPointAvrg(Map<String, Object> param) {
		return statisticsDAO.selectSrchPointAvrg(param);
	}

	@Override
	public List<Object> selectSrchPointGraph(Map<String, Object> param) {
		return statisticsDAO.selectSrchPointGraph(param);
	}

	@Override
	public List<Object> selectSrchPointTypeGraph(Map<String, Object> param) {
		return statisticsDAO.selectSrchPointTypeGraph(param);
	}

	@Override
	public int selectSrchPointCount(Map<String, Object> param) {
		return statisticsDAO.selectSrchPointCount(param);
	}

	@Override
	public List<Object> selectSrchPointList(Map<String, Object> param) {
		return statisticsDAO.selectSrchPointList(param);
	}
	
	@Override
	public List<Object> selectBmk(Map<String, Object> param) {
		return statisticsDAO.selectBmk(param);
	}

	@Override
	public List<Object> selectProfMstBmk(Map<String, Object> param) {
		List<Object> returnList = new ArrayList<>() ;
		
		List<Map<String, Object>> mariaResult = statisticsDAO.selectProfMstBmk(param);
		

        for (Map<String, Object> data : mariaResult) {
            String docId = data.get("DOC_ID").toString();
            String cnt = data.get("CNT").toString();
            String per = data.get("PER").toString();

            Map<String, Object> oraParam = new HashMap<String, Object>();
            oraParam.put("DOC_ID", docId);
            oraParam.put("CNT", cnt);
            oraParam.put("PER", per);

            Map<String, Object> oraResult = statisticsOraDAO.getProfName(oraParam);

            if (oraResult != null) {
            	oraResult.put("DOC_ID", docId);
            	oraResult.put("CNT", cnt);
            	oraResult.put("PER", per);
                returnList.add(oraResult);
            }
        }
        return returnList;				
	}

	@Override
	public List<Object> selectNonSbjtMstBmk(Map<String, Object> param) {
		List<Object> returnList = new ArrayList<>() ;
		
		List<Map<String, Object>> mariaResult = statisticsDAO.selectNonSbjtMstBmk(param);
		

        for (Map<String, Object> data : mariaResult) {
            String docId = data.get("DOC_ID").toString();
            String cnt = data.get("CNT").toString();
            String per = data.get("PER").toString();

            Map<String, Object> oraParam = new HashMap<String, Object>();
            oraParam.put("DOC_ID", docId);
            oraParam.put("CNT", cnt);
            oraParam.put("PER", per);

            Map<String, Object> oraResult = statisticsOceanDAO.getNonSbjtName(oraParam);

            if (oraResult != null) {
            	oraResult.put("DOC_ID", docId);
            	oraResult.put("CNT", cnt);
            	oraResult.put("PER", per);
                returnList.add(oraResult);
            }
        }
        return returnList;		
		
	}

	@Override
	public List<Object> selectClsMstBmk(Map<String, Object> param) {
		List<Object> returnList = new ArrayList<>() ;
		
		List<Map<String, Object>> mariaResult = statisticsDAO.selectClsMstBmk(param);
		

        for (Map<String, Object> data : mariaResult) {
            String docId = data.get("DOC_ID").toString();
            String cnt = data.get("CNT").toString();
            String per = data.get("PER").toString();

            Map<String, Object> oraParam = new HashMap<String, Object>();
            oraParam.put("DOC_ID", docId);
            oraParam.put("CNT", cnt);
            oraParam.put("PER", per);

            Map<String, Object> oraResult = statisticsOraDAO.getClsName(oraParam);

            if (oraResult != null) {
            	oraResult.put("DOC_ID", docId);
            	oraResult.put("CNT", cnt);
            	oraResult.put("PER", per);
                returnList.add(oraResult);
            }
        }
        return returnList;	
		
	}

	@Override
	public List<Object> selectMajorMstBmk(Map<String, Object> param) {
		List<Object> returnList = new ArrayList<>() ;
		
		List<Map<String, Object>> mariaResult = statisticsDAO.selectMajorMstBmk(param);
		

        for (Map<String, Object> data : mariaResult) {
            String docId = data.get("DOC_ID").toString();
            String cnt = data.get("CNT").toString();
            String per = data.get("PER").toString();

            Map<String, Object> oraParam = new HashMap<String, Object>();
            oraParam.put("DOC_ID", docId);
            oraParam.put("CNT", cnt);
            oraParam.put("PER", per);

            Map<String, Object> oraResult = statisticsOraDAO.getMajorName(oraParam);

            if (oraResult != null) {
            	oraResult.put("DOC_ID", docId);
            	oraResult.put("CNT", cnt);
            	oraResult.put("PER", per);
                returnList.add(oraResult);
            }
        }
        return returnList;	
		
	}
	
	@Override
	public List<Object> selectSbjtMstBmk(Map<String, Object> param) {		
		List<Object> returnList = new ArrayList<>() ;
		
		List<Map<String, Object>> mariaResult = statisticsDAO.selectSbjtMstBmk(param);
		

        for (Map<String, Object> data : mariaResult) {
            String docId = data.get("DOC_ID").toString();
            String cnt = data.get("CNT").toString();
            String per = data.get("PER").toString();

            Map<String, Object> oraParam = new HashMap<String, Object>();
            oraParam.put("DOC_ID", docId);
            oraParam.put("CNT", cnt);
            oraParam.put("PER", per);

            Map<String, Object> oraResult = statisticsOraDAO.getSbjtName(oraParam);

            if (oraResult != null) {
            	oraResult.put("DOC_ID", docId);
            	oraResult.put("CNT", cnt);
            	oraResult.put("PER", per);
                returnList.add(oraResult);
            }
        }
        return returnList;		
	}

	@Override
	public List<Object> selectStudMstBmk(Map<String, Object> param) {		
		return statisticsDAO.selectStudMstBmk(param);		
	}
	
	
	@Override
	public List<Object> selectHashtagCnt(Map<String, Object> param) {
		List<Object> list = statisticsDAO.selectHashtagCnt(param);
		return list;
	}
	
	@Override
	public List<Object> selectHashtagUsr(Map<String, Object> param) {
		List<Object> list = statisticsDAO.selectHashtagUsr(param);
		return list;
	}
	
	@Override
	public List<Object> selectHashtagStat(Map<String, Object> param) {
		List<Object> list = statisticsDAO.selectHashtagStat(param);
		return list;
	}


}