package rbs.egovframework.util;

import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

public @Getter @Setter class Paging {
	//For Paging
    int recordsPerPage;				// 페이지당 레코드 수
    int firstPageNo;          		// 첫번째 페이지 번호
    int prevPageNo;					// 이전 페이지 번호
    int startPageNo;				// 시작 페이지 (페이징 너비 기준)
    int currentPageNo;				// 페이지 번호
    int endPageNo;					// 끝 페이지 (페이징 너비 기준)
    int nextPageNo;					// 다음 페이지 번호
    int finalPageNo;				// 마지막 페이지 번호
    int numberOfRecords;     		// 전체 레코드 수
    int sizeOfPage;					// 보여지는 페이지 갯수 (1,2,3,4,5 갯수)
    int offSet;						// select시 limit 시작점
    int startNumPerPage;			// 조회왼 페이지의 시작 번호

    //For Search
    String sear;					// 검색어
    String sear2;					// 검색어2
    String sear3;					// 검색어3
    String sear4;					// 검색어4
    String sear5;					// 검색어5
    String sear6;					// 검색어6
    String sear7;					// 검색어7
    String sear8;					// 검색어8
    String sear9;					// 검색어9
    String sear10;					// 검색어10    
    String sField;					// 검색을 하는 특정한 필드
    String sType;					// 검색 타입
    String sStartDate;				// 검색 시작날짜
    String sEndDate;				// 검색 종료날짜

    //For Order
    String s_order_field;
    String s_order_method;
    String s_order_field2;
    String s_order_method2;

    /*
     * Init Paging
     */
    public Paging() {}
    public Paging(int currentPageNo, int recordsPerPage) {
        this.currentPageNo = currentPageNo;
        this.sizeOfPage = 5; //기본 페이지 : 5개 보기를 default로 설정함
        this.recordsPerPage = (recordsPerPage != 0) ? recordsPerPage : 5; //recordsPerPage가 0이 아니면 recordsPerPage, 아니면 무조건 5(default : 5)
        this.offSet = (currentPageNo - 1) * recordsPerPage;
    }
    
    /*
     * Init Paging - Request
     */
    public Paging(HttpServletRequest request) {
        int recordsPerPage = Integer.parseInt(null2Zero(request.getParameter("size")));
        String sort_field = null2Blank(request.getParameter("sorters[0][field]"));
        String sort_direct = null2Blank(request.getParameter("sorters[0][dir]"));
        
        setSear(null2Blank(request.getParameter("sear")).trim());
        setSear2(null2Blank(request.getParameter("sear2")).trim());
        setSear3(null2Blank(request.getParameter("sear3")).trim());
        setSear4(null2Blank(request.getParameter("sear4")).trim());
        setSear5(null2Blank(request.getParameter("sear5")).trim());
        setSear6(null2Blank(request.getParameter("sear6")).trim());
        setSear7(null2Blank(request.getParameter("sear7")).trim());
        setSear8(null2Blank(request.getParameter("sear8")).trim());
        setSear9(null2Blank(request.getParameter("sear9")).trim());
        setSType(null2Blank(request.getParameter("sType")).trim());
        
        this.currentPageNo = Integer.parseInt(null2Zero(request.getParameter("page")));
        this.sizeOfPage = 20;
        this.recordsPerPage = (recordsPerPage != 0) ? recordsPerPage : 20;
        this.offSet = (currentPageNo - 1) * recordsPerPage;
        setSorting(sort_field, sort_direct);
    }

    /*
     * Create paging
     */
    public void makePaging() {
        if(numberOfRecords == 0) return;//게시글 전체 수가 없는 경우
        if(currentPageNo == 0) setCurrentPageNo(1);//기본 값 설정
        if(recordsPerPage == 0) setRecordsPerPage(10);//기본 값 설정

        // 마지막 페이지
        int finalPage = (numberOfRecords + (recordsPerPage - 1)) / recordsPerPage;
        if(currentPageNo > finalPage) setCurrentPageNo(finalPage);//기본 값 설정

        if(currentPageNo < 0 || currentPageNo > finalPage) currentPageNo = 1;//현재 페이지 유효성 체크
        
        // 시작 페이지 (전체)
        boolean isNowFirst = currentPageNo == 1 ? true : false;
        boolean isNowFinal = currentPageNo == finalPage ? true : false;

        int startPage = (sizeOfPage > 0 ? (((currentPageNo - 1) / sizeOfPage) * sizeOfPage + 1) : 1);
        int endPage = startPage + sizeOfPage - 1;

        if(endPage > finalPage) endPage = finalPage;

        setFirstPageNo(1);//첫번째 페이지 번호

        if(isNowFirst) setPrevPageNo(1);//이전 페이지 번호
        else setPrevPageNo(((currentPageNo - 1) < 1 ? 1 : (currentPageNo - 1)));

        setStartPageNo(startPage);//시작페이지
        setEndPageNo(endPage);//끝 페이지

        if(isNowFinal) setNextPageNo(finalPage);//다음 페이지 번호
        else setNextPageNo(((currentPageNo + 1) > finalPage ? finalPage : (currentPageNo + 1)));

        setFinalPageNo(finalPage);//마지막 페이지 번호

        offSet = ( currentPageNo - 1 ) * recordsPerPage;
        startNumPerPage = numberOfRecords - (currentPageNo-1) * recordsPerPage;
    }
    
    /*
     * 정렬값 확인
     */
    public void setSorting(String sort_field, String sort_direct) {
    	if(!sort_field.equals("")){
    		this.setS_order_field(sort_field);
            if(sort_direct.equals(""))
            	this.setS_order_method("asc");
            else
            	this.setS_order_method(sort_direct);
        }
    }
    
	/* 
	 * null을 ""로 변환 
	 * */
	public String null2Blank(String str){
		String strTmp;
		if(str == null) strTmp = "";
		else strTmp = str;
		
		return strTmp;
	}

	/* 
	 * null을 0로 변환 
	 * */
	public String null2Zero(String str){
		String strTmp;
		if(str == null) strTmp = "0";
		else strTmp = str;
		
		return strTmp;
	}
}
