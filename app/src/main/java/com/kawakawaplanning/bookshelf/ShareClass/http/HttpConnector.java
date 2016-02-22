package com.kawakawaplanning.bookshelf.ShareClass.http;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by KP on 15/08/13.
 */
public class HttpConnector {

    String host = "";
    String message = "";
    private OnHttpResponseListener resLis = null;
    private OnHttpErrorListener errLis = null;

    //コンストラクタここから
    public HttpConnector(){}
    public HttpConnector(String host){
        this.host += host;
    }
    public HttpConnector(String host, String message){
        this.host += host;
        this.message = message;
    }
    //コンストラクタここまで

    public void get(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(host);
                    URLConnection uc = url.openConnection();

                    InputStream is = uc.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                    StringBuffer sb = new StringBuffer();
                    String s;
                    while ((s = reader.readLine()) != null) {
                        sb.append(s);
                    }
                    if (resLis != null)
                        resLis.onResponse(sb.toString());
                    reader.close();

                } catch (IOException e) {
                    if (errLis != null)
                        errLis.onError(0);
                }
            }
        }).start();
    }

    //ポスト用コマンド
    public void post(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(host);
                    URLConnection uc = url.openConnection();
                    uc.setDoOutput(true);
                    uc.setRequestProperty("Content-type", "application/json");
                    OutputStream os = uc.getOutputStream();

                    PrintStream ps = new PrintStream(os);
                    ps.print(message);
                    ps.close();

                    InputStream is = uc.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                    StringBuffer sb = new StringBuffer();
                    String s;
                    while ((s = reader.readLine()) != null) {
                        sb.append(s);
                    }
                    final String res = sb.toString();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (resLis != null)
                                resLis.onResponse(res);
                        }
                    });
                    reader.close();

                } catch (IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (errLis != null)
                                errLis.onError(0);
                        }
                    });
                }
            }
        }).start();
    }

    //ポスト、メインスレッド外時用
    public void postNoHandler(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(host);
                    URLConnection uc = url.openConnection();
                    uc.setDoOutput(true);
                    uc.setRequestProperty("Content-type", "application/json");
                    OutputStream os = uc.getOutputStream();

                    PrintStream ps = new PrintStream(os);
                    ps.print(message);
                    ps.close();

                    InputStream is = uc.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                    StringBuffer sb = new StringBuffer();
                    String s;
                    while ((s = reader.readLine()) != null) {
                        sb.append(s);
                    }
                    if (resLis != null)
                        resLis.onResponse(sb.toString());

                    reader.close();

                } catch (IOException e) {
                    if (errLis != null)
                        errLis.onError(0);
                }
            }
        }).start();
    }

    public void setHost(String host){
        this.host = host;
    }

    public String getHost(){
        return host;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setOnHttpResponseListener(OnHttpResponseListener listener){
        this.resLis = listener;
    }

    public void setOnHttpErrorListener(OnHttpErrorListener listener){
        this.errLis = listener;
    }

    public void removeResponseListener(){
        this.resLis = null;
    }

    public void removeErreListener(){
        this.errLis = null;
    }

}

/*
サンプル

    HttpConnector httpConnector = new HttpConnector("outgroup","");
    httpConnector.setOnHttpResponseListener((String message) -> {
        if(Integer.parseInt(message) == 0){
            //成功時
        }else{
            //失敗時
        }
    });
    httpConnector.post();

 */