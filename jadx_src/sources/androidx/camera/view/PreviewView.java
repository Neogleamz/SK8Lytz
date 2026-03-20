package androidx.camera.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Rational;
import android.util.Size;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import androidx.camera.core.g3;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.p1;
import androidx.camera.core.t1;
import androidx.camera.core.y1;
import androidx.camera.core.z2;
import androidx.camera.view.PreviewView;
import androidx.camera.view.k;
import androidx.core.view.c0;
import androidx.lifecycle.LiveData;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class PreviewView extends FrameLayout {

    /* renamed from: w  reason: collision with root package name */
    private static final ImplementationMode f2950w = ImplementationMode.PERFORMANCE;

    /* renamed from: a  reason: collision with root package name */
    ImplementationMode f2951a;

    /* renamed from: b  reason: collision with root package name */
    k f2952b;

    /* renamed from: c  reason: collision with root package name */
    final f f2953c;

    /* renamed from: d  reason: collision with root package name */
    boolean f2954d;

    /* renamed from: e  reason: collision with root package name */
    final androidx.lifecycle.p<StreamState> f2955e;

    /* renamed from: f  reason: collision with root package name */
    final AtomicReference<androidx.camera.view.e> f2956f;

    /* renamed from: g  reason: collision with root package name */
    androidx.camera.view.a f2957g;

    /* renamed from: h  reason: collision with root package name */
    d f2958h;

    /* renamed from: j  reason: collision with root package name */
    Executor f2959j;

    /* renamed from: k  reason: collision with root package name */
    l f2960k;

    /* renamed from: l  reason: collision with root package name */
    private final ScaleGestureDetector f2961l;

    /* renamed from: m  reason: collision with root package name */
    y.q f2962m;

    /* renamed from: n  reason: collision with root package name */
    private MotionEvent f2963n;

    /* renamed from: p  reason: collision with root package name */
    private final c f2964p;
    private final View.OnLayoutChangeListener q;

    /* renamed from: t  reason: collision with root package name */
    final y1.d f2965t;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum ImplementationMode {
        PERFORMANCE(0),
        COMPATIBLE(1);
        

        /* renamed from: a  reason: collision with root package name */
        private final int f2969a;

        ImplementationMode(int i8) {
            this.f2969a = i8;
        }

        static ImplementationMode c(int i8) {
            ImplementationMode[] values;
            for (ImplementationMode implementationMode : values()) {
                if (implementationMode.f2969a == i8) {
                    return implementationMode;
                }
            }
            throw new IllegalArgumentException("Unknown implementation mode id " + i8);
        }

        int f() {
            return this.f2969a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum ScaleType {
        FILL_START(0),
        FILL_CENTER(1),
        FILL_END(2),
        FIT_START(3),
        FIT_CENTER(4),
        FIT_END(5);
        

        /* renamed from: a  reason: collision with root package name */
        private final int f2977a;

        ScaleType(int i8) {
            this.f2977a = i8;
        }

        static ScaleType c(int i8) {
            ScaleType[] values;
            for (ScaleType scaleType : values()) {
                if (scaleType.f2977a == i8) {
                    return scaleType;
                }
            }
            throw new IllegalArgumentException("Unknown scale type id " + i8);
        }

        int f() {
            return this.f2977a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum StreamState {
        IDLE,
        STREAMING
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements y1.d {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void e(z2 z2Var) {
            PreviewView.this.f2965t.a(z2Var);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void f(CameraInternal cameraInternal, z2 z2Var, z2.g gVar) {
            boolean z4;
            PreviewView previewView;
            k kVar;
            p1.a("PreviewView", "Preview transformation info updated. " + gVar);
            Integer a9 = cameraInternal.m().a();
            if (a9 == null) {
                p1.k("PreviewView", "The lens facing is null, probably an external.");
            } else if (a9.intValue() != 0) {
                z4 = false;
                PreviewView.this.f2953c.p(gVar, z2Var.l(), z4);
                if (gVar.c() != -1 || ((kVar = (previewView = PreviewView.this).f2952b) != null && (kVar instanceof q))) {
                    PreviewView.this.f2954d = true;
                } else {
                    previewView.f2954d = false;
                }
                PreviewView.this.i();
                PreviewView.this.e();
            }
            z4 = true;
            PreviewView.this.f2953c.p(gVar, z2Var.l(), z4);
            if (gVar.c() != -1) {
            }
            PreviewView.this.f2954d = true;
            PreviewView.this.i();
            PreviewView.this.e();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void g(androidx.camera.view.e eVar, CameraInternal cameraInternal) {
            if (PreviewView.this.f2956f.compareAndSet(eVar, null)) {
                eVar.l(StreamState.IDLE);
            }
            eVar.f();
            cameraInternal.g().b(eVar);
        }

        @Override // androidx.camera.core.y1.d
        public void a(final z2 z2Var) {
            k qVar;
            Executor executor;
            if (!androidx.camera.core.impl.utils.m.b()) {
                androidx.core.content.a.i(PreviewView.this.getContext()).execute(new Runnable() { // from class: androidx.camera.view.j
                    @Override // java.lang.Runnable
                    public final void run() {
                        PreviewView.a.this.e(z2Var);
                    }
                });
                return;
            }
            p1.a("PreviewView", "Surface requested by Preview.");
            final CameraInternal j8 = z2Var.j();
            PreviewView.this.f2962m = j8.m();
            z2Var.w(androidx.core.content.a.i(PreviewView.this.getContext()), new z2.h() { // from class: androidx.camera.view.h
                @Override // androidx.camera.core.z2.h
                public final void a(z2.g gVar) {
                    PreviewView.a.this.f(j8, z2Var, gVar);
                }
            });
            PreviewView previewView = PreviewView.this;
            if (PreviewView.f(z2Var, previewView.f2951a)) {
                PreviewView previewView2 = PreviewView.this;
                qVar = new y(previewView2, previewView2.f2953c);
            } else {
                PreviewView previewView3 = PreviewView.this;
                qVar = new q(previewView3, previewView3.f2953c);
            }
            previewView.f2952b = qVar;
            y.q m8 = j8.m();
            PreviewView previewView4 = PreviewView.this;
            final androidx.camera.view.e eVar = new androidx.camera.view.e(m8, previewView4.f2955e, previewView4.f2952b);
            PreviewView.this.f2956f.set(eVar);
            j8.g().a(androidx.core.content.a.i(PreviewView.this.getContext()), eVar);
            PreviewView.this.f2952b.g(z2Var, new k.a() { // from class: androidx.camera.view.i
                @Override // androidx.camera.view.k.a
                public final void a() {
                    PreviewView.a.this.g(eVar, j8);
                }
            });
            PreviewView previewView5 = PreviewView.this;
            d dVar = previewView5.f2958h;
            if (dVar == null || (executor = previewView5.f2959j) == null) {
                return;
            }
            previewView5.f2952b.i(executor, dVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class b {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f2982a;

        /* renamed from: b  reason: collision with root package name */
        static final /* synthetic */ int[] f2983b;

        static {
            int[] iArr = new int[ImplementationMode.values().length];
            f2983b = iArr;
            try {
                iArr[ImplementationMode.COMPATIBLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f2983b[ImplementationMode.PERFORMANCE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            int[] iArr2 = new int[ScaleType.values().length];
            f2982a = iArr2;
            try {
                iArr2[ScaleType.FILL_END.ordinal()] = 1;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f2982a[ScaleType.FILL_CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f2982a[ScaleType.FILL_START.ordinal()] = 3;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f2982a[ScaleType.FIT_END.ordinal()] = 4;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f2982a[ScaleType.FIT_CENTER.ordinal()] = 5;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f2982a[ScaleType.FIT_START.ordinal()] = 6;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements DisplayManager.DisplayListener {
        c() {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int i8) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int i8) {
            Display display = PreviewView.this.getDisplay();
            if (display == null || display.getDisplayId() != i8) {
                return;
            }
            PreviewView.this.i();
            PreviewView.this.e();
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int i8) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        void a(long j8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class e extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        e() {
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            androidx.camera.view.a aVar = PreviewView.this.f2957g;
            return true;
        }
    }

    public PreviewView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PreviewView(Context context, AttributeSet attributeSet, int i8) {
        this(context, attributeSet, i8, 0);
    }

    public PreviewView(Context context, AttributeSet attributeSet, int i8, int i9) {
        super(context, attributeSet, i8, i9);
        ImplementationMode implementationMode = f2950w;
        this.f2951a = implementationMode;
        f fVar = new f();
        this.f2953c = fVar;
        this.f2954d = true;
        this.f2955e = new androidx.lifecycle.p<>(StreamState.IDLE);
        this.f2956f = new AtomicReference<>();
        this.f2960k = new l(fVar);
        this.f2964p = new c();
        this.q = new View.OnLayoutChangeListener() { // from class: androidx.camera.view.g
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
                PreviewView.this.d(view, i10, i11, i12, i13, i14, i15, i16, i17);
            }
        };
        this.f2965t = new a();
        androidx.camera.core.impl.utils.m.a();
        Resources.Theme theme = context.getTheme();
        int[] iArr = m.f3027a;
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(attributeSet, iArr, i8, i9);
        c0.r0(this, context, iArr, attributeSet, obtainStyledAttributes, i8, i9);
        try {
            setScaleType(ScaleType.c(obtainStyledAttributes.getInteger(m.f3029c, fVar.f().f())));
            setImplementationMode(ImplementationMode.c(obtainStyledAttributes.getInteger(m.f3028b, implementationMode.f())));
            obtainStyledAttributes.recycle();
            this.f2961l = new ScaleGestureDetector(context, new e());
            if (getBackground() == null) {
                setBackgroundColor(androidx.core.content.a.d(getContext(), 17170444));
            }
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    private void b(boolean z4) {
        androidx.camera.core.impl.utils.m.a();
        getDisplay();
        getViewPort();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d(View view, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15) {
        if ((i10 - i8 == i14 - i12 && i11 - i9 == i15 - i13) ? false : true) {
            e();
            b(true);
        }
    }

    static boolean f(z2 z2Var, ImplementationMode implementationMode) {
        int i8;
        boolean equals = z2Var.j().m().f().equals("androidx.camera.camera2.legacy");
        boolean z4 = (h0.a.a(h0.d.class) == null && h0.a.a(h0.c.class) == null) ? false : true;
        if (z2Var.m() || Build.VERSION.SDK_INT <= 24 || equals || z4 || (i8 = b.f2983b[implementationMode.ordinal()]) == 1) {
            return true;
        }
        if (i8 == 2) {
            return false;
        }
        throw new IllegalArgumentException("Invalid implementation mode: " + implementationMode);
    }

    private void g() {
        DisplayManager displayManager = getDisplayManager();
        if (displayManager == null) {
            return;
        }
        displayManager.registerDisplayListener(this.f2964p, new Handler(Looper.getMainLooper()));
    }

    private DisplayManager getDisplayManager() {
        Context context = getContext();
        if (context == null) {
            return null;
        }
        return (DisplayManager) context.getApplicationContext().getSystemService("display");
    }

    private int getViewPortScaleType() {
        switch (b.f2982a[getScaleType().ordinal()]) {
            case 1:
                return 2;
            case 2:
                return 1;
            case 3:
                return 0;
            case 4:
            case 5:
            case 6:
                return 3;
            default:
                throw new IllegalStateException("Unexpected scale type: " + getScaleType());
        }
    }

    private void h() {
        DisplayManager displayManager = getDisplayManager();
        if (displayManager == null) {
            return;
        }
        displayManager.unregisterDisplayListener(this.f2964p);
    }

    @SuppressLint({"WrongConstant"})
    public g3 c(int i8) {
        androidx.camera.core.impl.utils.m.a();
        if (getWidth() == 0 || getHeight() == 0) {
            return null;
        }
        return new g3.a(new Rational(getWidth(), getHeight()), i8).c(getViewPortScaleType()).b(getLayoutDirection()).a();
    }

    void e() {
        androidx.camera.core.impl.utils.m.a();
        k kVar = this.f2952b;
        if (kVar != null) {
            kVar.h();
        }
        this.f2960k.a(new Size(getWidth(), getHeight()), getLayoutDirection());
    }

    public Bitmap getBitmap() {
        androidx.camera.core.impl.utils.m.a();
        k kVar = this.f2952b;
        if (kVar == null) {
            return null;
        }
        return kVar.a();
    }

    public androidx.camera.view.a getController() {
        androidx.camera.core.impl.utils.m.a();
        return this.f2957g;
    }

    public ImplementationMode getImplementationMode() {
        androidx.camera.core.impl.utils.m.a();
        return this.f2951a;
    }

    public t1 getMeteringPointFactory() {
        androidx.camera.core.impl.utils.m.a();
        return this.f2960k;
    }

    public i0.a getOutputTransform() {
        Matrix matrix;
        androidx.camera.core.impl.utils.m.a();
        try {
            matrix = this.f2953c.h(new Size(getWidth(), getHeight()), getLayoutDirection());
        } catch (IllegalStateException unused) {
            matrix = null;
        }
        Rect g8 = this.f2953c.g();
        if (matrix == null || g8 == null) {
            p1.a("PreviewView", "Transform info is not ready");
            return null;
        }
        matrix.preConcat(androidx.camera.core.impl.utils.n.a(g8));
        if (this.f2952b instanceof y) {
            matrix.postConcat(getMatrix());
        } else {
            p1.k("PreviewView", "PreviewView needs to be in COMPATIBLE mode for the transform to work correctly.");
        }
        return new i0.a(matrix, new Size(g8.width(), g8.height()));
    }

    public LiveData<StreamState> getPreviewStreamState() {
        return this.f2955e;
    }

    public ScaleType getScaleType() {
        androidx.camera.core.impl.utils.m.a();
        return this.f2953c.f();
    }

    public y1.d getSurfaceProvider() {
        androidx.camera.core.impl.utils.m.a();
        return this.f2965t;
    }

    public g3 getViewPort() {
        androidx.camera.core.impl.utils.m.a();
        if (getDisplay() == null) {
            return null;
        }
        return c(getDisplay().getRotation());
    }

    void i() {
        Display display;
        y.q qVar;
        if (!this.f2954d || (display = getDisplay()) == null || (qVar = this.f2962m) == null) {
            return;
        }
        this.f2953c.m(qVar.g(display.getRotation()), display.getRotation());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        i();
        g();
        addOnLayoutChangeListener(this.q);
        k kVar = this.f2952b;
        if (kVar != null) {
            kVar.d();
        }
        b(true);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeOnLayoutChangeListener(this.q);
        k kVar = this.f2952b;
        if (kVar != null) {
            kVar.e();
        }
        h();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean performClick() {
        this.f2963n = null;
        return super.performClick();
    }

    public void setController(androidx.camera.view.a aVar) {
        androidx.camera.core.impl.utils.m.a();
        b(false);
    }

    public void setImplementationMode(ImplementationMode implementationMode) {
        androidx.camera.core.impl.utils.m.a();
        this.f2951a = implementationMode;
        if (implementationMode == ImplementationMode.PERFORMANCE && this.f2958h != null) {
            throw new IllegalArgumentException("PERFORMANCE mode doesn't support frame update listener");
        }
    }

    public void setScaleType(ScaleType scaleType) {
        androidx.camera.core.impl.utils.m.a();
        this.f2953c.o(scaleType);
        e();
        b(false);
    }
}
