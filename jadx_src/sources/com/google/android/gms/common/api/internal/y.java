package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.a;
import com.google.android.gms.common.api.internal.f;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y extends e {

    /* renamed from: e  reason: collision with root package name */
    final /* synthetic */ f.a f11714e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public y(f.a aVar, c cVar, Feature[] featureArr, boolean z4, int i8) {
        super(cVar, featureArr, z4, i8);
        this.f11714e = aVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.api.internal.e
    public final void d(a.b bVar, j7.k<Void> kVar) {
        l6.i iVar;
        iVar = this.f11714e.f11638a;
        iVar.accept(bVar, kVar);
    }
}
