package com.google.android.exoplayer2;

import android.net.Uri;
import android.os.Bundle;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.g;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z0 implements com.google.android.exoplayer2.g {

    /* renamed from: j  reason: collision with root package name */
    public static final z0 f11297j = new c().a();

    /* renamed from: k  reason: collision with root package name */
    private static final String f11298k = b6.l0.r0(0);

    /* renamed from: l  reason: collision with root package name */
    private static final String f11299l = b6.l0.r0(1);

    /* renamed from: m  reason: collision with root package name */
    private static final String f11300m = b6.l0.r0(2);

    /* renamed from: n  reason: collision with root package name */
    private static final String f11301n = b6.l0.r0(3);

    /* renamed from: p  reason: collision with root package name */
    private static final String f11302p = b6.l0.r0(4);
    public static final g.a<z0> q = i4.v.a;

    /* renamed from: a  reason: collision with root package name */
    public final String f11303a;

    /* renamed from: b  reason: collision with root package name */
    public final h f11304b;
    @Deprecated

    /* renamed from: c  reason: collision with root package name */
    public final i f11305c;

    /* renamed from: d  reason: collision with root package name */
    public final g f11306d;

    /* renamed from: e  reason: collision with root package name */
    public final a1 f11307e;

    /* renamed from: f  reason: collision with root package name */
    public final d f11308f;
    @Deprecated

    /* renamed from: g  reason: collision with root package name */
    public final e f11309g;

    /* renamed from: h  reason: collision with root package name */
    public final j f11310h;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        private String f11311a;

        /* renamed from: b  reason: collision with root package name */
        private Uri f11312b;

        /* renamed from: c  reason: collision with root package name */
        private String f11313c;

        /* renamed from: d  reason: collision with root package name */
        private d.a f11314d;

        /* renamed from: e  reason: collision with root package name */
        private f.a f11315e;

        /* renamed from: f  reason: collision with root package name */
        private List<StreamKey> f11316f;

        /* renamed from: g  reason: collision with root package name */
        private String f11317g;

        /* renamed from: h  reason: collision with root package name */
        private ImmutableList<l> f11318h;

        /* renamed from: i  reason: collision with root package name */
        private b f11319i;

        /* renamed from: j  reason: collision with root package name */
        private Object f11320j;

        /* renamed from: k  reason: collision with root package name */
        private a1 f11321k;

        /* renamed from: l  reason: collision with root package name */
        private g.a f11322l;

        /* renamed from: m  reason: collision with root package name */
        private j f11323m;

        public c() {
            this.f11314d = new d.a();
            this.f11315e = new f.a();
            this.f11316f = Collections.emptyList();
            this.f11318h = ImmutableList.E();
            this.f11322l = new g.a();
            this.f11323m = j.f11387d;
        }

        private c(z0 z0Var) {
            this();
            this.f11314d = z0Var.f11308f.b();
            this.f11311a = z0Var.f11303a;
            this.f11321k = z0Var.f11307e;
            this.f11322l = z0Var.f11306d.b();
            this.f11323m = z0Var.f11310h;
            h hVar = z0Var.f11304b;
            if (hVar != null) {
                this.f11317g = hVar.f11383f;
                this.f11313c = hVar.f11379b;
                this.f11312b = hVar.f11378a;
                this.f11316f = hVar.f11382e;
                this.f11318h = hVar.f11384g;
                this.f11320j = hVar.f11386i;
                f fVar = hVar.f11380c;
                this.f11315e = fVar != null ? fVar.b() : new f.a();
            }
        }

        public z0 a() {
            i iVar;
            b6.a.f(this.f11315e.f11354b == null || this.f11315e.f11353a != null);
            Uri uri = this.f11312b;
            if (uri != null) {
                iVar = new i(uri, this.f11313c, this.f11315e.f11353a != null ? this.f11315e.i() : null, this.f11319i, this.f11316f, this.f11317g, this.f11318h, this.f11320j);
            } else {
                iVar = null;
            }
            String str = this.f11311a;
            if (str == null) {
                str = BuildConfig.FLAVOR;
            }
            String str2 = str;
            e g8 = this.f11314d.g();
            g f5 = this.f11322l.f();
            a1 a1Var = this.f11321k;
            if (a1Var == null) {
                a1Var = a1.W;
            }
            return new z0(str2, g8, iVar, f5, a1Var, this.f11323m);
        }

        public c b(String str) {
            this.f11317g = str;
            return this;
        }

        public c c(String str) {
            this.f11311a = (String) b6.a.e(str);
            return this;
        }

        public c d(String str) {
            this.f11313c = str;
            return this;
        }

        public c e(Object obj) {
            this.f11320j = obj;
            return this;
        }

        public c f(Uri uri) {
            this.f11312b = uri;
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d implements com.google.android.exoplayer2.g {

        /* renamed from: f  reason: collision with root package name */
        public static final d f11324f = new a().f();

        /* renamed from: g  reason: collision with root package name */
        private static final String f11325g = b6.l0.r0(0);

        /* renamed from: h  reason: collision with root package name */
        private static final String f11326h = b6.l0.r0(1);

        /* renamed from: j  reason: collision with root package name */
        private static final String f11327j = b6.l0.r0(2);

        /* renamed from: k  reason: collision with root package name */
        private static final String f11328k = b6.l0.r0(3);

        /* renamed from: l  reason: collision with root package name */
        private static final String f11329l = b6.l0.r0(4);

        /* renamed from: m  reason: collision with root package name */
        public static final g.a<e> f11330m = i4.w.a;

        /* renamed from: a  reason: collision with root package name */
        public final long f11331a;

        /* renamed from: b  reason: collision with root package name */
        public final long f11332b;

        /* renamed from: c  reason: collision with root package name */
        public final boolean f11333c;

        /* renamed from: d  reason: collision with root package name */
        public final boolean f11334d;

        /* renamed from: e  reason: collision with root package name */
        public final boolean f11335e;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a {

            /* renamed from: a  reason: collision with root package name */
            private long f11336a;

            /* renamed from: b  reason: collision with root package name */
            private long f11337b;

            /* renamed from: c  reason: collision with root package name */
            private boolean f11338c;

            /* renamed from: d  reason: collision with root package name */
            private boolean f11339d;

            /* renamed from: e  reason: collision with root package name */
            private boolean f11340e;

            public a() {
                this.f11337b = Long.MIN_VALUE;
            }

            private a(d dVar) {
                this.f11336a = dVar.f11331a;
                this.f11337b = dVar.f11332b;
                this.f11338c = dVar.f11333c;
                this.f11339d = dVar.f11334d;
                this.f11340e = dVar.f11335e;
            }

            public d f() {
                return g();
            }

            @Deprecated
            public e g() {
                return new e(this);
            }

            public a h(long j8) {
                b6.a.a(j8 == Long.MIN_VALUE || j8 >= 0);
                this.f11337b = j8;
                return this;
            }

            public a i(boolean z4) {
                this.f11339d = z4;
                return this;
            }

            public a j(boolean z4) {
                this.f11338c = z4;
                return this;
            }

            public a k(long j8) {
                b6.a.a(j8 >= 0);
                this.f11336a = j8;
                return this;
            }

            public a l(boolean z4) {
                this.f11340e = z4;
                return this;
            }
        }

        private d(a aVar) {
            this.f11331a = aVar.f11336a;
            this.f11332b = aVar.f11337b;
            this.f11333c = aVar.f11338c;
            this.f11334d = aVar.f11339d;
            this.f11335e = aVar.f11340e;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ e c(Bundle bundle) {
            a aVar = new a();
            String str = f11325g;
            d dVar = f11324f;
            return aVar.k(bundle.getLong(str, dVar.f11331a)).h(bundle.getLong(f11326h, dVar.f11332b)).j(bundle.getBoolean(f11327j, dVar.f11333c)).i(bundle.getBoolean(f11328k, dVar.f11334d)).l(bundle.getBoolean(f11329l, dVar.f11335e)).g();
        }

        public a b() {
            return new a();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof d) {
                d dVar = (d) obj;
                return this.f11331a == dVar.f11331a && this.f11332b == dVar.f11332b && this.f11333c == dVar.f11333c && this.f11334d == dVar.f11334d && this.f11335e == dVar.f11335e;
            }
            return false;
        }

        public int hashCode() {
            long j8 = this.f11331a;
            long j9 = this.f11332b;
            return (((((((((int) (j8 ^ (j8 >>> 32))) * 31) + ((int) ((j9 >>> 32) ^ j9))) * 31) + (this.f11333c ? 1 : 0)) * 31) + (this.f11334d ? 1 : 0)) * 31) + (this.f11335e ? 1 : 0);
        }
    }

    @Deprecated
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e extends d {

        /* renamed from: n  reason: collision with root package name */
        public static final e f11341n = new d.a().g();

        private e(d.a aVar) {
            super(aVar);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class f {

        /* renamed from: a  reason: collision with root package name */
        public final UUID f11342a;
        @Deprecated

        /* renamed from: b  reason: collision with root package name */
        public final UUID f11343b;

        /* renamed from: c  reason: collision with root package name */
        public final Uri f11344c;
        @Deprecated

        /* renamed from: d  reason: collision with root package name */
        public final ImmutableMap<String, String> f11345d;

        /* renamed from: e  reason: collision with root package name */
        public final ImmutableMap<String, String> f11346e;

        /* renamed from: f  reason: collision with root package name */
        public final boolean f11347f;

        /* renamed from: g  reason: collision with root package name */
        public final boolean f11348g;

        /* renamed from: h  reason: collision with root package name */
        public final boolean f11349h;
        @Deprecated

        /* renamed from: i  reason: collision with root package name */
        public final ImmutableList<Integer> f11350i;

        /* renamed from: j  reason: collision with root package name */
        public final ImmutableList<Integer> f11351j;

        /* renamed from: k  reason: collision with root package name */
        private final byte[] f11352k;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a {

            /* renamed from: a  reason: collision with root package name */
            private UUID f11353a;

            /* renamed from: b  reason: collision with root package name */
            private Uri f11354b;

            /* renamed from: c  reason: collision with root package name */
            private ImmutableMap<String, String> f11355c;

            /* renamed from: d  reason: collision with root package name */
            private boolean f11356d;

            /* renamed from: e  reason: collision with root package name */
            private boolean f11357e;

            /* renamed from: f  reason: collision with root package name */
            private boolean f11358f;

            /* renamed from: g  reason: collision with root package name */
            private ImmutableList<Integer> f11359g;

            /* renamed from: h  reason: collision with root package name */
            private byte[] f11360h;

            @Deprecated
            private a() {
                this.f11355c = ImmutableMap.n();
                this.f11359g = ImmutableList.E();
            }

            private a(f fVar) {
                this.f11353a = fVar.f11342a;
                this.f11354b = fVar.f11344c;
                this.f11355c = fVar.f11346e;
                this.f11356d = fVar.f11347f;
                this.f11357e = fVar.f11348g;
                this.f11358f = fVar.f11349h;
                this.f11359g = fVar.f11351j;
                this.f11360h = fVar.f11352k;
            }

            public f i() {
                return new f(this);
            }
        }

        private f(a aVar) {
            b6.a.f((aVar.f11358f && aVar.f11354b == null) ? false : true);
            UUID uuid = (UUID) b6.a.e(aVar.f11353a);
            this.f11342a = uuid;
            this.f11343b = uuid;
            this.f11344c = aVar.f11354b;
            this.f11345d = aVar.f11355c;
            this.f11346e = aVar.f11355c;
            this.f11347f = aVar.f11356d;
            this.f11349h = aVar.f11358f;
            this.f11348g = aVar.f11357e;
            this.f11350i = aVar.f11359g;
            this.f11351j = aVar.f11359g;
            this.f11352k = aVar.f11360h != null ? Arrays.copyOf(aVar.f11360h, aVar.f11360h.length) : null;
        }

        public a b() {
            return new a();
        }

        public byte[] c() {
            byte[] bArr = this.f11352k;
            if (bArr != null) {
                return Arrays.copyOf(bArr, bArr.length);
            }
            return null;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof f) {
                f fVar = (f) obj;
                return this.f11342a.equals(fVar.f11342a) && b6.l0.c(this.f11344c, fVar.f11344c) && b6.l0.c(this.f11346e, fVar.f11346e) && this.f11347f == fVar.f11347f && this.f11349h == fVar.f11349h && this.f11348g == fVar.f11348g && this.f11351j.equals(fVar.f11351j) && Arrays.equals(this.f11352k, fVar.f11352k);
            }
            return false;
        }

        public int hashCode() {
            int hashCode = this.f11342a.hashCode() * 31;
            Uri uri = this.f11344c;
            return ((((((((((((hashCode + (uri != null ? uri.hashCode() : 0)) * 31) + this.f11346e.hashCode()) * 31) + (this.f11347f ? 1 : 0)) * 31) + (this.f11349h ? 1 : 0)) * 31) + (this.f11348g ? 1 : 0)) * 31) + this.f11351j.hashCode()) * 31) + Arrays.hashCode(this.f11352k);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g implements com.google.android.exoplayer2.g {

        /* renamed from: f  reason: collision with root package name */
        public static final g f11361f = new a().f();

        /* renamed from: g  reason: collision with root package name */
        private static final String f11362g = b6.l0.r0(0);

        /* renamed from: h  reason: collision with root package name */
        private static final String f11363h = b6.l0.r0(1);

        /* renamed from: j  reason: collision with root package name */
        private static final String f11364j = b6.l0.r0(2);

        /* renamed from: k  reason: collision with root package name */
        private static final String f11365k = b6.l0.r0(3);

        /* renamed from: l  reason: collision with root package name */
        private static final String f11366l = b6.l0.r0(4);

        /* renamed from: m  reason: collision with root package name */
        public static final g.a<g> f11367m = i4.x.a;

        /* renamed from: a  reason: collision with root package name */
        public final long f11368a;

        /* renamed from: b  reason: collision with root package name */
        public final long f11369b;

        /* renamed from: c  reason: collision with root package name */
        public final long f11370c;

        /* renamed from: d  reason: collision with root package name */
        public final float f11371d;

        /* renamed from: e  reason: collision with root package name */
        public final float f11372e;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a {

            /* renamed from: a  reason: collision with root package name */
            private long f11373a;

            /* renamed from: b  reason: collision with root package name */
            private long f11374b;

            /* renamed from: c  reason: collision with root package name */
            private long f11375c;

            /* renamed from: d  reason: collision with root package name */
            private float f11376d;

            /* renamed from: e  reason: collision with root package name */
            private float f11377e;

            public a() {
                this.f11373a = -9223372036854775807L;
                this.f11374b = -9223372036854775807L;
                this.f11375c = -9223372036854775807L;
                this.f11376d = -3.4028235E38f;
                this.f11377e = -3.4028235E38f;
            }

            private a(g gVar) {
                this.f11373a = gVar.f11368a;
                this.f11374b = gVar.f11369b;
                this.f11375c = gVar.f11370c;
                this.f11376d = gVar.f11371d;
                this.f11377e = gVar.f11372e;
            }

            public g f() {
                return new g(this);
            }

            public a g(long j8) {
                this.f11375c = j8;
                return this;
            }

            public a h(float f5) {
                this.f11377e = f5;
                return this;
            }

            public a i(long j8) {
                this.f11374b = j8;
                return this;
            }

            public a j(float f5) {
                this.f11376d = f5;
                return this;
            }

            public a k(long j8) {
                this.f11373a = j8;
                return this;
            }
        }

        @Deprecated
        public g(long j8, long j9, long j10, float f5, float f8) {
            this.f11368a = j8;
            this.f11369b = j9;
            this.f11370c = j10;
            this.f11371d = f5;
            this.f11372e = f8;
        }

        private g(a aVar) {
            this(aVar.f11373a, aVar.f11374b, aVar.f11375c, aVar.f11376d, aVar.f11377e);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ g c(Bundle bundle) {
            String str = f11362g;
            g gVar = f11361f;
            return new g(bundle.getLong(str, gVar.f11368a), bundle.getLong(f11363h, gVar.f11369b), bundle.getLong(f11364j, gVar.f11370c), bundle.getFloat(f11365k, gVar.f11371d), bundle.getFloat(f11366l, gVar.f11372e));
        }

        public a b() {
            return new a();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof g) {
                g gVar = (g) obj;
                return this.f11368a == gVar.f11368a && this.f11369b == gVar.f11369b && this.f11370c == gVar.f11370c && this.f11371d == gVar.f11371d && this.f11372e == gVar.f11372e;
            }
            return false;
        }

        public int hashCode() {
            long j8 = this.f11368a;
            long j9 = this.f11369b;
            long j10 = this.f11370c;
            int i8 = ((((((int) (j8 ^ (j8 >>> 32))) * 31) + ((int) (j9 ^ (j9 >>> 32)))) * 31) + ((int) ((j10 >>> 32) ^ j10))) * 31;
            float f5 = this.f11371d;
            int floatToIntBits = (i8 + (f5 != 0.0f ? Float.floatToIntBits(f5) : 0)) * 31;
            float f8 = this.f11372e;
            return floatToIntBits + (f8 != 0.0f ? Float.floatToIntBits(f8) : 0);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h {

        /* renamed from: a  reason: collision with root package name */
        public final Uri f11378a;

        /* renamed from: b  reason: collision with root package name */
        public final String f11379b;

        /* renamed from: c  reason: collision with root package name */
        public final f f11380c;

        /* renamed from: d  reason: collision with root package name */
        public final b f11381d;

        /* renamed from: e  reason: collision with root package name */
        public final List<StreamKey> f11382e;

        /* renamed from: f  reason: collision with root package name */
        public final String f11383f;

        /* renamed from: g  reason: collision with root package name */
        public final ImmutableList<l> f11384g;
        @Deprecated

        /* renamed from: h  reason: collision with root package name */
        public final List<k> f11385h;

        /* renamed from: i  reason: collision with root package name */
        public final Object f11386i;

        private h(Uri uri, String str, f fVar, b bVar, List<StreamKey> list, String str2, ImmutableList<l> immutableList, Object obj) {
            this.f11378a = uri;
            this.f11379b = str;
            this.f11380c = fVar;
            this.f11382e = list;
            this.f11383f = str2;
            this.f11384g = immutableList;
            ImmutableList.a u8 = ImmutableList.u();
            for (int i8 = 0; i8 < immutableList.size(); i8++) {
                u8.a(immutableList.get(i8).a().i());
            }
            this.f11385h = u8.k();
            this.f11386i = obj;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof h) {
                h hVar = (h) obj;
                return this.f11378a.equals(hVar.f11378a) && b6.l0.c(this.f11379b, hVar.f11379b) && b6.l0.c(this.f11380c, hVar.f11380c) && b6.l0.c(this.f11381d, hVar.f11381d) && this.f11382e.equals(hVar.f11382e) && b6.l0.c(this.f11383f, hVar.f11383f) && this.f11384g.equals(hVar.f11384g) && b6.l0.c(this.f11386i, hVar.f11386i);
            }
            return false;
        }

        public int hashCode() {
            int hashCode = this.f11378a.hashCode() * 31;
            String str = this.f11379b;
            int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
            f fVar = this.f11380c;
            int hashCode3 = (((((hashCode2 + (fVar == null ? 0 : fVar.hashCode())) * 31) + 0) * 31) + this.f11382e.hashCode()) * 31;
            String str2 = this.f11383f;
            int hashCode4 = (((hashCode3 + (str2 == null ? 0 : str2.hashCode())) * 31) + this.f11384g.hashCode()) * 31;
            Object obj = this.f11386i;
            return hashCode4 + (obj != null ? obj.hashCode() : 0);
        }
    }

    @Deprecated
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class i extends h {
        private i(Uri uri, String str, f fVar, b bVar, List<StreamKey> list, String str2, ImmutableList<l> immutableList, Object obj) {
            super(uri, str, fVar, bVar, list, str2, immutableList, obj);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class j implements com.google.android.exoplayer2.g {

        /* renamed from: d  reason: collision with root package name */
        public static final j f11387d = new a().d();

        /* renamed from: e  reason: collision with root package name */
        private static final String f11388e = b6.l0.r0(0);

        /* renamed from: f  reason: collision with root package name */
        private static final String f11389f = b6.l0.r0(1);

        /* renamed from: g  reason: collision with root package name */
        private static final String f11390g = b6.l0.r0(2);

        /* renamed from: h  reason: collision with root package name */
        public static final g.a<j> f11391h = i4.y.a;

        /* renamed from: a  reason: collision with root package name */
        public final Uri f11392a;

        /* renamed from: b  reason: collision with root package name */
        public final String f11393b;

        /* renamed from: c  reason: collision with root package name */
        public final Bundle f11394c;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a {

            /* renamed from: a  reason: collision with root package name */
            private Uri f11395a;

            /* renamed from: b  reason: collision with root package name */
            private String f11396b;

            /* renamed from: c  reason: collision with root package name */
            private Bundle f11397c;

            public j d() {
                return new j(this);
            }

            public a e(Bundle bundle) {
                this.f11397c = bundle;
                return this;
            }

            public a f(Uri uri) {
                this.f11395a = uri;
                return this;
            }

            public a g(String str) {
                this.f11396b = str;
                return this;
            }
        }

        private j(a aVar) {
            this.f11392a = aVar.f11395a;
            this.f11393b = aVar.f11396b;
            this.f11394c = aVar.f11397c;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ j b(Bundle bundle) {
            return new a().f((Uri) bundle.getParcelable(f11388e)).g(bundle.getString(f11389f)).e(bundle.getBundle(f11390g)).d();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof j) {
                j jVar = (j) obj;
                return b6.l0.c(this.f11392a, jVar.f11392a) && b6.l0.c(this.f11393b, jVar.f11393b);
            }
            return false;
        }

        public int hashCode() {
            Uri uri = this.f11392a;
            int hashCode = (uri == null ? 0 : uri.hashCode()) * 31;
            String str = this.f11393b;
            return hashCode + (str != null ? str.hashCode() : 0);
        }
    }

    @Deprecated
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class k extends l {
        private k(l.a aVar) {
            super(aVar);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class l {

        /* renamed from: a  reason: collision with root package name */
        public final Uri f11398a;

        /* renamed from: b  reason: collision with root package name */
        public final String f11399b;

        /* renamed from: c  reason: collision with root package name */
        public final String f11400c;

        /* renamed from: d  reason: collision with root package name */
        public final int f11401d;

        /* renamed from: e  reason: collision with root package name */
        public final int f11402e;

        /* renamed from: f  reason: collision with root package name */
        public final String f11403f;

        /* renamed from: g  reason: collision with root package name */
        public final String f11404g;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a {

            /* renamed from: a  reason: collision with root package name */
            private Uri f11405a;

            /* renamed from: b  reason: collision with root package name */
            private String f11406b;

            /* renamed from: c  reason: collision with root package name */
            private String f11407c;

            /* renamed from: d  reason: collision with root package name */
            private int f11408d;

            /* renamed from: e  reason: collision with root package name */
            private int f11409e;

            /* renamed from: f  reason: collision with root package name */
            private String f11410f;

            /* renamed from: g  reason: collision with root package name */
            private String f11411g;

            private a(l lVar) {
                this.f11405a = lVar.f11398a;
                this.f11406b = lVar.f11399b;
                this.f11407c = lVar.f11400c;
                this.f11408d = lVar.f11401d;
                this.f11409e = lVar.f11402e;
                this.f11410f = lVar.f11403f;
                this.f11411g = lVar.f11404g;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public k i() {
                return new k(this);
            }
        }

        private l(a aVar) {
            this.f11398a = aVar.f11405a;
            this.f11399b = aVar.f11406b;
            this.f11400c = aVar.f11407c;
            this.f11401d = aVar.f11408d;
            this.f11402e = aVar.f11409e;
            this.f11403f = aVar.f11410f;
            this.f11404g = aVar.f11411g;
        }

        public a a() {
            return new a();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof l) {
                l lVar = (l) obj;
                return this.f11398a.equals(lVar.f11398a) && b6.l0.c(this.f11399b, lVar.f11399b) && b6.l0.c(this.f11400c, lVar.f11400c) && this.f11401d == lVar.f11401d && this.f11402e == lVar.f11402e && b6.l0.c(this.f11403f, lVar.f11403f) && b6.l0.c(this.f11404g, lVar.f11404g);
            }
            return false;
        }

        public int hashCode() {
            int hashCode = this.f11398a.hashCode() * 31;
            String str = this.f11399b;
            int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
            String str2 = this.f11400c;
            int hashCode3 = (((((hashCode2 + (str2 == null ? 0 : str2.hashCode())) * 31) + this.f11401d) * 31) + this.f11402e) * 31;
            String str3 = this.f11403f;
            int hashCode4 = (hashCode3 + (str3 == null ? 0 : str3.hashCode())) * 31;
            String str4 = this.f11404g;
            return hashCode4 + (str4 != null ? str4.hashCode() : 0);
        }
    }

    private z0(String str, e eVar, i iVar, g gVar, a1 a1Var, j jVar) {
        this.f11303a = str;
        this.f11304b = iVar;
        this.f11305c = iVar;
        this.f11306d = gVar;
        this.f11307e = a1Var;
        this.f11308f = eVar;
        this.f11309g = eVar;
        this.f11310h = jVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static z0 c(Bundle bundle) {
        String str = (String) b6.a.e(bundle.getString(f11298k, BuildConfig.FLAVOR));
        Bundle bundle2 = bundle.getBundle(f11299l);
        g a9 = bundle2 == null ? g.f11361f : g.f11367m.a(bundle2);
        Bundle bundle3 = bundle.getBundle(f11300m);
        a1 a10 = bundle3 == null ? a1.W : a1.E0.a(bundle3);
        Bundle bundle4 = bundle.getBundle(f11301n);
        e a11 = bundle4 == null ? e.f11341n : d.f11330m.a(bundle4);
        Bundle bundle5 = bundle.getBundle(f11302p);
        return new z0(str, a11, null, a9, a10, bundle5 == null ? j.f11387d : j.f11391h.a(bundle5));
    }

    public static z0 d(Uri uri) {
        return new c().f(uri).a();
    }

    public c b() {
        return new c();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof z0) {
            z0 z0Var = (z0) obj;
            return b6.l0.c(this.f11303a, z0Var.f11303a) && this.f11308f.equals(z0Var.f11308f) && b6.l0.c(this.f11304b, z0Var.f11304b) && b6.l0.c(this.f11306d, z0Var.f11306d) && b6.l0.c(this.f11307e, z0Var.f11307e) && b6.l0.c(this.f11310h, z0Var.f11310h);
        }
        return false;
    }

    public int hashCode() {
        int hashCode = this.f11303a.hashCode() * 31;
        h hVar = this.f11304b;
        return ((((((((hashCode + (hVar != null ? hVar.hashCode() : 0)) * 31) + this.f11306d.hashCode()) * 31) + this.f11308f.hashCode()) * 31) + this.f11307e.hashCode()) * 31) + this.f11310h.hashCode();
    }
}
