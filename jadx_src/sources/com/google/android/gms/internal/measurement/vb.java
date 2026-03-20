package com.google.android.gms.internal.measurement;

import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class vb {

    /* renamed from: f  reason: collision with root package name */
    private static final vb f12590f = new vb(0, new int[0], new Object[0], false);

    /* renamed from: a  reason: collision with root package name */
    private int f12591a;

    /* renamed from: b  reason: collision with root package name */
    private int[] f12592b;

    /* renamed from: c  reason: collision with root package name */
    private Object[] f12593c;

    /* renamed from: d  reason: collision with root package name */
    private int f12594d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f12595e;

    private vb() {
        this(0, new int[8], new Object[8], true);
    }

    private vb(int i8, int[] iArr, Object[] objArr, boolean z4) {
        this.f12594d = -1;
        this.f12591a = i8;
        this.f12592b = iArr;
        this.f12593c = objArr;
        this.f12595e = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static vb c(vb vbVar, vb vbVar2) {
        int i8 = vbVar.f12591a + vbVar2.f12591a;
        int[] copyOf = Arrays.copyOf(vbVar.f12592b, i8);
        System.arraycopy(vbVar2.f12592b, 0, copyOf, vbVar.f12591a, vbVar2.f12591a);
        Object[] copyOf2 = Arrays.copyOf(vbVar.f12593c, i8);
        System.arraycopy(vbVar2.f12593c, 0, copyOf2, vbVar.f12591a, vbVar2.f12591a);
        return new vb(i8, copyOf, copyOf2, true);
    }

    private final void d(int i8) {
        int[] iArr = this.f12592b;
        if (i8 > iArr.length) {
            int i9 = this.f12591a;
            int i10 = i9 + (i9 / 2);
            if (i10 >= i8) {
                i8 = i10;
            }
            if (i8 < 8) {
                i8 = 8;
            }
            this.f12592b = Arrays.copyOf(iArr, i8);
            this.f12593c = Arrays.copyOf(this.f12593c, i8);
        }
    }

    private static void f(int i8, Object obj, rc rcVar) {
        int i9 = i8 >>> 3;
        int i10 = i8 & 7;
        if (i10 == 0) {
            rcVar.i(i9, ((Long) obj).longValue());
        } else if (i10 == 1) {
            rcVar.w(i9, ((Long) obj).longValue());
        } else if (i10 == 2) {
            rcVar.G(i9, (zzij) obj);
        } else if (i10 != 3) {
            if (i10 != 5) {
                throw new RuntimeException(zzkb.a());
            }
            rcVar.k(i9, ((Integer) obj).intValue());
        } else if (rcVar.zza() == qc.f12458a) {
            rcVar.j(i9);
            ((vb) obj).j(rcVar);
            rcVar.c(i9);
        } else {
            rcVar.c(i9);
            ((vb) obj).j(rcVar);
            rcVar.j(i9);
        }
    }

    public static vb k() {
        return f12590f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static vb l() {
        return new vb();
    }

    private final void n() {
        if (!this.f12595e) {
            throw new UnsupportedOperationException();
        }
    }

    public final int a() {
        int i02;
        int i8 = this.f12594d;
        if (i8 != -1) {
            return i8;
        }
        int i9 = 0;
        for (int i10 = 0; i10 < this.f12591a; i10++) {
            int i11 = this.f12592b[i10];
            int i12 = i11 >>> 3;
            int i13 = i11 & 7;
            if (i13 == 0) {
                i02 = zzja.i0(i12, ((Long) this.f12593c[i10]).longValue());
            } else if (i13 == 1) {
                i02 = zzja.i(i12, ((Long) this.f12593c[i10]).longValue());
            } else if (i13 == 2) {
                i02 = zzja.j(i12, (zzij) this.f12593c[i10]);
            } else if (i13 == 3) {
                i02 = (zzja.k0(i12) << 1) + ((vb) this.f12593c[i10]).a();
            } else if (i13 != 5) {
                throw new IllegalStateException(zzkb.a());
            } else {
                i02 = zzja.A(i12, ((Integer) this.f12593c[i10]).intValue());
            }
            i9 += i02;
        }
        this.f12594d = i9;
        return i9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final vb b(vb vbVar) {
        if (vbVar.equals(f12590f)) {
            return this;
        }
        n();
        int i8 = this.f12591a + vbVar.f12591a;
        d(i8);
        System.arraycopy(vbVar.f12592b, 0, this.f12592b, this.f12591a, vbVar.f12591a);
        System.arraycopy(vbVar.f12593c, 0, this.f12593c, this.f12591a, vbVar.f12591a);
        this.f12591a = i8;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void e(int i8, Object obj) {
        n();
        d(this.f12591a + 1);
        int[] iArr = this.f12592b;
        int i9 = this.f12591a;
        iArr[i9] = i8;
        this.f12593c[i9] = obj;
        this.f12591a = i9 + 1;
    }

    public final boolean equals(Object obj) {
        boolean z4;
        boolean z8;
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof vb)) {
            vb vbVar = (vb) obj;
            int i8 = this.f12591a;
            if (i8 == vbVar.f12591a) {
                int[] iArr = this.f12592b;
                int[] iArr2 = vbVar.f12592b;
                int i9 = 0;
                while (true) {
                    if (i9 >= i8) {
                        z4 = true;
                        break;
                    } else if (iArr[i9] != iArr2[i9]) {
                        z4 = false;
                        break;
                    } else {
                        i9++;
                    }
                }
                if (z4) {
                    Object[] objArr = this.f12593c;
                    Object[] objArr2 = vbVar.f12593c;
                    int i10 = this.f12591a;
                    int i11 = 0;
                    while (true) {
                        if (i11 >= i10) {
                            z8 = true;
                            break;
                        } else if (!objArr[i11].equals(objArr2[i11])) {
                            z8 = false;
                            break;
                        } else {
                            i11++;
                        }
                    }
                    if (z8) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void g(rc rcVar) {
        if (rcVar.zza() == qc.f12459b) {
            for (int i8 = this.f12591a - 1; i8 >= 0; i8--) {
                rcVar.r(this.f12592b[i8] >>> 3, this.f12593c[i8]);
            }
            return;
        }
        for (int i9 = 0; i9 < this.f12591a; i9++) {
            rcVar.r(this.f12592b[i9] >>> 3, this.f12593c[i9]);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void h(StringBuilder sb, int i8) {
        for (int i9 = 0; i9 < this.f12591a; i9++) {
            ja.d(sb, i8, String.valueOf(this.f12592b[i9] >>> 3), this.f12593c[i9]);
        }
    }

    public final int hashCode() {
        int i8 = this.f12591a;
        int i9 = (i8 + 527) * 31;
        int[] iArr = this.f12592b;
        int i10 = 17;
        int i11 = 17;
        for (int i12 = 0; i12 < i8; i12++) {
            i11 = (i11 * 31) + iArr[i12];
        }
        int i13 = (i9 + i11) * 31;
        Object[] objArr = this.f12593c;
        int i14 = this.f12591a;
        for (int i15 = 0; i15 < i14; i15++) {
            i10 = (i10 * 31) + objArr[i15].hashCode();
        }
        return i13 + i10;
    }

    public final int i() {
        int i8 = this.f12594d;
        if (i8 != -1) {
            return i8;
        }
        int i9 = 0;
        for (int i10 = 0; i10 < this.f12591a; i10++) {
            i9 += zzja.C(this.f12592b[i10] >>> 3, (zzij) this.f12593c[i10]);
        }
        this.f12594d = i9;
        return i9;
    }

    public final void j(rc rcVar) {
        if (this.f12591a == 0) {
            return;
        }
        if (rcVar.zza() == qc.f12458a) {
            for (int i8 = 0; i8 < this.f12591a; i8++) {
                f(this.f12592b[i8], this.f12593c[i8], rcVar);
            }
            return;
        }
        for (int i9 = this.f12591a - 1; i9 >= 0; i9--) {
            f(this.f12592b[i9], this.f12593c[i9], rcVar);
        }
    }

    public final void m() {
        if (this.f12595e) {
            this.f12595e = false;
        }
    }
}
