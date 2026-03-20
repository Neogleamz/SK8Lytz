package com.google.android.exoplayer2;

import android.os.Bundle;
import android.view.Surface;
import b6.k;
import com.google.android.exoplayer2.g;
import com.google.android.exoplayer2.metadata.Metadata;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface y1 {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements g {

        /* renamed from: b  reason: collision with root package name */
        public static final b f11272b = new a().e();

        /* renamed from: c  reason: collision with root package name */
        private static final String f11273c = b6.l0.r0(0);

        /* renamed from: d  reason: collision with root package name */
        public static final g.a<b> f11274d = i4.c0.a;

        /* renamed from: a  reason: collision with root package name */
        private final b6.k f11275a;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a {

            /* renamed from: b  reason: collision with root package name */
            private static final int[] f11276b = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 31, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};

            /* renamed from: a  reason: collision with root package name */
            private final k.b f11277a = new k.b();

            public a a(int i8) {
                this.f11277a.a(i8);
                return this;
            }

            public a b(b bVar) {
                this.f11277a.b(bVar.f11275a);
                return this;
            }

            public a c(int... iArr) {
                this.f11277a.c(iArr);
                return this;
            }

            public a d(int i8, boolean z4) {
                this.f11277a.d(i8, z4);
                return this;
            }

            public b e() {
                return new b(this.f11277a.e());
            }
        }

        private b(b6.k kVar) {
            this.f11275a = kVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static b c(Bundle bundle) {
            ArrayList<Integer> integerArrayList = bundle.getIntegerArrayList(f11273c);
            if (integerArrayList == null) {
                return f11272b;
            }
            a aVar = new a();
            for (int i8 = 0; i8 < integerArrayList.size(); i8++) {
                aVar.a(integerArrayList.get(i8).intValue());
            }
            return aVar.e();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof b) {
                return this.f11275a.equals(((b) obj).f11275a);
            }
            return false;
        }

        public int hashCode() {
            return this.f11275a.hashCode();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        private final b6.k f11278a;

        public c(b6.k kVar) {
            this.f11278a = kVar;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof c) {
                return this.f11278a.equals(((c) obj).f11278a);
            }
            return false;
        }

        public int hashCode() {
            return this.f11278a.hashCode();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        default void A(int i8) {
        }

        @Deprecated
        default void B(boolean z4) {
        }

        @Deprecated
        default void C(int i8) {
        }

        default void D(i2 i2Var) {
        }

        default void E(boolean z4) {
        }

        @Deprecated
        default void F() {
        }

        default void G(PlaybackException playbackException) {
        }

        default void H(b bVar) {
        }

        default void K(h2 h2Var, int i8) {
        }

        default void L(float f5) {
        }

        default void N(int i8) {
        }

        default void Q(j jVar) {
        }

        default void T(a1 a1Var) {
        }

        default void U(boolean z4) {
        }

        default void V(y1 y1Var, c cVar) {
        }

        default void Y(int i8, boolean z4) {
        }

        @Deprecated
        default void Z(boolean z4, int i8) {
        }

        default void a(boolean z4) {
        }

        default void a0(com.google.android.exoplayer2.audio.a aVar) {
        }

        default void d0() {
        }

        default void e0(z0 z0Var, int i8) {
        }

        default void h(Metadata metadata) {
        }

        default void h0(boolean z4, int i8) {
        }

        default void j0(int i8, int i9) {
        }

        default void l(int i8) {
        }

        default void m0(PlaybackException playbackException) {
        }

        default void p(c6.x xVar) {
        }

        default void p0(boolean z4) {
        }

        default void q(p5.e eVar) {
        }

        @Deprecated
        default void r(List<p5.b> list) {
        }

        default void w(x1 x1Var) {
        }

        default void z(e eVar, e eVar2, int i8) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e implements g {

        /* renamed from: l  reason: collision with root package name */
        private static final String f11279l = b6.l0.r0(0);

        /* renamed from: m  reason: collision with root package name */
        private static final String f11280m = b6.l0.r0(1);

        /* renamed from: n  reason: collision with root package name */
        private static final String f11281n = b6.l0.r0(2);

        /* renamed from: p  reason: collision with root package name */
        private static final String f11282p = b6.l0.r0(3);
        private static final String q = b6.l0.r0(4);

        /* renamed from: t  reason: collision with root package name */
        private static final String f11283t = b6.l0.r0(5);

        /* renamed from: w  reason: collision with root package name */
        private static final String f11284w = b6.l0.r0(6);

        /* renamed from: x  reason: collision with root package name */
        public static final g.a<e> f11285x = i4.d0.a;

        /* renamed from: a  reason: collision with root package name */
        public final Object f11286a;
        @Deprecated

        /* renamed from: b  reason: collision with root package name */
        public final int f11287b;

        /* renamed from: c  reason: collision with root package name */
        public final int f11288c;

        /* renamed from: d  reason: collision with root package name */
        public final z0 f11289d;

        /* renamed from: e  reason: collision with root package name */
        public final Object f11290e;

        /* renamed from: f  reason: collision with root package name */
        public final int f11291f;

        /* renamed from: g  reason: collision with root package name */
        public final long f11292g;

        /* renamed from: h  reason: collision with root package name */
        public final long f11293h;

        /* renamed from: j  reason: collision with root package name */
        public final int f11294j;

        /* renamed from: k  reason: collision with root package name */
        public final int f11295k;

        public e(Object obj, int i8, z0 z0Var, Object obj2, int i9, long j8, long j9, int i10, int i11) {
            this.f11286a = obj;
            this.f11287b = i8;
            this.f11288c = i8;
            this.f11289d = z0Var;
            this.f11290e = obj2;
            this.f11291f = i9;
            this.f11292g = j8;
            this.f11293h = j9;
            this.f11294j = i10;
            this.f11295k = i11;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static e b(Bundle bundle) {
            int i8 = bundle.getInt(f11279l, 0);
            Bundle bundle2 = bundle.getBundle(f11280m);
            return new e(null, i8, bundle2 == null ? null : z0.q.a(bundle2), null, bundle.getInt(f11281n, 0), bundle.getLong(f11282p, 0L), bundle.getLong(q, 0L), bundle.getInt(f11283t, -1), bundle.getInt(f11284w, -1));
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || e.class != obj.getClass()) {
                return false;
            }
            e eVar = (e) obj;
            return this.f11288c == eVar.f11288c && this.f11291f == eVar.f11291f && this.f11292g == eVar.f11292g && this.f11293h == eVar.f11293h && this.f11294j == eVar.f11294j && this.f11295k == eVar.f11295k && com.google.common.base.k.a(this.f11286a, eVar.f11286a) && com.google.common.base.k.a(this.f11290e, eVar.f11290e) && com.google.common.base.k.a(this.f11289d, eVar.f11289d);
        }

        public int hashCode() {
            return com.google.common.base.k.b(this.f11286a, Integer.valueOf(this.f11288c), this.f11289d, this.f11290e, Integer.valueOf(this.f11291f), Long.valueOf(this.f11292g), Long.valueOf(this.f11293h), Integer.valueOf(this.f11294j), Integer.valueOf(this.f11295k));
        }
    }

    i2 B();

    boolean D();

    int E();

    int F();

    void G(int i8);

    boolean H();

    int I();

    int J();

    h2 K();

    boolean M();

    boolean O();

    void a();

    long b();

    x1 c();

    void d(x1 x1Var);

    void e(float f5);

    void g(Surface surface);

    long getCurrentPosition();

    boolean h();

    long i();

    void j(int i8, long j8);

    boolean k();

    void l(boolean z4);

    int m();

    void o();

    boolean p();

    int q();

    int r();

    void release();

    PlaybackException s();

    void seekTo(long j8);

    void stop();

    void t(boolean z4);

    long u();

    void v(d dVar);

    long w();

    boolean x();

    void y();

    int z();
}
