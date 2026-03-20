package com.google.android.exoplayer2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioTrack;
import android.media.MediaFormat;
import android.media.metrics.LogSessionId;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import b6.o;
import com.google.android.exoplayer2.b;
import com.google.android.exoplayer2.d;
import com.google.android.exoplayer2.e2;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.k;
import com.google.android.exoplayer2.k0;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.source.x;
import com.google.android.exoplayer2.t1;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import com.google.android.exoplayer2.v0;
import com.google.android.exoplayer2.video.spherical.SphericalGLSurfaceView;
import com.google.android.exoplayer2.y1;
import com.google.android.exoplayer2.z1;
import com.google.android.gms.dynamite.descriptors.com.google.mlkit.dynamite.barcode.ModuleDescriptor;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeoutException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k0 extends com.google.android.exoplayer2.e implements k {
    private final com.google.android.exoplayer2.d A;
    private final e2 B;
    private final j2 C;
    private final k2 D;
    private final long E;
    private int F;
    private boolean G;
    private int H;
    private int I;
    private boolean J;
    private int K;
    private i4.i0 L;
    private com.google.android.exoplayer2.source.x M;
    private boolean N;
    private y1.b O;
    private a1 P;
    private a1 Q;
    private w0 R;
    private w0 S;
    private AudioTrack T;
    private Object U;
    private Surface V;
    private SurfaceHolder W;
    private SphericalGLSurfaceView X;
    private boolean Y;
    private TextureView Z;

    /* renamed from: a0  reason: collision with root package name */
    private int f9851a0;

    /* renamed from: b  reason: collision with root package name */
    final z5.b0 f9852b;

    /* renamed from: b0  reason: collision with root package name */
    private int f9853b0;

    /* renamed from: c  reason: collision with root package name */
    final y1.b f9854c;

    /* renamed from: c0  reason: collision with root package name */
    private b6.b0 f9855c0;

    /* renamed from: d  reason: collision with root package name */
    private final b6.g f9856d;

    /* renamed from: d0  reason: collision with root package name */
    private l4.e f9857d0;

    /* renamed from: e  reason: collision with root package name */
    private final Context f9858e;

    /* renamed from: e0  reason: collision with root package name */
    private l4.e f9859e0;

    /* renamed from: f  reason: collision with root package name */
    private final y1 f9860f;

    /* renamed from: f0  reason: collision with root package name */
    private int f9861f0;

    /* renamed from: g  reason: collision with root package name */
    private final c2[] f9862g;

    /* renamed from: g0  reason: collision with root package name */
    private com.google.android.exoplayer2.audio.a f9863g0;

    /* renamed from: h  reason: collision with root package name */
    private final z5.a0 f9864h;

    /* renamed from: h0  reason: collision with root package name */
    private float f9865h0;

    /* renamed from: i  reason: collision with root package name */
    private final b6.l f9866i;

    /* renamed from: i0  reason: collision with root package name */
    private boolean f9867i0;

    /* renamed from: j  reason: collision with root package name */
    private final v0.f f9868j;

    /* renamed from: j0  reason: collision with root package name */
    private p5.e f9869j0;

    /* renamed from: k  reason: collision with root package name */
    private final v0 f9870k;

    /* renamed from: k0  reason: collision with root package name */
    private boolean f9871k0;

    /* renamed from: l  reason: collision with root package name */
    private final b6.o<y1.d> f9872l;

    /* renamed from: l0  reason: collision with root package name */
    private boolean f9873l0;

    /* renamed from: m  reason: collision with root package name */
    private final CopyOnWriteArraySet<k.a> f9874m;

    /* renamed from: m0  reason: collision with root package name */
    private PriorityTaskManager f9875m0;

    /* renamed from: n  reason: collision with root package name */
    private final h2.b f9876n;

    /* renamed from: n0  reason: collision with root package name */
    private boolean f9877n0;

    /* renamed from: o  reason: collision with root package name */
    private final List<e> f9878o;

    /* renamed from: o0  reason: collision with root package name */
    private boolean f9879o0;

    /* renamed from: p  reason: collision with root package name */
    private final boolean f9880p;

    /* renamed from: p0  reason: collision with root package name */
    private j f9881p0;
    private final k.a q;

    /* renamed from: q0  reason: collision with root package name */
    private c6.x f9882q0;

    /* renamed from: r  reason: collision with root package name */
    private final j4.a f9883r;

    /* renamed from: r0  reason: collision with root package name */
    private a1 f9884r0;

    /* renamed from: s  reason: collision with root package name */
    private final Looper f9885s;

    /* renamed from: s0  reason: collision with root package name */
    private w1 f9886s0;

    /* renamed from: t  reason: collision with root package name */
    private final a6.d f9887t;

    /* renamed from: t0  reason: collision with root package name */
    private int f9888t0;

    /* renamed from: u  reason: collision with root package name */
    private final long f9889u;

    /* renamed from: u0  reason: collision with root package name */
    private int f9890u0;

    /* renamed from: v  reason: collision with root package name */
    private final long f9891v;

    /* renamed from: v0  reason: collision with root package name */
    private long f9892v0;

    /* renamed from: w  reason: collision with root package name */
    private final b6.d f9893w;

    /* renamed from: x  reason: collision with root package name */
    private final c f9894x;

    /* renamed from: y  reason: collision with root package name */
    private final d f9895y;

    /* renamed from: z  reason: collision with root package name */
    private final com.google.android.exoplayer2.b f9896z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b {
        public static j4.t1 a(Context context, k0 k0Var, boolean z4) {
            j4.r1 B0 = j4.r1.B0(context);
            if (B0 == null) {
                b6.p.i("ExoPlayerImpl", "MediaMetricsService unavailable.");
                return new j4.t1(LogSessionId.LOG_SESSION_ID_NONE);
            }
            if (z4) {
                k0Var.Z0(B0);
            }
            return new j4.t1(B0.I0());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class c implements c6.v, com.google.android.exoplayer2.audio.b, p5.m, a5.d, SurfaceHolder.Callback, TextureView.SurfaceTextureListener, SphericalGLSurfaceView.b, d.b, b.InterfaceC0105b, e2.b, k.a {
        private c() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void T(y1.d dVar) {
            dVar.T(k0.this.P);
        }

        @Override // com.google.android.exoplayer2.d.b
        public void A(float f5) {
            k0.this.c2();
        }

        @Override // com.google.android.exoplayer2.d.b
        public void B(int i8) {
            boolean k8 = k0.this.k();
            k0.this.l2(k8, i8, k0.n1(k8, i8));
        }

        @Override // com.google.android.exoplayer2.video.spherical.SphericalGLSurfaceView.b
        public void C(Surface surface) {
            k0.this.h2(null);
        }

        @Override // com.google.android.exoplayer2.video.spherical.SphericalGLSurfaceView.b
        public void D(Surface surface) {
            k0.this.h2(surface);
        }

        @Override // com.google.android.exoplayer2.e2.b
        public void E(final int i8, final boolean z4) {
            k0.this.f9872l.k(30, new o.a() { // from class: com.google.android.exoplayer2.l0
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    ((y1.d) obj).Y(i8, z4);
                }
            });
        }

        @Override // com.google.android.exoplayer2.audio.b
        public void a(final boolean z4) {
            if (k0.this.f9867i0 == z4) {
                return;
            }
            k0.this.f9867i0 = z4;
            k0.this.f9872l.k(23, new o.a() { // from class: com.google.android.exoplayer2.s0
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    ((y1.d) obj).a(z4);
                }
            });
        }

        @Override // com.google.android.exoplayer2.audio.b
        public void b(Exception exc) {
            k0.this.f9883r.b(exc);
        }

        @Override // c6.v
        public void c(String str) {
            k0.this.f9883r.c(str);
        }

        @Override // c6.v
        public void d(String str, long j8, long j9) {
            k0.this.f9883r.d(str, j8, j9);
        }

        @Override // c6.v
        public void e(l4.e eVar) {
            k0.this.f9857d0 = eVar;
            k0.this.f9883r.e(eVar);
        }

        @Override // com.google.android.exoplayer2.audio.b
        public void f(String str) {
            k0.this.f9883r.f(str);
        }

        @Override // com.google.android.exoplayer2.audio.b
        public void g(String str, long j8, long j9) {
            k0.this.f9883r.g(str, j8, j9);
        }

        @Override // a5.d
        public void h(final Metadata metadata) {
            k0 k0Var = k0.this;
            k0Var.f9884r0 = k0Var.f9884r0.b().K(metadata).H();
            a1 c12 = k0.this.c1();
            if (!c12.equals(k0.this.P)) {
                k0.this.P = c12;
                k0.this.f9872l.i(14, new o.a() { // from class: com.google.android.exoplayer2.o0
                    @Override // b6.o.a
                    public final void invoke(Object obj) {
                        k0.c.this.T((y1.d) obj);
                    }
                });
            }
            k0.this.f9872l.i(28, new o.a() { // from class: com.google.android.exoplayer2.p0
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    ((y1.d) obj).h(Metadata.this);
                }
            });
            k0.this.f9872l.f();
        }

        @Override // c6.v
        public void i(int i8, long j8) {
            k0.this.f9883r.i(i8, j8);
        }

        @Override // com.google.android.exoplayer2.audio.b
        public void j(l4.e eVar) {
            k0.this.f9883r.j(eVar);
            k0.this.S = null;
            k0.this.f9859e0 = null;
        }

        @Override // c6.v
        public void k(Object obj, long j8) {
            k0.this.f9883r.k(obj, j8);
            if (k0.this.U == obj) {
                k0.this.f9872l.k(26, i4.p.a);
            }
        }

        @Override // com.google.android.exoplayer2.e2.b
        public void l(int i8) {
            final j d12 = k0.d1(k0.this.B);
            if (d12.equals(k0.this.f9881p0)) {
                return;
            }
            k0.this.f9881p0 = d12;
            k0.this.f9872l.k(29, new o.a() { // from class: com.google.android.exoplayer2.n0
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    ((y1.d) obj).Q(j.this);
                }
            });
        }

        @Override // com.google.android.exoplayer2.audio.b
        public void m(l4.e eVar) {
            k0.this.f9859e0 = eVar;
            k0.this.f9883r.m(eVar);
        }

        @Override // com.google.android.exoplayer2.audio.b
        public void n(w0 w0Var, l4.g gVar) {
            k0.this.S = w0Var;
            k0.this.f9883r.n(w0Var, gVar);
        }

        @Override // c6.v
        public void o(l4.e eVar) {
            k0.this.f9883r.o(eVar);
            k0.this.R = null;
            k0.this.f9857d0 = null;
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i8, int i9) {
            k0.this.g2(surfaceTexture);
            k0.this.W1(i8, i9);
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            k0.this.h2(null);
            k0.this.W1(0, 0);
            return true;
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i8, int i9) {
            k0.this.W1(i8, i9);
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        @Override // c6.v
        public void p(final c6.x xVar) {
            k0.this.f9882q0 = xVar;
            k0.this.f9872l.k(25, new o.a() { // from class: com.google.android.exoplayer2.m0
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    ((y1.d) obj).p(c6.x.this);
                }
            });
        }

        @Override // p5.m
        public void q(final p5.e eVar) {
            k0.this.f9869j0 = eVar;
            k0.this.f9872l.k(27, new o.a() { // from class: com.google.android.exoplayer2.r0
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    ((y1.d) obj).q(p5.e.this);
                }
            });
        }

        @Override // p5.m
        public void r(final List<p5.b> list) {
            k0.this.f9872l.k(27, new o.a() { // from class: com.google.android.exoplayer2.q0
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    ((y1.d) obj).r(list);
                }
            });
        }

        @Override // com.google.android.exoplayer2.audio.b
        public void s(long j8) {
            k0.this.f9883r.s(j8);
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i8, int i9, int i10) {
            k0.this.W1(i9, i10);
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            if (k0.this.Y) {
                k0.this.h2(surfaceHolder.getSurface());
            }
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (k0.this.Y) {
                k0.this.h2(null);
            }
            k0.this.W1(0, 0);
        }

        @Override // com.google.android.exoplayer2.audio.b
        public void t(Exception exc) {
            k0.this.f9883r.t(exc);
        }

        @Override // c6.v
        public void u(w0 w0Var, l4.g gVar) {
            k0.this.R = w0Var;
            k0.this.f9883r.u(w0Var, gVar);
        }

        @Override // c6.v
        public void v(Exception exc) {
            k0.this.f9883r.v(exc);
        }

        @Override // com.google.android.exoplayer2.b.InterfaceC0105b
        public void w() {
            k0.this.l2(false, -1, 3);
        }

        @Override // com.google.android.exoplayer2.audio.b
        public void x(int i8, long j8, long j9) {
            k0.this.f9883r.x(i8, j8, j9);
        }

        @Override // c6.v
        public void y(long j8, int i8) {
            k0.this.f9883r.y(j8, i8);
        }

        @Override // com.google.android.exoplayer2.k.a
        public void z(boolean z4) {
            k0.this.o2();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d implements c6.i, d6.a, z1.b {

        /* renamed from: a  reason: collision with root package name */
        private c6.i f9898a;

        /* renamed from: b  reason: collision with root package name */
        private d6.a f9899b;

        /* renamed from: c  reason: collision with root package name */
        private c6.i f9900c;

        /* renamed from: d  reason: collision with root package name */
        private d6.a f9901d;

        private d() {
        }

        @Override // d6.a
        public void a(long j8, float[] fArr) {
            d6.a aVar = this.f9901d;
            if (aVar != null) {
                aVar.a(j8, fArr);
            }
            d6.a aVar2 = this.f9899b;
            if (aVar2 != null) {
                aVar2.a(j8, fArr);
            }
        }

        @Override // d6.a
        public void c() {
            d6.a aVar = this.f9901d;
            if (aVar != null) {
                aVar.c();
            }
            d6.a aVar2 = this.f9899b;
            if (aVar2 != null) {
                aVar2.c();
            }
        }

        @Override // c6.i
        public void d(long j8, long j9, w0 w0Var, MediaFormat mediaFormat) {
            c6.i iVar = this.f9900c;
            if (iVar != null) {
                iVar.d(j8, j9, w0Var, mediaFormat);
            }
            c6.i iVar2 = this.f9898a;
            if (iVar2 != null) {
                iVar2.d(j8, j9, w0Var, mediaFormat);
            }
        }

        @Override // com.google.android.exoplayer2.z1.b
        public void x(int i8, Object obj) {
            d6.a cameraMotionListener;
            if (i8 == 7) {
                this.f9898a = (c6.i) obj;
            } else if (i8 == 8) {
                this.f9899b = (d6.a) obj;
            } else if (i8 != 10000) {
            } else {
                SphericalGLSurfaceView sphericalGLSurfaceView = (SphericalGLSurfaceView) obj;
                if (sphericalGLSurfaceView == null) {
                    cameraMotionListener = null;
                    this.f9900c = null;
                } else {
                    this.f9900c = sphericalGLSurfaceView.getVideoFrameMetadataListener();
                    cameraMotionListener = sphericalGLSurfaceView.getCameraMotionListener();
                }
                this.f9901d = cameraMotionListener;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e implements f1 {

        /* renamed from: a  reason: collision with root package name */
        private final Object f9902a;

        /* renamed from: b  reason: collision with root package name */
        private h2 f9903b;

        public e(Object obj, h2 h2Var) {
            this.f9902a = obj;
            this.f9903b = h2Var;
        }

        @Override // com.google.android.exoplayer2.f1
        public Object a() {
            return this.f9902a;
        }

        @Override // com.google.android.exoplayer2.f1
        public h2 b() {
            return this.f9903b;
        }
    }

    static {
        i4.q.a("goog.exo.exoplayer");
    }

    @SuppressLint({"HandlerLeak"})
    public k0(k.b bVar, y1 y1Var) {
        b6.g gVar = new b6.g();
        this.f9856d = gVar;
        try {
            b6.p.f("ExoPlayerImpl", "Init " + Integer.toHexString(System.identityHashCode(this)) + " [ExoPlayerLib/2.18.7] [" + b6.l0.f8067e + "]");
            Context applicationContext = bVar.f9826a.getApplicationContext();
            this.f9858e = applicationContext;
            j4.a apply = bVar.f9834i.apply(bVar.f9827b);
            this.f9883r = apply;
            this.f9875m0 = bVar.f9836k;
            this.f9863g0 = bVar.f9837l;
            this.f9851a0 = bVar.q;
            this.f9853b0 = bVar.f9842r;
            this.f9867i0 = bVar.f9841p;
            this.E = bVar.f9849y;
            c cVar = new c();
            this.f9894x = cVar;
            d dVar = new d();
            this.f9895y = dVar;
            Handler handler = new Handler(bVar.f9835j);
            c2[] a9 = bVar.f9829d.get().a(handler, cVar, cVar, cVar, cVar);
            this.f9862g = a9;
            b6.a.f(a9.length > 0);
            z5.a0 a0Var = bVar.f9831f.get();
            this.f9864h = a0Var;
            this.q = bVar.f9830e.get();
            a6.d dVar2 = bVar.f9833h.get();
            this.f9887t = dVar2;
            this.f9880p = bVar.f9843s;
            this.L = bVar.f9844t;
            this.f9889u = bVar.f9845u;
            this.f9891v = bVar.f9846v;
            this.N = bVar.f9850z;
            Looper looper = bVar.f9835j;
            this.f9885s = looper;
            b6.d dVar3 = bVar.f9827b;
            this.f9893w = dVar3;
            k0 k0Var = y1Var == null ? this : y1Var;
            this.f9860f = k0Var;
            this.f9872l = new b6.o<>(looper, dVar3, new o.b() { // from class: com.google.android.exoplayer2.z
                @Override // b6.o.b
                public final void a(Object obj, b6.k kVar) {
                    k0.this.w1((y1.d) obj, kVar);
                }
            });
            this.f9874m = new CopyOnWriteArraySet<>();
            this.f9878o = new ArrayList();
            this.M = new x.a(0);
            z5.b0 b0Var = new z5.b0(new i4.g0[a9.length], new z5.r[a9.length], i2.f9796b, null);
            this.f9852b = b0Var;
            this.f9876n = new h2.b();
            y1.b e8 = new y1.b.a().c(1, 2, 3, 13, 14, 15, 16, 17, 18, 19, 31, 20, 30, 21, 22, 23, 24, 25, 26, 27, 28).d(29, a0Var.d()).e();
            this.f9854c = e8;
            this.O = new y1.b.a().b(e8).a(4).a(10).e();
            this.f9866i = dVar3.d(looper, null);
            v0.f fVar = new v0.f() { // from class: com.google.android.exoplayer2.a0
                @Override // com.google.android.exoplayer2.v0.f
                public final void a(v0.e eVar) {
                    k0.this.y1(eVar);
                }
            };
            this.f9868j = fVar;
            this.f9886s0 = w1.j(b0Var);
            apply.W(k0Var, looper);
            int i8 = b6.l0.f8063a;
            v0 v0Var = new v0(a9, a0Var, b0Var, bVar.f9832g.get(), dVar2, this.F, this.G, apply, this.L, bVar.f9847w, bVar.f9848x, this.N, looper, dVar3, fVar, i8 < 31 ? new j4.t1() : b.a(applicationContext, this, bVar.A), bVar.B);
            this.f9870k = v0Var;
            this.f9865h0 = 1.0f;
            this.F = 0;
            a1 a1Var = a1.W;
            this.P = a1Var;
            this.Q = a1Var;
            this.f9884r0 = a1Var;
            this.f9888t0 = -1;
            this.f9861f0 = i8 < 21 ? t1(0) : b6.l0.F(applicationContext);
            this.f9869j0 = p5.e.f22406c;
            this.f9871k0 = true;
            v(apply);
            dVar2.h(new Handler(looper), apply);
            a1(cVar);
            long j8 = bVar.f9828c;
            if (j8 > 0) {
                v0Var.u(j8);
            }
            com.google.android.exoplayer2.b bVar2 = new com.google.android.exoplayer2.b(bVar.f9826a, handler, cVar);
            this.f9896z = bVar2;
            bVar2.b(bVar.f9840o);
            com.google.android.exoplayer2.d dVar4 = new com.google.android.exoplayer2.d(bVar.f9826a, handler, cVar);
            this.A = dVar4;
            dVar4.m(bVar.f9838m ? this.f9863g0 : null);
            e2 e2Var = new e2(bVar.f9826a, handler, cVar);
            this.B = e2Var;
            e2Var.h(b6.l0.f0(this.f9863g0.f9322c));
            j2 j2Var = new j2(bVar.f9826a);
            this.C = j2Var;
            j2Var.a(bVar.f9839n != 0);
            k2 k2Var = new k2(bVar.f9826a);
            this.D = k2Var;
            k2Var.a(bVar.f9839n == 2);
            this.f9881p0 = d1(e2Var);
            this.f9882q0 = c6.x.f8455e;
            this.f9855c0 = b6.b0.f8019c;
            a0Var.h(this.f9863g0);
            b2(1, 10, Integer.valueOf(this.f9861f0));
            b2(2, 10, Integer.valueOf(this.f9861f0));
            b2(1, 3, this.f9863g0);
            b2(2, 4, Integer.valueOf(this.f9851a0));
            b2(2, 5, Integer.valueOf(this.f9853b0));
            b2(1, 9, Boolean.valueOf(this.f9867i0));
            b2(2, 7, dVar);
            b2(6, 8, dVar);
            gVar.e();
        } catch (Throwable th) {
            this.f9856d.e();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void F1(y1.d dVar) {
        dVar.H(this.O);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void G1(w1 w1Var, int i8, y1.d dVar) {
        dVar.K(w1Var.f11241a, i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void H1(int i8, y1.e eVar, y1.e eVar2, y1.d dVar) {
        dVar.C(i8);
        dVar.z(eVar, eVar2, i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void J1(w1 w1Var, y1.d dVar) {
        dVar.m0(w1Var.f11246f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void K1(w1 w1Var, y1.d dVar) {
        dVar.G(w1Var.f11246f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void L1(w1 w1Var, y1.d dVar) {
        dVar.D(w1Var.f11249i.f24606d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void N1(w1 w1Var, y1.d dVar) {
        dVar.B(w1Var.f11247g);
        dVar.E(w1Var.f11247g);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void O1(w1 w1Var, y1.d dVar) {
        dVar.Z(w1Var.f11252l, w1Var.f11245e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void P1(w1 w1Var, y1.d dVar) {
        dVar.N(w1Var.f11245e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void Q1(w1 w1Var, int i8, y1.d dVar) {
        dVar.h0(w1Var.f11252l, i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void R1(w1 w1Var, y1.d dVar) {
        dVar.A(w1Var.f11253m);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void S1(w1 w1Var, y1.d dVar) {
        dVar.p0(u1(w1Var));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void T1(w1 w1Var, y1.d dVar) {
        dVar.w(w1Var.f11254n);
    }

    private w1 U1(w1 w1Var, h2 h2Var, Pair<Object, Long> pair) {
        int i8;
        long j8;
        b6.a.a(h2Var.u() || pair != null);
        h2 h2Var2 = w1Var.f11241a;
        w1 i9 = w1Var.i(h2Var);
        if (h2Var.u()) {
            k.b k8 = w1.k();
            long C0 = b6.l0.C0(this.f9892v0);
            w1 b9 = i9.c(k8, C0, C0, C0, 0L, h5.w.f20313d, this.f9852b, ImmutableList.E()).b(k8);
            b9.f11256p = b9.f11257r;
            return b9;
        }
        Object obj = i9.f11242b.f20286a;
        boolean z4 = !obj.equals(((Pair) b6.l0.j(pair)).first);
        k.b bVar = z4 ? new k.b(pair.first) : i9.f11242b;
        long longValue = ((Long) pair.second).longValue();
        long C02 = b6.l0.C0(u());
        if (!h2Var2.u()) {
            C02 -= h2Var2.l(obj, this.f9876n).q();
        }
        if (z4 || longValue < C02) {
            b6.a.f(!bVar.b());
            w1 b10 = i9.c(bVar, longValue, longValue, longValue, 0L, z4 ? h5.w.f20313d : i9.f11248h, z4 ? this.f9852b : i9.f11249i, z4 ? ImmutableList.E() : i9.f11250j).b(bVar);
            b10.f11256p = longValue;
            return b10;
        }
        if (i8 == 0) {
            int f5 = h2Var.f(i9.f11251k.f20286a);
            if (f5 == -1 || h2Var.j(f5, this.f9876n).f9758c != h2Var.l(bVar.f20286a, this.f9876n).f9758c) {
                h2Var.l(bVar.f20286a, this.f9876n);
                j8 = bVar.b() ? this.f9876n.e(bVar.f20287b, bVar.f20288c) : this.f9876n.f9759d;
                i9 = i9.c(bVar, i9.f11257r, i9.f11257r, i9.f11244d, j8 - i9.f11257r, i9.f11248h, i9.f11249i, i9.f11250j).b(bVar);
            }
            return i9;
        }
        b6.a.f(!bVar.b());
        long max = Math.max(0L, i9.q - (longValue - C02));
        j8 = i9.f11256p;
        if (i9.f11251k.equals(i9.f11242b)) {
            j8 = longValue + max;
        }
        i9 = i9.c(bVar, longValue, longValue, longValue, max, i9.f11248h, i9.f11249i, i9.f11250j);
        i9.f11256p = j8;
        return i9;
    }

    private Pair<Object, Long> V1(h2 h2Var, int i8, long j8) {
        if (h2Var.u()) {
            this.f9888t0 = i8;
            if (j8 == -9223372036854775807L) {
                j8 = 0;
            }
            this.f9892v0 = j8;
            this.f9890u0 = 0;
            return null;
        }
        if (i8 == -1 || i8 >= h2Var.t()) {
            i8 = h2Var.e(this.G);
            j8 = h2Var.r(i8, this.f9640a).d();
        }
        return h2Var.n(this.f9640a, this.f9876n, i8, b6.l0.C0(j8));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void W1(final int i8, final int i9) {
        if (i8 == this.f9855c0.b() && i9 == this.f9855c0.a()) {
            return;
        }
        this.f9855c0 = new b6.b0(i8, i9);
        this.f9872l.k(24, new o.a() { // from class: com.google.android.exoplayer2.c0
            @Override // b6.o.a
            public final void invoke(Object obj) {
                ((y1.d) obj).j0(i8, i9);
            }
        });
    }

    private long X1(h2 h2Var, k.b bVar, long j8) {
        h2Var.l(bVar.f20286a, this.f9876n);
        return j8 + this.f9876n.q();
    }

    private w1 Y1(int i8, int i9) {
        int F = F();
        h2 K = K();
        int size = this.f9878o.size();
        boolean z4 = true;
        this.H++;
        Z1(i8, i9);
        h2 e12 = e1();
        w1 U1 = U1(this.f9886s0, e12, m1(K, e12));
        int i10 = U1.f11245e;
        if ((i10 == 1 || i10 == 4 || i8 >= i9 || i9 != size || F < U1.f11241a.t()) ? false : false) {
            U1 = U1.g(4);
        }
        this.f9870k.p0(i8, i9, this.M);
        return U1;
    }

    private void Z1(int i8, int i9) {
        for (int i10 = i9 - 1; i10 >= i8; i10--) {
            this.f9878o.remove(i10);
        }
        this.M = this.M.c(i8, i9);
    }

    private void a2() {
        if (this.X != null) {
            f1(this.f9895y).n(ModuleDescriptor.MODULE_VERSION).m(null).l();
            this.X.h(this.f9894x);
            this.X = null;
        }
        TextureView textureView = this.Z;
        if (textureView != null) {
            if (textureView.getSurfaceTextureListener() != this.f9894x) {
                b6.p.i("ExoPlayerImpl", "SurfaceTextureListener already unset or replaced.");
            } else {
                this.Z.setSurfaceTextureListener(null);
            }
            this.Z = null;
        }
        SurfaceHolder surfaceHolder = this.W;
        if (surfaceHolder != null) {
            surfaceHolder.removeCallback(this.f9894x);
            this.W = null;
        }
    }

    private List<t1.c> b1(int i8, List<com.google.android.exoplayer2.source.k> list) {
        ArrayList arrayList = new ArrayList();
        for (int i9 = 0; i9 < list.size(); i9++) {
            t1.c cVar = new t1.c(list.get(i9), this.f9880p);
            arrayList.add(cVar);
            this.f9878o.add(i9 + i8, new e(cVar.f10873b, cVar.f10872a.c0()));
        }
        this.M = this.M.g(i8, arrayList.size());
        return arrayList;
    }

    private void b2(int i8, int i9, Object obj) {
        c2[] c2VarArr;
        for (c2 c2Var : this.f9862g) {
            if (c2Var.h() == i8) {
                f1(c2Var).n(i9).m(obj).l();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public a1 c1() {
        h2 K = K();
        if (K.u()) {
            return this.f9884r0;
        }
        return this.f9884r0.b().J(K.r(F(), this.f9640a).f9772c.f11307e).H();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c2() {
        b2(1, 2, Float.valueOf(this.f9865h0 * this.A.g()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static j d1(e2 e2Var) {
        return new j(0, e2Var.d(), e2Var.c());
    }

    private h2 e1() {
        return new a2(this.f9878o, this.M);
    }

    private z1 f1(z1.b bVar) {
        int l12 = l1();
        v0 v0Var = this.f9870k;
        h2 h2Var = this.f9886s0.f11241a;
        if (l12 == -1) {
            l12 = 0;
        }
        return new z1(v0Var, bVar, h2Var, l12, this.f9893w, v0Var.D());
    }

    private void f2(List<com.google.android.exoplayer2.source.k> list, int i8, long j8, boolean z4) {
        int i9;
        long j9;
        int l12 = l1();
        long currentPosition = getCurrentPosition();
        boolean z8 = true;
        this.H++;
        if (!this.f9878o.isEmpty()) {
            Z1(0, this.f9878o.size());
        }
        List<t1.c> b12 = b1(0, list);
        h2 e12 = e1();
        if (!e12.u() && i8 >= e12.t()) {
            throw new IllegalSeekPositionException(e12, i8, j8);
        }
        if (z4) {
            j9 = -9223372036854775807L;
            i9 = e12.e(this.G);
        } else if (i8 == -1) {
            i9 = l12;
            j9 = currentPosition;
        } else {
            i9 = i8;
            j9 = j8;
        }
        w1 U1 = U1(this.f9886s0, e12, V1(e12, i9, j9));
        int i10 = U1.f11245e;
        if (i9 != -1 && i10 != 1) {
            i10 = (e12.u() || i9 >= e12.t()) ? 4 : 2;
        }
        w1 g8 = U1.g(i10);
        this.f9870k.P0(b12, i9, b6.l0.C0(j9), this.M);
        if (this.f9886s0.f11242b.f20286a.equals(g8.f11242b.f20286a) || this.f9886s0.f11241a.u()) {
            z8 = false;
        }
        m2(g8, 0, 1, false, z8, 4, k1(g8), -1, false);
    }

    private Pair<Boolean, Integer> g1(w1 w1Var, w1 w1Var2, boolean z4, int i8, boolean z8, boolean z9) {
        h2 h2Var = w1Var2.f11241a;
        h2 h2Var2 = w1Var.f11241a;
        if (h2Var2.u() && h2Var.u()) {
            return new Pair<>(Boolean.FALSE, -1);
        }
        int i9 = 3;
        if (h2Var2.u() != h2Var.u()) {
            return new Pair<>(Boolean.TRUE, 3);
        }
        if (h2Var.r(h2Var.l(w1Var2.f11242b.f20286a, this.f9876n).f9758c, this.f9640a).f9770a.equals(h2Var2.r(h2Var2.l(w1Var.f11242b.f20286a, this.f9876n).f9758c, this.f9640a).f9770a)) {
            return (z4 && i8 == 0 && w1Var2.f11242b.f20289d < w1Var.f11242b.f20289d) ? new Pair<>(Boolean.TRUE, 0) : (z4 && i8 == 1 && z9) ? new Pair<>(Boolean.TRUE, 2) : new Pair<>(Boolean.FALSE, -1);
        }
        if (z4 && i8 == 0) {
            i9 = 1;
        } else if (z4 && i8 == 1) {
            i9 = 2;
        } else if (!z8) {
            throw new IllegalStateException();
        }
        return new Pair<>(Boolean.TRUE, Integer.valueOf(i9));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g2(SurfaceTexture surfaceTexture) {
        Surface surface = new Surface(surfaceTexture);
        h2(surface);
        this.V = surface;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h2(Object obj) {
        boolean z4;
        ArrayList<z1> arrayList = new ArrayList();
        c2[] c2VarArr = this.f9862g;
        int length = c2VarArr.length;
        int i8 = 0;
        while (true) {
            z4 = true;
            if (i8 >= length) {
                break;
            }
            c2 c2Var = c2VarArr[i8];
            if (c2Var.h() == 2) {
                arrayList.add(f1(c2Var).n(1).m(obj).l());
            }
            i8++;
        }
        Object obj2 = this.U;
        if (obj2 == null || obj2 == obj) {
            z4 = false;
        } else {
            try {
                for (z1 z1Var : arrayList) {
                    z1Var.a(this.E);
                }
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
            } catch (TimeoutException unused2) {
            }
            z4 = false;
            Object obj3 = this.U;
            Surface surface = this.V;
            if (obj3 == surface) {
                surface.release();
                this.V = null;
            }
        }
        this.U = obj;
        if (z4) {
            j2(false, ExoPlaybackException.i(new ExoTimeoutException(3), 1003));
        }
    }

    private void j2(boolean z4, ExoPlaybackException exoPlaybackException) {
        w1 b9;
        if (z4) {
            b9 = Y1(0, this.f9878o.size()).e(null);
        } else {
            w1 w1Var = this.f9886s0;
            b9 = w1Var.b(w1Var.f11242b);
            b9.f11256p = b9.f11257r;
            b9.q = 0L;
        }
        w1 g8 = b9.g(1);
        if (exoPlaybackException != null) {
            g8 = g8.e(exoPlaybackException);
        }
        w1 w1Var2 = g8;
        this.H++;
        this.f9870k.j1();
        m2(w1Var2, 0, 1, false, w1Var2.f11241a.u() && !this.f9886s0.f11241a.u(), 4, k1(w1Var2), -1, false);
    }

    private long k1(w1 w1Var) {
        return w1Var.f11241a.u() ? b6.l0.C0(this.f9892v0) : w1Var.f11242b.b() ? w1Var.f11257r : X1(w1Var.f11241a, w1Var.f11242b, w1Var.f11257r);
    }

    private void k2() {
        y1.b bVar = this.O;
        y1.b H = b6.l0.H(this.f9860f, this.f9854c);
        this.O = H;
        if (H.equals(bVar)) {
            return;
        }
        this.f9872l.i(13, new o.a() { // from class: com.google.android.exoplayer2.e0
            @Override // b6.o.a
            public final void invoke(Object obj) {
                k0.this.F1((y1.d) obj);
            }
        });
    }

    private int l1() {
        if (this.f9886s0.f11241a.u()) {
            return this.f9888t0;
        }
        w1 w1Var = this.f9886s0;
        return w1Var.f11241a.l(w1Var.f11242b.f20286a, this.f9876n).f9758c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l2(boolean z4, int i8, int i9) {
        int i10 = 0;
        boolean z8 = z4 && i8 != -1;
        if (z8 && i8 != 1) {
            i10 = 1;
        }
        w1 w1Var = this.f9886s0;
        if (w1Var.f11252l == z8 && w1Var.f11253m == i10) {
            return;
        }
        this.H++;
        w1 d8 = w1Var.d(z8, i10);
        this.f9870k.S0(z8, i10);
        m2(d8, 0, i9, false, false, 5, -9223372036854775807L, -1, false);
    }

    private Pair<Object, Long> m1(h2 h2Var, h2 h2Var2) {
        long u8 = u();
        if (h2Var.u() || h2Var2.u()) {
            boolean z4 = !h2Var.u() && h2Var2.u();
            int l12 = z4 ? -1 : l1();
            if (z4) {
                u8 = -9223372036854775807L;
            }
            return V1(h2Var2, l12, u8);
        }
        Pair<Object, Long> n8 = h2Var.n(this.f9640a, this.f9876n, F(), b6.l0.C0(u8));
        Object obj = ((Pair) b6.l0.j(n8)).first;
        if (h2Var2.f(obj) != -1) {
            return n8;
        }
        Object A0 = v0.A0(this.f9640a, this.f9876n, this.F, this.G, obj, h2Var, h2Var2);
        if (A0 != null) {
            h2Var2.l(A0, this.f9876n);
            int i8 = this.f9876n.f9758c;
            return V1(h2Var2, i8, h2Var2.r(i8, this.f9640a).d());
        }
        return V1(h2Var2, -1, -9223372036854775807L);
    }

    private void m2(final w1 w1Var, final int i8, final int i9, boolean z4, boolean z8, final int i10, long j8, int i11, boolean z9) {
        w1 w1Var2 = this.f9886s0;
        this.f9886s0 = w1Var;
        boolean z10 = !w1Var2.f11241a.equals(w1Var.f11241a);
        Pair<Boolean, Integer> g12 = g1(w1Var, w1Var2, z8, i10, z10, z9);
        boolean booleanValue = ((Boolean) g12.first).booleanValue();
        final int intValue = ((Integer) g12.second).intValue();
        a1 a1Var = this.P;
        if (booleanValue) {
            r3 = w1Var.f11241a.u() ? null : w1Var.f11241a.r(w1Var.f11241a.l(w1Var.f11242b.f20286a, this.f9876n).f9758c, this.f9640a).f9772c;
            this.f9884r0 = a1.W;
        }
        if (booleanValue || !w1Var2.f11250j.equals(w1Var.f11250j)) {
            this.f9884r0 = this.f9884r0.b().L(w1Var.f11250j).H();
            a1Var = c1();
        }
        boolean z11 = !a1Var.equals(this.P);
        this.P = a1Var;
        boolean z12 = w1Var2.f11252l != w1Var.f11252l;
        boolean z13 = w1Var2.f11245e != w1Var.f11245e;
        if (z13 || z12) {
            o2();
        }
        boolean z14 = w1Var2.f11247g;
        boolean z15 = w1Var.f11247g;
        boolean z16 = z14 != z15;
        if (z16) {
            n2(z15);
        }
        if (z10) {
            this.f9872l.i(0, new o.a() { // from class: com.google.android.exoplayer2.s
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    k0.G1(w1.this, i8, (y1.d) obj);
                }
            });
        }
        if (z8) {
            final y1.e q12 = q1(i10, w1Var2, i11);
            final y1.e p12 = p1(j8);
            this.f9872l.i(11, new o.a() { // from class: com.google.android.exoplayer2.d0
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    k0.H1(i10, q12, p12, (y1.d) obj);
                }
            });
        }
        if (booleanValue) {
            this.f9872l.i(1, new o.a() { // from class: com.google.android.exoplayer2.f0
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    ((y1.d) obj).e0(z0.this, intValue);
                }
            });
        }
        if (w1Var2.f11246f != w1Var.f11246f) {
            this.f9872l.i(10, new o.a() { // from class: com.google.android.exoplayer2.h0
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    k0.J1(w1.this, (y1.d) obj);
                }
            });
            if (w1Var.f11246f != null) {
                this.f9872l.i(10, new o.a() { // from class: com.google.android.exoplayer2.p
                    @Override // b6.o.a
                    public final void invoke(Object obj) {
                        k0.K1(w1.this, (y1.d) obj);
                    }
                });
            }
        }
        z5.b0 b0Var = w1Var2.f11249i;
        z5.b0 b0Var2 = w1Var.f11249i;
        if (b0Var != b0Var2) {
            this.f9864h.e(b0Var2.f24607e);
            this.f9872l.i(2, new o.a() { // from class: com.google.android.exoplayer2.j0
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    k0.L1(w1.this, (y1.d) obj);
                }
            });
        }
        if (z11) {
            final a1 a1Var2 = this.P;
            this.f9872l.i(14, new o.a() { // from class: com.google.android.exoplayer2.g0
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    ((y1.d) obj).T(a1.this);
                }
            });
        }
        if (z16) {
            this.f9872l.i(3, new o.a() { // from class: com.google.android.exoplayer2.r
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    k0.N1(w1.this, (y1.d) obj);
                }
            });
        }
        if (z13 || z12) {
            this.f9872l.i(-1, new o.a() { // from class: com.google.android.exoplayer2.q
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    k0.O1(w1.this, (y1.d) obj);
                }
            });
        }
        if (z13) {
            this.f9872l.i(4, new o.a() { // from class: com.google.android.exoplayer2.i0
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    k0.P1(w1.this, (y1.d) obj);
                }
            });
        }
        if (z12) {
            this.f9872l.i(5, new o.a() { // from class: com.google.android.exoplayer2.t
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    k0.Q1(w1.this, i9, (y1.d) obj);
                }
            });
        }
        if (w1Var2.f11253m != w1Var.f11253m) {
            this.f9872l.i(6, new o.a() { // from class: com.google.android.exoplayer2.m
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    k0.R1(w1.this, (y1.d) obj);
                }
            });
        }
        if (u1(w1Var2) != u1(w1Var)) {
            this.f9872l.i(7, new o.a() { // from class: com.google.android.exoplayer2.o
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    k0.S1(w1.this, (y1.d) obj);
                }
            });
        }
        if (!w1Var2.f11254n.equals(w1Var.f11254n)) {
            this.f9872l.i(12, new o.a() { // from class: com.google.android.exoplayer2.n
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    k0.T1(w1.this, (y1.d) obj);
                }
            });
        }
        if (z4) {
            this.f9872l.i(-1, i4.o.a);
        }
        k2();
        this.f9872l.f();
        if (w1Var2.f11255o != w1Var.f11255o) {
            Iterator<k.a> it = this.f9874m.iterator();
            while (it.hasNext()) {
                it.next().z(w1Var.f11255o);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int n1(boolean z4, int i8) {
        return (!z4 || i8 == 1) ? 1 : 2;
    }

    private void n2(boolean z4) {
        PriorityTaskManager priorityTaskManager = this.f9875m0;
        if (priorityTaskManager != null) {
            if (z4 && !this.f9877n0) {
                priorityTaskManager.a(0);
                this.f9877n0 = true;
            } else if (z4 || !this.f9877n0) {
            } else {
                priorityTaskManager.b(0);
                this.f9877n0 = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void o2() {
        int z4 = z();
        boolean z8 = true;
        if (z4 != 1) {
            if (z4 == 2 || z4 == 3) {
                boolean h12 = h1();
                j2 j2Var = this.C;
                if (!k() || h12) {
                    z8 = false;
                }
                j2Var.b(z8);
                this.D.b(k());
                return;
            } else if (z4 != 4) {
                throw new IllegalStateException();
            }
        }
        this.C.b(false);
        this.D.b(false);
    }

    private y1.e p1(long j8) {
        int i8;
        z0 z0Var;
        Object obj;
        int F = F();
        Object obj2 = null;
        if (this.f9886s0.f11241a.u()) {
            i8 = -1;
            z0Var = null;
            obj = null;
        } else {
            w1 w1Var = this.f9886s0;
            Object obj3 = w1Var.f11242b.f20286a;
            w1Var.f11241a.l(obj3, this.f9876n);
            i8 = this.f9886s0.f11241a.f(obj3);
            obj = obj3;
            obj2 = this.f9886s0.f11241a.r(F, this.f9640a).f9770a;
            z0Var = this.f9640a.f9772c;
        }
        long a12 = b6.l0.a1(j8);
        long a13 = this.f9886s0.f11242b.b() ? b6.l0.a1(r1(this.f9886s0)) : a12;
        k.b bVar = this.f9886s0.f11242b;
        return new y1.e(obj2, F, z0Var, obj, i8, a12, a13, bVar.f20287b, bVar.f20288c);
    }

    private void p2() {
        this.f9856d.b();
        if (Thread.currentThread() != i1().getThread()) {
            String C = b6.l0.C("Player is accessed on the wrong thread.\nCurrent thread: '%s'\nExpected thread: '%s'\nSee https://developer.android.com/guide/topics/media/issues/player-accessed-on-wrong-thread", Thread.currentThread().getName(), i1().getThread().getName());
            if (this.f9871k0) {
                throw new IllegalStateException(C);
            }
            b6.p.j("ExoPlayerImpl", C, this.f9873l0 ? null : new IllegalStateException());
            this.f9873l0 = true;
        }
    }

    private y1.e q1(int i8, w1 w1Var, int i9) {
        int i10;
        int i11;
        Object obj;
        z0 z0Var;
        Object obj2;
        long j8;
        long j9;
        h2.b bVar = new h2.b();
        if (w1Var.f11241a.u()) {
            i10 = i9;
            i11 = -1;
            obj = null;
            z0Var = null;
            obj2 = null;
        } else {
            Object obj3 = w1Var.f11242b.f20286a;
            w1Var.f11241a.l(obj3, bVar);
            int i12 = bVar.f9758c;
            i10 = i12;
            obj2 = obj3;
            i11 = w1Var.f11241a.f(obj3);
            obj = w1Var.f11241a.r(i12, this.f9640a).f9770a;
            z0Var = this.f9640a.f9772c;
        }
        boolean b9 = w1Var.f11242b.b();
        if (i8 == 0) {
            if (b9) {
                k.b bVar2 = w1Var.f11242b;
                j8 = bVar.e(bVar2.f20287b, bVar2.f20288c);
                j9 = r1(w1Var);
            } else {
                j8 = w1Var.f11242b.f20290e != -1 ? r1(this.f9886s0) : bVar.f9760e + bVar.f9759d;
                j9 = j8;
            }
        } else if (b9) {
            j8 = w1Var.f11257r;
            j9 = r1(w1Var);
        } else {
            j8 = bVar.f9760e + w1Var.f11257r;
            j9 = j8;
        }
        long a12 = b6.l0.a1(j8);
        long a13 = b6.l0.a1(j9);
        k.b bVar3 = w1Var.f11242b;
        return new y1.e(obj, i10, z0Var, obj2, i11, a12, a13, bVar3.f20287b, bVar3.f20288c);
    }

    private static long r1(w1 w1Var) {
        h2.d dVar = new h2.d();
        h2.b bVar = new h2.b();
        w1Var.f11241a.l(w1Var.f11242b.f20286a, bVar);
        return w1Var.f11243c == -9223372036854775807L ? w1Var.f11241a.r(bVar.f9758c, dVar).e() : bVar.q() + w1Var.f11243c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: s1 */
    public void x1(v0.e eVar) {
        long j8;
        boolean z4;
        long j9;
        int i8 = this.H - eVar.f11046c;
        this.H = i8;
        boolean z8 = true;
        if (eVar.f11047d) {
            this.I = eVar.f11048e;
            this.J = true;
        }
        if (eVar.f11049f) {
            this.K = eVar.f11050g;
        }
        if (i8 == 0) {
            h2 h2Var = eVar.f11045b.f11241a;
            if (!this.f9886s0.f11241a.u() && h2Var.u()) {
                this.f9888t0 = -1;
                this.f9892v0 = 0L;
                this.f9890u0 = 0;
            }
            if (!h2Var.u()) {
                List<h2> I = ((a2) h2Var).I();
                b6.a.f(I.size() == this.f9878o.size());
                for (int i9 = 0; i9 < I.size(); i9++) {
                    this.f9878o.get(i9).f9903b = I.get(i9);
                }
            }
            if (this.J) {
                if (eVar.f11045b.f11242b.equals(this.f9886s0.f11242b) && eVar.f11045b.f11244d == this.f9886s0.f11257r) {
                    z8 = false;
                }
                if (z8) {
                    if (h2Var.u() || eVar.f11045b.f11242b.b()) {
                        j9 = eVar.f11045b.f11244d;
                    } else {
                        w1 w1Var = eVar.f11045b;
                        j9 = X1(h2Var, w1Var.f11242b, w1Var.f11244d);
                    }
                    j8 = j9;
                } else {
                    j8 = -9223372036854775807L;
                }
                z4 = z8;
            } else {
                j8 = -9223372036854775807L;
                z4 = false;
            }
            this.J = false;
            m2(eVar.f11045b, 1, this.K, false, z4, this.I, j8, -1, false);
        }
    }

    private int t1(int i8) {
        AudioTrack audioTrack = this.T;
        if (audioTrack != null && audioTrack.getAudioSessionId() != i8) {
            this.T.release();
            this.T = null;
        }
        if (this.T == null) {
            this.T = new AudioTrack(3, 4000, 4, 2, 2, 0, i8);
        }
        return this.T.getAudioSessionId();
    }

    private static boolean u1(w1 w1Var) {
        return w1Var.f11245e == 3 && w1Var.f11252l && w1Var.f11253m == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void w1(y1.d dVar, b6.k kVar) {
        dVar.V(this.f9860f, new y1.c(kVar));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void y1(final v0.e eVar) {
        this.f9866i.b(new Runnable() { // from class: com.google.android.exoplayer2.b0
            @Override // java.lang.Runnable
            public final void run() {
                k0.this.x1(eVar);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void z1(y1.d dVar) {
        dVar.G(ExoPlaybackException.i(new ExoTimeoutException(1), 1003));
    }

    @Override // com.google.android.exoplayer2.k
    public w0 A() {
        p2();
        return this.R;
    }

    @Override // com.google.android.exoplayer2.y1
    public i2 B() {
        p2();
        return this.f9886s0.f11249i.f24606d;
    }

    @Override // com.google.android.exoplayer2.k
    public void C(boolean z4) {
        p2();
        this.f9870k.v(z4);
        Iterator<k.a> it = this.f9874m.iterator();
        while (it.hasNext()) {
            it.next().I(z4);
        }
    }

    @Override // com.google.android.exoplayer2.y1
    public int E() {
        p2();
        if (h()) {
            return this.f9886s0.f11242b.f20287b;
        }
        return -1;
    }

    @Override // com.google.android.exoplayer2.y1
    public int F() {
        p2();
        int l12 = l1();
        if (l12 == -1) {
            return 0;
        }
        return l12;
    }

    @Override // com.google.android.exoplayer2.y1
    public void G(final int i8) {
        p2();
        if (this.F != i8) {
            this.F = i8;
            this.f9870k.W0(i8);
            this.f9872l.i(8, new o.a() { // from class: com.google.android.exoplayer2.w
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    ((y1.d) obj).l(i8);
                }
            });
            k2();
            this.f9872l.f();
        }
    }

    @Override // com.google.android.exoplayer2.y1
    public int I() {
        p2();
        return this.f9886s0.f11253m;
    }

    @Override // com.google.android.exoplayer2.y1
    public int J() {
        p2();
        return this.F;
    }

    @Override // com.google.android.exoplayer2.y1
    public h2 K() {
        p2();
        return this.f9886s0.f11241a;
    }

    @Override // com.google.android.exoplayer2.k
    public int L() {
        p2();
        return this.f9861f0;
    }

    @Override // com.google.android.exoplayer2.y1
    public boolean M() {
        p2();
        return this.G;
    }

    @Override // com.google.android.exoplayer2.k
    public void N(final com.google.android.exoplayer2.audio.a aVar, boolean z4) {
        p2();
        if (this.f9879o0) {
            return;
        }
        if (!b6.l0.c(this.f9863g0, aVar)) {
            this.f9863g0 = aVar;
            b2(1, 3, aVar);
            this.B.h(b6.l0.f0(aVar.f9322c));
            this.f9872l.i(20, new o.a() { // from class: com.google.android.exoplayer2.u
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    ((y1.d) obj).a0(com.google.android.exoplayer2.audio.a.this);
                }
            });
        }
        this.A.m(z4 ? aVar : null);
        this.f9864h.h(aVar);
        boolean k8 = k();
        int p8 = this.A.p(k8, z());
        l2(k8, p8, n1(k8, p8));
        this.f9872l.f();
    }

    @Override // com.google.android.exoplayer2.e
    public void U(int i8, long j8, int i9, boolean z4) {
        p2();
        b6.a.a(i8 >= 0);
        this.f9883r.S();
        h2 h2Var = this.f9886s0.f11241a;
        if (h2Var.u() || i8 < h2Var.t()) {
            this.H++;
            if (h()) {
                b6.p.i("ExoPlayerImpl", "seekTo ignored because an ad is playing");
                v0.e eVar = new v0.e(this.f9886s0);
                eVar.b(1);
                this.f9868j.a(eVar);
                return;
            }
            int i10 = z() != 1 ? 2 : 1;
            int F = F();
            w1 U1 = U1(this.f9886s0.g(i10), h2Var, V1(h2Var, i8, j8));
            this.f9870k.C0(h2Var, i8, b6.l0.C0(j8));
            m2(U1, 0, 1, true, true, 1, k1(U1), F, z4);
        }
    }

    public void Z0(j4.b bVar) {
        this.f9883r.n0((j4.b) b6.a.e(bVar));
    }

    @Override // com.google.android.exoplayer2.y1
    public void a() {
        p2();
        boolean k8 = k();
        int p8 = this.A.p(k8, 2);
        l2(k8, p8, n1(k8, p8));
        w1 w1Var = this.f9886s0;
        if (w1Var.f11245e != 1) {
            return;
        }
        w1 e8 = w1Var.e(null);
        w1 g8 = e8.g(e8.f11241a.u() ? 4 : 2);
        this.H++;
        this.f9870k.k0();
        m2(g8, 1, 1, false, false, 5, -9223372036854775807L, -1, false);
    }

    public void a1(k.a aVar) {
        this.f9874m.add(aVar);
    }

    @Override // com.google.android.exoplayer2.y1
    public long b() {
        p2();
        if (h()) {
            w1 w1Var = this.f9886s0;
            k.b bVar = w1Var.f11242b;
            w1Var.f11241a.l(bVar.f20286a, this.f9876n);
            return b6.l0.a1(this.f9876n.e(bVar.f20287b, bVar.f20288c));
        }
        return P();
    }

    @Override // com.google.android.exoplayer2.y1
    public x1 c() {
        p2();
        return this.f9886s0.f11254n;
    }

    @Override // com.google.android.exoplayer2.y1
    public void d(x1 x1Var) {
        p2();
        if (x1Var == null) {
            x1Var = x1.f11264d;
        }
        if (this.f9886s0.f11254n.equals(x1Var)) {
            return;
        }
        w1 f5 = this.f9886s0.f(x1Var);
        this.H++;
        this.f9870k.U0(x1Var);
        m2(f5, 0, 1, false, false, 5, -9223372036854775807L, -1, false);
    }

    public void d2(List<com.google.android.exoplayer2.source.k> list) {
        p2();
        e2(list, true);
    }

    @Override // com.google.android.exoplayer2.y1
    public void e(float f5) {
        p2();
        final float p8 = b6.l0.p(f5, 0.0f, 1.0f);
        if (this.f9865h0 == p8) {
            return;
        }
        this.f9865h0 = p8;
        c2();
        this.f9872l.k(22, new o.a() { // from class: com.google.android.exoplayer2.l
            @Override // b6.o.a
            public final void invoke(Object obj) {
                ((y1.d) obj).L(p8);
            }
        });
    }

    public void e2(List<com.google.android.exoplayer2.source.k> list, boolean z4) {
        p2();
        f2(list, -1, -9223372036854775807L, z4);
    }

    @Override // com.google.android.exoplayer2.k
    public void f(final boolean z4) {
        p2();
        if (this.f9867i0 == z4) {
            return;
        }
        this.f9867i0 = z4;
        b2(1, 9, Boolean.valueOf(z4));
        this.f9872l.k(23, new o.a() { // from class: com.google.android.exoplayer2.x
            @Override // b6.o.a
            public final void invoke(Object obj) {
                ((y1.d) obj).a(z4);
            }
        });
    }

    @Override // com.google.android.exoplayer2.y1
    public void g(Surface surface) {
        p2();
        a2();
        h2(surface);
        int i8 = surface == null ? 0 : -1;
        W1(i8, i8);
    }

    @Override // com.google.android.exoplayer2.y1
    public long getCurrentPosition() {
        p2();
        return b6.l0.a1(k1(this.f9886s0));
    }

    @Override // com.google.android.exoplayer2.y1
    public boolean h() {
        p2();
        return this.f9886s0.f11242b.b();
    }

    public boolean h1() {
        p2();
        return this.f9886s0.f11255o;
    }

    @Override // com.google.android.exoplayer2.y1
    public long i() {
        p2();
        return b6.l0.a1(this.f9886s0.q);
    }

    public Looper i1() {
        return this.f9885s;
    }

    public void i2(boolean z4) {
        p2();
        this.A.p(k(), 1);
        j2(z4, null);
        this.f9869j0 = new p5.e(ImmutableList.E(), this.f9886s0.f11257r);
    }

    public long j1() {
        p2();
        if (this.f9886s0.f11241a.u()) {
            return this.f9892v0;
        }
        w1 w1Var = this.f9886s0;
        if (w1Var.f11251k.f20289d != w1Var.f11242b.f20289d) {
            return w1Var.f11241a.r(F(), this.f9640a).f();
        }
        long j8 = w1Var.f11256p;
        if (this.f9886s0.f11251k.b()) {
            w1 w1Var2 = this.f9886s0;
            h2.b l8 = w1Var2.f11241a.l(w1Var2.f11251k.f20286a, this.f9876n);
            long i8 = l8.i(this.f9886s0.f11251k.f20287b);
            j8 = i8 == Long.MIN_VALUE ? l8.f9759d : i8;
        }
        w1 w1Var3 = this.f9886s0;
        return b6.l0.a1(X1(w1Var3.f11241a, w1Var3.f11251k, j8));
    }

    @Override // com.google.android.exoplayer2.y1
    public boolean k() {
        p2();
        return this.f9886s0.f11252l;
    }

    @Override // com.google.android.exoplayer2.y1
    public void l(final boolean z4) {
        p2();
        if (this.G != z4) {
            this.G = z4;
            this.f9870k.Z0(z4);
            this.f9872l.i(9, new o.a() { // from class: com.google.android.exoplayer2.v
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    ((y1.d) obj).U(z4);
                }
            });
            k2();
            this.f9872l.f();
        }
    }

    @Override // com.google.android.exoplayer2.y1
    public int m() {
        p2();
        if (this.f9886s0.f11241a.u()) {
            return this.f9890u0;
        }
        w1 w1Var = this.f9886s0;
        return w1Var.f11241a.f(w1Var.f11242b.f20286a);
    }

    @Override // com.google.android.exoplayer2.k
    public void n(com.google.android.exoplayer2.source.k kVar) {
        p2();
        d2(Collections.singletonList(kVar));
    }

    @Override // com.google.android.exoplayer2.y1
    /* renamed from: o1 */
    public ExoPlaybackException s() {
        p2();
        return this.f9886s0.f11246f;
    }

    @Override // com.google.android.exoplayer2.y1
    public int q() {
        p2();
        if (h()) {
            return this.f9886s0.f11242b.f20288c;
        }
        return -1;
    }

    @Override // com.google.android.exoplayer2.y1
    public void release() {
        AudioTrack audioTrack;
        b6.p.f("ExoPlayerImpl", "Release " + Integer.toHexString(System.identityHashCode(this)) + " [ExoPlayerLib/2.18.7] [" + b6.l0.f8067e + "] [" + i4.q.b() + "]");
        p2();
        if (b6.l0.f8063a < 21 && (audioTrack = this.T) != null) {
            audioTrack.release();
            this.T = null;
        }
        this.f9896z.b(false);
        this.B.g();
        this.C.b(false);
        this.D.b(false);
        this.A.i();
        if (!this.f9870k.m0()) {
            this.f9872l.k(10, new o.a() { // from class: com.google.android.exoplayer2.y
                @Override // b6.o.a
                public final void invoke(Object obj) {
                    k0.z1((y1.d) obj);
                }
            });
        }
        this.f9872l.j();
        this.f9866i.k(null);
        this.f9887t.a(this.f9883r);
        w1 g8 = this.f9886s0.g(1);
        this.f9886s0 = g8;
        w1 b9 = g8.b(g8.f11242b);
        this.f9886s0 = b9;
        b9.f11256p = b9.f11257r;
        this.f9886s0.q = 0L;
        this.f9883r.release();
        this.f9864h.f();
        a2();
        Surface surface = this.V;
        if (surface != null) {
            surface.release();
            this.V = null;
        }
        if (this.f9877n0) {
            ((PriorityTaskManager) b6.a.e(this.f9875m0)).b(0);
            this.f9877n0 = false;
        }
        this.f9869j0 = p5.e.f22406c;
        this.f9879o0 = true;
    }

    @Override // com.google.android.exoplayer2.y1
    public void stop() {
        p2();
        i2(false);
    }

    @Override // com.google.android.exoplayer2.y1
    public void t(boolean z4) {
        p2();
        int p8 = this.A.p(z4, z());
        l2(z4, p8, n1(z4, p8));
    }

    @Override // com.google.android.exoplayer2.y1
    public long u() {
        p2();
        if (h()) {
            w1 w1Var = this.f9886s0;
            w1Var.f11241a.l(w1Var.f11242b.f20286a, this.f9876n);
            w1 w1Var2 = this.f9886s0;
            return w1Var2.f11243c == -9223372036854775807L ? w1Var2.f11241a.r(F(), this.f9640a).d() : this.f9876n.p() + b6.l0.a1(this.f9886s0.f11243c);
        }
        return getCurrentPosition();
    }

    @Override // com.google.android.exoplayer2.y1
    public void v(y1.d dVar) {
        this.f9872l.c((y1.d) b6.a.e(dVar));
    }

    @Override // com.google.android.exoplayer2.y1
    public long w() {
        p2();
        if (h()) {
            w1 w1Var = this.f9886s0;
            return w1Var.f11251k.equals(w1Var.f11242b) ? b6.l0.a1(this.f9886s0.f11256p) : b();
        }
        return j1();
    }

    @Override // com.google.android.exoplayer2.y1
    public int z() {
        p2();
        return this.f9886s0.f11245e;
    }
}
