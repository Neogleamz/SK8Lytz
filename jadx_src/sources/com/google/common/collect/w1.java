package com.google.common.collect;

import com.google.common.collect.r1;
import com.google.common.collect.s1;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class w1<K> {

    /* renamed from: a  reason: collision with root package name */
    transient Object[] f19490a;

    /* renamed from: b  reason: collision with root package name */
    transient int[] f19491b;

    /* renamed from: c  reason: collision with root package name */
    transient int f19492c;

    /* renamed from: d  reason: collision with root package name */
    transient int f19493d;

    /* renamed from: e  reason: collision with root package name */
    private transient int[] f19494e;

    /* renamed from: f  reason: collision with root package name */
    transient long[] f19495f;

    /* renamed from: g  reason: collision with root package name */
    private transient float f19496g;

    /* renamed from: h  reason: collision with root package name */
    private transient int f19497h;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends s1.b<K> {

        /* renamed from: a  reason: collision with root package name */
        final K f19498a;

        /* renamed from: b  reason: collision with root package name */
        int f19499b;

        a(int i8) {
            this.f19498a = (K) w1.this.f19490a[i8];
            this.f19499b = i8;
        }

        @Override // com.google.common.collect.r1.a
        public K a() {
            return this.f19498a;
        }

        void b() {
            int i8 = this.f19499b;
            if (i8 == -1 || i8 >= w1.this.z() || !com.google.common.base.k.a(this.f19498a, w1.this.f19490a[this.f19499b])) {
                this.f19499b = w1.this.j(this.f19498a);
            }
        }

        @Override // com.google.common.collect.r1.a
        public int getCount() {
            b();
            int i8 = this.f19499b;
            if (i8 == -1) {
                return 0;
            }
            return w1.this.f19491b[i8];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public w1(int i8) {
        this(i8, 1.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public w1(int i8, float f5) {
        k(i8, f5);
    }

    private static long A(long j8, int i8) {
        return (j8 & (-4294967296L)) | (i8 & 4294967295L);
    }

    private static int e(long j8) {
        return (int) (j8 >>> 32);
    }

    private static int g(long j8) {
        return (int) j8;
    }

    private int i() {
        return this.f19494e.length - 1;
    }

    private static long[] n(int i8) {
        long[] jArr = new long[i8];
        Arrays.fill(jArr, -1L);
        return jArr;
    }

    private static int[] o(int i8) {
        int[] iArr = new int[i8];
        Arrays.fill(iArr, -1);
        return iArr;
    }

    private int t(Object obj, int i8) {
        int i9 = i() & i8;
        int i10 = this.f19494e[i9];
        if (i10 == -1) {
            return 0;
        }
        int i11 = -1;
        while (true) {
            if (e(this.f19495f[i10]) == i8 && com.google.common.base.k.a(obj, this.f19490a[i10])) {
                int i12 = this.f19491b[i10];
                if (i11 == -1) {
                    this.f19494e[i9] = g(this.f19495f[i10]);
                } else {
                    long[] jArr = this.f19495f;
                    jArr[i11] = A(jArr[i11], g(jArr[i10]));
                }
                m(i10);
                this.f19492c--;
                this.f19493d++;
                return i12;
            }
            int g8 = g(this.f19495f[i10]);
            if (g8 == -1) {
                return 0;
            }
            i11 = i10;
            i10 = g8;
        }
    }

    private void w(int i8) {
        int length = this.f19495f.length;
        if (i8 > length) {
            int max = Math.max(1, length >>> 1) + length;
            if (max < 0) {
                max = Integer.MAX_VALUE;
            }
            if (max != length) {
                v(max);
            }
        }
    }

    private void x(int i8) {
        if (this.f19494e.length >= 1073741824) {
            this.f19497h = Integer.MAX_VALUE;
            return;
        }
        int i9 = ((int) (i8 * this.f19496g)) + 1;
        int[] o5 = o(i8);
        long[] jArr = this.f19495f;
        int length = o5.length - 1;
        for (int i10 = 0; i10 < this.f19492c; i10++) {
            int e8 = e(jArr[i10]);
            int i11 = e8 & length;
            int i12 = o5[i11];
            o5[i11] = i10;
            jArr[i10] = (e8 << 32) | (i12 & 4294967295L);
        }
        this.f19497h = i9;
        this.f19494e = o5;
    }

    public void a() {
        this.f19493d++;
        Arrays.fill(this.f19490a, 0, this.f19492c, (Object) null);
        Arrays.fill(this.f19491b, 0, this.f19492c, 0);
        Arrays.fill(this.f19494e, -1);
        Arrays.fill(this.f19495f, -1L);
        this.f19492c = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b() {
        return this.f19492c == 0 ? -1 : 0;
    }

    public int c(Object obj) {
        int j8 = j(obj);
        if (j8 == -1) {
            return 0;
        }
        return this.f19491b[j8];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public r1.a<K> d(int i8) {
        com.google.common.base.l.l(i8, this.f19492c);
        return new a(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public K f(int i8) {
        com.google.common.base.l.l(i8, this.f19492c);
        return (K) this.f19490a[i8];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int h(int i8) {
        com.google.common.base.l.l(i8, this.f19492c);
        return this.f19491b[i8];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int j(Object obj) {
        int d8 = v0.d(obj);
        int i8 = this.f19494e[i() & d8];
        while (i8 != -1) {
            long j8 = this.f19495f[i8];
            if (e(j8) == d8 && com.google.common.base.k.a(obj, this.f19490a[i8])) {
                return i8;
            }
            i8 = g(j8);
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(int i8, float f5) {
        com.google.common.base.l.e(i8 >= 0, "Initial capacity must be non-negative");
        com.google.common.base.l.e(f5 > 0.0f, "Illegal load factor");
        int a9 = v0.a(i8, f5);
        this.f19494e = o(a9);
        this.f19496g = f5;
        this.f19490a = new Object[i8];
        this.f19491b = new int[i8];
        this.f19495f = n(i8);
        this.f19497h = Math.max(1, (int) (a9 * f5));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(int i8, K k8, int i9, int i10) {
        this.f19495f[i8] = (i10 << 32) | 4294967295L;
        this.f19490a[i8] = k8;
        this.f19491b[i8] = i9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(int i8) {
        int z4 = z() - 1;
        if (i8 >= z4) {
            this.f19490a[i8] = null;
            this.f19491b[i8] = 0;
            this.f19495f[i8] = -1;
            return;
        }
        Object[] objArr = this.f19490a;
        objArr[i8] = objArr[z4];
        int[] iArr = this.f19491b;
        iArr[i8] = iArr[z4];
        objArr[z4] = null;
        iArr[z4] = 0;
        long[] jArr = this.f19495f;
        long j8 = jArr[z4];
        jArr[i8] = j8;
        jArr[z4] = -1;
        int e8 = e(j8) & i();
        int[] iArr2 = this.f19494e;
        int i9 = iArr2[e8];
        if (i9 == z4) {
            iArr2[e8] = i8;
            return;
        }
        while (true) {
            long j9 = this.f19495f[i9];
            int g8 = g(j9);
            if (g8 == z4) {
                this.f19495f[i9] = A(j9, i8);
                return;
            }
            i9 = g8;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int p(int i8) {
        int i9 = i8 + 1;
        if (i9 < this.f19492c) {
            return i9;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int q(int i8, int i9) {
        return i8 - 1;
    }

    public int r(K k8, int i8) {
        t.c(i8, "count");
        long[] jArr = this.f19495f;
        Object[] objArr = this.f19490a;
        int[] iArr = this.f19491b;
        int d8 = v0.d(k8);
        int i9 = i() & d8;
        int i10 = this.f19492c;
        int[] iArr2 = this.f19494e;
        int i11 = iArr2[i9];
        if (i11 == -1) {
            iArr2[i9] = i10;
        } else {
            while (true) {
                long j8 = jArr[i11];
                if (e(j8) == d8 && com.google.common.base.k.a(k8, objArr[i11])) {
                    int i12 = iArr[i11];
                    iArr[i11] = i8;
                    return i12;
                }
                int g8 = g(j8);
                if (g8 == -1) {
                    jArr[i11] = A(j8, i10);
                    break;
                }
                i11 = g8;
            }
        }
        if (i10 != Integer.MAX_VALUE) {
            int i13 = i10 + 1;
            w(i13);
            l(i10, k8, i8, d8);
            this.f19492c = i13;
            if (i10 >= this.f19497h) {
                x(this.f19494e.length * 2);
            }
            this.f19493d++;
            return 0;
        }
        throw new IllegalStateException("Cannot contain more than Integer.MAX_VALUE elements!");
    }

    public int s(Object obj) {
        return t(obj, v0.d(obj));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int u(int i8) {
        return t(this.f19490a[i8], e(this.f19495f[i8]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v(int i8) {
        this.f19490a = Arrays.copyOf(this.f19490a, i8);
        this.f19491b = Arrays.copyOf(this.f19491b, i8);
        long[] jArr = this.f19495f;
        int length = jArr.length;
        long[] copyOf = Arrays.copyOf(jArr, i8);
        if (i8 > length) {
            Arrays.fill(copyOf, length, i8, -1L);
        }
        this.f19495f = copyOf;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y(int i8, int i9) {
        com.google.common.base.l.l(i8, this.f19492c);
        this.f19491b[i8] = i9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int z() {
        return this.f19492c;
    }
}
