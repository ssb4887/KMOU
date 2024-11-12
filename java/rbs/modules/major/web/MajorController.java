package rbs.modules.major.web;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.AuthHelper;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.egovframework.util.ApiUtil;
import rbs.modules.major.service.MajorService;
import rbs.modules.major.serviceOra.MajorServiceOra;
import rbs.modules.majorInfo.service.MajorInfoService;
import rbs.modules.sbjt.serviceOra.SbjtServiceOra;
import rbs.modules.search.service.SearchService;

//import kr.dogfoot.hwplib.object.HWPFile;
//import kr.dogfoot.hwplib.reader.HWPReader;
//import kr.dogfoot.hwplib.tool.textextractor.TextExtractMethod;
//import kr.dogfoot.hwplib.tool.textextractor.TextExtractor;
//
//import kr.dogfoot.hwplib.tool.textextractor.TextExtractOption;


/**
 * 샘플모듈<br/>
 * : 통합관리시스템 > 메뉴콘텐츠관리, 통합관리시스템 > 기능등록관리, 사용자 사이트 에서 사용
 * @author user
 *
 */
@Controller
@ModuleMapping(moduleId="major")
@RequestMapping({"/{siteId}/major", "/{admSiteId}/menuContents/{usrSiteId}/major"})
public class MajorController extends ModuleController {
	
	private static final Logger logger = LoggerFactory.getLogger(MajorController.class);
	
	@Resource(name = "majorService")
	private MajorService majorService;	
	
	@Resource(name="searchService")
	private SearchService searchService;	
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@Resource(name = "majorServiceOra")
	protected MajorServiceOra majorServiceOra;
	
	@Resource(name = "sbjtServiceOra")
	protected SbjtServiceOra sbjtServiceOra;
	
	@Resource(name = "majorInfoService")
	protected MajorInfoService majorInfoService;
	
	/*@Resource(name = "inuUserService")
	private InuUserService inuUserService;
	
	@Resource(name = "searchLogService")
	private SearchLogService searchLogService;*/

	
	/**
	 * 코드
	 * @param submitType
	 * @param itemInfo
	 * @return
	 */
	public HashMap<String, Object> getOptionHashMap(String submitType, JSONObject itemInfo) {
		return getOptionHashMap(submitType, itemInfo, null);
	}
	
	/**
	 * 코드
	 * @param submitType
	 * @param itemInfo
	 * @param addOptionHashMap
	 * @return
	 */
	public HashMap<String, Object> getOptionHashMap(String submitType, JSONObject itemInfo, HashMap<String, Object> addOptionHashMap) {
		// 코드
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		HashMap<String, Object> optnHashMap = (addOptionHashMap != null)?CodeHelper.getItemOptnHashMap(items, itemOrder, addOptionHashMap):CodeHelper.getItemOptnHashMap(items, itemOrder);
		return  optnHashMap;
	}
	
	/**
	 * 목록조회
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/list.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		
		model.addAttribute("collegeList", majorInfoService.getCollegeList());
		model.addAttribute("departList", majorInfoService.getDepartList(null));
		
		// 4. 기본경로
		fn_setCommonPath(attrVO);
		
		return getViewPath("/list");
	}
	
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/DepartAjax.json", headers="Ajax")
	public ModelAndView departAjax(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		
		// 모듈정보
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		
		String univ = request.getParameter("UNIV");
		
		param.put("univ", univ);
		list = majorInfoService.getDepartList(param);
		
		model.addAttribute("departList", list);
		
		return mav;
	}
	
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/MajorAjax.json", headers="Ajax")
	public ModelAndView majorAjax(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
			
		// 모듈정보
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		
		String depart = request.getParameter("DEPART");
		
		
		param.put("depart", depart);
		list = majorInfoService.getMajorList(param);
		
		model.addAttribute("majorList", list);
		
		return mav;
	}
	
	/**
	 * 최초 목록조회(비동기)
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/initMajorList.do", headers="Ajax")
	public ModelAndView initMajorList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
			
		//SSO 로그인 세션정보
		HttpSession session = request.getSession(true); 
		
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		//String memberId = loginVO.getMemberId();
		//List<Object> userInfo = inuUserService.getInuUserInfo(memberId);
		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int listUnit = 6;	// 페이지당 목록 수
		int listMaxUnit = 10;	// 최대 페이지당 목록 수 
		int listUnitStep = 6;	// 페이지당 목록 수 증가값
		
		int pageUnit = 6;
		int pageSize = 10;	
		
		int page = StringUtil.getInt(request.getParameter("page"), 1);				// 현재 페이지 index

		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(pageUnit);
		paginationInfo.setPageSize(pageSize);
		
		
		
		int endNum = page * 6;
		int startNum = endNum - 5;
		
		Map<String, Object> param = new HashMap<String, Object>();
		String univ = request.getParameter("univ");
		String depart = request.getParameter("depart");
		String major = request.getParameter("major");
		String orderBy = request.getParameter("orderBy");
		
		if(StringUtil.isEquals(orderBy, "byMajor")  ) {
			param.put("field", "MAJOR_NM_KOR");
			param.put("order", "ASC");
		} else if(StringUtil.isEquals(orderBy, "byDepart")  ) {
			param.put("field", "DEPT_CD");
			param.put("order", "ASC");
		} else if(StringUtil.isEquals(orderBy, "byType")  ) {
			param.put("field", "MAJOR_CD");
			param.put("order", "ASC");
		} 
		
		param.put("startNum", startNum);
		param.put("endNum", endNum);
		param.put("univ", univ);
		param.put("depart", depart);
		param.put("major", major);
		
		List<Object> majorList = new ArrayList<>();
		
		majorList = majorService.getInitMajorList(param);
		
		//전체 갯수 설정
		DataMap totalCount = majorService.getInitMajorListCount(param);
		int totCnt = ((Long) totalCount.get("TOTAL_COUNT")).intValue();
		model.addAttribute("totalCount", totCnt);
		paginationInfo.setTotalRecordCount(totCnt);
		
		model.addAttribute("paginationInfo", paginationInfo);
		
		model.addAttribute("majorList", (majorList == null) ? null : majorList);
		
		// 4. 기본경로
		fn_setCommonPath(attrVO);
		
		return mav;
	}
	
	/**
	 * 목록조회(비동기)
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/majorList.do", headers="Ajax")
	public ModelAndView sbjtList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
				
		String top_search = StringUtil.isEmpty(request.getParameter("top_search")) ? "" : request.getParameter("top_search");
		String univ = request.getParameter("univ");
		String depart = request.getParameter("depart");
		String major = request.getParameter("major");
		String orderBy = request.getParameter("orderBy");
		String flagLog = request.getParameter("flagLog");
		
		//SSO 로그인 세션정보
		HttpSession session = request.getSession(true); 
		
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		//String memberId = loginVO.getMemberId();
		//List<Object> userInfo = inuUserService.getInuUserInfo(memberId);
		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int listUnit = 10;	// 페이지당 목록 수
		int listMaxUnit = 5;	// 최대 페이지당 목록 수 
		int listUnitStep = 10;	// 페이지당 목록 수 증가값
		
		int pageUnit = 10;
		int pageSize = 10;	
		
		int page = StringUtil.getInt(request.getParameter("page"), 1);				// 현재 페이지 index

		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(pageUnit);
		paginationInfo.setPageSize(pageSize);
		
		// 검색어가 존재할 때
		if(request.getParameter("top_search") != null && !"".equals(request.getParameter("top_search"))) {		
		
			//api 세팅
			String url = RbsProperties.getProperty("Globals.search.url");
			String endpoint = RbsProperties.getProperty("Globals.search.endpoint.major");
			
			//api 호출 파라미터
			JSONObject reqJsonObj = new JSONObject();
			
			List<HashMap<String, String>> sortList = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> sortMap = new HashMap<String, String>();
			if(StringUtil.isEquals(orderBy, "byMajor")  ) {
				sortMap.put("sortType", "fieldSort");
				sortMap.put("field", "MAJOR_NM_KOR");
				sortMap.put("order", "ASC");
				sortList.add(sortMap);
			} else if(StringUtil.isEquals(orderBy, "byDepart")  ) {
				sortMap.put("sortType", "fieldSort");
				sortMap.put("field", "DEPT_CD");
				sortMap.put("order", "ASC");
				sortList.add(sortMap);
			} else {
				sortMap.put("sortType", "scoreSort");
				sortMap.put("order", "DESC");
				sortList.add(sortMap);
			} 
			
			
			
			reqJsonObj.put("keyword", top_search);		//검색어
			reqJsonObj.put("page_num", page-1);			//요청 페이지 번호
			reqJsonObj.put("page_per", 10);				//페이지당 목록 수
			reqJsonObj.put("sort", sortList);			//정렬
			reqJsonObj.put("university", univ);			//대학코드   309000
			reqJsonObj.put("department", depart);		//학과코드   309050
			reqJsonObj.put("major", major);				//전공코드   309050
	
			
			/////////////페이지 세팅
			//reqJsonObj.put("pageNo", 1);
			if(null != request.getParameter("spjtListPage")) {
				int startNum = Integer.parseInt(request.getParameter("spjtListPage"));
				System.out.println("**********************startNum : " + startNum);
				//reqJsonObj.put("pageNo", startNum);
			}
			
			//api 호출
			String responseData = null;
			responseData = ApiUtil.getRestApi(url, endpoint, ApiUtil.METHOD_POST, reqJsonObj);
			
			//log insert
			if(StringUtil.isEquals(flagLog, "Y")) {
				searchService.insertSearchLog(endpoint, reqJsonObj, responseData, request);
			}
			
			JSONObject responseJson = JSONObject.fromObject(responseData);
			
			//전체 갯수 설정
			int totalCount = 0;
			if(null != responseJson.getJSONObject("data").getString("total_count")) {
				totalCount = responseJson.getJSONObject("data").getInt("total_count");
			}
	
			model.addAttribute("totalCount", totalCount);
			paginationInfo.setTotalRecordCount(totalCount);
			model.addAttribute("paginationInfo", paginationInfo);
			
			String searchLogResponse = null;
	
			//전공 목록 추출
			JSONArray resultArray = responseJson.getJSONObject("data").getJSONArray("result");
	
			List<Map<String, String>> majorList = new ArrayList<>();
			
			if(totalCount>0) {
				
				//화면에 뿌려줄 데이터 세팅
				for (Object majorObj : resultArray) {
					JSONObject majorJobj = (JSONObject) majorObj;;
					Map<String, String> map = new HashMap<>();
					String talent = majorJobj.getString("TALENT").replaceAll("\"", "").replace("[", "").replace("]", "").replace(",", ", ");
					String field = majorJobj.getString("FIELD").replaceAll("\"", "").replace("[", "").replace("]", "").replace(",", ", ");
						
					map.put("MAJOR_CD", 		majorJobj.getString("MAJOR_CD"));
					map.put("COLG_CD", 			majorJobj.getString("COLG_CD"));
					map.put("DEPT_CD", 			majorJobj.getString("DEPT_CD"));
					map.put("MAJOR_NM_KOR", 	majorJobj.getString("MAJOR_NM_KOR"));
					map.put("MAJOR_NM_ENG", 	majorJobj.getString("MAJOR_NM_ENG"));
					map.put("MAJOR_INTRO", 		majorJobj.getString("MAJOR_INTRO"));
					map.put("GOAL", 			majorJobj.getString("GOAL"));
					map.put("TALENT", 			talent);
					map.put("FIELD", 			field);
					map.put("MAJOR_ABTY", 		majorJobj.getString("MAJOR_ABTY"));
						
					majorList.add(map);
				}
			}
//			logger.debug("majorList============>"+majorList);
	
			model.addAttribute("majorList", majorList);
		}
		
		return mav;
	}
	
	/**
	 * 관심교과목 등록 수정
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/inputCourBKProc.json", headers="Ajax")
	public ModelAndView inputCourBKProc(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@RequestParam Map<String, Object> paramMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		
		//SSO값 꺼내쓰기
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		
		List<DTForm> queryList = new ArrayList<DTForm>();
		
		queryList.add(new DTForm("PERS_NO",loginVO.getMemberId()));
		queryList.add(new DTForm("DEPT_CLSF_CD", paramMap.get("deptClsfCd")));
		queryList.add(new DTForm("YY", paramMap.get("yy")));
		queryList.add(new DTForm("TM_GBN", paramMap.get("tmGbn")));
		queryList.add(new DTForm("HG_MJ_CD", paramMap.get("hgMjCd")));
		queryList.add(new DTForm("SC_CD", paramMap.get("scCd")));
		
		param.put("searchList", queryList);
		int bkCnt = majorService.bkCourCount(param);
		
		int result =0;
		
		if(paramMap.get("booMarkIdx").equals("0")) {	//등록
			if(bkCnt<1) {
				param.put("queryList", queryList);			//처음 등록시
				result = majorService.insertBkCour(param, request);
			}else {
				param.put("isdelete", "0");					//재 등록시
				//param.put("searchList", queryList);			
				result = majorService.updateBkCour(param, request);
			}
		}else {
			param.put("isdelete", "1");						//삭제 시
			//param.put("searchList", queryList);
			result = majorService.updateBkCour(param, request);
		}
	
		model.addAttribute("res", result);

		return mav;
	}
	

	/**
	 * 상세조회
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/view.do")
	public String view(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
		// 1. 필수 parameter 검사 - 식별ID(전공코드)
		String majorCd = request.getParameter("MAJOR_CD");
		String colgNm = request.getParameter("COLG_NM");
		String deptNm = request.getParameter("DEPT_NM");
		
		if(majorCd == null) {
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		majorServiceOra.getNewMajorList();
		// 2. DB
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("MAJOR_CD", majorCd);
		
		// 전공 상세-학부(과)
		model.addAttribute("info", param);
		
		// 전공 상세-기본정보
		model.addAttribute("majorInfo", majorService.getView(param) != null ? majorService.getView(param) : null);
		
		// 전공 상세-인재상 + 전공능력
		model.addAttribute("majorTalent", majorService.getMajorAbty(param) != null ? majorService.getMajorAbty(param) : null);
		
		// 전공 상세- 전공능력 + 하위역량 정의
		model.addAttribute("checkAbty", majorService.checkAbty(param)); // 하위역량 존재 체크 
		model.addAttribute("majorAbty", majorService.getMajorAbtyDef(param) != null ? majorService.getMajorAbtyDef(param) : null);
		
		// 전공 상세-교육과정
		model.addAttribute("majorSbjt", majorService.getMajorSbjtList(param) != null ? majorService.getMajorSbjtList(param) : null);
		//majorServiceOra.getNewMajorList();
		
		// 4. 기본경로
		fn_setCommonPath(attrVO);
		
		return getViewPath("/view");
	}
	
	
	/**
	 * 글자 추출
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getTextPdf.json", headers="Ajax")
	public void getTextPdf(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@RequestParam Map<String, Object> paramMap) throws Exception {
//		logger.debug("****************getText**********apiStart****************");
//		
//		HWPFile hwpFile = HWPReader.fromFile("/eGovFrameDev-3.9.0-64bit/workspace/tmou/Source/src/main/webapp/upload/major/getText.hwp");
//		
//        System.out.println(hwpFile + "  읽기 성공 !!");
//        System.out.println();
//
//        TextExtractOption option = new TextExtractOption();
//        option.setMethod(TextExtractMethod.InsertControlTextBetweenParagraphText);
//        option.setWithControlChar(false);
//        option.setAppendEndingLF(true);
//
//        String hwpText = TextExtractor.extract(hwpFile, option);
//        System.out.println(hwpText);
//        System.out.println("========================================================");
//		
//        String fileName = "D:/eGovFrameDev-3.9.0-64bit/workspace/tmou/Source/src/main/webapp/upload/major/majorInfo.txt";
//		
//        try{
//			
//			// 파일 객체 생성
//			File file = new File(fileName) ;
//			
//			// true 지정시 파일의 기존 내용에 이어서 작성
//			FileWriter fw = new FileWriter(file, true) ;
//			
//			// 파일안에 문자열 쓰기
//			fw.write(hwpText);
//			fw.flush();
//
//			// 객체 닫기
//			fw.close(); 
//			
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
        
		/*HWPFile hwpFile;
		String hwpText;
		
		String filePath = "/eGovFrameDev-3.9.0-64bit/workspace/tmou/Source/src/main/webapp/upload/major/getText.hwp";
		
		try {
			hwpFile = HWPReader.fromFile(filePath);
			hwpText = TextExtractor.extract(hwpFile, TextExtractMethod.InsertControlTextBetweenParagraphText);
			
			System.out.println("===== hwp text extractor =====");
			System.out.println("hwpText = " + hwpText);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		
		/*try {
			String path = "/eGovFrameDev-3.9.0-64bit/workspace/tmou/Source/src/main/webapp/upload/major/";
			File file = new File(path + "/getText.pdf");
			
			PDDocument document;
			document = PDDocument.load(file);
			
			PDFTextStripper s = new PDFTextStripper();
			String content = s.getText(document);
			
			System.out.println("===== docx text extractor =====");
			System.out.println(content);
		} catch(IOException e){
			e.printStackTrace();
		}*/
		
		/*String filePath = "/eGovFrameDev-3.9.0-64bit/workspace/tmou/Source/src/main/webapp/upload/major/getText.pdf";
        int startPage = 2;
        int endPage = 7;

        // 추출할 영역 정의 (사각형 영역: x, y, width, height)
        Rectangle rect = new Rectangle(570, 10, 600, 900);

        try {
            // 특정 영역에서 텍스트 추출
            String text = extractTextFromArea(filePath, startPage, endPage, rect);

            // 추출한 텍스트 출력
            System.out.println("Extracted Text from specified area: ");
            System.out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
		
//		logger.debug("****************getText**********endStart****************");

		
	}
	
	
	public static String extractTextFromArea(String filePath, int startPage, int endPage, Rectangle rect) throws IOException {
        StringBuilder extractedText = new StringBuilder();

        // PDF 문서 로드
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            if (startPage < 1 || endPage > document.getNumberOfPages() || startPage > endPage) {
                throw new IllegalArgumentException("Invalid page range");
            }

            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.addRegion("region", rect);

            for (int pageNumber = startPage; pageNumber <= endPage; pageNumber++) {
                PDPage page = document.getPage(pageNumber - 1);
                stripper.extractRegions(page);
                String text = stripper.getTextForRegion("region");
                extractedText.append("Page ").append(pageNumber).append(":\n");
                extractedText.append(text).append("\n\n");
            }
        }

        return extractedText.toString();
    }
	
	
	
	/**
	 * 관심강좌 등록 수정
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/inputLectureBKProc.json", headers="Ajax")
	public ModelAndView inputLectureBKProc(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@RequestParam Map<String, Object> paramMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();		
		//SSO값 꺼내쓰기
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		
		List<DTForm> queryList = new ArrayList<DTForm>();	//검색항목
		
		queryList.add(new DTForm("PERS_NO", loginVO.getMemberId()));
		queryList.add(new DTForm("DEPT_CLSF_CD", paramMap.get("deptClsfCd")));
		queryList.add(new DTForm("YY", paramMap.get("yy")));
		queryList.add(new DTForm("TM_GBN", paramMap.get("tmGbn")));
		queryList.add(new DTForm("HAKSU_NO", paramMap.get("haksuNo")));
		
		param.put("searchList", queryList);
		int bkCnt = majorService.bkLectureCount(param);
		
		int result =0;
		
		if(paramMap.get("booMarkIdx").equals("0")) {	//등록
			if(bkCnt<1) {
				param.put("queryList", queryList);			//처음 등록시
				result = majorService.insertBkLecture(param, request);
			}else {
				param.put("isdelete", "0");					//재 등록시
				result = majorService.updateBkLecture(param, request);
			}
		}else {
			param.put("isdelete", "1");						//삭제 시
			result = majorService.updateBkLecture(param, request);
		}
		
		model.addAttribute("res", result);

		return mav;
	}
	

	/**
	 * 기본경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key

		String listSearchId = "list_search";
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, listSearchId));	// 목록 검색 항목
		String[] tabBaseParams = null;
		String cateTabId = JSONObjectUtil.getString(settingInfo, "dset_cate_tab_id");
		if(!StringUtil.isEmpty(cateTabId)) tabBaseParams = new String[]{RbsProperties.getProperty("Globals.item.tab.search.pre.flag") + cateTabId};
		
		String listName = "list.do";
		String viewName = "view.do";
		String inputName = "input.do";
		String inputProcName = "inputProc.do";
		String deleteProcName = "deleteProc.do";
		String deleteListName = "deleteList.do";
		String imageName = "image.do";
		String movieName = "movie.do";
		String downloadName = "download.do";
		
		if(useSsl){
			inputProcName = PathUtil.getSslPagePath(inputProcName);
		}
		
		PathUtil.fn_setCommonPath(queryString, baseParams, tabBaseParams, searchParams, null, null, pageName, idxName, listName, viewName, null, null, null, null, inputName, inputProcName, deleteProcName, deleteListName, imageName, movieName, downloadName);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

		fn_setCommonAddPath(request, attrVO);
	}
	
	public void fn_setCommonAddPath(HttpServletRequest request, @ModuleAttr ModuleAttrVO attrVO) {
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));
	
		//북마크 등록
		request.setAttribute("URL_INPUTCOURBKPROC", "inputCourBKProc.json" + baseQueryString);
		request.setAttribute("URL_INPUTLECTUREBKPROC", "inputLectureBKProc.json" + baseQueryString);
		
		//주관대학 3차 셀렉트 박스 
		request.setAttribute("URL_DEPARTLIST", "DepartAjax.json" + baseQueryString);
		request.setAttribute("URL_MAJORLIST", "MajorAjax.json" + baseQueryString);
		
		//상세화면내의 조회
		request.setAttribute("URL_CLASSVIEW", "classView.json" + baseQueryString);
		request.setAttribute("URL_PLANVIEW", "planView.do" + baseQueryString);
		request.setAttribute("URL_NANOVIEW", "nanoView.json" + baseQueryString);
		request.setAttribute("URL_MATIRXVIEW", "matrixView.json" + baseQueryString);
		request.setAttribute("URL_STATVIEW", "statView.json" + baseQueryString);
		request.setAttribute("URL_PROFVIEW", "/web/prof/view.do?mId=43");
		
		request.setAttribute("URL_SEARCHUNIVLIST", "searchUnivList.json" + baseQueryString);
		request.setAttribute("URL_SEARCHSUBJECTLIST", "searchSubjectList.json" + baseQueryString);

		
	}

	/**
	 * 저장 후 되돌려줄 페이지 속성명
	 * @param settingInfo
	 * @return
	 */
	public String fn_dsetInputNpname(JSONObject settingInfo){
		String dsetInputNpname = JSONObjectUtil.getString(settingInfo, "dset_input_npname");
		if(StringUtil.isEmpty(dsetInputNpname)){
			dsetInputNpname = "LIST";
			return dsetInputNpname;
		}
		
		return dsetInputNpname;
	}
	
	/**
	 * 관리권한 체크
	 * @param settingInfo
	 * @param fnIdx
	 * @param keyIdx
	 * @param memIdx
	 * @param useReply
	 * @param pwd
	 * @return
	 */
	public boolean isMngProcAuth(JSONObject settingInfo, int fnIdx, int keyIdx) {
		String columnName = JSONObjectUtil.getString(settingInfo, "idx_column");
		
		int modiCnt = 0;
		// 권한 체크
		Boolean isMngAuth = AuthHelper.isAuthenticated("MNG");		// 전체관리
		
		if(!isMngAuth){
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			
			searchList.add(new DTForm("A." + columnName, keyIdx));
			
			// 전체관리권한 없는 경우 : 자신글만 수정
			boolean isLogin = UserDetailsHelper.isLogin();		// 로그인한 경우
			if(isLogin){
				// 로그인한 경우
				String memberIdx = null;
				
				LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
				if(loginVO != null) {
					memberIdx = loginVO.getMemberIdx();
				}
				
				if(StringUtil.isEmpty(memberIdx)) return false;
				param.put("AUTH_MEMBER_IDX", memberIdx);
			} else {
				// 본인인증 or 로그인 안 한 경우
				return false;
			}

			param.put("searchList", searchList);
			modiCnt = majorService.getAuthCount(fnIdx, param);
			
			return (modiCnt > 0);
		}
		
		return true;
	}
}
