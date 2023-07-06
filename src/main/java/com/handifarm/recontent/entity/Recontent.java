package com.handifarm.recontent.entity;


import com.handifarm.cboard.entity.Cboard;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = {""})
@EqualsAndHashCode(of = "recontentId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "project_recontent")
public class Recontent {

    @Id
    @GeneratedValue(generator = "recontent_uuid")
    @GenericGenerator(name = "recontent_uuid", strategy = "uuid")
    private String recontentId;


    @Column(nullable = false)
    private String recontent;


    private String rewriter;

    @CreationTimestamp
    private LocalDateTime recontentTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cboard_id")
    private Cboard cboard;


}
