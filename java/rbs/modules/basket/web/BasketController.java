package rbs.modules.basket.web;

import java.io.BufferedInputStream;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.modules.basket.service.BasketService;
import rbs.modules.code.serviceOra.CodeOptnServiceOra;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 장바구니 Controller
 * @author	이동근
 * @since	2024.08.29
 */
@Controller
@ModuleMapping(moduleId="basket")
@RequestMapping({"/{siteId}/basket", "/{admSiteId}/menuContents/{usrSiteId}/basket"})
public class BasketController extends ModuleController {

	@Resource(name = "basketService")
	private BasketService basketService;
	

	@Resource(name = "codeOptnServiceOra")
	private CodeOptnServiceOra codeOptnServiceOra;
	
	@Resource(name = "jsonView")
	View jsonView;
	
	/**
	 * 장바구니 가져오기(교과목)
	 * 
	 */
	@RequestMapping(value = "/getSbjtBasket.do", method = RequestMethod.POST)
	public ModelAndView getSbjtBasket(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
		
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();
		
		Map<String, Object> param = new HashMap<String, Object>();
		String year = reqJsonObj.get("year").toString();
		String smt = reqJsonObj.get("smt").toString();
		String subjectCd = reqJsonObj.get("subjectCd").toString();
		String deptCd = reqJsonObj.get("deptCd").toString();
		String divcls = reqJsonObj.get("divcls").toString();
		String empNo = reqJsonObj.get("empNo").toString();
		
		param.put("STUDENT_NO", memberId);
		param.put("YEAR", year);
		param.put("SMT", smt);
		param.put("SUBJECT_CD", subjectCd);
		param.put("DEPT_CD", deptCd);
		param.put("DIVCLS", divcls);
		param.put("EMP_NO", empNo);
		
		List<Object> basketList = null;
		basketList = basketService.getSbjtBasket(param);	
		mav.setView(jsonView);
		mav.addObject("basketList", basketList.size() < 1 ? null : basketList);


		
		return mav;
	}
	
	/**
	 * 개설강좌
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getCurInfo.json", headers="Ajax")
	public ModelAndView getCurInfo(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@RequestParam Map<String, Object> paramMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
			 
		DataMap dt = basketService.getCurInfo();
		
		model.addAttribute("dt", dt); // 현재학기 정보
		
		
		return mav;
	}
	
	
	/**
	 * 장바구니 가져오기(마이페이지)
	 * 
	 */
	@RequestMapping(value = "/getBasketList.do", method = RequestMethod.POST)
	public ModelAndView getBasketList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();
		
		JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
		
/*//		// 1. 페이지정보 setting
//		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
//		int listUnit = 6;	// 페이지당 목록 수
//		int listMaxUnit = 10;	// 최대 페이지당 목록 수 
//		int listUnitStep = 6;	// 페이지당 목록 수 증가값
//		
//		//int recordCountPerPage = 5;	
//		int pageSize = 10;			//페이징 리스트의 사이즈(페이징을 1부터 몇까지 보여줄껀지)
//		int pageUnit = 6;			//한 페이지에 게시되는 게시물 건수(표 데이터 갯수)
//		int page = StringUtil.getInt(reqJsonObj.get("page").toString(), 1); // 현재 페이지 index
//		
//
//		paginationInfo.setUnitBeginCount(listUnit);
//		paginationInfo.setUnitEndCount(listMaxUnit);
//		paginationInfo.setUnitStep(listUnitStep);
//		paginationInfo.setCurrentPageNo(page);
//		paginationInfo.setRecordCountPerPage(pageUnit );	//한 페이지에 게시되는 게시물 건수
//		paginationInfo.setPageSize(pageSize); 						//페이징 리스트의 사이즈
*/		
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", memberId);
		param.put("YEAR", reqJsonObj.getString("YEAR"));
		param.put("SMT", reqJsonObj.getString("SMT"));
		
//		// 2.2 목록수
//    	int totalCount = basketService.getBasketCount(param);
//		paginationInfo.setTotalRecordCount(totalCount);
		List<Object> basketList = null;
//		
//		if(totalCount > 0) {
//    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
//    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
//    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
//		}
		
		basketList = basketService.getBasketList(param);	
		
		
		mav.setView(jsonView);
		mav.addObject("basketList", basketList.size() < 1 ? null : basketList);
//		mav.addObject("paginationInfo", paginationInfo);	// 페이징정보
										
		
		return mav;
	}
	
	
	/**
	 * 예비수강신청 현황 가져오기
	 * 
	 */
	@RequestMapping(value = "/getPreApplSbjt.do", method = RequestMethod.POST)
	public ModelAndView getPreApplSbjt(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", memberId);
		
		List<Object> preApplList = null;
		
		preApplList = basketService.getPreApplSbjt(param);	
		
		mav.setView(jsonView);
		mav.addObject("preApplList", preApplList);
		
		return mav;
	}

	/**
	 * 예비수강신청 순서변경
	 * 
	 */
	@RequestMapping(value = "/updateOrder.do", method = RequestMethod.POST)
	public ModelAndView updateOrder(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
			
			

			HttpSession session = request.getSession(true); 
			LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
			String memberId = loginVO.getMemberId();
			
			basketService.updateOrder(reqJsonObj, memberId);
			
			mav.setView(jsonView);
			mav.addObject("result", "DONE");
		} catch(Exception e) {
			mav.setView(jsonView);
			mav.addObject("result", "FAIL");				
			mav.addObject("error", e.getClass().getName());
			e.printStackTrace();
		}
		
		return mav;
	}
	
	
	/**
	* 예비수강신청 - 신청(프로시저 호출)
	* 
	*/
	@RequestMapping(value = "/sukangSin.do", method = RequestMethod.POST)
	public ModelAndView sukangSin(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
		
		String resultMsg = basketService.sukangSin(request, reqJsonObj);
		
		System.out.println(resultMsg);
		
		mav.setView(jsonView);
		mav.addObject("resultMsg", resultMsg);
		
		return mav;
	}
	
	/**
	 * 예비수강신청 - 삭제(프로시저 호출)
	 * 
	 */
	@RequestMapping(value = "/sukangDel.do", method = RequestMethod.POST)
	public ModelAndView sukangDel(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
		
		String resultMsg = basketService.sukangDel(request, reqJsonObj);
		
		System.out.println(resultMsg);
		
		mav.setView(jsonView);
		mav.addObject("resultMsg", resultMsg);
		
		return mav;
	}
	
	/**
	 * 장바구니 등록
	 * 
	 */
	@RequestMapping(value = "/insertBsk.do", method = RequestMethod.POST)
	public ModelAndView insertBsk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			HttpSession session = request.getSession(true); 
			LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
			String memberId = loginVO.getMemberId();
			
			int insert = basketService.insertBsk(memberId, rawBody, request.getRemoteAddr());
			
			mav.setView(jsonView);
			mav.addObject("result", "DONE");
		} catch(Exception e) {
			mav.setView(jsonView);
			mav.addObject("result", "FAIL");				
			mav.addObject("error", e.getClass().getName());
			e.printStackTrace();
		}
		
		return mav;
	}
	
	/**
	* 장바구니 삭제
	* 
	*/
	@RequestMapping(value = "/deleteBsk.do", method = RequestMethod.POST)
	public ModelAndView deleteBsk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		try {
			HttpSession session = request.getSession(true); 
			LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
			String memberId = loginVO.getMemberId();
			
			int insert = basketService.deleteBsk(memberId, rawBody);
			
			mav.setView(jsonView);
			mav.addObject("result", "DONE");
		} catch(Exception e) {
			mav.setView(jsonView);
			mav.addObject("result", "FAIL");				
			mav.addObject("error", e.getClass().getName());		
			e.printStackTrace();
		}
		
		return mav;
	}
	
	/**
	* 장바구니 선택 삭제
	* 
	*/
	@RequestMapping(value = "/deleteSelectedBsk.do", method = RequestMethod.POST)
	public ModelAndView deleteSelectedBsk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		try {
			HttpSession session = request.getSession(true); 
			LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
			String memberId = loginVO.getMemberId();
			
			int insert = basketService.deleteSelectedBsk(memberId, rawBody);
			
			mav.setView(jsonView);
			mav.addObject("result", "DONE");
		} catch(Exception e) {
			mav.setView(jsonView);
			mav.addObject("result", "FAIL");				
			mav.addObject("error", e.getClass().getName());	
			e.printStackTrace();
		}
		
		return mav;
	}
	
	/**
	 * 장바구니 전체 삭제
	 * 
	 */
	@RequestMapping(value = "/deleteAllBsk.do", method = RequestMethod.POST)
	public ModelAndView deleteAllBsk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		try {
			HttpSession session = request.getSession(true); 
			LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
			String memberId = loginVO.getMemberId();
			
			int insert = basketService.deleteAllBsk(memberId);
			
			mav.setView(jsonView);
			mav.addObject("result", "DONE");
		} catch (Exception e) {
			mav.setView(jsonView);
			mav.addObject("result", "FAIL");				
			mav.addObject("error", e.getClass().getName());	
			e.printStackTrace();
		}
		
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

		if (useSsl){
			inputProcName = PathUtil.getSslPagePath(inputProcName);
		}

		PathUtil.fn_setCommonPath(queryString, baseParams, tabBaseParams, searchParams, null, null, pageName, idxName, listName, viewName, null, null, null, null, inputName, inputProcName, deleteProcName, deleteListName, imageName, movieName, downloadName);

		// 추가경로
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		

		
		request.setAttribute("baseQueryString", baseQueryString);
	}
	
	private static byte[] blobToBytes(Blob blob) {
		BufferedInputStream is = null;
		byte[] bytes = null;
		try {
			is = new BufferedInputStream(blob.getBinaryStream());
			bytes = new byte[(int) blob.length()];
			int len = bytes.length;
			int offset = 0;
			int read = 0;

			while (offset < len
					&& (read = is.read(bytes, offset, len - offset)) >= 0) {
				offset += read;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}

	// [byte를 base64로 인코딩 해주는 메소드]
	private static String byteToBase64(byte[] arr) {
		String result = "";
		try {
			result = Base64Utils.encodeToString(arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	

}