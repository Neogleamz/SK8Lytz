package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.internal.measurement.wf;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class eb extends cb {
    /* JADX INFO: Access modifiers changed from: package-private */
    public eb(gb gbVar) {
        super(gbVar);
    }

    private final String u(String str) {
        String P = p().P(str);
        if (TextUtils.isEmpty(P)) {
            return c0.f16406s.a(null);
        }
        Uri parse = Uri.parse(c0.f16406s.a(null));
        Uri.Builder buildUpon = parse.buildUpon();
        String authority = parse.getAuthority();
        buildUpon.authority(P + "." + authority);
        return buildUpon.build().toString();
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

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void h() {
        super.h();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ x4 i() {
        return super.i();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void j() {
        super.j();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void k() {
        super.k();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ a6 l() {
        return super.l();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ nb m() {
        return super.m();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ wb n() {
        return super.n();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ l o() {
        return super.o();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ r5 p() {
        return super.p();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ ia q() {
        return super.q();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ eb r() {
        return super.r();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Uri.Builder s(String str) {
        String z4;
        String P = p().P(str);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(a().z(str, c0.Z));
        if (TextUtils.isEmpty(P)) {
            z4 = a().z(str, c0.f16361a0);
        } else {
            z4 = P + "." + a().z(str, c0.f16361a0);
        }
        builder.authority(z4);
        builder.path(a().z(str, c0.f16364b0));
        return builder;
    }

    public final Pair<db, Boolean> t(String str) {
        y3 C0;
        if (wf.a() && a().r(c0.f16415w0)) {
            g();
            if (sb.F0(str)) {
                i().I().a("sgtm feature flag enabled.");
                y3 C02 = o().C0(str);
                if (C02 == null) {
                    return Pair.create(new db(u(str)), Boolean.TRUE);
                }
                String i8 = C02.i();
                com.google.android.gms.internal.measurement.d4 J = p().J(str);
                boolean z4 = true;
                if (J == null || (C0 = o().C0(str)) == null || ((!J.c0() || J.R().m() != 100) && !g().C0(str, C0.r()) && (TextUtils.isEmpty(i8) || i8.hashCode() % 100 >= J.R().m()))) {
                    z4 = false;
                }
                if (!z4) {
                    return Pair.create(new db(u(str)), Boolean.TRUE);
                }
                db dbVar = null;
                if (C02.y()) {
                    i().I().a("sgtm upload enabled in manifest.");
                    com.google.android.gms.internal.measurement.d4 J2 = p().J(C02.h());
                    if (J2 != null && J2.c0()) {
                        String K = J2.R().K();
                        if (!TextUtils.isEmpty(K)) {
                            String J3 = J2.R().J();
                            i().I().c("sgtm configured with upload_url, server_info", K, TextUtils.isEmpty(J3) ? "Y" : "N");
                            if (TextUtils.isEmpty(J3)) {
                                dbVar = new db(K);
                            } else {
                                HashMap hashMap = new HashMap();
                                hashMap.put("x-sgtm-server-info", J3);
                                if (!TextUtils.isEmpty(C02.r())) {
                                    hashMap.put("x-gtm-server-preview", C02.r());
                                }
                                dbVar = new db(K, hashMap);
                            }
                        }
                    }
                }
                if (dbVar != null) {
                    return Pair.create(dbVar, Boolean.FALSE);
                }
            }
        }
        return Pair.create(new db(u(str)), Boolean.TRUE);
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
