package cn.bingoogolapple.qrcode.core;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import java.util.Collections;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c extends SurfaceView implements SurfaceHolder.Callback {

    /* renamed from: a  reason: collision with root package name */
    private Camera f8549a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f8550b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f8551c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f8552d;

    /* renamed from: e  reason: collision with root package name */
    private float f8553e;

    /* renamed from: f  reason: collision with root package name */
    private cn.bingoogolapple.qrcode.core.b f8554f;

    /* renamed from: g  reason: collision with root package name */
    private b f8555g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Camera.AutoFocusCallback {
        a() {
        }

        @Override // android.hardware.Camera.AutoFocusCallback
        public void onAutoFocus(boolean z4, Camera camera) {
            if (z4) {
                cn.bingoogolapple.qrcode.core.a.e("对焦测光成功");
            } else {
                cn.bingoogolapple.qrcode.core.a.h("对焦测光失败");
            }
            c.this.j();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a();
    }

    public c(Context context) {
        super(context);
        this.f8550b = false;
        this.f8551c = false;
        this.f8552d = false;
        this.f8553e = 1.0f;
        getHolder().addCallback(this);
    }

    private boolean c() {
        return f() && getContext().getPackageManager().hasSystemFeature("android.hardware.camera.flash");
    }

    private void d(float f5, float f8, int i8, int i9) {
        boolean z4;
        try {
            Camera.Parameters parameters = this.f8549a.getParameters();
            Camera.Size previewSize = parameters.getPreviewSize();
            boolean z8 = true;
            if (parameters.getMaxNumFocusAreas() > 0) {
                cn.bingoogolapple.qrcode.core.a.e("支持设置对焦区域");
                Rect c9 = cn.bingoogolapple.qrcode.core.a.c(1.0f, f5, f8, i8, i9, previewSize.width, previewSize.height);
                cn.bingoogolapple.qrcode.core.a.o("对焦区域", c9);
                parameters.setFocusAreas(Collections.singletonList(new Camera.Area(c9, 1000)));
                parameters.setFocusMode("macro");
                z4 = true;
            } else {
                cn.bingoogolapple.qrcode.core.a.e("不支持设置对焦区域");
                z4 = false;
            }
            if (parameters.getMaxNumMeteringAreas() > 0) {
                cn.bingoogolapple.qrcode.core.a.e("支持设置测光区域");
                Rect c10 = cn.bingoogolapple.qrcode.core.a.c(1.5f, f5, f8, i8, i9, previewSize.width, previewSize.height);
                cn.bingoogolapple.qrcode.core.a.o("测光区域", c10);
                parameters.setMeteringAreas(Collections.singletonList(new Camera.Area(c10, 1000)));
            } else {
                cn.bingoogolapple.qrcode.core.a.e("不支持设置测光区域");
                z8 = z4;
            }
            if (!z8) {
                this.f8552d = false;
                return;
            }
            this.f8549a.cancelAutoFocus();
            this.f8549a.setParameters(parameters);
            this.f8549a.autoFocus(new a());
        } catch (Exception e8) {
            e8.printStackTrace();
            cn.bingoogolapple.qrcode.core.a.h("对焦测光失败：" + e8.getMessage());
            j();
        }
    }

    private static void e(boolean z4, Camera camera) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            if (!parameters.isZoomSupported()) {
                cn.bingoogolapple.qrcode.core.a.e("不支持缩放");
                return;
            }
            int zoom = parameters.getZoom();
            if (z4 && zoom < parameters.getMaxZoom()) {
                cn.bingoogolapple.qrcode.core.a.e("放大");
                zoom++;
            } else if (z4 || zoom <= 0) {
                cn.bingoogolapple.qrcode.core.a.e("既不放大也不缩小");
            } else {
                cn.bingoogolapple.qrcode.core.a.e("缩小");
                zoom--;
            }
            parameters.setZoom(zoom);
            camera.setParameters(parameters);
        } catch (Exception e8) {
            e8.printStackTrace();
        }
    }

    private void i() {
        if (this.f8549a != null) {
            try {
                this.f8550b = true;
                SurfaceHolder holder = getHolder();
                holder.setKeepScreenOn(true);
                this.f8549a.setPreviewDisplay(holder);
                this.f8554f.k(this.f8549a);
                this.f8549a.startPreview();
                b bVar = this.f8555g;
                if (bVar != null) {
                    bVar.a();
                }
                j();
            } catch (Exception e8) {
                e8.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        this.f8552d = false;
        Camera camera = this.f8549a;
        if (camera == null) {
            return;
        }
        try {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFocusMode("continuous-picture");
            this.f8549a.setParameters(parameters);
            this.f8549a.cancelAutoFocus();
        } catch (Exception unused) {
            cn.bingoogolapple.qrcode.core.a.h("连续对焦失败");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        if (c()) {
            this.f8554f.a(this.f8549a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean f() {
        return this.f8549a != null && this.f8550b && this.f8551c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(Rect rect) {
        if (this.f8549a == null || rect == null || rect.left <= 0 || rect.top <= 0) {
            return;
        }
        int centerX = rect.centerX();
        int centerY = rect.centerY();
        int width = rect.width() / 2;
        int height = rect.height() / 2;
        cn.bingoogolapple.qrcode.core.a.o("转换前", rect);
        if (cn.bingoogolapple.qrcode.core.a.m(getContext())) {
            centerY = centerX;
            centerX = centerY;
            height = width;
            width = height;
        }
        Rect rect2 = new Rect(centerX - width, centerY - height, centerX + width, centerY + height);
        cn.bingoogolapple.qrcode.core.a.o("转换后", rect2);
        cn.bingoogolapple.qrcode.core.a.e("扫码框发生变化触发对焦测光");
        d(rect2.centerX(), rect2.centerY(), rect2.width(), rect2.height());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h() {
        if (c()) {
            this.f8554f.i(this.f8549a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k() {
        Camera camera = this.f8549a;
        if (camera != null) {
            try {
                this.f8550b = false;
                camera.cancelAutoFocus();
                this.f8549a.setOneShotPreviewCallback(null);
                this.f8549a.stopPreview();
            } catch (Exception e8) {
                e8.printStackTrace();
            }
        }
    }

    @Override // android.view.SurfaceView, android.view.View
    public void onMeasure(int i8, int i9) {
        int defaultSize = SurfaceView.getDefaultSize(getSuggestedMinimumWidth(), i8);
        int defaultSize2 = SurfaceView.getDefaultSize(getSuggestedMinimumHeight(), i9);
        cn.bingoogolapple.qrcode.core.b bVar = this.f8554f;
        if (bVar != null && bVar.e() != null) {
            Point e8 = this.f8554f.e();
            float f5 = defaultSize;
            float f8 = defaultSize2;
            float f9 = e8.x;
            float f10 = e8.y;
            float f11 = (f9 * 1.0f) / f10;
            if ((f5 * 1.0f) / f8 < f11) {
                defaultSize = (int) ((f8 / ((f10 * 1.0f) / f9)) + 0.5f);
            } else {
                defaultSize2 = (int) ((f5 / f11) + 0.5f);
            }
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(defaultSize, 1073741824), View.MeasureSpec.makeMeasureSpec(defaultSize2, 1073741824));
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (f()) {
            if (motionEvent.getPointerCount() == 1 && (motionEvent.getAction() & 255) == 1) {
                if (this.f8552d) {
                    return true;
                }
                this.f8552d = true;
                cn.bingoogolapple.qrcode.core.a.e("手指触摸触发对焦测光");
                float x8 = motionEvent.getX();
                float y8 = motionEvent.getY();
                if (cn.bingoogolapple.qrcode.core.a.m(getContext())) {
                    y8 = x8;
                    x8 = y8;
                }
                int g8 = cn.bingoogolapple.qrcode.core.a.g(getContext(), 120.0f);
                d(x8, y8, g8, g8);
            }
            if (motionEvent.getPointerCount() == 2) {
                int action = motionEvent.getAction() & 255;
                if (action == 2) {
                    float b9 = cn.bingoogolapple.qrcode.core.a.b(motionEvent);
                    float f5 = this.f8553e;
                    if (b9 > f5) {
                        e(true, this.f8549a);
                    } else if (b9 < f5) {
                        e(false, this.f8549a);
                    }
                } else if (action == 5) {
                    this.f8553e = cn.bingoogolapple.qrcode.core.a.b(motionEvent);
                }
            }
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCamera(Camera camera) {
        this.f8549a = camera;
        if (camera != null) {
            cn.bingoogolapple.qrcode.core.b bVar = new cn.bingoogolapple.qrcode.core.b(getContext());
            this.f8554f = bVar;
            bVar.h(this.f8549a);
            if (this.f8550b) {
                requestLayout();
            } else {
                i();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDelegate(b bVar) {
        this.f8555g = bVar;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i8, int i9, int i10) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        k();
        i();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.f8551c = true;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.f8551c = false;
        k();
    }
}
