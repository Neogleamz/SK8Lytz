package com.google.android.gms.common;

import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class v extends w {

    /* renamed from: f  reason: collision with root package name */
    private final Callable f11993f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ v(Callable callable, j6.f fVar) {
        super();
        this.f11993f = callable;
    }

    @Override // com.google.android.gms.common.w
    final String a() {
        try {
            return (String) this.f11993f.call();
        } catch (Exception e8) {
            throw new RuntimeException(e8);
        }
    }
}
