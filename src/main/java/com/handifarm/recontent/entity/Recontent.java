package com.handifarm.recontent.entity;


import com.handifarm.cboard.entity.Cboard;
import com.handifarm.like.entity.Like;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"cboard","likes"})
@EqualsAndHashCode(of = "recontentOrder")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "project_recontent")
public class Recontent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int recontentOrder;

    @Column(nullable = false)
    private String recontent;

    @Column(nullable = false)
    private String rewriter;

    @CreationTimestamp
    private LocalDateTime recontentTime;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cboard_id")
    private Cboard cboard;

    @OneToMany(mappedBy = "recontent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();


}
