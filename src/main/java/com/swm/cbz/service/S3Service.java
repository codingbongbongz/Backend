package com.swm.cbz.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.domain.Transcript;
import com.swm.cbz.repository.TranscriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.net.URL;

@Service
public class S3Service {
    private final TranscriptRepository transcriptRepository;
    private final AmazonS3 s3Client;
    private final String bucketName;
    @Autowired
    public S3Service(
                        @Value("${aws.accessKey}") String awsAccessKeyId,
                        @Value("${aws.secretKey}") String secretKey,
                        @Value("${aws.region}") String region,
                        @Value("${aws.s3.bucketName}") String bucketName,  TranscriptRepository transcriptRepository) throws IOException {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2.getName())
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
        this.bucketName = bucketName;
        this.transcriptRepository = transcriptRepository;
    }

    public String uploadProfileImage(MultipartFile file, Long userId) {
        try {
            byte[] bytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();

            String uniqueFileName = userId + "-" + originalFilename;

            s3Client.putObject(new PutObjectRequest(bucketName, uniqueFileName, new ByteArrayInputStream(bytes), new ObjectMetadata()));

            return uniqueFileName;

        } catch (IOException e) {
            throw new RuntimeException("Error uploading profile image to S3", e);
        }
    }


    public ResponseEntity<byte[]> getTranscriptAudio(Long videoId, Long transcriptId) {
        Transcript transcript = transcriptRepository.findByTranscriptIdAndVideoVideoId(transcriptId, videoId)
                .orElseThrow(() -> new EntityNotFoundException("Transcript not found with id " + transcriptId + " for video id " + videoId));

        String audioKey = transcript.getSoundLink();
        byte[] content = getAudioContent(bucketName, audioKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/mpeg"));
        headers.setContentLength(content.length);

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }


    private byte[] getAudioContent(String bucketName, String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        S3Object s3Object = s3Client.getObject(getObjectRequest);

        try (S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent()) {
            return IOUtils.toByteArray(s3ObjectInputStream);
        } catch (IOException e) {
            throw new RuntimeException("S3에서 파일을 불러오는데 실패하였습니다", e);
        }
    }

    public String uploadAudio(byte[] audioBytes, String fileName) {
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, new ByteArrayInputStream(audioBytes), null));

        /*
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName);
        URL s3Url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        */
        return fileName;

    }


}
