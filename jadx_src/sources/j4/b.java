package j4;

import android.util.SparseArray;
import c6.x;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.a1;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.i2;
import com.google.android.exoplayer2.j;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.w0;
import com.google.android.exoplayer2.x1;
import com.google.android.exoplayer2.y1;
import com.google.android.exoplayer2.z0;
import h5.h;
import h5.i;
import java.io.IOException;
import java.util.List;
import l4.g;
import p5.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface b {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final long f20637a;

        /* renamed from: b  reason: collision with root package name */
        public final h2 f20638b;

        /* renamed from: c  reason: collision with root package name */
        public final int f20639c;

        /* renamed from: d  reason: collision with root package name */
        public final k.b f20640d;

        /* renamed from: e  reason: collision with root package name */
        public final long f20641e;

        /* renamed from: f  reason: collision with root package name */
        public final h2 f20642f;

        /* renamed from: g  reason: collision with root package name */
        public final int f20643g;

        /* renamed from: h  reason: collision with root package name */
        public final k.b f20644h;

        /* renamed from: i  reason: collision with root package name */
        public final long f20645i;

        /* renamed from: j  reason: collision with root package name */
        public final long f20646j;

        public a(long j8, h2 h2Var, int i8, k.b bVar, long j9, h2 h2Var2, int i9, k.b bVar2, long j10, long j11) {
            this.f20637a = j8;
            this.f20638b = h2Var;
            this.f20639c = i8;
            this.f20640d = bVar;
            this.f20641e = j9;
            this.f20642f = h2Var2;
            this.f20643g = i9;
            this.f20644h = bVar2;
            this.f20645i = j10;
            this.f20646j = j11;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || a.class != obj.getClass()) {
                return false;
            }
            a aVar = (a) obj;
            return this.f20637a == aVar.f20637a && this.f20639c == aVar.f20639c && this.f20641e == aVar.f20641e && this.f20643g == aVar.f20643g && this.f20645i == aVar.f20645i && this.f20646j == aVar.f20646j && com.google.common.base.k.a(this.f20638b, aVar.f20638b) && com.google.common.base.k.a(this.f20640d, aVar.f20640d) && com.google.common.base.k.a(this.f20642f, aVar.f20642f) && com.google.common.base.k.a(this.f20644h, aVar.f20644h);
        }

        public int hashCode() {
            return com.google.common.base.k.b(Long.valueOf(this.f20637a), this.f20638b, Integer.valueOf(this.f20639c), this.f20640d, Long.valueOf(this.f20641e), this.f20642f, Integer.valueOf(this.f20643g), this.f20644h, Long.valueOf(this.f20645i), Long.valueOf(this.f20646j));
        }
    }

    /* renamed from: j4.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0177b {

        /* renamed from: a  reason: collision with root package name */
        private final b6.k f20647a;

        /* renamed from: b  reason: collision with root package name */
        private final SparseArray<a> f20648b;

        public C0177b(b6.k kVar, SparseArray<a> sparseArray) {
            this.f20647a = kVar;
            SparseArray<a> sparseArray2 = new SparseArray<>(kVar.c());
            for (int i8 = 0; i8 < kVar.c(); i8++) {
                int b9 = kVar.b(i8);
                sparseArray2.append(b9, (a) b6.a.e(sparseArray.get(b9)));
            }
            this.f20648b = sparseArray2;
        }

        public boolean a(int i8) {
            return this.f20647a.a(i8);
        }

        public int b(int i8) {
            return this.f20647a.b(i8);
        }

        public a c(int i8) {
            return (a) b6.a.e(this.f20648b.get(i8));
        }

        public int d() {
            return this.f20647a.c();
        }
    }

    default void A(a aVar, int i8, long j8) {
    }

    @Deprecated
    default void B(a aVar) {
    }

    @Deprecated
    default void C(a aVar, int i8, w0 w0Var) {
    }

    default void D(a aVar, e eVar) {
    }

    default void E(a aVar, String str, long j8, long j9) {
    }

    @Deprecated
    default void F(a aVar, List<p5.b> list) {
    }

    default void G(a aVar, boolean z4) {
    }

    default void H(a aVar, i2 i2Var) {
    }

    default void I(a aVar, boolean z4) {
    }

    default void J(a aVar, String str, long j8, long j9) {
    }

    default void K(a aVar, l4.e eVar) {
    }

    default void L(a aVar, Exception exc) {
    }

    default void M(a aVar) {
    }

    @Deprecated
    default void N(a aVar, String str, long j8) {
    }

    default void O(a aVar, z0 z0Var, int i8) {
    }

    default void P(a aVar, y1.e eVar, y1.e eVar2, int i8) {
    }

    default void Q(a aVar, int i8) {
    }

    default void R(a aVar) {
    }

    default void S(a aVar, com.google.android.exoplayer2.audio.a aVar2) {
    }

    default void U(a aVar, l4.e eVar) {
    }

    default void V(a aVar, l4.e eVar) {
    }

    default void W(a aVar, int i8) {
    }

    default void Y(a aVar, boolean z4) {
    }

    default void Z(a aVar, h hVar, i iVar) {
    }

    @Deprecated
    default void a(a aVar, boolean z4) {
    }

    default void a0(a aVar, PlaybackException playbackException) {
    }

    @Deprecated
    default void b(a aVar, int i8, l4.e eVar) {
    }

    default void b0(a aVar, long j8, int i8) {
    }

    default void c(a aVar, String str) {
    }

    default void c0(a aVar) {
    }

    default void d(a aVar, int i8) {
    }

    default void d0(y1 y1Var, C0177b c0177b) {
    }

    @Deprecated
    default void e(a aVar, boolean z4, int i8) {
    }

    @Deprecated
    default void e0(a aVar) {
    }

    @Deprecated
    default void f(a aVar, String str, long j8) {
    }

    default void f0(a aVar, i iVar) {
    }

    default void g(a aVar, h hVar, i iVar, IOException iOException, boolean z4) {
    }

    default void g0(a aVar, i iVar) {
    }

    @Deprecated
    default void h(a aVar, int i8, String str, long j8) {
    }

    default void h0(a aVar, int i8, int i9) {
    }

    default void i0(a aVar, Exception exc) {
    }

    default void j(a aVar, w0 w0Var, g gVar) {
    }

    default void j0(a aVar, int i8) {
    }

    @Deprecated
    default void k(a aVar, int i8, int i9, int i10, float f5) {
    }

    default void k0(a aVar, w0 w0Var, g gVar) {
    }

    default void l(a aVar, y1.b bVar) {
    }

    default void l0(a aVar, x1 x1Var) {
    }

    default void m(a aVar, Object obj, long j8) {
    }

    default void m0(a aVar, Metadata metadata) {
    }

    @Deprecated
    default void n(a aVar, int i8) {
    }

    default void n0(a aVar, int i8) {
    }

    default void o(a aVar, int i8, long j8, long j9) {
    }

    default void o0(a aVar) {
    }

    default void p(a aVar, int i8, boolean z4) {
    }

    default void p0(a aVar, Exception exc) {
    }

    default void q0(a aVar, PlaybackException playbackException) {
    }

    default void r(a aVar, int i8, long j8, long j9) {
    }

    default void r0(a aVar, String str) {
    }

    default void s(a aVar, l4.e eVar) {
    }

    default void s0(a aVar, boolean z4) {
    }

    default void t(a aVar) {
    }

    default void t0(a aVar, x xVar) {
    }

    default void u(a aVar, a1 a1Var) {
    }

    @Deprecated
    default void u0(a aVar, int i8, l4.e eVar) {
    }

    default void v(a aVar, h hVar, i iVar) {
    }

    @Deprecated
    default void v0(a aVar, w0 w0Var) {
    }

    default void w(a aVar, boolean z4, int i8) {
    }

    default void w0(a aVar, Exception exc) {
    }

    @Deprecated
    default void x(a aVar) {
    }

    default void x0(a aVar, long j8) {
    }

    default void y(a aVar, h hVar, i iVar) {
    }

    @Deprecated
    default void y0(a aVar, w0 w0Var) {
    }

    default void z(a aVar, j jVar) {
    }

    default void z0(a aVar, float f5) {
    }
}
