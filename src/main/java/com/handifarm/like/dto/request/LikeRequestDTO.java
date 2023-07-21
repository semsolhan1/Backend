package com.handifarm.like.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequestDTO {

    private Long postId;
    private Long commentId;

}
