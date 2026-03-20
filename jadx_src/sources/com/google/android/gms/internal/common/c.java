package com.google.android.gms.internal.common;

import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c extends zzag {

    /* renamed from: c  reason: collision with root package name */
    final transient int f12040c;

    /* renamed from: d  reason: collision with root package name */
    final transient int f12041d;

    /* renamed from: e  reason: collision with root package name */
    final /* synthetic */ zzag f12042e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(zzag zzagVar, int i8, int i9) {
        this.f12042e = zzagVar;
        this.f12040c = i8;
        this.f12041d = i9;
    }

    @Override // com.google.android.gms.internal.common.zzac
    final int g() {
        return this.f12042e.h() + this.f12040c + this.f12041d;
    }

    @Override // java.util.List
    public final Object get(int i8) {
        m.a(i8, this.f12041d, "index");
        return this.f12042e.get(i8 + this.f12040c);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.common.zzac
    public final int h() {
        return this.f12042e.h() + this.f12040c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.common.zzac
    public final Object[] k() {
        return this.f12042e.k();
    }

    @Override // com.google.android.gms.internal.common.zzag
    public final zzag n(int i8, int i9) {
        m.c(i8, i9, this.f12041d);
        int i10 = this.f12040c;
        return this.f12042e.subList(i8 + i10, i9 + i10);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f12041d;
    }

    @Override // com.google.android.gms.internal.common.zzag, java.util.List
    public final /* bridge */ /* synthetic */ List subList(int i8, int i9) {
        return subList(i8, i9);
    }
}
