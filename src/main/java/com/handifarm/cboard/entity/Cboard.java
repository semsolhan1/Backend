package com.handifarm.cboard.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    private String content;

    private String fileUp;

    private String writer;

    @CreationTimestamp
    private LocalDateTime boardTime;

    @OneToMany(mappedBy = "cboard", orphanRemoval = true)
    @Builder.Default
    private List<HashTag> hashTags = new ArrayList<>();


    public void addHashTag(HashTag savedTag) {
        hashTags.add(savedTag);
        if(this != savedTag.getCboard()){
            savedTag.setCboard(this);
        }

    }
}
