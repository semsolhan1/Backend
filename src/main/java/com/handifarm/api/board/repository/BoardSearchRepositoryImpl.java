package com.handifarm.api.board.repository;

import com.handifarm.api.board.entity.Board;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.handifarm.api.board.entity.QBoard.board;

@RequiredArgsConstructor
@Service
public class BoardSearchRepositoryImpl implements BoardSearchRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Board> searchBoardList(String category, String condition, String searchWord) {
        return queryFactory
                .selectFrom(board)
                .where(categoryEq(category), searchWordEq(condition, searchWord))
                .fetch();
    }

    private BooleanExpression categoryEq(String category) {
        return category.equals("all") ? null : board.category.eq(Board.Category.valueOf(category));
    }


    private BooleanExpression searchWordEq(String condition, String searchWord) {
        if (condition.equals("title")) {
            return board.title.contains(searchWord);
        } else if (condition.equals("content")) {
            return board.content.contains(searchWord);
        } else if (condition.equals("userNick")) {
            return board.userNick.contains(searchWord);
        } else {
            return null;
        }


    }
}
