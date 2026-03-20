package com.google.android.gms.internal.mlkit_common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class t extends zzar {

    /* renamed from: d  reason: collision with root package name */
    private final transient Object[] f13013d;

    /* renamed from: e  reason: collision with root package name */
    private final transient int f13014e;

    /* renamed from: f  reason: collision with root package name */
    private final transient int f13015f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t(Object[] objArr, int i8, int i9) {
        this.f13013d = objArr;
        this.f13014e = i8;
        this.f13015f = i9;
    }

    @Override // java.util.List
    public final Object get(int i8) {
        d.a(i8, this.f13015f, "index");
        Object obj = this.f13013d[i8 + i8 + this.f13014e];
        obj.getClass();
        return obj;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f13015f;
    }
}
