package com.google.android.gms.internal.mlkit_vision_common;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class jc extends zzp {

    /* renamed from: e  reason: collision with root package name */
    static final zzp f15602e = new jc(new Object[0], 0);

    /* renamed from: c  reason: collision with root package name */
    final transient Object[] f15603c;

    /* renamed from: d  reason: collision with root package name */
    private final transient int f15604d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public jc(Object[] objArr, int i8) {
        this.f15603c = objArr;
        this.f15604d = i8;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzp, com.google.android.gms.internal.mlkit_vision_common.zzl
    final int e(Object[] objArr, int i8) {
        System.arraycopy(this.f15603c, 0, objArr, 0, this.f15604d);
        return this.f15604d;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl
    final int g() {
        return this.f15604d;
    }

    @Override // java.util.List
    public final Object get(int i8) {
        e4.a(i8, this.f15604d, "index");
        Object obj = this.f15603c[i8];
        obj.getClass();
        return obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl
    public final int h() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl
    public final Object[] k() {
        return this.f15603c;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f15604d;
    }
}
