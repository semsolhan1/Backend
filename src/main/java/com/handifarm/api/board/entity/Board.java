package com.handifarm.api.board.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_board")
public class Board {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long boardNo; //글번호

  @Column(nullable = false)
  private String userId; // 작성자

  @Enumerated(EnumType.STRING)
  private Category category; //말머리


  @Column(nullable = false)
  private String title; //글제목


  private String content; //글내용

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createDate; //작성일

  @CreationTimestamp
  private LocalDateTime updateDate; //글 수정일



}
