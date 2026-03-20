package com.google.android.gms.internal.mlkit_vision_barcode;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ag {

    /* renamed from: a  reason: collision with root package name */
    private final zzcv f13286a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ ag(yf yfVar, zf zfVar) {
        zzcv zzcvVar;
        zzcvVar = yfVar.f14259a;
        this.f13286a = zzcvVar;
    }

    @j2(zza = 1)
    public final zzcv a() {
        return this.f13286a;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ag) {
            return n6.i.a(this.f13286a, ((ag) obj).f13286a);
        }
        return false;
    }

    public final int hashCode() {
        return n6.i.b(this.f13286a);
    }
}
