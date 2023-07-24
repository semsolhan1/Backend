package com.handifarm.api.user.controller;

import com.handifarm.api.user.dto.request.UserJoinRequestDTO;
import com.handifarm.api.user.dto.request.UserLoginRequestDTO;
import com.handifarm.api.user.dto.response.UserInfoResponseDTO;
import com.handifarm.api.user.dto.response.UserLoginResponseDTO;
import com.handifarm.api.user.service.UserService;
import com.handifarm.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
//@CrossOrigin
public class UserController {

    private final UserService service;

    // ID 중복 체크 요청 처리
    @GetMapping("/idCheck")
    public ResponseEntity<?> idCheck(String userId) {
        if (userId.trim().equals("")) {
            return ResponseEntity.badRequest().body("id가 넘어오지 않음.");
        }
        boolean check = service.idDuplicateCheck(userId);
        log.info("ID : {}, 중복 여부 : {}", userId, check);

        return ResponseEntity.ok().body(check);
    }

    // 휴대폰 인증번호 요청
    @PostMapping("/phoneNumAuthenticate")
    public ResponseEntity<?> sendAuthenticationNumber(@RequestBody Map<String, String> inputPhoneNum) {

        log.info("컨트롤러에서 넘겨받은 변수 : {}", inputPhoneNum);

        String sendTo = inputPhoneNum.get("sendTo");

        if (sendTo == null || sendTo.trim().equals("")) {
            return ResponseEntity.badRequest().body("휴대폰 번호가 넘어오지 않음.");
        }

        String authenticationNumber = service.sendMessage(sendTo);
        log.info("{} 번호의 인증번호 : {}", sendTo, authenticationNumber);

        return ResponseEntity.ok().body(authenticationNumber);
    }

    // 가입 요청 처리
    @PostMapping
    public ResponseEntity<?> join(@Validated @RequestBody UserJoinRequestDTO dto,
                                  BindingResult result) {
        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try {
            service.join(dto);
            return ResponseEntity.ok().body("joinSuccess");
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 로그인 요청 처리
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody UserLoginRequestDTO dto,
                                   BindingResult result) {
        if(result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        try {
            UserLoginResponseDTO responseDTO = service.authenticate(dto);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 마이페이지 진입 시 유저 정보 반환
    @GetMapping("/mypage")
    public ResponseEntity<?> mypage(@AuthenticationPrincipal TokenUserInfo userInfo) {
        try {
            UserInfoResponseDTO userInfoResponseDTO = service.userInfo(userInfo);
            return ResponseEntity.ok().body(userInfoResponseDTO);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 프로필 이미지 업로드 요청 처리
    @PatchMapping("/profile")
    public ResponseEntity<?> uploadProfileImg(@AuthenticationPrincipal TokenUserInfo userInfo, MultipartFile profileImg) {
        try {
            String uploaded = service.uploadUserProfileImg(userInfo, profileImg);
            return ResponseEntity.ok().body(uploaded);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
