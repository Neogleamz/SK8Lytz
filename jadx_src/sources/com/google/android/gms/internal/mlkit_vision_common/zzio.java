package com.google.android.gms.internal.mlkit_vision_common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public enum zzio implements h {
    SOURCE_UNKNOWN(0),
    BITMAP(1),
    BYTEARRAY(2),
    BYTEBUFFER(3),
    FILEPATH(4),
    ANDROID_MEDIA_IMAGE(5);
    

    /* renamed from: a  reason: collision with root package name */
    private final int f16152a;

    zzio(int i8) {
        this.f16152a = i8;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.h
    public final int zza() {
        return this.f16152a;
    }
}
