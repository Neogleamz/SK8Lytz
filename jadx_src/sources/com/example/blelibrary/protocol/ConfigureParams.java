package com.example.blelibrary.protocol;

import java.io.Serializable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ConfigureParams implements Serializable {
    private String capabilities;
    private String password;
    private byte[] ssid;

    public ConfigureParams() {
    }

    public ConfigureParams(byte[] bArr, String str, int i8) {
        this.ssid = bArr;
        this.password = str;
        this.capabilities = i8 != 0 ? i8 != 1 ? i8 != 2 ? i8 != 3 ? "unknown" : "WPA/WPA2 - Enterprise" : "WPA/WPA2 - PSK" : "wep" : "open";
    }

    public static String getCapabilities(int i8) {
        return i8 != 0 ? i8 != 1 ? i8 != 2 ? i8 != 3 ? i8 != 4 ? i8 != 5 ? "unknown" : "WPA_ENT" : "WPA_WPA2_PSK" : "WPA/WPA2 - Enterprise" : "WPA/WPA2 - PSK" : "wep" : "open";
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public int getAuth() {
        char c9;
        String str = this.capabilities;
        switch (str.hashCode()) {
            case -585420989:
                if (str.equals("WPA/WPA2 - Enterprise")) {
                    c9 = 3;
                    break;
                }
                c9 = 65535;
                break;
            case -569742042:
                if (str.equals("WPA/WPA2 - PSK")) {
                    c9 = 2;
                    break;
                }
                c9 = 65535;
                break;
            case -284840886:
                if (str.equals("unknown")) {
                    c9 = 5;
                    break;
                }
                c9 = 65535;
                break;
            case 117602:
                if (str.equals("wep")) {
                    c9 = 1;
                    break;
                }
                c9 = 65535;
                break;
            case 3417674:
                if (str.equals("open")) {
                    c9 = 0;
                    break;
                }
                c9 = 65535;
                break;
            default:
                c9 = 65535;
                break;
        }
        if (c9 != 0) {
            if (c9 != 1) {
                if (c9 != 2) {
                    return c9 != 3 ? -1 : 3;
                }
                return 2;
            }
            return 1;
        }
        return 0;
    }

    public String getCapabilities() {
        return this.capabilities;
    }

    public String getPassword() {
        return this.password;
    }

    public byte[] getSsid() {
        return this.ssid;
    }

    public void setCapabilities(int i8) {
        this.capabilities = i8 != 0 ? i8 != 1 ? i8 != 2 ? i8 != 3 ? "unknown" : "WPA/WPA2 - Enterprise" : "WPA/WPA2 - PSK" : "wep" : "open";
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public void setSsid(byte[] bArr) {
        this.ssid = bArr;
    }
}
