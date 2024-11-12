<%@page import="java.io.InputStream"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.bouncycastle.asn1.cmp.RevReqContent"%>
<%@page import="java.net.URLEncoder"%>
<%@ page import="com.woowonSoft.framework.util.StringUtil,com.woowonSoft.framework.util.FileUploadUtil"%><%@ page import="java.text.MessageFormat"%><%@ page import="org.apache.commons.fileupload.FileItem"%><%@ page import="java.util.ArrayList"%><%@ page import="java.io.File"%><%@ page import="org.apache.commons.fileupload.FileUploadException"%><%@ page import="rbs.util.CommonUtil"%><%@ page import="org.apache.commons.fileupload.FileUploadBase"%><%@ page import="org.apache.commons.fileupload.DiskFileUpload"%><%@ page import="java.util.List"%><%@ page import="rbs.Defines"%>
<% 
String sFileInfo = "";
//System.out.println("-sfidx:" + request.getHeader("file-idx"));
//System.out.println("-header contentType:" + request.getHeader("contentType"));
//System.out.println("-header file-Type:" + request.getHeader("file-Type"));
//System.out.println("-header file_name:" + request.getHeader("file-name"));
//System.out.println("-header file_size:" + request.getHeader("file-size"));
//System.out.println("-header file_content:" + request.getInputStream());

// 우원 cms customizing : 2015.04.27
int sfidx = StringUtil.getInt(request.getHeader("file-idx"), 0);		// 파일번호 : 번호 안 넘기는 경우 저장되는 시간차 없어 같은 파일명으로 저장됨 
String contentsType = request.getHeader("contentType");	
String fileType = request.getHeader("file-Type");
String fileSize = request.getHeader("file-size");
String originName = request.getHeader("file-name");						// 원본파일명
InputStream is = request.getInputStream();								// 파일내용 : 위치 이동 금지!!
String savedName = FileUploadUtil.getSavedFileName(originName, sfidx);	// 저장파일명
String savedPath = null;
List extStr = new ArrayList();
extStr.add((Defines.DEFAULT_DATA_FILE_USE_FILTEREXT + "," + Defines.DEFAULT_DATA_FILE_FILTEREXT).split(","));	// 업로드 가능/불가능 확장자

int menuIdx = StringUtil.getInt(request.getParameter("menuIdx"));
String filePath = Defines.PATH_FILE_SERV;// + "/member/1/editor";
if(menuIdx > 0) {
	filePath = Defines.PATH_TCLUB_FILE;
	int fnIdx = StringUtil.getInt(request.getParameter("fnIdx"));
	int id = StringUtil.getInt(request.getParameter("moduleIdx"));
	//System.out.println("--- filePath2:" + filePath);
	if(fnIdx > 0 && id > 0 && menuIdx > 0) {
		
		filePath += "/" + fnIdx + "/contents/" + id + "/" + menuIdx;
	}
} else {
	filePath = Defines.PATH_FILE_SERV;
	//System.out.println("--- filePath1:" + filePath);
	String moduleIdx = request.getParameter("moduleIdx");
	String fnIdx = request.getParameter("fnIdx");
	//System.out.println("--- filePath2:" + filePath);
	if(!StringUtil.isNull(moduleIdx) && !StringUtil.isNull(fnIdx)) {
		moduleIdx = moduleIdx.replaceAll("/", "");
		fnIdx = fnIdx.replaceAll("/", "");
		moduleIdx = moduleIdx.replaceAll("[.]", "");
		fnIdx = fnIdx.replaceAll("[.]", "");
		
		filePath += "/" + moduleIdx + "/" + fnIdx + "/editor";
	}
}

String realPath = FileUploadUtil.getRealPath(session, filePath);	// 실제 저장될 로컬경로

if(!FileUploadUtil.isUploadable(originName, extStr)) {
	// 파일업로드 할 수 없는 확장자인 경우
	out.println("NOTALLOW_" + originName);
} else {
	// 파일업로드 할 수 있는 확장자인 경우  : 파일 저장
	File dir = new File(realPath);
    if(!dir.isDirectory() && !dir.exists())
    	dir.mkdirs();
	
    File file = new File(realPath, savedName);
	byte b[] = new byte[1024];
	
	BufferedInputStream Bfin = new BufferedInputStream(is);
	BufferedOutputStream outs = new BufferedOutputStream(new FileOutputStream(file)); 
	
	int read = 0; 
	while ((read = Bfin.read(b)) != -1){ 
		outs.write(b,0,read); 
	} 
	
	if(outs != null) try { outs.close(); } catch(Exception e) {}; 
	if(Bfin != null) try { Bfin.close(); } catch(Exception e) {};

	// 돌려주는 값
	//sFileInfo += "&sfidx=" + sfidx;
	sFileInfo += "&bNewLine=true";
	sFileInfo += "&sFileName=" + savedName;
	sFileInfo += "&sFileURL=" + request.getContextPath() + filePath + "/" + savedName;
	out.print(sFileInfo);
}
%>