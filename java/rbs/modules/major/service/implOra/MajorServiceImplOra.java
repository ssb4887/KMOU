package rbs.modules.major.service.implOra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.util.ClientUtil;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.modules.major.mapper.MajorMapper;
import rbs.modules.major.mapper.MajorOraMapper;
import rbs.modules.major.serviceOra.MajorServiceOra;

/**
 * 기관 정보 관리에 관한 인터페이스 클래스를 정의한다.
 * @author user
 */
@Service("majorServiceOra")
public class MajorServiceImplOra extends EgovAbstractServiceImpl implements MajorServiceOra {
	private static final Logger logger = LoggerFactory.getLogger(MajorServiceImplOra.class);
	
	@Value("${Globals.locale.lang.use}")
	protected int useLang; // 언어 사용 여부
	
	@Resource(name="majorOraMapper")
	private MajorOraMapper majorOraDAO;
	
	@Resource(name="majorMapper")
	private MajorMapper majorDAO;
	
	
	/**
	 * 추가전공 목록 조회
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getNewMajorList() throws Exception {
		return majorOraDAO.getNewMajorList();
	}
	
	/**
	 * 상세조회
	 * @author 석수빈
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getView(Map<String, Object> param) throws Exception {
		DataMap viewDAO = majorDAO.getView(param);
		return viewDAO;
	}

	/**
	 * 교과목 상세-직업/직무
	 * @param docId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getJobCd(String docId) throws Exception {
		return majorOraDAO.getJobCd(docId);
	}

	/**
	 * 교과목 상세-해시태그
	 * @param docId
	 * @return
	 */
	@Override
	public Map<String, Object> getPlanInfo(String docId) throws Exception {
		return majorOraDAO.getPlanInfo(docId);
	}
	
	/**
	 * 나노디그리 & 매트릭스 상단 정보
	 * @param param
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getTrackList(Map<String, Object> param) throws Exception {
		return majorOraDAO.getTrackList(param);
	}
	
	/**
	 * 개설 강좌-강의실 정보
	 * @param docId
	 * @return
	 */
	@Override
	public List<Object> getRoomInfo(List<Object> arr) throws Exception {
		// 시간표에서 강의실 정보 얻어오기
		List<Map<String, Object>> ltTimeList = majorOraDAO.getLessonTime(arr);
		
		logger.debug("ltTimeList===11111111111111====>" + ltTimeList);
		
		if (ltTimeList.isEmpty()) {
			List<Object> getLessonRoom = new ArrayList<>();
			
			return getLessonRoom;
		}
		
		return majorOraDAO.getLessonRoom(ltTimeList);
	}
	
	/**
	 * 개설 강좌-강의계획서
	 * @param docId
	 * @return
	 */
	@Override
	public Map<String, Object> getLessonPlan(String docId) throws Exception {
		// 강의계획서 정보
		Map<String, Object> planInfoMap = majorOraDAO.getPlanInfo(docId);
		// 강의계획서 - 수업 진행 방법
		Map<String, Object> planProgList = majorOraDAO.getPlanProg(docId);
		// 강의계획서 - 기자재 활용
		Map<String, Object> planAprtList = majorOraDAO.getPlanAprt(docId);
		// 강의계획서 - 교제정보
		List<Map<String, Object>> planBookList = majorOraDAO.getPlanBook(docId);
		List<Map<String, Object>> mainBookList = new ArrayList<>(); // 주교제
		List<Map<String, Object>> referBookList = new ArrayList<>(); // 참고 문헌
		List<Map<String, Object>> etcBookList = new ArrayList<>(); // 기타 서적
		
		for (Map<String, Object> map : planBookList) {
			if (map.get("TEACHM_GBN").equals("01")) {
				mainBookList.add(map);
			} else if (map.get("TEACHM_GBN").equals("02")) {
				referBookList.add(map);
			} else {
				etcBookList.add(map);
			}
		}
		
		logger.debug("mainBookList==========>" + mainBookList);
		logger.debug("referBookList==========>" + referBookList);
		logger.debug("etcBookList==========>" + etcBookList);
		
		// 강의계획서 - 주별 세부 수업 계획
		List<Object> planWeekist = majorOraDAO.getPlanWeek(docId);
		// 강의계획서 - 과제
		List<Object> planPrjtList = majorOraDAO.getPlanPrjt(docId);
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("planInfoMap", planInfoMap);
		returnMap.put("planProgList", planProgList);
		returnMap.put("planAprtList", planAprtList);
		returnMap.put("mainBookList", mainBookList);
		returnMap.put("referBookList", referBookList);
		returnMap.put("etcBookList", etcBookList);
		returnMap.put("planWeekist", planWeekist);
		returnMap.put("planPrjtList", planPrjtList);
		
		return returnMap;
	}
	
	/**
	 * 수강생 통계
	 * @param stuno
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getStatList(Map<String, Object> param) throws Exception {	
		ArrayList<String> getColgCdlist = (ArrayList<String>) param.get("getColgCdlist");
		// 리스트에서 모든 코드 값 가져오기 위해 리스트를 중복이 없는 HashSet으로 변경하여 중복 제거한 모든 코드 값 가져온다(마치 group by)
		Set<String> set = new HashSet<String>(getColgCdlist);
		Map<String, Integer> sortMap = new HashMap<String, Integer>();
		
		for (String str : set) {
			sortMap.put(str, Collections.frequency(getColgCdlist, str));
		}
		
		// 자바 맵 벨루값으로 정렬
		List<String> keySet = new ArrayList<>(sortMap.keySet());
		// 정렬
		Collections.sort(keySet, (o1, o2) -> (sortMap.get(o2).compareTo(sortMap.get(o1))));
		List<Map<String, Object>> colgCdList = new ArrayList<>();
		int idx = 0;
		
		for (String key : keySet) {
			Map<String,Object> map = new HashMap<>();
			map.put("colgCd", key);
			map.put("colgCdCnt", sortMap.get(key));
			colgCdList.add(map);
			idx++;
			
			if (param.get("statRange").equals("top5") && idx > 5) {
				break;
			}
		}
			
		return majorOraDAO.getStatList(colgCdList);
	}
}