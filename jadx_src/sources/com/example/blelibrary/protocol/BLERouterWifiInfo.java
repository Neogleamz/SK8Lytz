package com.example.blelibrary.protocol;

import java.io.Serializable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class BLERouterWifiInfo implements Serializable {

    /* renamed from: a  reason: collision with root package name */
    private int f8882a;

    /* renamed from: b  reason: collision with root package name */
    private String f8883b;

    /* renamed from: ch  reason: collision with root package name */
    private int f8884ch;

    /* renamed from: r  reason: collision with root package name */
    private int f8885r;

    /* renamed from: s  reason: collision with root package name */
    private String f8886s;

    public int getA() {
        return this.f8882a;
    }

    public String getB() {
        return this.f8883b;
    }

    public int getCh() {
        return this.f8884ch;
    }

    public int getR() {
        return this.f8885r;
    }

    public String getS() {
        return this.f8886s;
    }

    public void setA(int i8) {
        this.f8882a = i8;
    }

    public void setB(String str) {
        this.f8883b = str;
    }

    public void setCh(int i8) {
        this.f8884ch = i8;
    }

    public void setR(int i8) {
        this.f8885r = i8;
    }

    public void setS(String str) {
        this.f8886s = str;
    }

    public String toString() {
        return "BLEWifiInfo{ch=" + this.f8884ch + ", b='" + this.f8883b + "', r=" + this.f8885r + ", a=" + this.f8882a + ", s='" + this.f8886s + "'}";
    }
}
