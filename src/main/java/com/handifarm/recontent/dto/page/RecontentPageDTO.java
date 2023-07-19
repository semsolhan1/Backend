package com.handifarm.recontent.dto.page;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class RecontentPageDTO {


    private int repage;

    private int resize;

    public RecontentPageDTO(){
        this.repage = 1;
        this.resize = 10;
    }
}
