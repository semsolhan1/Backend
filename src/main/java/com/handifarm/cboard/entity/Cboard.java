package com.handifarm.cboard.entity;

import com.handifarm.api.market.entity.ItemImg;
import com.handifarm.like.entity.Like;
import com.handifarm.recontent.entity.Recontent;
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
    private String content;

    @Column(nullable = false)
    private String writer;


    @CreationTimestamp
    private LocalDateTime boardTime;

    @OneToMany(mappedBy = "cboard", orphanRemoval = true)
    @Builder.Default
    private List<HashTag> hashTags = new ArrayList<>();

    @OneToMany(mappedBy = "cboard", orphanRemoval = false)
    @Builder.Default
    private List<Recontent> recontents = new ArrayList<>();

    @OneToMany(mappedBy = "cboard", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<BoardImg> itemImgs = new ArrayList<>();

    @OneToMany(mappedBy = "cboard", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();


    public void addHashTag(HashTag savedTag) {
        hashTags.add(savedTag);
        if(this != savedTag.getCboard()){
            savedTag.setCboard(this);
        }
    }

    public void addItemImg(BoardImg itemImg) {
        itemImgs.add(itemImg);
        if(this != itemImg.getCboard()) {
            itemImg.setCboard(this);
        }
    }
}
