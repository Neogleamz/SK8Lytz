package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.logging.Level;
import java.util.logging.Logger;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class w1 extends f1 {

    /* renamed from: b  reason: collision with root package name */
    private static final Logger f14872b = Logger.getLogger(w1.class.getName());

    /* renamed from: c  reason: collision with root package name */
    private static final boolean f14873c = s5.C();

    /* renamed from: d  reason: collision with root package name */
    public static final /* synthetic */ int f14874d = 0;

    /* renamed from: a  reason: collision with root package name */
    x1 f14875a;

    private w1() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ w1(v1 v1Var) {
    }

    public static int A(int i8) {
        if ((i8 & (-128)) == 0) {
            return 1;
        }
        if ((i8 & (-16384)) == 0) {
            return 2;
        }
        if (((-2097152) & i8) == 0) {
            return 3;
        }
        return (i8 & (-268435456)) == 0 ? 4 : 5;
    }

    public static int B(long j8) {
        int i8;
        if (((-128) & j8) == 0) {
            return 1;
        }
        if (j8 < 0) {
            return 10;
        }
        if (((-34359738368L) & j8) != 0) {
            i8 = 6;
            j8 >>>= 28;
        } else {
            i8 = 2;
        }
        if (((-2097152) & j8) != 0) {
            j8 >>>= 14;
            i8 += 2;
        }
        return (j8 & (-16384)) != 0 ? i8 + 1 : i8;
    }

    public static w1 a(byte[] bArr, int i8, int i9) {
        return new u1(bArr, 0, i9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public static int v(int i8, x3 x3Var, r4 r4Var) {
        int g8 = ((y0) x3Var).g(r4Var);
        int A = A(i8 << 3);
        return A + A + g8;
    }

    public static int w(int i8) {
        if (i8 >= 0) {
            return A(i8);
        }
        return 10;
    }

    public static int x(x3 x3Var) {
        int b9 = x3Var.b();
        return A(b9) + b9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int y(x3 x3Var, r4 r4Var) {
        int g8 = ((y0) x3Var).g(r4Var);
        return A(g8) + g8;
    }

    public static int z(String str) {
        int length;
        try {
            length = x5.e(str);
        } catch (w5 unused) {
            length = str.getBytes(y2.f14886b).length;
        }
        return A(length) + length;
    }

    public final void b() {
        if (e() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void c(String str, w5 w5Var) {
        f14872b.logp(Level.WARNING, "com.google.protobuf.CodedOutputStream", "inefficientWriteStringNoTag", "Converting ill-formed UTF-16. Your Protocol Buffer will not round trip correctly!", (Throwable) w5Var);
        byte[] bytes = str.getBytes(y2.f14886b);
        try {
            int length = bytes.length;
            s(length);
            o(bytes, 0, length);
        } catch (IndexOutOfBoundsException e8) {
            throw new zzdh(e8);
        }
    }

    public abstract int e();

    public abstract void f(byte b9);

    public abstract void g(int i8, boolean z4);

    public abstract void h(int i8, zzdb zzdbVar);

    public abstract void i(int i8, int i9);

    public abstract void j(int i8);

    public abstract void k(int i8, long j8);

    public abstract void l(long j8);

    public abstract void m(int i8, int i9);

    public abstract void n(int i8);

    public abstract void o(byte[] bArr, int i8, int i9);

    public abstract void p(int i8, String str);

    public abstract void q(int i8, int i9);

    public abstract void r(int i8, int i9);

    public abstract void s(int i8);

    public abstract void t(int i8, long j8);

    public abstract void u(long j8);
}
