package com.google.android.gms.measurement.internal;

import android.net.Uri;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class q8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ boolean f16904a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ Uri f16905b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ String f16906c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ String f16907d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ r8 f16908e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q8(r8 r8Var, boolean z4, Uri uri, String str, String str2) {
        this.f16904a = z4;
        this.f16905b = uri;
        this.f16906c = str;
        this.f16907d = str2;
        this.f16908e = r8Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        r8.a(this.f16908e, this.f16904a, this.f16905b, this.f16906c, this.f16907d);
    }
}
