package com.example.blelibrary.protocol.standard;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Request<T> {
    private int cmdId;
    private T payload;

    public Request() {
    }

    public Request(int i8, T t8) {
        this.cmdId = i8;
        this.payload = t8;
    }

    public int getCmdId() {
        return this.cmdId;
    }

    public T getPayload() {
        return this.payload;
    }

    public void setCmdId(int i8) {
        this.cmdId = i8;
    }

    public void setPayload(T t8) {
        this.payload = t8;
    }
}
