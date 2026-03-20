package m5;

import android.net.Uri;
import b6.h0;
import b6.j0;
import b6.z;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import com.google.android.exoplayer2.source.hls.playlist.d;
import com.google.android.exoplayer2.upstream.a;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.ImmutableList;
import j4.t1;
import j5.n;
import java.io.EOFException;
import java.io.InterruptedIOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import m5.f;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i extends n {
    private static final AtomicInteger M = new AtomicInteger();
    private final boolean A;
    private final boolean B;
    private final t1 C;
    private j D;
    private p E;
    private int F;
    private boolean G;
    private volatile boolean H;
    private boolean I;
    private ImmutableList<Integer> J;
    private boolean K;
    private boolean L;

    /* renamed from: k  reason: collision with root package name */
    public final int f21883k;

    /* renamed from: l  reason: collision with root package name */
    public final int f21884l;

    /* renamed from: m  reason: collision with root package name */
    public final Uri f21885m;

    /* renamed from: n  reason: collision with root package name */
    public final boolean f21886n;

    /* renamed from: o  reason: collision with root package name */
    public final int f21887o;

    /* renamed from: p  reason: collision with root package name */
    private final a6.h f21888p;
    private final com.google.android.exoplayer2.upstream.a q;

    /* renamed from: r  reason: collision with root package name */
    private final j f21889r;

    /* renamed from: s  reason: collision with root package name */
    private final boolean f21890s;

    /* renamed from: t  reason: collision with root package name */
    private final boolean f21891t;

    /* renamed from: u  reason: collision with root package name */
    private final h0 f21892u;

    /* renamed from: v  reason: collision with root package name */
    private final h f21893v;

    /* renamed from: w  reason: collision with root package name */
    private final List<w0> f21894w;

    /* renamed from: x  reason: collision with root package name */
    private final DrmInitData f21895x;

    /* renamed from: y  reason: collision with root package name */
    private final e5.b f21896y;

    /* renamed from: z  reason: collision with root package name */
    private final z f21897z;

    private i(h hVar, a6.h hVar2, com.google.android.exoplayer2.upstream.a aVar, w0 w0Var, boolean z4, a6.h hVar3, com.google.android.exoplayer2.upstream.a aVar2, boolean z8, Uri uri, List<w0> list, int i8, Object obj, long j8, long j9, long j10, int i9, boolean z9, int i10, boolean z10, boolean z11, h0 h0Var, DrmInitData drmInitData, j jVar, e5.b bVar, z zVar, boolean z12, t1 t1Var) {
        super(hVar2, aVar, w0Var, i8, obj, j8, j9, j10);
        this.A = z4;
        this.f21887o = i9;
        this.L = z9;
        this.f21884l = i10;
        this.q = aVar2;
        this.f21888p = hVar3;
        this.G = aVar2 != null;
        this.B = z8;
        this.f21885m = uri;
        this.f21890s = z11;
        this.f21892u = h0Var;
        this.f21891t = z10;
        this.f21893v = hVar;
        this.f21894w = list;
        this.f21895x = drmInitData;
        this.f21889r = jVar;
        this.f21896y = bVar;
        this.f21897z = zVar;
        this.f21886n = z12;
        this.C = t1Var;
        this.J = ImmutableList.E();
        this.f21883k = M.getAndIncrement();
    }

    private static a6.h i(a6.h hVar, byte[] bArr, byte[] bArr2) {
        if (bArr != null) {
            b6.a.e(bArr2);
            return new a(hVar, bArr, bArr2);
        }
        return hVar;
    }

    public static i j(h hVar, a6.h hVar2, w0 w0Var, long j8, com.google.android.exoplayer2.source.hls.playlist.d dVar, f.e eVar, Uri uri, List<w0> list, int i8, Object obj, boolean z4, q qVar, i iVar, byte[] bArr, byte[] bArr2, boolean z8, t1 t1Var) {
        boolean z9;
        a6.h hVar3;
        com.google.android.exoplayer2.upstream.a aVar;
        boolean z10;
        e5.b bVar;
        z zVar;
        j jVar;
        d.e eVar2 = eVar.f21878a;
        com.google.android.exoplayer2.upstream.a a9 = new a.b().i(j0.e(dVar.f22159a, eVar2.f10569a)).h(eVar2.f10577j).g(eVar2.f10578k).b(eVar.f21881d ? 8 : 0).a();
        boolean z11 = bArr != null;
        a6.h i9 = i(hVar2, bArr, z11 ? l((String) b6.a.e(eVar2.f10576h)) : null);
        d.C0112d c0112d = eVar2.f10570b;
        if (c0112d != null) {
            boolean z12 = bArr2 != null;
            byte[] l8 = z12 ? l((String) b6.a.e(c0112d.f10576h)) : null;
            z9 = z11;
            aVar = new com.google.android.exoplayer2.upstream.a(j0.e(dVar.f22159a, c0112d.f10569a), c0112d.f10577j, c0112d.f10578k);
            hVar3 = i(hVar2, bArr2, l8);
            z10 = z12;
        } else {
            z9 = z11;
            hVar3 = null;
            aVar = null;
            z10 = false;
        }
        long j9 = j8 + eVar2.f10573e;
        long j10 = j9 + eVar2.f10571c;
        int i10 = dVar.f10550j + eVar2.f10572d;
        if (iVar != null) {
            com.google.android.exoplayer2.upstream.a aVar2 = iVar.q;
            boolean z13 = aVar == aVar2 || (aVar != null && aVar2 != null && aVar.f10942a.equals(aVar2.f10942a) && aVar.f10948g == iVar.q.f10948g);
            boolean z14 = uri.equals(iVar.f21885m) && iVar.I;
            e5.b bVar2 = iVar.f21896y;
            bVar = bVar2;
            zVar = iVar.f21897z;
            jVar = (z13 && z14 && !iVar.K && iVar.f21884l == i10) ? iVar.D : null;
        } else {
            bVar = new e5.b();
            zVar = new z(10);
            jVar = null;
        }
        return new i(hVar, i9, a9, w0Var, z9, hVar3, aVar, z10, uri, list, i8, obj, j9, j10, eVar.f21879b, eVar.f21880c, !eVar.f21881d, i10, eVar2.f10579l, z4, qVar.a(i10), eVar2.f10574f, jVar, bVar, zVar, z8, t1Var);
    }

    private void k(a6.h hVar, com.google.android.exoplayer2.upstream.a aVar, boolean z4, boolean z8) {
        com.google.android.exoplayer2.upstream.a e8;
        long position;
        long j8;
        if (z4) {
            r0 = this.F != 0;
            e8 = aVar;
        } else {
            e8 = aVar.e(this.F);
        }
        try {
            n4.e u8 = u(hVar, e8, z8);
            if (r0) {
                u8.i(this.F);
            }
            do {
                try {
                    if (this.H) {
                        break;
                    }
                } catch (EOFException e9) {
                    if ((this.f20744d.f11200e & 16384) == 0) {
                        throw e9;
                    }
                    this.D.c();
                    position = u8.getPosition();
                    j8 = aVar.f10948g;
                }
            } while (this.D.a(u8));
            position = u8.getPosition();
            j8 = aVar.f10948g;
            this.F = (int) (position - j8);
        } finally {
            a6.j.a(hVar);
        }
    }

    private static byte[] l(String str) {
        if (com.google.common.base.c.e(str).startsWith("0x")) {
            str = str.substring(2);
        }
        byte[] byteArray = new BigInteger(str, 16).toByteArray();
        byte[] bArr = new byte[16];
        int length = byteArray.length > 16 ? byteArray.length - 16 : 0;
        System.arraycopy(byteArray, length, bArr, (16 - byteArray.length) + length, byteArray.length - length);
        return bArr;
    }

    private static boolean p(f.e eVar, com.google.android.exoplayer2.source.hls.playlist.d dVar) {
        d.e eVar2 = eVar.f21878a;
        return eVar2 instanceof d.b ? ((d.b) eVar2).f10562m || (eVar.f21880c == 0 && dVar.f22161c) : dVar.f22161c;
    }

    private void r() {
        k(this.f20749i, this.f20742b, this.A, true);
    }

    private void s() {
        if (this.G) {
            b6.a.e(this.f21888p);
            b6.a.e(this.q);
            k(this.f21888p, this.q, this.B, false);
            this.F = 0;
            this.G = false;
        }
    }

    private long t(n4.l lVar) {
        lVar.h();
        try {
            this.f21897z.Q(10);
            lVar.k(this.f21897z.e(), 0, 10);
        } catch (EOFException unused) {
        }
        if (this.f21897z.K() != 4801587) {
            return -9223372036854775807L;
        }
        this.f21897z.V(3);
        int G = this.f21897z.G();
        int i8 = G + 10;
        if (i8 > this.f21897z.b()) {
            byte[] e8 = this.f21897z.e();
            this.f21897z.Q(i8);
            System.arraycopy(e8, 0, this.f21897z.e(), 0, 10);
        }
        lVar.k(this.f21897z.e(), 10, G);
        Metadata e9 = this.f21896y.e(this.f21897z.e(), G);
        if (e9 == null) {
            return -9223372036854775807L;
        }
        int e10 = e9.e();
        for (int i9 = 0; i9 < e10; i9++) {
            Metadata.Entry d8 = e9.d(i9);
            if (d8 instanceof PrivFrame) {
                PrivFrame privFrame = (PrivFrame) d8;
                if ("com.apple.streaming.transportStreamTimestamp".equals(privFrame.f10118b)) {
                    System.arraycopy(privFrame.f10119c, 0, this.f21897z.e(), 0, 8);
                    this.f21897z.U(0);
                    this.f21897z.T(8);
                    return this.f21897z.A() & 8589934591L;
                }
            }
        }
        return -9223372036854775807L;
    }

    private n4.e u(a6.h hVar, com.google.android.exoplayer2.upstream.a aVar, boolean z4) {
        p pVar;
        long j8;
        long x8 = hVar.x(aVar);
        if (z4) {
            try {
                this.f21892u.h(this.f21890s, this.f20747g);
            } catch (InterruptedException unused) {
                throw new InterruptedIOException();
            }
        }
        n4.e eVar = new n4.e(hVar, aVar.f10948g, x8);
        if (this.D == null) {
            long t8 = t(eVar);
            eVar.h();
            j jVar = this.f21889r;
            j f5 = jVar != null ? jVar.f() : this.f21893v.a(aVar.f10942a, this.f20744d, this.f21894w, this.f21892u, hVar.y(), eVar, this.C);
            this.D = f5;
            if (f5.d()) {
                pVar = this.E;
                j8 = t8 != -9223372036854775807L ? this.f21892u.b(t8) : this.f20747g;
            } else {
                pVar = this.E;
                j8 = 0;
            }
            pVar.n0(j8);
            this.E.Z();
            this.D.b(this.E);
        }
        this.E.k0(this.f21895x);
        return eVar;
    }

    public static boolean w(i iVar, Uri uri, com.google.android.exoplayer2.source.hls.playlist.d dVar, f.e eVar, long j8) {
        if (iVar == null) {
            return false;
        }
        if (uri.equals(iVar.f21885m) && iVar.I) {
            return false;
        }
        return !p(eVar, dVar) || j8 + eVar.f21878a.f10573e < iVar.f20748h;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.e
    public void a() {
        j jVar;
        b6.a.e(this.E);
        if (this.D == null && (jVar = this.f21889r) != null && jVar.e()) {
            this.D = this.f21889r;
            this.G = false;
        }
        s();
        if (this.H) {
            return;
        }
        if (!this.f21891t) {
            r();
        }
        this.I = !this.H;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.e
    public void c() {
        this.H = true;
    }

    @Override // j5.n
    public boolean h() {
        return this.I;
    }

    public int m(int i8) {
        b6.a.f(!this.f21886n);
        if (i8 >= this.J.size()) {
            return 0;
        }
        return this.J.get(i8).intValue();
    }

    public void n(p pVar, ImmutableList<Integer> immutableList) {
        this.E = pVar;
        this.J = immutableList;
    }

    public void o() {
        this.K = true;
    }

    public boolean q() {
        return this.L;
    }

    public void v() {
        this.L = true;
    }
}
