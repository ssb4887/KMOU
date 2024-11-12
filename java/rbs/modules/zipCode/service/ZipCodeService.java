package rbs.modules.zipCode.service;


import net.sf.json.JSONObject;


/**
 * 우편번호관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface ZipCodeService {
	
	/**
	 * 공공데이터포털 새우편번호 도로명주소조회 서비스
	 * @param searchSe
	 * @param srchwrd
	 * @param countPerPage
	 * @param currentPage
	 * @return
	 */
	public JSONObject getOpenApiEpostXmlData(String searchSe, String srchwrd, int countPerPage, int currentPage);
	
	public JSONObject getOpenApiJusoJsonData(String srchwrd, int countPerPage, int currentPage);
}