package com.swm.cbz.config;

import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.client.WebSocketClient;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

public class SpeechSuperClient extends WebSocketClient {

    private Map<String, String> app;

    public Map<String, String> getApp() {
        return app;
    }

    public void setApp(Map<String, String> app) {
        this.app = app;
    }
    public int bytes;
    public ArrayList<Object> param;
    public String audioPath;

    public SpeechSuperClient(String url, ArrayList<Object> param, String audioPath) throws URISyntaxException {
        super(new URI(url));
        this.param = param;
        this.audioPath = audioPath;
    }

    public void onOpen(ServerHandshake serverHandshake) {
        this.send(param.get(0).toString());
        this.send(param.get(1).toString());
        try {
            FileInputStream fs = new FileInputStream(audioPath);
            byte[] buffer = new byte[1024];
            while ((bytes = fs.read(buffer)) > 0) {
                //send audio
                this.send(buffer);
            }
            fs.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.send(param.get(2).toString());
    }

    @Override
    public void onMessage(String msg) {
        //Receive results
        System.out.println("result===>" + msg);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        //Disconnect
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}