package com.example.blelibrary.protocol;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ConfigRouterParam {
    private String password;
    private String ssid;

    public ConfigRouterParam() {
    }

    public ConfigRouterParam(String str, String str2) {
        this.ssid = str;
        this.password = str2;
    }

    public String getPassword() {
        return this.password;
    }

    public String getSsid() {
        return this.ssid;
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public void setSsid(String str) {
        this.ssid = str;
    }
}
