package com.google.android.gms.measurement.internal;

import android.os.Handler;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ra {

    /* renamed from: a  reason: collision with root package name */
    private qa f16954a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ na f16955b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ra(na naVar) {
        this.f16955b = naVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a() {
        Handler handler;
        this.f16955b.k();
        if (this.f16954a != null) {
            handler = this.f16955b.f16839c;
            handler.removeCallbacks(this.f16954a);
        }
        this.f16955b.f().f16618u.a(false);
        this.f16955b.B(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void b(long j8) {
        Handler handler;
        this.f16954a = new qa(this, this.f16955b.zzb().a(), j8);
        handler = this.f16955b.f16839c;
        handler.postDelayed(this.f16954a, 2000L);
    }
}
