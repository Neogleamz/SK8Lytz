package com.google.android.gms.internal.mlkit_vision_common;

import java.io.UnsupportedEncodingException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class sb implements jb {

    /* renamed from: a  reason: collision with root package name */
    private final b8 f15902a;

    /* renamed from: b  reason: collision with root package name */
    private fa f15903b = new fa();

    private sb(b8 b8Var, int i8) {
        this.f15902a = b8Var;
        ec.a();
    }

    public static jb e(b8 b8Var) {
        return new sb(b8Var, 0);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.jb
    public final String a() {
        ha c9 = this.f15902a.f().c();
        return (c9 == null || f5.b(c9.k())) ? "NA" : (String) n6.j.l(c9.k());
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.jb
    public final jb b(zziv zzivVar) {
        this.f15902a.c(zzivVar);
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.jb
    public final byte[] c(int i8, boolean z4) {
        this.f15903b.f(Boolean.valueOf(1 == (i8 ^ 1)));
        this.f15903b.e(Boolean.FALSE);
        this.f15902a.e(this.f15903b.m());
        try {
            ec.a();
            if (i8 == 0) {
                return new l8.d().j(l6.f15666a).k(true).i().encode(this.f15902a.f()).getBytes("utf-8");
            }
            d8 f5 = this.f15902a.f();
            m mVar = new m();
            l6.f15666a.a(mVar);
            return mVar.b().a(f5);
        } catch (UnsupportedEncodingException e8) {
            throw new UnsupportedOperationException("Failed to covert logging to UTF-8 byte array", e8);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.jb
    public final jb d(fa faVar) {
        this.f15903b = faVar;
        return this;
    }
}
