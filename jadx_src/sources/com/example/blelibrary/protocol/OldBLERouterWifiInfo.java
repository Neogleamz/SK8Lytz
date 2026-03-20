package com.example.blelibrary.protocol;

import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class OldBLERouterWifiInfo {
    private String auth;
    private String bssid;
    private String chan;
    private String rssi;
    private String ssid;

    public OldBLERouterWifiInfo(String str, String str2, String str3, String str4) {
        this.chan = str;
        this.bssid = str2;
        this.rssi = str3;
        this.ssid = str4;
    }

    public int getAuth() {
        return Integer.parseInt(this.auth);
    }

    public String getBssid() {
        return this.bssid;
    }

    public String getChan() {
        return this.chan;
    }

    public int getRssi() {
        return Integer.parseInt(this.rssi);
    }

    public String getSsid() {
        return this.ssid;
    }

    public void setAuth(String str) {
        this.auth = str;
    }

    public void setBssid(String str) {
        this.bssid = str;
    }

    public void setChan(String str) {
        this.chan = str;
    }

    public void setRssi(String str) {
        this.rssi = str;
    }

    public void setSsid(String str) {
        this.ssid = str;
    }

    public String toString() {
        Locale locale = Locale.ENGLISH;
        return String.format(locale, this.ssid + this.bssid + this.rssi + this.chan, new Object[0]);
    }

    public BLERouterWifiInfo transNew() {
        BLERouterWifiInfo bLERouterWifiInfo = new BLERouterWifiInfo();
        bLERouterWifiInfo.setCh(Integer.parseInt(this.chan));
        bLERouterWifiInfo.setB(this.bssid);
        bLERouterWifiInfo.setR(Integer.parseInt(this.rssi));
        bLERouterWifiInfo.setA(Integer.parseInt(this.auth));
        bLERouterWifiInfo.setS(this.ssid);
        return bLERouterWifiInfo;
    }
}
