package cn.bingoogolapple.qrcode.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import cn.bingoogolapple.qrcode.core.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class QRCodeView extends RelativeLayout implements Camera.PreviewCallback {

    /* renamed from: t  reason: collision with root package name */
    private static final long[] f8520t = {255, 255, 255, 255};

    /* renamed from: a  reason: collision with root package name */
    protected Camera f8521a;

    /* renamed from: b  reason: collision with root package name */
    protected cn.bingoogolapple.qrcode.core.c f8522b;

    /* renamed from: c  reason: collision with root package name */
    protected h f8523c;

    /* renamed from: d  reason: collision with root package name */
    protected f f8524d;

    /* renamed from: e  reason: collision with root package name */
    protected boolean f8525e;

    /* renamed from: f  reason: collision with root package name */
    protected cn.bingoogolapple.qrcode.core.d f8526f;

    /* renamed from: g  reason: collision with root package name */
    protected int f8527g;

    /* renamed from: h  reason: collision with root package name */
    private PointF[] f8528h;

    /* renamed from: j  reason: collision with root package name */
    private Paint f8529j;

    /* renamed from: k  reason: collision with root package name */
    protected BarcodeType f8530k;

    /* renamed from: l  reason: collision with root package name */
    private long f8531l;

    /* renamed from: m  reason: collision with root package name */
    private ValueAnimator f8532m;

    /* renamed from: n  reason: collision with root package name */
    private long f8533n;

    /* renamed from: p  reason: collision with root package name */
    private long f8534p;
    private int q;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements c.b {
        a() {
        }

        @Override // cn.bingoogolapple.qrcode.core.c.b
        public void a() {
            QRCodeView.this.q();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            QRCodeView.this.f8522b.h();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f8537a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ int f8538b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ int f8539c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ String f8540d;

        c(int i8, int i9, int i10, String str) {
            this.f8537a = i8;
            this.f8538b = i9;
            this.f8539c = i10;
            this.f8540d = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            QRCodeView qRCodeView = QRCodeView.this;
            int i8 = this.f8537a;
            qRCodeView.t(i8, Math.min(this.f8538b + i8, this.f8539c), this.f8540d);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements ValueAnimator.AnimatorUpdateListener {
        d() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            cn.bingoogolapple.qrcode.core.c cVar = QRCodeView.this.f8522b;
            if (cVar == null || !cVar.f()) {
                return;
            }
            int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            Camera.Parameters parameters = QRCodeView.this.f8521a.getParameters();
            parameters.setZoom(intValue);
            QRCodeView.this.f8521a.setParameters(parameters);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ String f8543a;

        e(String str) {
            this.f8543a = str;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            QRCodeView.this.l(new i(this.f8543a));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f {
        void b();

        void c(String str);

        void d(boolean z4);
    }

    public QRCodeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public QRCodeView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f8525e = false;
        this.f8527g = 0;
        this.f8530k = BarcodeType.HIGH_FREQUENCY;
        this.f8531l = 0L;
        this.f8533n = 0L;
        this.f8534p = System.currentTimeMillis();
        this.q = 0;
        h(context, attributeSet);
        r();
    }

    private PointF C(float f5, float f8, float f9, float f10, boolean z4, int i8, Rect rect) {
        PointF pointF;
        int width = getWidth();
        int height = getHeight();
        if (cn.bingoogolapple.qrcode.core.a.m(getContext())) {
            float f11 = width;
            float f12 = height;
            pointF = new PointF((f10 - f5) * (f11 / f10), (f9 - f8) * (f12 / f9));
            float f13 = f12 - pointF.y;
            pointF.y = f13;
            pointF.x = f11 - pointF.x;
            if (rect == null) {
                pointF.y = f13 + i8;
            }
        } else {
            float f14 = width;
            pointF = new PointF(f5 * (f14 / f9), f8 * (height / f10));
            if (z4) {
                pointF.x = f14 - pointF.x;
            }
        }
        if (rect != null) {
            pointF.y += rect.top;
            pointF.x += rect.left;
        }
        return pointF;
    }

    private int d(int i8) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i9 = 0; i9 < Camera.getNumberOfCameras(); i9++) {
            try {
                Camera.getCameraInfo(i9, cameraInfo);
            } catch (Exception e8) {
                e8.printStackTrace();
            }
            if (cameraInfo.facing == i8) {
                return i9;
            }
        }
        return -1;
    }

    private void e(byte[] bArr, Camera camera) {
        cn.bingoogolapple.qrcode.core.c cVar = this.f8522b;
        if (cVar == null || !cVar.f()) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.f8534p < 150) {
            return;
        }
        this.f8534p = currentTimeMillis;
        long j8 = 0;
        long j9 = camera.getParameters().getPreviewSize().width * camera.getParameters().getPreviewSize().height;
        if (Math.abs(bArr.length - (((float) j9) * 1.5f)) < 1.0E-5f) {
            boolean z4 = false;
            for (int i8 = 0; i8 < j9; i8 += 10) {
                j8 += bArr[i8] & 255;
            }
            long j10 = j8 / (j9 / 10);
            long[] jArr = f8520t;
            int length = this.q % jArr.length;
            this.q = length;
            jArr[length] = j10;
            this.q = length + 1;
            int length2 = jArr.length;
            int i9 = 0;
            while (true) {
                if (i9 >= length2) {
                    z4 = true;
                    break;
                } else if (jArr[i9] > 60) {
                    break;
                } else {
                    i9++;
                }
            }
            cn.bingoogolapple.qrcode.core.a.e("摄像头环境亮度为：" + j10);
            f fVar = this.f8524d;
            if (fVar != null) {
                fVar.d(z4);
            }
        }
    }

    private boolean f(PointF[] pointFArr, String str) {
        if (this.f8521a == null || this.f8523c == null || pointFArr == null || pointFArr.length < 1) {
            return false;
        }
        ValueAnimator valueAnimator = this.f8532m;
        if ((valueAnimator == null || !valueAnimator.isRunning()) && System.currentTimeMillis() - this.f8533n >= 1200) {
            Camera.Parameters parameters = this.f8521a.getParameters();
            if (parameters.isZoomSupported()) {
                float f5 = pointFArr[0].x;
                float f8 = pointFArr[0].y;
                float f9 = pointFArr[1].x;
                float f10 = pointFArr[1].y;
                float abs = Math.abs(f5 - f9);
                float abs2 = Math.abs(f8 - f10);
                if (((int) Math.sqrt((abs * abs) + (abs2 * abs2))) > this.f8523c.getRectWidth() / 4) {
                    return false;
                }
                int maxZoom = parameters.getMaxZoom();
                post(new c(parameters.getZoom(), maxZoom / 4, maxZoom, str));
                return true;
            }
            return false;
        }
        return true;
    }

    private void h(Context context, AttributeSet attributeSet) {
        cn.bingoogolapple.qrcode.core.c cVar = new cn.bingoogolapple.qrcode.core.c(context);
        this.f8522b = cVar;
        cVar.setDelegate(new a());
        h hVar = new h(context);
        this.f8523c = hVar;
        hVar.i(this, attributeSet);
        this.f8522b.setId(cn.bingoogolapple.qrcode.core.e.f8564a);
        addView(this.f8522b);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(context, attributeSet);
        layoutParams.addRule(6, this.f8522b.getId());
        layoutParams.addRule(8, this.f8522b.getId());
        addView(this.f8523c, layoutParams);
        Paint paint = new Paint();
        this.f8529j = paint;
        paint.setColor(getScanBoxView().getCornerColor());
        this.f8529j.setStyle(Paint.Style.FILL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q() {
        if (this.f8525e && this.f8522b.f()) {
            try {
                this.f8521a.setOneShotPreviewCallback(this);
            } catch (Exception e8) {
                e8.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void t(int i8, int i9, String str) {
        ValueAnimator ofInt = ValueAnimator.ofInt(i8, i9);
        this.f8532m = ofInt;
        ofInt.addUpdateListener(new d());
        this.f8532m.addListener(new e(str));
        this.f8532m.setDuration(600L);
        this.f8532m.setRepeatCount(0);
        this.f8532m.start();
        this.f8533n = System.currentTimeMillis();
    }

    private void w(int i8) {
        try {
            this.f8527g = i8;
            Camera open = Camera.open(i8);
            this.f8521a = open;
            this.f8522b.setCamera(open);
        } catch (Exception e8) {
            e8.printStackTrace();
            f fVar = this.f8524d;
            if (fVar != null) {
                fVar.b();
            }
        }
    }

    public void A() {
        this.f8525e = false;
        cn.bingoogolapple.qrcode.core.d dVar = this.f8526f;
        if (dVar != null) {
            dVar.a();
            this.f8526f = null;
        }
        Camera camera = this.f8521a;
        if (camera != null) {
            try {
                camera.setOneShotPreviewCallback(null);
            } catch (Exception e8) {
                e8.printStackTrace();
            }
        }
    }

    public void B() {
        A();
        g();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean D(PointF[] pointFArr, Rect rect, boolean z4, String str) {
        if (pointFArr != null && pointFArr.length != 0) {
            try {
                Camera.Size previewSize = this.f8521a.getParameters().getPreviewSize();
                boolean z8 = this.f8527g == 1;
                int k8 = cn.bingoogolapple.qrcode.core.a.k(getContext());
                PointF[] pointFArr2 = new PointF[pointFArr.length];
                int i8 = 0;
                for (PointF pointF : pointFArr) {
                    pointFArr2[i8] = C(pointF.x, pointF.y, previewSize.width, previewSize.height, z8, k8, rect);
                    i8++;
                }
                this.f8528h = pointFArr2;
                postInvalidate();
                if (z4) {
                    return f(pointFArr2, str);
                }
                return false;
            } catch (Exception e8) {
                this.f8528h = null;
                e8.printStackTrace();
            }
        }
        return false;
    }

    public void c() {
        this.f8522b.b();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        PointF[] pointFArr;
        super.dispatchDraw(canvas);
        if (!j() || (pointFArr = this.f8528h) == null) {
            return;
        }
        for (PointF pointF : pointFArr) {
            canvas.drawCircle(pointF.x, pointF.y, 10.0f, this.f8529j);
        }
        this.f8528h = null;
        postInvalidateDelayed(2000L);
    }

    public void g() {
        h hVar = this.f8523c;
        if (hVar != null) {
            hVar.setVisibility(8);
        }
    }

    public cn.bingoogolapple.qrcode.core.c getCameraPreview() {
        return this.f8522b;
    }

    public boolean getIsScanBarcodeStyle() {
        return this.f8523c.getIsBarcode();
    }

    public h getScanBoxView() {
        return this.f8523c;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean i() {
        h hVar = this.f8523c;
        return hVar != null && hVar.k();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean j() {
        h hVar = this.f8523c;
        return hVar != null && hVar.m();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(i iVar) {
        f fVar = this.f8524d;
        if (fVar != null) {
            fVar.c(iVar == null ? null : iVar.f8631a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(i iVar) {
        if (this.f8525e) {
            String str = iVar == null ? null : iVar.f8631a;
            try {
                if (TextUtils.isEmpty(str)) {
                    Camera camera = this.f8521a;
                    if (camera != null) {
                        camera.setOneShotPreviewCallback(this);
                    }
                } else {
                    this.f8525e = false;
                    f fVar = this.f8524d;
                    if (fVar != null) {
                        fVar.c(str);
                    }
                }
            } catch (Exception e8) {
                e8.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(Rect rect) {
        this.f8522b.g(rect);
    }

    public void n() {
        postDelayed(new b(), this.f8522b.f() ? 0L : 500L);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract i o(Bitmap bitmap);

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ValueAnimator valueAnimator = this.f8532m;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] bArr, Camera camera) {
        if (cn.bingoogolapple.qrcode.core.a.l()) {
            cn.bingoogolapple.qrcode.core.a.e("两次 onPreviewFrame 时间间隔：" + (System.currentTimeMillis() - this.f8531l));
            this.f8531l = System.currentTimeMillis();
        }
        cn.bingoogolapple.qrcode.core.c cVar = this.f8522b;
        if (cVar != null && cVar.f()) {
            try {
                e(bArr, camera);
            } catch (Exception e8) {
                e8.printStackTrace();
            }
        }
        if (this.f8525e) {
            cn.bingoogolapple.qrcode.core.d dVar = this.f8526f;
            if (dVar == null || !(dVar.getStatus() == AsyncTask.Status.PENDING || this.f8526f.getStatus() == AsyncTask.Status.RUNNING)) {
                this.f8526f = new cn.bingoogolapple.qrcode.core.d(camera, bArr, this, cn.bingoogolapple.qrcode.core.a.m(getContext())).d();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract i p(byte[] bArr, int i8, int i9, boolean z4);

    protected abstract void r();

    public void s() {
        h hVar = this.f8523c;
        if (hVar != null) {
            hVar.setVisibility(0);
        }
    }

    public void setDelegate(f fVar) {
        this.f8524d = fVar;
    }

    public void u() {
        v(this.f8527g);
    }

    public void v(int i8) {
        if (this.f8521a != null || Camera.getNumberOfCameras() == 0) {
            return;
        }
        int d8 = d(i8);
        if (d8 != -1) {
            w(d8);
            return;
        }
        if (i8 == 0) {
            d8 = d(1);
        } else if (i8 == 1) {
            d8 = d(0);
        }
        if (d8 != -1) {
            w(d8);
        }
    }

    public void x() {
        this.f8525e = true;
        u();
        q();
    }

    public void y() {
        x();
        s();
    }

    public void z() {
        try {
            B();
            if (this.f8521a != null) {
                this.f8522b.k();
                this.f8522b.setCamera(null);
                this.f8521a.release();
                this.f8521a = null;
            }
        } catch (Exception e8) {
            e8.printStackTrace();
        }
    }
}
