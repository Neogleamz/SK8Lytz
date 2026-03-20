package com.google.android.gms.internal.mlkit_vision_barcode;

import java.io.UnsupportedEncodingException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class tg implements fg {

    /* renamed from: a  reason: collision with root package name */
    private final lc f14052a;

    /* renamed from: b  reason: collision with root package name */
    private df f14053b = new df();

    /* renamed from: c  reason: collision with root package name */
    private final int f14054c;

    private tg(lc lcVar, int i8) {
        this.f14052a = lcVar;
        ch.a();
        this.f14054c = i8;
    }

    public static fg e(lc lcVar) {
        return new tg(lcVar, 0);
    }

    public static fg f(lc lcVar, int i8) {
        return new tg(lcVar, 1);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.fg
    public final fg a(zzpk zzpkVar) {
        this.f14052a.f(zzpkVar);
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.fg
    public final String b() {
        ff g8 = this.f14052a.j().g();
        return (g8 == null || v.b(g8.k())) ? "NA" : (String) n6.j.l(g8.k());
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.fg
    public final fg c(df dfVar) {
        this.f14053b = dfVar;
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.fg
    public final byte[] d(int i8, boolean z4) {
        this.f14053b.f(Boolean.valueOf(1 == (i8 ^ 1)));
        this.f14053b.e(Boolean.FALSE);
        this.f14052a.i(this.f14053b.m());
        try {
            ch.a();
            if (i8 == 0) {
                return new l8.d().j(na.f13830a).k(true).i().encode(this.f14052a.j()).getBytes("utf-8");
            }
            nc j8 = this.f14052a.j();
            n2 n2Var = new n2();
            na.f13830a.a(n2Var);
            return n2Var.b().a(j8);
        } catch (UnsupportedEncodingException e8) {
            throw new UnsupportedOperationException("Failed to covert logging to UTF-8 byte array", e8);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.fg
    public final int zza() {
        return this.f14054c;
    }
}
