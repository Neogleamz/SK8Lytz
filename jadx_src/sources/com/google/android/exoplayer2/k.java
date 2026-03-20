package com.google.android.exoplayer2;

import android.content.Context;
import android.os.Looper;
import com.google.android.exoplayer2.h;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.util.PriorityTaskManager;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface k extends y1 {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        default void H(boolean z4) {
        }

        default void I(boolean z4) {
        }

        default void z(boolean z4) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {
        boolean A;
        Looper B;
        boolean C;

        /* renamed from: a  reason: collision with root package name */
        final Context f9826a;

        /* renamed from: b  reason: collision with root package name */
        b6.d f9827b;

        /* renamed from: c  reason: collision with root package name */
        long f9828c;

        /* renamed from: d  reason: collision with root package name */
        com.google.common.base.r<i4.h0> f9829d;

        /* renamed from: e  reason: collision with root package name */
        com.google.common.base.r<k.a> f9830e;

        /* renamed from: f  reason: collision with root package name */
        com.google.common.base.r<z5.a0> f9831f;

        /* renamed from: g  reason: collision with root package name */
        com.google.common.base.r<i4.u> f9832g;

        /* renamed from: h  reason: collision with root package name */
        com.google.common.base.r<a6.d> f9833h;

        /* renamed from: i  reason: collision with root package name */
        com.google.common.base.g<b6.d, j4.a> f9834i;

        /* renamed from: j  reason: collision with root package name */
        Looper f9835j;

        /* renamed from: k  reason: collision with root package name */
        PriorityTaskManager f9836k;

        /* renamed from: l  reason: collision with root package name */
        com.google.android.exoplayer2.audio.a f9837l;

        /* renamed from: m  reason: collision with root package name */
        boolean f9838m;

        /* renamed from: n  reason: collision with root package name */
        int f9839n;

        /* renamed from: o  reason: collision with root package name */
        boolean f9840o;

        /* renamed from: p  reason: collision with root package name */
        boolean f9841p;
        int q;

        /* renamed from: r  reason: collision with root package name */
        int f9842r;

        /* renamed from: s  reason: collision with root package name */
        boolean f9843s;

        /* renamed from: t  reason: collision with root package name */
        i4.i0 f9844t;

        /* renamed from: u  reason: collision with root package name */
        long f9845u;

        /* renamed from: v  reason: collision with root package name */
        long f9846v;

        /* renamed from: w  reason: collision with root package name */
        y0 f9847w;

        /* renamed from: x  reason: collision with root package name */
        long f9848x;

        /* renamed from: y  reason: collision with root package name */
        long f9849y;

        /* renamed from: z  reason: collision with root package name */
        boolean f9850z;

        public b(Context context) {
            this(context, new i4.i(context), new i4.k(context));
        }

        private b(Context context, com.google.common.base.r<i4.h0> rVar, com.google.common.base.r<k.a> rVar2) {
            this(context, rVar, rVar2, new i4.j(context), i4.n.a, new i4.h(context), i4.g.a);
        }

        private b(Context context, com.google.common.base.r<i4.h0> rVar, com.google.common.base.r<k.a> rVar2, com.google.common.base.r<z5.a0> rVar3, com.google.common.base.r<i4.u> rVar4, com.google.common.base.r<a6.d> rVar5, com.google.common.base.g<b6.d, j4.a> gVar) {
            this.f9826a = (Context) b6.a.e(context);
            this.f9829d = rVar;
            this.f9830e = rVar2;
            this.f9831f = rVar3;
            this.f9832g = rVar4;
            this.f9833h = rVar5;
            this.f9834i = gVar;
            this.f9835j = b6.l0.Q();
            this.f9837l = com.google.android.exoplayer2.audio.a.f9313g;
            this.f9839n = 0;
            this.q = 1;
            this.f9842r = 0;
            this.f9843s = true;
            this.f9844t = i4.i0.f20506g;
            this.f9845u = 5000L;
            this.f9846v = 15000L;
            this.f9847w = new h.b().a();
            this.f9827b = b6.d.f8029a;
            this.f9848x = 500L;
            this.f9849y = 2000L;
            this.A = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ i4.h0 h(Context context) {
            return new i4.d(context);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ k.a i(Context context) {
            return new com.google.android.exoplayer2.source.e(context, new n4.h());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ z5.a0 j(Context context) {
            return new z5.m(context);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ i4.u l(i4.u uVar) {
            return uVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ i4.h0 m(i4.h0 h0Var) {
            return h0Var;
        }

        public k g() {
            b6.a.f(!this.C);
            this.C = true;
            return new k0(this, null);
        }

        public b n(y0 y0Var) {
            b6.a.f(!this.C);
            this.f9847w = (y0) b6.a.e(y0Var);
            return this;
        }

        public b o(i4.u uVar) {
            b6.a.f(!this.C);
            b6.a.e(uVar);
            this.f9832g = new i4.l(uVar);
            return this;
        }

        public b p(i4.h0 h0Var) {
            b6.a.f(!this.C);
            b6.a.e(h0Var);
            this.f9829d = new i4.m(h0Var);
            return this;
        }
    }

    w0 A();

    void C(boolean z4);

    int L();

    void N(com.google.android.exoplayer2.audio.a aVar, boolean z4);

    void f(boolean z4);

    void n(com.google.android.exoplayer2.source.k kVar);
}
