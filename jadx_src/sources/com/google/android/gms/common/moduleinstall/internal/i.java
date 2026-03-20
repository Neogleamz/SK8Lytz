package com.google.android.gms.common.moduleinstall.internal;

import a7.l;
import android.content.Context;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.a;
import com.google.android.gms.common.api.b;
import com.google.android.gms.common.api.internal.f;
import com.google.android.gms.common.api.internal.g;
import com.google.android.gms.common.moduleinstall.ModuleAvailabilityResponse;
import com.google.android.gms.common.moduleinstall.ModuleInstallResponse;
import com.google.android.gms.common.moduleinstall.internal.ApiFeatureRequest;
import j7.k;
import j7.m;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i extends com.google.android.gms.common.api.b implements q6.c {

    /* renamed from: k  reason: collision with root package name */
    private static final a.g f11925k;

    /* renamed from: l  reason: collision with root package name */
    private static final a.AbstractC0121a f11926l;

    /* renamed from: m  reason: collision with root package name */
    private static final com.google.android.gms.common.api.a f11927m;

    /* renamed from: n  reason: collision with root package name */
    public static final /* synthetic */ int f11928n = 0;

    static {
        a.g gVar = new a.g();
        f11925k = gVar;
        f fVar = new f();
        f11926l = fVar;
        f11927m = new com.google.android.gms.common.api.a("ModuleInstall.API", fVar, gVar);
    }

    public i(Context context) {
        super(context, f11927m, a.d.f11562o, b.a.f11573c);
    }

    static final ApiFeatureRequest p(boolean z4, k6.b... bVarArr) {
        n6.j.m(bVarArr, "Requested APIs must not be null.");
        n6.j.b(bVarArr.length > 0, "Please provide at least one OptionalModuleApi.");
        for (k6.b bVar : bVarArr) {
            n6.j.m(bVar, "Requested API must not be null.");
        }
        return ApiFeatureRequest.Z(Arrays.asList(bVarArr), z4);
    }

    @Override // q6.c
    public final j7.j<ModuleAvailabilityResponse> b(k6.b... bVarArr) {
        final ApiFeatureRequest p8 = p(false, bVarArr);
        if (p8.u().isEmpty()) {
            return m.f(new ModuleAvailabilityResponse(true, 0));
        }
        g.a a9 = com.google.android.gms.common.api.internal.g.a();
        a9.d(l.f199a);
        a9.e(27301);
        a9.c(false);
        a9.b(new l6.i() { // from class: r6.j
            @Override // l6.i
            public final void accept(Object obj, Object obj2) {
                com.google.android.gms.common.moduleinstall.internal.i iVar = com.google.android.gms.common.moduleinstall.internal.i.this;
                ApiFeatureRequest apiFeatureRequest = p8;
                ((com.google.android.gms.common.moduleinstall.internal.c) ((com.google.android.gms.common.moduleinstall.internal.j) obj).B()).k(new k(iVar, (j7.k) obj2), apiFeatureRequest);
            }
        });
        return f(a9.a());
    }

    @Override // q6.c
    public final j7.j<ModuleInstallResponse> c(q6.d dVar) {
        final ApiFeatureRequest t8 = ApiFeatureRequest.t(dVar);
        final q6.a b9 = dVar.b();
        Executor c9 = dVar.c();
        boolean e8 = dVar.e();
        if (t8.u().isEmpty()) {
            return m.f(new ModuleInstallResponse(0));
        }
        if (b9 == null) {
            g.a a9 = com.google.android.gms.common.api.internal.g.a();
            a9.d(l.f199a);
            a9.c(e8);
            a9.e(27304);
            a9.b(new l6.i() { // from class: r6.i
                @Override // l6.i
                public final void accept(Object obj, Object obj2) {
                    com.google.android.gms.common.moduleinstall.internal.i iVar = com.google.android.gms.common.moduleinstall.internal.i.this;
                    ApiFeatureRequest apiFeatureRequest = t8;
                    ((com.google.android.gms.common.moduleinstall.internal.c) ((com.google.android.gms.common.moduleinstall.internal.j) obj).B()).q(new l(iVar, (j7.k) obj2), apiFeatureRequest, null);
                }
            });
            return f(a9.a());
        }
        n6.j.l(b9);
        String simpleName = q6.a.class.getSimpleName();
        com.google.android.gms.common.api.internal.c k8 = c9 == null ? k(b9, simpleName) : com.google.android.gms.common.api.internal.d.b(b9, c9, simpleName);
        final b bVar = new b(k8);
        final AtomicReference atomicReference = new AtomicReference();
        l6.i iVar = new l6.i() { // from class: com.google.android.gms.common.moduleinstall.internal.d
            @Override // l6.i
            public final void accept(Object obj, Object obj2) {
                i iVar2 = i.this;
                AtomicReference atomicReference2 = atomicReference;
                q6.a aVar = b9;
                ApiFeatureRequest apiFeatureRequest = t8;
                b bVar2 = bVar;
                ((c) ((j) obj).B()).q(new g(iVar2, atomicReference2, (k) obj2, aVar), apiFeatureRequest, bVar2);
            }
        };
        l6.i iVar2 = new l6.i() { // from class: com.google.android.gms.common.moduleinstall.internal.e
            @Override // l6.i
            public final void accept(Object obj, Object obj2) {
                i iVar3 = i.this;
                b bVar2 = bVar;
                ((c) ((j) obj).B()).v(new h(iVar3, (k) obj2), bVar2);
            }
        };
        f.a a10 = com.google.android.gms.common.api.internal.f.a();
        a10.g(k8);
        a10.d(l.f199a);
        a10.c(e8);
        a10.b(iVar);
        a10.f(iVar2);
        a10.e(27305);
        return g(a10.a()).q(new j7.i() { // from class: r6.h
            @Override // j7.i
            public final j7.j a(Object obj) {
                AtomicReference atomicReference2 = atomicReference;
                Void r22 = (Void) obj;
                int i8 = com.google.android.gms.common.moduleinstall.internal.i.f11928n;
                return atomicReference2.get() != null ? m.f((ModuleInstallResponse) atomicReference2.get()) : m.e(new ApiException(Status.f11549h));
            }
        });
    }
}
