package x4;

import b6.l0;
import b6.z;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.Arrays;
import java.util.Collections;
import x4.i0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o implements m {

    /* renamed from: l  reason: collision with root package name */
    private static final float[] f23981l = {1.0f, 1.0f, 1.0909091f, 0.90909094f, 1.4545455f, 1.2121212f, 1.0f};

    /* renamed from: a  reason: collision with root package name */
    private final k0 f23982a;

    /* renamed from: b  reason: collision with root package name */
    private final z f23983b;

    /* renamed from: e  reason: collision with root package name */
    private final u f23986e;

    /* renamed from: f  reason: collision with root package name */
    private b f23987f;

    /* renamed from: g  reason: collision with root package name */
    private long f23988g;

    /* renamed from: h  reason: collision with root package name */
    private String f23989h;

    /* renamed from: i  reason: collision with root package name */
    private n4.b0 f23990i;

    /* renamed from: j  reason: collision with root package name */
    private boolean f23991j;

    /* renamed from: c  reason: collision with root package name */
    private final boolean[] f23984c = new boolean[4];

    /* renamed from: d  reason: collision with root package name */
    private final a f23985d = new a(RecognitionOptions.ITF);

    /* renamed from: k  reason: collision with root package name */
    private long f23992k = -9223372036854775807L;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: f  reason: collision with root package name */
        private static final byte[] f23993f = {0, 0, 1};

        /* renamed from: a  reason: collision with root package name */
        private boolean f23994a;

        /* renamed from: b  reason: collision with root package name */
        private int f23995b;

        /* renamed from: c  reason: collision with root package name */
        public int f23996c;

        /* renamed from: d  reason: collision with root package name */
        public int f23997d;

        /* renamed from: e  reason: collision with root package name */
        public byte[] f23998e;

        public a(int i8) {
            this.f23998e = new byte[i8];
        }

        public void a(byte[] bArr, int i8, int i9) {
            if (this.f23994a) {
                int i10 = i9 - i8;
                byte[] bArr2 = this.f23998e;
                int length = bArr2.length;
                int i11 = this.f23996c;
                if (length < i11 + i10) {
                    this.f23998e = Arrays.copyOf(bArr2, (i11 + i10) * 2);
                }
                System.arraycopy(bArr, i8, this.f23998e, this.f23996c, i10);
                this.f23996c += i10;
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:26:0x003f, code lost:
            if (r9 != 181) goto L24;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean b(int r9, int r10) {
            /*
                r8 = this;
                int r0 = r8.f23995b
                r1 = 0
                r2 = 1
                if (r0 == 0) goto L4b
                r3 = 181(0xb5, float:2.54E-43)
                r4 = 2
                java.lang.String r5 = "Unexpected start code value"
                java.lang.String r6 = "H263Reader"
                if (r0 == r2) goto L3f
                r7 = 3
                if (r0 == r4) goto L37
                r4 = 4
                if (r0 == r7) goto L2b
                if (r0 != r4) goto L25
                r0 = 179(0xb3, float:2.51E-43)
                if (r9 == r0) goto L1d
                if (r9 != r3) goto L53
            L1d:
                int r9 = r8.f23996c
                int r9 = r9 - r10
                r8.f23996c = r9
                r8.f23994a = r1
                return r2
            L25:
                java.lang.IllegalStateException r9 = new java.lang.IllegalStateException
                r9.<init>()
                throw r9
            L2b:
                r9 = r9 & 240(0xf0, float:3.36E-43)
                r10 = 32
                if (r9 == r10) goto L32
                goto L41
            L32:
                int r9 = r8.f23996c
                r8.f23997d = r9
                goto L48
            L37:
                r10 = 31
                if (r9 <= r10) goto L3c
                goto L41
            L3c:
                r8.f23995b = r7
                goto L53
            L3f:
                if (r9 == r3) goto L48
            L41:
                b6.p.i(r6, r5)
                r8.c()
                goto L53
            L48:
                r8.f23995b = r4
                goto L53
            L4b:
                r10 = 176(0xb0, float:2.47E-43)
                if (r9 != r10) goto L53
                r8.f23995b = r2
                r8.f23994a = r2
            L53:
                byte[] r9 = x4.o.a.f23993f
                int r10 = r9.length
                r8.a(r9, r1, r10)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: x4.o.a.b(int, int):boolean");
        }

        public void c() {
            this.f23994a = false;
            this.f23996c = 0;
            this.f23995b = 0;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b {

        /* renamed from: a  reason: collision with root package name */
        private final n4.b0 f23999a;

        /* renamed from: b  reason: collision with root package name */
        private boolean f24000b;

        /* renamed from: c  reason: collision with root package name */
        private boolean f24001c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f24002d;

        /* renamed from: e  reason: collision with root package name */
        private int f24003e;

        /* renamed from: f  reason: collision with root package name */
        private int f24004f;

        /* renamed from: g  reason: collision with root package name */
        private long f24005g;

        /* renamed from: h  reason: collision with root package name */
        private long f24006h;

        public b(n4.b0 b0Var) {
            this.f23999a = b0Var;
        }

        public void a(byte[] bArr, int i8, int i9) {
            if (this.f24001c) {
                int i10 = this.f24004f;
                int i11 = (i8 + 1) - i10;
                if (i11 >= i9) {
                    this.f24004f = i10 + (i9 - i8);
                    return;
                }
                this.f24002d = ((bArr[i11] & 192) >> 6) == 0;
                this.f24001c = false;
            }
        }

        public void b(long j8, int i8, boolean z4) {
            if (this.f24003e == 182 && z4 && this.f24000b) {
                long j9 = this.f24006h;
                if (j9 != -9223372036854775807L) {
                    this.f23999a.d(j9, this.f24002d ? 1 : 0, (int) (j8 - this.f24005g), i8, null);
                }
            }
            if (this.f24003e != 179) {
                this.f24005g = j8;
            }
        }

        public void c(int i8, long j8) {
            this.f24003e = i8;
            this.f24002d = false;
            this.f24000b = i8 == 182 || i8 == 179;
            this.f24001c = i8 == 182;
            this.f24004f = 0;
            this.f24006h = j8;
        }

        public void d() {
            this.f24000b = false;
            this.f24001c = false;
            this.f24002d = false;
            this.f24003e = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public o(k0 k0Var) {
        z zVar;
        this.f23982a = k0Var;
        if (k0Var != null) {
            this.f23986e = new u(178, RecognitionOptions.ITF);
            zVar = new z();
        } else {
            zVar = null;
            this.f23986e = null;
        }
        this.f23983b = zVar;
    }

    private static w0 a(a aVar, int i8, String str) {
        byte[] copyOf = Arrays.copyOf(aVar.f23998e, aVar.f23996c);
        b6.y yVar = new b6.y(copyOf);
        yVar.s(i8);
        yVar.s(4);
        yVar.q();
        yVar.r(8);
        if (yVar.g()) {
            yVar.r(4);
            yVar.r(3);
        }
        int h8 = yVar.h(4);
        float f5 = 1.0f;
        if (h8 == 15) {
            int h9 = yVar.h(8);
            int h10 = yVar.h(8);
            if (h10 != 0) {
                f5 = h9 / h10;
            }
            b6.p.i("H263Reader", "Invalid aspect ratio");
        } else {
            float[] fArr = f23981l;
            if (h8 < fArr.length) {
                f5 = fArr[h8];
            }
            b6.p.i("H263Reader", "Invalid aspect ratio");
        }
        if (yVar.g()) {
            yVar.r(2);
            yVar.r(1);
            if (yVar.g()) {
                yVar.r(15);
                yVar.q();
                yVar.r(15);
                yVar.q();
                yVar.r(15);
                yVar.q();
                yVar.r(3);
                yVar.r(11);
                yVar.q();
                yVar.r(15);
                yVar.q();
            }
        }
        if (yVar.h(2) != 0) {
            b6.p.i("H263Reader", "Unhandled video object layer shape");
        }
        yVar.q();
        int h11 = yVar.h(16);
        yVar.q();
        if (yVar.g()) {
            if (h11 == 0) {
                b6.p.i("H263Reader", "Invalid vop_increment_time_resolution");
            } else {
                int i9 = 0;
                for (int i10 = h11 - 1; i10 > 0; i10 >>= 1) {
                    i9++;
                }
                yVar.r(i9);
            }
        }
        yVar.q();
        int h12 = yVar.h(13);
        yVar.q();
        int h13 = yVar.h(13);
        yVar.q();
        yVar.q();
        return new w0.b().U(str).g0("video/mp4v-es").n0(h12).S(h13).c0(f5).V(Collections.singletonList(copyOf)).G();
    }

    @Override // x4.m
    public void b(z zVar) {
        b6.a.h(this.f23987f);
        b6.a.h(this.f23990i);
        int f5 = zVar.f();
        int g8 = zVar.g();
        byte[] e8 = zVar.e();
        this.f23988g += zVar.a();
        this.f23990i.b(zVar, zVar.a());
        while (true) {
            int c9 = b6.u.c(e8, f5, g8, this.f23984c);
            if (c9 == g8) {
                break;
            }
            int i8 = c9 + 3;
            int i9 = zVar.e()[i8] & 255;
            int i10 = c9 - f5;
            int i11 = 0;
            if (!this.f23991j) {
                if (i10 > 0) {
                    this.f23985d.a(e8, f5, c9);
                }
                if (this.f23985d.b(i9, i10 < 0 ? -i10 : 0)) {
                    n4.b0 b0Var = this.f23990i;
                    a aVar = this.f23985d;
                    b0Var.f(a(aVar, aVar.f23997d, (String) b6.a.e(this.f23989h)));
                    this.f23991j = true;
                }
            }
            this.f23987f.a(e8, f5, c9);
            u uVar = this.f23986e;
            if (uVar != null) {
                if (i10 > 0) {
                    uVar.a(e8, f5, c9);
                } else {
                    i11 = -i10;
                }
                if (this.f23986e.b(i11)) {
                    u uVar2 = this.f23986e;
                    ((z) l0.j(this.f23983b)).S(this.f23986e.f24123d, b6.u.q(uVar2.f24123d, uVar2.f24124e));
                    ((k0) l0.j(this.f23982a)).a(this.f23992k, this.f23983b);
                }
                if (i9 == 178 && zVar.e()[c9 + 2] == 1) {
                    this.f23986e.e(i9);
                }
            }
            int i12 = g8 - c9;
            this.f23987f.b(this.f23988g - i12, i12, this.f23991j);
            this.f23987f.c(i9, this.f23992k);
            f5 = i8;
        }
        if (!this.f23991j) {
            this.f23985d.a(e8, f5, g8);
        }
        this.f23987f.a(e8, f5, g8);
        u uVar3 = this.f23986e;
        if (uVar3 != null) {
            uVar3.a(e8, f5, g8);
        }
    }

    @Override // x4.m
    public void c() {
        b6.u.a(this.f23984c);
        this.f23985d.c();
        b bVar = this.f23987f;
        if (bVar != null) {
            bVar.d();
        }
        u uVar = this.f23986e;
        if (uVar != null) {
            uVar.d();
        }
        this.f23988g = 0L;
        this.f23992k = -9223372036854775807L;
    }

    @Override // x4.m
    public void d(n4.m mVar, i0.d dVar) {
        dVar.a();
        this.f23989h = dVar.b();
        n4.b0 e8 = mVar.e(dVar.c(), 2);
        this.f23990i = e8;
        this.f23987f = new b(e8);
        k0 k0Var = this.f23982a;
        if (k0Var != null) {
            k0Var.b(mVar, dVar);
        }
    }

    @Override // x4.m
    public void e() {
    }

    @Override // x4.m
    public void f(long j8, int i8) {
        if (j8 != -9223372036854775807L) {
            this.f23992k = j8;
        }
    }
}
