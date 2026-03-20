package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y7 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f17181a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f17182b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ Object f17183c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ long f17184d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ h7 f17185e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y7(h7 h7Var, String str, String str2, Object obj, long j8) {
        this.f17181a = str;
        this.f17182b = str2;
        this.f17183c = obj;
        this.f17184d = j8;
        this.f17185e = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f17185e.Z(this.f17181a, this.f17182b, this.f17183c, this.f17184d);
    }
}
