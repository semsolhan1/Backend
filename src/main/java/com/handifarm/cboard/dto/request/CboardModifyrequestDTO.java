package com.handifarm.cboard.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CboardModifyrequestDTO {

    @NotBlank
    private String id;


    private String title;


    private String content;

    private List<String> hashTags;

    private String fileUp;

//    public Cboard toModifyEntity(Cboard ModifyBoard,String uploadedFilePath){
//        return Cboard.builder()
//                .title(ModifyBoard.getTitle())
//                .content(ModifyBoard.getContent())
//                .fileUp(uploadedFilePath)
//                .build();
//    }
}
