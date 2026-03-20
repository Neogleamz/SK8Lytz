package com.google.android.exoplayer2.source;

import android.net.Uri;
import b6.l0;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.source.j;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.w0;
import com.google.android.exoplayer2.z0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import i4.i0;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y extends com.google.android.exoplayer2.source.a {

    /* renamed from: k  reason: collision with root package name */
    private static final w0 f10836k;

    /* renamed from: l  reason: collision with root package name */
    private static final z0 f10837l;

    /* renamed from: m  reason: collision with root package name */
    private static final byte[] f10838m;

    /* renamed from: h  reason: collision with root package name */
    private final long f10839h;

    /* renamed from: j  reason: collision with root package name */
    private final z0 f10840j;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private long f10841a;

        /* renamed from: b  reason: collision with root package name */
        private Object f10842b;

        public y a() {
            b6.a.f(this.f10841a > 0);
            return new y(this.f10841a, y.f10837l.b().e(this.f10842b).a());
        }

        public b b(long j8) {
            this.f10841a = j8;
            return this;
        }

        public b c(Object obj) {
            this.f10842b = obj;
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class c implements j {

        /* renamed from: c  reason: collision with root package name */
        private static final h5.w f10843c = new h5.w(new h5.u(y.f10836k));

        /* renamed from: a  reason: collision with root package name */
        private final long f10844a;

        /* renamed from: b  reason: collision with root package name */
        private final ArrayList<h5.r> f10845b = new ArrayList<>();

        public c(long j8) {
            this.f10844a = j8;
        }

        private long a(long j8) {
            return l0.r(j8, 0L, this.f10844a);
        }

        @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
        public long b() {
            return Long.MIN_VALUE;
        }

        @Override // com.google.android.exoplayer2.source.j
        public long c(long j8, i0 i0Var) {
            return a(j8);
        }

        @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
        public boolean d(long j8) {
            return false;
        }

        @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
        public boolean f() {
            return false;
        }

        @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
        public long g() {
            return Long.MIN_VALUE;
        }

        @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
        public void h(long j8) {
        }

        @Override // com.google.android.exoplayer2.source.j
        public void l() {
        }

        @Override // com.google.android.exoplayer2.source.j
        public long n(long j8) {
            long a9 = a(j8);
            for (int i8 = 0; i8 < this.f10845b.size(); i8++) {
                ((d) this.f10845b.get(i8)).b(a9);
            }
            return a9;
        }

        @Override // com.google.android.exoplayer2.source.j
        public long p() {
            return -9223372036854775807L;
        }

        @Override // com.google.android.exoplayer2.source.j
        public void q(j.a aVar, long j8) {
            aVar.k(this);
        }

        @Override // com.google.android.exoplayer2.source.j
        public h5.w r() {
            return f10843c;
        }

        @Override // com.google.android.exoplayer2.source.j
        public long s(z5.r[] rVarArr, boolean[] zArr, h5.r[] rVarArr2, boolean[] zArr2, long j8) {
            long a9 = a(j8);
            for (int i8 = 0; i8 < rVarArr.length; i8++) {
                if (rVarArr2[i8] != null && (rVarArr[i8] == null || !zArr[i8])) {
                    this.f10845b.remove(rVarArr2[i8]);
                    rVarArr2[i8] = null;
                }
                if (rVarArr2[i8] == null && rVarArr[i8] != null) {
                    d dVar = new d(this.f10844a);
                    dVar.b(a9);
                    this.f10845b.add(dVar);
                    rVarArr2[i8] = dVar;
                    zArr2[i8] = true;
                }
            }
            return a9;
        }

        @Override // com.google.android.exoplayer2.source.j
        public void u(long j8, boolean z4) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class d implements h5.r {

        /* renamed from: a  reason: collision with root package name */
        private final long f10846a;

        /* renamed from: b  reason: collision with root package name */
        private boolean f10847b;

        /* renamed from: c  reason: collision with root package name */
        private long f10848c;

        public d(long j8) {
            this.f10846a = y.K(j8);
            b(0L);
        }

        @Override // h5.r
        public void a() {
        }

        public void b(long j8) {
            this.f10848c = l0.r(y.K(j8), 0L, this.f10846a);
        }

        @Override // h5.r
        public boolean e() {
            return true;
        }

        @Override // h5.r
        public int m(long j8) {
            long j9 = this.f10848c;
            b(j8);
            return (int) ((this.f10848c - j9) / y.f10838m.length);
        }

        @Override // h5.r
        public int o(i4.s sVar, DecoderInputBuffer decoderInputBuffer, int i8) {
            if (!this.f10847b || (i8 & 2) != 0) {
                sVar.f20512b = y.f10836k;
                this.f10847b = true;
                return -5;
            }
            long j8 = this.f10846a;
            long j9 = this.f10848c;
            long j10 = j8 - j9;
            if (j10 == 0) {
                decoderInputBuffer.j(4);
                return -4;
            }
            decoderInputBuffer.f9514e = y.L(j9);
            decoderInputBuffer.j(1);
            int min = (int) Math.min(y.f10838m.length, j10);
            if ((i8 & 4) == 0) {
                decoderInputBuffer.z(min);
                decoderInputBuffer.f9512c.put(y.f10838m, 0, min);
            }
            if ((i8 & 1) == 0) {
                this.f10848c += min;
            }
            return -4;
        }
    }

    static {
        w0 G = new w0.b().g0("audio/raw").J(2).h0(44100).a0(2).G();
        f10836k = G;
        f10837l = new z0.c().c("SilenceMediaSource").f(Uri.EMPTY).d(G.f11207m).a();
        f10838m = new byte[l0.d0(2, 2) * RecognitionOptions.UPC_E];
    }

    private y(long j8, z0 z0Var) {
        b6.a.a(j8 >= 0);
        this.f10839h = j8;
        this.f10840j = z0Var;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long K(long j8) {
        return l0.d0(2, 2) * ((j8 * 44100) / 1000000);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long L(long j8) {
        return ((j8 / l0.d0(2, 2)) * 1000000) / 44100;
    }

    @Override // com.google.android.exoplayer2.source.a
    protected void C(a6.y yVar) {
        D(new h5.s(this.f10839h, true, false, false, null, this.f10840j));
    }

    @Override // com.google.android.exoplayer2.source.a
    protected void E() {
    }

    @Override // com.google.android.exoplayer2.source.k
    public j b(k.b bVar, a6.b bVar2, long j8) {
        return new c(this.f10839h);
    }

    @Override // com.google.android.exoplayer2.source.k
    public z0 i() {
        return this.f10840j;
    }

    @Override // com.google.android.exoplayer2.source.k
    public void n() {
    }

    @Override // com.google.android.exoplayer2.source.k
    public void p(j jVar) {
    }
}
