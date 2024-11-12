package rbs.modules.nonSbjt.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class NonSbjtRequest {
	
	/**
     * 검색어
     */
	private String keyword;
	
	/**
     * 요청 페이지 번호
     */
	private Integer pageNum;
	
	/**
     * 페이지당 목록 수
     */
	private Integer pagePer;
	
    /**
     * 시작날짜
     */
    private String startDate;
    
    /**
     * 종료날짜
     */
    private String endDate;
    
    /**
     * 신청시작날짜
     */
    private String signInStartDate;
    
    /**
     * 신청종료날짜
     */
    private String signInEndDate;
    
    /**
     * 접수상태
     */
    private String status;
    
    /**
     * 모집대상
     */
    private String target;
    
    /**
     * 1차 카테고리
     */
    private String mainCategory;
    
    /**
     * 2차 카테고리
     */
    private String subCategory;
    
    /**
     * 태그
     */
    private String tag;
    
    @Builder
    private NonSbjtRequest(String keyword, Integer pageNum, Integer pagePer, String startDate, String endDate,String signInStartDate, String signInEndDate) {
        this.keyword = keyword;
        this.pageNum = pageNum;
        this.pagePer = pagePer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.signInStartDate = signInStartDate;
        this.signInEndDate = signInEndDate;
    }
    
}
