package com.example.seedpoint.event;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class NetworkInfo {
    private String ip;
    private String network_type;
    private boolean wifi;
    private String wifi_signal;
    private String wifi_ssid;

    public String getIp() {
        return this.ip;
    }

    public String getNetwork_type() {
        return this.network_type;
    }

    public String getWifi_signal() {
        return this.wifi_signal;
    }

    public String getWifi_ssid() {
        return this.wifi_ssid;
    }

    public boolean isWifi() {
        return this.wifi;
    }

    public void setIp(String str) {
        this.ip = str;
    }

    public void setNetwork_type(String str) {
        this.network_type = str;
    }

    public void setWifi(boolean z4) {
        this.wifi = z4;
    }

    public void setWifi_signal(String str) {
        this.wifi_signal = str;
    }

    public void setWifi_ssid(String str) {
        this.wifi_ssid = str;
    }
}
