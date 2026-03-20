package com.google.android.gms.internal.common;

import org.jspecify.nullness.NullMarked;
/* JADX INFO: Access modifiers changed from: package-private */
@NullMarked
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d extends zzag {

    /* renamed from: e  reason: collision with root package name */
    static final zzag f12043e = new d(new Object[0], 0);

    /* renamed from: c  reason: collision with root package name */
    final transient Object[] f12044c;

    /* renamed from: d  reason: collision with root package name */
    private final transient int f12045d;

    d(Object[] objArr, int i8) {
        this.f12044c = objArr;
        this.f12045d = i8;
    }

    @Override // com.google.android.gms.internal.common.zzag, com.google.android.gms.internal.common.zzac
    final int e(Object[] objArr, int i8) {
        System.arraycopy(this.f12044c, 0, objArr, 0, this.f12045d);
        return this.f12045d;
    }

    @Override // com.google.android.gms.internal.common.zzac
    final int g() {
        return this.f12045d;
    }

    @Override // java.util.List
    public final Object get(int i8) {
        m.a(i8, this.f12045d, "index");
        Object obj = this.f12044c[i8];
        obj.getClass();
        return obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.common.zzac
    public final int h() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.common.zzac
    public final Object[] k() {
        return this.f12044c;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f12045d;
    }
}
