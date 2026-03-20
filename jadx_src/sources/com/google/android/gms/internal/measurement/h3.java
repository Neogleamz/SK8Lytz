package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.p2;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h3 extends p2.a {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ boolean f12224e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ p2 f12225f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public h3(p2 p2Var, boolean z4) {
        super(p2Var);
        this.f12224e = z4;
        this.f12225f = p2Var;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    final void a() {
        c2 c2Var;
        c2Var = this.f12225f.f12429i;
        ((c2) n6.j.l(c2Var)).setDataCollectionEnabled(this.f12224e);
    }
}
