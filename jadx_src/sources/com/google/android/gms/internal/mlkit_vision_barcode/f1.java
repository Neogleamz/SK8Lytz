package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f1 extends zzcv {

    /* renamed from: d  reason: collision with root package name */
    final transient int f13438d;

    /* renamed from: e  reason: collision with root package name */
    final transient int f13439e;

    /* renamed from: f  reason: collision with root package name */
    final /* synthetic */ zzcv f13440f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f1(zzcv zzcvVar, int i8, int i9) {
        this.f13440f = zzcvVar;
        this.f13438d = i8;
        this.f13439e = i9;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcq
    final int g() {
        return this.f13440f.h() + this.f13438d + this.f13439e;
    }

    @Override // java.util.List
    public final Object get(int i8) {
        u.a(i8, this.f13439e, "index");
        return this.f13440f.get(i8 + this.f13438d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcq
    public final int h() {
        return this.f13440f.h() + this.f13438d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcq
    public final Object[] k() {
        return this.f13440f.k();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcv
    public final zzcv n(int i8, int i9) {
        u.d(i8, i9, this.f13439e);
        zzcv zzcvVar = this.f13440f;
        int i10 = this.f13438d;
        return zzcvVar.subList(i8 + i10, i9 + i10);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f13439e;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.zzcv, java.util.List
    public final /* bridge */ /* synthetic */ List subList(int i8, int i9) {
        return subList(i8, i9);
    }
}
