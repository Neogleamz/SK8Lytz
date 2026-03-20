package com.google.android.gms.internal.measurement;

import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class zzja extends o7 {

    /* renamed from: b  reason: collision with root package name */
    private static final Logger f12856b = Logger.getLogger(zzja.class.getName());

    /* renamed from: c  reason: collision with root package name */
    private static final boolean f12857c = yb.w();

    /* renamed from: a  reason: collision with root package name */
    h8 f12858a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a extends zzja {

        /* renamed from: d  reason: collision with root package name */
        private final byte[] f12859d;

        /* renamed from: e  reason: collision with root package name */
        private final int f12860e;

        /* renamed from: f  reason: collision with root package name */
        private final int f12861f;

        /* renamed from: g  reason: collision with root package name */
        private int f12862g;

        a(byte[] bArr, int i8, int i9) {
            super();
            Objects.requireNonNull(bArr, "buffer");
            if ((i9 | 0 | (bArr.length - i9)) < 0) {
                throw new IllegalArgumentException(String.format("Array range is invalid. Buffer.length=%d, offset=%d, length=%d", Integer.valueOf(bArr.length), 0, Integer.valueOf(i9)));
            }
            this.f12859d = bArr;
            this.f12860e = 0;
            this.f12862g = 0;
            this.f12861f = i9;
        }

        private final void E0(byte[] bArr, int i8, int i9) {
            try {
                System.arraycopy(bArr, i8, this.f12859d, this.f12862g, i9);
                this.f12862g += i9;
            } catch (IndexOutOfBoundsException e8) {
                throw new zza(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.f12862g), Integer.valueOf(this.f12861f), Integer.valueOf(i9)), e8);
            }
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void A0(int i8, int i9) {
            B0((i8 << 3) | i9);
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void B0(int i8) {
            while ((i8 & (-128)) != 0) {
                try {
                    byte[] bArr = this.f12859d;
                    int i9 = this.f12862g;
                    this.f12862g = i9 + 1;
                    bArr[i9] = (byte) (i8 | RecognitionOptions.ITF);
                    i8 >>>= 7;
                } catch (IndexOutOfBoundsException e8) {
                    throw new zza(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.f12862g), Integer.valueOf(this.f12861f), 1), e8);
                }
            }
            byte[] bArr2 = this.f12859d;
            int i10 = this.f12862g;
            this.f12862g = i10 + 1;
            bArr2[i10] = (byte) i8;
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void C0(int i8, int i9) {
            A0(i8, 0);
            B0(i9);
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void N(int i8, ia iaVar) {
            A0(1, 3);
            C0(2, i8);
            A0(3, 2);
            Z(iaVar);
            A0(1, 4);
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void O(int i8, String str) {
            A0(i8, 2);
            R(str);
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void P(int i8, boolean z4) {
            A0(i8, 0);
            x(z4 ? (byte) 1 : (byte) 0);
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void Q(zzij zzijVar) {
            B0(zzijVar.v());
            zzijVar.t(this);
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void R(String str) {
            int i8 = this.f12862g;
            try {
                int o02 = zzja.o0(str.length() * 3);
                int o03 = zzja.o0(str.length());
                if (o03 != o02) {
                    B0(dc.a(str));
                    this.f12862g = dc.b(str, this.f12859d, this.f12862g, b());
                    return;
                }
                int i9 = i8 + o03;
                this.f12862g = i9;
                int b9 = dc.b(str, this.f12859d, i9, b());
                this.f12862g = i8;
                B0((b9 - i8) - o03);
                this.f12862g = b9;
            } catch (gc e8) {
                this.f12862g = i8;
                y(str, e8);
            } catch (IndexOutOfBoundsException e9) {
                throw new zza(e9);
            }
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void X(int i8, zzij zzijVar) {
            A0(i8, 2);
            Q(zzijVar);
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        final void Y(int i8, ia iaVar, xa xaVar) {
            A0(i8, 2);
            B0(((g7) iaVar).a(xaVar));
            xaVar.d(iaVar, this.f12858a);
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void Z(ia iaVar) {
            B0(iaVar.f());
            iaVar.b(this);
        }

        @Override // com.google.android.gms.internal.measurement.o7
        public final void a(byte[] bArr, int i8, int i9) {
            E0(bArr, i8, i9);
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final int b() {
            return this.f12861f - this.f12862g;
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void f0(int i8, zzij zzijVar) {
            A0(1, 3);
            C0(2, i8);
            X(3, zzijVar);
            A0(1, 4);
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void m0(int i8, long j8) {
            A0(i8, 1);
            n0(j8);
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void n0(long j8) {
            try {
                byte[] bArr = this.f12859d;
                int i8 = this.f12862g;
                int i9 = i8 + 1;
                this.f12862g = i9;
                bArr[i8] = (byte) j8;
                int i10 = i9 + 1;
                this.f12862g = i10;
                bArr[i9] = (byte) (j8 >> 8);
                int i11 = i10 + 1;
                this.f12862g = i11;
                bArr[i10] = (byte) (j8 >> 16);
                int i12 = i11 + 1;
                this.f12862g = i12;
                bArr[i11] = (byte) (j8 >> 24);
                int i13 = i12 + 1;
                this.f12862g = i13;
                bArr[i12] = (byte) (j8 >> 32);
                int i14 = i13 + 1;
                this.f12862g = i14;
                bArr[i13] = (byte) (j8 >> 40);
                int i15 = i14 + 1;
                this.f12862g = i15;
                bArr[i14] = (byte) (j8 >> 48);
                this.f12862g = i15 + 1;
                bArr[i15] = (byte) (j8 >> 56);
            } catch (IndexOutOfBoundsException e8) {
                throw new zza(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.f12862g), Integer.valueOf(this.f12861f), 1), e8);
            }
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void p0(int i8, int i9) {
            A0(i8, 5);
            s0(i9);
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void s0(int i8) {
            try {
                byte[] bArr = this.f12859d;
                int i9 = this.f12862g;
                int i10 = i9 + 1;
                this.f12862g = i10;
                bArr[i9] = (byte) i8;
                int i11 = i10 + 1;
                this.f12862g = i11;
                bArr[i10] = (byte) (i8 >> 8);
                int i12 = i11 + 1;
                this.f12862g = i12;
                bArr[i11] = (byte) (i8 >> 16);
                this.f12862g = i12 + 1;
                bArr[i12] = (byte) (i8 >>> 24);
            } catch (IndexOutOfBoundsException e8) {
                throw new zza(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.f12862g), Integer.valueOf(this.f12861f), 1), e8);
            }
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void t0(int i8, int i9) {
            A0(i8, 0);
            x0(i9);
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void u0(int i8, long j8) {
            A0(i8, 0);
            v0(j8);
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void v0(long j8) {
            if (zzja.f12857c && b() >= 10) {
                while ((j8 & (-128)) != 0) {
                    byte[] bArr = this.f12859d;
                    int i8 = this.f12862g;
                    this.f12862g = i8 + 1;
                    yb.m(bArr, i8, (byte) (((int) j8) | RecognitionOptions.ITF));
                    j8 >>>= 7;
                }
                byte[] bArr2 = this.f12859d;
                int i9 = this.f12862g;
                this.f12862g = i9 + 1;
                yb.m(bArr2, i9, (byte) j8);
                return;
            }
            while ((j8 & (-128)) != 0) {
                try {
                    byte[] bArr3 = this.f12859d;
                    int i10 = this.f12862g;
                    this.f12862g = i10 + 1;
                    bArr3[i10] = (byte) (((int) j8) | RecognitionOptions.ITF);
                    j8 >>>= 7;
                } catch (IndexOutOfBoundsException e8) {
                    throw new zza(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.f12862g), Integer.valueOf(this.f12861f), 1), e8);
                }
            }
            byte[] bArr4 = this.f12859d;
            int i11 = this.f12862g;
            this.f12862g = i11 + 1;
            bArr4[i11] = (byte) j8;
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void x(byte b9) {
            try {
                byte[] bArr = this.f12859d;
                int i8 = this.f12862g;
                this.f12862g = i8 + 1;
                bArr[i8] = b9;
            } catch (IndexOutOfBoundsException e8) {
                throw new zza(String.format("Pos: %d, limit: %d, len: %d", Integer.valueOf(this.f12862g), Integer.valueOf(this.f12861f), 1), e8);
            }
        }

        @Override // com.google.android.gms.internal.measurement.zzja
        public final void x0(int i8) {
            if (i8 >= 0) {
                B0(i8);
            } else {
                v0(i8);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class zza extends IOException {
        zza() {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.");
        }

        zza(String str, Throwable th) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.: " + str, th);
        }

        zza(Throwable th) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.", th);
        }
    }

    private zzja() {
    }

    public static int A(int i8, int i9) {
        return o0(i8 << 3) + 4;
    }

    public static int B(int i8, long j8) {
        return o0(i8 << 3) + j0(j8);
    }

    public static int C(int i8, zzij zzijVar) {
        return (o0(8) << 1) + l0(2, i8) + j(3, zzijVar);
    }

    public static int D(int i8, m9 m9Var) {
        int o02 = o0(i8 << 3);
        int b9 = m9Var.b();
        return o02 + o0(b9) + b9;
    }

    private static int D0(int i8) {
        return (i8 >> 31) ^ (i8 << 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int E(int i8, ia iaVar, xa xaVar) {
        return o0(i8 << 3) + t(iaVar, xaVar);
    }

    public static int F(long j8) {
        return j0(j8);
    }

    public static int G(ia iaVar) {
        int f5 = iaVar.f();
        return o0(f5) + f5;
    }

    public static zzja H(byte[] bArr) {
        return new a(bArr, 0, bArr.length);
    }

    public static int T(int i8) {
        return j0(i8);
    }

    public static int U(int i8, int i9) {
        return o0(i8 << 3) + j0(i9);
    }

    public static int V(int i8, long j8) {
        return o0(i8 << 3) + 8;
    }

    public static int W(long j8) {
        return 8;
    }

    public static int b0(int i8) {
        return 4;
    }

    public static int c(double d8) {
        return 8;
    }

    public static int c0(int i8, int i9) {
        return o0(i8 << 3) + 4;
    }

    public static int d(float f5) {
        return 4;
    }

    public static int d0(int i8, long j8) {
        return o0(i8 << 3) + j0(w0(j8));
    }

    public static int e(int i8) {
        return j0(i8);
    }

    public static int e0(long j8) {
        return j0(w0(j8));
    }

    public static int f(int i8, double d8) {
        return o0(i8 << 3) + 8;
    }

    public static int g(int i8, float f5) {
        return o0(i8 << 3) + 4;
    }

    public static int g0(int i8) {
        return o0(D0(i8));
    }

    public static int h(int i8, int i9) {
        return o0(i8 << 3) + j0(i9);
    }

    public static int h0(int i8, int i9) {
        return o0(i8 << 3) + o0(D0(i9));
    }

    public static int i(int i8, long j8) {
        return o0(i8 << 3) + 8;
    }

    public static int i0(int i8, long j8) {
        return o0(i8 << 3) + j0(j8);
    }

    public static int j(int i8, zzij zzijVar) {
        int o02 = o0(i8 << 3);
        int v8 = zzijVar.v();
        return o02 + o0(v8) + v8;
    }

    public static int j0(long j8) {
        return (640 - (Long.numberOfLeadingZeros(j8) * 9)) >>> 6;
    }

    public static int k(int i8, m9 m9Var) {
        return (o0(8) << 1) + l0(2, i8) + D(3, m9Var);
    }

    public static int k0(int i8) {
        return o0(i8 << 3);
    }

    public static int l(int i8, ia iaVar) {
        return (o0(8) << 1) + l0(2, i8) + o0(24) + G(iaVar);
    }

    public static int l0(int i8, int i9) {
        return o0(i8 << 3) + o0(i9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public static int m(int i8, ia iaVar, xa xaVar) {
        return (o0(i8 << 3) << 1) + ((g7) iaVar).a(xaVar);
    }

    public static int n(int i8, String str) {
        return o0(i8 << 3) + u(str);
    }

    public static int o(int i8, boolean z4) {
        return o0(i8 << 3) + 1;
    }

    public static int o0(int i8) {
        return (352 - (Integer.numberOfLeadingZeros(i8) * 9)) >>> 6;
    }

    public static int p(long j8) {
        return 8;
    }

    public static int q(zzij zzijVar) {
        int v8 = zzijVar.v();
        return o0(v8) + v8;
    }

    public static int r(m9 m9Var) {
        int b9 = m9Var.b();
        return o0(b9) + b9;
    }

    @Deprecated
    public static int s(ia iaVar) {
        return iaVar.f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int t(ia iaVar, xa xaVar) {
        int a9 = ((g7) iaVar).a(xaVar);
        return o0(a9) + a9;
    }

    public static int u(String str) {
        int length;
        try {
            length = dc.a(str);
        } catch (gc unused) {
            length = str.getBytes(a9.f12069b).length;
        }
        return o0(length) + length;
    }

    public static int v(boolean z4) {
        return 1;
    }

    public static int w(byte[] bArr) {
        int length = bArr.length;
        return o0(length) + length;
    }

    private static long w0(long j8) {
        return (j8 >> 63) ^ (j8 << 1);
    }

    public static int z(int i8) {
        return 4;
    }

    public abstract void A0(int i8, int i9);

    public abstract void B0(int i8);

    public abstract void C0(int i8, int i9);

    public final void I() {
        if (b() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }

    public final void J(double d8) {
        n0(Double.doubleToRawLongBits(d8));
    }

    public final void K(float f5) {
        s0(Float.floatToRawIntBits(f5));
    }

    public final void L(int i8, double d8) {
        m0(i8, Double.doubleToRawLongBits(d8));
    }

    public final void M(int i8, float f5) {
        p0(i8, Float.floatToRawIntBits(f5));
    }

    public abstract void N(int i8, ia iaVar);

    public abstract void O(int i8, String str);

    public abstract void P(int i8, boolean z4);

    public abstract void Q(zzij zzijVar);

    public abstract void R(String str);

    public final void S(boolean z4) {
        x(z4 ? (byte) 1 : (byte) 0);
    }

    public abstract void X(int i8, zzij zzijVar);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void Y(int i8, ia iaVar, xa xaVar);

    public abstract void Z(ia iaVar);

    public abstract int b();

    public abstract void f0(int i8, zzij zzijVar);

    public abstract void m0(int i8, long j8);

    public abstract void n0(long j8);

    public abstract void p0(int i8, int i9);

    public final void q0(int i8, long j8) {
        u0(i8, w0(j8));
    }

    public final void r0(long j8) {
        v0(w0(j8));
    }

    public abstract void s0(int i8);

    public abstract void t0(int i8, int i9);

    public abstract void u0(int i8, long j8);

    public abstract void v0(long j8);

    public abstract void x(byte b9);

    public abstract void x0(int i8);

    final void y(String str, gc gcVar) {
        f12856b.logp(Level.WARNING, "com.google.protobuf.CodedOutputStream", "inefficientWriteStringNoTag", "Converting ill-formed UTF-16. Your Protocol Buffer will not round trip correctly!", (Throwable) gcVar);
        byte[] bytes = str.getBytes(a9.f12069b);
        try {
            B0(bytes.length);
            a(bytes, 0, bytes.length);
        } catch (IndexOutOfBoundsException e8) {
            throw new zza(e8);
        }
    }

    public final void y0(int i8, int i9) {
        C0(i8, D0(i9));
    }

    public final void z0(int i8) {
        B0(D0(i8));
    }
}
