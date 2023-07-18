package com.handifarm.recontent.entity;


import com.handifarm.cboard.entity.Cboard;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = {"cboard"})
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


}
