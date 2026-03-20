package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class p3 implements v3 {

    /* renamed from: a  reason: collision with root package name */
    private final v3[] f14831a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p3(v3... v3VarArr) {
        this.f14831a = v3VarArr;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.v3
    public final boolean a(Class cls) {
        v3[] v3VarArr = this.f14831a;
        for (int i8 = 0; i8 < 2; i8++) {
            if (v3VarArr[i8].a(cls)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.v3
    public final u3 b(Class cls) {
        v3[] v3VarArr = this.f14831a;
        for (int i8 = 0; i8 < 2; i8++) {
            v3 v3Var = v3VarArr[i8];
            if (v3Var.a(cls)) {
                return v3Var.b(cls);
            }
        }
        throw new UnsupportedOperationException("No factory is available for message type: ".concat(cls.getName()));
    }
}
