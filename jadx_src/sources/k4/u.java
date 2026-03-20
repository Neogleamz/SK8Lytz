package k4;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u {

    /* renamed from: a  reason: collision with root package name */
    private static final String[] f21014a = {"audio/mpeg-L1", "audio/mpeg-L2", "audio/mpeg"};

    /* renamed from: b  reason: collision with root package name */
    private static final int[] f21015b = {44100, 48000, 32000};

    /* renamed from: c  reason: collision with root package name */
    private static final int[] f21016c = {32000, 64000, 96000, 128000, 160000, 192000, 224000, 256000, 288000, 320000, 352000, 384000, 416000, 448000};

    /* renamed from: d  reason: collision with root package name */
    private static final int[] f21017d = {32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000, 176000, 192000, 224000, 256000};

    /* renamed from: e  reason: collision with root package name */
    private static final int[] f21018e = {32000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000, 384000};

    /* renamed from: f  reason: collision with root package name */
    private static final int[] f21019f = {32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000};

    /* renamed from: g  reason: collision with root package name */
    private static final int[] f21020g = {8000, 16000, 24000, 32000, 40000, 48000, 56000, 64000, 80000, 96000, 112000, 128000, 144000, 160000};

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public int f21021a;

        /* renamed from: b  reason: collision with root package name */
        public String f21022b;

        /* renamed from: c  reason: collision with root package name */
        public int f21023c;

        /* renamed from: d  reason: collision with root package name */
        public int f21024d;

        /* renamed from: e  reason: collision with root package name */
        public int f21025e;

        /* renamed from: f  reason: collision with root package name */
        public int f21026f;

        /* renamed from: g  reason: collision with root package name */
        public int f21027g;

        /* JADX WARN: Removed duplicated region for block: B:26:0x0052  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x0070  */
        /* JADX WARN: Removed duplicated region for block: B:43:0x00a6  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean a(int r9) {
            /*
                r8 = this;
                boolean r0 = k4.u.a(r9)
                r1 = 0
                if (r0 != 0) goto L8
                return r1
            L8:
                int r0 = r9 >>> 19
                r2 = 3
                r0 = r0 & r2
                r3 = 1
                if (r0 != r3) goto L10
                return r1
            L10:
                int r4 = r9 >>> 17
                r4 = r4 & r2
                if (r4 != 0) goto L16
                return r1
            L16:
                int r5 = r9 >>> 12
                r6 = 15
                r5 = r5 & r6
                if (r5 == 0) goto Laa
                if (r5 != r6) goto L21
                goto Laa
            L21:
                int r6 = r9 >>> 10
                r6 = r6 & r2
                if (r6 != r2) goto L27
                return r1
            L27:
                r8.f21021a = r0
                java.lang.String[] r1 = k4.u.b()
                int r7 = 3 - r4
                r1 = r1[r7]
                r8.f21022b = r1
                int[] r1 = k4.u.c()
                r1 = r1[r6]
                r8.f21024d = r1
                r6 = 2
                if (r0 != r6) goto L42
                int r1 = r1 / r6
            L3f:
                r8.f21024d = r1
                goto L47
            L42:
                if (r0 != 0) goto L47
                int r1 = r1 / 4
                goto L3f
            L47:
                int r1 = r9 >>> 9
                r1 = r1 & r3
                int r7 = k4.u.d(r0, r4)
                r8.f21027g = r7
                if (r4 != r2) goto L70
                if (r0 != r2) goto L5c
                int[] r0 = k4.u.e()
                int r5 = r5 - r3
                r0 = r0[r5]
                goto L63
            L5c:
                int[] r0 = k4.u.f()
                int r5 = r5 - r3
                r0 = r0[r5]
            L63:
                r8.f21026f = r0
                int r0 = r0 * 12
                int r4 = r8.f21024d
                int r0 = r0 / r4
                int r0 = r0 + r1
                int r0 = r0 * 4
            L6d:
                r8.f21023c = r0
                goto La1
            L70:
                r7 = 144(0x90, float:2.02E-43)
                if (r0 != r2) goto L8d
                if (r4 != r6) goto L7e
                int[] r0 = k4.u.g()
                int r5 = r5 - r3
                r0 = r0[r5]
                goto L85
            L7e:
                int[] r0 = k4.u.h()
                int r5 = r5 - r3
                r0 = r0[r5]
            L85:
                r8.f21026f = r0
                int r0 = r0 * r7
                int r4 = r8.f21024d
                int r0 = r0 / r4
                int r0 = r0 + r1
                goto L6d
            L8d:
                int[] r0 = k4.u.i()
                int r5 = r5 - r3
                r0 = r0[r5]
                r8.f21026f = r0
                if (r4 != r3) goto L9a
                r7 = 72
            L9a:
                int r7 = r7 * r0
                int r0 = r8.f21024d
                int r7 = r7 / r0
                int r7 = r7 + r1
                r8.f21023c = r7
            La1:
                int r9 = r9 >> 6
                r9 = r9 & r2
                if (r9 != r2) goto La7
                r6 = r3
            La7:
                r8.f21025e = r6
                return r3
            Laa:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: k4.u.a.a(int):boolean");
        }
    }

    public static int j(int i8) {
        int i9;
        int i10;
        int i11;
        int i12;
        if (!l(i8) || (i9 = (i8 >>> 19) & 3) == 1 || (i10 = (i8 >>> 17) & 3) == 0 || (i11 = (i8 >>> 12) & 15) == 0 || i11 == 15 || (i12 = (i8 >>> 10) & 3) == 3) {
            return -1;
        }
        int i13 = f21015b[i12];
        if (i9 == 2) {
            i13 /= 2;
        } else if (i9 == 0) {
            i13 /= 4;
        }
        int i14 = (i8 >>> 9) & 1;
        if (i10 == 3) {
            return ((((i9 == 3 ? f21016c[i11 - 1] : f21017d[i11 - 1]) * 12) / i13) + i14) * 4;
        }
        int i15 = i9 == 3 ? i10 == 2 ? f21018e[i11 - 1] : f21019f[i11 - 1] : f21020g[i11 - 1];
        if (i9 == 3) {
            return ((i15 * 144) / i13) + i14;
        }
        return (((i10 == 1 ? 72 : 144) * i15) / i13) + i14;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int k(int i8, int i9) {
        if (i9 == 1) {
            return i8 == 3 ? 1152 : 576;
        } else if (i9 != 2) {
            if (i9 == 3) {
                return 384;
            }
            throw new IllegalArgumentException();
        } else {
            return 1152;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean l(int i8) {
        return (i8 & (-2097152)) == -2097152;
    }

    public static int m(int i8) {
        int i9;
        int i10;
        if (!l(i8) || (i9 = (i8 >>> 19) & 3) == 1 || (i10 = (i8 >>> 17) & 3) == 0) {
            return -1;
        }
        int i11 = (i8 >>> 12) & 15;
        int i12 = (i8 >>> 10) & 3;
        if (i11 == 0 || i11 == 15 || i12 == 3) {
            return -1;
        }
        return k(i9, i10);
    }
}
