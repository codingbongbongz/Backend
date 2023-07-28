package com.swm.cbz.service;

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
import com.swm.cbz.config.AWSConfig;
import com.swm.cbz.repository.TranscriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.UUID;

@Service
public class PollyService {
    private String bucketName;
    private String defaultKey;
    private TranscriptRepository transcriptRepository;
    private final AmazonPolly pollyClient;
    private AmazonS3 s3Client;


    @Autowired
    public PollyService(AWSConfig awsConfig, TranscriptRepository transcriptRepository) throws IOException {
        this.pollyClient = AmazonPollyClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();

        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2) // Specify your region here.
                .build();

        this.bucketName = awsConfig.getBucketName();
        this.defaultKey = awsConfig.getDefaultKey();
        this.transcriptRepository = transcriptRepository;
    }

    public String synthesizeAndStore(String sentence) throws IOException {
        // Polly 생성
        SynthesizeSpeechRequest synthesizeSpeechRequest = new SynthesizeSpeechRequest()
                .withOutputFormat(OutputFormat.Mp3)
                .withVoiceId("Joanna")
                .withText(sentence);

        SynthesizeSpeechResult synthesizeSpeechResult = pollyClient.synthesizeSpeech(synthesizeSpeechRequest);

        String key = defaultKey + "-" + UUID.randomUUID().toString() + ".mp3"; // Generating unique key using transcriptId
        File audioFile = File.createTempFile(key, ".mp3");
        try (InputStream inStream = synthesizeSpeechResult.getAudioStream();
             OutputStream outStream = new FileOutputStream(audioFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
        }

        // S3 업로드
        s3Client.putObject(new PutObjectRequest(bucketName, key, audioFile));
        // S3 링크 가져오기

        return s3Client.getUrl(bucketName, key).toExternalForm();
    }

    public ResponseEntity<Resource> getAudio(String bucketName, String key) {
        byte[] content = getAudioContent(bucketName, key);
        return buildResponse(content);
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

    private ResponseEntity<Resource> buildResponse(byte[] content) {
        ByteArrayResource resource = new ByteArrayResource(content);

        return ResponseEntity.ok()
                .contentLength(content.length)
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(resource);
    }


}
