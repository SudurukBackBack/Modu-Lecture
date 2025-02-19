package com.sudurukBackBack.Modu_Lecture.domain.storage.service;

import com.sudurukBackBack.Modu_Lecture.domain.storage.exception.S3UploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3UploadService {

    private final S3Client s3Client;
    private final String bucketName = "${cloud.aws.s3.bucket-name}";

    /**
     * 로컬 파일을 S3에 업로드합니다.
     *
     * @param localFilePath 업로드할 로컬 파일 경로
     * @param s3Key         S3 버킷 내의 파일 키
     * @return 업로드된 파일의 S3 URL
     */
    public String uploadFileToS3(String localFilePath, String s3Key) {
        File file = new File(localFilePath);
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

            return s3Client.utilities().getUrl(b -> b.bucket(bucketName).key(s3Key)).toExternalForm();

        } catch (Exception e) {
            throw new S3UploadException();
        }
    }


}
