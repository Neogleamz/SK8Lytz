package com.google.android.gms.measurement.internal;

import android.os.Binder;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.internal.measurement.zzc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j6 extends f7.f {

    /* renamed from: a  reason: collision with root package name */
    private final gb f16700a;

    /* renamed from: b  reason: collision with root package name */
    private Boolean f16701b;

    /* renamed from: c  reason: collision with root package name */
    private String f16702c;

    public j6(gb gbVar) {
        this(gbVar, null);
    }

    private j6(gb gbVar, String str) {
        n6.j.l(gbVar);
        this.f16700a = gbVar;
        this.f16702c = null;
    }

    private final void f(Runnable runnable) {
        n6.j.l(runnable);
        if (this.f16700a.l().H()) {
            runnable.run();
        } else {
            this.f16700a.l().B(runnable);
        }
    }

    private final void k(String str, boolean z4) {
        boolean z8;
        if (TextUtils.isEmpty(str)) {
            this.f16700a.i().E().a("Measurement Service called without app package");
            throw new SecurityException("Measurement Service called without app package");
        }
        if (z4) {
            try {
                if (this.f16701b == null) {
                    if (!"com.google.android.gms".equals(this.f16702c) && !u6.p.a(this.f16700a.zza(), Binder.getCallingUid()) && !com.google.android.gms.common.e.a(this.f16700a.zza()).c(Binder.getCallingUid())) {
                        z8 = false;
                        this.f16701b = Boolean.valueOf(z8);
                    }
                    z8 = true;
                    this.f16701b = Boolean.valueOf(z8);
                }
                if (this.f16701b.booleanValue()) {
                    return;
                }
            } catch (SecurityException e8) {
                this.f16700a.i().E().b("Measurement Service called with invalid calling package. appId", x4.t(str));
                throw e8;
            }
        }
        if (this.f16702c == null && com.google.android.gms.common.d.j(this.f16700a.zza(), Binder.getCallingUid(), str)) {
            this.f16702c = str;
        }
        if (str.equals(this.f16702c)) {
            return;
        }
        throw new SecurityException(String.format("Unknown calling package name '%s'.", str));
    }

    private final void v(zzn zznVar, boolean z4) {
        n6.j.l(zznVar);
        n6.j.f(zznVar.f17288a);
        k(zznVar.f17288a, false);
        this.f16700a.o0().i0(zznVar.f17289b, zznVar.f17303w);
    }

    private final void y(zzbf zzbfVar, zzn zznVar) {
        this.f16700a.p0();
        this.f16700a.s(zzbfVar, zznVar);
    }

    @Override // f7.d
    public final void B(zzac zzacVar, zzn zznVar) {
        n6.j.l(zzacVar);
        n6.j.l(zzacVar.f17252c);
        v(zznVar, false);
        zzac zzacVar2 = new zzac(zzacVar);
        zzacVar2.f17250a = zznVar.f17288a;
        f(new m6(this, zzacVar2, zznVar));
    }

    @Override // f7.d
    public final void G1(final Bundle bundle, zzn zznVar) {
        v(zznVar, false);
        final String str = zznVar.f17288a;
        n6.j.l(str);
        f(new Runnable() { // from class: com.google.android.gms.measurement.internal.i6
            @Override // java.lang.Runnable
            public final void run() {
                j6.this.g(str, bundle);
            }
        });
    }

    @Override // f7.d
    public final List<zzno> I0(String str, String str2, boolean z4, zzn zznVar) {
        v(zznVar, false);
        String str3 = zznVar.f17288a;
        n6.j.l(str3);
        try {
            List<pb> list = (List) this.f16700a.l().u(new o6(this, str3, str, str2)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (pb pbVar : list) {
                if (z4 || !sb.H0(pbVar.f16887c)) {
                    arrayList.add(new zzno(pbVar));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e8) {
            this.f16700a.i().E().c("Failed to query user properties. appId", x4.t(zznVar.f17288a), e8);
            return Collections.emptyList();
        }
    }

    @Override // f7.d
    public final List<zzmv> K(zzn zznVar, Bundle bundle) {
        v(zznVar, false);
        n6.j.l(zznVar.f17288a);
        try {
            return (List) this.f16700a.l().u(new c7(this, zznVar, bundle)).get();
        } catch (InterruptedException | ExecutionException e8) {
            this.f16700a.i().E().c("Failed to get trigger URIs. appId", x4.t(zznVar.f17288a), e8);
            return Collections.emptyList();
        }
    }

    @Override // f7.d
    public final List<zzno> K0(zzn zznVar, boolean z4) {
        v(zznVar, false);
        String str = zznVar.f17288a;
        n6.j.l(str);
        try {
            List<pb> list = (List) this.f16700a.l().u(new b7(this, str)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (pb pbVar : list) {
                if (z4 || !sb.H0(pbVar.f16887c)) {
                    arrayList.add(new zzno(pbVar));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e8) {
            this.f16700a.i().E().c("Failed to get user properties. appId", x4.t(zznVar.f17288a), e8);
            return null;
        }
    }

    @Override // f7.d
    public final byte[] K1(zzbf zzbfVar, String str) {
        n6.j.f(str);
        n6.j.l(zzbfVar);
        k(str, true);
        this.f16700a.i().D().b("Log and bundle. event", this.f16700a.g0().c(zzbfVar.f17263a));
        long c9 = this.f16700a.zzb().c() / 1000000;
        try {
            byte[] bArr = (byte[]) this.f16700a.l().z(new a7(this, zzbfVar, str)).get();
            if (bArr == null) {
                this.f16700a.i().E().b("Log and bundle returned null. appId", x4.t(str));
                bArr = new byte[0];
            }
            this.f16700a.i().D().d("Log and bundle processed. event, size, time_ms", this.f16700a.g0().c(zzbfVar.f17263a), Integer.valueOf(bArr.length), Long.valueOf((this.f16700a.zzb().c() / 1000000) - c9));
            return bArr;
        } catch (InterruptedException | ExecutionException e8) {
            this.f16700a.i().E().d("Failed to log and bundle. appId, event, error", x4.t(str), this.f16700a.g0().c(zzbfVar.f17263a), e8);
            return null;
        }
    }

    @Override // f7.d
    public final zzal M0(zzn zznVar) {
        v(zznVar, false);
        n6.j.f(zznVar.f17288a);
        try {
            return (zzal) this.f16700a.l().z(new v6(this, zznVar)).get(10000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e8) {
            this.f16700a.i().E().c("Failed to get consent. appId", x4.t(zznVar.f17288a), e8);
            return new zzal(null);
        }
    }

    @Override // f7.d
    public final void Z(zzn zznVar) {
        v(zznVar, false);
        f(new k6(this, zznVar));
    }

    @Override // f7.d
    public final void a1(zzbf zzbfVar, String str, String str2) {
        n6.j.l(zzbfVar);
        n6.j.f(str);
        k(str, true);
        f(new x6(this, zzbfVar, str));
    }

    @Override // f7.d
    public final void f1(zzbf zzbfVar, zzn zznVar) {
        n6.j.l(zzbfVar);
        v(zznVar, false);
        f(new y6(this, zzbfVar, zznVar));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void g(String str, Bundle bundle) {
        this.f16700a.e0().f0(str, bundle);
    }

    @Override // f7.d
    public final void m(zzn zznVar) {
        n6.j.f(zznVar.f17288a);
        n6.j.l(zznVar.B);
        w6 w6Var = new w6(this, zznVar);
        n6.j.l(w6Var);
        if (this.f16700a.l().H()) {
            w6Var.run();
        } else {
            this.f16700a.l().E(w6Var);
        }
    }

    @Override // f7.d
    public final void m0(long j8, String str, String str2, String str3) {
        f(new n6(this, str2, str3, str, j8));
    }

    @Override // f7.d
    public final String n1(zzn zznVar) {
        v(zznVar, false);
        return this.f16700a.R(zznVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzbf q(zzbf zzbfVar, zzn zznVar) {
        zzba zzbaVar;
        boolean z4 = false;
        if ("_cmp".equals(zzbfVar.f17263a) && (zzbaVar = zzbfVar.f17264b) != null && zzbaVar.t() != 0) {
            String T0 = zzbfVar.f17264b.T0("_cis");
            if ("referrer broadcast".equals(T0) || "referrer API".equals(T0)) {
                z4 = true;
            }
        }
        if (z4) {
            this.f16700a.i().H().b("Event has been filtered ", zzbfVar.toString());
            return new zzbf("_cmpx", zzbfVar.f17264b, zzbfVar.f17265c, zzbfVar.f17266d);
        }
        return zzbfVar;
    }

    @Override // f7.d
    public final void r0(zzn zznVar) {
        n6.j.f(zznVar.f17288a);
        k(zznVar.f17288a, false);
        f(new t6(this, zznVar));
    }

    @Override // f7.d
    public final List<zzno> s(String str, String str2, String str3, boolean z4) {
        k(str, true);
        try {
            List<pb> list = (List) this.f16700a.l().u(new s6(this, str, str2, str3)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (pb pbVar : list) {
                if (z4 || !sb.H0(pbVar.f16887c)) {
                    arrayList.add(new zzno(pbVar));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e8) {
            this.f16700a.i().E().c("Failed to get user properties as. appId", x4.t(str), e8);
            return Collections.emptyList();
        }
    }

    @Override // f7.d
    public final List<zzac> s0(String str, String str2, String str3) {
        k(str, true);
        try {
            return (List) this.f16700a.l().u(new u6(this, str, str2, str3)).get();
        } catch (InterruptedException | ExecutionException e8) {
            this.f16700a.i().E().b("Failed to get conditional user properties as", e8);
            return Collections.emptyList();
        }
    }

    @Override // f7.d
    public final List<zzac> t0(String str, String str2, zzn zznVar) {
        v(zznVar, false);
        String str3 = zznVar.f17288a;
        n6.j.l(str3);
        try {
            return (List) this.f16700a.l().u(new q6(this, str3, str, str2)).get();
        } catch (InterruptedException | ExecutionException e8) {
            this.f16700a.i().E().b("Failed to get conditional user properties", e8);
            return Collections.emptyList();
        }
    }

    @Override // f7.d
    public final void u1(zzac zzacVar) {
        n6.j.l(zzacVar);
        n6.j.l(zzacVar.f17252c);
        n6.j.f(zzacVar.f17250a);
        k(zzacVar.f17250a, true);
        f(new p6(this, new zzac(zzacVar)));
    }

    @Override // f7.d
    public final void w(zzn zznVar) {
        v(zznVar, false);
        f(new l6(this, zznVar));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void x(zzbf zzbfVar, zzn zznVar) {
        z4 I;
        String str;
        String str2;
        if (!this.f16700a.i0().V(zznVar.f17288a)) {
            y(zzbfVar, zznVar);
            return;
        }
        this.f16700a.i().I().b("EES config found for", zznVar.f17288a);
        r5 i02 = this.f16700a.i0();
        String str3 = zznVar.f17288a;
        com.google.android.gms.internal.measurement.b0 c9 = TextUtils.isEmpty(str3) ? null : i02.f16938j.c(str3);
        if (c9 == null) {
            I = this.f16700a.i().I();
            str = zznVar.f17288a;
            str2 = "EES not loaded for";
        } else {
            boolean z4 = false;
            try {
                Map<String, Object> O = this.f16700a.n0().O(zzbfVar.f17264b.D0(), true);
                String a9 = f7.o.a(zzbfVar.f17263a);
                if (a9 == null) {
                    a9 = zzbfVar.f17263a;
                }
                z4 = c9.d(new com.google.android.gms.internal.measurement.e(a9, zzbfVar.f17266d, O));
            } catch (zzc unused) {
                this.f16700a.i().E().c("EES error. appId, eventName", zznVar.f17289b, zzbfVar.f17263a);
            }
            if (z4) {
                if (c9.g()) {
                    this.f16700a.i().I().b("EES edited event", zzbfVar.f17263a);
                    zzbfVar = this.f16700a.n0().F(c9.a().d());
                }
                y(zzbfVar, zznVar);
                if (c9.f()) {
                    for (com.google.android.gms.internal.measurement.e eVar : c9.a().f()) {
                        this.f16700a.i().I().b("EES logging created event", eVar.e());
                        y(this.f16700a.n0().F(eVar), zznVar);
                    }
                    return;
                }
                return;
            }
            I = this.f16700a.i().I();
            str = zzbfVar.f17263a;
            str2 = "EES was not applied to event";
        }
        I.b(str2, str);
        y(zzbfVar, zznVar);
    }

    @Override // f7.d
    public final void z0(zzno zznoVar, zzn zznVar) {
        n6.j.l(zznoVar);
        v(zznVar, false);
        f(new z6(this, zznoVar, zznVar));
    }
}
