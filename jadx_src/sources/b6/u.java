package b6;

import com.google.android.libraries.barhopper.RecognitionOptions;
import java.nio.ByteBuffer;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u {

    /* renamed from: a  reason: collision with root package name */
    public static final byte[] f8109a = {0, 0, 0, 1};

    /* renamed from: b  reason: collision with root package name */
    public static final float[] f8110b = {1.0f, 1.0f, 1.0909091f, 0.90909094f, 1.4545455f, 1.2121212f, 2.1818182f, 1.8181819f, 2.909091f, 2.4242425f, 1.6363636f, 1.3636364f, 1.939394f, 1.6161616f, 1.3333334f, 1.5f, 2.0f};

    /* renamed from: c  reason: collision with root package name */
    private static final Object f8111c = new Object();

    /* renamed from: d  reason: collision with root package name */
    private static int[] f8112d = new int[10];

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final int f8113a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean f8114b;

        /* renamed from: c  reason: collision with root package name */
        public final int f8115c;

        /* renamed from: d  reason: collision with root package name */
        public final int f8116d;

        /* renamed from: e  reason: collision with root package name */
        public final int[] f8117e;

        /* renamed from: f  reason: collision with root package name */
        public final int f8118f;

        /* renamed from: g  reason: collision with root package name */
        public final int f8119g;

        /* renamed from: h  reason: collision with root package name */
        public final int f8120h;

        /* renamed from: i  reason: collision with root package name */
        public final int f8121i;

        /* renamed from: j  reason: collision with root package name */
        public final float f8122j;

        /* renamed from: k  reason: collision with root package name */
        public final int f8123k;

        /* renamed from: l  reason: collision with root package name */
        public final int f8124l;

        /* renamed from: m  reason: collision with root package name */
        public final int f8125m;

        public a(int i8, boolean z4, int i9, int i10, int[] iArr, int i11, int i12, int i13, int i14, float f5, int i15, int i16, int i17) {
            this.f8113a = i8;
            this.f8114b = z4;
            this.f8115c = i9;
            this.f8116d = i10;
            this.f8117e = iArr;
            this.f8118f = i11;
            this.f8119g = i12;
            this.f8120h = i13;
            this.f8121i = i14;
            this.f8122j = f5;
            this.f8123k = i15;
            this.f8124l = i16;
            this.f8125m = i17;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final int f8126a;

        /* renamed from: b  reason: collision with root package name */
        public final int f8127b;

        /* renamed from: c  reason: collision with root package name */
        public final boolean f8128c;

        public b(int i8, int i9, boolean z4) {
            this.f8126a = i8;
            this.f8127b = i9;
            this.f8128c = z4;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        public final int f8129a;

        /* renamed from: b  reason: collision with root package name */
        public final int f8130b;

        /* renamed from: c  reason: collision with root package name */
        public final int f8131c;

        /* renamed from: d  reason: collision with root package name */
        public final int f8132d;

        /* renamed from: e  reason: collision with root package name */
        public final int f8133e;

        /* renamed from: f  reason: collision with root package name */
        public final int f8134f;

        /* renamed from: g  reason: collision with root package name */
        public final int f8135g;

        /* renamed from: h  reason: collision with root package name */
        public final float f8136h;

        /* renamed from: i  reason: collision with root package name */
        public final boolean f8137i;

        /* renamed from: j  reason: collision with root package name */
        public final boolean f8138j;

        /* renamed from: k  reason: collision with root package name */
        public final int f8139k;

        /* renamed from: l  reason: collision with root package name */
        public final int f8140l;

        /* renamed from: m  reason: collision with root package name */
        public final int f8141m;

        /* renamed from: n  reason: collision with root package name */
        public final boolean f8142n;

        public c(int i8, int i9, int i10, int i11, int i12, int i13, int i14, float f5, boolean z4, boolean z8, int i15, int i16, int i17, boolean z9) {
            this.f8129a = i8;
            this.f8130b = i9;
            this.f8131c = i10;
            this.f8132d = i11;
            this.f8133e = i12;
            this.f8134f = i13;
            this.f8135g = i14;
            this.f8136h = f5;
            this.f8137i = z4;
            this.f8138j = z8;
            this.f8139k = i15;
            this.f8140l = i16;
            this.f8141m = i17;
            this.f8142n = z9;
        }
    }

    public static void a(boolean[] zArr) {
        zArr[0] = false;
        zArr[1] = false;
        zArr[2] = false;
    }

    public static void b(ByteBuffer byteBuffer) {
        int position = byteBuffer.position();
        int i8 = 0;
        int i9 = 0;
        while (true) {
            int i10 = i8 + 1;
            if (i10 >= position) {
                byteBuffer.clear();
                return;
            }
            int i11 = byteBuffer.get(i8) & 255;
            if (i9 == 3) {
                if (i11 == 1 && (byteBuffer.get(i10) & 31) == 7) {
                    ByteBuffer duplicate = byteBuffer.duplicate();
                    duplicate.position(i8 - 3);
                    duplicate.limit(position);
                    byteBuffer.position(0);
                    byteBuffer.put(duplicate);
                    return;
                }
            } else if (i11 == 0) {
                i9++;
            }
            if (i11 != 0) {
                i9 = 0;
            }
            i8 = i10;
        }
    }

    public static int c(byte[] bArr, int i8, int i9, boolean[] zArr) {
        int i10 = i9 - i8;
        b6.a.f(i10 >= 0);
        if (i10 == 0) {
            return i9;
        }
        if (zArr[0]) {
            a(zArr);
            return i8 - 3;
        } else if (i10 > 1 && zArr[1] && bArr[i8] == 1) {
            a(zArr);
            return i8 - 2;
        } else if (i10 > 2 && zArr[2] && bArr[i8] == 0 && bArr[i8 + 1] == 1) {
            a(zArr);
            return i8 - 1;
        } else {
            int i11 = i9 - 1;
            int i12 = i8 + 2;
            while (i12 < i11) {
                if ((bArr[i12] & 254) == 0) {
                    int i13 = i12 - 2;
                    if (bArr[i13] == 0 && bArr[i12 - 1] == 0 && bArr[i12] == 1) {
                        a(zArr);
                        return i13;
                    }
                    i12 -= 2;
                }
                i12 += 3;
            }
            zArr[0] = i10 <= 2 ? !(i10 != 2 ? !(zArr[1] && bArr[i11] == 1) : !(zArr[2] && bArr[i9 + (-2)] == 0 && bArr[i11] == 1)) : bArr[i9 + (-3)] == 0 && bArr[i9 + (-2)] == 0 && bArr[i11] == 1;
            zArr[1] = i10 <= 1 ? zArr[2] && bArr[i11] == 0 : bArr[i9 + (-2)] == 0 && bArr[i11] == 0;
            zArr[2] = bArr[i11] == 0;
            return i9;
        }
    }

    private static int d(byte[] bArr, int i8, int i9) {
        while (i8 < i9 - 2) {
            if (bArr[i8] == 0 && bArr[i8 + 1] == 0 && bArr[i8 + 2] == 3) {
                return i8;
            }
            i8++;
        }
        return i9;
    }

    public static int e(byte[] bArr, int i8) {
        return (bArr[i8 + 3] & 126) >> 1;
    }

    public static int f(byte[] bArr, int i8) {
        return bArr[i8 + 3] & 31;
    }

    public static boolean g(String str, byte b9) {
        if ("video/avc".equals(str) && (b9 & 31) == 6) {
            return true;
        }
        return "video/hevc".equals(str) && ((b9 & 126) >> 1) == 39;
    }

    public static a h(byte[] bArr, int i8, int i9) {
        return i(bArr, i8 + 2, i9);
    }

    /* JADX WARN: Removed duplicated region for block: B:79:0x0170  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0179  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01b6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static b6.u.a i(byte[] r23, int r24, int r25) {
        /*
            Method dump skipped, instructions count: 472
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: b6.u.i(byte[], int, int):b6.u$a");
    }

    public static b j(byte[] bArr, int i8, int i9) {
        return k(bArr, i8 + 1, i9);
    }

    public static b k(byte[] bArr, int i8, int i9) {
        a0 a0Var = new a0(bArr, i8, i9);
        int h8 = a0Var.h();
        int h9 = a0Var.h();
        a0Var.k();
        return new b(h8, h9, a0Var.d());
    }

    public static c l(byte[] bArr, int i8, int i9) {
        return m(bArr, i8 + 1, i9);
    }

    /* JADX WARN: Removed duplicated region for block: B:56:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0145  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static b6.u.c m(byte[] r22, int r23, int r24) {
        /*
            Method dump skipped, instructions count: 364
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: b6.u.m(byte[], int, int):b6.u$c");
    }

    private static void n(a0 a0Var) {
        for (int i8 = 0; i8 < 4; i8++) {
            int i9 = 0;
            while (i9 < 6) {
                int i10 = 1;
                if (a0Var.d()) {
                    int min = Math.min(64, 1 << ((i8 << 1) + 4));
                    if (i8 > 1) {
                        a0Var.g();
                    }
                    for (int i11 = 0; i11 < min; i11++) {
                        a0Var.g();
                    }
                } else {
                    a0Var.h();
                }
                if (i8 == 3) {
                    i10 = 3;
                }
                i9 += i10;
            }
        }
    }

    private static void o(a0 a0Var, int i8) {
        int i9 = 8;
        int i10 = 8;
        for (int i11 = 0; i11 < i8; i11++) {
            if (i9 != 0) {
                i9 = ((a0Var.g() + i10) + RecognitionOptions.QR_CODE) % RecognitionOptions.QR_CODE;
            }
            if (i9 != 0) {
                i10 = i9;
            }
        }
    }

    private static void p(a0 a0Var) {
        int h8 = a0Var.h();
        int[] iArr = new int[0];
        int[] iArr2 = new int[0];
        int i8 = -1;
        int i9 = 0;
        int i10 = -1;
        while (i9 < h8) {
            if (((i9 == 0 || !a0Var.d()) ? null : 1) != null) {
                int i11 = i8 + i10;
                int h9 = (1 - ((a0Var.d() ? 1 : 0) * 2)) * (a0Var.h() + 1);
                int i12 = i11 + 1;
                boolean[] zArr = new boolean[i12];
                for (int i13 = 0; i13 <= i11; i13++) {
                    if (a0Var.d()) {
                        zArr[i13] = true;
                    } else {
                        zArr[i13] = a0Var.d();
                    }
                }
                int[] iArr3 = new int[i12];
                int[] iArr4 = new int[i12];
                int i14 = 0;
                for (int i15 = i10 - 1; i15 >= 0; i15--) {
                    int i16 = iArr2[i15] + h9;
                    if (i16 < 0 && zArr[i8 + i15]) {
                        iArr3[i14] = i16;
                        i14++;
                    }
                }
                if (h9 < 0 && zArr[i11]) {
                    iArr3[i14] = h9;
                    i14++;
                }
                for (int i17 = 0; i17 < i8; i17++) {
                    int i18 = iArr[i17] + h9;
                    if (i18 < 0 && zArr[i17]) {
                        iArr3[i14] = i18;
                        i14++;
                    }
                }
                int[] copyOf = Arrays.copyOf(iArr3, i14);
                int i19 = 0;
                for (int i20 = i8 - 1; i20 >= 0; i20--) {
                    int i21 = iArr[i20] + h9;
                    if (i21 > 0 && zArr[i20]) {
                        iArr4[i19] = i21;
                        i19++;
                    }
                }
                if (h9 > 0 && zArr[i11]) {
                    iArr4[i19] = h9;
                    i19++;
                }
                for (int i22 = 0; i22 < i10; i22++) {
                    int i23 = iArr2[i22] + h9;
                    if (i23 > 0 && zArr[i8 + i22]) {
                        iArr4[i19] = i23;
                        i19++;
                    }
                }
                iArr2 = Arrays.copyOf(iArr4, i19);
                iArr = copyOf;
                i8 = i14;
                i10 = i19;
            } else {
                int h10 = a0Var.h();
                int h11 = a0Var.h();
                int[] iArr5 = new int[h10];
                for (int i24 = 0; i24 < h10; i24++) {
                    iArr5[i24] = a0Var.h() + 1;
                    a0Var.k();
                }
                int[] iArr6 = new int[h11];
                for (int i25 = 0; i25 < h11; i25++) {
                    iArr6[i25] = a0Var.h() + 1;
                    a0Var.k();
                }
                i8 = h10;
                iArr = iArr5;
                i10 = h11;
                iArr2 = iArr6;
            }
            i9++;
        }
    }

    public static int q(byte[] bArr, int i8) {
        int i9;
        synchronized (f8111c) {
            int i10 = 0;
            int i11 = 0;
            while (i10 < i8) {
                try {
                    i10 = d(bArr, i10, i8);
                    if (i10 < i8) {
                        int[] iArr = f8112d;
                        if (iArr.length <= i11) {
                            f8112d = Arrays.copyOf(iArr, iArr.length * 2);
                        }
                        f8112d[i11] = i10;
                        i10 += 3;
                        i11++;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            i9 = i8 - i11;
            int i12 = 0;
            int i13 = 0;
            for (int i14 = 0; i14 < i11; i14++) {
                int i15 = f8112d[i14] - i13;
                System.arraycopy(bArr, i13, bArr, i12, i15);
                int i16 = i12 + i15;
                int i17 = i16 + 1;
                bArr[i16] = 0;
                i12 = i17 + 1;
                bArr[i17] = 0;
                i13 += i15 + 3;
            }
            System.arraycopy(bArr, i13, bArr, i12, i9 - i12);
        }
        return i9;
    }
}
