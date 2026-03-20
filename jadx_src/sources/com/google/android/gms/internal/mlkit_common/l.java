package com.google.android.gms.internal.mlkit_common;

import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l extends zzar {

    /* renamed from: d  reason: collision with root package name */
    final transient int f12987d;

    /* renamed from: e  reason: collision with root package name */
    final transient int f12988e;

    /* renamed from: f  reason: collision with root package name */
    final /* synthetic */ zzar f12989f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l(zzar zzarVar, int i8, int i9) {
        this.f12989f = zzarVar;
        this.f12987d = i8;
        this.f12988e = i9;
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzan
    final int g() {
        return this.f12989f.h() + this.f12987d + this.f12988e;
    }

    @Override // java.util.List
    public final Object get(int i8) {
        d.a(i8, this.f12988e, "index");
        return this.f12989f.get(i8 + this.f12987d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_common.zzan
    public final int h() {
        return this.f12989f.h() + this.f12987d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_common.zzan
    public final Object[] k() {
        return this.f12989f.k();
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzar
    public final zzar n(int i8, int i9) {
        d.c(i8, i9, this.f12988e);
        zzar zzarVar = this.f12989f;
        int i10 = this.f12987d;
        return zzarVar.subList(i8 + i10, i9 + i10);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f12988e;
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzar, java.util.List
    public final /* bridge */ /* synthetic */ List subList(int i8, int i9) {
        return subList(i8, i9);
    }
}
