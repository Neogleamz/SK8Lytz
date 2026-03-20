package com.google.android.gms.common.api.internal;

import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i implements j7.e {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ j7.k f11657a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ j f11658b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(j jVar, j7.k kVar) {
        this.f11658b = jVar;
        this.f11657a = kVar;
    }

    @Override // j7.e
    public final void a(j7.j jVar) {
        Map map;
        map = this.f11658b.f11662b;
        map.remove(this.f11657a);
    }
}
