package com.swm.cbz.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.config.AWSConfig;
import com.swm.cbz.domain.Transcript;
import com.swm.cbz.repository.TranscriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.util.UUID;

@Service
public class PollyService {
    private final TranscriptRepository transcriptRepository;
    @Autowired
    public PollyService(
                        @Value("${aws.accessKey}") String awsAccessKeyId,
                        @Value("${aws.secretKey}") String secretKey,
                        @Value("${aws.region}") String region,
                        @Value("${aws.s3.bucketName}") String bucketName,  TranscriptRepository transcriptRepository) throws IOException {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
        this.transcriptRepository = transcriptRepository;
    }


    public ApiResponse<Resource> getTranscriptAudio(Long videoId, Long transcriptId) {
        Transcript transcript = transcriptRepository.findByTranscriptIdAndVideoVideoId(transcriptId, videoId)
                .orElseThrow(() -> new EntityNotFoundException("Transcript not found with id " + transcriptId + " for video id " + videoId));

        String audioKey = transcript.getSoundLink();
        String bucketName = "bucket name";
        byte[] content = getAudioContent(bucketName, audioKey);

        ByteArrayResource resource = new ByteArrayResource(content);

        return ApiResponse.success(SuccessMessage.GET_TRANSCRIPT_AUDIO_SUCCESS, resource);
    }

    private byte[] getAudioContent(String bucketName, String key) {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2).build();
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        S3Object s3Object = s3Client.getObject(getObjectRequest);

        try (S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent()) {
            return IOUtils.toByteArray(s3ObjectInputStream);
        } catch (IOException e) {
            throw new RuntimeException("S3에서 파일을 불러오는데 실패하였습니다", e);
        }
    }



}
