package com.example.blelibrary.protocol;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class WifiNetworkInfo {
    private int LANcode;
    private String MAC;
    private String MACID;
    private String MID;
    private int WANcode;
    private String ip;
    private String lver;
    private int time;

    public String getIp() {
        return this.ip;
    }

    public int getLANcode() {
        return this.LANcode;
    }

    public String getLver() {
        return this.lver;
    }

    public String getMAC() {
        return this.MAC;
    }

    public String getMACID() {
        return this.MACID;
    }

    public String getMID() {
        return this.MID;
    }

    public int getTime() {
        return this.time;
    }

    public int getWANcode() {
        return this.WANcode;
    }

    public void setIp(String str) {
        this.ip = str;
    }

    public void setLANcode(int i8) {
        this.LANcode = i8;
    }

    public void setLver(String str) {
        this.lver = str;
    }

    public void setMAC(String str) {
        this.MAC = str;
    }

    public void setMACID(String str) {
        this.MACID = str;
    }

    public void setMID(String str) {
        this.MID = str;
    }

    public void setTime(int i8) {
        this.time = i8;
    }

    public void setWANcode(int i8) {
        this.WANcode = i8;
    }

    public String toString() {
        return "WifiNetworkInfo{MAC='" + this.MAC + "', ip='" + this.ip + "', MID='" + this.MID + "', MACID='" + this.MACID + "', LANcode=" + this.LANcode + ", WANcode=" + this.WANcode + ", lver='" + this.lver + "', time=" + this.time + '}';
    }
}
