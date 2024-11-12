package rbs.egovframework;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginVO implements Serializable{
	
	private static final long serialVersionUID = -8274004534207618049L;

	/** 고유아이디 */
	private String memberIdx;
	/** 아이디(암호화) */
	private String memberId;
	/** 아이디(원본) */
	private String memberIdOrg;
	/** 비밀번호 */
	private String memberPwd;
	/** 이름(암호화) */
	private String memberName;
	/** 이름(원본) */
	private String memberNameOrg;
	/** 이메일주소(암호화) */
	private String memberEmail;
	/** 이메일주소(원본) */
	private String memberEmailOrg;
	/** 사용자유형 */
	private int usertypeIdx;
	/** 사용자유형명 */
	private String usertypeNm;	
	/** 대학코드 */
	private String collCd;
	/** 대학명 */
	private String collNm;
	/** 학부(과)코드 */
	private String deptCd;
	/** 학부(과)명 */
	private String deptNm;
	/** 학년 */
	private String grade;
	/** 전공코드 - 없을시 학부(과)코드 */
	private String majorCd;
	/** 전공명  - 없을시 학부(과)명 */
	private String majorNm;
	/** 직급코드 - 학생은 신분코드 */
	private String type;	
	/** 직급명 - 학생은 신분명 */
	private String typeNm;		
	/** 부서코드 */
	private int departIdx;
	/** 부서코드:하위단계까지 ','로 구분 */
	private String departIdxs;
	
	private List<Object> groupList;
	
	/** 비밀번호 수정 안한 기간 */
	private double pwdModiIntv;
	
	/** 비밀번호 다음에 변경하기 한 기간 */
	private double pwdModiIntv2;
	
	/** 비밀번호 다음에 변경하기(1:다음에 변경하기) */
	private String pwdModiType;
	
	private int loginFail;
	
	/** 로그인 여부 */
	private boolean isLogin;
	
	/** SNS구분(facebook 등) */
	private String snsType;
	/** sns별 ID */
	private String snsId;
}
