package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.net.Uri;
import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class v6 {

    /* renamed from: a  reason: collision with root package name */
    final String f12574a;

    /* renamed from: b  reason: collision with root package name */
    final Uri f12575b;

    /* renamed from: c  reason: collision with root package name */
    final String f12576c;

    /* renamed from: d  reason: collision with root package name */
    final String f12577d;

    /* renamed from: e  reason: collision with root package name */
    final boolean f12578e;

    /* renamed from: f  reason: collision with root package name */
    final boolean f12579f;

    /* renamed from: g  reason: collision with root package name */
    private final boolean f12580g;

    /* renamed from: h  reason: collision with root package name */
    final boolean f12581h;

    /* renamed from: i  reason: collision with root package name */
    final com.google.common.base.g<Context, Boolean> f12582i;

    public v6(Uri uri) {
        this(null, uri, BuildConfig.FLAVOR, BuildConfig.FLAVOR, false, false, false, false, null);
    }

    private v6(String str, Uri uri, String str2, String str3, boolean z4, boolean z8, boolean z9, boolean z10, com.google.common.base.g<Context, Boolean> gVar) {
        this.f12574a = str;
        this.f12575b = uri;
        this.f12576c = str2;
        this.f12577d = str3;
        this.f12578e = z4;
        this.f12579f = z8;
        this.f12580g = z9;
        this.f12581h = z10;
        this.f12582i = gVar;
    }

    public final n6<Double> a(String str, double d8) {
        return n6.b(this, str, Double.valueOf(-3.0d), true);
    }

    public final n6<Long> b(String str, long j8) {
        return n6.c(this, str, Long.valueOf(j8), true);
    }

    public final n6<String> c(String str, String str2) {
        return n6.d(this, str, str2, true);
    }

    public final n6<Boolean> d(String str, boolean z4) {
        return n6.a(this, str, Boolean.valueOf(z4), true);
    }

    public final v6 e() {
        return new v6(this.f12574a, this.f12575b, this.f12576c, this.f12577d, this.f12578e, this.f12579f, true, this.f12581h, this.f12582i);
    }

    public final v6 f() {
        if (this.f12576c.isEmpty()) {
            com.google.common.base.g<Context, Boolean> gVar = this.f12582i;
            if (gVar == null) {
                return new v6(this.f12574a, this.f12575b, this.f12576c, this.f12577d, true, this.f12579f, this.f12580g, this.f12581h, gVar);
            }
            throw new IllegalStateException("Cannot skip gservices both always and conditionally");
        }
        throw new IllegalStateException("Cannot set GServices prefix and skip GServices");
    }
}
