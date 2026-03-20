package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m5 {

    /* renamed from: a  reason: collision with root package name */
    private final String f16783a;

    /* renamed from: b  reason: collision with root package name */
    private final long f16784b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f16785c;

    /* renamed from: d  reason: collision with root package name */
    private long f16786d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ h5 f16787e;

    public m5(h5 h5Var, String str, long j8) {
        this.f16787e = h5Var;
        n6.j.f(str);
        this.f16783a = str;
        this.f16784b = j8;
    }

    public final long a() {
        if (!this.f16785c) {
            this.f16785c = true;
            this.f16786d = this.f16787e.G().getLong(this.f16783a, this.f16784b);
        }
        return this.f16786d;
    }

    public final void b(long j8) {
        SharedPreferences.Editor edit = this.f16787e.G().edit();
        edit.putLong(this.f16783a, j8);
        edit.apply();
        this.f16786d = j8;
    }
}
