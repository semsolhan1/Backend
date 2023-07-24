package com.handifarm.like.api;


import com.handifarm.like.dto.request.LikeRequestDTO;
import com.handifarm.like.dto.response.LikeResponseDTO;
import com.handifarm.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<?> checkDuplicateLike(@RequestBody LikeRequestDTO likeRequestDTO) {

        try {
            boolean isDuplicate = likeService.checkDuplicateLikeAndAdd(likeRequestDTO);
            LikeResponseDTO responseDTO = new LikeResponseDTO(isDuplicate);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.error("Error occurred while checking duplicate like: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while checking duplicate like.");
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelLike(@RequestBody LikeRequestDTO likeRequestDTO) {
        try {
            likeService.cancelLike(likeRequestDTO);
            return ResponseEntity.ok().body("Like canceled successfully.");
        } catch (Exception e) {
            log.error("Error occurred while canceling like: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while canceling like.");
        }
    }

}
