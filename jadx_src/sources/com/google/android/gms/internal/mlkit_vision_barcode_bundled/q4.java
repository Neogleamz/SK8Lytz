package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.nio.charset.Charset;
import java.util.Iterator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q4 extends zzdb {

    /* renamed from: k  reason: collision with root package name */
    static final int[] f14838k = {1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169, 63245986, 102334155, 165580141, 267914296, 433494437, 701408733, 1134903170, 1836311903, Integer.MAX_VALUE};

    /* renamed from: e  reason: collision with root package name */
    private final int f14839e;

    /* renamed from: f  reason: collision with root package name */
    private final zzdb f14840f;

    /* renamed from: g  reason: collision with root package name */
    private final zzdb f14841g;

    /* renamed from: h  reason: collision with root package name */
    private final int f14842h;

    /* renamed from: j  reason: collision with root package name */
    private final int f14843j;

    private q4(zzdb zzdbVar, zzdb zzdbVar2) {
        this.f14840f = zzdbVar;
        this.f14841g = zzdbVar2;
        int i8 = zzdbVar.i();
        this.f14842h = i8;
        this.f14839e = i8 + zzdbVar2.i();
        this.f14843j = Math.max(zzdbVar.n(), zzdbVar2.n()) + 1;
    }

    private static zzdb N(zzdb zzdbVar, zzdb zzdbVar2) {
        int i8 = zzdbVar.i();
        int i9 = zzdbVar2.i();
        byte[] bArr = new byte[i8 + i9];
        zzdbVar.L(bArr, 0, 0, i8);
        zzdbVar2.L(bArr, 0, i8, i9);
        return new m1(bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int O(int i8) {
        int[] iArr = f14838k;
        int length = iArr.length;
        if (i8 >= 47) {
            return Integer.MAX_VALUE;
        }
        return iArr[i8];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzdb S(zzdb zzdbVar, zzdb zzdbVar2) {
        if (zzdbVar2.i() == 0) {
            return zzdbVar;
        }
        if (zzdbVar.i() == 0) {
            return zzdbVar2;
        }
        int i8 = zzdbVar.i() + zzdbVar2.i();
        if (i8 < 128) {
            return N(zzdbVar, zzdbVar2);
        }
        if (zzdbVar instanceof q4) {
            q4 q4Var = (q4) zzdbVar;
            if (q4Var.f14841g.i() + zzdbVar2.i() < 128) {
                return new q4(q4Var.f14840f, N(q4Var.f14841g, zzdbVar2));
            } else if (q4Var.f14840f.n() > q4Var.f14841g.n() && q4Var.f14843j > zzdbVar2.n()) {
                return new q4(q4Var.f14840f, new q4(q4Var.f14841g, zzdbVar2));
            }
        }
        return i8 >= O(Math.max(zzdbVar.n(), zzdbVar2.n()) + 1) ? new q4(zzdbVar, zzdbVar2) : m4.a(new m4(null), zzdbVar, zzdbVar2);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final k1 E() {
        return new k4(this);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final byte e(int i8) {
        zzdb.K(i8, this.f14839e);
        return g(i8);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzdb)) {
            return false;
        }
        zzdb zzdbVar = (zzdb) obj;
        if (this.f14839e != zzdbVar.i()) {
            return false;
        }
        if (this.f14839e == 0) {
            return true;
        }
        int D = D();
        int D2 = zzdbVar.D();
        if (D != 0 && D2 != 0 && D != D2) {
            return false;
        }
        o4 o4Var = new o4(this, null);
        l1 next = o4Var.next();
        o4 o4Var2 = new o4(zzdbVar, null);
        l1 next2 = o4Var2.next();
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        while (true) {
            int i11 = next.i() - i8;
            int i12 = next2.i() - i9;
            int min = Math.min(i11, i12);
            if (!(i8 == 0 ? next.N(next2, i9, min) : next2.N(next, i8, min))) {
                return false;
            }
            i10 += min;
            int i13 = this.f14839e;
            if (i10 >= i13) {
                if (i10 == i13) {
                    return true;
                }
                throw new IllegalStateException();
            }
            if (min == i11) {
                next = o4Var.next();
                i8 = 0;
            } else {
                i8 += min;
            }
            if (min == i12) {
                next2 = o4Var2.next();
                i9 = 0;
            } else {
                i9 += min;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final byte g(int i8) {
        int i9 = this.f14842h;
        return i8 < i9 ? this.f14840f.g(i8) : this.f14841g.g(i8 - i9);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final int i() {
        return this.f14839e;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb, java.lang.Iterable
    public final /* synthetic */ Iterator iterator() {
        return new k4(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final void k(byte[] bArr, int i8, int i9, int i10) {
        int i11 = i8 + i10;
        int i12 = this.f14842h;
        if (i11 <= i12) {
            this.f14840f.k(bArr, i8, i9, i10);
        } else if (i8 >= i12) {
            this.f14841g.k(bArr, i8 - i12, i9, i10);
        } else {
            int i13 = i12 - i8;
            this.f14840f.k(bArr, i8, i9, i13);
            this.f14841g.k(bArr, 0, i9 + i13, i10 - i13);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final int n() {
        return this.f14843j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final boolean p() {
        return this.f14839e >= O(this.f14843j);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final int q(int i8, int i9, int i10) {
        int i11 = i9 + i10;
        int i12 = this.f14842h;
        if (i11 <= i12) {
            return this.f14840f.q(i8, i9, i10);
        }
        if (i9 >= i12) {
            return this.f14841g.q(i8, i9 - i12, i10);
        }
        int i13 = i12 - i9;
        return this.f14841g.q(this.f14840f.q(i8, i9, i13), 0, i10 - i13);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final int t(int i8, int i9, int i10) {
        int i11 = i9 + i10;
        int i12 = this.f14842h;
        if (i11 <= i12) {
            return this.f14840f.t(i8, i9, i10);
        }
        if (i9 >= i12) {
            return this.f14841g.t(i8, i9 - i12, i10);
        }
        int i13 = i12 - i9;
        return this.f14841g.t(this.f14840f.t(i8, i9, i13), 0, i10 - i13);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final zzdb u(int i8, int i9) {
        int A = zzdb.A(i8, i9, this.f14839e);
        if (A == 0) {
            return zzdb.f14977b;
        }
        if (A == this.f14839e) {
            return this;
        }
        int i10 = this.f14842h;
        if (i9 <= i10) {
            return this.f14840f.u(i8, i9);
        }
        if (i8 >= i10) {
            return this.f14841g.u(i8 - i10, i9 - i10);
        }
        zzdb zzdbVar = this.f14840f;
        return new q4(zzdbVar.u(i8, zzdbVar.i()), this.f14841g.u(0, i9 - this.f14842h));
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    protected final String v(Charset charset) {
        return new String(M(), charset);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final void x(f1 f1Var) {
        this.f14840f.x(f1Var);
        this.f14841g.x(f1Var);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.zzdb
    public final boolean y() {
        int t8 = this.f14840f.t(0, 0, this.f14842h);
        zzdb zzdbVar = this.f14841g;
        return zzdbVar.t(t8, 0, zzdbVar.i()) == 0;
    }
}
