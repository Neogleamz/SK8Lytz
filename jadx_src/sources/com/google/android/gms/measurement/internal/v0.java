package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class v0 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f17041a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ long f17042b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ a f17043c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public v0(a aVar, String str, long j8) {
        this.f17041a = str;
        this.f17042b = j8;
        this.f17043c = aVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        a.w(this.f17043c, this.f17041a, this.f17042b);
    }
}
