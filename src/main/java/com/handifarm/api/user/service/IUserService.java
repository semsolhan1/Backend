package com.handifarm.api.user.service;

import com.handifarm.api.user.dto.request.UserInfoModifyRequestDTO;
import com.handifarm.api.user.dto.request.UserJoinRequestDTO;
import com.handifarm.api.user.dto.request.UserLoginRequestDTO;
import com.handifarm.api.user.dto.response.UserInfoResponseDTO;
import com.handifarm.api.user.dto.response.UserLoginResponseDTO;
import com.handifarm.jwt.TokenUserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {

    // ID 중복 체크
    boolean idDuplicateCheck(final String userId);

    // 휴대폰 인증번호 전송
    String sendMessage(final String phoneNum);

    // 회원가입 처리
    void join(final UserJoinRequestDTO dto);

    // 로그인 처리 및 토큰 발급
    UserLoginResponseDTO authenticate(final UserLoginRequestDTO dto);

    // 유저 정보 반환
    UserInfoResponseDTO userInfo(final TokenUserInfo userInfo);

    // 유저 정보 수정
    UserLoginResponseDTO userInfoModify(final TokenUserInfo userInfo, final UserInfoModifyRequestDTO requestDTO, final MultipartFile profileImg) throws Exception;

    // 프로필 이미지 등록
    String uploadUserProfileImg(final TokenUserInfo userInfo, final MultipartFile profileImg) throws Exception;

}
