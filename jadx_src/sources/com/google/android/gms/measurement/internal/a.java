package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.os.Bundle;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends w1 {

    /* renamed from: b  reason: collision with root package name */
    private final Map<String, Long> f16294b;

    /* renamed from: c  reason: collision with root package name */
    private final Map<String, Integer> f16295c;

    /* renamed from: d  reason: collision with root package name */
    private long f16296d;

    public a(f6 f6Var) {
        super(f6Var);
        this.f16295c = new k0.a();
        this.f16294b = new k0.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void A(a aVar, String str, long j8) {
        aVar.k();
        n6.j.f(str);
        Integer num = aVar.f16295c.get(str);
        if (num == null) {
            aVar.i().E().b("Call to endAdUnitExposure for unknown ad unit id", str);
            return;
        }
        x8 A = aVar.q().A(false);
        int intValue = num.intValue() - 1;
        if (intValue != 0) {
            aVar.f16295c.put(str, Integer.valueOf(intValue));
            return;
        }
        aVar.f16295c.remove(str);
        Long l8 = aVar.f16294b.get(str);
        if (l8 == null) {
            aVar.i().E().a("First ad unit exposure time was never set");
        } else {
            aVar.f16294b.remove(str);
            aVar.y(str, j8 - l8.longValue(), A);
        }
        if (aVar.f16295c.isEmpty()) {
            long j9 = aVar.f16296d;
            if (j9 == 0) {
                aVar.i().E().a("First ad exposure time was never set");
                return;
            }
            aVar.u(j8 - j9, A);
            aVar.f16296d = 0L;
        }
    }

    private final void u(long j8, x8 x8Var) {
        if (x8Var == null) {
            i().I().a("Not logging ad exposure. No active activity");
        } else if (j8 < 1000) {
            i().I().b("Not logging ad exposure. Less than 1000 ms. exposure", Long.valueOf(j8));
        } else {
            Bundle bundle = new Bundle();
            bundle.putLong("_xt", j8);
            sb.V(x8Var, bundle, true);
            p().A0("am", "_xa", bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void w(a aVar, String str, long j8) {
        aVar.k();
        n6.j.f(str);
        if (aVar.f16295c.isEmpty()) {
            aVar.f16296d = j8;
        }
        Integer num = aVar.f16295c.get(str);
        if (num != null) {
            aVar.f16295c.put(str, Integer.valueOf(num.intValue() + 1));
        } else if (aVar.f16295c.size() >= 100) {
            aVar.i().J().a("Too many ads visible");
        } else {
            aVar.f16295c.put(str, 1);
            aVar.f16294b.put(str, Long.valueOf(j8));
        }
    }

    private final void y(String str, long j8, x8 x8Var) {
        if (x8Var == null) {
            i().I().a("Not logging ad unit exposure. No active activity");
        } else if (j8 < 1000) {
            i().I().b("Not logging ad unit exposure. Less than 1000 ms. exposure", Long.valueOf(j8));
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("_ai", str);
            bundle.putLong("_xt", j8);
            sb.V(x8Var, bundle, true);
            p().A0("am", "_xu", bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void z(long j8) {
        for (String str : this.f16294b.keySet()) {
            this.f16294b.put(str, Long.valueOf(j8));
        }
        if (this.f16294b.isEmpty()) {
            return;
        }
        this.f16296d = j8;
    }

    public final void B(String str, long j8) {
        if (str == null || str.length() == 0) {
            i().E().a("Ad unit id must be a non-empty string");
        } else {
            l().B(new y(this, str, j8));
        }
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ e a() {
        return super.a();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ d b() {
        return super.b();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ x c() {
        return super.c();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ s4 e() {
        return super.e();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ h5 f() {
        return super.f();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ sb g() {
        return super.g();
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void h() {
        super.h();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ x4 i() {
        return super.i();
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void j() {
        super.j();
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void k() {
        super.k();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ a6 l() {
        return super.l();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ a m() {
        return super.m();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ r4 n() {
        return super.n();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ q4 o() {
        return super.o();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ h7 p() {
        return super.p();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ z8 q() {
        return super.q();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ f9 r() {
        return super.r();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ na s() {
        return super.s();
    }

    public final void t(long j8) {
        x8 A = q().A(false);
        for (String str : this.f16294b.keySet()) {
            y(str, j8 - this.f16294b.get(str).longValue(), A);
        }
        if (!this.f16294b.isEmpty()) {
            u(j8 - this.f16296d, A);
        }
        z(j8);
    }

    public final void x(String str, long j8) {
        if (str == null || str.length() == 0) {
            i().E().a("Ad unit id must be a non-empty string");
        } else {
            l().B(new v0(this, str, j8));
        }
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ Context zza() {
        return super.zza();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ u6.d zzb() {
        return super.zzb();
    }
}
