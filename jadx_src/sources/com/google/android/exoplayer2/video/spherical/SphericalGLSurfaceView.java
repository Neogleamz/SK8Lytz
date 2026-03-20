package com.google.android.exoplayer2.video.spherical;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.WindowManager;
import b6.l0;
import c6.i;
import com.google.android.exoplayer2.util.GlUtil;
import com.google.android.exoplayer2.video.spherical.b;
import com.google.android.exoplayer2.video.spherical.h;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SphericalGLSurfaceView extends GLSurfaceView {

    /* renamed from: a  reason: collision with root package name */
    private final CopyOnWriteArrayList<b> f11088a;

    /* renamed from: b  reason: collision with root package name */
    private final SensorManager f11089b;

    /* renamed from: c  reason: collision with root package name */
    private final Sensor f11090c;

    /* renamed from: d  reason: collision with root package name */
    private final com.google.android.exoplayer2.video.spherical.b f11091d;

    /* renamed from: e  reason: collision with root package name */
    private final Handler f11092e;

    /* renamed from: f  reason: collision with root package name */
    private final h f11093f;

    /* renamed from: g  reason: collision with root package name */
    private final g f11094g;

    /* renamed from: h  reason: collision with root package name */
    private SurfaceTexture f11095h;

    /* renamed from: j  reason: collision with root package name */
    private Surface f11096j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f11097k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f11098l;

    /* renamed from: m  reason: collision with root package name */
    private boolean f11099m;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class a implements GLSurfaceView.Renderer, h.a, b.a {

        /* renamed from: a  reason: collision with root package name */
        private final g f11100a;

        /* renamed from: d  reason: collision with root package name */
        private final float[] f11103d;

        /* renamed from: e  reason: collision with root package name */
        private final float[] f11104e;

        /* renamed from: f  reason: collision with root package name */
        private final float[] f11105f;

        /* renamed from: g  reason: collision with root package name */
        private float f11106g;

        /* renamed from: h  reason: collision with root package name */
        private float f11107h;

        /* renamed from: b  reason: collision with root package name */
        private final float[] f11101b = new float[16];

        /* renamed from: c  reason: collision with root package name */
        private final float[] f11102c = new float[16];

        /* renamed from: j  reason: collision with root package name */
        private final float[] f11108j = new float[16];

        /* renamed from: k  reason: collision with root package name */
        private final float[] f11109k = new float[16];

        public a(g gVar) {
            float[] fArr = new float[16];
            this.f11103d = fArr;
            float[] fArr2 = new float[16];
            this.f11104e = fArr2;
            float[] fArr3 = new float[16];
            this.f11105f = fArr3;
            this.f11100a = gVar;
            GlUtil.j(fArr);
            GlUtil.j(fArr2);
            GlUtil.j(fArr3);
            this.f11107h = 3.1415927f;
        }

        private float c(float f5) {
            if (f5 > 1.0f) {
                return (float) (Math.toDegrees(Math.atan(Math.tan(Math.toRadians(45.0d)) / f5)) * 2.0d);
            }
            return 90.0f;
        }

        private void d() {
            Matrix.setRotateM(this.f11104e, 0, -this.f11106g, (float) Math.cos(this.f11107h), (float) Math.sin(this.f11107h), 0.0f);
        }

        @Override // com.google.android.exoplayer2.video.spherical.b.a
        public synchronized void a(float[] fArr, float f5) {
            float[] fArr2 = this.f11103d;
            System.arraycopy(fArr, 0, fArr2, 0, fArr2.length);
            this.f11107h = -f5;
            d();
        }

        @Override // com.google.android.exoplayer2.video.spherical.h.a
        public synchronized void b(PointF pointF) {
            this.f11106g = pointF.y;
            d();
            Matrix.setRotateM(this.f11105f, 0, -pointF.x, 0.0f, 1.0f, 0.0f);
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onDrawFrame(GL10 gl10) {
            synchronized (this) {
                Matrix.multiplyMM(this.f11109k, 0, this.f11103d, 0, this.f11105f, 0);
                Matrix.multiplyMM(this.f11108j, 0, this.f11104e, 0, this.f11109k, 0);
            }
            Matrix.multiplyMM(this.f11102c, 0, this.f11101b, 0, this.f11108j, 0);
            this.f11100a.e(this.f11102c, false);
        }

        @Override // com.google.android.exoplayer2.video.spherical.h.a
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return SphericalGLSurfaceView.this.performClick();
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceChanged(GL10 gl10, int i8, int i9) {
            GLES20.glViewport(0, 0, i8, i9);
            float f5 = i8 / i9;
            Matrix.perspectiveM(this.f11101b, 0, c(f5), f5, 0.1f, 100.0f);
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public synchronized void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
            SphericalGLSurfaceView.this.f(this.f11100a.f());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void C(Surface surface);

        void D(Surface surface);
    }

    public SphericalGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f11088a = new CopyOnWriteArrayList<>();
        this.f11092e = new Handler(Looper.getMainLooper());
        SensorManager sensorManager = (SensorManager) b6.a.e(context.getSystemService("sensor"));
        this.f11089b = sensorManager;
        Sensor defaultSensor = l0.f8063a >= 18 ? sensorManager.getDefaultSensor(15) : null;
        this.f11090c = defaultSensor == null ? sensorManager.getDefaultSensor(11) : defaultSensor;
        g gVar = new g();
        this.f11094g = gVar;
        a aVar = new a(gVar);
        h hVar = new h(context, aVar, 25.0f);
        this.f11093f = hVar;
        this.f11091d = new com.google.android.exoplayer2.video.spherical.b(((WindowManager) b6.a.e((WindowManager) context.getSystemService("window"))).getDefaultDisplay(), hVar, aVar);
        this.f11097k = true;
        setEGLContextClientVersion(2);
        setRenderer(aVar);
        setOnTouchListener(hVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d() {
        Surface surface = this.f11096j;
        if (surface != null) {
            Iterator<b> it = this.f11088a.iterator();
            while (it.hasNext()) {
                it.next().C(surface);
            }
        }
        g(this.f11095h, surface);
        this.f11095h = null;
        this.f11096j = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void e(SurfaceTexture surfaceTexture) {
        SurfaceTexture surfaceTexture2 = this.f11095h;
        Surface surface = this.f11096j;
        Surface surface2 = new Surface(surfaceTexture);
        this.f11095h = surfaceTexture;
        this.f11096j = surface2;
        Iterator<b> it = this.f11088a.iterator();
        while (it.hasNext()) {
            it.next().D(surface2);
        }
        g(surfaceTexture2, surface);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f(SurfaceTexture surfaceTexture) {
        this.f11092e.post(new d6.d(this, surfaceTexture));
    }

    private static void g(SurfaceTexture surfaceTexture, Surface surface) {
        if (surfaceTexture != null) {
            surfaceTexture.release();
        }
        if (surface != null) {
            surface.release();
        }
    }

    private void i() {
        boolean z4 = this.f11097k && this.f11098l;
        Sensor sensor = this.f11090c;
        if (sensor == null || z4 == this.f11099m) {
            return;
        }
        if (z4) {
            this.f11089b.registerListener(this.f11091d, sensor, 0);
        } else {
            this.f11089b.unregisterListener(this.f11091d);
        }
        this.f11099m = z4;
    }

    public d6.a getCameraMotionListener() {
        return this.f11094g;
    }

    public i getVideoFrameMetadataListener() {
        return this.f11094g;
    }

    public Surface getVideoSurface() {
        return this.f11096j;
    }

    public void h(b bVar) {
        this.f11088a.remove(bVar);
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f11092e.post(new d6.c(this));
    }

    @Override // android.opengl.GLSurfaceView
    public void onPause() {
        this.f11098l = false;
        i();
        super.onPause();
    }

    @Override // android.opengl.GLSurfaceView
    public void onResume() {
        super.onResume();
        this.f11098l = true;
        i();
    }

    public void setDefaultStereoMode(int i8) {
        this.f11094g.h(i8);
    }

    public void setUseSensorRotation(boolean z4) {
        this.f11097k = z4;
        i();
    }
}
