package com.handifarm.api.board.repository;
import com.handifarm.api.board.entity.Board;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;
import static com.handifarm.api.board.entity.QBoard.board;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public List<Board> findSearch(Board.Category categoryParam, String contentParam) {
    return queryFactory
            .selectFrom(board)
            .where(categoryEq(categoryParam), contentEq(contentParam))
            .fetch();
  }

  private BooleanExpression contentEq(String contentParam) {
    return contentParam != null ? board.content.eq(contentParam) : null;
  }

  private BooleanExpression categoryEq(Board.Category categoryParam) {
    return categoryParam != null ? board.category.eq(categoryParam) : null;
  }
}
