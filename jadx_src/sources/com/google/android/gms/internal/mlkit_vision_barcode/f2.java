package com.google.android.gms.internal.mlkit_vision_barcode;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f2 implements j2 {

    /* renamed from: a  reason: collision with root package name */
    private final int f13441a;

    /* renamed from: b  reason: collision with root package name */
    private final zzff f13442b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f2(int i8, zzff zzffVar) {
        this.f13441a = i8;
        this.f13442b = zzffVar;
    }

    @Override // java.lang.annotation.Annotation
    public final Class annotationType() {
        return j2.class;
    }

    @Override // java.lang.annotation.Annotation
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof j2) {
            j2 j2Var = (j2) obj;
            return this.f13441a == j2Var.zza() && this.f13442b.equals(j2Var.zzb());
        }
        return false;
    }

    @Override // java.lang.annotation.Annotation
    public final int hashCode() {
        return (this.f13441a ^ 14552422) + (this.f13442b.hashCode() ^ 2041407134);
    }

    @Override // java.lang.annotation.Annotation
    public final String toString() {
        return "@com.google.firebase.encoders.proto.Protobuf(tag=" + this.f13441a + "intEncoding=" + this.f13442b + ')';
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.j2
    public final int zza() {
        return this.f13441a;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.j2
    public final zzff zzb() {
        return this.f13442b;
    }
}
