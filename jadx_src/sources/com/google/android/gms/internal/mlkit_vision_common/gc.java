package com.google.android.gms.internal.mlkit_vision_common;

import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class gc extends zzp {

    /* renamed from: c  reason: collision with root package name */
    final transient int f15508c;

    /* renamed from: d  reason: collision with root package name */
    final transient int f15509d;

    /* renamed from: e  reason: collision with root package name */
    final /* synthetic */ zzp f15510e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public gc(zzp zzpVar, int i8, int i9) {
        this.f15510e = zzpVar;
        this.f15508c = i8;
        this.f15509d = i9;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl
    final int g() {
        return this.f15510e.h() + this.f15508c + this.f15509d;
    }

    @Override // java.util.List
    public final Object get(int i8) {
        e4.a(i8, this.f15509d, "index");
        return this.f15510e.get(i8 + this.f15508c);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl
    public final int h() {
        return this.f15510e.h() + this.f15508c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_common.zzl
    public final Object[] k() {
        return this.f15510e.k();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzp
    public final zzp n(int i8, int i9) {
        e4.c(i8, i9, this.f15509d);
        zzp zzpVar = this.f15510e;
        int i10 = this.f15508c;
        return zzpVar.subList(i8 + i10, i9 + i10);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f15509d;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzp, java.util.List
    public final /* bridge */ /* synthetic */ List subList(int i8, int i9) {
        return subList(i8, i9);
    }
}
