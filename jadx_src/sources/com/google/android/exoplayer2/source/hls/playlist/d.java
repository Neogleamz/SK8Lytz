package com.google.android.exoplayer2.source.hls.playlist;

import android.net.Uri;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d extends n5.d {

    /* renamed from: d  reason: collision with root package name */
    public final int f10544d;

    /* renamed from: e  reason: collision with root package name */
    public final long f10545e;

    /* renamed from: f  reason: collision with root package name */
    public final boolean f10546f;

    /* renamed from: g  reason: collision with root package name */
    public final boolean f10547g;

    /* renamed from: h  reason: collision with root package name */
    public final long f10548h;

    /* renamed from: i  reason: collision with root package name */
    public final boolean f10549i;

    /* renamed from: j  reason: collision with root package name */
    public final int f10550j;

    /* renamed from: k  reason: collision with root package name */
    public final long f10551k;

    /* renamed from: l  reason: collision with root package name */
    public final int f10552l;

    /* renamed from: m  reason: collision with root package name */
    public final long f10553m;

    /* renamed from: n  reason: collision with root package name */
    public final long f10554n;

    /* renamed from: o  reason: collision with root package name */
    public final boolean f10555o;

    /* renamed from: p  reason: collision with root package name */
    public final boolean f10556p;
    public final DrmInitData q;

    /* renamed from: r  reason: collision with root package name */
    public final List<C0112d> f10557r;

    /* renamed from: s  reason: collision with root package name */
    public final List<b> f10558s;

    /* renamed from: t  reason: collision with root package name */
    public final Map<Uri, c> f10559t;

    /* renamed from: u  reason: collision with root package name */
    public final long f10560u;

    /* renamed from: v  reason: collision with root package name */
    public final f f10561v;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends e {

        /* renamed from: m  reason: collision with root package name */
        public final boolean f10562m;

        /* renamed from: n  reason: collision with root package name */
        public final boolean f10563n;

        public b(String str, C0112d c0112d, long j8, int i8, long j9, DrmInitData drmInitData, String str2, String str3, long j10, long j11, boolean z4, boolean z8, boolean z9) {
            super(str, c0112d, j8, i8, j9, drmInitData, str2, str3, j10, j11, z4);
            this.f10562m = z8;
            this.f10563n = z9;
        }

        public b f(long j8, int i8) {
            return new b(this.f10569a, this.f10570b, this.f10571c, i8, j8, this.f10574f, this.f10575g, this.f10576h, this.f10577j, this.f10578k, this.f10579l, this.f10562m, this.f10563n);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        public final Uri f10564a;

        /* renamed from: b  reason: collision with root package name */
        public final long f10565b;

        /* renamed from: c  reason: collision with root package name */
        public final int f10566c;

        public c(Uri uri, long j8, int i8) {
            this.f10564a = uri;
            this.f10565b = j8;
            this.f10566c = i8;
        }
    }

    /* renamed from: com.google.android.exoplayer2.source.hls.playlist.d$d  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0112d extends e {

        /* renamed from: m  reason: collision with root package name */
        public final String f10567m;

        /* renamed from: n  reason: collision with root package name */
        public final List<b> f10568n;

        public C0112d(String str, long j8, long j9, String str2, String str3) {
            this(str, null, BuildConfig.FLAVOR, 0L, -1, -9223372036854775807L, null, str2, str3, j8, j9, false, ImmutableList.E());
        }

        public C0112d(String str, C0112d c0112d, String str2, long j8, int i8, long j9, DrmInitData drmInitData, String str3, String str4, long j10, long j11, boolean z4, List<b> list) {
            super(str, c0112d, j8, i8, j9, drmInitData, str3, str4, j10, j11, z4);
            this.f10567m = str2;
            this.f10568n = ImmutableList.x(list);
        }

        public C0112d f(long j8, int i8) {
            ArrayList arrayList = new ArrayList();
            long j9 = j8;
            for (int i9 = 0; i9 < this.f10568n.size(); i9++) {
                b bVar = this.f10568n.get(i9);
                arrayList.add(bVar.f(j9, i8));
                j9 += bVar.f10571c;
            }
            return new C0112d(this.f10569a, this.f10570b, this.f10567m, this.f10571c, i8, j8, this.f10574f, this.f10575g, this.f10576h, this.f10577j, this.f10578k, this.f10579l, arrayList);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e implements Comparable<Long> {

        /* renamed from: a  reason: collision with root package name */
        public final String f10569a;

        /* renamed from: b  reason: collision with root package name */
        public final C0112d f10570b;

        /* renamed from: c  reason: collision with root package name */
        public final long f10571c;

        /* renamed from: d  reason: collision with root package name */
        public final int f10572d;

        /* renamed from: e  reason: collision with root package name */
        public final long f10573e;

        /* renamed from: f  reason: collision with root package name */
        public final DrmInitData f10574f;

        /* renamed from: g  reason: collision with root package name */
        public final String f10575g;

        /* renamed from: h  reason: collision with root package name */
        public final String f10576h;

        /* renamed from: j  reason: collision with root package name */
        public final long f10577j;

        /* renamed from: k  reason: collision with root package name */
        public final long f10578k;

        /* renamed from: l  reason: collision with root package name */
        public final boolean f10579l;

        private e(String str, C0112d c0112d, long j8, int i8, long j9, DrmInitData drmInitData, String str2, String str3, long j10, long j11, boolean z4) {
            this.f10569a = str;
            this.f10570b = c0112d;
            this.f10571c = j8;
            this.f10572d = i8;
            this.f10573e = j9;
            this.f10574f = drmInitData;
            this.f10575g = str2;
            this.f10576h = str3;
            this.f10577j = j10;
            this.f10578k = j11;
            this.f10579l = z4;
        }

        @Override // java.lang.Comparable
        /* renamed from: c */
        public int compareTo(Long l8) {
            if (this.f10573e > l8.longValue()) {
                return 1;
            }
            return this.f10573e < l8.longValue() ? -1 : 0;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class f {

        /* renamed from: a  reason: collision with root package name */
        public final long f10580a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean f10581b;

        /* renamed from: c  reason: collision with root package name */
        public final long f10582c;

        /* renamed from: d  reason: collision with root package name */
        public final long f10583d;

        /* renamed from: e  reason: collision with root package name */
        public final boolean f10584e;

        public f(long j8, boolean z4, long j9, long j10, boolean z8) {
            this.f10580a = j8;
            this.f10581b = z4;
            this.f10582c = j9;
            this.f10583d = j10;
            this.f10584e = z8;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0091  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public d(int r11, java.lang.String r12, java.util.List<java.lang.String> r13, long r14, boolean r16, long r17, boolean r19, int r20, long r21, int r23, long r24, long r26, boolean r28, boolean r29, boolean r30, com.google.android.exoplayer2.drm.DrmInitData r31, java.util.List<com.google.android.exoplayer2.source.hls.playlist.d.C0112d> r32, java.util.List<com.google.android.exoplayer2.source.hls.playlist.d.b> r33, com.google.android.exoplayer2.source.hls.playlist.d.f r34, java.util.Map<android.net.Uri, com.google.android.exoplayer2.source.hls.playlist.d.c> r35) {
        /*
            r10 = this;
            r0 = r10
            r1 = r14
            r3 = r12
            r4 = r13
            r5 = r28
            r10.<init>(r12, r13, r5)
            r3 = r11
            r0.f10544d = r3
            r3 = r17
            r0.f10548h = r3
            r3 = r16
            r0.f10547g = r3
            r3 = r19
            r0.f10549i = r3
            r3 = r20
            r0.f10550j = r3
            r3 = r21
            r0.f10551k = r3
            r3 = r23
            r0.f10552l = r3
            r3 = r24
            r0.f10553m = r3
            r3 = r26
            r0.f10554n = r3
            r3 = r29
            r0.f10555o = r3
            r3 = r30
            r0.f10556p = r3
            r3 = r31
            r0.q = r3
            com.google.common.collect.ImmutableList r3 = com.google.common.collect.ImmutableList.x(r32)
            r0.f10557r = r3
            com.google.common.collect.ImmutableList r3 = com.google.common.collect.ImmutableList.x(r33)
            r0.f10558s = r3
            com.google.common.collect.ImmutableMap r3 = com.google.common.collect.ImmutableMap.c(r35)
            r0.f10559t = r3
            boolean r3 = r33.isEmpty()
            r4 = 0
            if (r3 != 0) goto L60
            java.lang.Object r3 = com.google.common.collect.f1.f(r33)
            com.google.android.exoplayer2.source.hls.playlist.d$b r3 = (com.google.android.exoplayer2.source.hls.playlist.d.b) r3
        L58:
            long r6 = r3.f10573e
            long r8 = r3.f10571c
            long r6 = r6 + r8
            r0.f10560u = r6
            goto L6f
        L60:
            boolean r3 = r32.isEmpty()
            if (r3 != 0) goto L6d
            java.lang.Object r3 = com.google.common.collect.f1.f(r32)
            com.google.android.exoplayer2.source.hls.playlist.d$d r3 = (com.google.android.exoplayer2.source.hls.playlist.d.C0112d) r3
            goto L58
        L6d:
            r0.f10560u = r4
        L6f:
            r6 = -9223372036854775807(0x8000000000000001, double:-4.9E-324)
            int r3 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
            if (r3 != 0) goto L79
            goto L89
        L79:
            int r3 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            long r6 = r0.f10560u
            if (r3 < 0) goto L84
            long r6 = java.lang.Math.min(r6, r14)
            goto L89
        L84:
            long r6 = r6 + r1
            long r6 = java.lang.Math.max(r4, r6)
        L89:
            r0.f10545e = r6
            int r1 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r1 < 0) goto L91
            r1 = 1
            goto L92
        L91:
            r1 = 0
        L92:
            r0.f10546f = r1
            r1 = r34
            r0.f10561v = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.hls.playlist.d.<init>(int, java.lang.String, java.util.List, long, boolean, long, boolean, int, long, int, long, long, boolean, boolean, boolean, com.google.android.exoplayer2.drm.DrmInitData, java.util.List, java.util.List, com.google.android.exoplayer2.source.hls.playlist.d$f, java.util.Map):void");
    }

    @Override // g5.b
    /* renamed from: b */
    public d a(List<StreamKey> list) {
        return this;
    }

    public d c(long j8, int i8) {
        return new d(this.f10544d, this.f22159a, this.f22160b, this.f10545e, this.f10547g, j8, true, i8, this.f10551k, this.f10552l, this.f10553m, this.f10554n, this.f22161c, this.f10555o, this.f10556p, this.q, this.f10557r, this.f10558s, this.f10561v, this.f10559t);
    }

    public d d() {
        return this.f10555o ? this : new d(this.f10544d, this.f22159a, this.f22160b, this.f10545e, this.f10547g, this.f10548h, this.f10549i, this.f10550j, this.f10551k, this.f10552l, this.f10553m, this.f10554n, this.f22161c, true, this.f10556p, this.q, this.f10557r, this.f10558s, this.f10561v, this.f10559t);
    }

    public long e() {
        return this.f10548h + this.f10560u;
    }

    public boolean f(d dVar) {
        if (dVar != null) {
            long j8 = this.f10551k;
            long j9 = dVar.f10551k;
            if (j8 > j9) {
                return true;
            }
            if (j8 < j9) {
                return false;
            }
            int size = this.f10557r.size() - dVar.f10557r.size();
            if (size != 0) {
                return size > 0;
            }
            int size2 = this.f10558s.size();
            int size3 = dVar.f10558s.size();
            if (size2 <= size3) {
                return size2 == size3 && this.f10555o && !dVar.f10555o;
            }
            return true;
        }
        return true;
    }
}
