package com.google.android.exoplayer2.drm;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class UnsupportedDrmException extends Exception {

    /* renamed from: a  reason: collision with root package name */
    public final int f9606a;

    public UnsupportedDrmException(int i8) {
        this.f9606a = i8;
    }

    public UnsupportedDrmException(int i8, Exception exc) {
        super(exc);
        this.f9606a = i8;
    }
}
