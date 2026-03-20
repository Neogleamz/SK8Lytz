package b6;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h0 {

    /* renamed from: a  reason: collision with root package name */
    private long f8051a;

    /* renamed from: b  reason: collision with root package name */
    private long f8052b;

    /* renamed from: c  reason: collision with root package name */
    private long f8053c;

    /* renamed from: d  reason: collision with root package name */
    private final ThreadLocal<Long> f8054d = new ThreadLocal<>();

    public h0(long j8) {
        g(j8);
    }

    public static long f(long j8) {
        return (j8 * 1000000) / 90000;
    }

    public static long i(long j8) {
        return (j8 * 90000) / 1000000;
    }

    public static long j(long j8) {
        return i(j8) % 8589934592L;
    }

    public synchronized long a(long j8) {
        if (j8 == -9223372036854775807L) {
            return -9223372036854775807L;
        }
        if (this.f8052b == -9223372036854775807L) {
            long j9 = this.f8051a;
            if (j9 == 9223372036854775806L) {
                j9 = ((Long) a.e(this.f8054d.get())).longValue();
            }
            this.f8052b = j9 - j8;
            notifyAll();
        }
        this.f8053c = j8;
        return j8 + this.f8052b;
    }

    public synchronized long b(long j8) {
        if (j8 == -9223372036854775807L) {
            return -9223372036854775807L;
        }
        long j9 = this.f8053c;
        if (j9 != -9223372036854775807L) {
            long i8 = i(j9);
            long j10 = (4294967296L + i8) / 8589934592L;
            long j11 = ((j10 - 1) * 8589934592L) + j8;
            j8 += j10 * 8589934592L;
            if (Math.abs(j11 - i8) < Math.abs(j8 - i8)) {
                j8 = j11;
            }
        }
        return a(f(j8));
    }

    public synchronized long c() {
        long j8;
        j8 = this.f8051a;
        return (j8 == Long.MAX_VALUE || j8 == 9223372036854775806L) ? -9223372036854775807L : -9223372036854775807L;
    }

    public synchronized long d() {
        long j8;
        j8 = this.f8053c;
        return j8 != -9223372036854775807L ? j8 + this.f8052b : c();
    }

    public synchronized long e() {
        return this.f8052b;
    }

    public synchronized void g(long j8) {
        this.f8051a = j8;
        this.f8052b = j8 == Long.MAX_VALUE ? 0L : -9223372036854775807L;
        this.f8053c = -9223372036854775807L;
    }

    public synchronized void h(boolean z4, long j8) {
        a.f(this.f8051a == 9223372036854775806L);
        if (this.f8052b != -9223372036854775807L) {
            return;
        }
        if (z4) {
            this.f8054d.set(Long.valueOf(j8));
        } else {
            while (this.f8052b == -9223372036854775807L) {
                wait();
            }
        }
    }
}
