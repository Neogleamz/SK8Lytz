package i4;

import b6.l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i0 {

    /* renamed from: c  reason: collision with root package name */
    public static final i0 f20502c;

    /* renamed from: d  reason: collision with root package name */
    public static final i0 f20503d;

    /* renamed from: e  reason: collision with root package name */
    public static final i0 f20504e;

    /* renamed from: f  reason: collision with root package name */
    public static final i0 f20505f;

    /* renamed from: g  reason: collision with root package name */
    public static final i0 f20506g;

    /* renamed from: a  reason: collision with root package name */
    public final long f20507a;

    /* renamed from: b  reason: collision with root package name */
    public final long f20508b;

    static {
        i0 i0Var = new i0(0L, 0L);
        f20502c = i0Var;
        f20503d = new i0(Long.MAX_VALUE, Long.MAX_VALUE);
        f20504e = new i0(Long.MAX_VALUE, 0L);
        f20505f = new i0(0L, Long.MAX_VALUE);
        f20506g = i0Var;
    }

    public i0(long j8, long j9) {
        b6.a.a(j8 >= 0);
        b6.a.a(j9 >= 0);
        this.f20507a = j8;
        this.f20508b = j9;
    }

    public long a(long j8, long j9, long j10) {
        long j11 = this.f20507a;
        if (j11 == 0 && this.f20508b == 0) {
            return j8;
        }
        long V0 = l0.V0(j8, j11, Long.MIN_VALUE);
        long b9 = l0.b(j8, this.f20508b, Long.MAX_VALUE);
        boolean z4 = true;
        boolean z8 = V0 <= j9 && j9 <= b9;
        if (V0 > j10 || j10 > b9) {
            z4 = false;
        }
        return (z8 && z4) ? Math.abs(j9 - j8) <= Math.abs(j10 - j8) ? j9 : j10 : z8 ? j9 : z4 ? j10 : V0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || i0.class != obj.getClass()) {
            return false;
        }
        i0 i0Var = (i0) obj;
        return this.f20507a == i0Var.f20507a && this.f20508b == i0Var.f20508b;
    }

    public int hashCode() {
        return (((int) this.f20507a) * 31) + ((int) this.f20508b);
    }
}
