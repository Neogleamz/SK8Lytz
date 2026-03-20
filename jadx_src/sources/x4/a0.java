package x4;

import android.util.SparseArray;
import b6.z;
import com.google.android.libraries.barhopper.RecognitionOptions;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a0 implements n4.k {

    /* renamed from: l  reason: collision with root package name */
    public static final n4.p f23793l = z.b;

    /* renamed from: a  reason: collision with root package name */
    private final b6.h0 f23794a;

    /* renamed from: b  reason: collision with root package name */
    private final SparseArray<a> f23795b;

    /* renamed from: c  reason: collision with root package name */
    private final z f23796c;

    /* renamed from: d  reason: collision with root package name */
    private final y f23797d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f23798e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f23799f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f23800g;

    /* renamed from: h  reason: collision with root package name */
    private long f23801h;

    /* renamed from: i  reason: collision with root package name */
    private x f23802i;

    /* renamed from: j  reason: collision with root package name */
    private n4.m f23803j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f23804k;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final m f23805a;

        /* renamed from: b  reason: collision with root package name */
        private final b6.h0 f23806b;

        /* renamed from: c  reason: collision with root package name */
        private final b6.y f23807c = new b6.y(new byte[64]);

        /* renamed from: d  reason: collision with root package name */
        private boolean f23808d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f23809e;

        /* renamed from: f  reason: collision with root package name */
        private boolean f23810f;

        /* renamed from: g  reason: collision with root package name */
        private int f23811g;

        /* renamed from: h  reason: collision with root package name */
        private long f23812h;

        public a(m mVar, b6.h0 h0Var) {
            this.f23805a = mVar;
            this.f23806b = h0Var;
        }

        private void b() {
            this.f23807c.r(8);
            this.f23808d = this.f23807c.g();
            this.f23809e = this.f23807c.g();
            this.f23807c.r(6);
            this.f23811g = this.f23807c.h(8);
        }

        private void c() {
            this.f23812h = 0L;
            if (this.f23808d) {
                this.f23807c.r(4);
                this.f23807c.r(1);
                this.f23807c.r(1);
                long h8 = (this.f23807c.h(3) << 30) | (this.f23807c.h(15) << 15) | this.f23807c.h(15);
                this.f23807c.r(1);
                if (!this.f23810f && this.f23809e) {
                    this.f23807c.r(4);
                    this.f23807c.r(1);
                    this.f23807c.r(1);
                    this.f23807c.r(1);
                    this.f23806b.b((this.f23807c.h(3) << 30) | (this.f23807c.h(15) << 15) | this.f23807c.h(15));
                    this.f23810f = true;
                }
                this.f23812h = this.f23806b.b(h8);
            }
        }

        public void a(z zVar) {
            zVar.l(this.f23807c.f8152a, 0, 3);
            this.f23807c.p(0);
            b();
            zVar.l(this.f23807c.f8152a, 0, this.f23811g);
            this.f23807c.p(0);
            c();
            this.f23805a.f(this.f23812h, 4);
            this.f23805a.b(zVar);
            this.f23805a.e();
        }

        public void d() {
            this.f23810f = false;
            this.f23805a.c();
        }
    }

    public a0() {
        this(new b6.h0(0L));
    }

    public a0(b6.h0 h0Var) {
        this.f23794a = h0Var;
        this.f23796c = new z((int) RecognitionOptions.AZTEC);
        this.f23795b = new SparseArray<>();
        this.f23797d = new y();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ n4.k[] d() {
        return new n4.k[]{new a0()};
    }

    private void f(long j8) {
        n4.m mVar;
        n4.z bVar;
        if (this.f23804k) {
            return;
        }
        this.f23804k = true;
        if (this.f23797d.c() != -9223372036854775807L) {
            x xVar = new x(this.f23797d.d(), this.f23797d.c(), j8);
            this.f23802i = xVar;
            mVar = this.f23803j;
            bVar = xVar.b();
        } else {
            mVar = this.f23803j;
            bVar = new z.b(this.f23797d.c());
        }
        mVar.m(bVar);
    }

    @Override // n4.k
    public void b(n4.m mVar) {
        this.f23803j = mVar;
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        boolean z4 = true;
        boolean z8 = this.f23794a.e() == -9223372036854775807L;
        if (!z8) {
            long c9 = this.f23794a.c();
            z8 = (c9 == -9223372036854775807L || c9 == 0 || c9 == j9) ? false : false;
        }
        if (z8) {
            this.f23794a.g(j9);
        }
        x xVar = this.f23802i;
        if (xVar != null) {
            xVar.h(j9);
        }
        for (int i8 = 0; i8 < this.f23795b.size(); i8++) {
            this.f23795b.valueAt(i8).d();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x00f8  */
    @Override // n4.k
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int e(n4.l r10, n4.y r11) {
        /*
            Method dump skipped, instructions count: 365
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: x4.a0.e(n4.l, n4.y):int");
    }

    @Override // n4.k
    public boolean g(n4.l lVar) {
        byte[] bArr = new byte[14];
        lVar.k(bArr, 0, 14);
        if (442 == (((bArr[0] & 255) << 24) | ((bArr[1] & 255) << 16) | ((bArr[2] & 255) << 8) | (bArr[3] & 255)) && (bArr[4] & 196) == 68 && (bArr[6] & 4) == 4 && (bArr[8] & 4) == 4 && (bArr[9] & 1) == 1 && (bArr[12] & 3) == 3) {
            lVar.f(bArr[13] & 7);
            lVar.k(bArr, 0, 3);
            return 1 == ((((bArr[0] & 255) << 16) | ((bArr[1] & 255) << 8)) | (bArr[2] & 255));
        }
        return false;
    }

    @Override // n4.k
    public void release() {
    }
}
