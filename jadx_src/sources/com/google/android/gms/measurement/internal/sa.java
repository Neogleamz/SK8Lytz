package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import com.google.android.gms.internal.measurement.ae;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class sa {

    /* renamed from: a  reason: collision with root package name */
    private long f16976a;

    /* renamed from: b  reason: collision with root package name */
    protected long f16977b;

    /* renamed from: c  reason: collision with root package name */
    private final t f16978c;

    /* renamed from: d  reason: collision with root package name */
    final /* synthetic */ na f16979d;

    public sa(na naVar) {
        this.f16979d = naVar;
        this.f16978c = new va(this, naVar.f16485a);
        long b9 = naVar.zzb().b();
        this.f16976a = b9;
        this.f16977b = b9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void c(sa saVar) {
        saVar.f16979d.k();
        saVar.d(false, false, saVar.f16979d.zzb().b());
        saVar.f16979d.m().t(saVar.f16979d.zzb().b());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long a(long j8) {
        long j9 = j8 - this.f16977b;
        this.f16977b = j8;
        return j9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void b() {
        this.f16978c.a();
        this.f16976a = 0L;
        this.f16977b = 0L;
    }

    public final boolean d(boolean z4, boolean z8, long j8) {
        this.f16979d.k();
        this.f16979d.t();
        if (!ae.a() || !this.f16979d.a().r(c0.f16400o0) || this.f16979d.f16485a.n()) {
            this.f16979d.f().f16615r.b(this.f16979d.zzb().a());
        }
        long j9 = j8 - this.f16976a;
        if (!z4 && j9 < 1000) {
            this.f16979d.i().I().b("Screen exposed for less than 1000 ms. Event not sent. time", Long.valueOf(j9));
            return false;
        }
        if (!z8) {
            j9 = a(j8);
        }
        this.f16979d.i().I().b("Recording user engagement, ms", Long.valueOf(j9));
        Bundle bundle = new Bundle();
        bundle.putLong("_et", j9);
        sb.V(this.f16979d.q().A(!this.f16979d.a().R()), bundle, true);
        if (!z8) {
            this.f16979d.p().A0("auto", "_e", bundle);
        }
        this.f16976a = j8;
        this.f16978c.a();
        this.f16978c.b(3600000L);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void e(long j8) {
        this.f16978c.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void f(long j8) {
        this.f16979d.k();
        this.f16978c.a();
        this.f16976a = j8;
        this.f16977b = j8;
    }
}
