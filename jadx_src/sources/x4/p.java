package x4;

import android.util.SparseArray;
import b6.l0;
import b6.u;
import b6.z;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayList;
import java.util.Arrays;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p implements m {

    /* renamed from: a  reason: collision with root package name */
    private final d0 f24007a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f24008b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f24009c;

    /* renamed from: g  reason: collision with root package name */
    private long f24013g;

    /* renamed from: i  reason: collision with root package name */
    private String f24015i;

    /* renamed from: j  reason: collision with root package name */
    private n4.b0 f24016j;

    /* renamed from: k  reason: collision with root package name */
    private b f24017k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f24018l;

    /* renamed from: n  reason: collision with root package name */
    private boolean f24020n;

    /* renamed from: h  reason: collision with root package name */
    private final boolean[] f24014h = new boolean[3];

    /* renamed from: d  reason: collision with root package name */
    private final u f24010d = new u(7, RecognitionOptions.ITF);

    /* renamed from: e  reason: collision with root package name */
    private final u f24011e = new u(8, RecognitionOptions.ITF);

    /* renamed from: f  reason: collision with root package name */
    private final u f24012f = new u(6, RecognitionOptions.ITF);

    /* renamed from: m  reason: collision with root package name */
    private long f24019m = -9223372036854775807L;

    /* renamed from: o  reason: collision with root package name */
    private final z f24021o = new z();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private final n4.b0 f24022a;

        /* renamed from: b  reason: collision with root package name */
        private final boolean f24023b;

        /* renamed from: c  reason: collision with root package name */
        private final boolean f24024c;

        /* renamed from: f  reason: collision with root package name */
        private final b6.a0 f24027f;

        /* renamed from: g  reason: collision with root package name */
        private byte[] f24028g;

        /* renamed from: h  reason: collision with root package name */
        private int f24029h;

        /* renamed from: i  reason: collision with root package name */
        private int f24030i;

        /* renamed from: j  reason: collision with root package name */
        private long f24031j;

        /* renamed from: k  reason: collision with root package name */
        private boolean f24032k;

        /* renamed from: l  reason: collision with root package name */
        private long f24033l;

        /* renamed from: o  reason: collision with root package name */
        private boolean f24036o;

        /* renamed from: p  reason: collision with root package name */
        private long f24037p;
        private long q;

        /* renamed from: r  reason: collision with root package name */
        private boolean f24038r;

        /* renamed from: d  reason: collision with root package name */
        private final SparseArray<u.c> f24025d = new SparseArray<>();

        /* renamed from: e  reason: collision with root package name */
        private final SparseArray<u.b> f24026e = new SparseArray<>();

        /* renamed from: m  reason: collision with root package name */
        private a f24034m = new a();

        /* renamed from: n  reason: collision with root package name */
        private a f24035n = new a();

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a {

            /* renamed from: a  reason: collision with root package name */
            private boolean f24039a;

            /* renamed from: b  reason: collision with root package name */
            private boolean f24040b;

            /* renamed from: c  reason: collision with root package name */
            private u.c f24041c;

            /* renamed from: d  reason: collision with root package name */
            private int f24042d;

            /* renamed from: e  reason: collision with root package name */
            private int f24043e;

            /* renamed from: f  reason: collision with root package name */
            private int f24044f;

            /* renamed from: g  reason: collision with root package name */
            private int f24045g;

            /* renamed from: h  reason: collision with root package name */
            private boolean f24046h;

            /* renamed from: i  reason: collision with root package name */
            private boolean f24047i;

            /* renamed from: j  reason: collision with root package name */
            private boolean f24048j;

            /* renamed from: k  reason: collision with root package name */
            private boolean f24049k;

            /* renamed from: l  reason: collision with root package name */
            private int f24050l;

            /* renamed from: m  reason: collision with root package name */
            private int f24051m;

            /* renamed from: n  reason: collision with root package name */
            private int f24052n;

            /* renamed from: o  reason: collision with root package name */
            private int f24053o;

            /* renamed from: p  reason: collision with root package name */
            private int f24054p;

            private a() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public boolean c(a aVar) {
                int i8;
                int i9;
                int i10;
                boolean z4;
                if (this.f24039a) {
                    if (aVar.f24039a) {
                        u.c cVar = (u.c) b6.a.h(this.f24041c);
                        u.c cVar2 = (u.c) b6.a.h(aVar.f24041c);
                        return (this.f24044f == aVar.f24044f && this.f24045g == aVar.f24045g && this.f24046h == aVar.f24046h && (!this.f24047i || !aVar.f24047i || this.f24048j == aVar.f24048j) && (((i8 = this.f24042d) == (i9 = aVar.f24042d) || (i8 != 0 && i9 != 0)) && (((i10 = cVar.f8140l) != 0 || cVar2.f8140l != 0 || (this.f24051m == aVar.f24051m && this.f24052n == aVar.f24052n)) && ((i10 != 1 || cVar2.f8140l != 1 || (this.f24053o == aVar.f24053o && this.f24054p == aVar.f24054p)) && (z4 = this.f24049k) == aVar.f24049k && (!z4 || this.f24050l == aVar.f24050l))))) ? false : true;
                    }
                    return true;
                }
                return false;
            }

            public void b() {
                this.f24040b = false;
                this.f24039a = false;
            }

            public boolean d() {
                int i8;
                return this.f24040b && ((i8 = this.f24043e) == 7 || i8 == 2);
            }

            public void e(u.c cVar, int i8, int i9, int i10, int i11, boolean z4, boolean z8, boolean z9, boolean z10, int i12, int i13, int i14, int i15, int i16) {
                this.f24041c = cVar;
                this.f24042d = i8;
                this.f24043e = i9;
                this.f24044f = i10;
                this.f24045g = i11;
                this.f24046h = z4;
                this.f24047i = z8;
                this.f24048j = z9;
                this.f24049k = z10;
                this.f24050l = i12;
                this.f24051m = i13;
                this.f24052n = i14;
                this.f24053o = i15;
                this.f24054p = i16;
                this.f24039a = true;
                this.f24040b = true;
            }

            public void f(int i8) {
                this.f24043e = i8;
                this.f24040b = true;
            }
        }

        public b(n4.b0 b0Var, boolean z4, boolean z8) {
            this.f24022a = b0Var;
            this.f24023b = z4;
            this.f24024c = z8;
            byte[] bArr = new byte[RecognitionOptions.ITF];
            this.f24028g = bArr;
            this.f24027f = new b6.a0(bArr, 0, 0);
            g();
        }

        private void d(int i8) {
            long j8 = this.q;
            if (j8 == -9223372036854775807L) {
                return;
            }
            boolean z4 = this.f24038r;
            this.f24022a.d(j8, z4 ? 1 : 0, (int) (this.f24031j - this.f24037p), i8, null);
        }

        /* JADX WARN: Removed duplicated region for block: B:53:0x00ff  */
        /* JADX WARN: Removed duplicated region for block: B:54:0x0102  */
        /* JADX WARN: Removed duplicated region for block: B:56:0x0106  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x0118  */
        /* JADX WARN: Removed duplicated region for block: B:63:0x011e  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x0152  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void a(byte[] r24, int r25, int r26) {
            /*
                Method dump skipped, instructions count: 414
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: x4.p.b.a(byte[], int, int):void");
        }

        public boolean b(long j8, int i8, boolean z4, boolean z8) {
            boolean z9 = false;
            if (this.f24030i == 9 || (this.f24024c && this.f24035n.c(this.f24034m))) {
                if (z4 && this.f24036o) {
                    d(i8 + ((int) (j8 - this.f24031j)));
                }
                this.f24037p = this.f24031j;
                this.q = this.f24033l;
                this.f24038r = false;
                this.f24036o = true;
            }
            if (this.f24023b) {
                z8 = this.f24035n.d();
            }
            boolean z10 = this.f24038r;
            int i9 = this.f24030i;
            if (i9 == 5 || (z8 && i9 == 1)) {
                z9 = true;
            }
            boolean z11 = z10 | z9;
            this.f24038r = z11;
            return z11;
        }

        public boolean c() {
            return this.f24024c;
        }

        public void e(u.b bVar) {
            this.f24026e.append(bVar.f8126a, bVar);
        }

        public void f(u.c cVar) {
            this.f24025d.append(cVar.f8132d, cVar);
        }

        public void g() {
            this.f24032k = false;
            this.f24036o = false;
            this.f24035n.b();
        }

        public void h(long j8, int i8, long j9) {
            this.f24030i = i8;
            this.f24033l = j9;
            this.f24031j = j8;
            if (!this.f24023b || i8 != 1) {
                if (!this.f24024c) {
                    return;
                }
                if (i8 != 5 && i8 != 1 && i8 != 2) {
                    return;
                }
            }
            a aVar = this.f24034m;
            this.f24034m = this.f24035n;
            this.f24035n = aVar;
            aVar.b();
            this.f24029h = 0;
            this.f24032k = true;
        }
    }

    public p(d0 d0Var, boolean z4, boolean z8) {
        this.f24007a = d0Var;
        this.f24008b = z4;
        this.f24009c = z8;
    }

    private void a() {
        b6.a.h(this.f24016j);
        l0.j(this.f24017k);
    }

    private void g(long j8, int i8, int i9, long j9) {
        u uVar;
        if (!this.f24018l || this.f24017k.c()) {
            this.f24010d.b(i9);
            this.f24011e.b(i9);
            if (this.f24018l) {
                if (this.f24010d.c()) {
                    u uVar2 = this.f24010d;
                    this.f24017k.f(b6.u.l(uVar2.f24123d, 3, uVar2.f24124e));
                    uVar = this.f24010d;
                } else if (this.f24011e.c()) {
                    u uVar3 = this.f24011e;
                    this.f24017k.e(b6.u.j(uVar3.f24123d, 3, uVar3.f24124e));
                    uVar = this.f24011e;
                }
            } else if (this.f24010d.c() && this.f24011e.c()) {
                ArrayList arrayList = new ArrayList();
                u uVar4 = this.f24010d;
                arrayList.add(Arrays.copyOf(uVar4.f24123d, uVar4.f24124e));
                u uVar5 = this.f24011e;
                arrayList.add(Arrays.copyOf(uVar5.f24123d, uVar5.f24124e));
                u uVar6 = this.f24010d;
                u.c l8 = b6.u.l(uVar6.f24123d, 3, uVar6.f24124e);
                u uVar7 = this.f24011e;
                u.b j10 = b6.u.j(uVar7.f24123d, 3, uVar7.f24124e);
                this.f24016j.f(new w0.b().U(this.f24015i).g0("video/avc").K(b6.e.a(l8.f8129a, l8.f8130b, l8.f8131c)).n0(l8.f8134f).S(l8.f8135g).c0(l8.f8136h).V(arrayList).G());
                this.f24018l = true;
                this.f24017k.f(l8);
                this.f24017k.e(j10);
                this.f24010d.d();
                uVar = this.f24011e;
            }
            uVar.d();
        }
        if (this.f24012f.b(i9)) {
            u uVar8 = this.f24012f;
            this.f24021o.S(this.f24012f.f24123d, b6.u.q(uVar8.f24123d, uVar8.f24124e));
            this.f24021o.U(4);
            this.f24007a.a(j9, this.f24021o);
        }
        if (this.f24017k.b(j8, i8, this.f24018l, this.f24020n)) {
            this.f24020n = false;
        }
    }

    private void h(byte[] bArr, int i8, int i9) {
        if (!this.f24018l || this.f24017k.c()) {
            this.f24010d.a(bArr, i8, i9);
            this.f24011e.a(bArr, i8, i9);
        }
        this.f24012f.a(bArr, i8, i9);
        this.f24017k.a(bArr, i8, i9);
    }

    private void i(long j8, int i8, long j9) {
        if (!this.f24018l || this.f24017k.c()) {
            this.f24010d.e(i8);
            this.f24011e.e(i8);
        }
        this.f24012f.e(i8);
        this.f24017k.h(j8, i8, j9);
    }

    @Override // x4.m
    public void b(z zVar) {
        a();
        int f5 = zVar.f();
        int g8 = zVar.g();
        byte[] e8 = zVar.e();
        this.f24013g += zVar.a();
        this.f24016j.b(zVar, zVar.a());
        while (true) {
            int c9 = b6.u.c(e8, f5, g8, this.f24014h);
            if (c9 == g8) {
                h(e8, f5, g8);
                return;
            }
            int f8 = b6.u.f(e8, c9);
            int i8 = c9 - f5;
            if (i8 > 0) {
                h(e8, f5, c9);
            }
            int i9 = g8 - c9;
            long j8 = this.f24013g - i9;
            g(j8, i9, i8 < 0 ? -i8 : 0, this.f24019m);
            i(j8, f8, this.f24019m);
            f5 = c9 + 3;
        }
    }

    @Override // x4.m
    public void c() {
        this.f24013g = 0L;
        this.f24020n = false;
        this.f24019m = -9223372036854775807L;
        b6.u.a(this.f24014h);
        this.f24010d.d();
        this.f24011e.d();
        this.f24012f.d();
        b bVar = this.f24017k;
        if (bVar != null) {
            bVar.g();
        }
    }

    @Override // x4.m
    public void d(n4.m mVar, i0.d dVar) {
        dVar.a();
        this.f24015i = dVar.b();
        n4.b0 e8 = mVar.e(dVar.c(), 2);
        this.f24016j = e8;
        this.f24017k = new b(e8, this.f24008b, this.f24009c);
        this.f24007a.b(mVar, dVar);
    }

    @Override // x4.m
    public void e() {
    }

    @Override // x4.m
    public void f(long j8, int i8) {
        if (j8 != -9223372036854775807L) {
            this.f24019m = j8;
        }
        this.f24020n |= (i8 & 2) != 0;
    }
}
