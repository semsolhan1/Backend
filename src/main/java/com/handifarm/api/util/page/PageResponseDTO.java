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

    // 한 페이지에 배치할 페이지 버튼 수 (1 ~ 10 // 11 ~ 20)
    private static final int PAGE_COUNT = 10;

    // 제네릭 타입을 <T>로 선언하면, 전달되는 객체의 제네릭 타입에 따라 T가 결정된다.
    // -> 클래스에도 제네릭 타입 <T>를 선언해 주세요.
    // 댓글 페이징에도 사용할 것이기 때문에 <T>로 선언 (게시글과 댓글을 모두 받기 위해)
    public PageResponseDTO(Page<T> pageData) {
        this.totalCount = (int) pageData.getTotalElements();
        this.currentPage = pageData.getPageable().getPageNumber() + 1; // pageable이 zero base이므로 +1
        this.endPage = (int) (Math.ceil((double) currentPage / PAGE_COUNT) * PAGE_COUNT);
        this.startPage = endPage - PAGE_COUNT + 1;

        int realEnd = pageData.getTotalPages();

        if(realEnd < this.endPage) this.endPage = realEnd;

        this.prev = startPage > 1; // startPage가 1보다 크다면 prev는 true
        this.next = endPage < realEnd; // endPage가 realEnd보다 작다면 next는 true
    }

}
