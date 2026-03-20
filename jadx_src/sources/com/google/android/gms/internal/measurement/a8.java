package com.google.android.gms.internal.measurement;

import java.nio.charset.Charset;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a8 extends x7 {

    /* renamed from: e  reason: collision with root package name */
    protected final byte[] f12067e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a8(byte[] bArr) {
        Objects.requireNonNull(bArr);
        this.f12067e = bArr;
    }

    @Override // com.google.android.gms.internal.measurement.zzij
    public final boolean D() {
        int F = F();
        return dc.f(this.f12067e, F, v() + F);
    }

    @Override // com.google.android.gms.internal.measurement.x7
    final boolean E(zzij zzijVar, int i8, int i9) {
        if (i9 > zzijVar.v()) {
            int v8 = v();
            throw new IllegalArgumentException("Length too large: " + i9 + v8);
        } else if (i9 > zzijVar.v()) {
            int v9 = zzijVar.v();
            throw new IllegalArgumentException("Ran off end of other: 0, " + i9 + ", " + v9);
        } else if (zzijVar instanceof a8) {
            a8 a8Var = (a8) zzijVar;
            byte[] bArr = this.f12067e;
            byte[] bArr2 = a8Var.f12067e;
            int F = F() + i9;
            int F2 = F();
            int F3 = a8Var.F();
            while (F2 < F) {
                if (bArr[F2] != bArr2[F3]) {
                    return false;
                }
                F2++;
                F3++;
            }
            return true;
        } else {
            return zzijVar.k(0, i9).equals(k(0, i9));
        }
    }

    protected int F() {
        return 0;
    }

    @Override // com.google.android.gms.internal.measurement.zzij
    public byte e(int i8) {
        return this.f12067e[i8];
    }

    @Override // com.google.android.gms.internal.measurement.zzij
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if ((obj instanceof zzij) && v() == ((zzij) obj).v()) {
            if (v() == 0) {
                return true;
            }
            if (obj instanceof a8) {
                a8 a8Var = (a8) obj;
                int g8 = g();
                int g9 = a8Var.g();
                if (g8 == 0 || g9 == 0 || g8 == g9) {
                    return E(a8Var, 0, v());
                }
                return false;
            }
            return obj.equals(this);
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.zzij
    public final zzij k(int i8, int i9) {
        int i10 = zzij.i(0, i9, v());
        return i10 == 0 ? zzij.f12852b : new u7(this.f12067e, F(), i10);
    }

    @Override // com.google.android.gms.internal.measurement.zzij
    protected final String q(Charset charset) {
        return new String(this.f12067e, F(), v(), charset);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.zzij
    public final void t(o7 o7Var) {
        o7Var.a(this.f12067e, F(), v());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.zzij
    public byte u(int i8) {
        return this.f12067e[i8];
    }

    @Override // com.google.android.gms.internal.measurement.zzij
    public int v() {
        return this.f12067e.length;
    }

    @Override // com.google.android.gms.internal.measurement.zzij
    protected final int x(int i8, int i9, int i10) {
        return a9.a(i8, this.f12067e, F(), i10);
    }
}
