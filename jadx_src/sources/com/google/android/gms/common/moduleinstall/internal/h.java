package com.google.android.gms.common.moduleinstall.internal;

import com.google.android.gms.common.api.Status;
import j7.k;
import l6.d;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h extends d.a {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ k f11924a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(i iVar, k kVar) {
        this.f11924a = kVar;
    }

    @Override // l6.d
    public final void V(Status status) {
        l6.k.a(status, Boolean.TRUE, this.f11924a);
    }
}
