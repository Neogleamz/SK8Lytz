package com.google.android.gms.internal.mlkit_vision_common;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e implements i {

    /* renamed from: a  reason: collision with root package name */
    private final int f15401a;

    /* renamed from: b  reason: collision with root package name */
    private final zzah f15402b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(int i8, zzah zzahVar) {
        this.f15401a = i8;
        this.f15402b = zzahVar;
    }

    @Override // java.lang.annotation.Annotation
    public final Class annotationType() {
        return i.class;
    }

    @Override // java.lang.annotation.Annotation
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof i) {
            i iVar = (i) obj;
            return this.f15401a == iVar.zza() && this.f15402b.equals(iVar.zzb());
        }
        return false;
    }

    @Override // java.lang.annotation.Annotation
    public final int hashCode() {
        return (this.f15401a ^ 14552422) + (this.f15402b.hashCode() ^ 2041407134);
    }

    @Override // java.lang.annotation.Annotation
    public final String toString() {
        return "@com.google.firebase.encoders.proto.Protobuf(tag=" + this.f15401a + "intEncoding=" + this.f15402b + ')';
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.i
    public final int zza() {
        return this.f15401a;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.i
    public final zzah zzb() {
        return this.f15402b;
    }
}
