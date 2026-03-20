package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k5 {

    /* renamed from: a  reason: collision with root package name */
    private final String f16722a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f16723b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f16724c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f16725d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ h5 f16726e;

    public k5(h5 h5Var, String str, boolean z4) {
        this.f16726e = h5Var;
        n6.j.f(str);
        this.f16722a = str;
        this.f16723b = z4;
    }

    public final void a(boolean z4) {
        SharedPreferences.Editor edit = this.f16726e.G().edit();
        edit.putBoolean(this.f16722a, z4);
        edit.apply();
        this.f16725d = z4;
    }

    public final boolean b() {
        if (!this.f16724c) {
            this.f16724c = true;
            this.f16725d = this.f16726e.G().getBoolean(this.f16722a, this.f16723b);
        }
        return this.f16725d;
    }
}
