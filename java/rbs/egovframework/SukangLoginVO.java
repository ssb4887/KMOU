package rbs.egovframework;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SukangLoginVO implements Serializable{
	
	private static final long serialVersionUID = -8274004534207618049L;

	private String errCode;        // 에러 코드
    private String errMessage;     // 에러 메시지
    private String year;           // 년도
    private String smt;            // 학기
    private String smtNm;          // 학기명
    private String sinbun;         // 신분구분
    private String timeGb;         // 수강신청기간
    private String studentNo;      // 학번
    private String nm;             // 성명
    private String collCd;         // 대학코드
    private String collNm;         // 대학명
    private String deptCd;         // 학부(과)코드
    private String deptNm;         // 학부(과)명
    private String majorCd;        // 전공코드
    private String majorNm;        // 전공명
    private String grade;          // 학년/이수학기
    private String sucGrade;       // 학년/이수학기
    private String entYear;        // 입학년도
    private String gradeCode;      // 학년제구분코드
    private String hakjukNm;       // 학적상태
    private String addr;           // 주소
    private String maxCdt;         // 최대학점
    private String cdtSum;         // 신청학점
    private String cdtTeachSum;    // 교직학점
    private String pynYn;          // 편입생 구분
    private String managerYn;      // 관리자구분
    private String loginDt;        // 로그인일자
    private String juminNo2;       // 주민번호뒷자리
    private String smtCdtSum;      // 대학원과목 신청 학기계
    private String totCdtSum;      // 대학원과목 신청 총계
    private String conGb;          // 연결학과 여부
    private String smajorCd;       // 복수전공 코드
    private String minorCd;        // 부전공 코드
    private String fuseCd;         // 융합전공 코드
    private String smajorNm;       // 학부:복수전공명, 대학원:선수과목학점
    private String minorNm;        // 학부:부전공명, 대학원:지도교수
    private String isuSmt;         // 학부:이수학기, 대학원:이수학기
    private String teachYn;        // 학부:교직신청, 대학원:석사인정학점
    private String deglinkYn;      // 연계과정
    private String linkCd;         // 연계전공 코드
    private String fuseNm;         // 융합전공 명
    private String linkNm;         // 연계전공 명
    private String stuGrade;       // 학생 학년
    private String bokhakYn;       // 복학생 여부    
    private String tryTime;		   // 로그인 시도 일시
    private String preApplDttm;	   // 예비수강신청 일시
}
