package com.handifarm.cboard.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "cboardId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "project_cboard")
public class Cboard {

    @Id
    @GeneratedValue(generator = "cboard_uuid")
    @GenericGenerator(name = "cboard_uuid", strategy = "uuid")
    private String cboardId;

    @Column(nullable = false)
    private String title;


    private String writer;

    @CreationTimestamp
    private LocalDateTime boardTime;


}
