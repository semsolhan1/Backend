package com.handifarm.cboard.api;

import com.handifarm.cboard.dto.page.PageDTO;
import com.handifarm.cboard.dto.request.CboardCreateRequestDTO;
import com.handifarm.cboard.dto.request.CboardModifyrequestDTO;
import com.handifarm.cboard.dto.response.CboardListResponseDTO;
import com.handifarm.cboard.service.CboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/cboard")
public class CboardController {
//
    private  final CboardService cboardService;

    //게시판 목록
    @GetMapping
    public ResponseEntity<?> retrieveList(PageDTO pageDTO){

        CboardListResponseDTO dto = cboardService.retrieve(pageDTO);

        return ResponseEntity.ok().body(dto);
    }


    //게시판 등록 요청
    @PostMapping
    public ResponseEntity<?> createcboard(
        @Validated @RequestBody CboardCreateRequestDTO dto,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImg,
            BindingResult result
    ){
            log.info("/api/cboard/post -{}",dto);

        if(dto == null) {
            return ResponseEntity.badRequest().body("등록 제목을 전달해 주세요.");
        }

        ResponseEntity<List<FieldError>> fieldErrors = getValidated(result);

        if(fieldErrors != null){
            return fieldErrors;
        }

        try {
            String uploadedFilePath = null;

            if(profileImg != null) {
                log.info("attached file name: {}", profileImg.getOriginalFilename());
                uploadedFilePath = cboardService.uploadProfileImage(profileImg);
            }

        CboardListResponseDTO cboardListResponseDTO = cboardService.create(dto,uploadedFilePath);
        return ResponseEntity.ok().body(cboardListResponseDTO);
        } catch (IllegalStateException e){
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("서버 오류 발생: " + e.getMessage());
        } catch (Exception e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private static ResponseEntity<List<FieldError>> getValidated(BindingResult result) {
        if(result.hasErrors()){

            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(dm -> {
                log.warn("invalid client {} " , dm.toString());
            });

            return ResponseEntity.badRequest().body(fieldErrors);
        }

        return null;

    }

    //게시판 삭제 요청

    @DeleteMapping
    public ResponseEntity<?> deleteCboard(
            @Validated @RequestBody CboardModifyrequestDTO dto,
            @RequestParam(required = false) Integer page,
            BindingResult result
    ) {
//        log.info("/api/cboard/{} DELETE request", cboardid,page);

        ResponseEntity<List<FieldError>> fielderrors = getValidated(result);

        if(dto == null) return fielderrors;

        if (dto.getId() == null || dto.getId().trim().equals("")) {
            return ResponseEntity.badRequest().body(CboardListResponseDTO.builder().error("id를 전달해 주세요."));
        }

        // currentPage가 null일 경우 기본값으로 1을 사용하도록 처리
        int pageNumber = page != null ? page : 1;


        try {
            CboardListResponseDTO cboardListResponseDTO = cboardService.delete(dto,pageNumber);
            return ResponseEntity.ok().body(cboardListResponseDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> update(
            @Validated @RequestBody CboardModifyrequestDTO dto,
            @RequestParam(required = false) Integer page,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImg,
            BindingResult result
    ){

        log.info("/api/cboard/ PUT {} ", dto);

        ResponseEntity<List<FieldError>> fielderrors = getValidated(result);

        if(dto == null) return fielderrors;

        try {
            String uploadedFilePath = null;

            if(profileImg != null) {
                log.info("attached file name: {}", profileImg.getOriginalFilename());
                uploadedFilePath = cboardService.uploadProfileImage(profileImg);
            }

            CboardListResponseDTO cboardListResponseDTO = cboardService.update(dto,page,uploadedFilePath);
            return ResponseEntity.ok().body(cboardListResponseDTO);
        } catch ( Exception e ) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
