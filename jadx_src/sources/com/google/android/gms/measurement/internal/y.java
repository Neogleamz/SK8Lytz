package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f17143a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ long f17144b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ a f17145c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y(a aVar, String str, long j8) {
        this.f17143a = str;
        this.f17144b = j8;
        this.f17145c = aVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        a.A(this.f17145c, this.f17143a, this.f17144b);
    }
}
