package com.handifarm.cboard.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

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
}
