package com.handifarm.like.service;

import com.handifarm.cboard.entity.Cboard;
import com.handifarm.cboard.repository.CboardRepository;
import com.handifarm.like.dto.request.LikeRequestDTO;
import com.handifarm.like.entity.Like;
import com.handifarm.like.repository.LikeRepository;
import com.handifarm.recontent.entity.Recontent;
import com.handifarm.recontent.repository.RecontentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;

    private final CboardRepository cboardRepository;

    private final RecontentRepository recontentRepository;

    public boolean checkDuplicateLikeAndAdd(LikeRequestDTO likeRequestDTO) {

        Long postId = likeRequestDTO.getPostId();
        Long commentId = likeRequestDTO.getCommentId();

        boolean isDuplicate = likeRepository.existsByPostIdAndCommentId(postId, commentId);

        if (!isDuplicate) {
            Like newLike = new Like();

            Cboard cboard = null;
            Recontent recontent = null;

            // 게시판 ID가 있으면 Cboard를 조회하여 설정
            if (postId != null) {
                cboard = cboardRepository.findById(String.valueOf(postId)).orElse(null);
                newLike.setCboard(cboard);

                // 해당 게시글의 likes 리스트에도 좋아요를 추가
                cboard.getLikes().add(newLike);
            }

            // 댓글 ID가 있으면 Recontent를 조회하여 설정
            if (commentId != null) {
                recontent = recontentRepository.findById(String.valueOf(commentId)).orElse(null);
                newLike.setRecontent(recontent);

                // 해당 댓글의 likes 리스트에도 좋아요를 추가
                recontent.getLikes().add(newLike);
            }

            likeRepository.save(newLike);
        }

        return isDuplicate;

    }

    public void cancelLike(LikeRequestDTO likeRequestDTO) {

        Long postId = likeRequestDTO.getPostId();
        Long commentId = likeRequestDTO.getCommentId();

        likeRepository.deleteByPostIdAndCommentId(postId, commentId);
    }
}
