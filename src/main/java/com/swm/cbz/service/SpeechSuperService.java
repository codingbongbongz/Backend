package com.swm.cbz.service;

import com.swm.cbz.config.SpeechSuperClient;
import com.swm.cbz.config.SpeechSuperConfig;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jboss.jandex.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

@Service
public class SpeechSuperService {
    public static final String baseUrl = "https://api.speechsuper.com/";

    private final String appKey;
    private final String secretKey;

    @Autowired
    public SpeechSuperService(SpeechSuperConfig speechSuperConfig) {
        this.appKey = speechSuperConfig.getKey();
        this.secretKey = speechSuperConfig.getSecret();
    }
    public String HttpAPI(byte[] audioData, String audioType, String audioSampleRate, String refText, String coreType) {
        String url = baseUrl + coreType;
        String userId = getRandomString(5);
        String res = null;
        CloseableHttpClient httpclient = new DefaultHttpClient();
        String params = buildParam(appKey, secretKey, userId, audioType, audioSampleRate, refText, coreType);
        try {

            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Request-Index", "0");

            StringBody comment = new StringBody(params, ContentType.APPLICATION_JSON);
            System.out.println(comment);
            ByteArrayBody bin = new ByteArrayBody(audioData, ContentType.create("audio/wav"), "audio");
            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("text", comment).addPart("audio", bin).build();
            httppost.setEntity(reqEntity);

            CloseableHttpResponse response = httpclient.execute(httppost);

            try {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    res = EntityUtils.toString(resEntity, "UTF-8");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    private static String buildParam(String appkey, String secretKey, String userId, String audioType, String audioSampleRate, String refText, String coreType) {

        MessageDigest digest = DigestUtils.getSha1Digest();

        long timeReqMillis = System.currentTimeMillis();
        String connectSigStr = appkey + timeReqMillis + secretKey;
        String connectSig = Hex.encodeHexString(digest.digest(connectSigStr.getBytes()));

        long timeStartMillis = System.currentTimeMillis();
        String startSigStr = appkey + timeStartMillis + userId + secretKey;
        String startSig = Hex.encodeHexString(digest.digest(startSigStr.getBytes()));
        //request param
        String params = "{"
                + "\"connect\":{"
                + "\"cmd\":\"connect\","
                + "\"param\":{"
                + "\"sdk\":{"
                + "\"protocol\":2,"
                + "\"version\":16777472,"
                + "\"source\":9"
                + "},"
                + "\"app\":{"
                + "\"applicationId\":\"" + appkey + "\","
                + "\"sig\":\"" + connectSig + "\","
                + "\"timestamp\":\"" + timeReqMillis + "\""
                + "}"
                + "}"
                + "},"
                + "\"start\":{"
                + "\"cmd\":\"start\","
                + "\"param\":{"
                + "\"app\":{"
                + "\"applicationId\":\"" + appkey + "\","
                + "\"timestamp\":\"" + timeStartMillis + "\","
                + "\"sig\":\"" + startSig + "\","
                + "\"userId\":\"" + userId + "\""
                + "},"
                + "\"audio\":{"
                + "\"sampleBytes\":2,"
                + "\"channel\":1,"
                + "\"sampleRate\":" + audioSampleRate + ","
                + "\"audioType\":\"" + audioType + "\""
                + "},"
                + "\"request\":{"
                + "\"tokenId\":\"tokenId\","
                + "\"refText\":\"" + refText + "\","
                + "\"coreType\":\"" + coreType + "\""
                + "}"
                + "}"
                + "}"
                + "}";
        return params;
    }


    private static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }

    private static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        String charString = "abcdefghijklmnopqrstuvwxyz123456789";
        int len = charString.length();
        for (int i = 0; i < length; i++) {
            sb.append(charString.charAt(getRandom(len - 1)));
        }
        return sb.toString();
    }


    public String getEvaluation(String refText, byte[] audioData) {
        String coreType = "sent.eval.kr";
        String audioType = "wav";
        String audioSampleRate = "16000";
        System.out.println(audioType);
        return HttpAPI(audioData, audioType, audioSampleRate, refText, coreType);
    }
}