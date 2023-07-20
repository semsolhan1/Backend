package com.handifarm.recontent.dto.page;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Setter
@Getter
@ToString
public class RecontentPageResponseDTO<T> {

    private int restartPage;

    private int reendPage;

    private int recurrentPage;

    private boolean renext;

    private boolean reprev;

    private int retotalCount;

    private static final int RePage_Num = 10;

    public RecontentPageResponseDTO(Page<T> page){
        this.retotalCount = (int) page.getTotalElements();
        this.recurrentPage = page.getPageable().getPageNumber() + 1;
        this.reendPage = (int) (Math.ceil((double)recurrentPage/RePage_Num)*RePage_Num);
        this.restartPage = reendPage - RePage_Num + 1;

        int realEndPage = page.getTotalPages();

        if(realEndPage < this.reendPage) this.reendPage = realEndPage;

        this.reprev = restartPage > 1;
        this.renext = reendPage < realEndPage;


    }

}
