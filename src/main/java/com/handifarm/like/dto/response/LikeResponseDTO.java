package com.handifarm.like.dto.response;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponseDTO {

    private boolean isDuplicate;
}
