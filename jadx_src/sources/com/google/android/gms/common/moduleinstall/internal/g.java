package com.google.android.gms.common.moduleinstall.internal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.moduleinstall.ModuleInstallResponse;
import j7.k;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g extends r6.a {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ AtomicReference f11920a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ k f11921b;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ q6.a f11922c;

    /* renamed from: d  reason: collision with root package name */
    final /* synthetic */ i f11923d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(i iVar, AtomicReference atomicReference, k kVar, q6.a aVar) {
        this.f11923d = iVar;
        this.f11920a = atomicReference;
        this.f11921b = kVar;
        this.f11922c = aVar;
    }

    @Override // r6.a, r6.e
    public final void H0(Status status, ModuleInstallResponse moduleInstallResponse) {
        if (moduleInstallResponse != null) {
            this.f11920a.set(moduleInstallResponse);
        }
        l6.k.a(status, null, this.f11921b);
        if (!status.E0() || (moduleInstallResponse != null && moduleInstallResponse.u())) {
            this.f11923d.h(com.google.android.gms.common.api.internal.d.c(this.f11922c, q6.a.class.getSimpleName()), 27306);
        }
    }
}
