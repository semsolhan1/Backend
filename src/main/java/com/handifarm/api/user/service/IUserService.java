package com.handifarm.api.user.service;

import com.handifarm.api.user.dto.request.UserJoinRequestDTO;
import com.handifarm.api.user.dto.request.UserLoginRequestDTO;
import com.handifarm.api.user.dto.response.UserLoginResponseDTO;
import com.handifarm.jwt.TokenUserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {

    // id 중복체크
    boolean idDuplicateCheck(final String userId);

    // 휴대폰 인증번호 발송 및 프론트로 리턴
    int sendMessage(final String phoneNum);

    // 회원가입 처리
    void join(final UserJoinRequestDTO dto) throws Exception;

    // 로그인 처리 및 토큰 발급
    UserLoginResponseDTO authenticate(final UserLoginRequestDTO dto);

    // 프로필 이미지 등록
    String uploadUserProfileImg(TokenUserInfo userInfo, MultipartFile profileImg) throws Exception;

}
