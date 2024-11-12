package rbs.modules.sbjt.service.implOra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DataMap;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.modules.sbjt.mapper.SbjtOraMapper;
import rbs.modules.sbjt.serviceOra.SbjtServiceOra;

/**
 * 기관 정보 관리에 관한 인터페이스 클래스를 정의한다.
 * @author user
 */
@Service("sbjtServiceOra")
public class SbjtServiceImplOra extends EgovAbstractServiceImpl implements SbjtServiceOra {
	private static final Logger logger = LoggerFactory.getLogger(SbjtServiceImplOra.class);
	
	@Value("${Globals.locale.lang.use}")
	protected int useLang; // 언어 사용 여부
	
	@Resource(name="sbjtOraMapper")
	private SbjtOraMapper sbjtOraDAO;
	
	
	/**
	 * 교과목 최초 조회
	 * @return
	 */
	@Override
	public List<Object> getInitSbjtList(Map<String, Object> param) throws Exception {
		return sbjtOraDAO.getInitSbjtList(param);
	}
	
	
	@Override
	public DataMap getInitSbjtListCount(Map<String, Object> param) throws Exception {
		return sbjtOraDAO.getInitSbjtListCount(param);
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
		DataMap viewDAO = sbjtOraDAO.getView(param);
		return viewDAO;
	}
	
	/**
	 * 상세조회 핵심역량
	 * @author 석수빈
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getCoreView(Map<String, Object> param) throws Exception {
		DataMap viewDAO = sbjtOraDAO.getCoreView(param);
		return viewDAO;
	}
	
	/**
	 * 상세조회 전공능력
	 * @author 석수빈
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getAbiView(Map<String, Object> param) throws Exception {
		DataMap viewDAO = sbjtOraDAO.getAbiView(param);
		return viewDAO;
	}

	/**
	 * 주관대학 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getCollegeList() {
		return sbjtOraDAO.getCollegeList();
	}
	
	/**
	 * 주관 대학 - 학부(과) 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDepartList(Map<String, Object> param) {
		return sbjtOraDAO.getDepartList(param);
	}
	
	/**
	 * 주관 대학 - 학부(과) - 전공 목록
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getMajorList(Map<String, Object> param) {
		return sbjtOraDAO.getMajorList(param);
	}
	
	/**
	 * 개설과목
	 * @param year, smt, subjectCD
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getCoursesOffered(Map<String, Object> param) {
		return sbjtOraDAO.getCoursesOffered(param);
	}
	
	/**
	 * 개설강좌 목록(메인페이지 장바구니용) 
	 * @param empNo
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getClassList(Map<String, Object> param) {
		return sbjtOraDAO.getClassList(param);
	}
	
	/**
	 * 교수 강의과목 목록
	 * @param empNo
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getLectureList(Map<String, Object> param) {
		return sbjtOraDAO.getLectureList(param);
	}
	
	/**
	 * 교수 강의과목 총수
	 * @param empNo
	 * @param param
	 * @return
	 */
    @Override
	public int getLectureCount(Map<String, Object> param) {
    	return sbjtOraDAO.getLectureCount(param);
    }
    
	/**
	 * 개설강좌 상세조회
	 * @author 석수빈
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getLectureView(Map<String, Object> param) throws Exception {
		DataMap viewDAO = sbjtOraDAO.getLectureView(param);
		return viewDAO;
	}
	

	/**
	 * 핵심역량(서치API집어넣는용)
	 * @author 석수빈
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getCoreAbi(Map<String, String> param) throws Exception {
		DataMap viewDAO = sbjtOraDAO.getCoreAbi(param);
		return viewDAO;
	}
	
	/**
	 * 개설강좌 전공능력/핵심역량
	 * @author 석수빈
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getLectureCore(Map<String, Object> param) throws Exception {
		return sbjtOraDAO.getLectureCore(param);
	}
	
	/**
	 * 개설강좌 전공능력/핵심역량
	 * @author 석수빈
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getLectureAbi(Map<String, Object> param) throws Exception {
		return sbjtOraDAO.getLectureAbi(param);
	}
	
	/**
	 * 개설강좌 강의계획서
	 * @author 석수빈
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getPlanView(Map<String, Object> param) throws Exception {
		DataMap viewDAO = sbjtOraDAO.getPlanView(param);
		return viewDAO;
	}
	 
    /**
	 * 개설강좌 강의계획서(핵심역량)
	 * @param empNo
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getCoreList(Map<String, Object> param) {
		return sbjtOraDAO.getCoreList(param);
	}
	
	/**
	 * 개설강좌 강의계획서(전공능력)
	 * @param empNo
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getAbiList(Map<String, Object> param) {
		return sbjtOraDAO.getAbiList(param);
	}
	
	/**
	 * 개설강좌 강의계획서(교재 목록 수)
	 * @param empNo
	 * @param param
	 * @return
	 */
    @Override
	public int getBookCount(Map<String, Object> param) {
    	return sbjtOraDAO.getBookCount(param);
    }

	/**
	 * 개설강좌 강의계획서(교재 목록)
	 * @param empNo
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getBookList(Map<String, Object> param) {
		return sbjtOraDAO.getBookList(param);
	}
	
	/**
	 * 개설강좌 강의계획서(산학연연계)
	 * @param empNo
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getLinkList(Map<String, Object> param) {
		return sbjtOraDAO.getLinkList(param);
	}
	
	/**
	 * 개설강좌 강의계획서(산학연연계 수)
	 * @param empNo
	 * @param param
	 * @return
	 */
    @Override
	public int getLinkCount(Map<String, Object> param) {
    	return sbjtOraDAO.getLinkCount(param);
    }
    
	/**
	 * 개설강좌 강의계획서(주차별 수업계획)
	 * @param empNo
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getWeekList(Map<String, Object> param) {
		return sbjtOraDAO.getWeekList(param);
	}
	
	/**
	 * 강의평가 목록
	 * @param empNo
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getEvalList(Map<String, Object> param) {
		return sbjtOraDAO.getEvalList(param);
	}
	
	/**
	 * 강의평가 총수
	 * @param empNo
	 * @param param
	 * @return
	 */
    @Override
	public int getEvalCount(Map<String, Object> param) {
    	return sbjtOraDAO.getEvalCount(param);
    }

    /**
	 * 교과목 - 수강생통계
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getTotalStudent(Map<String, Object> param) {
		return sbjtOraDAO.getTotalStudent(param);
	}
    

	/**
	 * 교과목 상세-직업/직무
	 * @param docId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getJobCd(String docId) throws Exception {
		return sbjtOraDAO.getJobCd(docId);
	}

	/**
	 * 교과목 상세-해시태그
	 * @param docId
	 * @return
	 */
	@Override
	public Map<String, Object> getPlanInfo(String docId) throws Exception {
		return sbjtOraDAO.getPlanInfo(docId);
	}
	
	/**
	 * 나노디그리 & 매트릭스 상단 정보
	 * @param param
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getTrackList(Map<String, Object> param) throws Exception {
		return sbjtOraDAO.getTrackList(param);
	}
	
	/**
	 * 개설 강좌-강의실 정보
	 * @param docId
	 * @return
	 */
	@Override
	public List<Object> getRoomInfo(List<Object> arr) throws Exception {
		// 시간표에서 강의실 정보 얻어오기
		List<Map<String, Object>> ltTimeList = sbjtOraDAO.getLessonTime(arr);
		
		logger.debug("ltTimeList===11111111111111====>" + ltTimeList);
		
		if (ltTimeList.isEmpty()) {
			List<Object> getLessonRoom = new ArrayList<>();
			
			return getLessonRoom;
		}
		
		return sbjtOraDAO.getLessonRoom(ltTimeList);
	}
	
	/**
	 * 개설 강좌-강의계획서
	 * @param docId
	 * @return
	 */
	@Override
	public Map<String, Object> getLessonPlan(String docId) throws Exception {
		// 강의계획서 정보
		Map<String, Object> planInfoMap = sbjtOraDAO.getPlanInfo(docId);
		// 강의계획서 - 수업 진행 방법
		Map<String, Object> planProgList = sbjtOraDAO.getPlanProg(docId);
		// 강의계획서 - 기자재 활용
		Map<String, Object> planAprtList = sbjtOraDAO.getPlanAprt(docId);
		// 강의계획서 - 교제정보
		List<Map<String, Object>> planBookList = sbjtOraDAO.getPlanBook(docId);
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
		List<Object> planWeekist = sbjtOraDAO.getPlanWeek(docId);
		// 강의계획서 - 과제
		List<Object> planPrjtList = sbjtOraDAO.getPlanPrjt(docId);
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
			
		return sbjtOraDAO.getStatList(colgCdList);
	}
}