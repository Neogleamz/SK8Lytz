package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n2 implements f2 {

    /* renamed from: a  reason: collision with root package name */
    final int f14816a;

    /* renamed from: b  reason: collision with root package name */
    final zzho f14817b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n2(s2 s2Var, int i8, zzho zzhoVar, boolean z4, boolean z8) {
        this.f14816a = i8;
        this.f14817b = zzhoVar;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.f2
    public final zzho b() {
        return this.f14817b;
    }

    @Override // java.lang.Comparable
    public final /* synthetic */ int compareTo(Object obj) {
        return this.f14816a - ((n2) obj).f14816a;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.f2
    public final zzhp d() {
        return this.f14817b.c();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.f2
    public final boolean e() {
        return false;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.f2
    public final boolean g() {
        return false;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.f2
    public final c4 m(c4 c4Var, c4 c4Var2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.f2
    public final w3 p(w3 w3Var, x3 x3Var) {
        ((j2) w3Var).k((p2) x3Var);
        return w3Var;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.f2
    public final int zza() {
        return this.f14816a;
    }
}
