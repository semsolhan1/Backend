package com.handifarm.api.user.service;

import com.handifarm.api.user.dto.request.UserInfoModifyRequestDTO;
import com.handifarm.api.user.dto.request.UserJoinRequestDTO;
import com.handifarm.api.user.dto.request.UserLoginRequestDTO;
import com.handifarm.api.user.dto.response.UserInfoResponseDTO;
import com.handifarm.api.user.dto.response.UserLoginResponseDTO;
import com.handifarm.api.user.entity.User;
import com.handifarm.api.user.repository.UserRepository;
import com.handifarm.aws.S3Service;
import com.handifarm.jwt.TokenProvider;
import com.handifarm.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;
    private final S3Service s3Service;

    // 메시지 전송 API Key
    @Value("${sendMsg.apiKey}")
    private String apiKey;
    @Value("${sendMsg.secretKey}")
    private String secretKey;

    @Value("${sendMsg.sendNumber}")
    private String sendNumber;

    private final String serviceName = "USER";

    // ID 중복 체크
    @Override
    public boolean idDuplicateCheck(final String userId) {
        return userRepository.existsByUserId(userId);
    }

    // 휴대폰 인증번호 전송
    @Override
    public String sendMessage(final String phoneNum) {
        log.info("서비스에서 넘겨받은 변수 : {}", phoneNum);

        // 인증번호 생성
        String authenticationNumber = makeAuthenticationNumber();

//        // 메시지 전송 시작.
//        Message message = new Message();
//        final DefaultMessageService messageService
//                = NurigoApp.INSTANCE.initialize(apiKey, secretKey, "https://api.coolsms.co.kr");
//
//        // 전송할 메시지 정보 설정
//        message.setFrom(sendNumber); // 발신번호
//        message.setTo(phoneNum); // 수신번호
//        message.setText("HandiFarm 휴대폰 인증번호는 [ " + authenticationNumber + " ] 입니다. 인증번호 입력칸에 입력해주세요.");
//
//        SingleMessageSentResponse sentResponse = messageService.sendOne(new SingleMessageSendingRequest(message));
//        log.info("전송한 메세지 정보 : {}", sentResponse);
        // 메시지 전송 끝.

        return authenticationNumber;
    }

    // 인증번호 생성 메서드(난수)
    private String makeAuthenticationNumber() {
        Random r = new Random();
        int checkNum = r.nextInt(8999) + 1000;
        log.info("인증 번호 : " + checkNum);
        return String.valueOf(checkNum);
    }

    // 회원가입 처리
    @Override
    public void join(final UserJoinRequestDTO dto) {

        if (dto == null) {
            throw new RuntimeException("가입 정보가 없습니다.");
        }

        if (idDuplicateCheck(dto.getUserId())) {
            log.warn("아이디 중복. ID : {}", dto.getUserId());
            throw new RuntimeException("중복된 아이디입니다.");
        }

        String encoded = encoder.encode(dto.getUserPw());
        dto.setUserPw(encoded);

        User user = dto.dtoToEntity();

        userRepository.save(user);

        log.info("회원가입 처리 완료!");
    }

    // 로그인 처리 및 토큰 발급
    @Override
    public UserLoginResponseDTO authenticate(final UserLoginRequestDTO dto) {

        User user = userRepository.findById(dto.getUserId()).
                orElseThrow(() -> new RuntimeException("가입된 회원이 아닙니다."));

        String inputPassword = dto.getUserPw();
        String dbPassword = user.getUserPw();

        if (!encoder.matches(inputPassword, dbPassword)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        log.info("{}님 로그인 성공.", user.getUserId());

        String token = tokenProvider.createToken(user);

        return new UserLoginResponseDTO(user, token);
    }

    // 유저 정보 반환
    @Override
    public UserInfoResponseDTO userInfo(final TokenUserInfo userInfo) {

        User user = userRepository.findById(userInfo.getUserId())
                .orElseThrow(() -> new RuntimeException("회원 조회에 실패했습니다."));

        return new UserInfoResponseDTO(user);
    }

    // 유저 정보 수정
    @Override
    public UserInfoResponseDTO userInfoModify(final TokenUserInfo userInfo,
                                              final UserInfoModifyRequestDTO requestDTO,
                                              final MultipartFile profileImg) throws Exception {
        if (!userInfo.getUserId().equals(requestDTO.getUserId())) {
            throw new RuntimeException("인증이 유효하지 않습니다.");
        }

        User user = userRepository.findById(requestDTO.getUserId()).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        if (profileImg != null && !profileImg.isEmpty()) {
            // 프로필 이미지가 변경되면 기존 이미지는 삭제
            s3Service.deleteFromS3Bucket(user.getUserProfileImg());

            String uuidFileName = UUID.randomUUID() + "_" + profileImg.getOriginalFilename();

            String uploadUrl = s3Service.uploadToS3Bucket(profileImg.getBytes(), uuidFileName, serviceName);

            user.setUserProfileImg(uploadUrl);
        }

        if (requestDTO.getUserPw() != null) {
            if (!requestDTO.getUserPw().trim().equals("")) user.setUserPw(encoder.encode(requestDTO.getUserPw()));
        }

        user.setUserName(requestDTO.getUserName());
        user.setUserNick(requestDTO.getUserNick());
        user.setUserEmail(requestDTO.getUserEmail());
        user.setUserPhone(requestDTO.getUserPhone());
        user.setUserAddrBasic(requestDTO.getUserAddrBasic());
        user.setUserAddrDetail(requestDTO.getUserAddrDetail());
        user.setUserPostcode(requestDTO.getUserPostcode());

        User saved = userRepository.save(user);

        log.info("회원 정보 수정 완료. DB에 입력된 데이터 : {}", saved);

        return new UserInfoResponseDTO(saved);
    }

    // 프로필 이미지 등록
    @Override
    public String uploadUserProfileImg(final TokenUserInfo userInfo, final MultipartFile profileImg) throws Exception {

        if (profileImg == null || profileImg.isEmpty()) {
            throw new RuntimeException("프로필 사진이 업로드되지 않았습니다.");
        }

        User user = userRepository.findById(userInfo.getUserId()).orElseThrow(() -> new RuntimeException("회원 조회에 실패했습니다."));

        String uuidFileName = UUID.randomUUID() + "_" + profileImg.getOriginalFilename();

        String uploadUrl = s3Service.uploadToS3Bucket(profileImg.getBytes(), uuidFileName, serviceName);

        user.setUserProfileImg(uploadUrl);

        User saved = userRepository.save(user);

        log.info("회원 프로필 이미지 추가 완료. DB에 입력된 데이터 : {}", saved);

        return uploadUrl;
    }

}
