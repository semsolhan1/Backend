package com.handifarm.like.entity;

import com.handifarm.cboard.entity.Cboard;
import com.handifarm.recontent.entity.Recontent;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString(exclude = {"cboard","recontent"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "project_Like")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id") // postId 컬럼 매핑
    private Long postId;

    @Column(name = "comment_id") // commentId 컬럼 매핑
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "cboardId")
    private Cboard cboard;

    @ManyToOne
    @JoinColumn(name = "recontentOrder")
    private Recontent recontent;


}
