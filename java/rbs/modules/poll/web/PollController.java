package rbs.modules.poll.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.DataSecurityUtil;
import com.woowonsoft.egovframework.util.FormValidatorUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;

/**
 * 설문관리<br/>
 * @author user
 *
 */
@Controller
@RequestMapping({"/{admSiteId}/menuContents/{usrSiteId}/poll", "/{admSiteId}/moduleFn/poll"})
@ModuleMapping(moduleId="poll")
public class PollController extends PollResController {

	/**
	 * 목록조회
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/list.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		int fnIdx = attrVO.getFnIdx();
		DataForm queryString = attrVO.getQueryString();
		JSONObject crtMenu = attrVO.getCrtMenu();
		boolean isAdmMode = attrVO.isAdmMode();
		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int usePaging = JSONObjectUtil.getInt(settingInfo, "use_paging");
		if(usePaging == 1) {
			int listUnit = ModuleUtil.getSettingValue(isAdmMode, "dset_list_count", crtMenu, settingInfo, propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
			int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
			int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
			
			int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
			if(pageUnit == 0) pageUnit = listUnit;//JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("PAGE_UNIT"));	// 페이지당 목록 수
			int pageSize = ModuleUtil.getSettingValue(isAdmMode, "dset_list_block", crtMenu, settingInfo, propertiesService.getInt("PAGE_SIZE"));	
			
			String pageName = JSONObjectUtil.getString(settingInfo, "page_name");
			int page = StringUtil.getInt(request.getParameter(pageName), 1);				// 현재 페이지 index
	
			paginationInfo.setUnitBeginCount(listUnit);
			paginationInfo.setUnitEndCount(listMaxUnit);
			paginationInfo.setUnitStep(listUnitStep);
			paginationInfo.setCurrentPageNo(page);
			paginationInfo.setRecordCountPerPage(pageUnit);
			paginationInfo.setPageSize(pageSize);
		}
		
		// 2. DB
		int totalCount = 0;
		List<?> list = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		// 2.1 검색조건
		// 항목설정으로 검색조건 setting
		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		param.put("searchList", searchList);
		
		// 2.2 목록수
    	totalCount = pollService.getTotalCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		if(usePaging == 1) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		}
    		
    		// 2.3 목록
    		list = pollService.getRespCntList(fnIdx, param);
    	}
    	
    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 항목 코드
    	
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
		return getViewPath("/list");
	}
	
	/**
	 * 참여결과 엑셀다운로드
	 * @param pollIdx
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/respExcelDown.do")
	public ModelAndView respExcelDownload(@RequestParam(value="pollIdx") int pollIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		JSONObject settingInfo = attrVO.getSettingInfo();
		int fnIdx = attrVO.getFnIdx();
		
		// 자신글 결과보기 && 답글 사용하는 경우만 가능
		int usePrivate = JSONObjectUtil.getInt(settingInfo, "use_private");
		int useReply = JSONObjectUtil.getInt(settingInfo, "use_reply");
		
		if(usePrivate != 1 || useReply != 1) {
			return new ModelAndView(RbsProperties.getProperty("Globals.error.400.path"));
		}
		
		/*
		// 파일 없는  경우
		model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.file.searchFile"), ""));
		return new ModelAndView(RbsProperties.getProperty("Globals.fail.path"));
		 */
		// 1. DB
		DataMap dt = null;											// 설문정보
		List<Object> quesList = null;								// 문항목록
		List<Object> respList = null;								// 참여목록
		Map<String, List<Object>> inclassMap = null;				// 내부분류
		Map<String, List<Object>> inquesMap = null;					// 내부문항
		Map<String, List<Object>> etcitemMap = null;				// 기타의견 항목
		Map<String, List<Object>> resultMap = null;					// 참여결과
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		param.put("searchList", searchList);

		// 1.1 설문정보
		dt = pollService.getRespCntView(fnIdx, param);
		if(dt == null) {
			return new ModelAndView(RbsProperties.getProperty("Globals.error.400.path"));
		}
		// 1.2 문항목록
		quesList = quesService.getList(fnIdx, param);
		// 1.3 참여목록
		respList = respService.getList(fnIdx, param);
		// 1.4 내부분류
		inclassMap = pollService.getInclassMap(fnIdx, param);
		// 1.5 내부문항
		inquesMap = pollService.getInquesMap(fnIdx, param);
		//1.6 참여결과
		resultMap = respService.getResultListMap(fnIdx, param);
		
		//1.7 기타의견
		searchList.add(new DTForm("A.ISTEXT", "1"));
		param.put("searchList", searchList);
		etcitemMap = pollService.getItemMap(fnIdx, param);
		
		// 2. 엑셀파일 생성
		String pollTitle = StringUtil.substring(StringUtil.getString(dt.get("TITLE")), 0, 10);
		String fileName = pollTitle;
		String sheetName = pollTitle;
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		// 2.1 Head setting
		String[] columnHeads = {"구분", "성명", "아이디"};
		String[] columnNames = {"", "REGI_NAME", "REGI_ID"};											// 컬럼명
		int[] columnGubuns = {-1, 0, 1};																// 컬럼 구분(-1:번호, 0:일반, 1:seed암호화)
		int[] columnWidths = {1500, 3000, 3000};
		
		int columnHeadLen = columnHeads.length;

	    try {
		    //Sheet 생성
			HSSFSheet sheet = workbook.createSheet(sheetName);
			workbook.setSheetName(0, sheetName);

			//head Row 생성
			HSSFRow headRows = null;		// 문항
			HSSFRow headICRows = null;		// 내부분류
			HSSFRow headIQRows = null;		// 내부문항
			
			//data Row 생성
			HSSFRow rows = null;
			//Cell 생성
			HSSFCell cell = null;
		    
			// Excel Border 넣기
			HSSFCellStyle headStyle = workbook.createCellStyle();
			HSSFCellStyle numStyle = workbook.createCellStyle();
			HSSFCellStyle style = workbook.createCellStyle();
			
			//폰트 객체 생성
			HSSFFont headFont = workbook.createFont();
			HSSFFont font = workbook.createFont(); 

			try{
			    int cellHeight = 312;
			    int cellFontSize = 12;
			    String cellFontName = "맑은 고딕";
			    
			    // head style setting
			    short cellHeadBG = HSSFColor.WHITE.index;
			    short cellHeadColor = HSSFColor.BLACK.index;
			    short cellHeadBorder = HSSFCellStyle.BORDER_THIN;
			    //short cellHeadFWeight = HSSFFont.BOLDWEIGHT_BOLD;
			    
	            headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	            headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	            
		    	// 배경색 지정
			    headStyle.setFillForegroundColor(cellHeadBG);
			    headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			    
			    // 테두리
	            headStyle.setBorderBottom(cellHeadBorder);
	            //headStyle.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
	            headStyle.setBorderLeft(cellHeadBorder);
	            headStyle.setBorderRight(cellHeadBorder);
	            headStyle.setBorderTop(cellHeadBorder);
	            
	            headStyle.setWrapText(true);
        		
			    // 폰트 지정
        		//headFont.setBoldweight(cellHeadFWeight); //폰트 굵게
        		headFont.setColor(cellHeadColor);
        		headFont.setFontHeightInPoints((short)cellFontSize);
        		headFont.setFontName(cellFontName);
			    headStyle.setFont(headFont);

			    
			    // 목록 style setting
			    numStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	            numStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	            
		    	// 배경색 지정
			    numStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			    numStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			    
			    // 테두리
	            numStyle.setBorderBottom(cellHeadBorder);
	            numStyle.setBorderLeft(cellHeadBorder);
	            numStyle.setBorderRight(cellHeadBorder);
	            numStyle.setBorderTop(cellHeadBorder);
        		
			    // 폰트 지정
        		font.setFontHeightInPoints((short)cellFontSize);
        		font.setFontName(cellFontName);
			    numStyle.setFont(font);
			    
			    // 목록 style setting
	            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	            
		    	// 배경색 지정
			    style.setFillForegroundColor(HSSFColor.WHITE.index);
			    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			    
			    // 테두리
	            style.setBorderBottom(cellHeadBorder);
	            style.setBorderLeft(cellHeadBorder);
	            style.setBorderRight(cellHeadBorder);
	            style.setBorderTop(cellHeadBorder);
	            
	            // 자동줄바꿈
	            style.setWrapText(true);
	            
	            // header row setting
				int rowTIdx = 0;								// 항목 row index
				int rowTICIdx = rowTIdx + 1;					// 내부분류 row index
				int rowTIQIdx = rowTICIdx + 1;					// 내부문항 row index
				
				headRows = sheet.createRow((short)rowTIdx);
				headRows.setHeight((short)cellHeight);				// 셀 높이 설정
				

				headICRows = sheet.createRow((short)rowTICIdx);
				headICRows.setHeight((short)cellHeight);				// 셀 높이 설정

				headIQRows = sheet.createRow((short)rowTIQIdx);
				headIQRows.setHeight((short)cellHeight);				// 셀 높이 설정

				String columnHead = null;
				for (int colIdx = 0; colIdx < columnHeadLen ; colIdx++) {
		        	columnHead = columnHeads[colIdx];
		            
		            cell = headRows.createCell(colIdx);
		            cell.setCellValue(columnHead);
		    		sheet.setColumnWidth(colIdx, columnWidths[colIdx]);
		    		cell.setCellStyle(headStyle);
		    		
		            cell = headICRows.createCell(colIdx);
		    		cell.setCellStyle(headStyle);
		    		
		            cell = headIQRows.createCell(colIdx);
		    		cell.setCellStyle(headStyle);
		        }
				
				// data row setting
			    int dataRowIdx = rowTIQIdx + 1;					// data row index
			    int dataColIdx = columnHeadLen;
		        int headColIdx = columnHeadLen - 1;
		        
		        // 문항
		        List<int[]> quesMergeList = new ArrayList<int[]>();
		        List<int[]> inclassMergeList = new ArrayList<int[]>();
		        boolean isInclassCell = false;
		        boolean isInquesCell = false;
		        Object quesIdx = null;
		        int useInques = 0;		// 내부문항 사용여부
		        int answerType = 0;		// 주관식/객관식여부
		        int quesOrder = 0;		// 문항 순서
		        String quesName = null;	// head column 값
		        String inclassName = null;
		        List<Object[]> totQuesList = new ArrayList<Object[]>();				// HEAD question_idx
		        List<Object> inclassList = null;
		        List<Object> inquesList = null;
		        List<Object> etcitemList = null;
		        Object inclassIdx = null;
		        int msColIdx = 0;
		        for (Object quesObj:quesList){
		        	DataMap quesDt = (DataMap)quesObj;
		        	quesIdx = quesDt.get("QUESTION_IDX");
		        	useInques = StringUtil.getInt(quesDt.get("USE_INQUES"));
		        	answerType = StringUtil.getInt(quesDt.get("ANSWER_TYPE"));
		        	//quesOrder = StringUtil.getInt(quesDt.get("QUESTION_ORDER"));
		        	quesName = "문" + (++quesOrder);
		        	
		        	headColIdx ++;
		        	msColIdx = headColIdx;
		        	// 문항 cell
		            cell = headRows.createCell(headColIdx);
		            cell.setCellValue(quesName);
		            sheet.setColumnWidth(headColIdx, 2000);
		    		cell.setCellStyle(headStyle);

		    		int mergeRowIdx = rowTIQIdx;
        			int icIdx = 0;
		        	if(answerType == 1) {
		        		// 객관식
		        		if(useInques == 1) {
		        			// 내부문항 사용
		        			// 내부분류
		        			inclassList = StringUtil.getList(inclassMap.get(quesIdx));
		        			for(Object inclassObj:inclassList) {
		        				DataMap inclassDt = (DataMap)inclassObj;
		        				inclassIdx = inclassDt.get("CLASS_IDX");
			        			inquesList = StringUtil.getList(inquesMap.get(quesIdx + "," + inclassIdx));
			        			if(inquesList == null) continue;
			        			
		        				inclassName = StringUtil.getString(inclassDt.get("CLASS_NAME"), "");
		        				if(!StringUtil.isEmpty(inclassName)) {
		        					// 분류명 있는 경우
		        					isInclassCell = true;
		        					mergeRowIdx = rowTIdx;
		        				} else {
		        					// 분류명 없는 경우
		        					mergeRowIdx = rowTICIdx;
		        				}
		        				
		        				if(icIdx > 0) headColIdx ++;
		        				// 내부분류 cell
					            cell = headICRows.createCell(headColIdx);
					            cell.setCellValue(inclassName);
					    		cell.setCellStyle(headStyle);
					    		
			        			// 내부문항
			        			int msColICIdx = headColIdx;
			        			int iqIdx = 0;
			        			for(Object inquesObj:inquesList) {
			        				DataMap inquesDt = (DataMap)inquesObj;
			        				totQuesList.add(new Object[]{inquesDt.get("QUESTION_IDX"), 0});
			        				isInquesCell = true;
			        				if(iqIdx > 0) {
			        					cell = headRows.createCell(++ headColIdx);
							    		cell.setCellStyle(headStyle);

							            cell = headICRows.createCell(headColIdx);
							    		cell.setCellStyle(headStyle);
			        				}
						    		
				        			// 내부문항 cell
						    		cell = headIQRows.createCell(headColIdx);
						    		cell.setCellValue((iqIdx + 1) + "");
						    		cell.setCellStyle(headStyle);
			        				iqIdx ++;
			        			}
			        			
					        	if(msColICIdx < headColIdx) {
					        		// 내부분류 cell merge
					        		inclassMergeList.add(new int[]{msColICIdx, headColIdx, 0});
					        	}
			        			icIdx ++;
		        			}
		        		} else {
		        			// 내부문항 사용 안하는 경우
	        				totQuesList.add(new Object[]{quesDt.get("QUESTION_IDX"), 0});

				            cell = headICRows.createCell(headColIdx);
				    		cell.setCellStyle(headStyle);
				    		
				    		cell = headIQRows.createCell(headColIdx);
				    		cell.setCellStyle(headStyle);
				    		
		        			//createCellCnt = 1;
		        			etcitemList = StringUtil.getList(etcitemMap.get(quesIdx));
		        			
		        			if(etcitemList != null) {
			        			//int eiIdx = 0;
			        			for(Object etcitemObj:etcitemList) {
			        				DataMap etcitemDt = (DataMap)etcitemObj;
			        				totQuesList.add(new Object[]{quesDt.get("QUESTION_IDX"), etcitemDt.get("ITEM_IDX")});
			        				// 문항 cell
						            cell = headRows.createCell(++ headColIdx);
						    		cell.setCellStyle(headStyle);
	
						    		// 내부분류 cell
						    		cell = headICRows.createCell(headColIdx);
						    		cell.setCellStyle(headStyle);
						    		
						    		// 내부문항 cell
						    		cell = headIQRows.createCell(headColIdx);
						    		cell.setCellValue(etcitemDt.get("ITEM_ORDER") + " 기타의견");
						    		cell.setCellStyle(headStyle);
			        			}
		        				mergeRowIdx = rowTICIdx;
		        				isInquesCell = true;
		        			}
		        			//createCellCnt += etcitemList.size();
		        		}
		        		
		        	} else {
		        		// 주관식

	        			etcitemList = StringUtil.getList(etcitemMap.get(quesIdx));
	        			int etcitemSize = (etcitemList == null)?0:etcitemList.size();
	        			//for(eiIdx = 0 ; eiIdx < etcitemSize ; eiIdx ++) {
	        			if(etcitemList != null) {
		        			int eiIdx = 0;
		        			for(Object etcitemObj:etcitemList) {
		        				DataMap etcitemDt = (DataMap)etcitemObj;
		        				//System.out.println(etcitemObj);
		        				totQuesList.add(new Object[]{quesDt.get("QUESTION_IDX"), etcitemDt.get("ITEM_IDX")});
		        				if(eiIdx > 0) {
		        					headColIdx ++;
			        				// 문항 cell
						            cell = headRows.createCell(headColIdx);
						    		cell.setCellStyle(headStyle);
		        				}
	
					    		// 내부분류 cell
					    		cell = headICRows.createCell(headColIdx);
					    		cell.setCellStyle(headStyle);
					    		
					    		// 내부문항 cell
					    		cell = headIQRows.createCell(headColIdx);
					    		cell.setCellValue((eiIdx + 1) + "");
					    		cell.setCellStyle(headStyle);
					    		
					    		eiIdx ++;
		        			}
	        			}
	        			if(etcitemSize > 1) {
	        				mergeRowIdx = rowTICIdx;
	        				isInquesCell = true;
	        			}
	        			//createCellCnt += etcitemList.size();
		        	}
		        	
		        	//if(msColIdx < headColIdx || mergeRowIdx > rowTIdx) {
	    				// 문항 cell merge
			        	quesMergeList.add(new int[]{msColIdx, headColIdx, mergeRowIdx});
			        	//System.out.println("[" + qIdx + "]:" + msColIdx + ":" +  headColIdx+ ":" +  mergeRowIdx);
	    				//sheet.addMergedRegion(new CellRangeAddress(rowTIdx, rowTIdx, msColIdx, headColIdx));
		        	//}
		        }
	        	
		        if(!isInquesCell) {
		        	// 내부문항 cell 없는 경우
		        	sheet.removeRow(sheet.getRow(rowTIQIdx));
		        	dataRowIdx --;
		        }
		        
		        if(!isInclassCell && isInquesCell) {
        			sheet.shiftRows(rowTIQIdx, rowTIQIdx, -1);
        			dataRowIdx --;
        		} else if(!isInclassCell) {
		        	// 내부분류 cell 없는 경우
		        	sheet.removeRow(sheet.getRow(rowTICIdx));
		        	dataRowIdx --;
		        }/* else {
		        	// 내부분류 cell 있는 경우 : merge
		        	for(int[] mInfo:inclassMergeList) {
		        		sheet.addMergedRegion(new CellRangeAddress(rowTICIdx, rowTICIdx, mInfo[0], mInfo[1]));
		        	}
		        }*/
		        
				// 문항 cell merge
	        	for(int[] mInfo:quesMergeList) {
	        		int mRowIdx = mInfo[2];
	        		if(!isInquesCell && mRowIdx > 1) mRowIdx --;
	        		if(!isInclassCell && mRowIdx > 0) mRowIdx --;
		        	//System.out.println("-------------:" + mInfo[0] + ":" +  mInfo[1]+ "|" +  rowTIdx + ":" +  mRowIdx);
	        		if(mInfo[0] < mInfo[1] || rowTIdx < mRowIdx) {
	        			sheet.addMergedRegion(new CellRangeAddress(rowTIdx, mRowIdx, mInfo[0], mInfo[1]));
	        		}
	        	}
	        	if(isInclassCell) {
		        	// 내부분류 cell 있는 경우 : merge
		        	for(int[] mInfo:inclassMergeList) {
		        		sheet.addMergedRegion(new CellRangeAddress(rowTICIdx, rowTICIdx, mInfo[0], mInfo[1]));
		        	}
		        }
	        	
	        	// 구분/성명/아이디 merge
	        	for (int colIdx = 0; colIdx < columnHeadLen ; colIdx++) {
        			sheet.addMergedRegion(new CellRangeAddress(rowTIdx, dataRowIdx - 1, colIdx, colIdx));
		        }
	        	
	        	if(respList != null) {
	        		// 참여목록
	        		List<Object> resultList = null;
	        		Object respIdx = null;
	        		int itemIdx = 0;
	        		Object qitemIdx = null;
	        		int columnGubun = 0;
    				String columnVal = null;
    				int rIdx = 0;
	        		for(Object respObj:respList) {
	        			DataMap respDt = (DataMap)respObj;
	        			respIdx = respDt.get("RESP_IDX");
	        			
	        			rows = sheet.createRow((short) dataRowIdx);
	        			
	        			for (int colIdx = 0 ; colIdx < columnHeadLen ; colIdx++) {
    			        	columnGubun = columnGubuns[colIdx];

    			            cell = rows.createCell(colIdx);
    			        	switch (columnGubun) {
    							case -1:
    								columnVal = (rIdx + 1) + "";
    							    cell.setCellStyle(numStyle);
    								break;
    								
    							case 1:
    								columnVal = DataSecurityUtil.getSeedDecrypt(StringUtil.getString(respDt.get(columnNames[colIdx]), ""));
    							    cell.setCellStyle(style);
    								break;

    							default:
    								columnVal = StringUtil.getString(respDt.get(columnNames[colIdx]), "");
    							    cell.setCellStyle(style);
    								break;
    						}
    			            cell.setCellValue(columnVal);
    			        }
	        			
	        			dataColIdx = columnHeadLen;
	        			//System.out.println("-----" + totQuesList.size());
	        			for(Object[] quesObjs:totQuesList) {
	        				resultList = resultMap.get(respIdx + "," + quesObjs[0]);
	        				qitemIdx = quesObjs[1];

					        int etcItemIdx = StringUtil.getInt(qitemIdx);
		        			//System.out.println("-----" + qitemIdx + ":" + etcItemIdx);
					        int dataItemColIdx = dataColIdx;
					        if(etcItemIdx == 0) {
						        dataColIdx ++;
					        }
					        
					        boolean isEtcVal = false;
	        				columnVal = "";
	        				if(resultList != null) {
		        				String resultContents = null;
		        				int rsIdx = 0;
		        				for(Object resultObj:resultList) {
		        					DataMap resultDt = (DataMap)resultObj;
		        					itemIdx = StringUtil.getInt(resultDt.get("ITEM_IDX"));
		        					if(rsIdx > 0) columnVal += ",";
		        					columnVal += itemIdx;
		        					//System.out.println("-----" + quesObjs[0] + ":"+ resultDt);
								       
				        			//System.out.println("-----" + quesObjs[0] + ":"+ etcItemIdx + ":" + qitemIdx + ":" + itemIdx);
							        if(etcItemIdx > 0 && etcItemIdx == itemIdx) {
							        	isEtcVal = true;
								        resultContents = StringUtil.getString(resultDt.get("CONTENTS"), "");
								        cell = rows.createCell(dataColIdx);
								        cell.setCellValue(resultContents);
								        cell.setCellStyle(style);
								        dataColIdx ++;
							        }
		        					rsIdx ++;
		        				}
	        				}
					        if(etcItemIdx == 0) {
						        cell = rows.createCell(dataItemColIdx);
						        cell.setCellValue(columnVal);
						        cell.setCellStyle(style);
					        }
					        if(etcItemIdx > 0 && !isEtcVal) {
						        cell = rows.createCell(dataColIdx);
						        cell.setCellValue("");
						        cell.setCellStyle(style);
						        dataColIdx ++;
					        }
	        			}
    					rIdx ++;
        				dataRowIdx ++;
	        		}
	        	}
			}finally{ 
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("오류 :" + e);
		}finally{
		}
		
		Map<String, Object> downFileInfo = new HashMap<String, Object>();
		downFileInfo.put("workbook", workbook);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String originFileName = fileName + "_" + sdf.format(new Date()) + ".xls";
		downFileInfo.put("orgFileName", originFileName);
		return new ModelAndView("rbsExcelDownloadView", downFileInfo);
	}
	/**
	 * 참여목록
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/respList.do")
	public String respList(@RequestParam(value="pollIdx") int pollIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject moduleSetting = attrVO.getModuleSetting();
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "resp_setting_info");
		
		// 1. 필수 parameter 검사

		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		param.put("searchList", searchList);
		
		// 설문 상세정보
		dt = pollService.getRespCntView(fnIdx, param);
		if(dt == null) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int usePaging = JSONObjectUtil.getInt(settingInfo, "use_paging");
		if(usePaging == 1) {
			int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", 0, propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
			int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
			int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
			
			int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
			if(pageUnit == 0) pageUnit = listUnit;//JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("PAGE_UNIT"));	// 페이지당 목록 수
			int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", 0, propertiesService.getInt("PAGE_SIZE"));	
			
			String pageName = JSONObjectUtil.getString(settingInfo, "page_name");
			int page = StringUtil.getInt(request.getParameter(pageName), 1);				// 현재 페이지 index
	
			paginationInfo.setUnitBeginCount(listUnit);
			paginationInfo.setUnitEndCount(listMaxUnit);
			paginationInfo.setUnitStep(listUnitStep);
			paginationInfo.setCurrentPageNo(page);
			paginationInfo.setRecordCountPerPage(pageUnit);
			paginationInfo.setPageSize(pageSize);
		}

		// 2. DB
		int totalCount = 0;
		List<?> list = null;
		searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		param.put("searchList", searchList);
		// 2.2 목록수
    	totalCount = respService.getTotalCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		if(usePaging == 1) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		}
    		
    		// 2.3 목록
    		list = respService.getList(fnIdx, param);
    	}
    	
    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
    	model.addAttribute("dt", dt);																// 설문 상세 정보

    	// 5. 기본경로
    	fn_setRespPath(attrVO);
    	return getViewPath("/respList");
	}
	
	/**
	 * 참여 상세조회
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/respView.do")
	public String respView(@RequestParam(value="pollIdx") int pollIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject moduleSetting = attrVO.getModuleSetting();
		JSONObject settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "resp_setting_info");
		
		// 1. 필수 parameter 검사
		int respIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(respIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		String viewPath = super.view(1, 0, attrVO, request, model);

		// 3. 항목설정
		String submitType = "write";

		// 4. 속성 setting
		//model.addAttribute("dt", dt);															// 상세정보
		model.addAttribute("submitType", submitType);											// 페이지구분
		
    	// 5. 기본경로
		fn_setRespPath(attrVO);
		
    	// 6. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_MODIFYPROC"));
    	return viewPath;
	}
	
	/**
	 * 참여 결과 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/respResultProc.do")
	public String respResultProc(@RequestParam(value="pollIdx") int pollIdx, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		JSONObject moduleItem = attrVO.getModuleItem();
		JSONObject itemInfo = JSONObjectUtil.getJSONObject(moduleItem, "result_item_info");
		
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx = attrVO.getFnIdx();
		JSONObject moduleSetting = attrVO.getModuleSetting();
		JSONObject settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "resp_setting_info");
		
		// 1. 필수 parameter 검사
		int respIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(respIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 2. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 3. DB
    	int result = respService.resultUpdate(fnIdx, pollIdx, respIdx, request.getRemoteAddr(), parameterMap, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 4. 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "location.reload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -1) {
    		// 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert"), ""));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 의견보기
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/contList.do")
	public String contList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		String viewPath = super.contList(attrVO, request, model);
    	// 5. 기본경로
    	fn_setContListPath(attrVO);
		return viewPath;
	}

	/**
	 * 응답결과 상세조회
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/view.do")
	public String view(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		String viewPath = super.view(0, 0, attrVO, request, model);
    	// 5. 기본경로
    	fn_setCommonPath(attrVO);
		return viewPath;
	}
	
	/**
	 * 진행설문 1개 사용시 설문기간항목 필수 제외
	 * @param settingInfo
	 * @param itemInfo
	 * @param model
	 */
	/*public JSONObject getLimitDateSetItem(JSONObject settingInfo, JSONObject itemInfo, ModelMap model) {

		int useOnePoll = JSONObjectUtil.getInt(settingInfo, "use_list_noreply");
		if(useOnePoll == 1) {
			JSONObject newItemInfo = new JSONObject();
			newItemInfo.accumulateAll(itemInfo);
			JSONObject items = JSONObjectUtil.getJSONObject(newItemInfo, "items");
			JSONObject item = JSONObjectUtil.getJSONObject(items, "limitDate");
			item.put("required_write", 0);
			item.put("required_modify", 0);
			
			model.addAttribute("itemInfo", newItemInfo);
			
			return newItemInfo;
		}
		
		return itemInfo;
	}*/
	
	/**
	 * 수정
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/input.do", params="mode")
	public String input(@RequestParam(value="mode") String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 수정모드 여부 / 필수 parameter 검사
		// 1.1 수정모드 여부 검사
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;

		// 1.2 필수 parameter 검사
		int pollIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);

		if(!isModify || pollIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 진행설문 1개 사용시 설문기간항목 필수 제외
		//getLimitDateSetItem(settingInfo, itemInfo, model);
		
		// 2. DB
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A." + idxColumnName, pollIdx));
		param.put("searchList", searchList);

		// 2.1 상세정보
		dt = pollService.getModify(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		int respCnt = quesService.getRespCnt(fnIdx, param);
		
		int quesCnt = quesService.getTotalCount(fnIdx, param);
		
		List<Object> itemList = pollService.getPItemList(fnIdx, param);

		// 3. 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		// 4. 속성 setting
		model.addAttribute("dt", dt);															// 상세정보
		model.addAttribute("respCnt", respCnt);													// 응답수
		model.addAttribute("quesCnt", quesCnt);													// 문항수
		model.addAttribute("itemList", itemList);												// 항목
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분

    	// 5. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 6. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_MODIFYPROC"));
    	
		return getViewPath("/input");
	}
	
	/**
	 * 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/input.do")
	public String input(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		//JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// 진행설문 1개 사용시 설문기간항목 필수 제외
		//getLimitDateSetItem(settingInfo, itemInfo, model);
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		// 2. 속성 setting
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분

    	// 3. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 4. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
    	
		return getViewPath("/input");
	}

	/**
	 * 수정처리
	 * @param mode
	 * @param parameterMap
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do", params="mode")
	public String inputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 수정모드 여부 / 필수 parameter 검사
		// 1.1 수정모드 여부 검사
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);

		// 1.2 필수 parameter 검사
		if(!isModify || brdIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 진행설문 1개 사용시 설문기간항목 필수 제외
		//JSONObject newItemInfo = getLimitDateSetItem(settingInfo, itemInfo, model);

		// 2. 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 3. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 4. DB
    	int result = pollService.update(uploadModulePath, fnIdx, brdIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 5. 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procActionOpReload(\"" + request.getAttribute("URL_LIST") + "\");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -1) {
    		// 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 등록처리
	 * @param parameterMap
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do")
	public String inputProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		int fnIdx = attrVO.getFnIdx();
		
		// 진행설문 1개 사용시 설문기간항목 필수 제외
		//JSONObject newItemInfo = getLimitDateSetItem(settingInfo, itemInfo, model);

		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 2. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 3. DB
    	int result = pollService.insert(uploadModulePath, fnIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 4. 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procActionOpReload(\"" + request.getAttribute("URL_LIST") + "\");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -1) {
    		// 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 삭제처리
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/deleteProc.do", params="select")
	public String delete(@RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. DB
		int result = pollService.delete(fnIdx, deleteIdxs, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.delete"), "top.location.reload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.delete")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 삭제처리 : 단일 parameter
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.GET, value = "/deleteProc.do")
	public String delete(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(brdIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		String[] deleteIdxs = {brdIdx + ""};
		// 2. DB
		int result = pollService.delete(fnIdx, deleteIdxs, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.delete"), "top.location.reload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.delete")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 복원처리
	 * @param restoreIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/restoreProc.do", params="select")
	public String restore(@RequestParam(value="select") String[] restoreIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. DB
		int result = pollService.restore(fnIdx, restoreIdxs, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.restore"), "fn_procReload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.restore")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	
	/**
	 * 완전삭제처리
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="CDT")
	@RequestMapping(method=RequestMethod.POST, value = "/cdeleteProc.do", params="select")
	public String cdelete(@RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 2. DB
		int result = pollService.cdelete(uploadModulePath, fnIdx, deleteIdxs, items, itemOrder);
    	
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.cdelete"), "location.reload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.cdelete")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 삭제목록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/deleteList.do")
	public String deleteList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		int fnIdx = attrVO.getFnIdx();
		
    	// 1. 페이지정보 setting
		int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
		int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
		int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
		
		int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
		if(pageUnit == 0) pageUnit = listUnit;	// 페이지당 목록 수
		int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", propertiesService.getInt("PAGE_SIZE"));	// 페이징 크기
		int page = StringUtil.getInt(request.getParameter("page"), 1);				// 현재 페이지 index

		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(pageUnit);
		paginationInfo.setPageSize(pageSize);
		
		// 2. DB
		int totalCount = 0;
		List<?> list = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 2.1 검색조건
		
		// 항목설정으로 검색조건 setting
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, "list_search");
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoDeleteSearchList("A.LAST_MODI_DATE", deleteListSearchParams, listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		param.put("searchList", searchList);

		// 2.2 목록수
    	totalCount = pollService.getDeleteCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = pollService.getDeleteList(fnIdx, param);
    	}

    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);					// 페이징정보
    	model.addAttribute("list", list);										// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 코드
		    	
    	// 4. 휴지통 경로
    	fn_setDeleteListPath(attrVO);
    	
		return getViewPath("/deleteList");
	}
	
	/**
	 * 기본경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		PathUtil.fn_setCommonPath(queryString, baseParams, searchParams, idxName, pageName);

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		fn_setAddPath(request);
	}
	
	/**
	 * 추가 경로
	 * @param request
	 */
	public void fn_setAddPath(HttpServletRequest request) {
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		String quesListName = "quesList.do";
		String respListName = "respList.do";
		String excelDownName = "respExcelDown.do";
		String rescontListName = "contList.do";		//의견보기
		String quesListUrl = quesListName;
		String respListUrl = respListName;
		String excelDownUrl = excelDownName;
		String rescontListUrl = rescontListName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			quesListUrl += baseQueryString;
			respListUrl += baseQueryString;
			excelDownUrl += baseQueryString;
			rescontListUrl += baseQueryString;
		}

		request.setAttribute("URL_QUESLIST", quesListUrl);
		request.setAttribute("URL_RESPLIST", respListUrl);
		request.setAttribute("URL_RESEXCELDOWN", excelDownUrl);
		request.setAttribute("URL_RESCONT_LIST", rescontListUrl);
	}
	
	/**
	 * 의견목록 경로
	 * @param attrVO
	 */
	public void fn_setContListPath(ModuleAttrVO attrVO) {
		JSONObject moduleSetting = attrVO.getModuleSetting();
		JSONObject settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "cont_setting_info");
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key

		String listName = "contList.do";
		String[] baseParams = StringUtil.insertStringArrays(this.baseParams, new String[]{"pollIdx", "quesIdx", "itemIdx"});													// 기본 parameter
		PathUtil.fn_setListPath(queryString, baseParams, null, pageName, listName);
		
	}
	public void fn_setRespPath(ModuleAttrVO attrVO) {
		JSONObject moduleSetting = attrVO.getModuleSetting();
		JSONObject settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "resp_setting_info");
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] baseParams = StringUtil.insertStringArrays(this.baseParams, new String[]{"pollIdx"});
		String[] searchParams = null;
		String attrFlag = null;
		String listName = "respList.do";
		String viewName = "respView.do";
		PathUtil.fn_setListViewPath(queryString, baseParams, searchParams, attrFlag, pageName, idxName, listName, viewName);
		
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String allQueryString = StringUtil.getString(request.getAttribute("allQueryString"));
		String inputProcName = "respResultProc.do";
		String inputProctUrl = inputProcName;
		if(!StringUtil.isEmpty(allQueryString)) {
			inputProctUrl += allQueryString;
		}

		request.setAttribute("URL_RESULTINPUTPROC", inputProctUrl);
		
	}

	/**
	 * 휴지통 경로
	 */
	public void fn_setDeleteListPath(ModuleAttrVO attrVO) {

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");			// 목록 페이징  key

		// 항목 설정 정보
		String[] searchParams = StringUtil.concatenateStringArrays(PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search")), deleteListSearchParams);	// 검색 항목
		PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, pageName);
	}
}
