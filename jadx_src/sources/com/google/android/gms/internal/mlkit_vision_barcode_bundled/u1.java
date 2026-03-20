package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u1 extends w1 {

    /* renamed from: e  reason: collision with root package name */
    private final byte[] f14866e;

    /* renamed from: f  reason: collision with root package name */
    private final int f14867f;

    /* renamed from: g  reason: collision with root package name */
    private int f14868g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public u1(byte[] bArr, int i8, int i9) {
        super(null);
        Objects.requireNonNull(bArr, "buffer");
        int length = bArr.length;
        if (((length - i9) | i9) < 0) {
            throw new IllegalArgumentException(String.format("Array range is invalid. Buffer.length=%d, offset=%d, length=%d", Integer.valueOf(length), 0, Integer.valueOf(i9)));
        }
        this.f14866e = bArr;
        this.f14868g = 0;
        this.f14867f = i9;
    }

    public final void C(byte[] bArr, int i8, int i9) {
        try {
            System.arraycopy(bArr, i8, this.f14866e, this.f14868g, i9);
            this.f14868g += i9;
        } catch (IndexOutOfBoundsException e8) {
            throw new zzdh(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.f14868g), Integer.valueOf(this.f14867f), Integer.valueOf(i9)), e8);
        }
    }

    public final void D(String str) {
        int i8 = this.f14868g;
        try {
            int A = w1.A(str.length() * 3);
            int A2 = w1.A(str.length());
            if (A2 != A) {
                s(x5.e(str));
                byte[] bArr = this.f14866e;
                int i9 = this.f14868g;
                this.f14868g = x5.d(str, bArr, i9, this.f14867f - i9);
                return;
            }
            int i10 = i8 + A2;
            this.f14868g = i10;
            int d8 = x5.d(str, this.f14866e, i10, this.f14867f - i10);
            this.f14868g = i8;
            s((d8 - i8) - A2);
            this.f14868g = d8;
        } catch (w5 e8) {
            this.f14868g = i8;
            c(str, e8);
        } catch (IndexOutOfBoundsException e9) {
            throw new zzdh(e9);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final int e() {
        return this.f14867f - this.f14868g;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void f(byte b9) {
        try {
            byte[] bArr = this.f14866e;
            int i8 = this.f14868g;
            this.f14868g = i8 + 1;
            bArr[i8] = b9;
        } catch (IndexOutOfBoundsException e8) {
            throw new zzdh(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.f14868g), Integer.valueOf(this.f14867f), 1), e8);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void g(int i8, boolean z4) {
        s(i8 << 3);
        f(z4 ? (byte) 1 : (byte) 0);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void h(int i8, zzdb zzdbVar) {
        s((i8 << 3) | 2);
        s(zzdbVar.i());
        zzdbVar.x(this);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void i(int i8, int i9) {
        s((i8 << 3) | 5);
        j(i9);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void j(int i8) {
        try {
            byte[] bArr = this.f14866e;
            int i9 = this.f14868g;
            int i10 = i9 + 1;
            this.f14868g = i10;
            bArr[i9] = (byte) (i8 & 255);
            int i11 = i10 + 1;
            this.f14868g = i11;
            bArr[i10] = (byte) ((i8 >> 8) & 255);
            int i12 = i11 + 1;
            this.f14868g = i12;
            bArr[i11] = (byte) ((i8 >> 16) & 255);
            this.f14868g = i12 + 1;
            bArr[i12] = (byte) ((i8 >> 24) & 255);
        } catch (IndexOutOfBoundsException e8) {
            throw new zzdh(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.f14868g), Integer.valueOf(this.f14867f), 1), e8);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void k(int i8, long j8) {
        s((i8 << 3) | 1);
        l(j8);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void l(long j8) {
        try {
            byte[] bArr = this.f14866e;
            int i8 = this.f14868g;
            int i9 = i8 + 1;
            this.f14868g = i9;
            bArr[i8] = (byte) (((int) j8) & 255);
            int i10 = i9 + 1;
            this.f14868g = i10;
            bArr[i9] = (byte) (((int) (j8 >> 8)) & 255);
            int i11 = i10 + 1;
            this.f14868g = i11;
            bArr[i10] = (byte) (((int) (j8 >> 16)) & 255);
            int i12 = i11 + 1;
            this.f14868g = i12;
            bArr[i11] = (byte) (((int) (j8 >> 24)) & 255);
            int i13 = i12 + 1;
            this.f14868g = i13;
            bArr[i12] = (byte) (((int) (j8 >> 32)) & 255);
            int i14 = i13 + 1;
            this.f14868g = i14;
            bArr[i13] = (byte) (((int) (j8 >> 40)) & 255);
            int i15 = i14 + 1;
            this.f14868g = i15;
            bArr[i14] = (byte) (((int) (j8 >> 48)) & 255);
            this.f14868g = i15 + 1;
            bArr[i15] = (byte) (((int) (j8 >> 56)) & 255);
        } catch (IndexOutOfBoundsException e8) {
            throw new zzdh(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.f14868g), Integer.valueOf(this.f14867f), 1), e8);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void m(int i8, int i9) {
        s(i8 << 3);
        n(i9);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void n(int i8) {
        if (i8 >= 0) {
            s(i8);
        } else {
            u(i8);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void o(byte[] bArr, int i8, int i9) {
        C(bArr, 0, i9);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void p(int i8, String str) {
        s((i8 << 3) | 2);
        D(str);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void q(int i8, int i9) {
        s((i8 << 3) | i9);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void r(int i8, int i9) {
        s(i8 << 3);
        s(i9);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void s(int i8) {
        while ((i8 & (-128)) != 0) {
            try {
                byte[] bArr = this.f14866e;
                int i9 = this.f14868g;
                this.f14868g = i9 + 1;
                bArr[i9] = (byte) ((i8 & 127) | RecognitionOptions.ITF);
                i8 >>>= 7;
            } catch (IndexOutOfBoundsException e8) {
                throw new zzdh(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.f14868g), Integer.valueOf(this.f14867f), 1), e8);
            }
        }
        byte[] bArr2 = this.f14866e;
        int i10 = this.f14868g;
        this.f14868g = i10 + 1;
        bArr2[i10] = (byte) i8;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void t(int i8, long j8) {
        s(i8 << 3);
        u(j8);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w1
    public final void u(long j8) {
        boolean z4;
        z4 = w1.f14873c;
        if (z4 && this.f14867f - this.f14868g >= 10) {
            while ((j8 & (-128)) != 0) {
                byte[] bArr = this.f14866e;
                int i8 = this.f14868g;
                this.f14868g = i8 + 1;
                s5.s(bArr, i8, (byte) ((((int) j8) & 127) | RecognitionOptions.ITF));
                j8 >>>= 7;
            }
            byte[] bArr2 = this.f14866e;
            int i9 = this.f14868g;
            this.f14868g = i9 + 1;
            s5.s(bArr2, i9, (byte) j8);
            return;
        }
        while ((j8 & (-128)) != 0) {
            try {
                byte[] bArr3 = this.f14866e;
                int i10 = this.f14868g;
                this.f14868g = i10 + 1;
                bArr3[i10] = (byte) ((((int) j8) & 127) | RecognitionOptions.ITF);
                j8 >>>= 7;
            } catch (IndexOutOfBoundsException e8) {
                throw new zzdh(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.f14868g), Integer.valueOf(this.f14867f), 1), e8);
            }
        }
        byte[] bArr4 = this.f14866e;
        int i11 = this.f14868g;
        this.f14868g = i11 + 1;
        bArr4[i11] = (byte) j8;
    }
}
