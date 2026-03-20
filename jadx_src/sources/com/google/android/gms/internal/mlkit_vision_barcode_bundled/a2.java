package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a2 {

    /* renamed from: a  reason: collision with root package name */
    private final Object f14703a;

    /* renamed from: b  reason: collision with root package name */
    private final int f14704b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a2(Object obj, int i8) {
        this.f14703a = obj;
        this.f14704b = i8;
    }

    public final boolean equals(Object obj) {
        if (obj instanceof a2) {
            a2 a2Var = (a2) obj;
            return this.f14703a == a2Var.f14703a && this.f14704b == a2Var.f14704b;
        }
        return false;
    }

    public final int hashCode() {
        return (System.identityHashCode(this.f14703a) * 65535) + this.f14704b;
    }
}
