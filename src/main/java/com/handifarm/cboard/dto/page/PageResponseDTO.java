package com.handifarm.cboard.dto.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Setter
@Getter
@ToString
public class PageResponseDTO<T> {

    private int startPage;

    private int endPage;

    private int currentPage;

    private boolean next;

    private boolean prev;

    private int totalCount;

    private static final int Page_Num = 10;

    public PageResponseDTO(Page<T> page){
        this.totalCount = (int) page.getTotalElements();
        this.currentPage = page.getPageable().getPageNumber() + 1;
        this.endPage = (int) (Math.ceil((double)currentPage/Page_Num)*Page_Num);
        this.startPage = endPage - Page_Num + 1;

        int realEndPage = page.getTotalPages();

        if(realEndPage < this.endPage) this.endPage = realEndPage;

        this.prev = startPage > 1;
        this.next = endPage < realEndPage;


    }



}
