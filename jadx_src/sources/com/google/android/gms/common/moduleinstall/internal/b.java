package com.google.android.gms.common.moduleinstall.internal;

import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b extends r6.f {

    /* renamed from: a  reason: collision with root package name */
    private final com.google.android.gms.common.api.internal.c f11912a;

    public b(com.google.android.gms.common.api.internal.c cVar) {
        this.f11912a = cVar;
    }

    @Override // r6.g
    public final void F1(ModuleInstallStatusUpdate moduleInstallStatusUpdate) {
        this.f11912a.c(new a(this, moduleInstallStatusUpdate));
    }
}
