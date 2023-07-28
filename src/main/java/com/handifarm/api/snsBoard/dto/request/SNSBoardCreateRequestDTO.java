package com.handifarm.api.snsBoard.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SNSBoardCreateRequestDTO {

    @NotBlank
    private String content;

    @NotNull
    List<MultipartFile> snsImgs;

    List<String> hashTags;

}
