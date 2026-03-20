package x4;

import b6.z;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w implements i0 {

    /* renamed from: a  reason: collision with root package name */
    private final m f24128a;

    /* renamed from: b  reason: collision with root package name */
    private final b6.y f24129b = new b6.y(new byte[10]);

    /* renamed from: c  reason: collision with root package name */
    private int f24130c = 0;

    /* renamed from: d  reason: collision with root package name */
    private int f24131d;

    /* renamed from: e  reason: collision with root package name */
    private b6.h0 f24132e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f24133f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f24134g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f24135h;

    /* renamed from: i  reason: collision with root package name */
    private int f24136i;

    /* renamed from: j  reason: collision with root package name */
    private int f24137j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f24138k;

    /* renamed from: l  reason: collision with root package name */
    private long f24139l;

    public w(m mVar) {
        this.f24128a = mVar;
    }

    private boolean d(z zVar, byte[] bArr, int i8) {
        int min = Math.min(zVar.a(), i8 - this.f24131d);
        if (min <= 0) {
            return true;
        }
        if (bArr == null) {
            zVar.V(min);
        } else {
            zVar.l(bArr, this.f24131d, min);
        }
        int i9 = this.f24131d + min;
        this.f24131d = i9;
        return i9 == i8;
    }

    private boolean e() {
        this.f24129b.p(0);
        int h8 = this.f24129b.h(24);
        if (h8 != 1) {
            b6.p.i("PesReader", "Unexpected start code prefix: " + h8);
            this.f24137j = -1;
            return false;
        }
        this.f24129b.r(8);
        int h9 = this.f24129b.h(16);
        this.f24129b.r(5);
        this.f24138k = this.f24129b.g();
        this.f24129b.r(2);
        this.f24133f = this.f24129b.g();
        this.f24134g = this.f24129b.g();
        this.f24129b.r(6);
        int h10 = this.f24129b.h(8);
        this.f24136i = h10;
        if (h9 != 0) {
            int i8 = ((h9 + 6) - 9) - h10;
            this.f24137j = i8;
            if (i8 < 0) {
                b6.p.i("PesReader", "Found negative packet payload size: " + this.f24137j);
            }
            return true;
        }
        this.f24137j = -1;
        return true;
    }

    private void f() {
        this.f24129b.p(0);
        this.f24139l = -9223372036854775807L;
        if (this.f24133f) {
            this.f24129b.r(4);
            this.f24129b.r(1);
            this.f24129b.r(1);
            long h8 = (this.f24129b.h(3) << 30) | (this.f24129b.h(15) << 15) | this.f24129b.h(15);
            this.f24129b.r(1);
            if (!this.f24135h && this.f24134g) {
                this.f24129b.r(4);
                this.f24129b.r(1);
                this.f24129b.r(1);
                this.f24129b.r(1);
                this.f24132e.b((this.f24129b.h(3) << 30) | (this.f24129b.h(15) << 15) | this.f24129b.h(15));
                this.f24135h = true;
            }
            this.f24139l = this.f24132e.b(h8);
        }
    }

    private void g(int i8) {
        this.f24130c = i8;
        this.f24131d = 0;
    }

    @Override // x4.i0
    public void a(b6.h0 h0Var, n4.m mVar, i0.d dVar) {
        this.f24132e = h0Var;
        this.f24128a.d(mVar, dVar);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x004d  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:34:0x007a -> B:35:0x007c). Please submit an issue!!! */
    @Override // x4.i0
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void b(b6.z r8, int r9) {
        /*
            r7 = this;
            b6.h0 r0 = r7.f24132e
            b6.a.h(r0)
            r0 = r9 & 1
            r1 = -1
            r2 = 3
            r3 = 2
            r4 = 1
            if (r0 == 0) goto L47
            int r0 = r7.f24130c
            if (r0 == 0) goto L44
            if (r0 == r4) goto L44
            java.lang.String r5 = "PesReader"
            if (r0 == r3) goto L3f
            if (r0 != r2) goto L39
            int r0 = r7.f24137j
            if (r0 == r1) goto L7c
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r6 = "Unexpected start indicator: expected "
            r0.append(r6)
            int r6 = r7.f24137j
            r0.append(r6)
            java.lang.String r6 = " more bytes"
            r0.append(r6)
            java.lang.String r0 = r0.toString()
            b6.p.i(r5, r0)
            goto L7c
        L39:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            r8.<init>()
            throw r8
        L3f:
            java.lang.String r0 = "Unexpected start indicator reading extended header"
            b6.p.i(r5, r0)
        L44:
            r7.g(r4)
        L47:
            int r0 = r8.a()
            if (r0 <= 0) goto Ld8
            int r0 = r7.f24130c
            if (r0 == 0) goto Lcf
            r5 = 0
            if (r0 == r4) goto Lb7
            if (r0 == r3) goto L88
            if (r0 != r2) goto L82
            int r0 = r8.a()
            int r6 = r7.f24137j
            if (r6 != r1) goto L61
            goto L63
        L61:
            int r5 = r0 - r6
        L63:
            if (r5 <= 0) goto L6e
            int r0 = r0 - r5
            int r5 = r8.f()
            int r5 = r5 + r0
            r8.T(r5)
        L6e:
            x4.m r5 = r7.f24128a
            r5.b(r8)
            int r5 = r7.f24137j
            if (r5 == r1) goto L47
            int r5 = r5 - r0
            r7.f24137j = r5
            if (r5 != 0) goto L47
        L7c:
            x4.m r0 = r7.f24128a
            r0.e()
            goto L44
        L82:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            r8.<init>()
            throw r8
        L88:
            r0 = 10
            int r6 = r7.f24136i
            int r0 = java.lang.Math.min(r0, r6)
            b6.y r6 = r7.f24129b
            byte[] r6 = r6.f8152a
            boolean r0 = r7.d(r8, r6, r0)
            if (r0 == 0) goto L47
            r0 = 0
            int r6 = r7.f24136i
            boolean r0 = r7.d(r8, r0, r6)
            if (r0 == 0) goto L47
            r7.f()
            boolean r0 = r7.f24138k
            if (r0 == 0) goto Lab
            r5 = 4
        Lab:
            r9 = r9 | r5
            x4.m r0 = r7.f24128a
            long r5 = r7.f24139l
            r0.f(r5, r9)
            r7.g(r2)
            goto L47
        Lb7:
            b6.y r0 = r7.f24129b
            byte[] r0 = r0.f8152a
            r6 = 9
            boolean r0 = r7.d(r8, r0, r6)
            if (r0 == 0) goto L47
            boolean r0 = r7.e()
            if (r0 == 0) goto Lca
            r5 = r3
        Lca:
            r7.g(r5)
            goto L47
        Lcf:
            int r0 = r8.a()
            r8.V(r0)
            goto L47
        Ld8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: x4.w.b(b6.z, int):void");
    }

    @Override // x4.i0
    public final void c() {
        this.f24130c = 0;
        this.f24131d = 0;
        this.f24135h = false;
        this.f24128a.c();
    }
}
