package rbs.egovframework.web;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;












import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.FileUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;


@Controller
@RequestMapping({"/{admSiteId}/menuContents/{usrSiteId}/editor", "/{siteId}/editor"})
public class EditorController {

	@Value("${Globals.rbs.jsp.path}")
	protected String rbsJspPath;													// rbis jsp 경로
	/*protected String moduleId;											// 모듈 id
	protected int fnIdx;												// 모듈 fnIdx*/


	/** EgovMessageSource */
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	public boolean checkController(HttpServletRequest request, HttpServletResponse response, JSONObject crtMenu) throws Exception {
    	// 모듈 설정 정보
		String moduleId = JSONObjectUtil.getString(crtMenu, "module_id");
	    int thisFnIdx = JSONObjectUtil.getInt(crtMenu, "fn_idx");
		int paramFnIdx = StringUtil.getInt(request.getParameter("fnIdx"));
		if(paramFnIdx > 0) thisFnIdx = paramFnIdx;

		//request.setAttribute("moduleId", moduleId);
		//request.setAttribute("fnIdx", thisFnIdx);
		
		ModuleAttrVO moduleAttrVO = new ModuleAttrVO();
		moduleAttrVO.setModuleId(moduleId);
		moduleAttrVO.setFnIdx(thisFnIdx);

		request.setAttribute("moduleAttrVO", moduleAttrVO);
		
		return true;
	}

	@RequestMapping("/photoUpload.do")
	public String upload(ModelMap model){
		return rbsJspPath + "/editor/photo_uploader";
	}
	//@RequestMapping(/*method=RequestMethod.POST, */value = "/taa.do")

	@RequestMapping("/photoUploadProc.do")
	public String uploadProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		String callback = null;
		String callback_func = null;
		String url = "";

		String contextPath = PathUtil.getContextPath();
    	callback = StringUtil.getString(parameterMap.get("callback"));
    	callback_func = StringUtil.getString(parameterMap.get("callback_func"));
    	
		Object multipartNameObjs = parameterMap.get("multipartFiles");
		Iterator<String> multipartFileNames = null;
		if(multipartNameObjs instanceof Iterator) {
			multipartFileNames = (Iterator<String>)multipartNameObjs;
			
			String originFileName = null;
			String savedFileName = null;
			int fileNameIdx = 1;
			if(multipartFileNames != null) {

				//boolean fileError = false;
				List extStr = new ArrayList();
		        extStr.add(("1," + RbsProperties.getProperty("Globals.image.upload.file")).split(","));				// 첨부가능/불가능 파일확장자 setting
				long totalMaxFileSize = RbsProperties.getPropertyInt("Globals.upload.maximum.filesize");				// 전체 파일업로드 최대크기
				int totalFileSize = 0;

				String imgRootPath = RbsProperties.getProperty("Globals.image.domain.path");
				if(StringUtil.isEmpty(imgRootPath)) imgRootPath = contextPath + RbsProperties.getProperty("Globals.download.file.path");
				
				String downloadPath = imgRootPath + "/editor";
				String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + "editor";
				String fileItemId = null;
				while(multipartFileNames.hasNext()) {
					fileItemId = StringUtil.getString(multipartFileNames.next());
					
					// 파일업로드 가능 파일 체크
					// 파일업로드 용량 체크
					// 파일저장
					Object multipartObj = parameterMap.get(fileItemId);
					if(multipartObj == null) continue;
					if(multipartObj instanceof List) {
						// 다중
						List<MultipartFile> multipartFileList = StringUtil.getList(parameterMap.get(fileItemId));
						if(multipartFileList == null) continue;
						
						for (final MultipartFile multipartFile : multipartFileList) {
							originFileName = multipartFile.getOriginalFilename();
							if(StringUtil.isEmpty(originFileName)) continue;
							
							long fileSize = multipartFile.getSize();
							totalFileSize += fileSize;
							
							// 파일체크 
							if(!FileUtil.isUploadable(originFileName, extStr)) {
								// 파일업로드 가능 파일 체크
						    	//url += "&errstr=" + URLEncoder.encode(originFileName, "UTF-8");
						    	//fileError = true;
							} else if(totalMaxFileSize > 0 && totalFileSize > totalMaxFileSize) {
								// 전체 파일업로드 용량 체크
						    	//fileError = true;
							} else {
								
								savedFileName = FileUtil.getSavedFileName(originFileName, fileNameIdx ++);
								FileUtil.getSavedFile(fileRealPath, savedFileName, multipartFile, false, 0, 0);
								
								url += "&bNewLine=true";
								url += "&sFileName=" + URLEncoder.encode(originFileName, "UTF-8");
								url += "&sFileURL=" + downloadPath + "/" + URLEncoder.encode(savedFileName, "UTF-8");
							}

						}
	
					} else {
						// 단일
						MultipartFile multipartFile = (MultipartFile)parameterMap.get(fileItemId);
						originFileName = multipartFile.getOriginalFilename();
						if(StringUtil.isEmpty(originFileName)) continue;
						
						long fileSize = multipartFile.getSize();
						totalFileSize += fileSize;
						
						// 파일체크 
						if(!FileUtil.isUploadable(originFileName, extStr)) {
							// 파일업로드 가능 파일 체크
					    	//url += "&errstr=" + URLEncoder.encode(originFileName, "UTF-8");
					    	//fileError = true;
						} else if(totalMaxFileSize > 0 && totalFileSize > totalMaxFileSize) {
							// 전체 파일업로드 용량 체크
					    	//fileError = true;
						} else {
							
							savedFileName = FileUtil.getSavedFileName(originFileName, fileNameIdx ++);
							FileUtil.getSavedFile(fileRealPath, savedFileName, multipartFile, false, 0, 0);
							
							url += "&bNewLine=true";
							url += "&sFileName=" + URLEncoder.encode(originFileName, "UTF-8");
							url += "&sFileURL=" + downloadPath + "/" + URLEncoder.encode(savedFileName, "UTF-8");
						}
					}
				}
			}
		}

    	String reUrl = callback + "?callback_func=" + callback_func + url;
    	
		model.addAttribute("message", MessageUtil.getLocationHref(false, reUrl));
		return RbsProperties.getProperty("Globals.message.path");
	}
}
