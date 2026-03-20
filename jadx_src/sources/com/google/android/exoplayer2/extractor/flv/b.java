package com.google.android.exoplayer2.extractor.flv;

import b6.z;
import n4.k;
import n4.l;
import n4.m;
import n4.p;
import n4.y;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b implements k {
    public static final p q = r4.a.b;

    /* renamed from: f  reason: collision with root package name */
    private m f9674f;

    /* renamed from: h  reason: collision with root package name */
    private boolean f9676h;

    /* renamed from: i  reason: collision with root package name */
    private long f9677i;

    /* renamed from: j  reason: collision with root package name */
    private int f9678j;

    /* renamed from: k  reason: collision with root package name */
    private int f9679k;

    /* renamed from: l  reason: collision with root package name */
    private int f9680l;

    /* renamed from: m  reason: collision with root package name */
    private long f9681m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f9682n;

    /* renamed from: o  reason: collision with root package name */
    private a f9683o;

    /* renamed from: p  reason: collision with root package name */
    private d f9684p;

    /* renamed from: a  reason: collision with root package name */
    private final z f9669a = new z(4);

    /* renamed from: b  reason: collision with root package name */
    private final z f9670b = new z(9);

    /* renamed from: c  reason: collision with root package name */
    private final z f9671c = new z(11);

    /* renamed from: d  reason: collision with root package name */
    private final z f9672d = new z();

    /* renamed from: e  reason: collision with root package name */
    private final c f9673e = new c();

    /* renamed from: g  reason: collision with root package name */
    private int f9675g = 1;

    private void d() {
        if (this.f9682n) {
            return;
        }
        this.f9674f.m(new z.b(-9223372036854775807L));
        this.f9682n = true;
    }

    private long f() {
        if (this.f9676h) {
            return this.f9677i + this.f9681m;
        }
        if (this.f9673e.d() == -9223372036854775807L) {
            return 0L;
        }
        return this.f9681m;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ k[] h() {
        return new k[]{new b()};
    }

    private b6.z i(l lVar) {
        if (this.f9680l > this.f9672d.b()) {
            b6.z zVar = this.f9672d;
            zVar.S(new byte[Math.max(zVar.b() * 2, this.f9680l)], 0);
        } else {
            this.f9672d.U(0);
        }
        this.f9672d.T(this.f9680l);
        lVar.readFully(this.f9672d.e(), 0, this.f9680l);
        return this.f9672d;
    }

    private boolean j(l lVar) {
        if (lVar.c(this.f9670b.e(), 0, 9, true)) {
            this.f9670b.U(0);
            this.f9670b.V(4);
            int H = this.f9670b.H();
            boolean z4 = (H & 4) != 0;
            boolean z8 = (H & 1) != 0;
            if (z4 && this.f9683o == null) {
                this.f9683o = new a(this.f9674f.e(8, 1));
            }
            if (z8 && this.f9684p == null) {
                this.f9684p = new d(this.f9674f.e(9, 2));
            }
            this.f9674f.o();
            this.f9678j = (this.f9670b.q() - 9) + 4;
            this.f9675g = 2;
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0071 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0083  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean k(n4.l r10) {
        /*
            r9 = this;
            long r0 = r9.f()
            int r2 = r9.f9679k
            r3 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
            r5 = 0
            r6 = 1
            r7 = 8
            if (r2 != r7) goto L24
            com.google.android.exoplayer2.extractor.flv.a r7 = r9.f9683o
            if (r7 == 0) goto L24
            r9.d()
            com.google.android.exoplayer2.extractor.flv.a r2 = r9.f9683o
        L1a:
            b6.z r10 = r9.i(r10)
            boolean r5 = r2.a(r10, r0)
        L22:
            r10 = r6
            goto L6d
        L24:
            r7 = 9
            if (r2 != r7) goto L32
            com.google.android.exoplayer2.extractor.flv.d r7 = r9.f9684p
            if (r7 == 0) goto L32
            r9.d()
            com.google.android.exoplayer2.extractor.flv.d r2 = r9.f9684p
            goto L1a
        L32:
            r7 = 18
            if (r2 != r7) goto L67
            boolean r2 = r9.f9682n
            if (r2 != 0) goto L67
            com.google.android.exoplayer2.extractor.flv.c r2 = r9.f9673e
            b6.z r10 = r9.i(r10)
            boolean r5 = r2.a(r10, r0)
            com.google.android.exoplayer2.extractor.flv.c r10 = r9.f9673e
            long r0 = r10.d()
            int r10 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r10 == 0) goto L22
            n4.m r10 = r9.f9674f
            n4.x r2 = new n4.x
            com.google.android.exoplayer2.extractor.flv.c r7 = r9.f9673e
            long[] r7 = r7.e()
            com.google.android.exoplayer2.extractor.flv.c r8 = r9.f9673e
            long[] r8 = r8.f()
            r2.<init>(r7, r8, r0)
            r10.m(r2)
            r9.f9682n = r6
            goto L22
        L67:
            int r0 = r9.f9680l
            r10.i(r0)
            r10 = r5
        L6d:
            boolean r0 = r9.f9676h
            if (r0 != 0) goto L87
            if (r5 == 0) goto L87
            r9.f9676h = r6
            com.google.android.exoplayer2.extractor.flv.c r0 = r9.f9673e
            long r0 = r0.d()
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 != 0) goto L83
            long r0 = r9.f9681m
            long r0 = -r0
            goto L85
        L83:
            r0 = 0
        L85:
            r9.f9677i = r0
        L87:
            r0 = 4
            r9.f9678j = r0
            r0 = 2
            r9.f9675g = r0
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.flv.b.k(n4.l):boolean");
    }

    private boolean l(l lVar) {
        if (lVar.c(this.f9671c.e(), 0, 11, true)) {
            this.f9671c.U(0);
            this.f9679k = this.f9671c.H();
            this.f9680l = this.f9671c.K();
            this.f9681m = this.f9671c.K();
            this.f9681m = ((this.f9671c.H() << 24) | this.f9681m) * 1000;
            this.f9671c.V(3);
            this.f9675g = 4;
            return true;
        }
        return false;
    }

    private void m(l lVar) {
        lVar.i(this.f9678j);
        this.f9678j = 0;
        this.f9675g = 3;
    }

    @Override // n4.k
    public void b(m mVar) {
        this.f9674f = mVar;
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        if (j8 == 0) {
            this.f9675g = 1;
            this.f9676h = false;
        } else {
            this.f9675g = 3;
        }
        this.f9678j = 0;
    }

    @Override // n4.k
    public int e(l lVar, y yVar) {
        b6.a.h(this.f9674f);
        while (true) {
            int i8 = this.f9675g;
            if (i8 != 1) {
                if (i8 == 2) {
                    m(lVar);
                } else if (i8 != 3) {
                    if (i8 != 4) {
                        throw new IllegalStateException();
                    }
                    if (k(lVar)) {
                        return 0;
                    }
                } else if (!l(lVar)) {
                    return -1;
                }
            } else if (!j(lVar)) {
                return -1;
            }
        }
    }

    @Override // n4.k
    public boolean g(l lVar) {
        lVar.k(this.f9669a.e(), 0, 3);
        this.f9669a.U(0);
        if (this.f9669a.K() != 4607062) {
            return false;
        }
        lVar.k(this.f9669a.e(), 0, 2);
        this.f9669a.U(0);
        if ((this.f9669a.N() & 250) != 0) {
            return false;
        }
        lVar.k(this.f9669a.e(), 0, 4);
        this.f9669a.U(0);
        int q8 = this.f9669a.q();
        lVar.h();
        lVar.f(q8);
        lVar.k(this.f9669a.e(), 0, 4);
        this.f9669a.U(0);
        return this.f9669a.q() == 0;
    }

    @Override // n4.k
    public void release() {
    }
}
