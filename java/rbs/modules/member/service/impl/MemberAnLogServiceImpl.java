package rbs.modules.member.service.impl;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.DateUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;

import rbs.egovframework.util.MailUtil;
import rbs.modules.member.mapper.MemberAnLogMapper;
import rbs.modules.member.service.MemberAnLogService;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 */
@Service("memberAnLogService")
public class MemberAnLogServiceImpl extends EgovAbstractServiceImpl implements MemberAnLogService {

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@Resource(name="memberAnLogMapper")
	private MemberAnLogMapper memberLogDAO;
	/**
     * 전체 목록 수
	 * @param mapFlag
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(Map<String, Object> param) {
    	return memberLogDAO.getTotalCount(param);
    }

	/**
	 * 전체 목록
	 * @param mapFlag
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param) {
		return memberLogDAO.getList(param);
	}
	
	@Override
	public void setEprivacy(Logger logger, String logType, DataMap dt, String changePw, JSONObject items, String memberIdx, String regiIdx, String dbRegiId, String dbRegiName) throws Exception{
		if(StringUtil.isEmpty(memberIdx) || StringUtil.isEmpty(regiIdx)) return;

		JSONObject mbrIdItem = JSONObjectUtil.getJSONObject(items, "mbrId");				// mbrId 항목 설정 정보
		JSONObject mbrNameItem = JSONObjectUtil.getJSONObject(items, "mbrName");			// mbrName 항목 설정 정보
		JSONObject mbrEmailItem = JSONObjectUtil.getJSONObject(items, "mbrEmail");			// mbrEmail 항목 설정 정보
		JSONObject mobilePhoneItem = JSONObjectUtil.getJSONObject(items, "mobilePhone");	// mobilePhone 항목 설정 정보

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.MEMBER_IDX", memberIdx));
		param.put("searchList", searchList);
		
		if(dt == null) dt = memberLogDAO.getView(param);
		
		if(dt == null) return;

		String regiDateFormat = "yyyy년 MM월 dd일";
		String regiDateFormat1 = "yyyy-MM-dd HH:mm:ss";
		
		String columnIdKey = "column_id";
		String mbrIdColumn = JSONObjectUtil.getString(mbrIdItem, columnIdKey);
		String mbrNameColumn = JSONObjectUtil.getString(mbrNameItem, columnIdKey);
		String mbrEmailColumn = JSONObjectUtil.getString(mbrEmailItem, columnIdKey);
		String mobilePhoneColumn = JSONObjectUtil.getString(mobilePhoneItem, columnIdKey);
		String mobilePhoneColumn1 = mobilePhoneColumn + "1";
		String mobilePhoneColumn2 = mobilePhoneColumn + "2";
		String mobilePhoneColumn3 = mobilePhoneColumn + "3";
		String dbMbrId = StringUtil.getString(dt.get(mbrIdColumn));
		String dbMbrName = StringUtil.getString(dt.get(mbrNameColumn));
		String dbMbrEmail = StringUtil.getString(dt.get(mbrEmailColumn));
		String dbMobilePhone1 = StringUtil.getString(dt.get(mobilePhoneColumn1));
		String dbMobilePhone2 = StringUtil.getString(dt.get(mobilePhoneColumn2));
		String dbMobilePhone3 = StringUtil.getString(dt.get(mobilePhoneColumn3));
		String mbrId = ModuleUtil.getPrivDecValue(mbrIdItem, dbMbrId);
		String mbrName = ModuleUtil.getPrivDecValue(mbrNameItem, dbMbrName);
		String mbrEmail = ModuleUtil.getPrivDecValue(mbrEmailItem, dbMbrEmail);
		String mobilePhone1 = ModuleUtil.getPrivDecValue(mobilePhoneItem, dbMobilePhone1);
		String mobilePhone2 = ModuleUtil.getPrivDecValue(mobilePhoneItem, dbMobilePhone2);
		String mobilePhone3 = ModuleUtil.getPrivDecValue(mobilePhoneItem, dbMobilePhone3);
		String mbrRegiDate = DateUtil.getTimestampFormat((Timestamp)dt.get("REGI_DATE"), regiDateFormat);
		int usertypeIdx = StringUtil.getInt(dt.get("USERTYPE_IDX"));

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		JSONObject siteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("siteInfo"));
		String siteName = JSONObjectUtil.getString(siteInfo, "site_name", "");
		String siteUrl = JSONObjectUtil.getString(siteInfo, "site_domain", "") + request.getContextPath() + JSONObjectUtil.getString(siteInfo, "local_path", "");
		String siteCopyright = JSONObjectUtil.getString(siteInfo, "site_copyright", "");

		Date dbRegiDate = new Date();
		String regiDate = DateUtil.getThisDate(regiDateFormat);
		String regiIp = request.getRemoteAddr();
		String regiId = ModuleUtil.getPrivDecValue(mbrIdItem, dbRegiId);
		String regiName = ModuleUtil.getPrivDecValue(mbrNameItem, dbRegiName);
		
		boolean sendMail = false;
		String mailTitle = null;
		String mailContent = null;
		
		if(StringUtil.isEquals(logType, rbsMessageSource.getMessage("message.member.log.insert"))){
			dbRegiDate = (Date)dt.get("REGI_DATE");
			regiDate = DateUtil.getTimestampFormat((Timestamp)dt.get("REGI_DATE"), regiDateFormat1);
			sendMail = true;
			mailTitle = MessageFormat.format(RbsProperties.getProperty("Globals.mail.join.title"), new String[]{mbrName});
			mailContent = MessageFormat.format(RbsProperties.getProperty("Globals.mail.join.content"), new String[]{mbrName, mbrId, mbrRegiDate, siteName, siteUrl, siteCopyright});
		}
		else if(StringUtil.isEquals(logType, rbsMessageSource.getMessage("message.member.log.modify"))){
			dbRegiDate = (Date)dt.get("LAST_MODI_DATE");
			regiDate = DateUtil.getTimestampFormat((Timestamp)dt.get("LAST_MODI_DATE"), regiDateFormat1);
			sendMail = true;
			mailTitle = MessageFormat.format(RbsProperties.getProperty("Globals.mail.myinfo.title"), new String[]{mbrName});
			mailContent = MessageFormat.format(RbsProperties.getProperty("Globals.mail.myinfo.content"), new String[]{mbrName, mbrId, regiDate, mbrRegiDate, siteName, siteUrl, siteCopyright});
		}
		else if(StringUtil.isEquals(logType, rbsMessageSource.getMessage("message.member.log.delete"))){
			sendMail = true;
			mailTitle = MessageFormat.format(RbsProperties.getProperty("Globals.mail.joinout.title"), new String[]{mbrName, siteName});
			mailContent = MessageFormat.format(RbsProperties.getProperty("Globals.mail.joinout.content"), new String[]{mbrName, mbrId, regiDate, mbrRegiDate, siteName, siteUrl, siteCopyright});
		}
		else if(StringUtil.isEquals(logType, rbsMessageSource.getMessage("message.member.log.idsearch"))){
			sendMail = true;
			mailTitle = MessageFormat.format(RbsProperties.getProperty("Globals.mail.id.title"), new String[]{mbrName});
			mailContent = MessageFormat.format(RbsProperties.getProperty("Globals.mail.id.content"), new String[]{mbrName, mbrId, regiDate, mbrRegiDate, siteName, siteUrl, siteCopyright});
		}
		else if(StringUtil.isEquals(logType, rbsMessageSource.getMessage("message.member.log.pwsearch"))){
			dbRegiDate = (Date)dt.get("PWD_MODI_DATE");
			regiDate = DateUtil.getTimestampFormat((Timestamp)dt.get("PWD_MODI_DATE"), regiDateFormat1);
			sendMail = true;
			mailTitle = MessageFormat.format(RbsProperties.getProperty("Globals.mail.password.title"), new String[]{mbrName});
			mailContent = MessageFormat.format(RbsProperties.getProperty("Globals.mail.password.content"), new String[]{mbrName, mbrId, changePw, regiDate, mbrRegiDate, siteName, siteUrl, siteCopyright});
		}
		
		if(RbsProperties.getPropertyInt("Globals.memberAn.log.use.db") == 1){
			long logIdx = memberLogDAO.getNextIdx(param);
			dataList.add(new DTForm("LOG_IDX", logIdx));
			dataList.add(new DTForm("MEMBER_IDX", memberIdx));
			dataList.add(new DTForm(mbrIdColumn, dbMbrId));
			dataList.add(new DTForm(mbrNameColumn, dbMbrName));
			dataList.add(new DTForm(mbrEmailColumn, dbMbrEmail));
			dataList.add(new DTForm(mobilePhoneColumn1, dbMobilePhone1));
			dataList.add(new DTForm(mobilePhoneColumn2, dbMobilePhone2));
			dataList.add(new DTForm(mobilePhoneColumn3, dbMobilePhone3));
			dataList.add(new DTForm("USERTYPE_IDX", usertypeIdx));
			dataList.add(new DTForm("LOG_TYPE", logType));
			dataList.add(new DTForm("REGI_IDX", regiIdx));
			dataList.add(new DTForm("REGI_ID", dbRegiId));
			dataList.add(new DTForm("REGI_NAME", dbRegiName));
			dataList.add(new DTForm("REGI_DATE", dbRegiDate));
			dataList.add(new DTForm("REGI_IP", regiIp));
			
			param.put("dataList", dataList);
			
			memberLogDAO.insert(param);
		}else{
			logger.info(memberIdx + ", " + dbMbrId + ", " + dbMbrName + ", " + dbMbrEmail + ", " + dbMobilePhone1 + ", " + dbMobilePhone2 + ", " + dbMobilePhone3 + ", " + usertypeIdx + ", " + logType + ", " + regiIdx + ", " + dbRegiId + ", " + dbRegiName + ", " + regiDate + ", " +  regiIp);
		}

		if(sendMail && StringUtil.isEquals(memberIdx, regiIdx))
			MailUtil.sendMail(RbsProperties.getProperty("Globals.mail.fromMail"), RbsProperties.getProperty("Globals.mail.fromName"), mbrEmail, mbrName, mailTitle, mailContent);
	}
}