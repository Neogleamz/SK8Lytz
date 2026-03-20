package com.google.android.exoplayer2;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.k;
import com.google.common.collect.ImmutableList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w1 {

    /* renamed from: s  reason: collision with root package name */
    private static final k.b f11240s = new k.b(new Object());

    /* renamed from: a  reason: collision with root package name */
    public final h2 f11241a;

    /* renamed from: b  reason: collision with root package name */
    public final k.b f11242b;

    /* renamed from: c  reason: collision with root package name */
    public final long f11243c;

    /* renamed from: d  reason: collision with root package name */
    public final long f11244d;

    /* renamed from: e  reason: collision with root package name */
    public final int f11245e;

    /* renamed from: f  reason: collision with root package name */
    public final ExoPlaybackException f11246f;

    /* renamed from: g  reason: collision with root package name */
    public final boolean f11247g;

    /* renamed from: h  reason: collision with root package name */
    public final h5.w f11248h;

    /* renamed from: i  reason: collision with root package name */
    public final z5.b0 f11249i;

    /* renamed from: j  reason: collision with root package name */
    public final List<Metadata> f11250j;

    /* renamed from: k  reason: collision with root package name */
    public final k.b f11251k;

    /* renamed from: l  reason: collision with root package name */
    public final boolean f11252l;

    /* renamed from: m  reason: collision with root package name */
    public final int f11253m;

    /* renamed from: n  reason: collision with root package name */
    public final x1 f11254n;

    /* renamed from: o  reason: collision with root package name */
    public final boolean f11255o;

    /* renamed from: p  reason: collision with root package name */
    public volatile long f11256p;
    public volatile long q;

    /* renamed from: r  reason: collision with root package name */
    public volatile long f11257r;

    public w1(h2 h2Var, k.b bVar, long j8, long j9, int i8, ExoPlaybackException exoPlaybackException, boolean z4, h5.w wVar, z5.b0 b0Var, List<Metadata> list, k.b bVar2, boolean z8, int i9, x1 x1Var, long j10, long j11, long j12, boolean z9) {
        this.f11241a = h2Var;
        this.f11242b = bVar;
        this.f11243c = j8;
        this.f11244d = j9;
        this.f11245e = i8;
        this.f11246f = exoPlaybackException;
        this.f11247g = z4;
        this.f11248h = wVar;
        this.f11249i = b0Var;
        this.f11250j = list;
        this.f11251k = bVar2;
        this.f11252l = z8;
        this.f11253m = i9;
        this.f11254n = x1Var;
        this.f11256p = j10;
        this.q = j11;
        this.f11257r = j12;
        this.f11255o = z9;
    }

    public static w1 j(z5.b0 b0Var) {
        h2 h2Var = h2.f9745a;
        k.b bVar = f11240s;
        return new w1(h2Var, bVar, -9223372036854775807L, 0L, 1, null, false, h5.w.f20313d, b0Var, ImmutableList.E(), bVar, false, 0, x1.f11264d, 0L, 0L, 0L, false);
    }

    public static k.b k() {
        return f11240s;
    }

    public w1 a(boolean z4) {
        return new w1(this.f11241a, this.f11242b, this.f11243c, this.f11244d, this.f11245e, this.f11246f, z4, this.f11248h, this.f11249i, this.f11250j, this.f11251k, this.f11252l, this.f11253m, this.f11254n, this.f11256p, this.q, this.f11257r, this.f11255o);
    }

    public w1 b(k.b bVar) {
        return new w1(this.f11241a, this.f11242b, this.f11243c, this.f11244d, this.f11245e, this.f11246f, this.f11247g, this.f11248h, this.f11249i, this.f11250j, bVar, this.f11252l, this.f11253m, this.f11254n, this.f11256p, this.q, this.f11257r, this.f11255o);
    }

    public w1 c(k.b bVar, long j8, long j9, long j10, long j11, h5.w wVar, z5.b0 b0Var, List<Metadata> list) {
        return new w1(this.f11241a, bVar, j9, j10, this.f11245e, this.f11246f, this.f11247g, wVar, b0Var, list, this.f11251k, this.f11252l, this.f11253m, this.f11254n, this.f11256p, j11, j8, this.f11255o);
    }

    public w1 d(boolean z4, int i8) {
        return new w1(this.f11241a, this.f11242b, this.f11243c, this.f11244d, this.f11245e, this.f11246f, this.f11247g, this.f11248h, this.f11249i, this.f11250j, this.f11251k, z4, i8, this.f11254n, this.f11256p, this.q, this.f11257r, this.f11255o);
    }

    public w1 e(ExoPlaybackException exoPlaybackException) {
        return new w1(this.f11241a, this.f11242b, this.f11243c, this.f11244d, this.f11245e, exoPlaybackException, this.f11247g, this.f11248h, this.f11249i, this.f11250j, this.f11251k, this.f11252l, this.f11253m, this.f11254n, this.f11256p, this.q, this.f11257r, this.f11255o);
    }

    public w1 f(x1 x1Var) {
        return new w1(this.f11241a, this.f11242b, this.f11243c, this.f11244d, this.f11245e, this.f11246f, this.f11247g, this.f11248h, this.f11249i, this.f11250j, this.f11251k, this.f11252l, this.f11253m, x1Var, this.f11256p, this.q, this.f11257r, this.f11255o);
    }

    public w1 g(int i8) {
        return new w1(this.f11241a, this.f11242b, this.f11243c, this.f11244d, i8, this.f11246f, this.f11247g, this.f11248h, this.f11249i, this.f11250j, this.f11251k, this.f11252l, this.f11253m, this.f11254n, this.f11256p, this.q, this.f11257r, this.f11255o);
    }

    public w1 h(boolean z4) {
        return new w1(this.f11241a, this.f11242b, this.f11243c, this.f11244d, this.f11245e, this.f11246f, this.f11247g, this.f11248h, this.f11249i, this.f11250j, this.f11251k, this.f11252l, this.f11253m, this.f11254n, this.f11256p, this.q, this.f11257r, z4);
    }

    public w1 i(h2 h2Var) {
        return new w1(h2Var, this.f11242b, this.f11243c, this.f11244d, this.f11245e, this.f11246f, this.f11247g, this.f11248h, this.f11249i, this.f11250j, this.f11251k, this.f11252l, this.f11253m, this.f11254n, this.f11256p, this.q, this.f11257r, this.f11255o);
    }
}
