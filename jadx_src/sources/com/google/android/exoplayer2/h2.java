package com.google.android.exoplayer2;

import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Pair;
import com.google.android.exoplayer2.g;
import com.google.android.exoplayer2.z0;
import com.google.common.collect.ImmutableList;
import i5.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class h2 implements g {

    /* renamed from: a  reason: collision with root package name */
    public static final h2 f9745a = new a();

    /* renamed from: b  reason: collision with root package name */
    private static final String f9746b = b6.l0.r0(0);

    /* renamed from: c  reason: collision with root package name */
    private static final String f9747c = b6.l0.r0(1);

    /* renamed from: d  reason: collision with root package name */
    private static final String f9748d = b6.l0.r0(2);

    /* renamed from: e  reason: collision with root package name */
    public static final g.a<h2> f9749e = i4.l0.a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends h2 {
        a() {
        }

        @Override // com.google.android.exoplayer2.h2
        public int f(Object obj) {
            return -1;
        }

        @Override // com.google.android.exoplayer2.h2
        public b k(int i8, b bVar, boolean z4) {
            throw new IndexOutOfBoundsException();
        }

        @Override // com.google.android.exoplayer2.h2
        public int m() {
            return 0;
        }

        @Override // com.google.android.exoplayer2.h2
        public Object q(int i8) {
            throw new IndexOutOfBoundsException();
        }

        @Override // com.google.android.exoplayer2.h2
        public d s(int i8, d dVar, long j8) {
            throw new IndexOutOfBoundsException();
        }

        @Override // com.google.android.exoplayer2.h2
        public int t() {
            return 0;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements g {

        /* renamed from: h  reason: collision with root package name */
        private static final String f9750h = b6.l0.r0(0);

        /* renamed from: j  reason: collision with root package name */
        private static final String f9751j = b6.l0.r0(1);

        /* renamed from: k  reason: collision with root package name */
        private static final String f9752k = b6.l0.r0(2);

        /* renamed from: l  reason: collision with root package name */
        private static final String f9753l = b6.l0.r0(3);

        /* renamed from: m  reason: collision with root package name */
        private static final String f9754m = b6.l0.r0(4);

        /* renamed from: n  reason: collision with root package name */
        public static final g.a<b> f9755n = i4.m0.a;

        /* renamed from: a  reason: collision with root package name */
        public Object f9756a;

        /* renamed from: b  reason: collision with root package name */
        public Object f9757b;

        /* renamed from: c  reason: collision with root package name */
        public int f9758c;

        /* renamed from: d  reason: collision with root package name */
        public long f9759d;

        /* renamed from: e  reason: collision with root package name */
        public long f9760e;

        /* renamed from: f  reason: collision with root package name */
        public boolean f9761f;

        /* renamed from: g  reason: collision with root package name */
        private i5.c f9762g = i5.c.f20513g;

        /* JADX INFO: Access modifiers changed from: private */
        public static b c(Bundle bundle) {
            int i8 = bundle.getInt(f9750h, 0);
            long j8 = bundle.getLong(f9751j, -9223372036854775807L);
            long j9 = bundle.getLong(f9752k, 0L);
            boolean z4 = bundle.getBoolean(f9753l, false);
            Bundle bundle2 = bundle.getBundle(f9754m);
            i5.c a9 = bundle2 != null ? i5.c.f20519n.a(bundle2) : i5.c.f20513g;
            b bVar = new b();
            bVar.v(null, null, i8, j8, j9, a9, z4);
            return bVar;
        }

        public int d(int i8) {
            return this.f9762g.c(i8).f20535b;
        }

        public long e(int i8, int i9) {
            c.a c9 = this.f9762g.c(i8);
            if (c9.f20535b != -1) {
                return c9.f20539f[i9];
            }
            return -9223372036854775807L;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !b.class.equals(obj.getClass())) {
                return false;
            }
            b bVar = (b) obj;
            return b6.l0.c(this.f9756a, bVar.f9756a) && b6.l0.c(this.f9757b, bVar.f9757b) && this.f9758c == bVar.f9758c && this.f9759d == bVar.f9759d && this.f9760e == bVar.f9760e && this.f9761f == bVar.f9761f && b6.l0.c(this.f9762g, bVar.f9762g);
        }

        public int f() {
            return this.f9762g.f20521b;
        }

        public int g(long j8) {
            return this.f9762g.d(j8, this.f9759d);
        }

        public int h(long j8) {
            return this.f9762g.e(j8, this.f9759d);
        }

        public int hashCode() {
            Object obj = this.f9756a;
            int hashCode = (217 + (obj == null ? 0 : obj.hashCode())) * 31;
            Object obj2 = this.f9757b;
            int hashCode2 = obj2 != null ? obj2.hashCode() : 0;
            long j8 = this.f9759d;
            long j9 = this.f9760e;
            return ((((((((((hashCode + hashCode2) * 31) + this.f9758c) * 31) + ((int) (j8 ^ (j8 >>> 32)))) * 31) + ((int) (j9 ^ (j9 >>> 32)))) * 31) + (this.f9761f ? 1 : 0)) * 31) + this.f9762g.hashCode();
        }

        public long i(int i8) {
            return this.f9762g.c(i8).f20534a;
        }

        public long j() {
            return this.f9762g.f20522c;
        }

        public int k(int i8, int i9) {
            c.a c9 = this.f9762g.c(i8);
            if (c9.f20535b != -1) {
                return c9.f20538e[i9];
            }
            return 0;
        }

        public long l(int i8) {
            return this.f9762g.c(i8).f20540g;
        }

        public long m() {
            return this.f9759d;
        }

        public int n(int i8) {
            return this.f9762g.c(i8).e();
        }

        public int o(int i8, int i9) {
            return this.f9762g.c(i8).f(i9);
        }

        public long p() {
            return b6.l0.a1(this.f9760e);
        }

        public long q() {
            return this.f9760e;
        }

        public int r() {
            return this.f9762g.f20524e;
        }

        public boolean s(int i8) {
            return !this.f9762g.c(i8).g();
        }

        public boolean t(int i8) {
            return this.f9762g.c(i8).f20541h;
        }

        public b u(Object obj, Object obj2, int i8, long j8, long j9) {
            return v(obj, obj2, i8, j8, j9, i5.c.f20513g, false);
        }

        public b v(Object obj, Object obj2, int i8, long j8, long j9, i5.c cVar, boolean z4) {
            this.f9756a = obj;
            this.f9757b = obj2;
            this.f9758c = i8;
            this.f9759d = j8;
            this.f9760e = j9;
            this.f9762g = cVar;
            this.f9761f = z4;
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c extends h2 {

        /* renamed from: f  reason: collision with root package name */
        private final ImmutableList<d> f9763f;

        /* renamed from: g  reason: collision with root package name */
        private final ImmutableList<b> f9764g;

        /* renamed from: h  reason: collision with root package name */
        private final int[] f9765h;

        /* renamed from: j  reason: collision with root package name */
        private final int[] f9766j;

        public c(ImmutableList<d> immutableList, ImmutableList<b> immutableList2, int[] iArr) {
            b6.a.a(immutableList.size() == iArr.length);
            this.f9763f = immutableList;
            this.f9764g = immutableList2;
            this.f9765h = iArr;
            this.f9766j = new int[iArr.length];
            for (int i8 = 0; i8 < iArr.length; i8++) {
                this.f9766j[iArr[i8]] = i8;
            }
        }

        @Override // com.google.android.exoplayer2.h2
        public int e(boolean z4) {
            if (u()) {
                return -1;
            }
            if (z4) {
                return this.f9765h[0];
            }
            return 0;
        }

        @Override // com.google.android.exoplayer2.h2
        public int f(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.android.exoplayer2.h2
        public int g(boolean z4) {
            if (u()) {
                return -1;
            }
            return z4 ? this.f9765h[t() - 1] : t() - 1;
        }

        @Override // com.google.android.exoplayer2.h2
        public int i(int i8, int i9, boolean z4) {
            if (i9 == 1) {
                return i8;
            }
            if (i8 != g(z4)) {
                return z4 ? this.f9765h[this.f9766j[i8] + 1] : i8 + 1;
            } else if (i9 == 2) {
                return e(z4);
            } else {
                return -1;
            }
        }

        @Override // com.google.android.exoplayer2.h2
        public b k(int i8, b bVar, boolean z4) {
            b bVar2 = this.f9764g.get(i8);
            bVar.v(bVar2.f9756a, bVar2.f9757b, bVar2.f9758c, bVar2.f9759d, bVar2.f9760e, bVar2.f9762g, bVar2.f9761f);
            return bVar;
        }

        @Override // com.google.android.exoplayer2.h2
        public int m() {
            return this.f9764g.size();
        }

        @Override // com.google.android.exoplayer2.h2
        public int p(int i8, int i9, boolean z4) {
            if (i9 == 1) {
                return i8;
            }
            if (i8 != e(z4)) {
                return z4 ? this.f9765h[this.f9766j[i8] - 1] : i8 - 1;
            } else if (i9 == 2) {
                return g(z4);
            } else {
                return -1;
            }
        }

        @Override // com.google.android.exoplayer2.h2
        public Object q(int i8) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.android.exoplayer2.h2
        public d s(int i8, d dVar, long j8) {
            d dVar2 = this.f9763f.get(i8);
            dVar.i(dVar2.f9770a, dVar2.f9772c, dVar2.f9773d, dVar2.f9774e, dVar2.f9775f, dVar2.f9776g, dVar2.f9777h, dVar2.f9778j, dVar2.f9780l, dVar2.f9782n, dVar2.f9783p, dVar2.q, dVar2.f9784t, dVar2.f9785w);
            dVar.f9781m = dVar2.f9781m;
            return dVar;
        }

        @Override // com.google.android.exoplayer2.h2
        public int t() {
            return this.f9763f.size();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d implements g {
        @Deprecated

        /* renamed from: b  reason: collision with root package name */
        public Object f9771b;

        /* renamed from: d  reason: collision with root package name */
        public Object f9773d;

        /* renamed from: e  reason: collision with root package name */
        public long f9774e;

        /* renamed from: f  reason: collision with root package name */
        public long f9775f;

        /* renamed from: g  reason: collision with root package name */
        public long f9776g;

        /* renamed from: h  reason: collision with root package name */
        public boolean f9777h;

        /* renamed from: j  reason: collision with root package name */
        public boolean f9778j;
        @Deprecated

        /* renamed from: k  reason: collision with root package name */
        public boolean f9779k;

        /* renamed from: l  reason: collision with root package name */
        public z0.g f9780l;

        /* renamed from: m  reason: collision with root package name */
        public boolean f9781m;

        /* renamed from: n  reason: collision with root package name */
        public long f9782n;

        /* renamed from: p  reason: collision with root package name */
        public long f9783p;
        public int q;

        /* renamed from: t  reason: collision with root package name */
        public int f9784t;

        /* renamed from: w  reason: collision with root package name */
        public long f9785w;

        /* renamed from: x  reason: collision with root package name */
        public static final Object f9767x = new Object();

        /* renamed from: y  reason: collision with root package name */
        private static final Object f9768y = new Object();

        /* renamed from: z  reason: collision with root package name */
        private static final z0 f9769z = new z0.c().c("com.google.android.exoplayer2.Timeline").f(Uri.EMPTY).a();
        private static final String A = b6.l0.r0(1);
        private static final String B = b6.l0.r0(2);
        private static final String C = b6.l0.r0(3);
        private static final String E = b6.l0.r0(4);
        private static final String F = b6.l0.r0(5);
        private static final String G = b6.l0.r0(6);
        private static final String H = b6.l0.r0(7);
        private static final String K = b6.l0.r0(8);
        private static final String L = b6.l0.r0(9);
        private static final String O = b6.l0.r0(10);
        private static final String P = b6.l0.r0(11);
        private static final String Q = b6.l0.r0(12);
        private static final String R = b6.l0.r0(13);
        public static final g.a<d> T = i4.n0.a;

        /* renamed from: a  reason: collision with root package name */
        public Object f9770a = f9767x;

        /* renamed from: c  reason: collision with root package name */
        public z0 f9772c = f9769z;

        /* JADX INFO: Access modifiers changed from: private */
        public static d b(Bundle bundle) {
            Bundle bundle2 = bundle.getBundle(A);
            z0 a9 = bundle2 != null ? z0.q.a(bundle2) : z0.f11297j;
            long j8 = bundle.getLong(B, -9223372036854775807L);
            long j9 = bundle.getLong(C, -9223372036854775807L);
            long j10 = bundle.getLong(E, -9223372036854775807L);
            boolean z4 = bundle.getBoolean(F, false);
            boolean z8 = bundle.getBoolean(G, false);
            Bundle bundle3 = bundle.getBundle(H);
            z0.g a10 = bundle3 != null ? z0.g.f11367m.a(bundle3) : null;
            boolean z9 = bundle.getBoolean(K, false);
            long j11 = bundle.getLong(L, 0L);
            long j12 = bundle.getLong(O, -9223372036854775807L);
            int i8 = bundle.getInt(P, 0);
            int i9 = bundle.getInt(Q, 0);
            long j13 = bundle.getLong(R, 0L);
            d dVar = new d();
            dVar.i(f9768y, a9, null, j8, j9, j10, z4, z8, a10, j11, j12, i8, i9, j13);
            dVar.f9781m = z9;
            return dVar;
        }

        public long c() {
            return b6.l0.a0(this.f9776g);
        }

        public long d() {
            return b6.l0.a1(this.f9782n);
        }

        public long e() {
            return this.f9782n;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !d.class.equals(obj.getClass())) {
                return false;
            }
            d dVar = (d) obj;
            return b6.l0.c(this.f9770a, dVar.f9770a) && b6.l0.c(this.f9772c, dVar.f9772c) && b6.l0.c(this.f9773d, dVar.f9773d) && b6.l0.c(this.f9780l, dVar.f9780l) && this.f9774e == dVar.f9774e && this.f9775f == dVar.f9775f && this.f9776g == dVar.f9776g && this.f9777h == dVar.f9777h && this.f9778j == dVar.f9778j && this.f9781m == dVar.f9781m && this.f9782n == dVar.f9782n && this.f9783p == dVar.f9783p && this.q == dVar.q && this.f9784t == dVar.f9784t && this.f9785w == dVar.f9785w;
        }

        public long f() {
            return b6.l0.a1(this.f9783p);
        }

        public long g() {
            return this.f9785w;
        }

        public boolean h() {
            b6.a.f(this.f9779k == (this.f9780l != null));
            return this.f9780l != null;
        }

        public int hashCode() {
            int hashCode = (((217 + this.f9770a.hashCode()) * 31) + this.f9772c.hashCode()) * 31;
            Object obj = this.f9773d;
            int hashCode2 = (hashCode + (obj == null ? 0 : obj.hashCode())) * 31;
            z0.g gVar = this.f9780l;
            int hashCode3 = gVar != null ? gVar.hashCode() : 0;
            long j8 = this.f9774e;
            long j9 = this.f9775f;
            long j10 = this.f9776g;
            long j11 = this.f9782n;
            long j12 = this.f9783p;
            long j13 = this.f9785w;
            return ((((((((((((((((((((((hashCode2 + hashCode3) * 31) + ((int) (j8 ^ (j8 >>> 32)))) * 31) + ((int) (j9 ^ (j9 >>> 32)))) * 31) + ((int) (j10 ^ (j10 >>> 32)))) * 31) + (this.f9777h ? 1 : 0)) * 31) + (this.f9778j ? 1 : 0)) * 31) + (this.f9781m ? 1 : 0)) * 31) + ((int) (j11 ^ (j11 >>> 32)))) * 31) + ((int) (j12 ^ (j12 >>> 32)))) * 31) + this.q) * 31) + this.f9784t) * 31) + ((int) (j13 ^ (j13 >>> 32)));
        }

        public d i(Object obj, z0 z0Var, Object obj2, long j8, long j9, long j10, boolean z4, boolean z8, z0.g gVar, long j11, long j12, int i8, int i9, long j13) {
            z0.h hVar;
            this.f9770a = obj;
            this.f9772c = z0Var != null ? z0Var : f9769z;
            this.f9771b = (z0Var == null || (hVar = z0Var.f11304b) == null) ? null : hVar.f11386i;
            this.f9773d = obj2;
            this.f9774e = j8;
            this.f9775f = j9;
            this.f9776g = j10;
            this.f9777h = z4;
            this.f9778j = z8;
            this.f9779k = gVar != null;
            this.f9780l = gVar;
            this.f9782n = j11;
            this.f9783p = j12;
            this.q = i8;
            this.f9784t = i9;
            this.f9785w = j13;
            this.f9781m = false;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static h2 b(Bundle bundle) {
        ImmutableList c9 = c(d.T, b6.b.a(bundle, f9746b));
        ImmutableList c10 = c(b.f9755n, b6.b.a(bundle, f9747c));
        int[] intArray = bundle.getIntArray(f9748d);
        if (intArray == null) {
            intArray = d(c9.size());
        }
        return new c(c9, c10, intArray);
    }

    private static <T extends g> ImmutableList<T> c(g.a<T> aVar, IBinder iBinder) {
        if (iBinder == null) {
            return ImmutableList.E();
        }
        ImmutableList.a aVar2 = new ImmutableList.a();
        ImmutableList<Bundle> a9 = i4.a.a(iBinder);
        for (int i8 = 0; i8 < a9.size(); i8++) {
            aVar2.a(aVar.a(a9.get(i8)));
        }
        return aVar2.k();
    }

    private static int[] d(int i8) {
        int[] iArr = new int[i8];
        for (int i9 = 0; i9 < i8; i9++) {
            iArr[i9] = i9;
        }
        return iArr;
    }

    public int e(boolean z4) {
        return u() ? -1 : 0;
    }

    public boolean equals(Object obj) {
        int g8;
        if (this == obj) {
            return true;
        }
        if (obj instanceof h2) {
            h2 h2Var = (h2) obj;
            if (h2Var.t() == t() && h2Var.m() == m()) {
                d dVar = new d();
                b bVar = new b();
                d dVar2 = new d();
                b bVar2 = new b();
                for (int i8 = 0; i8 < t(); i8++) {
                    if (!r(i8, dVar).equals(h2Var.r(i8, dVar2))) {
                        return false;
                    }
                }
                for (int i9 = 0; i9 < m(); i9++) {
                    if (!k(i9, bVar, true).equals(h2Var.k(i9, bVar2, true))) {
                        return false;
                    }
                }
                int e8 = e(true);
                if (e8 == h2Var.e(true) && (g8 = g(true)) == h2Var.g(true)) {
                    while (e8 != g8) {
                        int i10 = i(e8, 0, true);
                        if (i10 != h2Var.i(e8, 0, true)) {
                            return false;
                        }
                        e8 = i10;
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public abstract int f(Object obj);

    public int g(boolean z4) {
        if (u()) {
            return -1;
        }
        return t() - 1;
    }

    public final int h(int i8, b bVar, d dVar, int i9, boolean z4) {
        int i10 = j(i8, bVar).f9758c;
        if (r(i10, dVar).f9784t == i8) {
            int i11 = i(i10, i9, z4);
            if (i11 == -1) {
                return -1;
            }
            return r(i11, dVar).q;
        }
        return i8 + 1;
    }

    public int hashCode() {
        int i8;
        d dVar = new d();
        b bVar = new b();
        int t8 = 217 + t();
        int i9 = 0;
        while (true) {
            i8 = t8 * 31;
            if (i9 >= t()) {
                break;
            }
            t8 = i8 + r(i9, dVar).hashCode();
            i9++;
        }
        int m8 = i8 + m();
        for (int i10 = 0; i10 < m(); i10++) {
            m8 = (m8 * 31) + k(i10, bVar, true).hashCode();
        }
        int e8 = e(true);
        while (e8 != -1) {
            m8 = (m8 * 31) + e8;
            e8 = i(e8, 0, true);
        }
        return m8;
    }

    public int i(int i8, int i9, boolean z4) {
        if (i9 == 0) {
            if (i8 == g(z4)) {
                return -1;
            }
            return i8 + 1;
        } else if (i9 != 1) {
            if (i9 == 2) {
                return i8 == g(z4) ? e(z4) : i8 + 1;
            }
            throw new IllegalStateException();
        } else {
            return i8;
        }
    }

    public final b j(int i8, b bVar) {
        return k(i8, bVar, false);
    }

    public abstract b k(int i8, b bVar, boolean z4);

    public b l(Object obj, b bVar) {
        return k(f(obj), bVar, true);
    }

    public abstract int m();

    public final Pair<Object, Long> n(d dVar, b bVar, int i8, long j8) {
        return (Pair) b6.a.e(o(dVar, bVar, i8, j8, 0L));
    }

    public final Pair<Object, Long> o(d dVar, b bVar, int i8, long j8, long j9) {
        b6.a.c(i8, 0, t());
        s(i8, dVar, j9);
        if (j8 == -9223372036854775807L) {
            j8 = dVar.e();
            if (j8 == -9223372036854775807L) {
                return null;
            }
        }
        int i9 = dVar.q;
        j(i9, bVar);
        while (i9 < dVar.f9784t && bVar.f9760e != j8) {
            int i10 = i9 + 1;
            if (j(i10, bVar).f9760e > j8) {
                break;
            }
            i9 = i10;
        }
        k(i9, bVar, true);
        long j10 = j8 - bVar.f9760e;
        long j11 = bVar.f9759d;
        if (j11 != -9223372036854775807L) {
            j10 = Math.min(j10, j11 - 1);
        }
        return Pair.create(b6.a.e(bVar.f9757b), Long.valueOf(Math.max(0L, j10)));
    }

    public int p(int i8, int i9, boolean z4) {
        if (i9 == 0) {
            if (i8 == e(z4)) {
                return -1;
            }
            return i8 - 1;
        } else if (i9 != 1) {
            if (i9 == 2) {
                return i8 == e(z4) ? g(z4) : i8 - 1;
            }
            throw new IllegalStateException();
        } else {
            return i8;
        }
    }

    public abstract Object q(int i8);

    public final d r(int i8, d dVar) {
        return s(i8, dVar, 0L);
    }

    public abstract d s(int i8, d dVar, long j8);

    public abstract int t();

    public final boolean u() {
        return t() == 0;
    }

    public final boolean v(int i8, b bVar, d dVar, int i9, boolean z4) {
        return h(i8, bVar, dVar, i9, z4) == -1;
    }
}
