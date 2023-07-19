package com.handifarm.cboard.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString(exclude = {"cboard"})
@EqualsAndHashCode(of = {"hashId"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hash_tag")
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hash_id")
    private long hashId;

    private String hashName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cboard_id")
    private Cboard cboard;

}
