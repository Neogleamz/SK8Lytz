package u4;

import android.util.Pair;
import b6.l0;
import com.google.android.exoplayer2.metadata.id3.MlltFrame;
import n4.a0;
import n4.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c implements g {

    /* renamed from: a  reason: collision with root package name */
    private final long[] f22994a;

    /* renamed from: b  reason: collision with root package name */
    private final long[] f22995b;

    /* renamed from: c  reason: collision with root package name */
    private final long f22996c;

    private c(long[] jArr, long[] jArr2, long j8) {
        this.f22994a = jArr;
        this.f22995b = jArr2;
        this.f22996c = j8 == -9223372036854775807L ? l0.C0(jArr2[jArr2.length - 1]) : j8;
    }

    public static c b(long j8, MlltFrame mlltFrame, long j9) {
        int length = mlltFrame.f10116e.length;
        int i8 = length + 1;
        long[] jArr = new long[i8];
        long[] jArr2 = new long[i8];
        jArr[0] = j8;
        long j10 = 0;
        jArr2[0] = 0;
        for (int i9 = 1; i9 <= length; i9++) {
            int i10 = i9 - 1;
            j8 += mlltFrame.f10114c + mlltFrame.f10116e[i10];
            j10 += mlltFrame.f10115d + mlltFrame.f10117f[i10];
            jArr[i9] = j8;
            jArr2[i9] = j10;
        }
        return new c(jArr, jArr2, j9);
    }

    private static Pair<Long, Long> c(long j8, long[] jArr, long[] jArr2) {
        Long valueOf;
        Long valueOf2;
        int i8 = l0.i(jArr, j8, true, true);
        long j9 = jArr[i8];
        long j10 = jArr2[i8];
        int i9 = i8 + 1;
        if (i9 == jArr.length) {
            valueOf = Long.valueOf(j9);
            valueOf2 = Long.valueOf(j10);
        } else {
            long j11 = jArr[i9];
            long j12 = jArr2[i9];
            double d8 = j11 == j9 ? 0.0d : (j8 - j9) / (j11 - j9);
            valueOf = Long.valueOf(j8);
            valueOf2 = Long.valueOf(((long) (d8 * (j12 - j10))) + j10);
        }
        return Pair.create(valueOf, valueOf2);
    }

    @Override // u4.g
    public long a(long j8) {
        return l0.C0(((Long) c(j8, this.f22994a, this.f22995b).second).longValue());
    }

    @Override // n4.z
    public long d() {
        return this.f22996c;
    }

    @Override // u4.g
    public long f() {
        return -1L;
    }

    @Override // n4.z
    public boolean h() {
        return true;
    }

    @Override // n4.z
    public z.a i(long j8) {
        Pair<Long, Long> c9 = c(l0.a1(l0.r(j8, 0L, this.f22996c)), this.f22995b, this.f22994a);
        return new z.a(new a0(l0.C0(((Long) c9.first).longValue()), ((Long) c9.second).longValue()));
    }
}
