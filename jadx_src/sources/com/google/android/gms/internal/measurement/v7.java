package com.google.android.gms.internal.measurement;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class v7 {

    /* renamed from: a  reason: collision with root package name */
    private final zzja f12583a;

    /* renamed from: b  reason: collision with root package name */
    private final byte[] f12584b;

    private v7(int i8) {
        byte[] bArr = new byte[i8];
        this.f12584b = bArr;
        this.f12583a = zzja.H(bArr);
    }

    public final zzij a() {
        this.f12583a.I();
        return new a8(this.f12584b);
    }

    public final zzja b() {
        return this.f12583a;
    }
}
