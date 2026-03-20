package androidx.camera.core;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.util.Rational;
import android.util.Size;
import androidx.camera.core.e1;
import androidx.camera.core.h0;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseConfigFactory;
import androidx.camera.core.impl.f;
import androidx.camera.core.impl.v;
import androidx.camera.core.internal.utils.ImageUtil;
import androidx.concurrent.futures.c;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import y.g0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e1 extends a3 {
    public static final g L = new g();
    static final e0.a M = new e0.a();
    SessionConfig.b A;
    m2 B;
    c2 C;
    private com.google.common.util.concurrent.d<Void> D;
    private y.h E;
    private DeferrableSurface F;
    private i G;
    final Executor H;
    private x.o I;
    private x.k0 J;
    private final x.n K;

    /* renamed from: m  reason: collision with root package name */
    boolean f2299m;

    /* renamed from: n  reason: collision with root package name */
    private final g0.a f2300n;

    /* renamed from: o  reason: collision with root package name */
    final Executor f2301o;

    /* renamed from: p  reason: collision with root package name */
    private final int f2302p;
    private final AtomicReference<Integer> q;

    /* renamed from: r  reason: collision with root package name */
    private final int f2303r;

    /* renamed from: s  reason: collision with root package name */
    private int f2304s;

    /* renamed from: t  reason: collision with root package name */
    private Rational f2305t;

    /* renamed from: u  reason: collision with root package name */
    private ExecutorService f2306u;

    /* renamed from: v  reason: collision with root package name */
    private androidx.camera.core.impl.f f2307v;

    /* renamed from: w  reason: collision with root package name */
    private y.u f2308w;

    /* renamed from: x  reason: collision with root package name */
    private int f2309x;

    /* renamed from: y  reason: collision with root package name */
    private y.v f2310y;

    /* renamed from: z  reason: collision with root package name */
    private boolean f2311z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends y.h {
        a() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends y.h {
        b() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements a0.c<Void> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ c.a f2314a;

        c(c.a aVar) {
            this.f2314a = aVar;
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Void r12) {
            e1.this.B0();
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            e1.this.B0();
            this.f2314a.f(th);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d implements ThreadFactory {

        /* renamed from: a  reason: collision with root package name */
        private final AtomicInteger f2316a = new AtomicInteger(0);

        d() {
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "CameraX-image_capture_" + this.f2316a.getAndIncrement());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class e implements x.n {
        e() {
        }

        @Override // x.n
        public com.google.common.util.concurrent.d<Void> a(List<androidx.camera.core.impl.f> list) {
            return e1.this.y0(list);
        }

        @Override // x.n
        public void b() {
            e1.this.w0();
        }

        @Override // x.n
        public void c() {
            e1.this.B0();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class f implements v.a<e1, androidx.camera.core.impl.j, f> {

        /* renamed from: a  reason: collision with root package name */
        private final androidx.camera.core.impl.n f2319a;

        public f() {
            this(androidx.camera.core.impl.n.P());
        }

        private f(androidx.camera.core.impl.n nVar) {
            this.f2319a = nVar;
            Class cls = (Class) nVar.f(b0.h.f7926x, null);
            if (cls == null || cls.equals(e1.class)) {
                h(e1.class);
                return;
            }
            throw new IllegalArgumentException("Invalid target class configuration for " + this + ": " + cls);
        }

        public static f d(Config config) {
            return new f(androidx.camera.core.impl.n.Q(config));
        }

        @Override // androidx.camera.core.g0
        public androidx.camera.core.impl.m a() {
            return this.f2319a;
        }

        public e1 c() {
            androidx.camera.core.impl.m a9;
            Config.a<Integer> aVar;
            int i8;
            Integer num;
            if (a().f(androidx.camera.core.impl.l.f2576g, null) == null || a().f(androidx.camera.core.impl.l.f2579j, null) == null) {
                Integer num2 = (Integer) a().f(androidx.camera.core.impl.j.F, null);
                if (num2 != null) {
                    androidx.core.util.h.b(a().f(androidx.camera.core.impl.j.E, null) == null, "Cannot set buffer format with CaptureProcessor defined.");
                    a().s(androidx.camera.core.impl.k.f2575f, num2);
                } else {
                    if (a().f(androidx.camera.core.impl.j.E, null) != null) {
                        a9 = a();
                        aVar = androidx.camera.core.impl.k.f2575f;
                        i8 = 35;
                    } else {
                        a9 = a();
                        aVar = androidx.camera.core.impl.k.f2575f;
                        i8 = RecognitionOptions.QR_CODE;
                    }
                    a9.s(aVar, Integer.valueOf(i8));
                }
                e1 e1Var = new e1(b());
                Size size = (Size) a().f(androidx.camera.core.impl.l.f2579j, null);
                if (size != null) {
                    e1Var.x0(new Rational(size.getWidth(), size.getHeight()));
                }
                Integer num3 = (Integer) a().f(androidx.camera.core.impl.j.G, 2);
                androidx.core.util.h.i(num3, "Maximum outstanding image count must be at least 1");
                androidx.core.util.h.b(num3.intValue() >= 1, "Maximum outstanding image count must be at least 1");
                androidx.core.util.h.i((Executor) a().f(b0.g.f7924v, z.a.c()), "The IO executor can't be null");
                androidx.camera.core.impl.m a10 = a();
                Config.a<Integer> aVar2 = androidx.camera.core.impl.j.C;
                if (!a10.b(aVar2) || ((num = (Integer) a().a(aVar2)) != null && (num.intValue() == 0 || num.intValue() == 1 || num.intValue() == 2))) {
                    return e1Var;
                }
                throw new IllegalArgumentException("The flash mode is not allowed to set: " + num);
            }
            throw new IllegalArgumentException("Cannot use both setTargetResolution and setTargetAspectRatio on the same config.");
        }

        @Override // androidx.camera.core.impl.v.a
        /* renamed from: e */
        public androidx.camera.core.impl.j b() {
            return new androidx.camera.core.impl.j(androidx.camera.core.impl.o.N(this.f2319a));
        }

        public f f(int i8) {
            a().s(androidx.camera.core.impl.v.f2662r, Integer.valueOf(i8));
            return this;
        }

        public f g(int i8) {
            a().s(androidx.camera.core.impl.l.f2576g, Integer.valueOf(i8));
            return this;
        }

        public f h(Class<e1> cls) {
            a().s(b0.h.f7926x, cls);
            if (a().f(b0.h.f7925w, null) == null) {
                i(cls.getCanonicalName() + "-" + UUID.randomUUID());
            }
            return this;
        }

        public f i(String str) {
            a().s(b0.h.f7925w, str);
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class g {

        /* renamed from: a  reason: collision with root package name */
        private static final androidx.camera.core.impl.j f2320a = new f().f(4).g(0).b();

        public androidx.camera.core.impl.j a() {
            return f2320a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h {

        /* renamed from: a  reason: collision with root package name */
        final int f2321a;

        /* renamed from: b  reason: collision with root package name */
        final int f2322b;

        /* renamed from: c  reason: collision with root package name */
        private final Rational f2323c;

        /* renamed from: d  reason: collision with root package name */
        private final Executor f2324d;

        /* renamed from: e  reason: collision with root package name */
        private final k f2325e;

        /* renamed from: f  reason: collision with root package name */
        AtomicBoolean f2326f;

        /* renamed from: g  reason: collision with root package name */
        private final Rect f2327g;

        /* renamed from: h  reason: collision with root package name */
        private final Matrix f2328h;

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void d(l1 l1Var) {
            this.f2325e.a(l1Var);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void e(int i8, String str, Throwable th) {
            this.f2325e.b(new ImageCaptureException(i8, str, th));
        }

        void c(l1 l1Var) {
            Size size;
            int n8;
            if (!this.f2326f.compareAndSet(false, true)) {
                l1Var.close();
                return;
            }
            if (e1.M.b(l1Var)) {
                try {
                    ByteBuffer b9 = l1Var.A()[0].b();
                    b9.rewind();
                    byte[] bArr = new byte[b9.capacity()];
                    b9.get(bArr);
                    androidx.camera.core.impl.utils.f h8 = androidx.camera.core.impl.utils.f.h(new ByteArrayInputStream(bArr));
                    b9.rewind();
                    size = new Size(h8.p(), h8.k());
                    n8 = h8.n();
                } catch (IOException e8) {
                    f(1, "Unable to parse JPEG exif", e8);
                    l1Var.close();
                    return;
                }
            } else {
                size = new Size(l1Var.getWidth(), l1Var.getHeight());
                n8 = this.f2321a;
            }
            final n2 n2Var = new n2(l1Var, size, o1.f(l1Var.e1().a(), l1Var.e1().d(), n8, this.f2328h));
            n2Var.Y0(e1.Z(this.f2327g, this.f2323c, this.f2321a, size, n8));
            try {
                this.f2324d.execute(new Runnable() { // from class: androidx.camera.core.g1
                    @Override // java.lang.Runnable
                    public final void run() {
                        e1.h.this.d(n2Var);
                    }
                });
            } catch (RejectedExecutionException unused) {
                p1.c("ImageCapture", "Unable to post to the supplied executor.");
                l1Var.close();
            }
        }

        void f(final int i8, final String str, final Throwable th) {
            if (this.f2326f.compareAndSet(false, true)) {
                try {
                    this.f2324d.execute(new Runnable() { // from class: androidx.camera.core.f1
                        @Override // java.lang.Runnable
                        public final void run() {
                            e1.h.this.e(i8, str, th);
                        }
                    });
                } catch (RejectedExecutionException unused) {
                    p1.c("ImageCapture", "Unable to post to the supplied executor.");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class i implements h0.a {

        /* renamed from: e  reason: collision with root package name */
        private final b f2333e;

        /* renamed from: f  reason: collision with root package name */
        private final int f2334f;

        /* renamed from: g  reason: collision with root package name */
        private final c f2335g;

        /* renamed from: a  reason: collision with root package name */
        private final Deque<h> f2329a = new ArrayDeque();

        /* renamed from: b  reason: collision with root package name */
        h f2330b = null;

        /* renamed from: c  reason: collision with root package name */
        com.google.common.util.concurrent.d<l1> f2331c = null;

        /* renamed from: d  reason: collision with root package name */
        int f2332d = 0;

        /* renamed from: h  reason: collision with root package name */
        final Object f2336h = new Object();

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a implements a0.c<l1> {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ h f2337a;

            a(h hVar) {
                this.f2337a = hVar;
            }

            @Override // a0.c
            /* renamed from: a */
            public void c(l1 l1Var) {
                synchronized (i.this.f2336h) {
                    androidx.core.util.h.h(l1Var);
                    p2 p2Var = new p2(l1Var);
                    p2Var.a(i.this);
                    i.this.f2332d++;
                    this.f2337a.c(p2Var);
                    i iVar = i.this;
                    iVar.f2330b = null;
                    iVar.f2331c = null;
                    iVar.c();
                }
            }

            @Override // a0.c
            public void onFailure(Throwable th) {
                synchronized (i.this.f2336h) {
                    if (!(th instanceof CancellationException)) {
                        this.f2337a.f(e1.g0(th), th != null ? th.getMessage() : "Unknown error", th);
                    }
                    i iVar = i.this;
                    iVar.f2330b = null;
                    iVar.f2331c = null;
                    iVar.c();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface b {
            com.google.common.util.concurrent.d<l1> a(h hVar);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface c {
            void a(h hVar);
        }

        i(int i8, b bVar, c cVar) {
            this.f2334f = i8;
            this.f2333e = bVar;
            this.f2335g = cVar;
        }

        public void a(Throwable th) {
            h hVar;
            com.google.common.util.concurrent.d<l1> dVar;
            ArrayList<h> arrayList;
            synchronized (this.f2336h) {
                hVar = this.f2330b;
                this.f2330b = null;
                dVar = this.f2331c;
                this.f2331c = null;
                arrayList = new ArrayList(this.f2329a);
                this.f2329a.clear();
            }
            if (hVar != null && dVar != null) {
                hVar.f(e1.g0(th), th.getMessage(), th);
                dVar.cancel(true);
            }
            for (h hVar2 : arrayList) {
                hVar2.f(e1.g0(th), th.getMessage(), th);
            }
        }

        @Override // androidx.camera.core.h0.a
        public void b(l1 l1Var) {
            synchronized (this.f2336h) {
                this.f2332d--;
                z.a.d().execute(new Runnable() { // from class: androidx.camera.core.h1
                    @Override // java.lang.Runnable
                    public final void run() {
                        e1.i.this.c();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void c() {
            synchronized (this.f2336h) {
                if (this.f2330b != null) {
                    return;
                }
                if (this.f2332d >= this.f2334f) {
                    p1.k("ImageCapture", "Too many acquire images. Close image to be able to process next.");
                    return;
                }
                h poll = this.f2329a.poll();
                if (poll == null) {
                    return;
                }
                this.f2330b = poll;
                c cVar = this.f2335g;
                if (cVar != null) {
                    cVar.a(poll);
                }
                com.google.common.util.concurrent.d<l1> a9 = this.f2333e.a(poll);
                this.f2331c = a9;
                a0.f.b(a9, new a(poll), z.a.d());
            }
        }

        public List<h> d() {
            ArrayList arrayList;
            com.google.common.util.concurrent.d<l1> dVar;
            synchronized (this.f2336h) {
                arrayList = new ArrayList(this.f2329a);
                this.f2329a.clear();
                h hVar = this.f2330b;
                this.f2330b = null;
                if (hVar != null && (dVar = this.f2331c) != null && dVar.cancel(true)) {
                    arrayList.add(0, hVar);
                }
            }
            return arrayList;
        }

        public void e(h hVar) {
            synchronized (this.f2336h) {
                this.f2329a.offer(hVar);
                Locale locale = Locale.US;
                Object[] objArr = new Object[2];
                objArr[0] = Integer.valueOf(this.f2330b != null ? 1 : 0);
                objArr[1] = Integer.valueOf(this.f2329a.size());
                p1.a("ImageCapture", String.format(locale, "Send image capture request [current, pending] = [%d, %d]", objArr));
                c();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class j {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class k {
        public abstract void a(l1 l1Var);

        public abstract void b(ImageCaptureException imageCaptureException);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface l {
        void a(n nVar);

        void b(ImageCaptureException imageCaptureException);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class m {
        public ContentResolver a() {
            throw null;
        }

        public ContentValues b() {
            throw null;
        }

        public File c() {
            throw null;
        }

        public j d() {
            throw null;
        }

        public OutputStream e() {
            throw null;
        }

        public Uri f() {
            throw null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class n {

        /* renamed from: a  reason: collision with root package name */
        private final Uri f2339a;

        public n(Uri uri) {
            this.f2339a = uri;
        }
    }

    e1(androidx.camera.core.impl.j jVar) {
        super(jVar);
        this.f2299m = false;
        this.f2300n = new g0.a() { // from class: androidx.camera.core.u0
            @Override // y.g0.a
            public final void a(y.g0 g0Var) {
                e1.r0(g0Var);
            }
        };
        this.q = new AtomicReference<>(null);
        this.f2304s = -1;
        this.f2305t = null;
        this.f2311z = false;
        this.D = a0.f.h(null);
        this.K = new e();
        androidx.camera.core.impl.j jVar2 = (androidx.camera.core.impl.j) g();
        this.f2302p = jVar2.b(androidx.camera.core.impl.j.B) ? jVar2.M() : 1;
        this.f2303r = jVar2.P(0);
        Executor executor = (Executor) androidx.core.util.h.h(jVar2.R(z.a.c()));
        this.f2301o = executor;
        this.H = z.a.f(executor);
    }

    private void A0() {
        synchronized (this.q) {
            if (this.q.get() != null) {
                return;
            }
            e().g(h0());
        }
    }

    private void W() {
        if (this.G != null) {
            this.G.a(new androidx.camera.core.n("Camera is closed."));
        }
    }

    private void Y() {
        Log.d("ImageCapture", "clearPipelineWithNode");
        androidx.camera.core.impl.utils.m.a();
        this.I.a();
        this.I = null;
        this.J.d();
        this.J = null;
    }

    static Rect Z(Rect rect, Rational rational, int i8, Size size, int i9) {
        if (rect != null) {
            return ImageUtil.b(rect, i8, size, i9);
        }
        if (rational != null) {
            if (i9 % 180 != 0) {
                rational = new Rational(rational.getDenominator(), rational.getNumerator());
            }
            if (ImageUtil.f(size, rational)) {
                Rect a9 = ImageUtil.a(size, rational);
                Objects.requireNonNull(a9);
                return a9;
            }
        }
        return new Rect(0, 0, size.getWidth(), size.getHeight());
    }

    private SessionConfig.b b0(final String str, androidx.camera.core.impl.j jVar, Size size) {
        androidx.camera.core.impl.utils.m.a();
        Log.d("ImageCapture", String.format("createPipelineWithNode(cameraId: %s, resolution: %s)", str, size));
        androidx.core.util.h.j(this.I == null);
        this.I = new x.o(jVar, size);
        androidx.core.util.h.j(this.J == null);
        this.J = new x.k0(this.K, this.I);
        SessionConfig.b f5 = this.I.f();
        if (Build.VERSION.SDK_INT >= 23 && e0() == 2) {
            e().a(f5);
        }
        f5.f(new SessionConfig.c() { // from class: androidx.camera.core.x0
            @Override // androidx.camera.core.impl.SessionConfig.c
            public final void a(SessionConfig sessionConfig, SessionConfig.SessionError sessionError) {
                e1.this.p0(str, sessionConfig, sessionError);
            }
        });
        return f5;
    }

    static boolean c0(androidx.camera.core.impl.m mVar) {
        Boolean bool = Boolean.TRUE;
        Config.a<Boolean> aVar = androidx.camera.core.impl.j.I;
        Boolean bool2 = Boolean.FALSE;
        boolean z4 = false;
        if (bool.equals(mVar.f(aVar, bool2))) {
            boolean z8 = true;
            int i8 = Build.VERSION.SDK_INT;
            if (i8 < 26) {
                p1.k("ImageCapture", "Software JPEG only supported on API 26+, but current API level is " + i8);
                z8 = false;
            }
            Integer num = (Integer) mVar.f(androidx.camera.core.impl.j.F, null);
            if (num == null || num.intValue() == 256) {
                z4 = z8;
            } else {
                p1.k("ImageCapture", "Software JPEG cannot be used with non-JPEG output buffer format.");
            }
            if (!z4) {
                p1.k("ImageCapture", "Unable to support software JPEG. Disabling.");
                mVar.s(aVar, bool2);
            }
        }
        return z4;
    }

    private y.u d0(y.u uVar) {
        List<androidx.camera.core.impl.g> a9 = this.f2308w.a();
        return (a9 == null || a9.isEmpty()) ? uVar : z.a(a9);
    }

    private int f0(androidx.camera.core.impl.j jVar) {
        List<androidx.camera.core.impl.g> a9;
        y.u L2 = jVar.L(null);
        if (L2 == null || (a9 = L2.a()) == null) {
            return 1;
        }
        return a9.size();
    }

    static int g0(Throwable th) {
        if (th instanceof androidx.camera.core.n) {
            return 3;
        }
        if (th instanceof ImageCaptureException) {
            return ((ImageCaptureException) th).a();
        }
        return 0;
    }

    private int i0() {
        androidx.camera.core.impl.j jVar = (androidx.camera.core.impl.j) g();
        if (jVar.b(androidx.camera.core.impl.j.K)) {
            return jVar.S();
        }
        int i8 = this.f2302p;
        if (i8 != 0) {
            if (i8 == 1 || i8 == 2) {
                return 95;
            }
            throw new IllegalStateException("CaptureMode " + this.f2302p + " is invalid");
        }
        return 100;
    }

    private static boolean j0(List<Pair<Integer, Size[]>> list, int i8) {
        if (list == null) {
            return false;
        }
        for (Pair<Integer, Size[]> pair : list) {
            if (((Integer) pair.first).equals(Integer.valueOf(i8))) {
                return true;
            }
        }
        return false;
    }

    private boolean k0() {
        androidx.camera.core.impl.utils.m.a();
        androidx.camera.core.impl.j jVar = (androidx.camera.core.impl.j) g();
        if (jVar.Q() == null && !l0() && this.f2310y == null && f0(jVar) <= 1) {
            Integer num = (Integer) jVar.f(androidx.camera.core.impl.k.f2575f, Integer.valueOf((int) RecognitionOptions.QR_CODE));
            Objects.requireNonNull(num);
            if (num.intValue() != 256) {
                return false;
            }
            return this.f2299m;
        }
        return false;
    }

    private boolean l0() {
        return (d() == null || d().i().y(null) == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void n0(b0.m mVar, h hVar) {
        if (Build.VERSION.SDK_INT >= 26) {
            mVar.g(hVar.f2322b);
            mVar.h(hVar.f2321a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void o0(String str, androidx.camera.core.impl.j jVar, Size size, SessionConfig sessionConfig, SessionConfig.SessionError sessionError) {
        i iVar = this.G;
        List<h> d8 = iVar != null ? iVar.d() : Collections.emptyList();
        X();
        if (r(str)) {
            this.A = a0(str, jVar, size);
            if (this.G != null) {
                for (h hVar : d8) {
                    this.G.e(hVar);
                }
            }
            K(this.A.m());
            v();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void p0(String str, SessionConfig sessionConfig, SessionConfig.SessionError sessionError) {
        if (!r(str)) {
            Y();
            return;
        }
        this.J.i();
        K(this.A.m());
        v();
        this.J.j();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void q0(h hVar, String str, Throwable th) {
        p1.c("ImageCapture", "Processing image failed! " + str);
        hVar.f(2, str, th);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void r0(y.g0 g0Var) {
        try {
            l1 acquireLatestImage = g0Var.acquireLatestImage();
            Log.d("ImageCapture", "Discarding ImageProxy which was inadvertently acquired: " + acquireLatestImage);
            if (acquireLatestImage != null) {
                acquireLatestImage.close();
            }
        } catch (IllegalStateException e8) {
            Log.e("ImageCapture", "Failed to acquire latest image.", e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Void s0(List list) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void t0(c.a aVar, y.g0 g0Var) {
        try {
            l1 acquireLatestImage = g0Var.acquireLatestImage();
            if (acquireLatestImage == null) {
                aVar.f(new IllegalStateException("Unable to acquire image"));
            } else if (!aVar.c(acquireLatestImage)) {
                acquireLatestImage.close();
            }
        } catch (IllegalStateException e8) {
            aVar.f(e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object v0(h hVar, final c.a aVar) {
        this.B.a(new g0.a() { // from class: androidx.camera.core.d1
            @Override // y.g0.a
            public final void a(y.g0 g0Var) {
                e1.t0(c.a.this, g0Var);
            }
        }, z.a.d());
        w0();
        final com.google.common.util.concurrent.d<Void> m02 = m0(hVar);
        a0.f.b(m02, new c(aVar), this.f2306u);
        aVar.a(new Runnable() { // from class: androidx.camera.core.a1
            @Override // java.lang.Runnable
            public final void run() {
                com.google.common.util.concurrent.d.this.cancel(true);
            }
        }, z.a.a());
        return "takePictureInternal";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.google.common.util.concurrent.d<l1> z0(final h hVar) {
        return androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.core.z0
            @Override // androidx.concurrent.futures.c.InterfaceC0024c
            public final Object a(c.a aVar) {
                Object v02;
                v02 = e1.this.v0(hVar, aVar);
                return v02;
            }
        });
    }

    @Override // androidx.camera.core.a3
    protected void A() {
        A0();
    }

    void B0() {
        synchronized (this.q) {
            Integer andSet = this.q.getAndSet(null);
            if (andSet == null) {
                return;
            }
            if (andSet.intValue() != h0()) {
                A0();
            }
        }
    }

    @Override // androidx.camera.core.a3
    public void C() {
        com.google.common.util.concurrent.d<Void> dVar = this.D;
        W();
        X();
        this.f2311z = false;
        final ExecutorService executorService = this.f2306u;
        Objects.requireNonNull(executorService);
        dVar.c(new Runnable() { // from class: androidx.camera.core.b1
            @Override // java.lang.Runnable
            public final void run() {
                executorService.shutdown();
            }
        }, z.a.a());
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x00cf, code lost:
        if (j0(r8, 35) != false) goto L32;
     */
    /* JADX WARN: Type inference failed for: r0v0, types: [androidx.camera.core.impl.q, androidx.camera.core.impl.v] */
    /* JADX WARN: Type inference failed for: r8v20, types: [androidx.camera.core.impl.v<?>, androidx.camera.core.impl.v] */
    @Override // androidx.camera.core.a3
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected androidx.camera.core.impl.v<?> D(y.q r8, androidx.camera.core.impl.v.a<?, ?, ?> r9) {
        /*
            r7 = this;
            androidx.camera.core.impl.v r0 = r9.b()
            androidx.camera.core.impl.Config$a<y.v> r1 = androidx.camera.core.impl.j.E
            r2 = 0
            java.lang.Object r0 = r0.f(r1, r2)
            java.lang.String r3 = "ImageCapture"
            if (r0 == 0) goto L26
            int r0 = android.os.Build.VERSION.SDK_INT
            r4 = 29
            if (r0 < r4) goto L26
            java.lang.String r8 = "Requesting software JPEG due to a CaptureProcessor is set."
            androidx.camera.core.p1.e(r3, r8)
            androidx.camera.core.impl.m r8 = r9.a()
            androidx.camera.core.impl.Config$a<java.lang.Boolean> r0 = androidx.camera.core.impl.j.I
            java.lang.Boolean r3 = java.lang.Boolean.TRUE
            r8.s(r0, r3)
            goto L58
        L26:
            y.t0 r8 = r8.j()
            java.lang.Class<d0.e> r0 = d0.e.class
            boolean r8 = r8.a(r0)
            if (r8 == 0) goto L58
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            androidx.camera.core.impl.m r0 = r9.a()
            androidx.camera.core.impl.Config$a<java.lang.Boolean> r4 = androidx.camera.core.impl.j.I
            java.lang.Boolean r5 = java.lang.Boolean.TRUE
            java.lang.Object r0 = r0.f(r4, r5)
            boolean r8 = r8.equals(r0)
            if (r8 == 0) goto L4c
            java.lang.String r8 = "Device quirk suggests software JPEG encoder, but it has been explicitly disabled."
            androidx.camera.core.p1.k(r3, r8)
            goto L58
        L4c:
            java.lang.String r8 = "Requesting software JPEG due to device quirk."
            androidx.camera.core.p1.e(r3, r8)
            androidx.camera.core.impl.m r8 = r9.a()
            r8.s(r4, r5)
        L58:
            androidx.camera.core.impl.m r8 = r9.a()
            boolean r8 = c0(r8)
            androidx.camera.core.impl.m r0 = r9.a()
            androidx.camera.core.impl.Config$a<java.lang.Integer> r3 = androidx.camera.core.impl.j.F
            java.lang.Object r0 = r0.f(r3, r2)
            java.lang.Integer r0 = (java.lang.Integer) r0
            r3 = 0
            r4 = 1
            r5 = 35
            if (r0 == 0) goto L99
            androidx.camera.core.impl.m r6 = r9.a()
            java.lang.Object r1 = r6.f(r1, r2)
            if (r1 != 0) goto L7e
            r1 = r4
            goto L7f
        L7e:
            r1 = r3
        L7f:
            java.lang.String r2 = "Cannot set buffer format with CaptureProcessor defined."
            androidx.core.util.h.b(r1, r2)
            androidx.camera.core.impl.m r1 = r9.a()
            androidx.camera.core.impl.Config$a<java.lang.Integer> r2 = androidx.camera.core.impl.k.f2575f
            if (r8 == 0) goto L8d
            goto L91
        L8d:
            int r5 = r0.intValue()
        L91:
            java.lang.Integer r8 = java.lang.Integer.valueOf(r5)
            r1.s(r2, r8)
            goto Lde
        L99:
            androidx.camera.core.impl.m r0 = r9.a()
            java.lang.Object r0 = r0.f(r1, r2)
            if (r0 != 0) goto Ld1
            if (r8 == 0) goto La6
            goto Ld1
        La6:
            androidx.camera.core.impl.m r8 = r9.a()
            androidx.camera.core.impl.Config$a<java.util.List<android.util.Pair<java.lang.Integer, android.util.Size[]>>> r0 = androidx.camera.core.impl.l.f2582m
            java.lang.Object r8 = r8.f(r0, r2)
            java.util.List r8 = (java.util.List) r8
            r0 = 256(0x100, float:3.59E-43)
            if (r8 != 0) goto Lc4
        Lb6:
            androidx.camera.core.impl.m r8 = r9.a()
            androidx.camera.core.impl.Config$a<java.lang.Integer> r1 = androidx.camera.core.impl.k.f2575f
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r8.s(r1, r0)
            goto Lde
        Lc4:
            boolean r1 = j0(r8, r0)
            if (r1 == 0) goto Lcb
            goto Lb6
        Lcb:
            boolean r8 = j0(r8, r5)
            if (r8 == 0) goto Lde
        Ld1:
            androidx.camera.core.impl.m r8 = r9.a()
            androidx.camera.core.impl.Config$a<java.lang.Integer> r0 = androidx.camera.core.impl.k.f2575f
            java.lang.Integer r1 = java.lang.Integer.valueOf(r5)
            r8.s(r0, r1)
        Lde:
            androidx.camera.core.impl.m r8 = r9.a()
            androidx.camera.core.impl.Config$a<java.lang.Integer> r0 = androidx.camera.core.impl.j.G
            r1 = 2
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            java.lang.Object r8 = r8.f(r0, r1)
            java.lang.Integer r8 = (java.lang.Integer) r8
            java.lang.String r0 = "Maximum outstanding image count must be at least 1"
            androidx.core.util.h.i(r8, r0)
            int r8 = r8.intValue()
            if (r8 < r4) goto Lfb
            r3 = r4
        Lfb:
            androidx.core.util.h.b(r3, r0)
            androidx.camera.core.impl.v r8 = r9.b()
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.core.e1.D(y.q, androidx.camera.core.impl.v$a):androidx.camera.core.impl.v");
    }

    @Override // androidx.camera.core.a3
    public void F() {
        W();
    }

    @Override // androidx.camera.core.a3
    protected Size G(Size size) {
        SessionConfig.b a02 = a0(f(), (androidx.camera.core.impl.j) g(), size);
        this.A = a02;
        K(a02.m());
        t();
        return size;
    }

    void X() {
        androidx.camera.core.impl.utils.m.a();
        if (k0()) {
            Y();
            return;
        }
        i iVar = this.G;
        if (iVar != null) {
            iVar.a(new CancellationException("Request is canceled."));
            this.G = null;
        }
        DeferrableSurface deferrableSurface = this.F;
        this.F = null;
        this.B = null;
        this.C = null;
        this.D = a0.f.h(null);
        if (deferrableSurface != null) {
            deferrableSurface.c();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x01bd  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01d2  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x021a  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x021f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    androidx.camera.core.impl.SessionConfig.b a0(final java.lang.String r15, final androidx.camera.core.impl.j r16, final android.util.Size r17) {
        /*
            Method dump skipped, instructions count: 591
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.core.e1.a0(java.lang.String, androidx.camera.core.impl.j, android.util.Size):androidx.camera.core.impl.SessionConfig$b");
    }

    public int e0() {
        return this.f2302p;
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [androidx.camera.core.impl.v<?>, androidx.camera.core.impl.v] */
    @Override // androidx.camera.core.a3
    public androidx.camera.core.impl.v<?> h(boolean z4, UseCaseConfigFactory useCaseConfigFactory) {
        Config a9 = useCaseConfigFactory.a(UseCaseConfigFactory.CaptureType.IMAGE_CAPTURE, e0());
        if (z4) {
            a9 = Config.A(a9, L.a());
        }
        if (a9 == null) {
            return null;
        }
        return p(a9).b();
    }

    public int h0() {
        int i8;
        synchronized (this.q) {
            i8 = this.f2304s;
            if (i8 == -1) {
                i8 = ((androidx.camera.core.impl.j) g()).O(2);
            }
        }
        return i8;
    }

    @Override // androidx.camera.core.a3
    protected j2 m() {
        CameraInternal d8 = d();
        Size c9 = c();
        if (d8 == null || c9 == null) {
            return null;
        }
        Rect q = q();
        Rational rational = this.f2305t;
        if (q == null) {
            q = rational != null ? ImageUtil.a(c9, rational) : new Rect(0, 0, c9.getWidth(), c9.getHeight());
        }
        int k8 = k(d8);
        Objects.requireNonNull(q);
        return j2.a(c9, q, k8);
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x00aa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    com.google.common.util.concurrent.d<java.lang.Void> m0(final androidx.camera.core.e1.h r8) {
        /*
            Method dump skipped, instructions count: 293
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.core.e1.m0(androidx.camera.core.e1$h):com.google.common.util.concurrent.d");
    }

    @Override // androidx.camera.core.a3
    public v.a<?, ?, ?> p(Config config) {
        return f.d(config);
    }

    public String toString() {
        return "ImageCapture:" + j();
    }

    void w0() {
        synchronized (this.q) {
            if (this.q.get() != null) {
                return;
            }
            this.q.set(Integer.valueOf(h0()));
        }
    }

    public void x0(Rational rational) {
        this.f2305t = rational;
    }

    com.google.common.util.concurrent.d<Void> y0(List<androidx.camera.core.impl.f> list) {
        androidx.camera.core.impl.utils.m.a();
        return a0.f.o(e().c(list, this.f2302p, this.f2303r), new n.a() { // from class: androidx.camera.core.c1
            @Override // n.a
            public final Object apply(Object obj) {
                Void s02;
                s02 = e1.s0((List) obj);
                return s02;
            }
        }, z.a.a());
    }

    @Override // androidx.camera.core.a3
    public void z() {
        androidx.camera.core.impl.j jVar = (androidx.camera.core.impl.j) g();
        this.f2307v = f.a.j(jVar).h();
        this.f2310y = jVar.N(null);
        this.f2309x = jVar.T(2);
        this.f2308w = jVar.L(z.c());
        this.f2311z = jVar.V();
        androidx.core.util.h.i(d(), "Attached camera cannot be null");
        this.f2306u = Executors.newFixedThreadPool(1, new d());
    }
}
