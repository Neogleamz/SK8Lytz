package com.example.blelibrary.protocol.standard;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Response<T> {
    private int code;
    private T payload;

    public int getCode() {
        return this.code;
    }

    public T getPayload() {
        return this.payload;
    }

    public void setCode(int i8) {
        this.code = i8;
    }

    public void setPayload(T t8) {
        this.payload = t8;
    }

    public String toString() {
        return "Response{code=" + this.code + ", payload=" + this.payload + '}';
    }
}
