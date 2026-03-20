package com.example.blelibrary.protocol;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class CloudParam {
    private int app;
    private String ip;
    private int port;
    private String uuid;

    public CloudParam() {
    }

    public CloudParam(String str, int i8, String str2, int i9) {
        this.ip = str;
        this.port = i8;
        this.uuid = str2;
        this.app = i9;
    }

    public int getApp() {
        return this.app;
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setApp(int i8) {
        this.app = i8;
    }

    public void setIp(String str) {
        this.ip = str;
    }

    public void setPort(int i8) {
        this.port = i8;
    }

    public void setUuid(String str) {
        this.uuid = str;
    }
}
