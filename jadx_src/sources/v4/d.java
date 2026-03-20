package v4;

import b6.l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final long[] f23211a;

        /* renamed from: b  reason: collision with root package name */
        public final int[] f23212b;

        /* renamed from: c  reason: collision with root package name */
        public final int f23213c;

        /* renamed from: d  reason: collision with root package name */
        public final long[] f23214d;

        /* renamed from: e  reason: collision with root package name */
        public final int[] f23215e;

        /* renamed from: f  reason: collision with root package name */
        public final long f23216f;

        private b(long[] jArr, int[] iArr, int i8, long[] jArr2, int[] iArr2, long j8) {
            this.f23211a = jArr;
            this.f23212b = iArr;
            this.f23213c = i8;
            this.f23214d = jArr2;
            this.f23215e = iArr2;
            this.f23216f = j8;
        }
    }

    public static b a(int i8, long[] jArr, int[] iArr, long j8) {
        int i9 = 8192 / i8;
        int i10 = 0;
        for (int i11 : iArr) {
            i10 += l0.l(i11, i9);
        }
        long[] jArr2 = new long[i10];
        int[] iArr2 = new int[i10];
        long[] jArr3 = new long[i10];
        int[] iArr3 = new int[i10];
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        for (int i15 = 0; i15 < iArr.length; i15++) {
            int i16 = iArr[i15];
            long j9 = jArr[i15];
            while (i16 > 0) {
                int min = Math.min(i9, i16);
                jArr2[i13] = j9;
                iArr2[i13] = i8 * min;
                i14 = Math.max(i14, iArr2[i13]);
                jArr3[i13] = i12 * j8;
                iArr3[i13] = 1;
                j9 += iArr2[i13];
                i12 += min;
                i16 -= min;
                i13++;
            }
        }
        return new b(jArr2, iArr2, i14, jArr3, iArr3, j8 * i12);
    }
}
