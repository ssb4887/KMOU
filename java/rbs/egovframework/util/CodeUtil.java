package rbs.egovframework.util;

import java.util.List;

import javax.annotation.Resource;

import rbs.modules.code.serviceOra.CodeOptnServiceOra;

public class CodeUtil {
	public static final String PRIMITIVE_UP_CD = "UAA0";
	public static final String UNIV_UP_CD = "0000587";
	
	@Resource(name="CodeOptnServiceOra")
	private static CodeOptnServiceOra codeOptnServiceOra;
	
	public static List<Object> getHaksaCode(String upCode) throws Exception {
		return codeOptnServiceOra.getHaksaCode(upCode);
	}
}