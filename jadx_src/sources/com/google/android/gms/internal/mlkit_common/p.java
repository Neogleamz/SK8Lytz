package com.google.android.gms.internal.mlkit_common;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p extends zzar {

    /* renamed from: f  reason: collision with root package name */
    static final zzar f13000f = new p(new Object[0], 0);

    /* renamed from: d  reason: collision with root package name */
    final transient Object[] f13001d;

    /* renamed from: e  reason: collision with root package name */
    private final transient int f13002e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(Object[] objArr, int i8) {
        this.f13001d = objArr;
        this.f13002e = i8;
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzar, com.google.android.gms.internal.mlkit_common.zzan
    final int e(Object[] objArr, int i8) {
        System.arraycopy(this.f13001d, 0, objArr, 0, this.f13002e);
        return this.f13002e;
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzan
    final int g() {
        return this.f13002e;
    }

    @Override // java.util.List
    public final Object get(int i8) {
        d.a(i8, this.f13002e, "index");
        Object obj = this.f13001d[i8];
        obj.getClass();
        return obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_common.zzan
    public final int h() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_common.zzan
    public final Object[] k() {
        return this.f13001d;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f13002e;
    }
}
