package com.google.android.gms.internal.mlkit_vision_barcode;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class vb {

    /* renamed from: a  reason: collision with root package name */
    private final zzoq f14132a;

    /* renamed from: b  reason: collision with root package name */
    private final Integer f14133b;

    /* renamed from: c  reason: collision with root package name */
    private final Integer f14134c;

    /* renamed from: d  reason: collision with root package name */
    private final Boolean f14135d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ vb(tb tbVar, ub ubVar) {
        zzoq zzoqVar;
        Integer num;
        zzoqVar = tbVar.f14050a;
        this.f14132a = zzoqVar;
        num = tbVar.f14051b;
        this.f14133b = num;
        this.f14134c = null;
        this.f14135d = null;
    }

    @j2(zza = 1)
    public final zzoq a() {
        return this.f14132a;
    }

    @j2(zza = 2)
    public final Integer b() {
        return this.f14133b;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof vb) {
            vb vbVar = (vb) obj;
            return n6.i.a(this.f14132a, vbVar.f14132a) && n6.i.a(this.f14133b, vbVar.f14133b) && n6.i.a(null, null) && n6.i.a(null, null);
        }
        return false;
    }

    public final int hashCode() {
        return n6.i.b(this.f14132a, this.f14133b, null, null);
    }
}
