package y4;

import android.util.Pair;
import b6.l0;
import b6.z;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.w0;
import k4.w;
import n4.b0;
import n4.k;
import n4.l;
import n4.m;
import n4.p;
import n4.y;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b implements k {

    /* renamed from: h  reason: collision with root package name */
    public static final p f24378h = y4.a.b;

    /* renamed from: a  reason: collision with root package name */
    private m f24379a;

    /* renamed from: b  reason: collision with root package name */
    private b0 f24380b;

    /* renamed from: e  reason: collision with root package name */
    private InterfaceC0232b f24383e;

    /* renamed from: c  reason: collision with root package name */
    private int f24381c = 0;

    /* renamed from: d  reason: collision with root package name */
    private long f24382d = -1;

    /* renamed from: f  reason: collision with root package name */
    private int f24384f = -1;

    /* renamed from: g  reason: collision with root package name */
    private long f24385g = -1;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a implements InterfaceC0232b {

        /* renamed from: m  reason: collision with root package name */
        private static final int[] f24386m = {-1, -1, -1, -1, 2, 4, 6, 8, -1, -1, -1, -1, 2, 4, 6, 8};

        /* renamed from: n  reason: collision with root package name */
        private static final int[] f24387n = {7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 19, 21, 23, 25, 28, 31, 34, 37, 41, 45, 50, 55, 60, 66, 73, 80, 88, 97, 107, 118, 130, 143, 157, 173, 190, 209, 230, 253, 279, 307, 337, 371, 408, 449, 494, 544, 598, 658, 724, 796, 876, 963, 1060, 1166, 1282, 1411, 1552, 1707, 1878, 2066, 2272, 2499, 2749, 3024, 3327, 3660, 4026, 4428, 4871, 5358, 5894, 6484, 7132, 7845, 8630, 9493, 10442, 11487, 12635, 13899, 15289, 16818, 18500, 20350, 22385, 24623, 27086, 29794, 32767};

        /* renamed from: a  reason: collision with root package name */
        private final m f24388a;

        /* renamed from: b  reason: collision with root package name */
        private final b0 f24389b;

        /* renamed from: c  reason: collision with root package name */
        private final y4.c f24390c;

        /* renamed from: d  reason: collision with root package name */
        private final int f24391d;

        /* renamed from: e  reason: collision with root package name */
        private final byte[] f24392e;

        /* renamed from: f  reason: collision with root package name */
        private final z f24393f;

        /* renamed from: g  reason: collision with root package name */
        private final int f24394g;

        /* renamed from: h  reason: collision with root package name */
        private final w0 f24395h;

        /* renamed from: i  reason: collision with root package name */
        private int f24396i;

        /* renamed from: j  reason: collision with root package name */
        private long f24397j;

        /* renamed from: k  reason: collision with root package name */
        private int f24398k;

        /* renamed from: l  reason: collision with root package name */
        private long f24399l;

        public a(m mVar, b0 b0Var, y4.c cVar) {
            this.f24388a = mVar;
            this.f24389b = b0Var;
            this.f24390c = cVar;
            int max = Math.max(1, cVar.f24410c / 10);
            this.f24394g = max;
            z zVar = new z(cVar.f24414g);
            zVar.z();
            int z4 = zVar.z();
            this.f24391d = z4;
            int i8 = cVar.f24409b;
            int i9 = (((cVar.f24412e - (i8 * 4)) * 8) / (cVar.f24413f * i8)) + 1;
            if (z4 == i9) {
                int l8 = l0.l(max, z4);
                this.f24392e = new byte[cVar.f24412e * l8];
                this.f24393f = new z(l8 * h(z4, i8));
                int i10 = ((cVar.f24410c * cVar.f24412e) * 8) / z4;
                this.f24395h = new w0.b().g0("audio/raw").I(i10).b0(i10).Y(h(max, i8)).J(cVar.f24409b).h0(cVar.f24410c).a0(2).G();
                return;
            }
            throw ParserException.a("Expected frames per block: " + i9 + "; got: " + z4, null);
        }

        private void d(byte[] bArr, int i8, z zVar) {
            for (int i9 = 0; i9 < i8; i9++) {
                for (int i10 = 0; i10 < this.f24390c.f24409b; i10++) {
                    e(bArr, i9, i10, zVar.e());
                }
            }
            int g8 = g(this.f24391d * i8);
            zVar.U(0);
            zVar.T(g8);
        }

        private void e(byte[] bArr, int i8, int i9, byte[] bArr2) {
            y4.c cVar = this.f24390c;
            int i10 = cVar.f24412e;
            int i11 = cVar.f24409b;
            int i12 = (i8 * i10) + (i9 * 4);
            int i13 = (i11 * 4) + i12;
            int i14 = (i10 / i11) - 4;
            int i15 = (short) (((bArr[i12 + 1] & 255) << 8) | (bArr[i12] & 255));
            int min = Math.min(bArr[i12 + 2] & 255, 88);
            int i16 = f24387n[min];
            int i17 = ((i8 * this.f24391d * i11) + i9) * 2;
            bArr2[i17] = (byte) (i15 & 255);
            bArr2[i17 + 1] = (byte) (i15 >> 8);
            for (int i18 = 0; i18 < i14 * 2; i18++) {
                int i19 = bArr[((i18 / 8) * i11 * 4) + i13 + ((i18 / 2) % 4)] & 255;
                int i20 = i18 % 2 == 0 ? i19 & 15 : i19 >> 4;
                int i21 = ((((i20 & 7) * 2) + 1) * i16) >> 3;
                if ((i20 & 8) != 0) {
                    i21 = -i21;
                }
                i15 = l0.q(i15 + i21, -32768, 32767);
                i17 += i11 * 2;
                bArr2[i17] = (byte) (i15 & 255);
                bArr2[i17 + 1] = (byte) (i15 >> 8);
                int i22 = min + f24386m[i20];
                int[] iArr = f24387n;
                min = l0.q(i22, 0, iArr.length - 1);
                i16 = iArr[min];
            }
        }

        private int f(int i8) {
            return i8 / (this.f24390c.f24409b * 2);
        }

        private int g(int i8) {
            return h(i8, this.f24390c.f24409b);
        }

        private static int h(int i8, int i9) {
            return i8 * 2 * i9;
        }

        private void i(int i8) {
            long O0 = this.f24397j + l0.O0(this.f24399l, 1000000L, this.f24390c.f24410c);
            int g8 = g(i8);
            this.f24389b.d(O0, 1, g8, this.f24398k - g8, null);
            this.f24399l += i8;
            this.f24398k -= g8;
        }

        @Override // y4.b.InterfaceC0232b
        public void a(long j8) {
            this.f24396i = 0;
            this.f24397j = j8;
            this.f24398k = 0;
            this.f24399l = 0L;
        }

        @Override // y4.b.InterfaceC0232b
        public void b(int i8, long j8) {
            this.f24388a.m(new e(this.f24390c, this.f24391d, i8, j8));
            this.f24389b.f(this.f24395h);
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x0047  */
        /* JADX WARN: Removed duplicated region for block: B:7:0x0020  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:10:0x0035 -> B:4:0x001b). Please submit an issue!!! */
        @Override // y4.b.InterfaceC0232b
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean c(n4.l r7, long r8) {
            /*
                r6 = this;
                int r0 = r6.f24394g
                int r1 = r6.f24398k
                int r1 = r6.f(r1)
                int r0 = r0 - r1
                int r1 = r6.f24391d
                int r0 = b6.l0.l(r0, r1)
                y4.c r1 = r6.f24390c
                int r1 = r1.f24412e
                int r0 = r0 * r1
                r1 = 0
                int r1 = (r8 > r1 ? 1 : (r8 == r1 ? 0 : -1))
                r2 = 1
                if (r1 != 0) goto L1d
            L1b:
                r1 = r2
                goto L1e
            L1d:
                r1 = 0
            L1e:
                if (r1 != 0) goto L3e
                int r3 = r6.f24396i
                if (r3 >= r0) goto L3e
                int r3 = r0 - r3
                long r3 = (long) r3
                long r3 = java.lang.Math.min(r3, r8)
                int r3 = (int) r3
                byte[] r4 = r6.f24392e
                int r5 = r6.f24396i
                int r3 = r7.read(r4, r5, r3)
                r4 = -1
                if (r3 != r4) goto L38
                goto L1b
            L38:
                int r4 = r6.f24396i
                int r4 = r4 + r3
                r6.f24396i = r4
                goto L1e
            L3e:
                int r7 = r6.f24396i
                y4.c r8 = r6.f24390c
                int r8 = r8.f24412e
                int r7 = r7 / r8
                if (r7 <= 0) goto L75
                byte[] r8 = r6.f24392e
                b6.z r9 = r6.f24393f
                r6.d(r8, r7, r9)
                int r8 = r6.f24396i
                y4.c r9 = r6.f24390c
                int r9 = r9.f24412e
                int r7 = r7 * r9
                int r8 = r8 - r7
                r6.f24396i = r8
                b6.z r7 = r6.f24393f
                int r7 = r7.g()
                n4.b0 r8 = r6.f24389b
                b6.z r9 = r6.f24393f
                r8.b(r9, r7)
                int r8 = r6.f24398k
                int r8 = r8 + r7
                r6.f24398k = r8
                int r7 = r6.f(r8)
                int r8 = r6.f24394g
                if (r7 < r8) goto L75
                r6.i(r8)
            L75:
                if (r1 == 0) goto L82
                int r7 = r6.f24398k
                int r7 = r6.f(r7)
                if (r7 <= 0) goto L82
                r6.i(r7)
            L82:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: y4.b.a.c(n4.l, long):boolean");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: y4.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface InterfaceC0232b {
        void a(long j8);

        void b(int i8, long j8);

        boolean c(l lVar, long j8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c implements InterfaceC0232b {

        /* renamed from: a  reason: collision with root package name */
        private final m f24400a;

        /* renamed from: b  reason: collision with root package name */
        private final b0 f24401b;

        /* renamed from: c  reason: collision with root package name */
        private final y4.c f24402c;

        /* renamed from: d  reason: collision with root package name */
        private final w0 f24403d;

        /* renamed from: e  reason: collision with root package name */
        private final int f24404e;

        /* renamed from: f  reason: collision with root package name */
        private long f24405f;

        /* renamed from: g  reason: collision with root package name */
        private int f24406g;

        /* renamed from: h  reason: collision with root package name */
        private long f24407h;

        public c(m mVar, b0 b0Var, y4.c cVar, String str, int i8) {
            this.f24400a = mVar;
            this.f24401b = b0Var;
            this.f24402c = cVar;
            int i9 = (cVar.f24409b * cVar.f24413f) / 8;
            if (cVar.f24412e == i9) {
                int i10 = cVar.f24410c;
                int i11 = i10 * i9 * 8;
                int max = Math.max(i9, (i10 * i9) / 10);
                this.f24404e = max;
                this.f24403d = new w0.b().g0(str).I(i11).b0(i11).Y(max).J(cVar.f24409b).h0(cVar.f24410c).a0(i8).G();
                return;
            }
            throw ParserException.a("Expected block size: " + i9 + "; got: " + cVar.f24412e, null);
        }

        @Override // y4.b.InterfaceC0232b
        public void a(long j8) {
            this.f24405f = j8;
            this.f24406g = 0;
            this.f24407h = 0L;
        }

        @Override // y4.b.InterfaceC0232b
        public void b(int i8, long j8) {
            this.f24400a.m(new e(this.f24402c, 1, i8, j8));
            this.f24401b.f(this.f24403d);
        }

        @Override // y4.b.InterfaceC0232b
        public boolean c(l lVar, long j8) {
            int i8;
            y4.c cVar;
            int i9;
            int i10;
            long j9 = j8;
            while (true) {
                i8 = (j9 > 0L ? 1 : (j9 == 0L ? 0 : -1));
                if (i8 <= 0 || (i9 = this.f24406g) >= (i10 = this.f24404e)) {
                    break;
                }
                int c9 = this.f24401b.c(lVar, (int) Math.min(i10 - i9, j9), true);
                if (c9 == -1) {
                    j9 = 0;
                } else {
                    this.f24406g += c9;
                    j9 -= c9;
                }
            }
            int i11 = this.f24402c.f24412e;
            int i12 = this.f24406g / i11;
            if (i12 > 0) {
                int i13 = i12 * i11;
                int i14 = this.f24406g - i13;
                this.f24401b.d(this.f24405f + l0.O0(this.f24407h, 1000000L, cVar.f24410c), 1, i13, i14, null);
                this.f24407h += i12;
                this.f24406g = i14;
            }
            return i8 <= 0;
        }
    }

    private void d() {
        b6.a.h(this.f24380b);
        l0.j(this.f24379a);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ k[] f() {
        return new k[]{new b()};
    }

    private void h(l lVar) {
        b6.a.f(lVar.getPosition() == 0);
        int i8 = this.f24384f;
        if (i8 != -1) {
            lVar.i(i8);
            this.f24381c = 4;
        } else if (!d.a(lVar)) {
            throw ParserException.a("Unsupported or unrecognized wav file type.", null);
        } else {
            lVar.i((int) (lVar.e() - lVar.getPosition()));
            this.f24381c = 1;
        }
    }

    private void i(l lVar) {
        InterfaceC0232b cVar;
        y4.c b9 = d.b(lVar);
        int i8 = b9.f24408a;
        if (i8 == 17) {
            cVar = new a(this.f24379a, this.f24380b, b9);
        } else if (i8 == 6) {
            cVar = new c(this.f24379a, this.f24380b, b9, "audio/g711-alaw", -1);
        } else if (i8 == 7) {
            cVar = new c(this.f24379a, this.f24380b, b9, "audio/g711-mlaw", -1);
        } else {
            int a9 = w.a(i8, b9.f24413f);
            if (a9 == 0) {
                throw ParserException.d("Unsupported WAV format type: " + b9.f24408a);
            }
            cVar = new c(this.f24379a, this.f24380b, b9, "audio/raw", a9);
        }
        this.f24383e = cVar;
        this.f24381c = 3;
    }

    private void j(l lVar) {
        this.f24382d = d.c(lVar);
        this.f24381c = 2;
    }

    private int k(l lVar) {
        b6.a.f(this.f24385g != -1);
        return ((InterfaceC0232b) b6.a.e(this.f24383e)).c(lVar, this.f24385g - lVar.getPosition()) ? -1 : 0;
    }

    private void l(l lVar) {
        Pair<Long, Long> e8 = d.e(lVar);
        this.f24384f = ((Long) e8.first).intValue();
        long longValue = ((Long) e8.second).longValue();
        long j8 = this.f24382d;
        if (j8 != -1 && longValue == 4294967295L) {
            longValue = j8;
        }
        this.f24385g = this.f24384f + longValue;
        long b9 = lVar.b();
        if (b9 != -1 && this.f24385g > b9) {
            b6.p.i("WavExtractor", "Data exceeds input length: " + this.f24385g + ", " + b9);
            this.f24385g = b9;
        }
        ((InterfaceC0232b) b6.a.e(this.f24383e)).b(this.f24384f, this.f24385g);
        this.f24381c = 4;
    }

    @Override // n4.k
    public void b(m mVar) {
        this.f24379a = mVar;
        this.f24380b = mVar.e(0, 1);
        mVar.o();
    }

    @Override // n4.k
    public void c(long j8, long j9) {
        this.f24381c = j8 == 0 ? 0 : 4;
        InterfaceC0232b interfaceC0232b = this.f24383e;
        if (interfaceC0232b != null) {
            interfaceC0232b.a(j9);
        }
    }

    @Override // n4.k
    public int e(l lVar, y yVar) {
        d();
        int i8 = this.f24381c;
        if (i8 == 0) {
            h(lVar);
            return 0;
        } else if (i8 == 1) {
            j(lVar);
            return 0;
        } else if (i8 == 2) {
            i(lVar);
            return 0;
        } else if (i8 == 3) {
            l(lVar);
            return 0;
        } else if (i8 == 4) {
            return k(lVar);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override // n4.k
    public boolean g(l lVar) {
        return d.a(lVar);
    }

    @Override // n4.k
    public void release() {
    }
}
