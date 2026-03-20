package com.google.android.gms.internal.mlkit_vision_common;

import java.util.AbstractMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class kc extends zzp {

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ lc f15636c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public kc(lc lcVar) {
        this.f15636c = lcVar;
    }

    @Override // java.util.List
    public final /* bridge */ /* synthetic */ Object get(int i8) {
        int i9;
        Object[] objArr;
        Object[] objArr2;
        i9 = this.f15636c.f15669e;
        e4.a(i8, i9, "index");
        lc lcVar = this.f15636c;
        objArr = lcVar.f15668d;
        int i10 = i8 + i8;
        Object obj = objArr[i10];
        obj.getClass();
        objArr2 = lcVar.f15668d;
        Object obj2 = objArr2[i10 + 1];
        obj2.getClass();
        return new AbstractMap.SimpleImmutableEntry(obj, obj2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        int i8;
        i8 = this.f15636c.f15669e;
        return i8;
    }
}
