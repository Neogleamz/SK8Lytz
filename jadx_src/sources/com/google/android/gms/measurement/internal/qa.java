package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import com.google.android.gms.internal.measurement.ff;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class qa implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    long f16910a;

    /* renamed from: b  reason: collision with root package name */
    long f16911b;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ ra f16912c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public qa(ra raVar, long j8, long j9) {
        this.f16912c = raVar;
        this.f16910a = j8;
        this.f16911b = j9;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16912c.f16955b.l().B(new Runnable() { // from class: com.google.android.gms.measurement.internal.ta
            @Override // java.lang.Runnable
            public final void run() {
                qa qaVar = qa.this;
                ra raVar = qaVar.f16912c;
                long j8 = qaVar.f16910a;
                long j9 = qaVar.f16911b;
                raVar.f16955b.k();
                raVar.f16955b.i().D().a("Application going to the background");
                raVar.f16955b.f().f16618u.a(true);
                raVar.f16955b.B(true);
                if (!raVar.f16955b.a().R()) {
                    raVar.f16955b.f16842f.e(j9);
                    raVar.f16955b.C(false, false, j9);
                }
                if (ff.a() && raVar.f16955b.a().r(c0.I0)) {
                    raVar.f16955b.i().H().b("Application backgrounded at: timestamp_millis", Long.valueOf(j8));
                } else {
                    raVar.f16955b.p().T("auto", "_ab", j8, new Bundle());
                }
            }
        });
    }
}
