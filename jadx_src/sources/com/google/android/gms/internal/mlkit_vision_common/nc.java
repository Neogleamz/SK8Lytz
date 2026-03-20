package com.google.android.gms.internal.mlkit_vision_common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class nc extends zzp {

    /* renamed from: c  reason: collision with root package name */
    private final transient Object[] f15725c;

    /* renamed from: d  reason: collision with root package name */
    private final transient int f15726d;

    /* renamed from: e  reason: collision with root package name */
    private final transient int f15727e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public nc(Object[] objArr, int i8, int i9) {
        this.f15725c = objArr;
        this.f15726d = i8;
        this.f15727e = i9;
    }

    @Override // java.util.List
    public final Object get(int i8) {
        e4.a(i8, this.f15727e, "index");
        Object obj = this.f15725c[i8 + i8 + this.f15726d];
        obj.getClass();
        return obj;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f15727e;
    }
}
