package com.google.android.exoplayer2.video.spherical;

import android.graphics.SurfaceTexture;
import android.media.MediaFormat;
import android.opengl.GLES20;
import android.opengl.Matrix;
import b6.g0;
import b6.p;
import c6.i;
import com.google.android.exoplayer2.util.GlUtil;
import com.google.android.exoplayer2.w0;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g implements i, d6.a {

    /* renamed from: j  reason: collision with root package name */
    private int f11158j;

    /* renamed from: k  reason: collision with root package name */
    private SurfaceTexture f11159k;

    /* renamed from: n  reason: collision with root package name */
    private byte[] f11162n;

    /* renamed from: a  reason: collision with root package name */
    private final AtomicBoolean f11150a = new AtomicBoolean();

    /* renamed from: b  reason: collision with root package name */
    private final AtomicBoolean f11151b = new AtomicBoolean(true);

    /* renamed from: c  reason: collision with root package name */
    private final e f11152c = new e();

    /* renamed from: d  reason: collision with root package name */
    private final a f11153d = new a();

    /* renamed from: e  reason: collision with root package name */
    private final g0<Long> f11154e = new g0<>();

    /* renamed from: f  reason: collision with root package name */
    private final g0<c> f11155f = new g0<>();

    /* renamed from: g  reason: collision with root package name */
    private final float[] f11156g = new float[16];

    /* renamed from: h  reason: collision with root package name */
    private final float[] f11157h = new float[16];

    /* renamed from: l  reason: collision with root package name */
    private volatile int f11160l = 0;

    /* renamed from: m  reason: collision with root package name */
    private int f11161m = -1;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void g(SurfaceTexture surfaceTexture) {
        this.f11150a.set(true);
    }

    private void i(byte[] bArr, int i8, long j8) {
        byte[] bArr2 = this.f11162n;
        int i9 = this.f11161m;
        this.f11162n = bArr;
        if (i8 == -1) {
            i8 = this.f11160l;
        }
        this.f11161m = i8;
        if (i9 == i8 && Arrays.equals(bArr2, this.f11162n)) {
            return;
        }
        byte[] bArr3 = this.f11162n;
        c a9 = bArr3 != null ? d.a(bArr3, this.f11161m) : null;
        if (a9 == null || !e.c(a9)) {
            a9 = c.b(this.f11161m);
        }
        this.f11155f.a(j8, a9);
    }

    @Override // d6.a
    public void a(long j8, float[] fArr) {
        this.f11153d.e(j8, fArr);
    }

    @Override // d6.a
    public void c() {
        this.f11154e.c();
        this.f11153d.d();
        this.f11151b.set(true);
    }

    @Override // c6.i
    public void d(long j8, long j9, w0 w0Var, MediaFormat mediaFormat) {
        this.f11154e.a(j9, Long.valueOf(j8));
        i(w0Var.B, w0Var.C, j9);
    }

    public void e(float[] fArr, boolean z4) {
        GLES20.glClear(16384);
        try {
            GlUtil.b();
        } catch (GlUtil.GlException e8) {
            p.d("SceneRenderer", "Failed to draw a frame", e8);
        }
        if (this.f11150a.compareAndSet(true, false)) {
            ((SurfaceTexture) b6.a.e(this.f11159k)).updateTexImage();
            try {
                GlUtil.b();
            } catch (GlUtil.GlException e9) {
                p.d("SceneRenderer", "Failed to draw a frame", e9);
            }
            if (this.f11151b.compareAndSet(true, false)) {
                GlUtil.j(this.f11156g);
            }
            long timestamp = this.f11159k.getTimestamp();
            Long g8 = this.f11154e.g(timestamp);
            if (g8 != null) {
                this.f11153d.c(this.f11156g, g8.longValue());
            }
            c j8 = this.f11155f.j(timestamp);
            if (j8 != null) {
                this.f11152c.d(j8);
            }
        }
        Matrix.multiplyMM(this.f11157h, 0, fArr, 0, this.f11156g, 0);
        this.f11152c.a(this.f11158j, this.f11157h, z4);
    }

    public SurfaceTexture f() {
        try {
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
            GlUtil.b();
            this.f11152c.b();
            GlUtil.b();
            this.f11158j = GlUtil.f();
        } catch (GlUtil.GlException e8) {
            p.d("SceneRenderer", "Failed to initialize the renderer", e8);
        }
        SurfaceTexture surfaceTexture = new SurfaceTexture(this.f11158j);
        this.f11159k = surfaceTexture;
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() { // from class: com.google.android.exoplayer2.video.spherical.f
            @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
            public final void onFrameAvailable(SurfaceTexture surfaceTexture2) {
                g.this.g(surfaceTexture2);
            }
        });
        return this.f11159k;
    }

    public void h(int i8) {
        this.f11160l = i8;
    }
}
