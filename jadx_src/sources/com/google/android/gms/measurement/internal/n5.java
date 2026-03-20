package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n5 {

    /* renamed from: a  reason: collision with root package name */
    private final String f16822a;

    /* renamed from: b  reason: collision with root package name */
    private final String f16823b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f16824c;

    /* renamed from: d  reason: collision with root package name */
    private String f16825d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ h5 f16826e;

    public n5(h5 h5Var, String str, String str2) {
        this.f16826e = h5Var;
        n6.j.f(str);
        this.f16822a = str;
        this.f16823b = null;
    }

    public final String a() {
        if (!this.f16824c) {
            this.f16824c = true;
            this.f16825d = this.f16826e.G().getString(this.f16822a, null);
        }
        return this.f16825d;
    }

    public final void b(String str) {
        SharedPreferences.Editor edit = this.f16826e.G().edit();
        edit.putString(this.f16822a, str);
        edit.apply();
        this.f16825d = str;
    }
}
