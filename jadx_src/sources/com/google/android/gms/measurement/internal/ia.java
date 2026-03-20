package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Pair;
import com.daimajia.numberprogressbar.BuildConfig;
import e6.a;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ia extends bb {

    /* renamed from: d  reason: collision with root package name */
    private final Map<String, ha> f16682d;

    /* renamed from: e  reason: collision with root package name */
    public final m5 f16683e;

    /* renamed from: f  reason: collision with root package name */
    public final m5 f16684f;

    /* renamed from: g  reason: collision with root package name */
    public final m5 f16685g;

    /* renamed from: h  reason: collision with root package name */
    public final m5 f16686h;

    /* renamed from: i  reason: collision with root package name */
    public final m5 f16687i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ia(gb gbVar) {
        super(gbVar);
        this.f16682d = new HashMap();
        h5 f5 = f();
        Objects.requireNonNull(f5);
        this.f16683e = new m5(f5, "last_delete_stale", 0L);
        h5 f8 = f();
        Objects.requireNonNull(f8);
        this.f16684f = new m5(f8, "backoff", 0L);
        h5 f9 = f();
        Objects.requireNonNull(f9);
        this.f16685g = new m5(f9, "last_upload", 0L);
        h5 f10 = f();
        Objects.requireNonNull(f10);
        this.f16686h = new m5(f10, "last_upload_attempt", 0L);
        h5 f11 = f();
        Objects.requireNonNull(f11);
        this.f16687i = new m5(f11, "midnight_offset", 0L);
    }

    @Deprecated
    private final Pair<String, Boolean> w(String str) {
        ha haVar;
        k();
        long b9 = zzb().b();
        ha haVar2 = this.f16682d.get(str);
        if (haVar2 == null || b9 >= haVar2.f16655c) {
            e6.a.b(true);
            long y8 = a().y(str) + b9;
            a.C0166a c0166a = null;
            try {
                long x8 = a().x(str, c0.f16369d);
                if (x8 > 0) {
                    try {
                        c0166a = e6.a.a(zza());
                    } catch (PackageManager.NameNotFoundException unused) {
                        if (haVar2 != null && b9 < haVar2.f16655c + x8) {
                            return new Pair<>(haVar2.f16653a, Boolean.valueOf(haVar2.f16654b));
                        }
                    }
                } else {
                    c0166a = e6.a.a(zza());
                }
            } catch (Exception e8) {
                i().D().b("Unable to get advertising id", e8);
                haVar = new ha(BuildConfig.FLAVOR, false, y8);
            }
            if (c0166a == null) {
                return new Pair<>("00000000-0000-0000-0000-000000000000", Boolean.FALSE);
            }
            String a9 = c0166a.a();
            haVar = a9 != null ? new ha(a9, c0166a.b(), y8) : new ha(BuildConfig.FLAVOR, c0166a.b(), y8);
            this.f16682d.put(str, haVar);
            e6.a.b(false);
            return new Pair<>(haVar.f16653a, Boolean.valueOf(haVar.f16654b));
        }
        return new Pair<>(haVar2.f16653a, Boolean.valueOf(haVar2.f16654b));
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

    @Override // com.google.android.gms.measurement.internal.bb
    protected final boolean v() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Pair<String, Boolean> x(String str, zziq zziqVar) {
        return zziqVar.A() ? w(str) : new Pair<>(BuildConfig.FLAVOR, Boolean.FALSE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public final String y(String str, boolean z4) {
        k();
        String str2 = z4 ? (String) w(str).first : "00000000-0000-0000-0000-000000000000";
        MessageDigest T0 = sb.T0();
        if (T0 == null) {
            return null;
        }
        return String.format(Locale.US, "%032X", new BigInteger(1, T0.digest(str2.getBytes())));
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
