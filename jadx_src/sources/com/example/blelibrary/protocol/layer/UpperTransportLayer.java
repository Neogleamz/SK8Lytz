package com.example.blelibrary.protocol.layer;

import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class UpperTransportLayer {
    private static final byte HEX = 1;
    private static final byte JSON = 0;
    private boolean ack;
    private byte cmdId;
    private byte[] payload;
    private boolean protect;
    private byte seq;

    public UpperTransportLayer(boolean z4, boolean z8, byte b9, byte b10, byte[] bArr) {
        this.ack = z4;
        this.protect = z8;
        this.seq = b9;
        this.cmdId = b10;
        this.payload = bArr;
    }

    public static UpperTransportLayer createUpper(boolean z4, boolean z8, byte b9, byte b10, byte[] bArr) {
        return new UpperTransportLayer(z4, z8, b9, b10, bArr);
    }

    public byte getCmdId() {
        return this.cmdId;
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public byte getSeq() {
        return this.seq;
    }

    public boolean isAck() {
        return this.ack;
    }

    public boolean isProtect() {
        return this.protect;
    }

    public String toString() {
        return "UpperTransportLayer{ack=" + this.ack + ", protect=" + this.protect + ", seq=" + ((int) this.seq) + ", cmdId=" + ((int) this.cmdId) + ", payload=" + Arrays.toString(this.payload) + '}';
    }
}
