package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences;
import android.util.Pair;
import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l5 {

    /* renamed from: a  reason: collision with root package name */
    private final String f16754a;

    /* renamed from: b  reason: collision with root package name */
    private final String f16755b;

    /* renamed from: c  reason: collision with root package name */
    private final String f16756c;

    /* renamed from: d  reason: collision with root package name */
    private final long f16757d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ h5 f16758e;

    private l5(h5 h5Var, String str, long j8) {
        this.f16758e = h5Var;
        n6.j.f(str);
        n6.j.a(j8 > 0);
        this.f16754a = str + ":start";
        this.f16755b = str + ":count";
        this.f16756c = str + ":value";
        this.f16757d = j8;
    }

    private final long c() {
        return this.f16758e.G().getLong(this.f16754a, 0L);
    }

    private final void d() {
        this.f16758e.k();
        long a9 = this.f16758e.zzb().a();
        SharedPreferences.Editor edit = this.f16758e.G().edit();
        edit.remove(this.f16755b);
        edit.remove(this.f16756c);
        edit.putLong(this.f16754a, a9);
        edit.apply();
    }

    public final Pair<String, Long> a() {
        long abs;
        this.f16758e.k();
        this.f16758e.k();
        long c9 = c();
        if (c9 == 0) {
            d();
            abs = 0;
        } else {
            abs = Math.abs(c9 - this.f16758e.zzb().a());
        }
        long j8 = this.f16757d;
        if (abs < j8) {
            return null;
        }
        if (abs > (j8 << 1)) {
            d();
            return null;
        }
        String string = this.f16758e.G().getString(this.f16756c, null);
        long j9 = this.f16758e.G().getLong(this.f16755b, 0L);
        d();
        return (string == null || j9 <= 0) ? h5.B : new Pair<>(string, Long.valueOf(j9));
    }

    public final void b(String str, long j8) {
        this.f16758e.k();
        if (c() == 0) {
            d();
        }
        if (str == null) {
            str = BuildConfig.FLAVOR;
        }
        long j9 = this.f16758e.G().getLong(this.f16755b, 0L);
        if (j9 <= 0) {
            SharedPreferences.Editor edit = this.f16758e.G().edit();
            edit.putString(this.f16756c, str);
            edit.putLong(this.f16755b, 1L);
            edit.apply();
            return;
        }
        long j10 = j9 + 1;
        boolean z4 = (this.f16758e.g().U0().nextLong() & Long.MAX_VALUE) < Long.MAX_VALUE / j10;
        SharedPreferences.Editor edit2 = this.f16758e.G().edit();
        if (z4) {
            edit2.putString(this.f16756c, str);
        }
        edit2.putLong(this.f16755b, j10);
        edit2.apply();
    }
}
