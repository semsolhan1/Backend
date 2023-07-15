package com.handifarm.api.util.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Setter @Getter
@ToString
public class PageResponseDTO<T> {

    private int startPage;
    private int endPage;
    private int currentPage;

    private boolean prev;
    private boolean next;

    private int totalCount;

    //한 페이지에 배치할 페이지 버튼 수 (1~10 // 11~20)
    private static final int PAGE_COUNT = 10;

    //제네릭 타입을 <T>로 선언하면, 전달되는 객체의 제네릭 타입에 따라
    //T가 결정된다. -> 클래스에도 제네릭 타입 <T>를 선언해 주세요.
    public PageResponseDTO(Page<T> pageData) {
        this.totalCount = (int) pageData.getTotalElements();
        this.currentPage = pageData.getPageable().getPageNumber() + 1;
        this.endPage
                = (int) (Math.ceil((double) currentPage / PAGE_COUNT) * PAGE_COUNT);
        this.startPage = endPage - PAGE_COUNT + 1;

        int realEnd = pageData.getTotalPages();

        if(realEnd < this.endPage) this.endPage = realEnd;

        this.prev = startPage > 1;
        this.next = endPage < realEnd;
    }

}
