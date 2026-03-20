package t4;

import n4.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g {

    /* renamed from: d  reason: collision with root package name */
    private static final long[] f22920d = {128, 64, 32, 16, 8, 4, 2, 1};

    /* renamed from: a  reason: collision with root package name */
    private final byte[] f22921a = new byte[8];

    /* renamed from: b  reason: collision with root package name */
    private int f22922b;

    /* renamed from: c  reason: collision with root package name */
    private int f22923c;

    public static long a(byte[] bArr, int i8, boolean z4) {
        long j8 = bArr[0] & 255;
        if (z4) {
            j8 &= ~f22920d[i8 - 1];
        }
        for (int i9 = 1; i9 < i8; i9++) {
            j8 = (j8 << 8) | (bArr[i9] & 255);
        }
        return j8;
    }

    public static int c(int i8) {
        int i9;
        int i10 = 0;
        do {
            long[] jArr = f22920d;
            if (i10 >= jArr.length) {
                return -1;
            }
            i9 = ((jArr[i10] & i8) > 0L ? 1 : ((jArr[i10] & i8) == 0L ? 0 : -1));
            i10++;
        } while (i9 == 0);
        return i10;
    }

    public int b() {
        return this.f22923c;
    }

    public long d(l lVar, boolean z4, boolean z8, int i8) {
        if (this.f22922b == 0) {
            if (!lVar.c(this.f22921a, 0, 1, z4)) {
                return -1L;
            }
            int c9 = c(this.f22921a[0] & 255);
            this.f22923c = c9;
            if (c9 == -1) {
                throw new IllegalStateException("No valid varint length mask found");
            }
            this.f22922b = 1;
        }
        int i9 = this.f22923c;
        if (i9 > i8) {
            this.f22922b = 0;
            return -2L;
        }
        if (i9 != 1) {
            lVar.readFully(this.f22921a, 1, i9 - 1);
        }
        this.f22922b = 0;
        return a(this.f22921a, this.f22923c, z8);
    }

    public void e() {
        this.f22922b = 0;
        this.f22923c = 0;
    }
}
