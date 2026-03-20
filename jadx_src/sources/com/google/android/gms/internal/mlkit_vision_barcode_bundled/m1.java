package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.nio.charset.Charset;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m1 extends l1 {

    /* renamed from: e  reason: collision with root package name */
    protected final byte[] f14809e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m1(byte[] bArr) {
        Objects.requireNonNull(bArr);
        this.f14809e = bArr;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.l1
    final boolean N(zzdb zzdbVar, int i8, int i9) {
        if (i9 > zzdbVar.i()) {
            int i10 = i();
            throw new IllegalArgumentException("Length too large: " + i9 + i10);
        }
        int i11 = i8 + i9;
        if (i11 > zzdbVar.i()) {
            int i12 = zzdbVar.i();
            throw new IllegalArgumentException("Ran off end of other: " + i8 + ", " + i9 + ", " + i12);
        } else if (zzdbVar instanceof m1) {
            m1 m1Var = (m1) zzdbVar;
            byte[] bArr = this.f14809e;
            byte[] bArr2 = m1Var.f14809e;
            int O = O() + i9;
            int O2 = O();
            int O3 = m1Var.O() + i8;
            while (O2 < O) {
                if (bArr[O2] != bArr2[O3]) {
                    return false;
                }
                O2++;
                O3++;
            }
            return true;
        } else {
            return zzdbVar.u(i8, i11).equals(u(0, i9));
        }
    }

    protected int O() {
        return 0;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public byte e(int i8) {
        return this.f14809e[i8];
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if ((obj instanceof zzdb) && i() == ((zzdb) obj).i()) {
            if (i() == 0) {
                return true;
            }
            if (obj instanceof m1) {
                m1 m1Var = (m1) obj;
                int D = D();
                int D2 = m1Var.D();
                if (D == 0 || D2 == 0 || D == D2) {
                    return N(m1Var, 0, i());
                }
                return false;
            }
            return obj.equals(this);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public byte g(int i8) {
        return this.f14809e[i8];
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public int i() {
        return this.f14809e.length;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public void k(byte[] bArr, int i8, int i9, int i10) {
        System.arraycopy(this.f14809e, i8, bArr, i9, i10);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final int q(int i8, int i9, int i10) {
        return y2.b(i8, this.f14809e, O() + i9, i10);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final int t(int i8, int i9, int i10) {
        int O = O() + i9;
        return x5.f(i8, this.f14809e, O, i10 + O);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final zzdb u(int i8, int i9) {
        int A = zzdb.A(i8, i9, i());
        return A == 0 ? zzdb.f14977b : new j1(this.f14809e, O() + i8, A);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    protected final String v(Charset charset) {
        return new String(this.f14809e, O(), i(), charset);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final void x(f1 f1Var) {
        ((u1) f1Var).C(this.f14809e, O(), i());
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final boolean y() {
        int O = O();
        return x5.h(this.f14809e, O, i() + O);
    }
}
