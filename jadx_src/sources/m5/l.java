package m5;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.source.hls.SampleQueueMappingException;
import i4.s;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class l implements h5.r {

    /* renamed from: a  reason: collision with root package name */
    private final int f21918a;

    /* renamed from: b  reason: collision with root package name */
    private final p f21919b;

    /* renamed from: c  reason: collision with root package name */
    private int f21920c = -1;

    public l(p pVar, int i8) {
        this.f21919b = pVar;
        this.f21918a = i8;
    }

    private boolean c() {
        int i8 = this.f21920c;
        return (i8 == -1 || i8 == -3 || i8 == -2) ? false : true;
    }

    @Override // h5.r
    public void a() {
        int i8 = this.f21920c;
        if (i8 == -2) {
            throw new SampleQueueMappingException(this.f21919b.r().b(this.f21918a).b(0).f11207m);
        }
        if (i8 == -1) {
            this.f21919b.U();
        } else if (i8 != -3) {
            this.f21919b.V(i8);
        }
    }

    public void b() {
        b6.a.a(this.f21920c == -1);
        this.f21920c = this.f21919b.y(this.f21918a);
    }

    public void d() {
        if (this.f21920c != -1) {
            this.f21919b.p0(this.f21918a);
            this.f21920c = -1;
        }
    }

    @Override // h5.r
    public boolean e() {
        return this.f21920c == -3 || (c() && this.f21919b.Q(this.f21920c));
    }

    @Override // h5.r
    public int m(long j8) {
        if (c()) {
            return this.f21919b.o0(this.f21920c, j8);
        }
        return 0;
    }

    @Override // h5.r
    public int o(s sVar, DecoderInputBuffer decoderInputBuffer, int i8) {
        if (this.f21920c == -3) {
            decoderInputBuffer.j(4);
            return -4;
        } else if (c()) {
            return this.f21919b.e0(this.f21920c, sVar, decoderInputBuffer, i8);
        } else {
            return -3;
        }
    }
}
