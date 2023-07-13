package com.handifarm.api.board.repository;

import com.handifarm.api.board.entity.Board;
import com.handifarm.api.board.entity.Category;
import com.handifarm.api.board.entity.QBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;


import java.util.List;

import static com.handifarm.api.board.entity.Board.Category.NOTIFY;
import static com.handifarm.api.board.entity.QBoard.board;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(false)
class BoardRepositoryTest {

  @Autowired
  BoardRepository boardRepository;

  @Autowired
  EntityManager em;

  JPAQueryFactory factory;


  @BeforeEach
  void settingObject() {
    factory = new JPAQueryFactory(em);
  }


  void bulkInsert() {
    // 게시물 147개 저장
    int j = 0;
    for(int i=1; i<147; i++) {
      j++;
      if(j == 4) {
        j = 0;
      }
      Category[] categoryArr = Category.values();
      Board b = Board.builder()
              .boardNo(i)
              .userId("아이디" + i)
              .title("제목" + i)
              .content("내용" +i)
//              .category(categoryArr[j])
              .build();
      boardRepository.save(b);
    }


  }

  @Test
  @DisplayName("testJPA")
  void testJPA() {
    List<Board> boards = boardRepository.findAll();

    boards.forEach(System.out::println);
  }


  @Test
  @DisplayName("카테고리 FREE")
  void testFree() {
    //given
    // QBoard는 QueryDSL의 코드 생성 도구가 생성한 Board 엔티티에 대한 쿼리 타입입니다.
    QBoard board = QBoard.board;
    //when
    // JPAQueryFactory는 QueryDSL 쿼리를 생성하고 실행하는 데 사용됩니다.
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    List<Board> boards = queryFactory
            .selectFrom(board)
            .where(board.category.eq(Board.Category.FREE))
            .fetch();

    //then
    assertEquals(boards.size(), 36);
    System.out.println("\n\n\n");
    System.out.println("boards = " + boards);
    System.out.println("\n\n\n");
  }

  @Test
  @DisplayName("카테고리 REPORT")
  void testReport() {
    //given
    // QBoard는 QueryDSL의 코드 생성 도구가 생성한 Board 엔티티에 대한 쿼리 타입입니다.
    QBoard board = QBoard.board;
    //when
    // JPAQueryFactory는 QueryDSL 쿼리를 생성하고 실행하는 데 사용됩니다.
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    List<Board> boards = queryFactory
            .selectFrom(board)
            .where(board.category.eq(Board.Category.REPORT))
            .fetch();

    //then
    assertEquals(boards.size(), 37);
    System.out.println("\n\n\n");
    System.out.println("boards = " + boards);
    System.out.println("\n\n\n");
  }

  @Test
  @DisplayName("카테고리 ENQUIRY")
  void testEnquiry() {
    //given
    // QBoard는 QueryDSL의 코드 생성 도구가 생성한 Board 엔티티에 대한 쿼리 타입입니다.
    QBoard board = QBoard.board;
    //when
    // JPAQueryFactory는 QueryDSL 쿼리를 생성하고 실행하는 데 사용됩니다.
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    List<Board> boards = queryFactory
            .selectFrom(board)
            .where(board.category.eq(Board.Category.ENQUIRY))
            .fetch();

    //then
    assertEquals(boards.size(), 36);
    System.out.println("\n\n\n");
    System.out.println("boards = " + boards);
    System.out.println("\n\n\n");
  }

  @Test
  @DisplayName("카테고리 NOTIFY")
  void testNotify() {
    //given
    // QBoard는 QueryDSL의 코드 생성 도구가 생성한 Board 엔티티에 대한 쿼리 타입입니다.
    QBoard board = QBoard.board;
    //when
    // JPAQueryFactory는 QueryDSL 쿼리를 생성하고 실행하는 데 사용됩니다.
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    List<Board> boards = queryFactory
            .selectFrom(board)
            .where(board.category.eq(NOTIFY))
            .fetch();

    //then
    assertEquals(boards.size(), 37);
    System.out.println("\n\n\n");
    System.out.println("boards = " + boards);
    System.out.println("\n\n\n");
  }

  @Test
  @DisplayName("Join 카테고리")
  void leftJoin() {
      //given

      //when
    List<Board> result = factory.selectFrom(board)
            .where(board.content.contains("3"))
            .fetch();
    //then

    assertEquals(result.size(), 33);

    System.out.println("\n\n\n");
    System.out.println(result);
    System.out.println("\n\n\n");
  }

  @Test
  @DisplayName("2가지 조건")
  void andWhere() {
      //given

      //when
       List<Board> result = factory
            .selectFrom(board)
             .where(board.content.contains("3").and(board.category.eq(Board.Category.FREE)))
                     .fetch();
    //then
    assertEquals(result.size(), 4);

    System.out.println("\n\n\n");
    result.forEach(System.out::println);
    System.out.println("\n\n\n");

  }

  @Test
  @DisplayName("동적 sql 테스트")
  void queryTest() {
      //given
      String content = null;
      Board.Category category;
      category = NOTIFY;
    //when
      List<Board> result = boardRepository.findSearch(category, content);
      //then
//    assertEquals(result.size(), 4);

    System.out.println("\n\n\n");
    result.forEach(System.out::println);
    System.out.println("\n\n\n");

  }

}