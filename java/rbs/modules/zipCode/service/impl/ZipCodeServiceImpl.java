package rbs.modules.zipCode.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.stereotype.Service;

import rbs.modules.zipCode.service.ZipCodeService;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.JSONObjectUtil;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * 우편번호관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
@Service("zipCodeService")
public class ZipCodeServiceImpl extends EgovAbstractServiceImpl implements ZipCodeService {

	/**
	 * 공공데이터포털 새우편번호 도로명주소조회 서비스
	 * @param searchSe
	 * @param srchwrd
	 * @param countPerPage
	 * @param currentPage
	 * @return
	 */
	public JSONObject getOpenApiEpostXmlData(String searchSe, String srchwrd, int countPerPage, int currentPage) {
		String url = RbsProperties.getProperty("Globals.address.search.epost.url");
		try{
			url += "&srchwrd=" + URLEncoder.encode(srchwrd, "UTF-8");
		}catch(UnsupportedEncodingException e){}
		
		url += "&countPerPage=" + countPerPage;
		if(currentPage > 0)	url += "&currentPage=" + currentPage;
		
		JSONObject jsonObject = null;
		HttpClient client = new HttpClient();
		HttpMethod post = null;

		try{
			client = new HttpClient(new MultiThreadedHttpConnectionManager());
			client.getParams().setParameter("http.useragent", "Mozilla/4.0");
			post = new GetMethod(url);

			// timeout 설정
			HttpMethodParams hmp = new HttpMethodParams();
			hmp.setSoTimeout(1000*10);
			post.setParams(hmp);
			int httpResultCode = client.executeMethod(post);

			if(httpResultCode == HttpStatus.SC_OK){
				BufferedReader br = null;
				InputStream in = null;
			
				try{
					in = post.getResponseBodyAsStream();
					br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
					
				    StringBuffer xmlStr = new StringBuffer();
				    String line = null;
			
					try{
					    while ((line = br.readLine()) != null) {
					    	xmlStr.append(line);
					    }
					}catch(Exception e){
						e.printStackTrace();
					}
					Object object = new XMLSerializer().read(xmlStr.toString());
					try {
						jsonObject = JSONObjectUtil.getJSONObject(object);
					} catch(Exception e) {}
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("++++ : error > " + e.getMessage());
				}finally{
					// inputstream close
					if(in != null) in.close();
				}
			}else{
				// 요청 실패 처리
			}
		}catch(Exception e){
		// post 요청 오류 처리
		}finally{
			// post close
			if(post != null) post.releaseConnection();
		}
		//return jsonArray;
		return jsonObject;
	}

	public JSONObject getOpenApiJusoJsonData(String srchwrd, int countPerPage, int currentPage) {
		String url = RbsProperties.getProperty("Globals.address.search.juso.url");
		try{
			url += "&keyword=" + URLEncoder.encode(srchwrd, "UTF-8");
		}catch(UnsupportedEncodingException e){}
		
		url += "&countPerPage=" + countPerPage;
		if(currentPage > 0)	url += "&currentPage=" + currentPage;
		
		JSONObject jsonObject = null;
		HttpClient client = new HttpClient();
		HttpMethod post = null;

		try{
			client = new HttpClient(new MultiThreadedHttpConnectionManager());
			client.getParams().setParameter("http.useragent", "Mozilla/4.0");
			post = new GetMethod(url);

			// timeout 설정
			HttpMethodParams hmp = new HttpMethodParams();
			hmp.setSoTimeout(1000*10);
			post.setParams(hmp);
			int httpResultCode = client.executeMethod(post);

			if(httpResultCode == HttpStatus.SC_OK){
				BufferedReader br = null;
				InputStream in = null;
			
				try{
					in = post.getResponseBodyAsStream();
					br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
					
				    StringBuffer xmlStr = new StringBuffer();
				    String line = null;
			
					try{
					    while ((line = br.readLine()) != null) {
					    	xmlStr.append(line);
					    }
					}catch(Exception e){
						e.printStackTrace();
					}
					Object object = new XMLSerializer().read(xmlStr.toString());
					try {
						jsonObject = JSONObjectUtil.getJSONObject(object);
					} catch(Exception e) {}
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("++++ : error > " + e.getMessage());
				}finally{
					// inputstream close
					if(in != null) in.close();
				}
			}else{
				// 요청 실패 처리
			}
		}catch(Exception e){
		// post 요청 오류 처리
		}finally{
			// post close
			if(post != null) post.releaseConnection();
		}
		//return jsonArray;
		return jsonObject;
	}
}