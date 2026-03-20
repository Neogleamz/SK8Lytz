package com.google.android.gms.measurement.internal;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.internal.measurement.lf;
import com.google.android.gms.internal.measurement.md;
import com.google.android.gms.internal.measurement.r4;
import com.google.android.gms.internal.measurement.t4;
import com.google.android.gms.internal.measurement.v4;
import com.google.android.gms.internal.measurement.y4;
import com.google.android.gms.internal.measurement.zzft$zzi;
import com.google.android.gms.internal.measurement.zzft$zzk;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class v8 extends bb {
    public v8(gb gbVar) {
        super(gbVar);
    }

    private static String d(String str, String str2) {
        throw new SecurityException("This implementation should not be used.");
    }

    @Override // com.google.android.gms.measurement.internal.bb
    protected final boolean v() {
        return false;
    }

    public final byte[] w(zzbf zzbfVar, String str) {
        pb pbVar;
        v4.a aVar;
        Bundle bundle;
        y3 y3Var;
        zzft$zzi.a aVar2;
        byte[] bArr;
        long j8;
        z a9;
        k();
        this.f16485a.O();
        n6.j.l(zzbfVar);
        n6.j.f(str);
        if (!a().B(str, c0.f16379g0)) {
            i().D().b("Generating ScionPayload disabled. packageName", str);
            return new byte[0];
        } else if (!"_iap".equals(zzbfVar.f17263a) && !"_iapx".equals(zzbfVar.f17263a)) {
            i().D().c("Generating a payload for this event is not available. package_name, event_name", str, zzbfVar.f17263a);
            return null;
        } else {
            zzft$zzi.a M = zzft$zzi.M();
            o().P0();
            try {
                y3 C0 = o().C0(str);
                if (C0 == null) {
                    i().D().b("Log and bundle not available. package_name", str);
                    return new byte[0];
                } else if (!C0.w()) {
                    i().D().b("Log and bundle disabled. package_name", str);
                    return new byte[0];
                } else {
                    v4.a Y0 = com.google.android.gms.internal.measurement.v4.F3().w0(1).Y0("android");
                    if (!TextUtils.isEmpty(C0.h())) {
                        Y0.T(C0.h());
                    }
                    if (!TextUtils.isEmpty(C0.j())) {
                        Y0.g0((String) n6.j.l(C0.j()));
                    }
                    if (!TextUtils.isEmpty(C0.k())) {
                        Y0.m0((String) n6.j.l(C0.k()));
                    }
                    if (C0.O() != -2147483648L) {
                        Y0.j0((int) C0.O());
                    }
                    Y0.p0(C0.t0()).e0(C0.p0());
                    String m8 = C0.m();
                    String F0 = C0.F0();
                    if (!TextUtils.isEmpty(m8)) {
                        Y0.S0(m8);
                    } else if (!TextUtils.isEmpty(F0)) {
                        Y0.J(F0);
                    }
                    Y0.H0(C0.D0());
                    zziq Q = this.f16446b.Q(str);
                    Y0.X(C0.n0());
                    if (this.f16485a.n() && a().K(Y0.f1()) && Q.A() && !TextUtils.isEmpty(null)) {
                        Y0.J0(null);
                    }
                    Y0.u0(Q.y());
                    if (Q.A() && C0.v()) {
                        Pair<String, Boolean> x8 = q().x(C0.h(), Q);
                        if (C0.v() && x8 != null && !TextUtils.isEmpty((CharSequence) x8.first)) {
                            Y0.a1(d((String) x8.first, Long.toString(zzbfVar.f17266d)));
                            Object obj = x8.second;
                            if (obj != null) {
                                Y0.b0(((Boolean) obj).booleanValue());
                            }
                        }
                    }
                    c().n();
                    v4.a C02 = Y0.C0(Build.MODEL);
                    c().n();
                    C02.W0(Build.VERSION.RELEASE).G0((int) c().t()).e1(c().u());
                    if (Q.B() && C0.i() != null) {
                        Y0.a0(d((String) n6.j.l(C0.i()), Long.toString(zzbfVar.f17266d)));
                    }
                    if (!TextUtils.isEmpty(C0.l())) {
                        Y0.Q0((String) n6.j.l(C0.l()));
                    }
                    String h8 = C0.h();
                    List<pb> L0 = o().L0(h8);
                    Iterator<pb> it = L0.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            pbVar = null;
                            break;
                        }
                        pbVar = it.next();
                        if ("_lte".equals(pbVar.f16887c)) {
                            break;
                        }
                    }
                    if (pbVar == null || pbVar.f16889e == null) {
                        pb pbVar2 = new pb(h8, "auto", "_lte", zzb().a(), 0L);
                        L0.add(pbVar2);
                        o().c0(pbVar2);
                    }
                    com.google.android.gms.internal.measurement.y4[] y4VarArr = new com.google.android.gms.internal.measurement.y4[L0.size()];
                    for (int i8 = 0; i8 < L0.size(); i8++) {
                        y4.a C = com.google.android.gms.internal.measurement.y4.Y().A(L0.get(i8).f16887c).C(L0.get(i8).f16888d);
                        m().U(C, L0.get(i8).f16889e);
                        y4VarArr[i8] = (com.google.android.gms.internal.measurement.y4) ((com.google.android.gms.internal.measurement.x8) C.n());
                    }
                    Y0.l0(Arrays.asList(y4VarArr));
                    m().T(Y0);
                    if (md.a() && a().r(c0.S0)) {
                        this.f16446b.u(C0, Y0);
                    }
                    b5 b9 = b5.b(zzbfVar);
                    g().L(b9.f16336d, o().A0(str));
                    g().U(b9, a().s(str));
                    Bundle bundle2 = b9.f16336d;
                    bundle2.putLong("_c", 1L);
                    i().D().a("Marking in-app purchase as real-time");
                    bundle2.putLong("_r", 1L);
                    bundle2.putString("_o", zzbfVar.f17265c);
                    if (g().C0(Y0.f1(), C0.r())) {
                        g().M(bundle2, "_dbg", 1L);
                        g().M(bundle2, "_r", 1L);
                    }
                    z B0 = o().B0(str, zzbfVar.f17263a);
                    if (B0 == null) {
                        aVar = Y0;
                        bundle = bundle2;
                        y3Var = C0;
                        aVar2 = M;
                        bArr = null;
                        a9 = new z(str, zzbfVar.f17263a, 0L, 0L, zzbfVar.f17266d, 0L, null, null, null, null);
                        j8 = 0;
                    } else {
                        aVar = Y0;
                        bundle = bundle2;
                        y3Var = C0;
                        aVar2 = M;
                        bArr = null;
                        j8 = B0.f17211f;
                        a9 = B0.a(zzbfVar.f17266d);
                    }
                    o().S(a9);
                    w wVar = new w(this.f16485a, zzbfVar.f17265c, str, zzbfVar.f17263a, zzbfVar.f17266d, j8, bundle);
                    r4.a B = com.google.android.gms.internal.measurement.r4.b0().H(wVar.f17059d).F(wVar.f17057b).B(wVar.f17060e);
                    Iterator<String> it2 = wVar.f17061f.iterator();
                    while (it2.hasNext()) {
                        String next = it2.next();
                        t4.a C2 = com.google.android.gms.internal.measurement.t4.b0().C(next);
                        Object I0 = wVar.f17061f.I0(next);
                        if (I0 != null) {
                            m().S(C2, I0);
                            B.C(C2);
                        }
                    }
                    v4.a aVar3 = aVar;
                    aVar3.E(B).F(zzft$zzk.H().x(com.google.android.gms.internal.measurement.s4.H().x(a9.f17208c).y(zzbfVar.f17263a)));
                    aVar3.I(n().x(y3Var.h(), Collections.emptyList(), aVar3.M(), Long.valueOf(B.J()), Long.valueOf(B.J())));
                    if (B.N()) {
                        aVar3.B0(B.J()).k0(B.J());
                    }
                    long x02 = y3Var.x0();
                    int i9 = (x02 > 0L ? 1 : (x02 == 0L ? 0 : -1));
                    if (i9 != 0) {
                        aVar3.t0(x02);
                    }
                    long B02 = y3Var.B0();
                    if (B02 != 0) {
                        aVar3.x0(B02);
                    } else if (i9 != 0) {
                        aVar3.x0(x02);
                    }
                    String q = y3Var.q();
                    if (lf.a() && a().B(str, c0.f16411u0) && q != null) {
                        aVar3.c1(q);
                    }
                    y3Var.u();
                    aVar3.o0((int) y3Var.z0()).P0(87000L).L0(zzb().a()).h0(true);
                    if (a().r(c0.A0)) {
                        this.f16446b.A(aVar3.f1(), aVar3);
                    }
                    zzft$zzi.a aVar4 = aVar2;
                    aVar4.y(aVar3);
                    y3 y3Var2 = y3Var;
                    y3Var2.w0(aVar3.n0());
                    y3Var2.s0(aVar3.i0());
                    o().T(y3Var2);
                    o().S0();
                    try {
                        return m().h0(((zzft$zzi) ((com.google.android.gms.internal.measurement.x8) aVar4.n())).k());
                    } catch (IOException e8) {
                        i().E().c("Data loss. Failed to bundle and serialize. appId", x4.t(str), e8);
                        return bArr;
                    }
                }
            } catch (SecurityException e9) {
                i().D().b("Resettable device id encryption failed", e9.getMessage());
                return new byte[0];
            } catch (SecurityException e10) {
                i().D().b("app instance id encryption failed", e10.getMessage());
                return new byte[0];
            } finally {
                o().Q0();
            }
        }
    }
}
