package b6;

import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q {

    /* renamed from: a  reason: collision with root package name */
    private int f8100a;

    /* renamed from: b  reason: collision with root package name */
    private long[] f8101b;

    public q() {
        this(32);
    }

    public q(int i8) {
        this.f8101b = new long[i8];
    }

    public void a(long j8) {
        int i8 = this.f8100a;
        long[] jArr = this.f8101b;
        if (i8 == jArr.length) {
            this.f8101b = Arrays.copyOf(jArr, i8 * 2);
        }
        long[] jArr2 = this.f8101b;
        int i9 = this.f8100a;
        this.f8100a = i9 + 1;
        jArr2[i9] = j8;
    }

    public long b(int i8) {
        if (i8 < 0 || i8 >= this.f8100a) {
            throw new IndexOutOfBoundsException("Invalid index " + i8 + ", size is " + this.f8100a);
        }
        return this.f8101b[i8];
    }

    public int c() {
        return this.f8100a;
    }

    public long[] d() {
        return Arrays.copyOf(this.f8101b, this.f8100a);
    }
}
