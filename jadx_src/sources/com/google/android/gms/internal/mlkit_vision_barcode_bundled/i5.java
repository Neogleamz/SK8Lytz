package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i5 {

    /* renamed from: f  reason: collision with root package name */
    private static final i5 f14786f = new i5(0, new int[0], new Object[0], false);

    /* renamed from: a  reason: collision with root package name */
    private int f14787a;

    /* renamed from: b  reason: collision with root package name */
    private int[] f14788b;

    /* renamed from: c  reason: collision with root package name */
    private Object[] f14789c;

    /* renamed from: d  reason: collision with root package name */
    private int f14790d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f14791e;

    private i5() {
        this(0, new int[8], new Object[8], true);
    }

    private i5(int i8, int[] iArr, Object[] objArr, boolean z4) {
        this.f14790d = -1;
        this.f14787a = i8;
        this.f14788b = iArr;
        this.f14789c = objArr;
        this.f14791e = z4;
    }

    public static i5 c() {
        return f14786f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static i5 e(i5 i5Var, i5 i5Var2) {
        int i8 = i5Var.f14787a + i5Var2.f14787a;
        int[] copyOf = Arrays.copyOf(i5Var.f14788b, i8);
        System.arraycopy(i5Var2.f14788b, 0, copyOf, i5Var.f14787a, i5Var2.f14787a);
        Object[] copyOf2 = Arrays.copyOf(i5Var.f14789c, i8);
        System.arraycopy(i5Var2.f14789c, 0, copyOf2, i5Var.f14787a, i5Var2.f14787a);
        return new i5(i8, copyOf, copyOf2, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static i5 f() {
        return new i5(0, new int[8], new Object[8], true);
    }

    private final void m(int i8) {
        int[] iArr = this.f14788b;
        if (i8 > iArr.length) {
            int i9 = this.f14787a;
            int i10 = i9 + (i9 / 2);
            if (i10 >= i8) {
                i8 = i10;
            }
            if (i8 < 8) {
                i8 = 8;
            }
            this.f14788b = Arrays.copyOf(iArr, i8);
            this.f14789c = Arrays.copyOf(this.f14789c, i8);
        }
    }

    public final int a() {
        int B;
        int A;
        int i8;
        int i9 = this.f14790d;
        if (i9 == -1) {
            int i10 = 0;
            for (int i11 = 0; i11 < this.f14787a; i11++) {
                int i12 = this.f14788b[i11];
                int i13 = i12 >>> 3;
                int i14 = i12 & 7;
                if (i14 != 0) {
                    if (i14 == 1) {
                        ((Long) this.f14789c[i11]).longValue();
                        i8 = w1.A(i13 << 3) + 8;
                    } else if (i14 == 2) {
                        int i15 = w1.f14874d;
                        int i16 = ((zzdb) this.f14789c[i11]).i();
                        i8 = w1.A(i13 << 3) + w1.A(i16) + i16;
                    } else if (i14 == 3) {
                        int i17 = i13 << 3;
                        int i18 = w1.f14874d;
                        B = ((i5) this.f14789c[i11]).a();
                        int A2 = w1.A(i17);
                        A = A2 + A2;
                    } else if (i14 != 5) {
                        throw new IllegalStateException(zzeo.a());
                    } else {
                        ((Integer) this.f14789c[i11]).intValue();
                        i8 = w1.A(i13 << 3) + 4;
                    }
                    i10 += i8;
                } else {
                    int i19 = i13 << 3;
                    B = w1.B(((Long) this.f14789c[i11]).longValue());
                    A = w1.A(i19);
                }
                i8 = A + B;
                i10 += i8;
            }
            this.f14790d = i10;
            return i10;
        }
        return i9;
    }

    public final int b() {
        int i8 = this.f14790d;
        if (i8 == -1) {
            int i9 = 0;
            for (int i10 = 0; i10 < this.f14787a; i10++) {
                int i11 = w1.f14874d;
                int i12 = ((zzdb) this.f14789c[i10]).i();
                int A = w1.A(i12) + i12;
                int A2 = w1.A(16);
                int A3 = w1.A(this.f14788b[i10] >>> 3);
                int A4 = w1.A(8);
                i9 += A4 + A4 + A2 + A3 + w1.A(24) + A;
            }
            this.f14790d = i9;
            return i9;
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final i5 d(i5 i5Var) {
        if (i5Var.equals(f14786f)) {
            return this;
        }
        g();
        int i8 = this.f14787a + i5Var.f14787a;
        m(i8);
        System.arraycopy(i5Var.f14788b, 0, this.f14788b, this.f14787a, i5Var.f14787a);
        System.arraycopy(i5Var.f14789c, 0, this.f14789c, this.f14787a, i5Var.f14787a);
        this.f14787a = i8;
        return this;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof i5)) {
            i5 i5Var = (i5) obj;
            int i8 = this.f14787a;
            if (i8 == i5Var.f14787a) {
                int[] iArr = this.f14788b;
                int[] iArr2 = i5Var.f14788b;
                int i9 = 0;
                while (true) {
                    if (i9 >= i8) {
                        Object[] objArr = this.f14789c;
                        Object[] objArr2 = i5Var.f14789c;
                        int i10 = this.f14787a;
                        for (int i11 = 0; i11 < i10; i11++) {
                            if (objArr[i11].equals(objArr2[i11])) {
                            }
                        }
                        return true;
                    } else if (iArr[i9] != iArr2[i9]) {
                        break;
                    } else {
                        i9++;
                    }
                }
            }
            return false;
        }
        return false;
    }

    final void g() {
        if (!this.f14791e) {
            throw new UnsupportedOperationException();
        }
    }

    public final void h() {
        if (this.f14791e) {
            this.f14791e = false;
        }
    }

    public final int hashCode() {
        int i8 = this.f14787a;
        int i9 = i8 + 527;
        int[] iArr = this.f14788b;
        int i10 = 17;
        int i11 = 17;
        for (int i12 = 0; i12 < i8; i12++) {
            i11 = (i11 * 31) + iArr[i12];
        }
        int i13 = (i9 * 31) + i11;
        Object[] objArr = this.f14789c;
        int i14 = this.f14787a;
        for (int i15 = 0; i15 < i14; i15++) {
            i10 = (i10 * 31) + objArr[i15].hashCode();
        }
        return (i13 * 31) + i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void i(StringBuilder sb, int i8) {
        for (int i9 = 0; i9 < this.f14787a; i9++) {
            z3.b(sb, i8, String.valueOf(this.f14788b[i9] >>> 3), this.f14789c[i9]);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void j(int i8, Object obj) {
        g();
        m(this.f14787a + 1);
        int[] iArr = this.f14788b;
        int i9 = this.f14787a;
        iArr[i9] = i8;
        this.f14789c[i9] = obj;
        this.f14787a = i9 + 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void k(y5 y5Var) {
        for (int i8 = 0; i8 < this.f14787a; i8++) {
            y5Var.A(this.f14788b[i8] >>> 3, this.f14789c[i8]);
        }
    }

    public final void l(y5 y5Var) {
        if (this.f14787a != 0) {
            for (int i8 = 0; i8 < this.f14787a; i8++) {
                int i9 = this.f14788b[i8];
                Object obj = this.f14789c[i8];
                int i10 = i9 & 7;
                int i11 = i9 >>> 3;
                if (i10 == 0) {
                    y5Var.m(i11, ((Long) obj).longValue());
                } else if (i10 == 1) {
                    y5Var.F(i11, ((Long) obj).longValue());
                } else if (i10 == 2) {
                    y5Var.h(i11, (zzdb) obj);
                } else if (i10 == 3) {
                    y5Var.K(i11);
                    ((i5) obj).l(y5Var);
                    y5Var.J(i11);
                } else if (i10 != 5) {
                    throw new RuntimeException(zzeo.a());
                } else {
                    y5Var.k(i11, ((Integer) obj).intValue());
                }
            }
        }
    }
}
