package c1;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.view.Surface;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b {

    /* renamed from: a  reason: collision with root package name */
    private EGLDisplay f8216a = EGL14.EGL_NO_DISPLAY;

    /* renamed from: b  reason: collision with root package name */
    private EGLContext f8217b = EGL14.EGL_NO_CONTEXT;

    /* renamed from: c  reason: collision with root package name */
    private EGLSurface f8218c = EGL14.EGL_NO_SURFACE;

    /* renamed from: d  reason: collision with root package name */
    private EGLConfig[] f8219d = new EGLConfig[1];

    /* renamed from: e  reason: collision with root package name */
    private Surface f8220e;

    /* renamed from: f  reason: collision with root package name */
    private int f8221f;

    /* renamed from: g  reason: collision with root package name */
    private int f8222g;

    public b(Surface surface) {
        Objects.requireNonNull(surface);
        this.f8220e = surface;
        c();
    }

    private void a(String str) {
        int eglGetError = EGL14.eglGetError();
        if (eglGetError == 12288) {
            return;
        }
        throw new RuntimeException(str + ": EGL error: 0x" + Integer.toHexString(eglGetError));
    }

    private void b() {
        this.f8218c = EGL14.eglCreateWindowSurface(this.f8216a, this.f8219d[0], this.f8220e, new int[]{12344}, 0);
        a("eglCreateWindowSurface");
        if (this.f8218c == null) {
            throw new RuntimeException("surface was null");
        }
    }

    private void c() {
        EGLDisplay eglGetDisplay = EGL14.eglGetDisplay(0);
        this.f8216a = eglGetDisplay;
        if (Objects.equals(eglGetDisplay, EGL14.EGL_NO_DISPLAY)) {
            throw new RuntimeException("unable to get EGL14 display");
        }
        int[] iArr = new int[2];
        if (!EGL14.eglInitialize(this.f8216a, iArr, 0, iArr, 1)) {
            this.f8216a = null;
            throw new RuntimeException("unable to initialize EGL14");
        }
        EGLDisplay eGLDisplay = this.f8216a;
        EGLConfig[] eGLConfigArr = this.f8219d;
        if (!EGL14.eglChooseConfig(eGLDisplay, new int[]{12324, 8, 12323, 8, 12322, 8, 12352, 4, 12610, 1, 12344}, 0, eGLConfigArr, 0, eGLConfigArr.length, new int[1], 0)) {
            throw new RuntimeException("unable to find RGB888+recordable ES2 EGL config");
        }
        this.f8217b = EGL14.eglCreateContext(this.f8216a, this.f8219d[0], EGL14.EGL_NO_CONTEXT, new int[]{12440, 2, 12344}, 0);
        a("eglCreateContext");
        if (this.f8217b == null) {
            throw new RuntimeException("null context");
        }
        b();
        this.f8221f = e();
        this.f8222g = d();
    }

    public int d() {
        int[] iArr = new int[1];
        EGL14.eglQuerySurface(this.f8216a, this.f8218c, 12374, iArr, 0);
        return iArr[0];
    }

    public int e() {
        int[] iArr = new int[1];
        EGL14.eglQuerySurface(this.f8216a, this.f8218c, 12375, iArr, 0);
        return iArr[0];
    }

    public void f() {
        EGLDisplay eGLDisplay = this.f8216a;
        EGLSurface eGLSurface = this.f8218c;
        if (!EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.f8217b)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public void g() {
        EGLDisplay eGLDisplay = this.f8216a;
        EGLSurface eGLSurface = EGL14.EGL_NO_SURFACE;
        if (!EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL14.EGL_NO_CONTEXT)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public void h() {
        if (!Objects.equals(this.f8216a, EGL14.EGL_NO_DISPLAY)) {
            EGL14.eglDestroySurface(this.f8216a, this.f8218c);
            EGL14.eglDestroyContext(this.f8216a, this.f8217b);
            EGL14.eglReleaseThread();
            EGL14.eglTerminate(this.f8216a);
        }
        this.f8220e.release();
        this.f8216a = EGL14.EGL_NO_DISPLAY;
        this.f8217b = EGL14.EGL_NO_CONTEXT;
        this.f8218c = EGL14.EGL_NO_SURFACE;
        this.f8220e = null;
    }

    public void i(long j8) {
        EGLExt.eglPresentationTimeANDROID(this.f8216a, this.f8218c, j8);
    }

    public boolean j() {
        return EGL14.eglSwapBuffers(this.f8216a, this.f8218c);
    }
}
