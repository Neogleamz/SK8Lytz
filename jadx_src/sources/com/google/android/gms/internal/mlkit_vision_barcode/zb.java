package com.google.android.gms.internal.mlkit_vision_barcode;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zb {

    /* renamed from: a */
    private Long f14292a;

    /* renamed from: b */
    private zzpj f14293b;

    /* renamed from: c */
    private Boolean f14294c;

    /* renamed from: d */
    private Boolean f14295d;

    /* renamed from: e */
    private Boolean f14296e;

    public final zb a(Boolean bool) {
        this.f14295d = bool;
        return this;
    }

    public final zb b(Boolean bool) {
        this.f14296e = bool;
        return this;
    }

    public final zb c(Long l8) {
        this.f14292a = Long.valueOf(l8.longValue() & Long.MAX_VALUE);
        return this;
    }

    public final zb d(zzpj zzpjVar) {
        this.f14293b = zzpjVar;
        return this;
    }

    public final zb e(Boolean bool) {
        this.f14294c = bool;
        return this;
    }

    public final bc f() {
        return new bc(this, null);
    }
}
