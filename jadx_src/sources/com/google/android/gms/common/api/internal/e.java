package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.a;
import com.google.android.gms.common.api.a.b;
import com.google.android.gms.common.api.internal.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class e<A extends a.b, L> {

    /* renamed from: a  reason: collision with root package name */
    private final c f11628a;

    /* renamed from: b  reason: collision with root package name */
    private final Feature[] f11629b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f11630c;

    /* renamed from: d  reason: collision with root package name */
    private final int f11631d;

    /* JADX INFO: Access modifiers changed from: protected */
    public e(c<L> cVar, Feature[] featureArr, boolean z4, int i8) {
        this.f11628a = cVar;
        this.f11629b = featureArr;
        this.f11630c = z4;
        this.f11631d = i8;
    }

    public void a() {
        this.f11628a.a();
    }

    public c.a<L> b() {
        return this.f11628a.b();
    }

    public Feature[] c() {
        return this.f11629b;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void d(A a9, j7.k<Void> kVar);

    public final int e() {
        return this.f11631d;
    }

    public final boolean f() {
        return this.f11630c;
    }
}
