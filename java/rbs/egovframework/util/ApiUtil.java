package rbs.egovframework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowonsoft.egovframework.util.StringUtil;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import rbs.modules.search.web.SearchController;

@Slf4j
public class ApiUtil {
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";

	// searchStudio 검색 로그 쌓는 APi
	public static final String SEARCH_LOG_URL = "http://117.16.191.140:11000/api/v1/service/log/insert";
	public static final String POPULAR_KEYWORD_URL = "http://117.16.191.140:8080/devcenter/management/top-keyword";
	public static final String RELATED_KEYWORD_URL = "http://117.16.191.140:11000/api/v1/decorator/keyword/related";

	@SuppressWarnings("finally")
	public static String getRestApi(String apiUrl, String endpoint, String method, Object param) throws Exception {
		StringBuffer sb = new StringBuffer();
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String paramStr = objectMapper.writeValueAsString(param);
			URL url = new URL(apiUrl + endpoint);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(StringUtil.getString(method, METHOD_GET));
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			PrintWriter postReq = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			postReq.write(paramStr);
			postReq.flush();
			
			System.out.println("url : " + url);
			System.out.println("paramStr : " + paramStr);
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			String output;			
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}

			conn.disconnect();			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			sb.append("");
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			sb.append("");
			throw e;
		} finally {
			return sb.toString();
		}
	}
	

	public static CompletableFuture<String> getAiRecommendApi(String apiUrl, String endpoint, String method, Object param){
		return CompletableFuture.supplyAsync(() -> {
		    StringBuilder sb = new StringBuilder();
		    
//			StopWatch stopWatch = new StopWatch();
//	        stopWatch.start("@@@@@@@@@@@@@@@ getAiRecommendApi start");
		    
		    try {
		        ObjectMapper objectMapper = new ObjectMapper();
		        String paramStr = objectMapper.writeValueAsString(param);
		        URL url = new URL(apiUrl + endpoint);
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod(StringUtil.getString(method, METHOD_GET));
		        conn.setRequestProperty("Accept", "*/*");
		        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
		        conn.setDoInput(true);
		        conn.setDoOutput(true);
		        
		        try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8")) {
		            writer.write(paramStr);
		            writer.flush();
		        }
		        
		        if (conn.getResponseCode() != 200) {
		            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		        }
	
		        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
		            String output;
		            while ((output = br.readLine()) != null) {
		                sb.append(output);
		            }
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		        throw new RuntimeException(e);
		    }
		    
//			stopWatch.stop();
//	        log.debug("[TimeCheck][{}][{}]", stopWatch.getLastTaskInfo().getTaskName(), DurationFormatUtils.formatDurationHMS(stopWatch.getLastTaskInfo().getTimeMillis()));
		    
		    return sb.toString();
		});

	}

	public static void getRestApiAsync(String remoteURL, String method, Object param) throws Exception {
        // CompletableFuture.runAsync(() -> {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String paramStr = objectMapper.writeValueAsString(param);
			URL url = new URL(remoteURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(StringUtil.getString(method, METHOD_GET));
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setDoOutput(true);
			
			PrintWriter postReq = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			postReq.write(paramStr);
			postReq.flush();

			System.out.println("paramStr : " + paramStr);

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			
			throw e;
		}
	}

	
/*-------------------------------------------하단 코드는 사용하지 않는 코드(추후에 재사용할 여지가 있기때문에 지우진 않았음)-------------------------------------------------------------------------*/
	
	// 인기 검색어
	public static String popularKeyword(Map<String,Object> param) throws Exception {
		StringBuffer sb = new StringBuffer();
		HttpURLConnection conn = null;

		try {
			String urlWihtParam = POPULAR_KEYWORD_URL;
			urlWihtParam += "?cnt=" + param.get("cnt");
			
			System.out.println("urlWihtParam : " + urlWihtParam);
			
			URL url = new URL(urlWihtParam);
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setRequestProperty("Accept", "application/json;");

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				System.out.println("인기 검색어 가져오기 성공!!!!! : " + conn.getResponseCode());
				
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
				String line = "";
				
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				
				br.close();
			} else {
				System.out.println("인기검색어 가져오기 실패............. : " + conn.getResponseCode());
				
				try (InputStreamReader ir = new InputStreamReader(conn.getErrorStream()); BufferedReader br = new BufferedReader(ir)) {
					String line = "";
					
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
					
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println(conn.getResponseCode());
			System.out.println(conn.getResponseMessage());
			System.out.println(sb);
			System.out.println("####################################################################################");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			
			throw e;
		}

		return sb.toString();
	}
		
	// 연관 검색어
	public static String relatedKeyword(Map<String,Object> relatedParam) throws Exception {
		StringBuffer sb = new StringBuffer();
		HttpURLConnection conn = null;
		String apiParam = RELATED_KEYWORD_URL;
		
		if ("" != relatedParam.get("antecedent") && null != relatedParam.get("antecedent")) {
			apiParam += "?antecedent=" + URLEncoder.encode((String)relatedParam.get("antecedent"));
		}
		
		apiParam += "&limit=" + relatedParam.get("limit");
		apiParam += "&time_type=" + relatedParam.get("time_type");
		
		try {
			System.out.println(">>>>>>>>>>>>>>strParam : " + apiParam);
			
			URL url = new URL(apiParam);
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setRequestProperty("Accept", "application/json;");

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				System.out.println("연관검색어 조회 성공!!!!! : " + conn.getResponseCode());
				
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
				String line = "";
				
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				
				br.close();
			} else {
				System.out.println("연관검색어 조회 실패............. : " + conn.getResponseCode());
				
				try (InputStreamReader ir = new InputStreamReader(conn.getErrorStream()); BufferedReader br = new BufferedReader(ir)) {
					String line = "";
					
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
					
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println(conn.getResponseCode());
			System.out.println(conn.getResponseMessage());
			System.out.println(sb);
			System.out.println("####################################################################################");
			System.out.println("####################################################################################");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			
			throw e;
		}

		return sb.toString();
	}
}