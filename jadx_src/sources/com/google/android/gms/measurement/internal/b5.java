package com.google.android.gms.measurement.internal;

import android.os.Bundle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b5 {

    /* renamed from: a  reason: collision with root package name */
    public String f16333a;

    /* renamed from: b  reason: collision with root package name */
    private String f16334b;

    /* renamed from: c  reason: collision with root package name */
    private long f16335c;

    /* renamed from: d  reason: collision with root package name */
    public Bundle f16336d;

    private b5(String str, String str2, Bundle bundle, long j8) {
        this.f16333a = str;
        this.f16334b = str2;
        this.f16336d = bundle == null ? new Bundle() : bundle;
        this.f16335c = j8;
    }

    public static b5 b(zzbf zzbfVar) {
        return new b5(zzbfVar.f17263a, zzbfVar.f17265c, zzbfVar.f17264b.D0(), zzbfVar.f17266d);
    }

    public final zzbf a() {
        return new zzbf(this.f16333a, new zzba(new Bundle(this.f16336d)), this.f16334b, this.f16335c);
    }

    public final String toString() {
        String str = this.f16334b;
        String str2 = this.f16333a;
        String valueOf = String.valueOf(this.f16336d);
        return "origin=" + str + ",name=" + str2 + ",params=" + valueOf;
    }
}
