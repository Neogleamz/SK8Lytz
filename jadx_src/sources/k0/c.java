package k0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class c {

    /* renamed from: a  reason: collision with root package name */
    static final int[] f20866a = new int[0];

    /* renamed from: b  reason: collision with root package name */
    static final long[] f20867b = new long[0];

    /* renamed from: c  reason: collision with root package name */
    static final Object[] f20868c = new Object[0];

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(int[] iArr, int i8, int i9) {
        int i10 = i8 - 1;
        int i11 = 0;
        while (i11 <= i10) {
            int i12 = (i11 + i10) >>> 1;
            int i13 = iArr[i12];
            if (i13 < i9) {
                i11 = i12 + 1;
            } else if (i13 <= i9) {
                return i12;
            } else {
                i10 = i12 - 1;
            }
        }
        return ~i11;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int b(long[] jArr, int i8, long j8) {
        int i9 = i8 - 1;
        int i10 = 0;
        while (i10 <= i9) {
            int i11 = (i10 + i9) >>> 1;
            int i12 = (jArr[i11] > j8 ? 1 : (jArr[i11] == j8 ? 0 : -1));
            if (i12 < 0) {
                i10 = i11 + 1;
            } else if (i12 <= 0) {
                return i11;
            } else {
                i9 = i11 - 1;
            }
        }
        return ~i10;
    }

    public static boolean c(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static int d(int i8) {
        for (int i9 = 4; i9 < 32; i9++) {
            int i10 = (1 << i9) - 12;
            if (i8 <= i10) {
                return i10;
            }
        }
        return i8;
    }

    public static int e(int i8) {
        return d(i8 * 4) / 4;
    }

    public static int f(int i8) {
        return d(i8 * 8) / 8;
    }
}
