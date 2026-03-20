package com.example.blelibrary.protocol;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class WifiState {
    private String gw;
    private String ip;
    private String mask;
    private int state;

    public String getGw() {
        return this.gw;
    }

    public String getIp() {
        return this.ip;
    }

    public String getMask() {
        return this.mask;
    }

    public int getState() {
        return this.state;
    }

    public void setGw(String str) {
        this.gw = str;
    }

    public void setIp(String str) {
        this.ip = str;
    }

    public void setMask(String str) {
        this.mask = str;
    }

    public void setState(int i8) {
        this.state = i8;
    }

    public String toString() {
        return "WifiState{state=" + this.state + ", ip='" + this.ip + "', gw='" + this.gw + "', mask='" + this.mask + "'}";
    }
}
