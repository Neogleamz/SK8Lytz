package com.google.android.exoplayer2.source;

import b6.l0;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.z0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i extends a0 {

    /* renamed from: n  reason: collision with root package name */
    private final boolean f10606n;

    /* renamed from: p  reason: collision with root package name */
    private final h2.d f10607p;
    private final h2.b q;

    /* renamed from: t  reason: collision with root package name */
    private a f10608t;

    /* renamed from: w  reason: collision with root package name */
    private h f10609w;

    /* renamed from: x  reason: collision with root package name */
    private boolean f10610x;

    /* renamed from: y  reason: collision with root package name */
    private boolean f10611y;

    /* renamed from: z  reason: collision with root package name */
    private boolean f10612z;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends f {

        /* renamed from: j  reason: collision with root package name */
        public static final Object f10613j = new Object();

        /* renamed from: g  reason: collision with root package name */
        private final Object f10614g;

        /* renamed from: h  reason: collision with root package name */
        private final Object f10615h;

        private a(h2 h2Var, Object obj, Object obj2) {
            super(h2Var);
            this.f10614g = obj;
            this.f10615h = obj2;
        }

        public static a y(z0 z0Var) {
            return new a(new b(z0Var), h2.d.f9767x, f10613j);
        }

        public static a z(h2 h2Var, Object obj, Object obj2) {
            return new a(h2Var, obj, obj2);
        }

        @Override // com.google.android.exoplayer2.source.f, com.google.android.exoplayer2.h2
        public int f(Object obj) {
            Object obj2;
            h2 h2Var = this.f10433f;
            if (f10613j.equals(obj) && (obj2 = this.f10615h) != null) {
                obj = obj2;
            }
            return h2Var.f(obj);
        }

        @Override // com.google.android.exoplayer2.source.f, com.google.android.exoplayer2.h2
        public h2.b k(int i8, h2.b bVar, boolean z4) {
            this.f10433f.k(i8, bVar, z4);
            if (l0.c(bVar.f9757b, this.f10615h) && z4) {
                bVar.f9757b = f10613j;
            }
            return bVar;
        }

        @Override // com.google.android.exoplayer2.source.f, com.google.android.exoplayer2.h2
        public Object q(int i8) {
            Object q = this.f10433f.q(i8);
            return l0.c(q, this.f10615h) ? f10613j : q;
        }

        @Override // com.google.android.exoplayer2.source.f, com.google.android.exoplayer2.h2
        public h2.d s(int i8, h2.d dVar, long j8) {
            this.f10433f.s(i8, dVar, j8);
            if (l0.c(dVar.f9770a, this.f10614g)) {
                dVar.f9770a = h2.d.f9767x;
            }
            return dVar;
        }

        public a x(h2 h2Var) {
            return new a(h2Var, this.f10614g, this.f10615h);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends h2 {

        /* renamed from: f  reason: collision with root package name */
        private final z0 f10616f;

        public b(z0 z0Var) {
            this.f10616f = z0Var;
        }

        @Override // com.google.android.exoplayer2.h2
        public int f(Object obj) {
            return obj == a.f10613j ? 0 : -1;
        }

        @Override // com.google.android.exoplayer2.h2
        public h2.b k(int i8, h2.b bVar, boolean z4) {
            bVar.v(z4 ? 0 : null, z4 ? a.f10613j : null, 0, -9223372036854775807L, 0L, i5.c.f20513g, true);
            return bVar;
        }

        @Override // com.google.android.exoplayer2.h2
        public int m() {
            return 1;
        }

        @Override // com.google.android.exoplayer2.h2
        public Object q(int i8) {
            return a.f10613j;
        }

        @Override // com.google.android.exoplayer2.h2
        public h2.d s(int i8, h2.d dVar, long j8) {
            dVar.i(h2.d.f9767x, this.f10616f, null, -9223372036854775807L, -9223372036854775807L, -9223372036854775807L, false, true, null, 0L, -9223372036854775807L, 0, 0, 0L);
            dVar.f9781m = true;
            return dVar;
        }

        @Override // com.google.android.exoplayer2.h2
        public int t() {
            return 1;
        }
    }

    public i(k kVar, boolean z4) {
        super(kVar);
        this.f10606n = z4 && kVar.o();
        this.f10607p = new h2.d();
        this.q = new h2.b();
        h2 q = kVar.q();
        if (q == null) {
            this.f10608t = a.y(kVar.i());
            return;
        }
        this.f10608t = a.z(q, null, null);
        this.f10612z = true;
    }

    private Object a0(Object obj) {
        return (this.f10608t.f10615h == null || !this.f10608t.f10615h.equals(obj)) ? obj : a.f10613j;
    }

    private Object b0(Object obj) {
        return (this.f10608t.f10615h == null || !obj.equals(a.f10613j)) ? obj : this.f10608t.f10615h;
    }

    private void d0(long j8) {
        h hVar = this.f10609w;
        int f5 = this.f10608t.f(hVar.f10439a.f20286a);
        if (f5 == -1) {
            return;
        }
        long j9 = this.f10608t.j(f5, this.q).f9759d;
        if (j9 != -9223372036854775807L && j8 >= j9) {
            j8 = Math.max(0L, j9 - 1);
        }
        hVar.w(j8);
    }

    @Override // com.google.android.exoplayer2.source.c, com.google.android.exoplayer2.source.a
    public void E() {
        this.f10611y = false;
        this.f10610x = false;
        super.E();
    }

    @Override // com.google.android.exoplayer2.source.a0
    protected k.b P(k.b bVar) {
        return bVar.c(a0(bVar.f20286a));
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    @Override // com.google.android.exoplayer2.source.a0
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void V(com.google.android.exoplayer2.h2 r15) {
        /*
            r14 = this;
            boolean r0 = r14.f10611y
            if (r0 == 0) goto L19
            com.google.android.exoplayer2.source.i$a r0 = r14.f10608t
            com.google.android.exoplayer2.source.i$a r15 = r0.x(r15)
            r14.f10608t = r15
            com.google.android.exoplayer2.source.h r15 = r14.f10609w
            if (r15 == 0) goto Lae
            long r0 = r15.m()
            r14.d0(r0)
            goto Lae
        L19:
            boolean r0 = r15.u()
            if (r0 == 0) goto L36
            boolean r0 = r14.f10612z
            if (r0 == 0) goto L2a
            com.google.android.exoplayer2.source.i$a r0 = r14.f10608t
            com.google.android.exoplayer2.source.i$a r15 = r0.x(r15)
            goto L32
        L2a:
            java.lang.Object r0 = com.google.android.exoplayer2.h2.d.f9767x
            java.lang.Object r1 = com.google.android.exoplayer2.source.i.a.f10613j
            com.google.android.exoplayer2.source.i$a r15 = com.google.android.exoplayer2.source.i.a.z(r15, r0, r1)
        L32:
            r14.f10608t = r15
            goto Lae
        L36:
            com.google.android.exoplayer2.h2$d r0 = r14.f10607p
            r1 = 0
            r15.r(r1, r0)
            com.google.android.exoplayer2.h2$d r0 = r14.f10607p
            long r2 = r0.e()
            com.google.android.exoplayer2.h2$d r0 = r14.f10607p
            java.lang.Object r0 = r0.f9770a
            com.google.android.exoplayer2.source.h r4 = r14.f10609w
            if (r4 == 0) goto L74
            long r4 = r4.o()
            com.google.android.exoplayer2.source.i$a r6 = r14.f10608t
            com.google.android.exoplayer2.source.h r7 = r14.f10609w
            com.google.android.exoplayer2.source.k$b r7 = r7.f10439a
            java.lang.Object r7 = r7.f20286a
            com.google.android.exoplayer2.h2$b r8 = r14.q
            r6.l(r7, r8)
            com.google.android.exoplayer2.h2$b r6 = r14.q
            long r6 = r6.q()
            long r6 = r6 + r4
            com.google.android.exoplayer2.source.i$a r4 = r14.f10608t
            com.google.android.exoplayer2.h2$d r5 = r14.f10607p
            com.google.android.exoplayer2.h2$d r1 = r4.r(r1, r5)
            long r4 = r1.e()
            int r1 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r1 == 0) goto L74
            r12 = r6
            goto L75
        L74:
            r12 = r2
        L75:
            com.google.android.exoplayer2.h2$d r9 = r14.f10607p
            com.google.android.exoplayer2.h2$b r10 = r14.q
            r11 = 0
            r8 = r15
            android.util.Pair r1 = r8.n(r9, r10, r11, r12)
            java.lang.Object r2 = r1.first
            java.lang.Object r1 = r1.second
            java.lang.Long r1 = (java.lang.Long) r1
            long r3 = r1.longValue()
            boolean r1 = r14.f10612z
            if (r1 == 0) goto L94
            com.google.android.exoplayer2.source.i$a r0 = r14.f10608t
            com.google.android.exoplayer2.source.i$a r15 = r0.x(r15)
            goto L98
        L94:
            com.google.android.exoplayer2.source.i$a r15 = com.google.android.exoplayer2.source.i.a.z(r15, r0, r2)
        L98:
            r14.f10608t = r15
            com.google.android.exoplayer2.source.h r15 = r14.f10609w
            if (r15 == 0) goto Lae
            r14.d0(r3)
            com.google.android.exoplayer2.source.k$b r15 = r15.f10439a
            java.lang.Object r0 = r15.f20286a
            java.lang.Object r0 = r14.b0(r0)
            com.google.android.exoplayer2.source.k$b r15 = r15.c(r0)
            goto Laf
        Lae:
            r15 = 0
        Laf:
            r0 = 1
            r14.f10612z = r0
            r14.f10611y = r0
            com.google.android.exoplayer2.source.i$a r0 = r14.f10608t
            r14.D(r0)
            if (r15 == 0) goto Lc6
            com.google.android.exoplayer2.source.h r0 = r14.f10609w
            java.lang.Object r0 = b6.a.e(r0)
            com.google.android.exoplayer2.source.h r0 = (com.google.android.exoplayer2.source.h) r0
            r0.j(r15)
        Lc6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.i.V(com.google.android.exoplayer2.h2):void");
    }

    @Override // com.google.android.exoplayer2.source.a0
    public void Y() {
        if (this.f10606n) {
            return;
        }
        this.f10610x = true;
        X();
    }

    @Override // com.google.android.exoplayer2.source.k
    /* renamed from: Z */
    public h b(k.b bVar, a6.b bVar2, long j8) {
        h hVar = new h(bVar, bVar2, j8);
        hVar.y(this.f10261l);
        if (this.f10611y) {
            hVar.j(bVar.c(b0(bVar.f20286a)));
        } else {
            this.f10609w = hVar;
            if (!this.f10610x) {
                this.f10610x = true;
                X();
            }
        }
        return hVar;
    }

    public h2 c0() {
        return this.f10608t;
    }

    @Override // com.google.android.exoplayer2.source.c, com.google.android.exoplayer2.source.k
    public void n() {
    }

    @Override // com.google.android.exoplayer2.source.k
    public void p(j jVar) {
        ((h) jVar).x();
        if (jVar == this.f10609w) {
            this.f10609w = null;
        }
    }
}
