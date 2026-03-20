package h5;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c implements com.google.android.exoplayer2.source.w {

    /* renamed from: a  reason: collision with root package name */
    protected final com.google.android.exoplayer2.source.w[] f20270a;

    public c(com.google.android.exoplayer2.source.w[] wVarArr) {
        this.f20270a = wVarArr;
    }

    @Override // com.google.android.exoplayer2.source.w
    public final long b() {
        long j8 = Long.MAX_VALUE;
        for (com.google.android.exoplayer2.source.w wVar : this.f20270a) {
            long b9 = wVar.b();
            if (b9 != Long.MIN_VALUE) {
                j8 = Math.min(j8, b9);
            }
        }
        if (j8 == Long.MAX_VALUE) {
            return Long.MIN_VALUE;
        }
        return j8;
    }

    @Override // com.google.android.exoplayer2.source.w
    public boolean d(long j8) {
        com.google.android.exoplayer2.source.w[] wVarArr;
        boolean z4;
        boolean z8 = false;
        do {
            long b9 = b();
            if (b9 == Long.MIN_VALUE) {
                break;
            }
            z4 = false;
            for (com.google.android.exoplayer2.source.w wVar : this.f20270a) {
                long b10 = wVar.b();
                boolean z9 = b10 != Long.MIN_VALUE && b10 <= j8;
                if (b10 == b9 || z9) {
                    z4 |= wVar.d(j8);
                }
            }
            z8 |= z4;
        } while (z4);
        return z8;
    }

    @Override // com.google.android.exoplayer2.source.w
    public boolean f() {
        for (com.google.android.exoplayer2.source.w wVar : this.f20270a) {
            if (wVar.f()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.android.exoplayer2.source.w
    public final long g() {
        long j8 = Long.MAX_VALUE;
        for (com.google.android.exoplayer2.source.w wVar : this.f20270a) {
            long g8 = wVar.g();
            if (g8 != Long.MIN_VALUE) {
                j8 = Math.min(j8, g8);
            }
        }
        if (j8 == Long.MAX_VALUE) {
            return Long.MIN_VALUE;
        }
        return j8;
    }

    @Override // com.google.android.exoplayer2.source.w
    public final void h(long j8) {
        for (com.google.android.exoplayer2.source.w wVar : this.f20270a) {
            wVar.h(j8);
        }
    }
}
