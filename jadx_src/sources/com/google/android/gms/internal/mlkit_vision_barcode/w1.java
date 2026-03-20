package com.google.android.gms.internal.mlkit_vision_barcode;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class w1 extends zzcv {

    /* renamed from: d  reason: collision with root package name */
    private final transient Object[] f14145d;

    /* renamed from: e  reason: collision with root package name */
    private final transient int f14146e;

    /* renamed from: f  reason: collision with root package name */
    private final transient int f14147f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w1(Object[] objArr, int i8, int i9) {
        this.f14145d = objArr;
        this.f14146e = i8;
        this.f14147f = i9;
    }

    @Override // java.util.List
    public final Object get(int i8) {
        u.a(i8, this.f14147f, "index");
        Object obj = this.f14145d[i8 + i8 + this.f14146e];
        obj.getClass();
        return obj;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f14147f;
    }
}
