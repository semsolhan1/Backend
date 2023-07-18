package com.handifarm.aws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

@Component
@Slf4j
public class S3Service {

    private S3Client s3;

    @Value("${aws.credentials.accessKey}")
    private String accessKey;

    @Value("${aws.credentials.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.bucketName}")
    private String bucketName;

    @PostConstruct
    private void initializeAmazon() {
        // 액세스 키와 시크릿 키를 이용해서 계정 인증 받기
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        this.s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    /**
     * 버킷에 파일을 업로드하고, 업로드 한 버킷의 url 정보를 리턴
     * @param uploadFile - 업로드 할 파일의 실제 raw 데이터
     * @param fileName - 업로드 할 파일명
     * @return - 버킷에 업로드 된 버킷 경로 (url)
     */
    public String uploadToS3Bucket(byte[] uploadFile, String fileName) {

        // 업로드 할 파일을 S3 객체로 생성
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName) // 버킷 이름
                .key(fileName) // 파일명
                .build();

        // 오브젝트를 버킷에 업로드
        s3.putObject(request, RequestBody.fromBytes(uploadFile));

        // 업로드 된 파일의 url 을 반환
        return s3.utilities().getUrl(b -> b.bucket(bucketName).key(fileName)).toString();
    }

    public void deleteFromS3Bucket(String fileLink) {

        // fileLink에서 버킷 이름과 파일 키를 추출
        String bucketName;
        String key;

        try {
            URI uri = new URI(fileLink);
            bucketName = uri.getHost();
            key = uri.getPath().startsWith("/") ? uri.getPath().substring(1) : uri.getPath();
        } catch (URISyntaxException e) {
            log.error("Invalid fileLink format: {}", fileLink);
            return;
        }

        // 파일 삭제 요청 생성
        s3.deleteObject(builder -> builder.bucket(bucketName).key(key));

        log.info("File deleted successfully: {}", fileLink);

    }

}
