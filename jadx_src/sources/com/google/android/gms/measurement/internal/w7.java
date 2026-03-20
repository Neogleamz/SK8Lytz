package com.google.android.gms.measurement.internal;

import android.os.Bundle;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w7 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f17074a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f17075b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ long f17076c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ Bundle f17077d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ boolean f17078e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ boolean f17079f;

    /* renamed from: g  reason: collision with root package name */
    private final /* synthetic */ boolean f17080g;

    /* renamed from: h  reason: collision with root package name */
    private final /* synthetic */ String f17081h;

    /* renamed from: j  reason: collision with root package name */
    private final /* synthetic */ h7 f17082j;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w7(h7 h7Var, String str, String str2, long j8, Bundle bundle, boolean z4, boolean z8, boolean z9, String str3) {
        this.f17074a = str;
        this.f17075b = str2;
        this.f17076c = j8;
        this.f17077d = bundle;
        this.f17078e = z4;
        this.f17079f = z8;
        this.f17080g = z9;
        this.f17081h = str3;
        this.f17082j = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f17082j.U(this.f17074a, this.f17075b, this.f17076c, this.f17077d, this.f17078e, this.f17079f, this.f17080g, this.f17081h);
    }
}
