package rbs.modules.member.web;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.StringUtil;

@Component("loginValidator")
public class LoginValidator implements SmartValidator {

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		validate(target, errors, null);
	}

	@Override
	public void validate(Object target, Errors errors,
			Object... validationHints) {
		// TODO Auto-generated method stub
		ParamForm parameterMap = (ParamForm)target;
		
		JSONObject itemInfo = null;
		if (validationHints != null) {
			for (Object hint : validationHints) {
				if (hint instanceof JSONObject) {
					itemInfo = (JSONObject) hint;
				}
			}
		}
		
		if(itemInfo != null) {
			JSONObject loginItems = JSONObjectUtil.getJSONObject(itemInfo, "items");
			JSONArray loginOrder = JSONObjectUtil.getJSONArray(itemInfo, "login_order");
			if(loginOrder != null) {
				
				int chkSize = 0;
				String chkItemId = null;
				for (Object itemIdObj : loginOrder) {
					if (itemIdObj instanceof String) {
						String itemId = (String)itemIdObj;
						if(StringUtil.isEmpty(parameterMap.getString(itemId))) {
							if(chkSize == 0) chkItemId = itemId;
							chkSize ++;
						}
					}
				}
				if(chkSize > 0) {
					int loginOrderSize = loginOrder.size();
					if(chkSize == loginOrderSize) {
						errors.reject("all", rbsMessageSource.getMessage("message.logIn.no.info"));
						return;
					} else if(!StringUtil.isEmpty(chkItemId)){
						JSONObject loginItem = JSONObjectUtil.getJSONObject(loginItems, chkItemId);
						String itemName = JSONObjectUtil.getString(loginItem, "item_name");
						errors.reject(chkItemId, new String[]{itemName}, rbsMessageSource.getMessage("errors.required"));
						return;
					}
				}
			}
		}		
	}


}
