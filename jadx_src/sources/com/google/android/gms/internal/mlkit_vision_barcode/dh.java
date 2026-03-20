package com.google.android.gms.internal.mlkit_vision_barcode;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class dh extends fh {

    /* renamed from: a  reason: collision with root package name */
    private final float f13401a;

    /* renamed from: b  reason: collision with root package name */
    private final float f13402b;

    /* renamed from: c  reason: collision with root package name */
    private final float f13403c;

    /* renamed from: d  reason: collision with root package name */
    private final float f13404d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public dh(float f5, float f8, float f9, float f10, float f11) {
        this.f13401a = f5;
        this.f13402b = f8;
        this.f13403c = f9;
        this.f13404d = f10;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.fh
    final float a() {
        return 0.0f;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.fh
    final float b() {
        return this.f13403c;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.fh
    final float c() {
        return this.f13401a;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.fh
    final float d() {
        return this.f13404d;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.fh
    final float e() {
        return this.f13402b;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof fh) {
            fh fhVar = (fh) obj;
            if (Float.floatToIntBits(this.f13401a) == Float.floatToIntBits(fhVar.c()) && Float.floatToIntBits(this.f13402b) == Float.floatToIntBits(fhVar.e()) && Float.floatToIntBits(this.f13403c) == Float.floatToIntBits(fhVar.b()) && Float.floatToIntBits(this.f13404d) == Float.floatToIntBits(fhVar.d())) {
                int floatToIntBits = Float.floatToIntBits(0.0f);
                fhVar.a();
                if (floatToIntBits == Float.floatToIntBits(0.0f)) {
                    return true;
                }
            }
        }
        return false;
    }

    public final int hashCode() {
        return ((((((((Float.floatToIntBits(this.f13401a) ^ 1000003) * 1000003) ^ Float.floatToIntBits(this.f13402b)) * 1000003) ^ Float.floatToIntBits(this.f13403c)) * 1000003) ^ Float.floatToIntBits(this.f13404d)) * 1000003) ^ Float.floatToIntBits(0.0f);
    }

    public final String toString() {
        float f5 = this.f13401a;
        float f8 = this.f13402b;
        float f9 = this.f13403c;
        float f10 = this.f13404d;
        return "PredictedArea{xMin=" + f5 + ", yMin=" + f8 + ", xMax=" + f9 + ", yMax=" + f10 + ", confidenceScore=0.0}";
    }
}
