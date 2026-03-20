package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f9 extends v4 {

    /* renamed from: c  reason: collision with root package name */
    private final ba f16528c;

    /* renamed from: d  reason: collision with root package name */
    private f7.d f16529d;

    /* renamed from: e  reason: collision with root package name */
    private volatile Boolean f16530e;

    /* renamed from: f  reason: collision with root package name */
    private final t f16531f;

    /* renamed from: g  reason: collision with root package name */
    private final wa f16532g;

    /* renamed from: h  reason: collision with root package name */
    private final List<Runnable> f16533h;

    /* renamed from: i  reason: collision with root package name */
    private final t f16534i;

    /* JADX INFO: Access modifiers changed from: protected */
    public f9(f6 f6Var) {
        super(f6Var);
        this.f16533h = new ArrayList();
        this.f16532g = new wa(f6Var.zzb());
        this.f16528c = new ba(this);
        this.f16531f = new i9(this, f6Var);
        this.f16534i = new q9(this, f6Var);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void I(f9 f9Var, ComponentName componentName) {
        f9Var.k();
        if (f9Var.f16529d != null) {
            f9Var.f16529d = null;
            f9Var.i().I().b("Disconnected from device MeasurementService", componentName);
            f9Var.k();
            f9Var.W();
        }
    }

    private final void N(Runnable runnable) {
        k();
        if (a0()) {
            runnable.run();
        } else if (this.f16533h.size() >= 1000) {
            i().E().a("Discarding data. Max runnable queue size reached");
        } else {
            this.f16533h.add(runnable);
            this.f16534i.b(60000L);
            W();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void e0() {
        k();
        i().I().b("Processing queued up service tasks", Integer.valueOf(this.f16533h.size()));
        for (Runnable runnable : this.f16533h) {
            try {
                runnable.run();
            } catch (RuntimeException e8) {
                i().E().b("Task exception while flushing queue", e8);
            }
        }
        this.f16533h.clear();
        this.f16534i.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void f0() {
        k();
        this.f16532g.c();
        this.f16531f.b(c0.L.a(null).longValue());
    }

    private final zzn h0(boolean z4) {
        return n().z(z4 ? i().M() : null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void j0(f9 f9Var) {
        f9Var.k();
        if (f9Var.a0()) {
            f9Var.i().I().a("Inactivity, disconnecting from the service");
            f9Var.X();
        }
    }

    public final void A(Bundle bundle) {
        k();
        t();
        N(new r9(this, h0(false), bundle));
    }

    public final void B(com.google.android.gms.internal.measurement.h2 h2Var) {
        k();
        t();
        N(new m9(this, h0(false), h2Var));
    }

    public final void C(com.google.android.gms.internal.measurement.h2 h2Var, zzbf zzbfVar, String str) {
        k();
        t();
        if (g().s(com.google.android.gms.common.d.f11721a) == 0) {
            N(new t9(this, zzbfVar, str, h2Var));
            return;
        }
        i().J().a("Not bundling data. Service unavailable or out of date");
        g().T(h2Var, new byte[0]);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void D(com.google.android.gms.internal.measurement.h2 h2Var, String str, String str2) {
        k();
        t();
        N(new z9(this, str, str2, h0(false), h2Var));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void E(com.google.android.gms.internal.measurement.h2 h2Var, String str, String str2, boolean z4) {
        k();
        t();
        N(new h9(this, str, str2, h0(false), z4, h2Var));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void F(zzac zzacVar) {
        n6.j.l(zzacVar);
        k();
        t();
        N(new x9(this, true, h0(true), o().C(zzacVar), new zzac(zzacVar), zzacVar));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void G(zzbf zzbfVar, String str) {
        n6.j.l(zzbfVar);
        k();
        t();
        N(new u9(this, true, h0(true), o().D(zzbfVar), zzbfVar, str));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void H(x8 x8Var) {
        k();
        t();
        N(new o9(this, x8Var));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void K(zzno zznoVar) {
        k();
        t();
        N(new l9(this, h0(true), o().E(zznoVar), zznoVar));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void L(f7.d dVar) {
        k();
        n6.j.l(dVar);
        this.f16529d = dVar;
        f0();
        e0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void M(f7.d dVar, AbstractSafeParcelable abstractSafeParcelable, zzn zznVar) {
        int i8;
        z4 E;
        String str;
        k();
        t();
        int i9 = 0;
        int i10 = 100;
        while (i9 < 1001 && i10 == 100) {
            ArrayList arrayList = new ArrayList();
            List<AbstractSafeParcelable> A = o().A(100);
            if (A != null) {
                arrayList.addAll(A);
                i8 = A.size();
            } else {
                i8 = 0;
            }
            if (abstractSafeParcelable != null && i8 < 100) {
                arrayList.add(abstractSafeParcelable);
            }
            int size = arrayList.size();
            int i11 = 0;
            while (i11 < size) {
                Object obj = arrayList.get(i11);
                i11++;
                AbstractSafeParcelable abstractSafeParcelable2 = (AbstractSafeParcelable) obj;
                if (abstractSafeParcelable2 instanceof zzbf) {
                    try {
                        dVar.f1((zzbf) abstractSafeParcelable2, zznVar);
                    } catch (RemoteException e8) {
                        e = e8;
                        E = i().E();
                        str = "Failed to send event to the service";
                        E.b(str, e);
                    }
                } else if (abstractSafeParcelable2 instanceof zzno) {
                    try {
                        dVar.z0((zzno) abstractSafeParcelable2, zznVar);
                    } catch (RemoteException e9) {
                        e = e9;
                        E = i().E();
                        str = "Failed to send user property to the service";
                        E.b(str, e);
                    }
                } else if (abstractSafeParcelable2 instanceof zzac) {
                    try {
                        dVar.B((zzac) abstractSafeParcelable2, zznVar);
                    } catch (RemoteException e10) {
                        e = e10;
                        E = i().E();
                        str = "Failed to send conditional user property to the service";
                        E.b(str, e);
                    }
                } else {
                    i().E().a("Discarding data. Unrecognized parcel type.");
                }
            }
            i9++;
            i10 = i8;
        }
    }

    public final void O(AtomicReference<String> atomicReference) {
        k();
        t();
        N(new n9(this, atomicReference, h0(false)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void P(AtomicReference<List<zzmv>> atomicReference, Bundle bundle) {
        k();
        t();
        N(new j9(this, atomicReference, h0(false), bundle));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void Q(AtomicReference<List<zzac>> atomicReference, String str, String str2, String str3) {
        k();
        t();
        N(new w9(this, atomicReference, str, str2, str3, h0(false)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void R(AtomicReference<List<zzno>> atomicReference, String str, String str2, String str3, boolean z4) {
        k();
        t();
        N(new y9(this, atomicReference, str, str2, str3, h0(false), z4));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void S(boolean z4) {
        k();
        t();
        if (z4) {
            o().F();
        }
        if (c0()) {
            N(new v9(this, h0(false)));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final zzal T() {
        k();
        t();
        f7.d dVar = this.f16529d;
        if (dVar == null) {
            W();
            i().D().a("Failed to get consents; not connected to service yet.");
            return null;
        }
        zzn h02 = h0(false);
        n6.j.l(h02);
        try {
            zzal M0 = dVar.M0(h02);
            f0();
            return M0;
        } catch (RemoteException e8) {
            i().E().b("Failed to get consents; remote exception", e8);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Boolean U() {
        return this.f16530e;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void V() {
        k();
        t();
        zzn h02 = h0(true);
        o().G();
        N(new p9(this, h02));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void W() {
        k();
        t();
        if (a0()) {
            return;
        }
        if (d0()) {
            this.f16528c.a();
        } else if (a().T()) {
        } else {
            List<ResolveInfo> queryIntentServices = zza().getPackageManager().queryIntentServices(new Intent().setClassName(zza(), "com.google.android.gms.measurement.AppMeasurementService"), 65536);
            if (!((queryIntentServices == null || queryIntentServices.isEmpty()) ? false : true)) {
                i().E().a("Unable to use remote or local measurement implementation. Please register the AppMeasurementService service in the app manifest");
                return;
            }
            Intent intent = new Intent("com.google.android.gms.measurement.START");
            intent.setComponent(new ComponentName(zza(), "com.google.android.gms.measurement.AppMeasurementService"));
            this.f16528c.b(intent);
        }
    }

    public final void X() {
        k();
        t();
        this.f16528c.g();
        try {
            t6.a.b().c(zza(), this.f16528c);
        } catch (IllegalArgumentException | IllegalStateException unused) {
        }
        this.f16529d = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void Y() {
        k();
        t();
        zzn h02 = h0(false);
        o().F();
        N(new k9(this, h02));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void Z() {
        k();
        t();
        N(new s9(this, h0(true)));
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ e a() {
        return super.a();
    }

    public final boolean a0() {
        k();
        t();
        return this.f16529d != null;
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ d b() {
        return super.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean b0() {
        k();
        t();
        return !d0() || g().G0() >= 200900;
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ x c() {
        return super.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean c0() {
        k();
        t();
        return !d0() || g().G0() >= c0.f16402p0.a(null).intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00d4  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00ef  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean d0() {
        /*
            Method dump skipped, instructions count: 259
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.f9.d0():boolean");
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
