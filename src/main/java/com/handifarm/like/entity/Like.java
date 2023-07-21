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

    @ManyToOne
    @JoinColumn(name = "cboardId")
    private Cboard cboard;

    @ManyToOne
    @JoinColumn(name = "recontentOrder")
    private Recontent recontent;


}
