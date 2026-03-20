package com.google.android.exoplayer2.source;

import b6.l0;
import com.google.android.exoplayer2.source.j;
import com.google.android.exoplayer2.source.k;
import i4.i0;
import java.io.IOException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h implements j, j.a {

    /* renamed from: a  reason: collision with root package name */
    public final k.b f10439a;

    /* renamed from: b  reason: collision with root package name */
    private final long f10440b;

    /* renamed from: c  reason: collision with root package name */
    private final a6.b f10441c;

    /* renamed from: d  reason: collision with root package name */
    private k f10442d;

    /* renamed from: e  reason: collision with root package name */
    private j f10443e;

    /* renamed from: f  reason: collision with root package name */
    private j.a f10444f;

    /* renamed from: g  reason: collision with root package name */
    private a f10445g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f10446h;

    /* renamed from: j  reason: collision with root package name */
    private long f10447j = -9223372036854775807L;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(k.b bVar);

        void b(k.b bVar, IOException iOException);
    }

    public h(k.b bVar, a6.b bVar2, long j8) {
        this.f10439a = bVar;
        this.f10441c = bVar2;
        this.f10440b = j8;
    }

    private long t(long j8) {
        long j9 = this.f10447j;
        return j9 != -9223372036854775807L ? j9 : j8;
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public long b() {
        return ((j) l0.j(this.f10443e)).b();
    }

    @Override // com.google.android.exoplayer2.source.j
    public long c(long j8, i0 i0Var) {
        return ((j) l0.j(this.f10443e)).c(j8, i0Var);
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public boolean d(long j8) {
        j jVar = this.f10443e;
        return jVar != null && jVar.d(j8);
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public boolean f() {
        j jVar = this.f10443e;
        return jVar != null && jVar.f();
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public long g() {
        return ((j) l0.j(this.f10443e)).g();
    }

    @Override // com.google.android.exoplayer2.source.j, com.google.android.exoplayer2.source.w
    public void h(long j8) {
        ((j) l0.j(this.f10443e)).h(j8);
    }

    public void j(k.b bVar) {
        long t8 = t(this.f10440b);
        j b9 = ((k) b6.a.e(this.f10442d)).b(bVar, this.f10441c, t8);
        this.f10443e = b9;
        if (this.f10444f != null) {
            b9.q(this, t8);
        }
    }

    @Override // com.google.android.exoplayer2.source.j.a
    public void k(j jVar) {
        ((j.a) l0.j(this.f10444f)).k(this);
        a aVar = this.f10445g;
        if (aVar != null) {
            aVar.a(this.f10439a);
        }
    }

    @Override // com.google.android.exoplayer2.source.j
    public void l() {
        try {
            j jVar = this.f10443e;
            if (jVar != null) {
                jVar.l();
            } else {
                k kVar = this.f10442d;
                if (kVar != null) {
                    kVar.n();
                }
            }
        } catch (IOException e8) {
            a aVar = this.f10445g;
            if (aVar == null) {
                throw e8;
            }
            if (this.f10446h) {
                return;
            }
            this.f10446h = true;
            aVar.b(this.f10439a, e8);
        }
    }

    public long m() {
        return this.f10447j;
    }

    @Override // com.google.android.exoplayer2.source.j
    public long n(long j8) {
        return ((j) l0.j(this.f10443e)).n(j8);
    }

    public long o() {
        return this.f10440b;
    }

    @Override // com.google.android.exoplayer2.source.j
    public long p() {
        return ((j) l0.j(this.f10443e)).p();
    }

    @Override // com.google.android.exoplayer2.source.j
    public void q(j.a aVar, long j8) {
        this.f10444f = aVar;
        j jVar = this.f10443e;
        if (jVar != null) {
            jVar.q(this, t(this.f10440b));
        }
    }

    @Override // com.google.android.exoplayer2.source.j
    public h5.w r() {
        return ((j) l0.j(this.f10443e)).r();
    }

    @Override // com.google.android.exoplayer2.source.j
    public long s(z5.r[] rVarArr, boolean[] zArr, h5.r[] rVarArr2, boolean[] zArr2, long j8) {
        long j9;
        long j10 = this.f10447j;
        if (j10 == -9223372036854775807L || j8 != this.f10440b) {
            j9 = j8;
        } else {
            this.f10447j = -9223372036854775807L;
            j9 = j10;
        }
        return ((j) l0.j(this.f10443e)).s(rVarArr, zArr, rVarArr2, zArr2, j9);
    }

    @Override // com.google.android.exoplayer2.source.j
    public void u(long j8, boolean z4) {
        ((j) l0.j(this.f10443e)).u(j8, z4);
    }

    @Override // com.google.android.exoplayer2.source.w.a
    /* renamed from: v */
    public void e(j jVar) {
        ((j.a) l0.j(this.f10444f)).e(this);
    }

    public void w(long j8) {
        this.f10447j = j8;
    }

    public void x() {
        if (this.f10443e != null) {
            ((k) b6.a.e(this.f10442d)).p(this.f10443e);
        }
    }

    public void y(k kVar) {
        b6.a.f(this.f10442d == null);
        this.f10442d = kVar;
    }
}
