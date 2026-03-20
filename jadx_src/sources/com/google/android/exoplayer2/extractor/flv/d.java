package com.google.android.exoplayer2.extractor.flv;

import b6.u;
import b6.z;
import com.google.android.exoplayer2.extractor.flv.TagPayloadReader;
import com.google.android.exoplayer2.w0;
import n4.b0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d extends TagPayloadReader {

    /* renamed from: b  reason: collision with root package name */
    private final z f9688b;

    /* renamed from: c  reason: collision with root package name */
    private final z f9689c;

    /* renamed from: d  reason: collision with root package name */
    private int f9690d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f9691e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f9692f;

    /* renamed from: g  reason: collision with root package name */
    private int f9693g;

    public d(b0 b0Var) {
        super(b0Var);
        this.f9688b = new z(u.f8109a);
        this.f9689c = new z(4);
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    protected boolean b(z zVar) {
        int H = zVar.H();
        int i8 = (H >> 4) & 15;
        int i9 = H & 15;
        if (i9 == 7) {
            this.f9693g = i8;
            return i8 != 5;
        }
        throw new TagPayloadReader.UnsupportedFormatException("Video format not supported: " + i9);
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    protected boolean c(z zVar, long j8) {
        int H = zVar.H();
        long r4 = j8 + (zVar.r() * 1000);
        if (H == 0 && !this.f9691e) {
            z zVar2 = new z(new byte[zVar.a()]);
            zVar.l(zVar2.e(), 0, zVar.a());
            c6.a b9 = c6.a.b(zVar2);
            this.f9690d = b9.f8326b;
            this.f9664a.f(new w0.b().g0("video/avc").K(b9.f8330f).n0(b9.f8327c).S(b9.f8328d).c0(b9.f8329e).V(b9.f8325a).G());
            this.f9691e = true;
            return false;
        } else if (H == 1 && this.f9691e) {
            int i8 = this.f9693g == 1 ? 1 : 0;
            if (this.f9692f || i8 != 0) {
                byte[] e8 = this.f9689c.e();
                e8[0] = 0;
                e8[1] = 0;
                e8[2] = 0;
                int i9 = 4 - this.f9690d;
                int i10 = 0;
                while (zVar.a() > 0) {
                    zVar.l(this.f9689c.e(), i9, this.f9690d);
                    this.f9689c.U(0);
                    int L = this.f9689c.L();
                    this.f9688b.U(0);
                    this.f9664a.b(this.f9688b, 4);
                    this.f9664a.b(zVar, L);
                    i10 = i10 + 4 + L;
                }
                this.f9664a.d(r4, i8, i10, 0, null);
                this.f9692f = true;
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
}
