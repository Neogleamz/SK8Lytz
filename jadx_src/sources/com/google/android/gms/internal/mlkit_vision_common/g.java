package com.google.android.gms.internal.mlkit_vision_common;

import java.io.OutputStream;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g extends OutputStream {

    /* renamed from: a  reason: collision with root package name */
    private long f15481a = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long a() {
        return this.f15481a;
    }

    @Override // java.io.OutputStream
    public final void write(int i8) {
        this.f15481a++;
    }

    @Override // java.io.OutputStream
    public final void write(byte[] bArr) {
        this.f15481a += bArr.length;
    }

    @Override // java.io.OutputStream
    public final void write(byte[] bArr, int i8, int i9) {
        int length;
        int i10;
        if (i8 < 0 || i8 > (length = bArr.length) || i9 < 0 || (i10 = i8 + i9) > length || i10 < 0) {
            throw new IndexOutOfBoundsException();
        }
        this.f15481a += i9;
    }
}
