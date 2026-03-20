package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class na extends v4 {

    /* renamed from: c  reason: collision with root package name */
    private Handler f16839c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f16840d;

    /* renamed from: e  reason: collision with root package name */
    protected final ua f16841e;

    /* renamed from: f  reason: collision with root package name */
    protected final sa f16842f;

    /* renamed from: g  reason: collision with root package name */
    private final ra f16843g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public na(f6 f6Var) {
        super(f6Var);
        this.f16840d = true;
        this.f16841e = new ua(this);
        this.f16842f = new sa(this);
        this.f16843g = new ra(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void A(na naVar, long j8) {
        naVar.k();
        naVar.E();
        naVar.i().I().b("Activity paused, time", Long.valueOf(j8));
        naVar.f16843g.b(j8);
        if (naVar.a().R()) {
            naVar.f16842f.e(j8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void E() {
        k();
        if (this.f16839c == null) {
            this.f16839c = new com.google.android.gms.internal.measurement.b2(Looper.getMainLooper());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void G(na naVar, long j8) {
        naVar.k();
        naVar.E();
        naVar.i().I().b("Activity resumed, time", Long.valueOf(j8));
        if (!naVar.a().r(c0.Q0) ? naVar.a().R() || naVar.f().f16618u.b() : naVar.a().R() || naVar.f16840d) {
            naVar.f16842f.f(j8);
        }
        naVar.f16843g.a();
        ua uaVar = naVar.f16841e;
        uaVar.f17033a.k();
        if (uaVar.f17033a.f16485a.n()) {
            uaVar.b(uaVar.f17033a.zzb().a(), false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void B(boolean z4) {
        k();
        this.f16840d = z4;
    }

    public final boolean C(boolean z4, boolean z8, long j8) {
        return this.f16842f.d(z4, z8, j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean D() {
        k();
        return this.f16840d;
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

    @Override // com.google.android.gms.measurement.internal.v4
    protected final boolean y() {
        return false;
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
