package com.google.android.gms.internal.mlkit_common;

import java.util.AbstractMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class q extends zzar {

    /* renamed from: d  reason: collision with root package name */
    final /* synthetic */ r f13006d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q(r rVar) {
        this.f13006d = rVar;
    }

    @Override // java.util.List
    public final /* bridge */ /* synthetic */ Object get(int i8) {
        int i9;
        Object[] objArr;
        Object[] objArr2;
        i9 = this.f13006d.f13009e;
        d.a(i8, i9, "index");
        r rVar = this.f13006d;
        objArr = rVar.f13008d;
        int i10 = i8 + i8;
        Object obj = objArr[i10];
        obj.getClass();
        objArr2 = rVar.f13008d;
        Object obj2 = objArr2[i10 + 1];
        obj2.getClass();
        return new AbstractMap.SimpleImmutableEntry(obj, obj2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        int i8;
        i8 = this.f13006d.f13009e;
        return i8;
    }
}
