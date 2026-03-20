package com.google.android.gms.internal.mlkit_vision_barcode;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s1 extends zzcv {

    /* renamed from: f  reason: collision with root package name */
    static final zzcv f13983f = new s1(new Object[0], 0);

    /* renamed from: d  reason: collision with root package name */
    final transient Object[] f13984d;

    /* renamed from: e  reason: collision with root package name */
    private final transient int f13985e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s1(Object[] objArr, int i8) {
        this.f13984d = objArr;
        this.f13985e = i8;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcv, com.google.android.gms.internal.mlkit_vision_barcode.zzcq
    final int e(Object[] objArr, int i8) {
        System.arraycopy(this.f13984d, 0, objArr, i8, this.f13985e);
        return i8 + this.f13985e;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcq
    final int g() {
        return this.f13985e;
    }

    @Override // java.util.List
    public final Object get(int i8) {
        u.a(i8, this.f13985e, "index");
        Object obj = this.f13984d[i8];
        obj.getClass();
        return obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcq
    public final int h() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcq
    public final Object[] k() {
        return this.f13984d;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f13985e;
    }
}
