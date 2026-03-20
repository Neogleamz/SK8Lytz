package j5;

import com.google.android.exoplayer2.w0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class n extends f {

    /* renamed from: j  reason: collision with root package name */
    public final long f20787j;

    public n(a6.h hVar, com.google.android.exoplayer2.upstream.a aVar, w0 w0Var, int i8, Object obj, long j8, long j9, long j10) {
        super(hVar, aVar, 1, w0Var, i8, obj, j8, j9);
        b6.a.e(w0Var);
        this.f20787j = j10;
    }

    public long g() {
        long j8 = this.f20787j;
        if (j8 != -1) {
            return 1 + j8;
        }
        return -1L;
    }

    public abstract boolean h();
}
