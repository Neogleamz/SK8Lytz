package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import sun.misc.Unsafe;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class q5 extends r5 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public q5(Unsafe unsafe) {
        super(unsafe);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r5
    public final double a(Object obj, long j8) {
        return Double.longBitsToDouble(this.f14848a.getLong(obj, j8));
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r5
    public final float b(Object obj, long j8) {
        return Float.intBitsToFloat(this.f14848a.getInt(obj, j8));
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r5
    public final void c(Object obj, long j8, boolean z4) {
        if (s5.f14857h) {
            s5.d(obj, j8, r3 ? (byte) 1 : (byte) 0);
        } else {
            s5.e(obj, j8, r3 ? (byte) 1 : (byte) 0);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r5
    public final void d(Object obj, long j8, byte b9) {
        if (s5.f14857h) {
            s5.d(obj, j8, b9);
        } else {
            s5.e(obj, j8, b9);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r5
    public final void e(Object obj, long j8, double d8) {
        this.f14848a.putLong(obj, j8, Double.doubleToLongBits(d8));
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r5
    public final void f(Object obj, long j8, float f5) {
        this.f14848a.putInt(obj, j8, Float.floatToIntBits(f5));
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.r5
    public final boolean g(Object obj, long j8) {
        return s5.f14857h ? s5.y(obj, j8) : s5.z(obj, j8);
    }
}
