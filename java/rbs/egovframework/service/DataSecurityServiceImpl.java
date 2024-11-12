package rbs.egovframework.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.service.DataSecurityService;
import com.woowonsoft.egovframework.util.SEEDUtil;
import com.woowonsoft.egovframework.util.StringUtil;

public class DataSecurityServiceImpl implements DataSecurityService {

	/**
	 * 비밀번호 암호화 : SHA-256
	 * @param password
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getPassEncrypt(String password, String id) {
		if (password == null) return "";
		byte[] hashValue = null; // 해쉬값

		try {
			int useSecurity = RbsProperties.getPropertyInt("Globals.password.use.digest", 0);
	    	
			// 사용하지 않는 경우
	    	if(useSecurity == 0) return password;

			// 암호화
			String algorithm = RbsProperties.getProperty("Globals.password.digest");
			String encode = RbsProperties.getProperty("Globals.page.encode");
			int useOld = RbsProperties.getPropertyInt("Globals.password.use.old");
			if(StringUtil.isEmpty(algorithm)) algorithm = "SHA-256";
			if(StringUtil.isEmpty(encode)) encode = "UTF-8";
			MessageDigest md = MessageDigest.getInstance(algorithm);
			try {
				if(useOld == 0) {
					md.reset();
					md.update(id.getBytes(encode));
					hashValue = md.digest(password.getBytes());
					return new String(Base64.encodeBase64(hashValue));
				} else {
					hashValue = md.digest(password.getBytes(encode));
					return new String(getHexaEncodingBytes(hashValue));
				}
	        } catch (UnsupportedEncodingException e){}
		} catch (NoSuchAlgorithmException e){}
		return "";
	}
	
    private static byte[] hexaMap = { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39,
        0x61, 0x62, 0x63, 0x64, 0x65, 0x66 };
    /**
     * Digest 알고리즘을 수행위한 HexaEncoding
     * @param src
     * @return
     */
    protected static byte[] getHexaEncodingBytes(byte[] src)
    {
        byte[] buffer = new byte[src.length * 2];
        int index = 0;
        for (int i = 0; i < src.length; i++)
        {
            buffer[index++] = hexaMap[((src[i] & 0xf0) >> 4)];
            buffer[index++] = hexaMap[(src[i] & 0x0f)];
        }
        return buffer;
    }
	/*
	public String getPassEncrypt(String data) {
		if (data == null) return "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			try {
				byte[] plainText = data.getBytes("UTF-8"); 				// 평문
				byte[] hashValue = md.digest(plainText); 				// 해쉬값
				return new String(Base64.encodeBase64(hashValue));
            } catch (UnsupportedEncodingException e){}
		} catch (NoSuchAlgorithmException e){}
        return "";
    }*/

	/**
	 * 개인정보 암호화
	 * @param str
	 * @return
	 */
	@Override
	public String getPrivEncrypt(String str) {
    	if(StringUtil.isEmpty(str)) return str;
    	
		int useSecurity = RbsProperties.getPropertyInt("Globals.seed.use.security", 0);
    	
		// 사용하지 않는 경우
    	if(useSecurity == 0) return str;

			// seed 암호화
		String key = RbsProperties.getProperty("Globals.seed.security.key");
		String encStr = null;
		try {
			int[] seedKey = SEEDUtil.getSeedRoundKey(key);
			encStr = SEEDUtil.getSeedEncrypt(str, seedKey);
		} catch(Exception e){System.out.println("e:" + e);}
		return encStr;
	}

	/**
	 * 개인정보 복호화
	 * @param str
	 * @return
	 */
	@Override
	public String getPrivDecrypt(String str) {
    	if(StringUtil.isEmpty(str)) return str;
    	
		int useSecurity = RbsProperties.getPropertyInt("Globals.seed.use.security", 0);
    	
		// 사용하지 않는 경우
    	if(useSecurity == 0) return str;
		
		// seed 복호화
    	String key = RbsProperties.getProperty("Globals.seed.security.key");
		String decStr = null;
		
		try {
			int[] seedKey = SEEDUtil.getSeedRoundKey(key);
			decStr = SEEDUtil.getSeedDecrypt(str, seedKey);
		} catch(Exception e){}
		
		return decStr;
		
	}
}