package rbs.modules.basket.service.impl;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.egovframework.SukangLoginVO;
import rbs.modules.basket.mapper.BasketMapper;
import rbs.modules.basket.mapper.BasketOraDevMapper;
import rbs.modules.basket.mapper.BasketOraMapper;
import rbs.modules.basket.service.BasketService;


/**
 * 장바구니 serviceImple
 * @author 이동근
 */
@Service("basketService")
public class BasketServiceImpl extends EgovAbstractServiceImpl implements BasketService{

	@Resource(name="basketMapper")
	private BasketMapper basketDAO;
	
	@Resource(name="basketOraMapper")
	private BasketOraMapper basketOraDAO;
	
//	개발
	@Resource(name="basketOraDevMapper")
	private BasketOraDevMapper basketOraDevDAO;
	
	@Override
	public List<Object> getSbjtBasket(Map<String, Object> param) {
		return basketDAO.getSbjtBasket(param);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Object> getBasketList(Map<String, Object> param) {
		List basketList = basketDAO.getMyBasketList(param);
		List<Map<String, Object>> result = null;
		List cartList = new ArrayList<Object>();
		if(basketList != null) {
			int basketListSize = basketList.size();
			for(int i = 0 ; i < basketListSize ; i ++) {
				
				Map<String, Object> basketParam = (Map<String, Object>)basketList.get(i);
			
				cartList.addAll(basketOraDAO.getMyBasketView(basketParam)); 
				
			}
		}
		return cartList;
	}
	
	/**
	 * 현재 학기 정보
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getCurInfo() {
		return basketOraDAO.getCurInfo();
	}
	
	/**
	 * 장바구니 목록 총 수
	 * @param empNo
	 * @param param
	 * @return
	 */
    @Override
	public int getBasketCount(Map<String, Object> param) {
    	return basketDAO.getBasketCount(param);
    }

	@Override
	public int insertBsk(String memberId, String rawBody, String regiIp) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String loginMemberId = null;
		
		if(loginVO != null) {
			loginMemberId = loginVO.getMemberId();
		}
		
		JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
		String year = reqJsonObj.get("year").toString();
		String smt = reqJsonObj.get("smt").toString();
		String subjectCd = reqJsonObj.get("subjectCd").toString();
		String divcls = reqJsonObj.get("divcls").toString();
		String deptCd = reqJsonObj.get("deptCd").toString();
		String sbjt = reqJsonObj.get("sbjt").toString();
		String empNo = reqJsonObj.get("empNo").toString();
		
		dataList.add(new DTForm("STD_NO", memberId));
		dataList.add(new DTForm("YEAR", year));
		dataList.add(new DTForm("SMT", smt));
		dataList.add(new DTForm("SUBJECT_CD", subjectCd));
		dataList.add(new DTForm("DEPT_CD", deptCd));
		dataList.add(new DTForm("DIVCLS", divcls));
		dataList.add(new DTForm("EMP_NO", empNo));
		dataList.add(new DTForm("SBJT_NM_KOR", sbjt));
		
    	dataList.add(new DTForm("REGI_ID", loginMemberId));
    	dataList.add(new DTForm("REGI_IP", regiIp));
    	
    	param.put("dataList", dataList);
		
    	int result = basketDAO.insert(param);
    	
		return result;
	}

	@Override
	public int deleteBsk(String memberId, String rawBody) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색항목
		
		JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
		String year = reqJsonObj.get("year").toString();
		String smt = reqJsonObj.get("smt").toString();
		String subjectCd = reqJsonObj.get("subjectCd").toString();
		String divcls = reqJsonObj.get("divcls").toString();
		String deptCd = reqJsonObj.get("deptCd").toString();
		String empNo = reqJsonObj.get("empNo").toString();
		
		searchList.add(new DTForm("STD_NO", memberId));
		searchList.add(new DTForm("YEAR", year));
		searchList.add(new DTForm("SMT", smt));
		searchList.add(new DTForm("SUBJECT_CD", subjectCd));
		searchList.add(new DTForm("DEPT_CD", deptCd));
		searchList.add(new DTForm("DIVCLS", divcls));
		searchList.add(new DTForm("EMP_NO", empNo));
		
		param.put("searchList", searchList);
		
		int result = basketDAO.cdelete(param);
		
		return result;
	}

	@Override
	public int deleteAllBsk(String memberId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색항목
		
		searchList.add(new DTForm("STD_NO", memberId));
		
		param.put("searchList", searchList);
		
		int result = basketDAO.cdelete(param);
		
		return result;
	}

	@Override
	public int deleteSelectedBsk(String memberId, String rawBody) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색항목
		int result = 0;
		
		JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
		String[] deleteArray = reqJsonObj.get("deleteList").toString().split(",");
		
		for(int i = 0; i < deleteArray.length; i++) {
			String[] deleteInfo = deleteArray[i].split("-");
			String year = deleteInfo[0];
			String smt = deleteInfo[1];
			String subjectCd = deleteInfo[2];
			String deptCd = deleteInfo[3];
			String divcls = deleteInfo[4];
			String empNo = deleteInfo[5];
			
			searchList.add(new DTForm("STD_NO", memberId));
			searchList.add(new DTForm("YEAR", year));
			searchList.add(new DTForm("SMT", smt));
			searchList.add(new DTForm("SUBJECT_CD", subjectCd));
			searchList.add(new DTForm("DEPT_CD", deptCd));
			searchList.add(new DTForm("DIVCLS", divcls));
			searchList.add(new DTForm("EMP_NO", empNo));
			
			param.put("searchList", searchList);
			
			result += basketDAO.cdelete(param);
			
			searchList.clear();
		}
		
		return result;
	}

	@Override
	public List<Object> getPreApplSbjt(Map<String, Object> param) throws Exception {
		return basketOraDevDAO.getPreApplSbjt(param); 
	}


	@Override
	public int updateOrder(JSONObject reqJsonObj, String memberId) throws Exception {
		
		JSONArray reqJsonArray = reqJsonObj.getJSONArray("changedItems");			
		
	    for (int i = 0; i < reqJsonArray.size(); i++) {
	    	Map<String, Object> param = new HashMap<String, Object>();
	        JSONObject item = reqJsonArray.getJSONObject(i);
	        param.put("STUDENT_NO", memberId);
	        param.put("ORD", item.getString("ORD"));
	        param.put("YEAR", item.getString("YEAR"));
	        param.put("SMT", item.getString("SMT"));
	        param.put("SUBJECT_CD", item.getString("SUBJECT_CD"));
	        param.put("DIVCLS", item.getString("DIVCLS"));
	        basketOraDevDAO.updateOrder(param);
	    }
		return 0;
	}
	

	@Override
	public void sukangLogin(ParamForm parameterMap, HttpServletRequest request) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();	
		HttpSession session = request.getSession(true);
		SukangLoginVO sukangLoginVO;		
		
		//예비수강신청 기간인지 체크
		List<HashMap<String, Object>> preApplResult = basketOraDevDAO.getPreApplDate();
		HashMap<String, Object> applResultMap = preApplResult.get(0);
		
		String preApplYn = (String)applResultMap.get("PRE_APPL_YN"); 
		String applDttm  = (String)applResultMap.get("APPL_DTTM"); 
		
		
		if(preApplYn.equals("Y")) {
			sukangLoginVO = new SukangLoginVO();
			
			//수강신청시스템 로그인
			param.put("vs_id", parameterMap.getString("mbrId")); // 사용자 ID (학번)
			param.put("vs_pass", parameterMap.getString("mbrPwd")); // 비밀번호
			param.put("vs_uid", parameterMap.getString("mbrId")); // 사용자 ID (학번)
			param.put("vs_g_ip", request.getRemoteAddr()); // 사용자의 IP 주소
			param.put("vs_g_prg_id", "AI_SYSTEM"); // 프로그램 ID (AI 시스템)
			param.put("vs_pgm_gb", "WEB"); // 프로그램 구분 (웹)
			param.put("vs_precourse", "Y"); // 예비 수강신청 여부 (Y: 예비 수강신청)
			
			param.put("O_CURSOR", new HashMap<String, Object>());
			
			basketOraDevDAO.sukangLogin(param);
			
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> resultList = (List<Map<String, Object>>) param.get("O_CURSOR");
			
			sukangLoginVO = (SukangLoginVO)resultList.get(0);
			
			//로그인 시도시간 set
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY년MM월dd일 HH시mm분");
			String currentTime = sdf.format(new Date());
			sukangLoginVO.setTryTime(currentTime);
			
			//예비수강신청일시 set
			sukangLoginVO.setPreApplDttm(applDttm);
			
			 
			session.setAttribute("sukangLoginVO", sukangLoginVO);			
		}else {						
			sukangLoginVO = new SukangLoginVO();
			
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY년MM월dd일 HH시mm분");
			String currentTime = sdf.format(new Date());
			
			//VO set
			sukangLoginVO.setTryTime(currentTime);
			sukangLoginVO.setErrCode("N");
			sukangLoginVO.setErrMessage("예비수강신청기간이 아닙니다.");
			sukangLoginVO.setPreApplDttm(applDttm);
			
			session.setAttribute("sukangLoginVO", sukangLoginVO);
		}
		
	}
	
	@Override
	public String sukangSin(HttpServletRequest request, JSONObject reqJsonObj) throws Exception {

		
		Map<String, Object> param = new HashMap<String, Object>();
		
		HttpSession session = request.getSession(true); 
		SukangLoginVO sukangLoginVO = (SukangLoginVO)session.getAttribute("sukangLoginVO");
        
		JSONArray selSubjectList = reqJsonObj.getJSONArray("SEL_SUBJECT_LIST");
		
		String resultMessage = "";
		boolean allSuccess = true;
				
		
		for (int i = 0; i < selSubjectList.size(); i++) {
			//로그인하고 받은 데이터 세팅
			param.put("vs_year", sukangLoginVO.getYear()); // 수강신청 년도
			param.put("vs_smt", sukangLoginVO.getSmt()); // 수강신청 학기
			param.put("vs_smt_nm", sukangLoginVO.getSmtNm()); // 학기명
			param.put("vs_sinbun", sukangLoginVO.getSinbun()); // 신분구분
			param.put("vs_time_gb", sukangLoginVO.getTimeGb()); // 수강신청기간구분
			param.put("vs_student_no", sukangLoginVO.getStudentNo()); // 학번
			param.put("vs_nm", sukangLoginVO.getNm()); // 성명
			param.put("vs_coll_cd", sukangLoginVO.getCollCd()); // 대학코드
			param.put("vs_coll_nm", sukangLoginVO.getCollNm()); // 대학명
			param.put("vs_dept_cd", sukangLoginVO.getDeptCd()); // 학부(과)코드
			param.put("vs_dept_nm", sukangLoginVO.getDeptNm()); // 학부(과)명
			param.put("vs_major_cd", sukangLoginVO.getMajorCd()); // 전공코드
			param.put("vs_major_nm", sukangLoginVO.getMajorNm()); // 전공명
			param.put("vs_grade", sukangLoginVO.getGrade()); // 학년/이수학기
			param.put("vs_suc_grade", sukangLoginVO.getSucGrade()); // 학년/이수학기
			param.put("vs_ent_year", sukangLoginVO.getEntYear()); // 입학년도
			param.put("vs_grade_code", sukangLoginVO.getGradeCode()); // 학년제구분코드
			param.put("vs_hakjuk_nm", sukangLoginVO.getHakjukNm()); // 학적상태
			param.put("vs_addr", sukangLoginVO.getAddr()); // 주소
			param.put("vd_max_cdt", sukangLoginVO.getMaxCdt()); // 최대학점
			param.put("vd_cdt_sum", sukangLoginVO.getCdtSum()); // 신청학점
			param.put("vd_cdt_teach_sum", sukangLoginVO.getCdtTeachSum()); // 교직학점
			param.put("vs_pyn_yn", sukangLoginVO.getPynYn()); // 편입생 구분
			param.put("vs_manager_yn", sukangLoginVO.getManagerYn()); // 관리자구분
			param.put("vs_login_dt", sukangLoginVO.getLoginDt()); // 로그인일자
			param.put("vs_jumin_no2", sukangLoginVO.getJuminNo2()); // 주민번호뒷자리
			param.put("vd_smt_cdt_sum", sukangLoginVO.getSmtCdtSum()); // 대학원과목 신청 학기계
			param.put("vd_tot_cdt_sum", sukangLoginVO.getTotCdtSum()); // 대학원과목 신청 총계
			param.put("vs_con_gb", sukangLoginVO.getConGb()); // 연결학과 여부
			param.put("vs_smajor_cd", sukangLoginVO.getSmajorCd()); // 복수전공 코드
			param.put("vs_minor_cd", sukangLoginVO.getMinorCd()); // 부전공 코드
			param.put("vs_fuse_cd", sukangLoginVO.getFuseCd()); // 융합전공 코드
			param.put("vs_smajor_nm", sukangLoginVO.getSmajorNm()); // 복수전공명 (학부), 선수과목학점 (대학원)
			param.put("vs_minor_nm", sukangLoginVO.getMinorNm()); // 부전공명 (학부), 지도교수 (대학원)
			param.put("vs_isu_smt", sukangLoginVO.getIsuSmt()); // 이수학기 (학부), 이수학기 (대학원)
			param.put("vs_teach_yn", sukangLoginVO.getTeachYn()); // 교직신청 (학부), 석사인정학점 (대학원)
			param.put("vs_deglink_yn", sukangLoginVO.getDeglinkYn()); // 연계과정
			param.put("vs_link_cd", sukangLoginVO.getLinkCd()); // 연계전공 코드
			param.put("vs_fuse_nm", sukangLoginVO.getFuseNm()); // 융합전공 명
			param.put("vs_link_nm", sukangLoginVO.getLinkNm()); // 연계전공 명
			param.put("ls_suc_grade", sukangLoginVO.getStuGrade()); // 학생 학년
			param.put("ls_bokhak_yn", sukangLoginVO.getBokhakYn()); // 복학생 여부

			// 로그인하고 받은 것과 이름은 다르지만 매칭될 데이터 세팅
			param.put("vi_max_cdt", sukangLoginVO.getMaxCdt()); // 최종 수강신청 가능학점
			param.put("vs_try_dept_cd", sukangLoginVO.getDeptCd()); // 예비수강신청 학과
			param.put("vs_try_grade", sukangLoginVO.getGrade()); // 예비수강신청 학년
			param.put("vs_g_id", sukangLoginVO.getStudentNo()); // 학번
	
			//하드코딩할 데이터 세팅
			param.put("vs_precourse", "Y"); // 예비수강신청
			param.put("vs_inwon_over", "Y"); // 인원초과 허용
			param.put("vs_other_dept", "N"); // 타과개설과목 신청 허용
			param.put("vs_cdt_over", "N"); // 가능학점 초과 허용
			param.put("vs_pgm_gb", "WEB"); // JSP/MP 구분
			param.put("vs_coll_not", "N"); // 단대 수강제한 교과목
			param.put("vs_g_prg_id", "AI_SYSTEM"); // 프로그램 ID
	
			//쿼리에서 null로 설정할 데이터
	//		vs_belong_num // 동일교과목 쿼리시 사용
	//		vs_recourse_on // 재수강 버튼 활성
	//		vs_re_year // 재수강 : 년도
	//		vs_re_smt // 재수강 : 학기
	//		vs_re_subject_cd // 재수강 : 과목코드
	//		vs_sess_token // 로그온시 발급된 세션토큰값
	//		vs_rtn_ticket // 화면에서 넘겨주는 매크로 발급키
	//		vs_try_cnt // 매크로 가능횟수 초기값
	//		vs_confirm_yn // 확인/취소창 처리
			
			//자바에서 처박아넣을 데이터 세팅
			param.put("vs_g_ip", request.getRemoteAddr());
							
	        JSONObject subject = selSubjectList.getJSONObject(i);	        
	        //화면에서 받은 데이터 세팅(교과목, 분반)
	        param.put("vs_subject_cd", subject.optString("subjectCd"));
			param.put("vs_divcls", subject.optString("divcls"));
			
			basketOraDevDAO.sukangSin(param);

	        // OUT 파라미터 추출
	        String errCode = (String) param.get("vs_err_code");
	        String msg = (String) param.get("vs_msg");
	        
	        if ("N".equals(errCode)) {
	        	resultMessage += "[" + subject.optString("subjectNm") + "]\n  - " + msg + "\n";
	            allSuccess = false;
	        }else {
	        	resultMessage += "[" + subject.optString("subjectNm") + "]\n  - 정상등록\n";
	        }

	    }
		
	    if (allSuccess) {
	    	resultMessage = "정상적으로 예비수강신청되었습니다.";
	    }
		
		return resultMessage;
	}
	
	@Override
	public String sukangDel(HttpServletRequest request, JSONObject reqJsonObj) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		
		HttpSession session = request.getSession(true); 
		SukangLoginVO sukangLoginVO = (SukangLoginVO)session.getAttribute("sukangLoginVO");
        
		JSONArray selSubjectList = reqJsonObj.getJSONArray("SEL_SUBJECT_LIST");
		
		String resultMessage = "";
		boolean allSuccess = true;
		
		for (int i = 0; i < selSubjectList.size(); i++) {
			//로그인하고 받은 데이터 세팅
			param.put("vs_year", sukangLoginVO.getYear()); // 수강신청 년도
			param.put("vs_smt", sukangLoginVO.getSmt()); // 수강신청 학기
			param.put("vs_sinbun", sukangLoginVO.getSinbun()); // 신분구분
			param.put("vs_student_no", sukangLoginVO.getStudentNo()); // 학번
			param.put("vs_bokhak_yn", sukangLoginVO.getBokhakYn()); // 복학생 구분(Y:복학생,N:재학생)

			// 로그인하고 받은 것과 이름은 다르지만 매칭될 데이터 설정
			param.put("vs_g_id", sukangLoginVO.getStudentNo()); // 학번
	
			//하드코딩할 데이터 세팅
			param.put("vs_manager_yn", "N"); // 관리자구분
			param.put("vs_precourse", "Y"); // 예비수강신청
			param.put("vs_g_prg_id", "AI_SYSTEM");
	
			//자바에서 처박아넣을 데이터 세팅
			param.put("vs_g_ip", request.getRemoteAddr());
							
			//화면에서 받은 데이터 세팅(교과목, 분반)
	        JSONObject subject = selSubjectList.getJSONObject(i);
	        param.put("vs_subject_cd", subject.optString("subjectCd"));
			param.put("vs_divcls", subject.optString("divcls"));
	        
			basketOraDevDAO.sukangDel(param);

	        // OUT 파라미터 추출
	        String errCode = (String) param.get("vs_err_code");
	        String msg = (String) param.get("vs_msg");
	        
	        if ("N".equals(errCode)) {
	        	resultMessage += "[" + subject.optString("subjectNm") + "]\n  - " + msg + "\n";
	            allSuccess = false;
	        }else {
	        	resultMessage += "[" + subject.optString("subjectNm") + "]\n  - 정상삭제\n";
	        }

	    }
		
	    if (allSuccess) {
	    	resultMessage = "정상적으로 삭제되었습니다.";
	    }
		
		return resultMessage;
	}
	
	
	
	
	
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;

    public static String decryptPassword(String encryptedPassword, String secretKeyHex) throws Exception {
        // 16진수 문자열을 바이트 배열로 변환
        byte[] encryptedBytes = hexStringToByteArray(encryptedPassword);
        byte[] secretKeyBytes = hexStringToByteArray(secretKeyHex);
        
        // IV 추출 (처음 12바이트)
        byte[] iv = new byte[GCM_IV_LENGTH];
        System.arraycopy(encryptedBytes, 0, iv, 0, GCM_IV_LENGTH);
        
        // 실제 암호화된 데이터
        byte[] cipherText = new byte[encryptedBytes.length - GCM_IV_LENGTH];
        System.arraycopy(encryptedBytes, GCM_IV_LENGTH, cipherText, 0, cipherText.length);

        // 비밀키 생성
        SecretKey aesKey = new SecretKeySpec(secretKeyBytes, "AES");

        // Cipher 객체 초기화
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, gcmParameterSpec);

        // 복호화
        byte[] decryptedBytes = cipher.doFinal(cipherText);

        // 바이트 배열을 문자열로 변환
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }




}