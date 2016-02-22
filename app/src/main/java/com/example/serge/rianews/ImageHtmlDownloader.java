package com.example.serge.rianews;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ImageHtmlDownloader<Token> extends HandlerThread {

    private static final String NAME = "ImageHtmlDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    Handler handler;
    Handler responseHandler;
    Listener<Token> listener;
    Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());
    Map<Token, Integer> positionMap = Collections.synchronizedMap(new HashMap<Token, Integer>());

    public interface Listener<Token> {
        void onGetImageLink(Token token, String imageLink, int position);
    }

    public void setListener(Listener<Token> listener) {
        this.listener = listener;
    }

    public ImageHtmlDownloader(Handler responseHandler) {
        super(NAME);
        this.responseHandler = responseHandler;
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == MESSAGE_DOWNLOAD) {
                    @SuppressWarnings("unchecked")
                    Token token = (Token)msg.obj;
                    handleRequest(token);
                }
            }
        };
    }

    public void queueImage(Token token, String url, int position) {
        requestMap.put(token, url);
        positionMap.put(token, position);
        handler.obtainMessage(MESSAGE_DOWNLOAD, token)
                .sendToTarget();
    }

    private void handleRequest(final Token token) {

        final String url = requestMap.get(token);

        if(url == null)
            return;

        final String imageLink = new RiaHtmlParser().getImageLink(url);

        responseHandler.post(new Runnable() {
            @Override
            public void run() {
                if(requestMap.get(token) != url)
                    return;
                requestMap.remove(token);
                listener.onGetImageLink(token, imageLink, positionMap.get(token));
                positionMap.remove(token);
            }
        });
    }

    public void clearQueue() {
        handler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }
}
