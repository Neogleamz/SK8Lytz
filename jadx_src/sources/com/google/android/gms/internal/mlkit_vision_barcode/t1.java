package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.AbstractMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class t1 extends zzcv {

    /* renamed from: d  reason: collision with root package name */
    final /* synthetic */ u1 f14026d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t1(u1 u1Var) {
        this.f14026d = u1Var;
    }

    @Override // java.util.List
    public final /* bridge */ /* synthetic */ Object get(int i8) {
        int i9;
        Object[] objArr;
        Object[] objArr2;
        i9 = this.f14026d.f14058e;
        u.a(i8, i9, "index");
        u1 u1Var = this.f14026d;
        objArr = u1Var.f14057d;
        int i10 = i8 + i8;
        Object obj = objArr[i10];
        obj.getClass();
        objArr2 = u1Var.f14057d;
        Object obj2 = objArr2[i10 + 1];
        obj2.getClass();
        return new AbstractMap.SimpleImmutableEntry(obj, obj2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        int i8;
        i8 = this.f14026d.f14058e;
        return i8;
    }
}
