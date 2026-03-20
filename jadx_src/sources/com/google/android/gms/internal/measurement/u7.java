package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class u7 extends a8 {

    /* renamed from: f  reason: collision with root package name */
    private final int f12549f;

    /* renamed from: g  reason: collision with root package name */
    private final int f12550g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public u7(byte[] bArr, int i8, int i9) {
        super(bArr);
        zzij.i(i8, i8 + i9, bArr.length);
        this.f12549f = i8;
        this.f12550g = i9;
    }

    @Override // com.google.android.gms.internal.measurement.a8
    protected final int F() {
        return this.f12549f;
    }

    @Override // com.google.android.gms.internal.measurement.a8, com.google.android.gms.internal.measurement.zzij
    public final byte e(int i8) {
        int v8 = v();
        if (((v8 - (i8 + 1)) | i8) < 0) {
            if (i8 < 0) {
                throw new ArrayIndexOutOfBoundsException("Index < 0: " + i8);
            }
            throw new ArrayIndexOutOfBoundsException("Index > length: " + i8 + ", " + v8);
        }
        return this.f12067e[this.f12549f + i8];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.a8, com.google.android.gms.internal.measurement.zzij
    public final byte u(int i8) {
        return this.f12067e[this.f12549f + i8];
    }

    @Override // com.google.android.gms.internal.measurement.a8, com.google.android.gms.internal.measurement.zzij
    public final int v() {
        return this.f12550g;
    }
}
