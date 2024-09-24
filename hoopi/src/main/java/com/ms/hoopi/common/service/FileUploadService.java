package com.ms.hoopi.common.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ms.hoopi.constants.Constants;
import com.ms.hoopi.common.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final AmazonS3Client amazonS3Client;
    private final AmazonS3 amazonS3;
    private final CommonUtil commonUtil;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public ResponseEntity<?> uploadFile(List<MultipartFile> files, List<String> keys) {
        List<String> fileUrls = new ArrayList<>();
        try {
            for(int i = 0; i < files.size(); i++) {
                String fileKey = keys.get(i);
                String fileUrl= "https://" + bucket + "/" + fileKey;
                ObjectMetadata metadata= new ObjectMetadata();
                metadata.setContentType(files.get(i).getContentType());
                metadata.setContentLength(files.get(i).getSize());
                amazonS3Client.putObject(bucket,fileKey,files.get(i).getInputStream(),metadata);
                fileUrls.add(fileUrl);
        }
            return ResponseEntity.ok(fileUrls);
        } catch (IOException e) {
            log.error(Constants.FILE_UPLOAD_FAIL, e);
            return ResponseEntity.badRequest().body(Constants.FILE_UPLOAD_FAIL);
        } catch (AmazonS3Exception e) {
            log.error(Constants.AWS_S3_FAIL, e);
            return ResponseEntity.badRequest().body(Constants.AWS_S3_FAIL);
        } catch (AmazonClientException e) {
            log.error(Constants.AWS_S3_CLIENT_FAIL, e);
            return ResponseEntity.badRequest().body(Constants.AWS_S3_CLIENT_FAIL);
        } catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    public String getS3(String key) {
        return amazonS3.getUrl(bucket, key).toString() == null? "" : amazonS3.getUrl(bucket, key).toString();
    }

}
