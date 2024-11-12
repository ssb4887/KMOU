package rbs.egovframework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rbs.modules.log.service.MngLogService;


/**
 * log util
 * @author user
 *
 */
public class LogHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogHelper.class);
	static MngLogService mngLogService;
	public static MngLogService getMngLogService() {
		return mngLogService;
	}
	public static void setMngLogService(MngLogService mngLogService) {
		LogHelper.mngLogService = mngLogService;
	}
	
	/**
	 * 로그 등록
	 * @param logType		로그구분(1:등록,2:수정,3:삭제,4:복원,5:완전삭제, 9:적용)
	 * @param confSModule	내부모듈경로
	 * @param keyCodes		key값
	 * @return
	 */
	public static int insert(int logType, Object ... keyCodes){
		try {
			return mngLogService.insert(null, logType, keyCodes);
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			return -1;
		}
	}
	
	/**
	 * 로그 등록
	 * @param logType		로그구분(1:등록,2:수정,3:삭제,4:복원,5:완전삭제, 9:적용)
	 * @param confSModule	내부모듈경로
	 * @param keyCodes		key값
	 * @return
	 */
	public static int insert(String confSModule, int logType, Object ... keyCodes){
		try {
			return mngLogService.insert(confSModule, logType, keyCodes);
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			return -1;
		}
	}
	
	/**
	 * 로그 등록
	 * @param logType		로그구분(1:등록,2:수정,3:삭제,4:복원,5:완전삭제, 9:적용)
	 * @param confSModule	내부모듈경로
	 * @param preKeyCode	keyCode앞에 추가할 key값
	 * @param keyCodes		key값
	 * @return
	 */
	public static int insert(String confSModule, String preKeyCode, int logType, Object ... keyCodes){
		try {
			return mngLogService.insert(confSModule, preKeyCode, logType, keyCodes);
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			return -1;
		}
	}
}