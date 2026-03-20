package com.google.android.exoplayer2;

import android.util.Pair;
import com.google.android.exoplayer2.h2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a extends h2 {

    /* renamed from: f  reason: collision with root package name */
    private final int f9151f;

    /* renamed from: g  reason: collision with root package name */
    private final com.google.android.exoplayer2.source.x f9152g;

    /* renamed from: h  reason: collision with root package name */
    private final boolean f9153h;

    public a(boolean z4, com.google.android.exoplayer2.source.x xVar) {
        this.f9153h = z4;
        this.f9152g = xVar;
        this.f9151f = xVar.b();
    }

    public static Object A(Object obj) {
        return ((Pair) obj).first;
    }

    public static Object C(Object obj, Object obj2) {
        return Pair.create(obj, obj2);
    }

    private int F(int i8, boolean z4) {
        if (z4) {
            return this.f9152g.f(i8);
        }
        if (i8 < this.f9151f - 1) {
            return i8 + 1;
        }
        return -1;
    }

    private int G(int i8, boolean z4) {
        if (z4) {
            return this.f9152g.e(i8);
        }
        if (i8 > 0) {
            return i8 - 1;
        }
        return -1;
    }

    public static Object z(Object obj) {
        return ((Pair) obj).second;
    }

    protected abstract Object B(int i8);

    protected abstract int D(int i8);

    protected abstract int E(int i8);

    protected abstract h2 H(int i8);

    @Override // com.google.android.exoplayer2.h2
    public int e(boolean z4) {
        if (this.f9151f == 0) {
            return -1;
        }
        if (this.f9153h) {
            z4 = false;
        }
        int d8 = z4 ? this.f9152g.d() : 0;
        while (H(d8).u()) {
            d8 = F(d8, z4);
            if (d8 == -1) {
                return -1;
            }
        }
        return E(d8) + H(d8).e(z4);
    }

    @Override // com.google.android.exoplayer2.h2
    public final int f(Object obj) {
        int f5;
        if (obj instanceof Pair) {
            Object A = A(obj);
            Object z4 = z(obj);
            int w8 = w(A);
            if (w8 == -1 || (f5 = H(w8).f(z4)) == -1) {
                return -1;
            }
            return D(w8) + f5;
        }
        return -1;
    }

    @Override // com.google.android.exoplayer2.h2
    public int g(boolean z4) {
        int i8 = this.f9151f;
        if (i8 == 0) {
            return -1;
        }
        if (this.f9153h) {
            z4 = false;
        }
        int h8 = z4 ? this.f9152g.h() : i8 - 1;
        while (H(h8).u()) {
            h8 = G(h8, z4);
            if (h8 == -1) {
                return -1;
            }
        }
        return E(h8) + H(h8).g(z4);
    }

    @Override // com.google.android.exoplayer2.h2
    public int i(int i8, int i9, boolean z4) {
        if (this.f9153h) {
            if (i9 == 1) {
                i9 = 2;
            }
            z4 = false;
        }
        int y8 = y(i8);
        int E = E(y8);
        int i10 = H(y8).i(i8 - E, i9 != 2 ? i9 : 0, z4);
        if (i10 != -1) {
            return E + i10;
        }
        int F = F(y8, z4);
        while (F != -1 && H(F).u()) {
            F = F(F, z4);
        }
        if (F != -1) {
            return E(F) + H(F).e(z4);
        }
        if (i9 == 2) {
            return e(z4);
        }
        return -1;
    }

    @Override // com.google.android.exoplayer2.h2
    public final h2.b k(int i8, h2.b bVar, boolean z4) {
        int x8 = x(i8);
        int E = E(x8);
        H(x8).k(i8 - D(x8), bVar, z4);
        bVar.f9758c += E;
        if (z4) {
            bVar.f9757b = C(B(x8), b6.a.e(bVar.f9757b));
        }
        return bVar;
    }

    @Override // com.google.android.exoplayer2.h2
    public final h2.b l(Object obj, h2.b bVar) {
        Object A = A(obj);
        Object z4 = z(obj);
        int w8 = w(A);
        int E = E(w8);
        H(w8).l(z4, bVar);
        bVar.f9758c += E;
        bVar.f9757b = obj;
        return bVar;
    }

    @Override // com.google.android.exoplayer2.h2
    public int p(int i8, int i9, boolean z4) {
        if (this.f9153h) {
            if (i9 == 1) {
                i9 = 2;
            }
            z4 = false;
        }
        int y8 = y(i8);
        int E = E(y8);
        int p8 = H(y8).p(i8 - E, i9 != 2 ? i9 : 0, z4);
        if (p8 != -1) {
            return E + p8;
        }
        int G = G(y8, z4);
        while (G != -1 && H(G).u()) {
            G = G(G, z4);
        }
        if (G != -1) {
            return E(G) + H(G).g(z4);
        }
        if (i9 == 2) {
            return g(z4);
        }
        return -1;
    }

    @Override // com.google.android.exoplayer2.h2
    public final Object q(int i8) {
        int x8 = x(i8);
        return C(B(x8), H(x8).q(i8 - D(x8)));
    }

    @Override // com.google.android.exoplayer2.h2
    public final h2.d s(int i8, h2.d dVar, long j8) {
        int y8 = y(i8);
        int E = E(y8);
        int D = D(y8);
        H(y8).s(i8 - E, dVar, j8);
        Object B = B(y8);
        if (!h2.d.f9767x.equals(dVar.f9770a)) {
            B = C(B, dVar.f9770a);
        }
        dVar.f9770a = B;
        dVar.q += D;
        dVar.f9784t += D;
        return dVar;
    }

    protected abstract int w(Object obj);

    protected abstract int x(int i8);

    protected abstract int y(int i8);
}
