package com.google.android.exoplayer2.source;

import android.os.Handler;
import b6.l0;
import com.google.android.exoplayer2.drm.h;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.source.l;
import java.io.IOException;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class c<T> extends com.google.android.exoplayer2.source.a {

    /* renamed from: h  reason: collision with root package name */
    private final HashMap<T, b<T>> f10272h = new HashMap<>();

    /* renamed from: j  reason: collision with root package name */
    private Handler f10273j;

    /* renamed from: k  reason: collision with root package name */
    private a6.y f10274k;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private final class a implements l, com.google.android.exoplayer2.drm.h {

        /* renamed from: a  reason: collision with root package name */
        private final T f10275a;

        /* renamed from: b  reason: collision with root package name */
        private l.a f10276b;

        /* renamed from: c  reason: collision with root package name */
        private h.a f10277c;

        public a(T t8) {
            this.f10276b = c.this.w(null);
            this.f10277c = c.this.u(null);
            this.f10275a = t8;
        }

        private boolean a(int i8, k.b bVar) {
            k.b bVar2;
            if (bVar != null) {
                bVar2 = c.this.I(this.f10275a, bVar);
                if (bVar2 == null) {
                    return false;
                }
            } else {
                bVar2 = null;
            }
            int K = c.this.K(this.f10275a, i8);
            l.a aVar = this.f10276b;
            if (aVar.f10617a != K || !l0.c(aVar.f10618b, bVar2)) {
                this.f10276b = c.this.v(K, bVar2, 0L);
            }
            h.a aVar2 = this.f10277c;
            if (aVar2.f9618a == K && l0.c(aVar2.f9619b, bVar2)) {
                return true;
            }
            this.f10277c = c.this.s(K, bVar2);
            return true;
        }

        private h5.i h(h5.i iVar) {
            long J = c.this.J(this.f10275a, iVar.f20284f);
            long J2 = c.this.J(this.f10275a, iVar.f20285g);
            return (J == iVar.f20284f && J2 == iVar.f20285g) ? iVar : new h5.i(iVar.f20279a, iVar.f20280b, iVar.f20281c, iVar.f20282d, iVar.f20283e, J, J2);
        }

        @Override // com.google.android.exoplayer2.source.l
        public void I(int i8, k.b bVar, h5.i iVar) {
            if (a(i8, bVar)) {
                this.f10276b.E(h(iVar));
            }
        }

        @Override // com.google.android.exoplayer2.drm.h
        public void J(int i8, k.b bVar, Exception exc) {
            if (a(i8, bVar)) {
                this.f10277c.l(exc);
            }
        }

        @Override // com.google.android.exoplayer2.source.l
        public void M(int i8, k.b bVar, h5.i iVar) {
            if (a(i8, bVar)) {
                this.f10276b.j(h(iVar));
            }
        }

        @Override // com.google.android.exoplayer2.source.l
        public void O(int i8, k.b bVar, h5.h hVar, h5.i iVar) {
            if (a(i8, bVar)) {
                this.f10276b.v(hVar, h(iVar));
            }
        }

        @Override // com.google.android.exoplayer2.source.l
        public void R(int i8, k.b bVar, h5.h hVar, h5.i iVar) {
            if (a(i8, bVar)) {
                this.f10276b.B(hVar, h(iVar));
            }
        }

        @Override // com.google.android.exoplayer2.drm.h
        public void b0(int i8, k.b bVar) {
            if (a(i8, bVar)) {
                this.f10277c.i();
            }
        }

        @Override // com.google.android.exoplayer2.source.l
        public void c0(int i8, k.b bVar, h5.h hVar, h5.i iVar) {
            if (a(i8, bVar)) {
                this.f10276b.s(hVar, h(iVar));
            }
        }

        @Override // com.google.android.exoplayer2.drm.h
        public void g0(int i8, k.b bVar) {
            if (a(i8, bVar)) {
                this.f10277c.h();
            }
        }

        @Override // com.google.android.exoplayer2.source.l
        public void i0(int i8, k.b bVar, h5.h hVar, h5.i iVar, IOException iOException, boolean z4) {
            if (a(i8, bVar)) {
                this.f10276b.y(hVar, h(iVar), iOException, z4);
            }
        }

        @Override // com.google.android.exoplayer2.drm.h
        public void k0(int i8, k.b bVar, int i9) {
            if (a(i8, bVar)) {
                this.f10277c.k(i9);
            }
        }

        @Override // com.google.android.exoplayer2.drm.h
        public void l0(int i8, k.b bVar) {
            if (a(i8, bVar)) {
                this.f10277c.m();
            }
        }

        @Override // com.google.android.exoplayer2.drm.h
        public void o0(int i8, k.b bVar) {
            if (a(i8, bVar)) {
                this.f10277c.j();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b<T> {

        /* renamed from: a  reason: collision with root package name */
        public final k f10279a;

        /* renamed from: b  reason: collision with root package name */
        public final k.c f10280b;

        /* renamed from: c  reason: collision with root package name */
        public final c<T>.a f10281c;

        public b(k kVar, k.c cVar, c<T>.a aVar) {
            this.f10279a = kVar;
            this.f10280b = cVar;
            this.f10281c = aVar;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.a
    public void C(a6.y yVar) {
        this.f10274k = yVar;
        this.f10273j = l0.w();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.a
    public void E() {
        for (b<T> bVar : this.f10272h.values()) {
            bVar.f10279a.c(bVar.f10280b);
            bVar.f10279a.f(bVar.f10281c);
            bVar.f10279a.m(bVar.f10281c);
        }
        this.f10272h.clear();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void G(T t8) {
        b bVar = (b) b6.a.e(this.f10272h.get(t8));
        bVar.f10279a.g(bVar.f10280b);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void H(T t8) {
        b bVar = (b) b6.a.e(this.f10272h.get(t8));
        bVar.f10279a.r(bVar.f10280b);
    }

    protected abstract k.b I(T t8, k.b bVar);

    protected long J(T t8, long j8) {
        return j8;
    }

    protected abstract int K(T t8, int i8);

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: M */
    public abstract void L(T t8, k kVar, h2 h2Var);

    /* JADX INFO: Access modifiers changed from: protected */
    public final void N(T t8, k kVar) {
        b6.a.a(!this.f10272h.containsKey(t8));
        k.c bVar = new h5.b(this, t8);
        a aVar = new a(t8);
        this.f10272h.put(t8, new b<>(kVar, bVar, aVar));
        kVar.e((Handler) b6.a.e(this.f10273j), aVar);
        kVar.l((Handler) b6.a.e(this.f10273j), aVar);
        kVar.a(bVar, this.f10274k, A());
        if (B()) {
            return;
        }
        kVar.g(bVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void O(T t8) {
        b bVar = (b) b6.a.e(this.f10272h.remove(t8));
        bVar.f10279a.c(bVar.f10280b);
        bVar.f10279a.f(bVar.f10281c);
        bVar.f10279a.m(bVar.f10281c);
    }

    @Override // com.google.android.exoplayer2.source.k
    public void n() {
        for (b<T> bVar : this.f10272h.values()) {
            bVar.f10279a.n();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.a
    public void y() {
        for (b<T> bVar : this.f10272h.values()) {
            bVar.f10279a.g(bVar.f10280b);
        }
    }

    @Override // com.google.android.exoplayer2.source.a
    protected void z() {
        for (b<T> bVar : this.f10272h.values()) {
            bVar.f10279a.r(bVar.f10280b);
        }
    }
}
