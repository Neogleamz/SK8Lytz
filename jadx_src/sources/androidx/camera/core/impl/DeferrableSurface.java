package androidx.camera.core.impl;

import android.util.Log;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.p1;
import androidx.concurrent.futures.c;
import java.util.concurrent.atomic.AtomicInteger;
import y.x;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class DeferrableSurface {

    /* renamed from: i  reason: collision with root package name */
    public static final Size f2474i = new Size(0, 0);

    /* renamed from: j  reason: collision with root package name */
    private static final boolean f2475j = p1.f("DeferrableSurface");

    /* renamed from: k  reason: collision with root package name */
    private static final AtomicInteger f2476k = new AtomicInteger(0);

    /* renamed from: l  reason: collision with root package name */
    private static final AtomicInteger f2477l = new AtomicInteger(0);

    /* renamed from: a  reason: collision with root package name */
    private final Object f2478a;

    /* renamed from: b  reason: collision with root package name */
    private int f2479b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f2480c;

    /* renamed from: d  reason: collision with root package name */
    private c.a<Void> f2481d;

    /* renamed from: e  reason: collision with root package name */
    private final com.google.common.util.concurrent.d<Void> f2482e;

    /* renamed from: f  reason: collision with root package name */
    private final Size f2483f;

    /* renamed from: g  reason: collision with root package name */
    private final int f2484g;

    /* renamed from: h  reason: collision with root package name */
    Class<?> f2485h;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class SurfaceClosedException extends Exception {

        /* renamed from: a  reason: collision with root package name */
        DeferrableSurface f2486a;

        public SurfaceClosedException(String str, DeferrableSurface deferrableSurface) {
            super(str);
            this.f2486a = deferrableSurface;
        }

        public DeferrableSurface a() {
            return this.f2486a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class SurfaceUnavailableException extends Exception {
        public SurfaceUnavailableException(String str) {
            super(str);
        }
    }

    public DeferrableSurface() {
        this(f2474i, 0);
    }

    public DeferrableSurface(Size size, int i8) {
        this.f2478a = new Object();
        this.f2479b = 0;
        this.f2480c = false;
        this.f2483f = size;
        this.f2484g = i8;
        com.google.common.util.concurrent.d<Void> a9 = androidx.concurrent.futures.c.a(new y.w(this));
        this.f2482e = a9;
        if (p1.f("DeferrableSurface")) {
            m("Surface created", f2477l.incrementAndGet(), f2476k.get());
            a9.c(new x(this, Log.getStackTraceString(new Exception())), z.a.a());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object k(c.a aVar) {
        synchronized (this.f2478a) {
            this.f2481d = aVar;
        }
        return "DeferrableSurface-termination(" + this + ")";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void l(String str) {
        try {
            this.f2482e.get();
            m("Surface terminated", f2477l.decrementAndGet(), f2476k.get());
        } catch (Exception e8) {
            p1.c("DeferrableSurface", "Unexpected surface termination for " + this + "\nStack Trace:\n" + str);
            synchronized (this.f2478a) {
                throw new IllegalArgumentException(String.format("DeferrableSurface %s [closed: %b, use_count: %s] terminated with unexpected exception.", this, Boolean.valueOf(this.f2480c), Integer.valueOf(this.f2479b)), e8);
            }
        }
    }

    private void m(String str, int i8, int i9) {
        if (!f2475j && p1.f("DeferrableSurface")) {
            p1.a("DeferrableSurface", "DeferrableSurface usage statistics may be inaccurate since debug logging was not enabled at static initialization time. App restart may be required to enable accurate usage statistics.");
        }
        p1.a("DeferrableSurface", str + "[total_surfaces=" + i8 + ", used_surfaces=" + i9 + "](" + this + "}");
    }

    public void c() {
        c.a<Void> aVar;
        synchronized (this.f2478a) {
            if (this.f2480c) {
                aVar = null;
            } else {
                this.f2480c = true;
                if (this.f2479b == 0) {
                    aVar = this.f2481d;
                    this.f2481d = null;
                } else {
                    aVar = null;
                }
                if (p1.f("DeferrableSurface")) {
                    p1.a("DeferrableSurface", "surface closed,  useCount=" + this.f2479b + " closed=true " + this);
                }
            }
        }
        if (aVar != null) {
            aVar.c(null);
        }
    }

    public void d() {
        c.a<Void> aVar;
        synchronized (this.f2478a) {
            int i8 = this.f2479b;
            if (i8 == 0) {
                throw new IllegalStateException("Decrementing use count occurs more times than incrementing");
            }
            int i9 = i8 - 1;
            this.f2479b = i9;
            if (i9 == 0 && this.f2480c) {
                aVar = this.f2481d;
                this.f2481d = null;
            } else {
                aVar = null;
            }
            if (p1.f("DeferrableSurface")) {
                p1.a("DeferrableSurface", "use count-1,  useCount=" + this.f2479b + " closed=" + this.f2480c + " " + this);
                if (this.f2479b == 0) {
                    m("Surface no longer in use", f2477l.get(), f2476k.decrementAndGet());
                }
            }
        }
        if (aVar != null) {
            aVar.c(null);
        }
    }

    public Class<?> e() {
        return this.f2485h;
    }

    public Size f() {
        return this.f2483f;
    }

    public int g() {
        return this.f2484g;
    }

    public final com.google.common.util.concurrent.d<Surface> h() {
        synchronized (this.f2478a) {
            if (this.f2480c) {
                return a0.f.f(new SurfaceClosedException("DeferrableSurface already closed.", this));
            }
            return n();
        }
    }

    public com.google.common.util.concurrent.d<Void> i() {
        return a0.f.j(this.f2482e);
    }

    public void j() {
        synchronized (this.f2478a) {
            int i8 = this.f2479b;
            if (i8 == 0 && this.f2480c) {
                throw new SurfaceClosedException("Cannot begin use on a closed surface.", this);
            }
            this.f2479b = i8 + 1;
            if (p1.f("DeferrableSurface")) {
                if (this.f2479b == 1) {
                    m("New surface in use", f2477l.get(), f2476k.incrementAndGet());
                }
                p1.a("DeferrableSurface", "use count+1, useCount=" + this.f2479b + " " + this);
            }
        }
    }

    protected abstract com.google.common.util.concurrent.d<Surface> n();

    public void o(Class<?> cls) {
        this.f2485h = cls;
    }
}
