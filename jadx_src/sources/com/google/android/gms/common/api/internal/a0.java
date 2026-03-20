package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.a;
import com.google.android.gms.common.api.internal.g;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a0 extends g {

    /* renamed from: d  reason: collision with root package name */
    final /* synthetic */ g.a f11601d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public a0(g.a aVar, Feature[] featureArr, boolean z4, int i8) {
        super(featureArr, z4, i8);
        this.f11601d = aVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.api.internal.g
    public final void b(a.b bVar, j7.k kVar) {
        l6.i iVar;
        iVar = this.f11601d.f11649a;
        iVar.accept(bVar, kVar);
    }
}
