package com.google.android.gms.internal.mlkit_vision_barcode;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y2 {

    /* renamed from: a  reason: collision with root package name */
    private final zzpj f14227a;

    /* renamed from: c  reason: collision with root package name */
    private final Boolean f14229c;

    /* renamed from: e  reason: collision with root package name */
    private final ag f14231e;

    /* renamed from: f  reason: collision with root package name */
    private final zzcv f14232f;

    /* renamed from: g  reason: collision with root package name */
    private final zzcv f14233g;

    /* renamed from: b  reason: collision with root package name */
    private final Boolean f14228b = null;

    /* renamed from: d  reason: collision with root package name */
    private final vb f14230d = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ y2(w2 w2Var, x2 x2Var) {
        this.f14227a = w2.i(w2Var);
        this.f14229c = w2.k(w2Var);
        this.f14231e = w2.j(w2Var);
        this.f14232f = w2.a(w2Var);
        this.f14233g = w2.b(w2Var);
    }

    @j2(zza = 6)
    public final zzcv a() {
        return this.f14232f;
    }

    @j2(zza = 7)
    public final zzcv b() {
        return this.f14233g;
    }

    @j2(zza = 1)
    public final zzpj c() {
        return this.f14227a;
    }

    @j2(zza = 5)
    public final ag d() {
        return this.f14231e;
    }

    @j2(zza = 3)
    public final Boolean e() {
        return this.f14229c;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof y2) {
            y2 y2Var = (y2) obj;
            return n6.i.a(this.f14227a, y2Var.f14227a) && n6.i.a(null, null) && n6.i.a(this.f14229c, y2Var.f14229c) && n6.i.a(null, null) && n6.i.a(this.f14231e, y2Var.f14231e) && n6.i.a(this.f14232f, y2Var.f14232f) && n6.i.a(this.f14233g, y2Var.f14233g);
        }
        return false;
    }

    public final int hashCode() {
        return n6.i.b(this.f14227a, null, this.f14229c, null, this.f14231e, this.f14232f, this.f14233g);
    }
}
