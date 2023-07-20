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

    @Value("${aws.bucketNames.profileBucket}")
    private String profileBucketName;

    @Value("${aws.bucketNames.marketBucket}")
    private String marketBucketName;

    @Value("${aws.bucketNames.snsBucket}")
    private String snsBucketName;

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
    public String uploadToS3Bucket(byte[] uploadFile, String fileName, String serviceName) {
        // serviceName에 따라 다른 버킷 이름 선택
        String bucketName = getBucketName(serviceName);

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
        String bucketName = getBucketNameFromLink(fileLink);
        String key = getKeyFromLink(fileLink);

        log.info("bucketName : {}, key : {}", bucketName, key);

        // 파일 삭제 요청 생성
        s3.deleteObject(builder -> builder.bucket(bucketName).key(key));

        log.info("File deleted successfully: {}", fileLink);
    }

    // serviceName에 따라 다른 버킷 이름 반환
    private String getBucketName(String serviceName) {
        switch (serviceName.toUpperCase()) {
            case "USER":
                return profileBucketName;
            case "MARKET":
                return marketBucketName;
            case "SNS":
                return snsBucketName;
            default:
                throw new RuntimeException("올바르지 않은 serviceName : " + serviceName);
        }
    }

    // fileLink에서 버킷 이름 추출
    private String getBucketNameFromLink(String fileLink) {
        try {
            URI uri = new URI(fileLink);
            String scheme = uri.getScheme();
            if (!"https".equalsIgnoreCase(scheme)) {
                throw new RuntimeException("Invalid fileLink scheme: " + scheme);
            }

            String host = uri.getHost();
            int endIndex = host.indexOf(".s3.ap-northeast-2.amazonaws.com");
            if (endIndex == -1) {
              throw new RuntimeException("Invalid fileLink format: " + fileLink);
            }
            return host.substring(0, endIndex);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid fileLink format: " + fileLink);
        }
    }

    // fileLink에서 파일 키 추출
    private String getKeyFromLink(String fileLink) {
        try {
            URI uri = new URI(fileLink);
            return uri.getPath().startsWith("/") ? uri.getPath().substring(1) : uri.getPath();
        } catch (URISyntaxException e) {
            log.error("Invalid fileLink format: {}", fileLink);
            throw new RuntimeException("Invalid fileLink format");
        }
    }

}
